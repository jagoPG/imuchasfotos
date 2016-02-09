/*
 * Jagoba Pérez Copyright214
 * This program is distributed under the terms of the GNU General Public License
 * 
 * GUIAddImages.java
 * 
 * This file is part of Image Manager.
 * 
 * Image Manager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Image Manager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Image Manager.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jagobapg.imuchasfotos.gui;

import com.jagobapg.imuchasfotos.dto.Photo;
import com.jagobapg.imuchasfotos.sqlite.DBManipulation;
import com.jagobapg.imuchasfotos.sqlite.DBQueries;
import com.jagobapg.imuchasfotos.gui.utilities.ActionProgressListener;
import com.jagobapg.imuchasfotos.gui.utilities.FFPhoto;
import com.jagobapg.imuchasfotos.gui.utilities.OSOperations;

import java.io.File;
import java.util.ArrayList;

/**
 * JDialog divided into two parts, one visual and another one that will run the
 * main operation. The main aim of the class is to copy the image files to the
 * program's location. After that, the locations and images will be saved into
 * the database. The visual part of this class, will show the progress of both
 * operations.
 *
 * This class is called both from gui.AddFolder and
 * gui.imageManager.GUIImageManager (mniReloadImages).
 */
public class GUIAddImages extends javax.swing.JDialog {

    private static final long serialVersionUID = 1035631777456021628L;

    private String[] srcFolders = null;
    private String[] dstFolders = null;
    private ArrayList<String> alFolders = null; //List of folders where there are images to be inserted into the database.
    private FFPhoto photoFilter;

    /**
     * Copy data from one folder to another and save the images data to the
     * database.
     *
     * @param parent parent frame.
     * @param modal focus to this window.
     * @param srcFolders folders from data is going to be copied.
     * @param dstFolders folders where data is going to be copied.
     */
    public GUIAddImages(java.awt.Frame parent, boolean modal, final String[] srcFolders, final String[] dstFolders) {
        super(parent, modal);
        this.srcFolders = srcFolders;
        this.dstFolders = dstFolders;
        this.photoFilter = new FFPhoto();
        initComponents();

        /* Launch operations. */
        new Thread(new Runnable() {

            @Override
            public void run() {
                Thread th;
                ActionProgressListener apl = new ActionProgressListener() {

                    @Override
                    public void setNumberOperations(long operations) {
                        pbProgress.setMinimum(0);
                        pbProgress.setMaximum((int) operations);
                        pbProgress.setValue(0);
                    }

                    @Override
                    public void operationDone() {
                        pbProgress.setValue(pbProgress.getValue() + 1);

                        String progress = pbProgress.getValue() + " de " + pbProgress.getMaximum();
                        lblNumb.setText(progress);
                    }

                    @Override
                    public void setInformationText(String text) {
                        lblProcess.setText(text);
                        lblNumb.setText("");
                    }

                    @Override
                    public void finished(String txt) {
                        lblProcess.setText(txt);
                    }

                };

                /* Copy files. */
                th = new Thread(new RCopyFiles(apl));
                th.start();
                try {
                    th.join();
                } catch (InterruptedException ex) {
                }

                /* Insert files into database. */
                listFolders(); // Get all folders.
                th = new Thread(new RInsertDBFiles(apl));
                th.start();
                try {
                    th.join();
                } catch (InterruptedException ex) {
                }
            }

        }).start();
    }

    /**
     * Reload data stored in the program's folder to the database.
     *
     * @param parent parent frame.
     * @param modal focus to this window.
     */
    public GUIAddImages(java.awt.Frame parent, boolean modal) {
        super(parent, modal);

        Thread th;

        this.photoFilter = new FFPhoto();
        initComponents();
        ActionProgressListener apl = new ActionProgressListener() {

            @Override
            public void setNumberOperations(long operations) {
                pbProgress.setMinimum(0);
                pbProgress.setMaximum((int) operations);
                pbProgress.setValue(0);
            }

            @Override
            public void operationDone() {
                pbProgress.setValue(pbProgress.getValue() + 1);

                String progress = pbProgress.getValue() + " de " + pbProgress.getMaximum();
                lblNumb.setText(progress);
            }

            @Override
            public void setInformationText(String text) {
                lblProcess.setText(text);
                lblNumb.setText("");
            }

            @Override
            public void finished(String txt) {
                lblProcess.setText(txt);
            }

        };
        // Get all folders
        listFolders();

        // Insert files into database
        th = new Thread(new RInsertDBFiles(apl));
        th.start();
    }

    /* List all folders. */
    private void listFolders() {
        ArrayList<String> alChildren;

        // Folders from the database
        alFolders = DBQueries.INSTANCE.getFolderNames();

        alChildren = new ArrayList<String>();

        // Children folders
        for (String folder : alFolders) {
            childrenDirs(folder, alChildren);
        }

        for (String folder : alChildren) {
            alFolders.add(folder);
        }
    }

    /**
     * Children folders of a directory.
     *
     * @param folder current directory.
     * @param alFolders storage of all directories.
     */
    private static void childrenDirs(final String folder, final ArrayList<String> alFolders) {
        File[] aFiles = new File(folder).listFiles();

        if (aFiles != null) {
            for (File f : aFiles) {
                if (f.isDirectory()) {
                    String nDir = folder + f.getName() + File.separatorChar;
                    // Search more children folders
                    childrenDirs(nDir, alFolders);
                    // Add current level
                    alFolders.add(nDir);
                }
            }
        }
    }

    /* Files are going to be copied. */
    class RCopyFiles implements Runnable {

        //Progress of the actions.
        ActionProgressListener apl;

        /**
         * Create a new thread to copy files.
         *
         * @param apl Progress of the actions, updates JProgressBar.
         */
        public RCopyFiles(ActionProgressListener apl) {
            this.apl = apl;
            alFolders = new ArrayList<String>();
        }

        @Override
        public void run() {
            // Step 1: Calculate number of files
            apl.setInformationText("Calculando número de ficheros...");
            long numbFiles = calculateFiles(srcFolders);
            apl.setNumberOperations(numbFiles);

            // Step 2: Create new folders and begin the copying
            apl.setInformationText("Copiando ficheros...");
            apl.setNumberOperations(numbFiles);
            copyFiles();
        }

        /**
         * Calculates the number of files to be processed.
         *
         * @return the number of files that are going to be copied.
         */
        private long calculateFiles(final String[] folders) {
            long numbFiles = 0;

            for (String folder : folders) {
                numbFiles = numbFiles + countImagesInFolder(folder);
            }

            return numbFiles;
        }

        /**
         * Number of files in a folder and its children folder.
         *
         * @param folder Name of the folder.
         * @return number of folders.
         */
        private long countImagesInFolder(final String folder) {
            long images = 0;
            File f = new File(folder);

            // List files of a folder
            File[] listFiles = f.listFiles();

            for (File eachFile : listFiles) {
                if (eachFile.isDirectory()) {
                    // Get number of files of a children folder
                    images = images + countImagesInFolder(eachFile.getAbsolutePath());
                } else if (photoFilter.accept(eachFile)) {
                    // Is image
                    ++images;
                }
            }

            return images;
        }

        /* Copy files from the srcFolders to the dstFolders. */
        private void copyFiles() {
            for (int i = 0; i < srcFolders.length; i++) {
                copyFiles_processFolder(srcFolders[i], dstFolders[i]);
            }

            apl.finished("¡Hecho!");
        }

        /**
         * Copy all files from the srcFolder to the dstFolder, check children
         * folders and copy its images.
         *
         * @param orgFolder the next folder to be processes.
         * @param dstFolder the new folder created where files are going to be
         * created.
         */
        private void copyFiles_processFolder(final String orgFolder, final String dstFolder) {
            File old_folder = new File(orgFolder);

            // Current dir
            File new_folder = new File(dstFolder);
            new_folder.mkdir();
            alFolders.add(dstFolder);

            // List of files of the current dir
            File[] listFiles = old_folder.listFiles();

            for (File f : listFiles) {
                // Process next directory
                if (f.isDirectory()) {
                    // new directory
                    String children_dir = dstFolder + File.separatorChar + f.getName() + File.separatorChar;
                    copyFiles_processFolder(f.getAbsolutePath(), children_dir);
                } else // Copy file
                if (photoFilter.accept(f)) {
                    // File to be created
                    File new_file = new File(dstFolder + f.getName());
                    OSOperations.copyFile(f.getAbsolutePath(), new_file.getAbsolutePath());
                    // Update JProgressBar
                    apl.operationDone();
                }
            }
        }

    }

    /* Files are going to be inserted into the database. */
    class RInsertDBFiles implements Runnable {

        ActionProgressListener apl;

        public RInsertDBFiles(ActionProgressListener apl) {
            this.apl = apl;
        }

        @Override
        public void run() {
            // Step 1: calculate files
            apl.setInformationText("Calculando número de ficheros...");
            long numFiles = calculateImages(alFolders.toArray(new String[alFolders.size()]));
            apl.setNumberOperations(numFiles);

            // Step 2: process files
            apl.setInformationText("Actualizando información...");
            insertImagesDB();
            apl.finished("¡Hecho!");

            // Wait and close window
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
            dispose();
        }

        /**
         * Insert photos in the database.
         */
        private void insertImagesDB() {
            // Folders to process
            for (String folder : alFolders) {
                // Process all files
                File[] folder_files = new File(folder).listFiles();

                for (File file : folder_files) {
                    if (file.isFile() && photoFilter.accept(file)) {
                        Photo ph = new Photo();
                        // Fill the needed fields
                        preparePhoto(ph, folder, file);
                        if (!DBQueries.INSTANCE.existPhoto(ph.getFile())) {
                            // Insert into database
                            DBManipulation.INSTANCE.insertPhoto(ph);
                        }

                        // Update JProgressBar
                        apl.operationDone();
                    }
                }
            }
        }

        /**
         * Calculate the number of photos in a folder.
         *
         * @param folders folders to be checked.
         * @return number of photos.
         */
        private long calculateImages(final String[] folders) {
            int numImages = 0;

            for (String folder : folders) {
                // List files in the directory
                File[] files = new File(folder).listFiles();

                for (File f : files) {
                    // Count images
                    if (f.isFile() && photoFilter.accept(f)) {
                        ++numImages;
                    }
                }
            }

            return numImages;
        }

        /**
         * Prepare a photo to be inserted into the database.
         *
         * @param ph photo object
         * @param folder folder where is stored
         * @param photo_file physical file
         */
        private void preparePhoto(final Photo ph, final String folder, final File photo_file) {
            // 1. Set name of the photo
            ph.setName(photo_file.getName().substring(0, photo_file.getName().indexOf(".")));

            // 2. Set photo path
            ph.setFile(folder + photo_file.getName());

            // 3. Set folder id
            ph.setIdDir(DBQueries.INSTANCE.getSubfolderID(folder));
        }

    }

    private void initComponents() {
        lblProcess = new javax.swing.JLabel();
        pbProgress = new javax.swing.JProgressBar();
        lblNumb = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Procesamiento de fotos");
        setUndecorated(true);

        lblProcess.setText("Copiando archivos...");

        lblNumb.setText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblProcess)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblNumb))
                                .addComponent(pbProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblProcess)
                                .addComponent(lblNumb))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pbProgress, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                        .addContainerGap())
        );

        pack();
    }

    // Variables declaration
    private javax.swing.JLabel lblNumb;
    private javax.swing.JLabel lblProcess;
    private javax.swing.JProgressBar pbProgress;
    // End of variables declaration
}
