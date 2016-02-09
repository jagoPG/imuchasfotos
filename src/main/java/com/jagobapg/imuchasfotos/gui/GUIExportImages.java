/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * GUIExportImages.java
 *
 * 
 * This file is part of iMuchasFotos.
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

import com.jagobapg.imuchasfotos.gui.utilities.OSOperations;
import com.jagobapg.imuchasfotos.gui.utilities.RCompressFiles;
import com.jagobapg.imuchasfotos.gui.utilities.RConvertQuality;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import com.jagobapg.imuchasfotos.dto.Photo;

/**
 * This class is a GUI for exporting images in JPG or ZIP format to a desired
 * folder.
 */
public class GUIExportImages extends JDialog {

    private static final long serialVersionUID = -4180016407061355733L;

    private static final String TEMP_FOLDER = "temp";
    private static final String JPG_FILE = "Archivos JPG";
    private static final String ZIP_FILE = "Archivo ZIP";
    private static final String LBL_FILE_INFO = "Tamaño total de los archivos adjuntos: ";
    private Photo photo;

    public GUIExportImages(Frame parent, Photo p) {
        super(parent, true);
        this.photo = p;
        initComponents();
        updateComponents();
        calculateSize();
    }

    /* Load design. */
    private void initComponents() {
        ButtonGroup bg;
        Image i;

        // Initialise variables
        this.pnlPhotoSelection = new JPanel();
        this.rbtSelectedPhoto = new JRadioButton("Enviar foto seleccionada.");
        this.rbtListPhotos = new JRadioButton("Enviar fotos de la lista.");
        this.pnlList = new JPanel();
        this.spPhotoList = new JScrollPane();
        this.lstPhotos = new JList<Photo>(new DefaultListModel<Photo>());
        this.pnlListButtons = new JPanel();
        this.btnAdd = new JButton("");
        this.btnRemove = new JButton("");
        this.btnClear = new JButton("Vaciar");
        this.pnlQuality = new JPanel();
        this.rbtLowQ = new JRadioButton("Baja (icono)");
        this.rbtMediumQ = new JRadioButton("Media (vista previa)");
        this.rbtHighQ = new JRadioButton("Alta (JPEG)");
        this.pnlFormat = new JPanel();
        this.cbxFormat = new JComboBox<String>(new DefaultComboBoxModel<String>());
        this.pnlFiles = new JPanel();
        this.lblFileSize = new JLabel(LBL_FILE_INFO);
        this.pnlButtons = new JPanel();
        this.btnExport = new JButton("Exportar...");
        this.btnClose = new JButton("Cerrar");
        // End initialise variable

        this.setTitle("Exportar fotos");
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.add(this.pnlPhotoSelection);

        // Photo selection panel
        this.pnlPhotoSelection.setLayout(new BoxLayout(pnlPhotoSelection,
                BoxLayout.Y_AXIS));
        bg = new ButtonGroup();
        bg.add(rbtSelectedPhoto);
        bg.add(rbtListPhotos);

        this.rbtSelectedPhoto.setAlignmentX(LEFT_ALIGNMENT);
        this.pnlPhotoSelection.add(rbtSelectedPhoto);
        this.rbtListPhotos.setAlignmentX(LEFT_ALIGNMENT);
        this.pnlPhotoSelection.add(rbtListPhotos);

        this.rbtSelectedPhoto.setSelected(true);

        // Photo list
        this.add(this.pnlList);
        this.pnlList.setLayout(new BoxLayout(pnlList, BoxLayout.X_AXIS));
        this.pnlList.setAlignmentX(LEFT_ALIGNMENT);
        this.pnlList.setBorder(BorderFactory
                .createTitledBorder("Lista de fotos"));

        this.spPhotoList.setViewportView(lstPhotos);
        this.spPhotoList.setAlignmentY(TOP_ALIGNMENT);
        this.pnlList.add(spPhotoList);

        // Photo button list
        this.pnlListButtons.setAlignmentY(TOP_ALIGNMENT);
        this.pnlListButtons.setLayout(new BoxLayout(pnlListButtons,
                BoxLayout.Y_AXIS));
        this.pnlListButtons.add(btnAdd);
        i = new ImageIcon(getClass().getClassLoader().getResource("images/add.png"))
                .getImage();
        i = i.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        this.btnAdd.setIcon(new ImageIcon(i));
        this.btnAdd.setAlignmentX(CENTER_ALIGNMENT);

        this.pnlListButtons.add(btnRemove);
        i = new ImageIcon(getClass().getClassLoader().getResource("images/remove.png"))
                .getImage();
        i = i.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        this.btnRemove.setIcon(new ImageIcon(i));
        this.btnRemove.setAlignmentX(CENTER_ALIGNMENT);

        this.pnlListButtons.add(Box.createVerticalGlue());
        this.pnlListButtons.add(btnClear);
        this.btnClear.setAlignmentX(CENTER_ALIGNMENT);
        this.pnlList.add(pnlListButtons);

        // Quality
        this.add(pnlQuality);
        bg = new ButtonGroup();
        bg.add(rbtLowQ);
        bg.add(rbtMediumQ);
        bg.add(rbtHighQ);
        this.pnlQuality.setBorder(BorderFactory.createTitledBorder("Calidad"));
        this.pnlQuality.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.pnlQuality.setAlignmentX(LEFT_ALIGNMENT);
        this.pnlQuality.add(rbtLowQ);
        this.pnlQuality.add(rbtMediumQ);
        this.pnlQuality.add(rbtHighQ);

        this.rbtMediumQ.setSelected(true);
        // Format
        this.add(pnlFormat);
        this.pnlFormat.setAlignmentX(LEFT_ALIGNMENT);
        this.pnlFormat.setBorder(BorderFactory.createTitledBorder("Formato"));
        this.pnlFormat.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.pnlFormat.add(cbxFormat);
        this.cbxFormat.addItem(JPG_FILE);
        this.cbxFormat.addItem(ZIP_FILE);
        this.cbxFormat.setPreferredSize(new Dimension(310, 25));

        // File size
        this.add(pnlFiles);
        this.pnlFiles.setBorder(BorderFactory
                .createTitledBorder("Información del tamaño"));
        this.pnlFiles.add(lblFileSize);
        this.pnlFiles.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.pnlFiles.setAlignmentX(LEFT_ALIGNMENT);

        // Buttons
        this.add(pnlButtons);
        this.pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.pnlButtons.setAlignmentX(LEFT_ALIGNMENT);
        this.pnlButtons.add(btnExport);
        this.pnlButtons.add(btnClose);

        this.setResizable(false);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.pack();

        // Action listener
        this.rbtSelectedPhoto.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                rbtSelectedPhotoActionPerformed(e);
            }

        });

        this.rbtListPhotos.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                rbtListPhotosActionPerformed(e);
            }

        });

        this.btnAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnAddActionPerformed(e);
            }

        });

        this.btnRemove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnRemoveActionPerformed(e);
            }

        });

        this.btnClear.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnClearActionPerformed(e);
            }

        });

        this.cbxFormat.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cbxFormatActionPerformed(e);
            }

        });

        this.rbtLowQ.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                rbtLowQActionPerformed(e);
            }

        });

        this.rbtMediumQ.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                rbtMediumQActionPerformed(e);
            }

        });

        this.rbtHighQ.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                rbtHighQActionPerformed(e);
            }

        });

        this.btnExport.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnSendActionPerformed(e);
            }

        });

        this.btnClose.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnCloseActionPerformed(e);
            }

        });
        // End of action listener
    }

    /* Update components behaviour. */
    private void updateComponents() {
        DefaultListModel<Photo> dlm = (DefaultListModel<Photo>) lstPhotos.getModel();
        this.lstPhotos.setEnabled(rbtListPhotos.isSelected());
        this.btnAdd.setEnabled(rbtListPhotos.isSelected());
        this.btnRemove.setEnabled(rbtListPhotos.isSelected() && !dlm.isEmpty());
        this.btnClear.setEnabled(rbtListPhotos.isSelected() && !dlm.isEmpty());
    }

    /* Calculate size of the file/s to send. */
    private void calculateSize() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                File tempDir = new File(TEMP_FOLDER);
                Photo[] aPhotos = null;
                RConvertQuality qConversor;
                Thread th;
                File[] aFiles;

                if (tempDir.exists()) {
                    tempDir.delete();
                }
                tempDir.mkdirs();

                qConversor = null;

                /* One or more images. */
                if (rbtSelectedPhoto.isSelected()) {
                    aPhotos = new Photo[1];
                    aPhotos[0] = photo;
                } else if (rbtListPhotos.isSelected()) {
                    DefaultListModel<Photo> dlm = (DefaultListModel<Photo>) lstPhotos.getModel();
                    aPhotos = new Photo[dlm.size()];

                    for (int i = 0; i < dlm.size(); i++) {
                        Photo p = dlm.get(i);
                        aPhotos[i] = p;
                    }
                }

                /* Image quality. */
                if (rbtMediumQ.isSelected()) {
                    qConversor = new RConvertQuality(aPhotos, RConvertQuality.EQuality.MEDIUM);
                } else if (rbtHighQ.isSelected()) {
                    qConversor = new RConvertQuality(aPhotos, RConvertQuality.EQuality.HIGH);
                } else if (rbtLowQ.isSelected()) {
                    qConversor = new RConvertQuality(aPhotos, RConvertQuality.EQuality.LOW);
                }

                th = new Thread(qConversor);
                th.start();
                try {
                    th.join();
                } catch (InterruptedException e) {
                }

                aFiles = qConversor.getConvertedPhotos(); //Converted images.

                /* Compress images if necessary and show size information. */
                if (cbxFormat.getSelectedItem().equals(ZIP_FILE)) {
                    RCompressFiles rCompressed = new RCompressFiles("fotos.zip", aFiles);
                    th = new Thread(rCompressed);
                    th.start();
                    try {
                        th.join();
                    } catch (InterruptedException e) {
                    }

                    File compressedFile = new File(TEMP_FOLDER + File.separatorChar + rCompressed.getCompressedFileName());
                    lblFileSize.setText(LBL_FILE_INFO + compressedFile.length() / 1024 + " KB");

                    /* Delete not compressed files. */
                    for (File f : new File(TEMP_FOLDER).listFiles()) {
                        if (!f.getName().endsWith(".zip")) {
                            OSOperations.removeFile(f.getAbsolutePath());
                        }
                    }
                } else {
                    long totalSize = 0;
                    for (File f : aFiles) {
                        totalSize = totalSize + (f.length() / 1024);
                    }

                    lblFileSize.setText(LBL_FILE_INFO + totalSize + " KB");
                }

            }

        }).start();
    }

    /**
     * Sets the selected photo.
     *
     * @param p selected photo in GUIMain.
     */
    public void setPhoto(Photo p) {
        this.photo = p;
    }

    /* Hide window.*/
    private void btnCloseActionPerformed(ActionEvent e) {
        this.setVisible(false);
    }

    /* Export files to the selected directory. */
    private void btnSendActionPerformed(ActionEvent e) {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setDialogTitle("¿Dónde guardar los ficheros?");

        if (jfc.showOpenDialog(GUIExportImages.this) == JFileChooser.APPROVE_OPTION) {
            File dirTo = jfc.getSelectedFile();
            File dirFrom = new File(TEMP_FOLDER);
            String descr;

            for (File f : dirFrom.listFiles()) {
                OSOperations.copyFile(f.getAbsolutePath(),
                        dirTo.getAbsolutePath() + File.separatorChar + f.getName());
            }

            // Show information message
            if (dirFrom.listFiles().length == 1) {
                descr = "Se ha exportado la foto en el directorio seleccionado";
            } else {
                descr = "Se han exportado las fotos en el directorio seleccionado";
            }

            JOptionPane.showMessageDialog(GUIExportImages.this,
                    descr,
                    "Exportado",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
        }
    }

    /* Create high quality images. */
    private void rbtHighQActionPerformed(ActionEvent e) {
        updateComponents();
        calculateSize();
    }

    /* Create medium quality images. */
    private void rbtMediumQActionPerformed(ActionEvent e) {
        updateComponents();
        calculateSize();
    }

    /* Create low quality images. */
    private void rbtLowQActionPerformed(ActionEvent e) {
        updateComponents();
        calculateSize();
    }

    /* Select files format. */
    private void cbxFormatActionPerformed(ActionEvent e) {
        updateComponents();
        calculateSize();
    }

    /* Remove all items from the list. */
    private void btnClearActionPerformed(ActionEvent e) {
        DefaultListModel<Photo> dlm = (DefaultListModel<Photo>) this.lstPhotos.getModel();
        dlm.clear();

        updateComponents();
        calculateSize();
    }

    /* Remove a photo from the list. */
    private void btnRemoveActionPerformed(ActionEvent e) {
        if (lstPhotos.getSelectedIndex() != -1) {
            DefaultListModel<Photo> dlm = (DefaultListModel<Photo>) this.lstPhotos.getModel();
            dlm.remove(lstPhotos.getSelectedIndex());
        }

        updateComponents();
        calculateSize();
    }

    /* Add a photo to the list. */
    private void btnAddActionPerformed(ActionEvent e) {
        DefaultListModel<Photo> dlm = (DefaultListModel<Photo>) this.lstPhotos.getModel();
        int i;

        for (i = 0; i < dlm.getSize() && !photo.getFile().equals(dlm.get(i).getFile()); i++);
        if (i == dlm.getSize()) {
            dlm.addElement(photo);
        }

        updateComponents();
        calculateSize();
    }

    /* Generate photos from the list. */
    private void rbtListPhotosActionPerformed(ActionEvent e) {
        updateComponents();
        calculateSize();
    }

    /* Generate selected photo. */
    private void rbtSelectedPhotoActionPerformed(ActionEvent e) {
        updateComponents();
        calculateSize();
    }

    //Variables declaration
    private JPanel pnlPhotoSelection;
    private JRadioButton rbtSelectedPhoto;
    private JRadioButton rbtListPhotos;
    private JPanel pnlList;
    private JScrollPane spPhotoList;
    private JList<Photo> lstPhotos;
    private JPanel pnlListButtons;
    private JButton btnAdd;
    private JButton btnRemove;
    private JButton btnClear;
    private JPanel pnlQuality;
    private JRadioButton rbtLowQ;
    private JRadioButton rbtMediumQ;
    private JRadioButton rbtHighQ;
    private JPanel pnlFormat;
    private JComboBox<String> cbxFormat;
    private JPanel pnlFiles;
    private JLabel lblFileSize;
    private JPanel pnlButtons;
    private JButton btnExport;
    private JButton btnClose;
    //End of variables declaration
}
