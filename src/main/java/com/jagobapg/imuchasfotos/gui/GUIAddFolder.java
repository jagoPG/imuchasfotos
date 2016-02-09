/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 *
 * GUIAddFolder.java
 *
 * Created on Jul 11, 2011, 9:33:19 PM
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

import com.jagobapg.imuchasfotos.dto.Folder;
import com.jagobapg.imuchasfotos.Main;
import com.jagobapg.imuchasfotos.sqlite.DBManipulation;
import com.jagobapg.imuchasfotos.sqlite.DBQueries;
import com.jagobapg.imuchasfotos.gui.utilities.OSOperations;

import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * JDialog which will allow the user to add the folders containing the images
 * that are wanted to be loaded into the database. Folders must be selected from
 * a JFileChooser and added or removed with the JButton components.
 *
 * Class called from both base.Main and gui.imageManager.GUIimageManager
 * (mniLoadFolders).
 */
public class GUIAddFolder extends javax.swing.JDialog {

    private static final long serialVersionUID = 870619112264809776L;

    /**
     * Creates new form AddFolder
     */
    public GUIAddFolder(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        DefaultListModel<String> dlm;
        initComponents();
        dlm = new DefaultListModel<String>();
        this.lstFolders.setModel(dlm);

        new Thread(new ThFolderCheck()).start(); // Run the loop of folder checking.
    }

    private void initComponents() {
        DefaultListModel<String> dlm;

        pnl = new javax.swing.JPanel();
        lblText = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnConfirm = new javax.swing.JButton();
        jstFolders = new javax.swing.JScrollPane();
        lstFolders = new javax.swing.JList<String>();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Añadir Carpetas");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnl.setLayout(new java.awt.BorderLayout());

        lblText.setText("Seleccione el directorio donde tenga las imágenes a procesar.");

        txtBusqueda.setEditable(false);

        btnSearch.setText("Explorar");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnConfirm.setText("Aceptar");
        btnConfirm.setEnabled(false);
        btnConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        dlm = new DefaultListModel<String>();
        lstFolders.setModel(dlm);
        dlm.addElement("<Listado de carpetas>");

        jstFolders.setViewportView(lstFolders);

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("images/add.png"))); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAniadirActionPerformed(evt);
            }
        });

        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("images/remove.png"))); // NOI18N
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarActionPerformed(evt);
            }
        });

        btnClose.setText("Cerrar");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addComponent(jstFolders, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                                                                .addComponent(txtBusqueda, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(btnSearch)
                                                                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(lblText)
                                                                .addGroup(layout.createSequentialGroup()
                                                                        .addComponent(btnConfirm)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(btnClose)))
                                                        .addGap(0, 0, Short.MAX_VALUE))))
                                .addComponent(pnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(pnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnSearch))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(jstFolders, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(btnConfirm)
                                                .addComponent(btnClose)))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnAdd)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnRemove)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    /* Seek the folder. */
    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {
        seekFolder();
    }

    /*
     * Add folder to the list
     */
    private void btnAniadirActionPerformed(java.awt.event.ActionEvent evt) {
        String dir = txtBusqueda.getText();

        if (dir.equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(GUIAddFolder.this, "Debe especificar un directorio.", "Inserte un directorio", JOptionPane.ERROR_MESSAGE);
            if (seekFolder()) {
                dir = txtBusqueda.getText();
                addItem_dirList(dir);
            }
        } else {
            addItem_dirList(dir);
        }
    }

    /**
     * Adds a new folder to the list
     *
     * @param dir
     */
    private void addItem_dirList(String dir) {
        if (!dir.equalsIgnoreCase("")) {
            //Checks if the folder still is in the list
            DefaultListModel<String> dlmFolders = (DefaultListModel<String>) lstFolders.getModel();

            int i;
            for (i = 0; i < dlmFolders.getSize() && !dir.equalsIgnoreCase(String.valueOf(dlmFolders.get(i))); i++);

            //If it is not in the list, we add it
            if (dlmFolders.getSize() == i && !dir.equalsIgnoreCase("")) {
                dlmFolders.addElement(txtBusqueda.getText());
                lstFolders.setModel(dlmFolders);
                update(); //Update controls.
            } else if (dlmFolders.getSize() != i && !dir.equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(GUIAddFolder.this, "Error. El directorio que ha señalado, ya existe.", "Directorio existente", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /* Remove selected folder from the list. */
    private void btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {
        int selection = lstFolders.getSelectedIndex();
        DefaultListModel<String> dlmFolders = (DefaultListModel<String>) lstFolders.getModel();

        if (selection == -1) {
            JOptionPane.showMessageDialog(GUIAddFolder.this, "Error. No ha seleccionado ningún directorio.", "Directorio no seleccionado", JOptionPane.ERROR_MESSAGE);
        } else { //Delete dir.
            dlmFolders.remove(selection);
        }
    }

    /* Save data in the database. */
    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {
        Folder[] dbFolders;
        DefaultListModel<String> dlm;
        int index;
        File dbFolder;
        final String folders[];
        final String nFolders[];

        // Delete folders that has been removed from the list
        dbFolders = DBQueries.INSTANCE.getFolders();
        dlm = (DefaultListModel<String>) lstFolders.getModel();
        for (Folder f : dbFolders) {
            dbFolder = new File(f.getName());
            for (index = 0; index < dlm.size() && !dbFolder.getAbsolutePath().equals(dlm.get(index)); index++);

            if (index == dlm.size()) {
                File folder = new File(f.getName());
                DBManipulation.INSTANCE.deletePhotosFromDir(f.getId());
                DBManipulation.INSTANCE.deleteFolder(f.getId());
                OSOperations.removeDirectory(folder.getAbsolutePath());
            }
        }

        // Create new folders in the program's folder
        folders = new String[dlm.size()];
        for (int i = 0; i < dlm.size(); i++) {
            folders[i] = dlm.get(i);
        }

        // Convert folders, follow the same order as the folders array
        nFolders = new String[folders.length];
        for (int i = 0; i < folders.length; i++) {
            File dir = new File(folders[i]);
            nFolders[i] = Main.IMAGES_FOLDER + dir.getName() + File.separatorChar;
        }

        // Insert new folders into the database.
        DBManipulation.INSTANCE.insertFolders(nFolders);

        // copy files
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                GUIAddImages gui = new GUIAddImages(null, true, folders, nFolders);
                gui.setVisible(true);

                dispose();
            }

        });
    }

    /* Close window. */
    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    /* Close window. */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        dispose();
    }

    /*
     * Enable/disable the accept button
     * Enable if we have photos in the database
     */
    private void update() {
        DefaultListModel<String> dlmFolders = (DefaultListModel<String>) lstFolders.getModel();
        this.lstFolders.repaint();
        btnConfirm.setEnabled(dlmFolders.getSize() != 0); //Enough folders to run the program.
    }

    /**
     * Launchs the JFileChooser, the user have to choose the image's folder
     *
     * @return false if does not choose any folder
     */
    private boolean seekFolder() {
        JFileChooser jfc = new JFileChooser();
        jfc.setMultiSelectionEnabled(false);
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int option = jfc.showOpenDialog(GUIAddFolder.this);
        switch (option) {
            case JFileChooser.APPROVE_OPTION:
                txtBusqueda.setText(String.valueOf(jfc.getSelectedFile()));
                return true;
            case JFileChooser.CANCEL_OPTION:
                return false;
            default:
                return false;
        }
    }

    /* Thread detects if new folders were added by hand. */
    class ThFolderCheck implements Runnable {

        public void run() {
            DefaultListModel<String> dlm;
            String dir;
            File f = new File(Main.IMAGES_FOLDER);

            // List all files in the folder.
            File[] files = f.listFiles();

            for (File eachFile : files) {
                // Check each file
                if (eachFile.isDirectory()) {
                    // Check if the folder is in the list, if not is added
                    dlm = (DefaultListModel<String>) lstFolders
                            .getModel();
                    dir = eachFile.getAbsolutePath();

                    int i;
                    for (i = 0; i < dlm.size() && !dir.equals(dlm.get(i)); i++) ;

                    if (i == dlm.size()) { // Add item.
                        dlm.addElement(dir);
                        update();
                    }
                }
            }
        }
    }

    //Variables declaration
    private javax.swing.JButton btnConfirm;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnRemove;
    private javax.swing.JScrollPane jstFolders;
    private javax.swing.JLabel lblText;
    private javax.swing.JList<String> lstFolders;
    private javax.swing.JPanel pnl;
    private javax.swing.JTextField txtBusqueda;
    //End of variables declaration
}
