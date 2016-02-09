/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * GUIImageManager.java
 *
 * Created on Jul 10, 2011, 10:59:12 PM
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
package com.jagobapg.imuchasfotos.gui.imanager;

import com.jagobapg.imuchasfotos.dto.Folder;
import com.jagobapg.imuchasfotos.dto.Topic;
import com.jagobapg.imuchasfotos.dto.Key;
import com.jagobapg.imuchasfotos.dto.Format;
import com.jagobapg.imuchasfotos.dto.Photo;
import com.jagobapg.imuchasfotos.dto.Author;
import com.jagobapg.imuchasfotos.gui.imanager.filters.GUIAuthorFilter;
import com.jagobapg.imuchasfotos.gui.imanager.filters.GUINameFilter;
import com.jagobapg.imuchasfotos.gui.imanager.filters.GUIMixedFilter;
import com.jagobapg.imuchasfotos.gui.imanager.filters.GUIYearFilter;
import com.jagobapg.imuchasfotos.gui.imanager.filters.GUIKeyFilter;
import com.jagobapg.imuchasfotos.gui.imanager.filters.GUITopicFilter;
import com.jagobapg.imuchasfotos.gui.imanager.filters.EGUIFilters;
import com.jagobapg.imuchasfotos.sqlite.DBConnection;
import com.jagobapg.imuchasfotos.sqlite.DBManipulation;
import com.jagobapg.imuchasfotos.sqlite.DBQueries;
import com.jagobapg.imuchasfotos.gui.*;
import com.jagobapg.imuchasfotos.gui.imanager.ImageManagerFilter.EFilter;
import com.jagobapg.imuchasfotos.gui.utilities.OSOperations;
import com.jagobapg.imuchasfotos.gui.utilities.PhotoPrint;
import com.jagobapg.imuchasfotos.gui.utilities.RDeleteFolders;
import com.jagobapg.imuchasfotos.gui.utilities.RDeletePhotos;
import com.jagobapg.imuchasfotos.gui.utilities.RInsertImageLabel;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * This class is the main GUI. Any photo data can be edited from here. In
 * addition, themes, key, authors and format data can be created and modified.
 */
public class GUIImageManager extends javax.swing.JFrame {

    private static final long serialVersionUID = 6330080846154537794L;

    public enum EGUIManagerState {
        CONSULT, EDIT, NEW, NEW_SUBHTHEME
    }

    private static final String STR_NONE_ESP_F = "* Ninguna";
    private static final String STR_NONE_ESP_M = "* Ninguno";

    private final ImageManagerFilter img_filter; //Image filter manager.

    private Thread thLoadPhotos; //Only allow one picture loading.        

    /**
     * Creates new form ImageManager
     */
    public GUIImageManager() {
        this.img_filter = new ImageManagerFilter(this);
        initComponents();
    }

    /**
     * Set text in the notification JLabel.
     *
     * @param text Text to be written.
     */
    public void setNotification(String text) {
        this.lblNotif.setText(text);
    }

    /* Load window design. */
    private void initComponents() {
        ButtonGroup bgFilters;

        pnl = new javax.swing.JPanel();
        tbp = new javax.swing.JTabbedPane();
        pnlPhotos = new javax.swing.JPanel();
        pnlPhotosData = new javax.swing.JPanel();
        lblName = new javax.swing.JLabel("Nombre");
        lblAuthor = new javax.swing.JLabel("Autor");
        lblYear = new javax.swing.JLabel("Año");
        txtName = new javax.swing.JTextField();
        ftxYear = new javax.swing.JFormattedTextField(NumberFormat.getNumberInstance());
        lblThemes = new javax.swing.JLabel("Temas");
        cbxTopics = new javax.swing.JComboBox<Topic>();
        btnAddTopics = new javax.swing.JButton();
        btnRemoveTopics = new javax.swing.JButton();
        lblKeys = new javax.swing.JLabel("Claves");
        cbxKeys = new javax.swing.JComboBox<Key>();
        btnAddKeys = new javax.swing.JButton();
        btnRemoveKeys = new javax.swing.JButton();
        spDescription = new javax.swing.JScrollPane();
        txaDescription = new javax.swing.JTextArea();
        lblDescription = new javax.swing.JLabel("Descripción");
        lblID = new javax.swing.JLabel("ID");
        ftxID = new javax.swing.JFormattedTextField(NumberFormat.getNumberInstance());
        lblPhoto = new javax.swing.JLabel();
        spTopics = new javax.swing.JScrollPane();
        lstTopics = new javax.swing.JList<Topic>();
        spKeys = new javax.swing.JScrollPane();
        lstKeys = new javax.swing.JList<Key>();
        cbxAuthors = new javax.swing.JComboBox<Author>();
        lblFormat = new javax.swing.JLabel("Formato");
        cbxFormat = new javax.swing.JComboBox<Format>();
        lblFolders = new javax.swing.JLabel("Carpetas");
        cbxFolders = new javax.swing.JComboBox<Folder>();
        jspPhotos = new javax.swing.JScrollPane();
        lstPhotos = new javax.swing.JList<Photo>();
        btnLeft = new javax.swing.JButton();
        btnRight = new javax.swing.JButton();
        lblCurrentFolder = new javax.swing.JLabel("Carpeta actual");
        tbEditar = new javax.swing.JToggleButton();
        lblNumImages = new javax.swing.JLabel("Número de imágenes");
        pnlBottom = new javax.swing.JPanel();
        lblNotif = new javax.swing.JLabel("");
        mbr = new javax.swing.JMenuBar();
        mnuTools = new javax.swing.JMenu("Herramientas");
        mniReloadImages = new javax.swing.JMenuItem("Recargar imágenes");
        mniLoadFolders = new javax.swing.JMenuItem("Gestionar carpetas");
        mnuFilters = new javax.swing.JMenu("Filtros");
        rmiNone = new javax.swing.JRadioButtonMenuItem("Ninguno");
        rmiName = new javax.swing.JRadioButtonMenuItem("Nombre");
        rmiKey = new javax.swing.JRadioButtonMenuItem("Clave");
        rmiTopic = new javax.swing.JRadioButtonMenuItem("Tema");
        rmiAuthor = new javax.swing.JRadioButtonMenuItem("Autor");
        rmiYear = new javax.swing.JRadioButtonMenuItem("Año");
        rmiMixed = new javax.swing.JRadioButtonMenuItem("Combinadas");
        mnuHelp = new javax.swing.JMenu("Ayuda");
        mniAbout = new javax.swing.JMenuItem("Acerca de...");
        mnuPhoto = new javax.swing.JMenu("Imágen");
        mniPrint = new javax.swing.JMenuItem("Imprimir selección");
        mniExport = new javax.swing.JMenuItem("Exportar imágenes");
        mniUpdate = new javax.swing.JMenuItem("Actualizar");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("iMuchasFotos - v" + GUIUpdate.SW_CURRENT_VERSION);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnl.setLayout(new java.awt.BorderLayout());

        tbp.setPreferredSize(new java.awt.Dimension(946, 500));

        pnlPhotos.setPreferredSize(new java.awt.Dimension(914, 600));
        pnlPhotos.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                pnlPhotosComponentHidden(evt);
            }

            public void componentShown(java.awt.event.ComponentEvent evt) {
                pnlPhotosComponentShown(evt);
            }
        });

        pnlPhotosData.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        txtName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNameFocusLost(evt);
            }
        });
        txtName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNameKeyTyped(evt);
            }
        });

        ftxYear.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ftxYearFocusLost(evt);
            }
        });
        ftxYear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ftxYearKeyTyped(evt);
            }
        });

        btnAddTopics.setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("images/add.png"))); // NOI18N
        btnAddTopics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddTopicsActionPerformed(evt);
            }
        });

        btnRemoveTopics.setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("images/remove.png"))); // NOI18N
        btnRemoveTopics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveTopicsActionPerformed(evt);
            }
        });

        btnAddKeys.setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("images/add.png"))); // NOI18N
        btnAddKeys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddKeysActionPerformed(evt);
            }
        });

        btnRemoveKeys.setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("images/remove.png"))); // NOI18N
        btnRemoveKeys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveKeysActionPerformed(evt);
            }
        });

        txaDescription.setColumns(20);
        txaDescription.setRows(5);
        txaDescription.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txaDescriptionFocusLost(evt);
            }
        });
        txaDescription.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txaDescriptionKeyTyped(evt);
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    txtName.requestFocus();
                }
            }
        });

        spDescription.setViewportView(txaDescription);

        ftxID.setEditable(false);

        lblPhoto.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblPhoto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblPhoto.setPreferredSize(new java.awt.Dimension(50, 20));
        lblPhoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblPhotoMouseReleased(evt);
            }
        });

        spTopics.setViewportView(lstTopics);

        spKeys.setViewportView(lstKeys);

        cbxAuthors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxAuthorsActionPerformed(evt);
            }
        });

        cbxFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxFormatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlPhotosDataLayout = new javax.swing.GroupLayout(pnlPhotosData);
        pnlPhotosData.setLayout(pnlPhotosDataLayout);
        pnlPhotosDataLayout.setHorizontalGroup(
                pnlPhotosDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlPhotosDataLayout.createSequentialGroup()
                        .addGroup(pnlPhotosDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(spDescription, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlPhotosDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlPhotosDataLayout.createSequentialGroup()
                                                .addComponent(lblKeys)
                                                .addGap(47, 47, 47)
                                                .addComponent(cbxKeys, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnAddKeys)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnRemoveKeys))
                                        .addComponent(lblPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(spTopics, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
                                        .addComponent(spKeys, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblDescription, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlPhotosDataLayout.createSequentialGroup()
                                                .addGroup(pnlPhotosDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblID)
                                                        .addComponent(lblName)
                                                        .addComponent(lblYear)
                                                        .addComponent(lblAuthor)
                                                        .addComponent(lblFormat)
                                                        .addComponent(lblThemes))
                                                .addGap(18, 18, 18)
                                                .addGroup(pnlPhotosDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(ftxYear, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(ftxID, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(pnlPhotosDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                .addComponent(cbxFormat, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(cbxAuthors, javax.swing.GroupLayout.Alignment.LEADING, 0, 286, Short.MAX_VALUE))
                                                        .addGroup(pnlPhotosDataLayout.createSequentialGroup()
                                                                .addComponent(cbxTopics, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(btnAddTopics)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(btnRemoveTopics))))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlPhotosDataLayout.setVerticalGroup(
                pnlPhotosDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlPhotosDataLayout.createSequentialGroup()
                        .addGroup(pnlPhotosDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(pnlPhotosDataLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(lblPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(pnlPhotosDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(btnAddTopics, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(btnRemoveTopics, javax.swing.GroupLayout.Alignment.TRAILING)))
                                .addGroup(pnlPhotosDataLayout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addGroup(pnlPhotosDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(lblID)
                                                .addComponent(ftxID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(pnlPhotosDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(lblName)
                                                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(pnlPhotosDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(lblYear)
                                                .addComponent(ftxYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(pnlPhotosDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(lblAuthor)
                                                .addComponent(cbxAuthors, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(pnlPhotosDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(lblFormat)
                                                .addComponent(cbxFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(16, 16, 16)
                                        .addGroup(pnlPhotosDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(lblThemes)
                                                .addComponent(cbxTopics, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                        .addComponent(spTopics, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(pnlPhotosDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(pnlPhotosDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblKeys)
                                        .addComponent(cbxKeys, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(btnAddKeys)
                                .addComponent(btnRemoveKeys))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spKeys, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDescription)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
        );

        cbxFolders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxFoldersActionPerformed(evt);
            }
        });

        lstPhotos.setModel(new DefaultListModel<Photo>());

        lstPhotos.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstPhotosValueChanged(evt);
            }
        });
        jspPhotos.setViewportView(lstPhotos);

        btnLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeftActionPerformed(evt);
            }
        });

        btnRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRightActionPerformed(evt);
            }
        });

        tbEditar.setText("Modo edición");
        tbEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbEditarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlPhotosLayout = new javax.swing.GroupLayout(pnlPhotos);
        pnlPhotos.setLayout(pnlPhotosLayout);
        pnlPhotosLayout.setHorizontalGroup(
                pnlPhotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlPhotosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlPhotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(pnlPhotosLayout.createSequentialGroup()
                                        .addComponent(lblFolders)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbxFolders, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(tbEditar))
                                .addGroup(pnlPhotosLayout.createSequentialGroup()
                                        .addGroup(pnlPhotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(pnlPhotosLayout.createSequentialGroup()
                                                        .addComponent(jspPhotos, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                                .addGroup(pnlPhotosLayout.createSequentialGroup()
                                                        .addGap(10, 10, 10)
                                                        .addComponent(btnLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(18, 18, 18)
                                                        .addGroup(pnlPhotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(pnlPhotosLayout.createSequentialGroup()
                                                                        .addComponent(lblNumImages)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                .addGroup(pnlPhotosLayout.createSequentialGroup()
                                                                        .addComponent(lblCurrentFolder)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(btnRight, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(22, 22, 22)))))
                                        .addComponent(pnlPhotosData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(12, Short.MAX_VALUE))
        );
        pnlPhotosLayout.setVerticalGroup(
                pnlPhotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlPhotosLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(pnlPhotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblFolders)
                                .addComponent(cbxFolders, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tbEditar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlPhotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(pnlPhotosData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(pnlPhotosLayout.createSequentialGroup()
                                        .addComponent(jspPhotos, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(pnlPhotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(btnLeft, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnRight, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lblCurrentFolder, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblNumImages)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tbp.addTab("Gestor de Imágenes", pnlPhotos);

        tbp.addTab("Gestor de Temas", new PnlTopicManager(this));
        tbp.addTab("Gestor de Autores", new PnlAuthorManager(this));
        tbp.addTab("Gestor de Claves", new PnlKeyManager(this));
        tbp.addTab("Gestor de Formatos", new PnlFormatManager(this));

        pnl.add(tbp, java.awt.BorderLayout.CENTER);

        pnlBottom.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        pnlBottom.setLayout(new javax.swing.BoxLayout(pnlBottom, javax.swing.BoxLayout.LINE_AXIS));

        lblNotif.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNotif.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblNotif.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        pnlBottom.add(lblNotif);

        pnl.add(pnlBottom, java.awt.BorderLayout.SOUTH);

        getContentPane().add(pnl, java.awt.BorderLayout.CENTER);

        //Menus.
        mniReloadImages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniReloadImagesActionPerformed(evt);
            }
        });

        mniLoadFolders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniLoadFoldersActionPerformed(evt);
            }
        });

        mnuTools.add(mniReloadImages);
        mnuTools.add(mniLoadFolders);
        mbr.add(mnuTools);

        rmiNone.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                rmiNoneActionPerformed(e);
            }

        });

        rmiName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rmiNameActionPerformed(evt);
            }
        });

        rmiKey.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                rmiKeyActionPerformed(e);
            }

        });

        rmiAuthor.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                rmiAuthorActionPerformed(e);
            }

        });

        rmiTopic.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                rmiTopicActionPerformed(e);
            }

        });

        rmiYear.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                rmiYearActionPerformed(e);
            }

        });

        rmiMixed.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                rmiMixedActionPerformed(e);
            }

        });

        bgFilters = new ButtonGroup(); //Filter group.
        bgFilters.add(rmiNone);
        bgFilters.add(rmiName);
        bgFilters.add(rmiKey);
        bgFilters.add(rmiAuthor);
        bgFilters.add(rmiTopic);
        bgFilters.add(rmiYear);
        bgFilters.add(rmiMixed);

        rmiNone.setSelected(true);
        mnuFilters.add(rmiNone);
        mnuFilters.add(rmiName);
        mnuFilters.add(rmiAuthor);
        mnuFilters.add(rmiYear);
        mnuFilters.add(rmiKey);
        mnuFilters.add(rmiTopic);
        mnuFilters.add(rmiMixed);
        mbr.add(mnuFilters);

        mniPrint.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mniPrintActionPerformed(e);
            }

        });

        mniExport.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mniExportActionPerformed(e);
            }

        });

        mnuPhoto.add(mniPrint);
        mnuPhoto.add(mniExport);
        mbr.add(mnuPhoto);

        mniUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniUpdateActionPerformed(evt);
            }
        });

        mniAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniAboutActionPerformed(evt);
            }
        });

        mnuHelp.add(mniUpdate);
        mnuHelp.add(mniAbout);
        mbr.add(mnuHelp);

        setJMenuBar(mbr);
        //End menus

        setSize(new java.awt.Dimension(937, 820));
        setLocationRelativeTo(null);
    }

    private void clearData() {
        Thread th;

        /* Remove directories. */
        OSOperations.removeDirectory(new File("temp").getAbsolutePath());
        th = new Thread(new RDeleteFolders());
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
        }

        /* Remove photos. */
        th = new Thread(new RDeletePhotos());
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
        }
    }

    //Global action listener.
    /* Close window. */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        this.setVisible(false);
        clearData();

        DBConnection.closeConexion();
        System.exit(0);
    }
    //End Global action listener.

    /**
     * ************************ Photo TAB. ********************************
     */
    /* Data vars. */
    protected EFilter imageFilter;
    protected DefaultListModel<Photo> dlmPhoto;

    protected Photo[] aPhotoBefore;
    protected Photo[] aPhotoCurrent;
    protected Photo[] aPhotoAfter;

    /* Navigation vars. */
    protected static final int PHOTOS_EACH_PAGE = 30;
    protected int maxPages;
    protected int currentPage;

    /**
     * Load design. *
     */
    private void loadDesign_photoTab() {
        final Folder[] aFolder;
        final Author[] aAuthor;
        final Format[] aFormat;
        final Topic[] aTopic;
        final Key[] aKey;

        rmiNone.setSelected(true);

        // Set up buttons
        setButtonIcon(this.btnLeft, "images/left.png", 15, 15);
        setButtonIcon(this.btnRight, "images/right.png", 15, 15);

        // Set up JLists
        this.lstKeys.setModel(new DefaultListModel<Key>());
        this.lstTopics.setModel(new DefaultListModel<Topic>());
        this.dlmPhoto = new DefaultListModel<Photo>();
        this.lstPhotos.setModel(this.dlmPhoto);

        // Set up JComboBoxes
        this.cbxAuthors.setModel(new DefaultComboBoxModel<Author>());
        this.cbxAuthors.addItem(new Author(0, STR_NONE_ESP_M));
        this.cbxFolders.setModel(new DefaultComboBoxModel<Folder>());
        this.cbxFolders.addItem(new Folder(0, STR_NONE_ESP_F));
        this.cbxFormat.setModel(new DefaultComboBoxModel<Format>());
        this.cbxFormat.addItem(new Format(0, STR_NONE_ESP_M));
        this.cbxKeys.setModel(new DefaultComboBoxModel<Key>());
        this.cbxKeys.addItem(new Key(0, STR_NONE_ESP_F));
        this.cbxTopics.setModel(new DefaultComboBoxModel<Topic>());
        this.cbxTopics.addItem(new Topic(0, STR_NONE_ESP_M));

        // Fill JComboBoxes
        // Fill folders
        aFolder = DBQueries.INSTANCE.INSTANCE.getFolders();
        for (Folder f : aFolder) {
            this.cbxFolders.addItem(f);
        }

        // Fill authors
        aAuthor = DBQueries.INSTANCE.INSTANCE.getAuthors();
        for (Author a : aAuthor) {
            this.cbxAuthors.addItem(a);
        }

        // Fill formats
        aFormat = DBQueries.INSTANCE.getFormats();
        for (Format a : aFormat) {
            this.cbxFormat.addItem(a);
        }

        // Fill topics
        aTopic = DBQueries.INSTANCE.getTopics();
        for (Topic t : aTopic) {
            this.cbxTopics.addItem(t);
        }

        // Fill keys
        aKey = DBQueries.INSTANCE.getKeys();
        for (Key k : aKey) {
            this.cbxKeys.addItem(k);
        }

        // Labels
        lblNotif.setText("");
        lblNotif.setForeground(Color.red);
        lblCurrentFolder.setText("Listado " + this.currentPage + " de " + this.maxPages);
    }

    /**
     * Set button properties.
     *
     * @param btn component.
     * @param image_src source of the image.
     * @param width desired width.
     * @param height desired height.
     */
    private void setButtonIcon(JButton btn, String image_src, int width, int height) {
        Image i;

        // Load and scale image
        i = new ImageIcon(getClass().getClassLoader().getResource(image_src)).getImage();
        i = i.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        btn.setIcon(new ImageIcon(i)); //Set icon in button.
        btn.setText("");
    }

    /* Initial photo loading. */
    protected void loadInitialPhotosPage() {
        // Set initial data
        this.currentPage = 1;
        this.dlmPhoto.clear();
        this.aPhotoAfter = null;
        this.aPhotoCurrent = null;
        this.aPhotoBefore = null;
    }

    /* Update components behaviour. */
    private void refreshPhotoTab() {
        this.lblCurrentFolder.setText("Listado " + this.currentPage + " de " + this.maxPages);

        // Buttons
        this.btnLeft.setEnabled(this.currentPage > 1);
        this.btnRight.setEnabled(this.currentPage < this.maxPages);

        // Text Fields
        this.txtName.setEditable(this.tbEditar.isSelected());
        this.ftxYear.setEditable(this.tbEditar.isSelected());
        this.cbxAuthors.setEnabled(this.tbEditar.isSelected());
        this.cbxFormat.setEnabled(this.tbEditar.isSelected());
        this.cbxKeys.setEnabled(this.tbEditar.isSelected());
        this.cbxTopics.setEnabled(this.tbEditar.isSelected());
        this.btnAddTopics.setEnabled(this.tbEditar.isSelected());
        this.btnRemoveTopics.setEnabled(this.tbEditar.isSelected());
        this.btnAddKeys.setEnabled(this.tbEditar.isSelected());
        this.btnRemoveKeys.setEnabled(this.tbEditar.isSelected());
        this.txaDescription.setEditable(this.tbEditar.isSelected());
    }

    /* Fills data with the desired data. */
    private void fillData_photoTab(Photo p) {
        /* Text fields. */
        ftxID.setValue(p.getId());
        txtName.setText(p.getName());
        if (p.getYear() != 0) {
            ftxYear.setValue(p.getYear());
        } else {
            ftxYear.setValue(0);
        }
        if (p.getDescription() == null || p.getDescription().equals("")) {
            txaDescription.setText("");
        } else {
            txaDescription.setText(p.getDescription());
        }

        // Image loading
        // If a image was loading, the thread is interrupted.
        if (this.thLoadPhotos != null) {
            this.thLoadPhotos.interrupt();
        }

        this.thLoadPhotos = new Thread(new RInsertImageLabel(lblPhoto, p.getFile(), 150, 150), "Load image");
        this.thLoadPhotos.start();

        // Selected author
        if (p.getIdAuthor() != 0) {
            Author temp = new Author();
            temp.setId(p.getIdAuthor());

            for (int i = 0; i < cbxAuthors.getItemCount() && !temp.equals((Author) cbxAuthors.getSelectedItem()); i++) {
                cbxAuthors.setSelectedIndex(i);
            }
        } else {
            cbxAuthors.setSelectedIndex(0);
        }

        // Selected format
        if (p.getIdFormat() != 0) {
            Format temp = new Format();
            temp.setId(p.getIdFormat());

            for (int i = 0; i < cbxFormat.getItemCount() && !temp.equals((Format) cbxFormat.getSelectedItem()); i++) {
                cbxFormat.setSelectedIndex(i);
            }
        } else {
            cbxFormat.setSelectedIndex(0);
        }

        fillData_topics(p);
        fillData_keys(p);
    }

    /* Fills data of topics. */
    private void fillData_topics(Photo p) {
        class RFillData implements Runnable {

            private Photo p;

            public RFillData(Photo p) {
                this.p = p;
            }

            @Override
            public void run() {
                // fill topics
                Topic[] aTopic = DBQueries.INSTANCE.getPhotoThemes(p.getId());
                DefaultListModel<Topic> dlm = new DefaultListModel<Topic>();
                lstTopics.setModel(dlm);
                for (Topic t : aTopic) {
                    dlm.addElement(t);
                }
            }

        }

        new Thread(new RFillData(p)).start();
    }

    /* Fills data of keys. */
    private void fillData_keys(Photo p) {
        class RFillData implements Runnable {

            private Photo p;

            public RFillData(Photo p) {
                this.p = p;
            }

            @Override
            public void run() {
                /* Keys. */
                Key[] aKey = DBQueries.INSTANCE.getPhotoKeys(p.getId());
                DefaultListModel<Key> dlm = new DefaultListModel<Key>();
                lstKeys.setModel(dlm);
                for (Key k : aKey) {
                    dlm.addElement(k);
                }
            }

        }

        new Thread(new RFillData(p)).start();
    }

    //Component listeners 
    /* Show photo tab. */
    private void pnlPhotosComponentShown(java.awt.event.ComponentEvent evt) {
        this.imageFilter = EFilter.NO_FILTER;
        loadDesign_photoTab();
        loadInitialPhotosPage();

        // load initial image set: Depends on the current filter
        this.img_filter.applyFilter();

        // default value
        if (!this.dlmPhoto.isEmpty()) {
            this.lstPhotos.setSelectedIndex(0);
        }
    }

    /* Hide photo tab. */
    private void pnlPhotosComponentHidden(java.awt.event.ComponentEvent evt) {
        if (dlmPhoto != null) {
            // Clear JLists
            ((DefaultListModel<Photo>) this.lstPhotos.getModel()).clear();
            ((DefaultListModel<Key>) this.lstKeys.getModel()).clear();
            ((DefaultListModel<Topic>) this.lstTopics.getModel()).clear();;

            // Set up JComboBoxes
            ((DefaultComboBoxModel<Author>) this.cbxAuthors.getModel()).removeAllElements();
            ((DefaultComboBoxModel<Format>) this.cbxFormat.getModel()).removeAllElements();
            ((DefaultComboBoxModel<Folder>) this.cbxFolders.getModel()).removeAllElements();
            ((DefaultComboBoxModel<Key>) this.cbxKeys.getModel()).removeAllElements();
            ((DefaultComboBoxModel<Topic>) this.cbxTopics.getModel()).removeAllElements();
        }
    }

    /* Selected photo changed. */
    private void lstPhotosValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (!evt.getValueIsAdjusting()) {
            int[] index = this.lstPhotos.getSelectedIndices();

            if (index.length > 1) {
                // multiple selection edition
                ftxID.setValue(null);
                txtName.setText("");
                ftxYear.setValue(null);
                cbxFormat.setSelectedIndex(0);
                cbxAuthors.setSelectedIndex(0);
                ((DefaultListModel<Key>) lstKeys.getModel()).clear();
                ((DefaultListModel<Topic>) lstTopics.getModel()).clear();
                txaDescription.setText("");
            } else {
                // only one item selected
                if (lstPhotos.getSelectedIndex() != -1) {
                    // load data
                    Photo p;
                    if ((p = (Photo) this.dlmPhoto.get(lstPhotos.getSelectedIndex())) != null) {
                        fillData_photoTab(p);
                    }
                }

                refreshPhotoTab();
            }
        }
    }

    /* Go to one page previous. */
    private void btnLeftActionPerformed(java.awt.event.ActionEvent evt) {
        --this.currentPage;
        this.aPhotoAfter = this.aPhotoCurrent;
        this.aPhotoCurrent = this.aPhotoBefore;

        // load aPhotoBefore depending on the filter
        img_filter.previousPageFilter();

        // reload list
        this.dlmPhoto.clear();
        for (Photo p : this.aPhotoCurrent) {
            this.dlmPhoto.addElement(p);
        }

        // default value
        if (!this.dlmPhoto.isEmpty()) {
            this.lstPhotos.setSelectedIndex(0);
        }
        refreshPhotoTab();
    }

    /* Go to one page after. */
    private void btnRightActionPerformed(java.awt.event.ActionEvent evt) {
        ++this.currentPage;
        this.aPhotoBefore = this.aPhotoCurrent;
        this.aPhotoCurrent = this.aPhotoAfter;

        // load aPhotoNext, use the proper filter for loading
        img_filter.nextPageFilter();

        // reload list
        this.dlmPhoto.clear();
        for (Photo p : this.aPhotoCurrent) {
            this.dlmPhoto.addElement(p);
        }

        // default value
        if (!this.dlmPhoto.isEmpty()) {
            this.lstPhotos.setSelectedIndex(0);
        }
        refreshPhotoTab();
    }

    /* Toggle to edit or consult mode. */
    private void tbEditarActionPerformed(java.awt.event.ActionEvent evt) {
        refreshPhotoTab();
    }

    /* Check and save name. */
    private void txtNameFocusLost(java.awt.event.FocusEvent evt) {
        String name;
        Photo p;

        for (int index : this.lstPhotos.getSelectedIndices()) {
            // single or multiple list selection.
            name = txtName.getText();
            p = (Photo) this.dlmPhoto.get(index);

            if (!name.trim().equals("") && name.length() < 255) {
                // save data into database
                DBManipulation.INSTANCE.updatePhotoName(p.getId(), name.trim());
                p.setName(name.trim());
                txtName.setText(name.trim());
                lstPhotos.repaint();
            } else {
                Timer t;
                // show notification and hide
                lblNotif.setText("Nombre de foto no válido.");
                txtName.setText(p.getName());
                t = new Timer(5000, new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        lblNotif.setText("");
                    }

                });
                t.start();
            }
        }
    }

    /* Check and save year. */
    private void ftxYearFocusLost(java.awt.event.FocusEvent evt) {
        Photo p;

        try {
            int value = Integer.parseInt(ftxYear.getText());

            for (int index : this.lstPhotos.getSelectedIndices()) {
                // single or multiple list selection.
                p = this.dlmPhoto.get(index);
                DBManipulation.INSTANCE.updatePhotoYear(p.getId(), value);
                p.setYear(value);
            }
        } catch (NumberFormatException ex) {
        }
    }

    /* Check and save description. */
    private void txaDescriptionFocusLost(java.awt.event.FocusEvent evt) {
        String txt = txaDescription.getText();
        Photo p;

        for (int index : this.lstPhotos.getSelectedIndices()) {
            // single or multiple list selection
            p = this.dlmPhoto.get(index);

            if (!txt.trim().equals("") && txt.length() < 255) {
                // save data into database
                DBManipulation.INSTANCE.updatePhotoDescription(p.getId(), txt.trim());
                p.setDescription(txt.trim());
                txaDescription.setText(txt.trim());
            } else if (tbEditar.isSelected() && txt.trim().equals("") && txt.length() > 255) {
                // show notification and hide
                lblNotif.setText("Descripción no válida.");
                txaDescription.setText(p.getDescription());
                Timer t = new Timer(5000, new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        lblNotif.setText("");
                    }

                });
                t.start();
            }
        }
    }

    /* Control name words. */
    private void txtNameKeyTyped(java.awt.event.KeyEvent evt) {
        String txt = this.txtName.getText();

        if (txt.length() > 255) {
            txt = txt.substring(0, 60);
            this.txtName.setText(txt);
            this.lblNotif.setText("Caracteres máximos: 255");
        }
    }

    /* Control year chars. */
    private void ftxYearKeyTyped(java.awt.event.KeyEvent evt) {
        String txt = this.ftxYear.getText();

        if (txt.length() > 6) {
            txt = txt.substring(0, 6);
            this.ftxYear.setText(txt);
            this.lblNotif.setText("Caracteres máximos: 6");
        }
    }

    /* Control description words. */
    private void txaDescriptionKeyTyped(java.awt.event.KeyEvent evt) {
        String txt = this.txaDescription.getText();

        if (txt.length() > 255) {
            txt = txt.substring(0, 255);
            this.ftxYear.setText(txt);
            this.lblNotif.setText("Caracteres máximos: 255");
        }
    }

    /* Save author. */
    private void cbxAuthorsActionPerformed(java.awt.event.ActionEvent evt) {
        Author a;
        Photo p;

        for (int index : this.lstPhotos.getSelectedIndices()) {
            // single or multiple list selection
            if (this.cbxAuthors.getSelectedIndex() != -1 && this.lstPhotos.getSelectedIndex() != -1) {
                a = (Author) this.cbxAuthors.getItemAt(this.cbxAuthors.getSelectedIndex());
                p = (Photo) this.dlmPhoto.get(index);
                if (a.getName().equals(STR_NONE_ESP_M)) { //No authors added.          
                    DBManipulation.INSTANCE.updatePhotoAuthor(p.getId(), 0);
                    p.setIdAuthor(0);
                } else { //Add author.
                    DBManipulation.INSTANCE.updatePhotoAuthor(p.getId(), a.getId());
                    p.setIdAuthor(a.getId());
                }
            }
        }
    }

    /* Save format. */
    private void cbxFormatActionPerformed(java.awt.event.ActionEvent evt) {
        Format f;
        Photo p;

        for (int index : this.lstPhotos.getSelectedIndices()) {
            // single or multiple list selection
            if (this.cbxFormat.getSelectedIndex() != -1 && this.lstPhotos.getSelectedIndex() != -1) {
                f = this.cbxFormat.getItemAt(this.cbxFormat.getSelectedIndex());
                p = this.dlmPhoto.get(index);

                if (f.getName().equals(STR_NONE_ESP_M)) {
                    // no format added
                    DBManipulation.INSTANCE.updatePhotoFormat(p.getId(), 0);
                    p.setIdFormat(0);
                } else {
                    // add format
                    DBManipulation.INSTANCE.updatePhotoFormat(p.getId(), f.getId());
                    p.setIdFormat(f.getId());
                }
            }
        }
    }

    /* Add topic to the photo. */
    private void btnAddTopicsActionPerformed(java.awt.event.ActionEvent evt) {
        Topic t;
        Photo p;

        for (int index : this.lstPhotos.getSelectedIndices()) {
            // single or multiple list selection
            if (index != -1 && (t = (Topic) cbxTopics.getSelectedItem()) != null && !t.getTopic().equals(STR_NONE_ESP_M)) {
                p = this.dlmPhoto.get(index);

                // insert topic
                DBManipulation.INSTANCE.insertPhotoTopic(p.getId(), t.getId());

                if (t.isSubtopic()) {
                    Topic parent = t.getParentTema();
                    // insert topic
                    DBManipulation.INSTANCE.insertPhotoTopic(p.getId(), parent.getId());
                }

                // update showed information
                fillData_topics(p);
            }
        }
    }

    /* Remove topic from the photo. */
    private void btnRemoveTopicsActionPerformed(java.awt.event.ActionEvent evt) {
        int indexLst = this.lstTopics.getSelectedIndex();
        Topic t;
        DefaultListModel<Topic> dlm = (DefaultListModel<Topic>) this.lstTopics.getModel();

        // Single or multiple list selection.
        for (int index : this.lstPhotos.getSelectedIndices()) {

            if (indexLst != -1 && (t = (Topic) dlm.get(indexLst)) != null) {
                Photo p = this.dlmPhoto.get(index);

                // Insert topic
                DBManipulation.INSTANCE.deletePhotoTopic(p.getId(), t.getId());

                // Update showed information
                fillData_topics(p);
            }

        }
    }

    /* Add key to the list. */
    private void btnAddKeysActionPerformed(java.awt.event.ActionEvent evt) {
        int indexLst = this.cbxKeys.getSelectedIndex();
        Key k;
        for (int index : this.lstPhotos.getSelectedIndices()) {
            // Single or multiple list selection
            if (indexLst != -1 && (k = (Key) cbxKeys.getSelectedItem()) != null && !k.getName().equals(STR_NONE_ESP_F)) {
                Photo p = (Photo) this.dlmPhoto.get(index);
                // Insert key
                DBManipulation.INSTANCE.insertPhotoKey(p.getId(), k.getId());

                // Update showed information
                fillData_keys(p);
            }

        }
    }

    /* Remove key from the list. */
    private void btnRemoveKeysActionPerformed(java.awt.event.ActionEvent evt) {
        int indexLst = this.lstKeys.getSelectedIndex();
        DefaultListModel<Key> dlm = (DefaultListModel<Key>) this.lstKeys.getModel();
        Key k;

        for (int index : this.lstPhotos.getSelectedIndices()) {

            // single or multiple list selection
            if (indexLst != -1 && (k = (Key) dlm.get(indexLst)) != null) {
                Photo p = (Photo) this.dlmPhoto.get(index);

                // insert key
                DBManipulation.INSTANCE.deletePhotoKey(p.getId(), k.getId());

                // update showed information.
                fillData_keys(p);
            }

        }
    }

    /* Show photos of selected folder. */
    private void cbxFoldersActionPerformed(java.awt.event.ActionEvent evt) {
        int index = this.cbxFolders.getSelectedIndex();
        Folder f = null;

        if (index != -1 && (f = (Folder) cbxFolders.getSelectedItem()) != null && !f.getName().equals(STR_NONE_ESP_F)) {
            // Filter folder
            this.imageFilter = EFilter.FOLDER_FILTER;
        } else if (index != -1 && (f = (Folder) cbxFolders.getSelectedItem()) != null && f.getName().equals(STR_NONE_ESP_F)) {
            // Load all folders
            this.imageFilter = EFilter.NO_FILTER;
        }

        if (f != null) {
            // Change filter menu radio button
            this.rmiNone.setSelected(true);

            // Load images
            this.img_filter.applyFilter();
        }
    }

    /* Reload images and save new images in the DB. */
    private void mniReloadImagesActionPerformed(java.awt.event.ActionEvent evt) {
        new GUIAddImages(GUIImageManager.this, true).setVisible(true);
    }

    /* Add or remove folders in the DB. */
    private void mniLoadFoldersActionPerformed(java.awt.event.ActionEvent evt) {
        new GUIAddFolder(GUIImageManager.this, true).setVisible(true);

        // Reload data
        this.imageFilter = EFilter.NO_FILTER;
        loadDesign_photoTab();
        loadInitialPhotosPage();

        // Load initial image set: Depends on the current filter
        this.img_filter.applyFilter();

        if (!this.dlmPhoto.isEmpty()) {
            this.lstPhotos.setSelectedIndex(0); //Default value.
        }
    }

    /* Load image in a bigger window. */
    private void lblPhotoMouseReleased(java.awt.event.MouseEvent evt) {
        int index = this.lstPhotos.getSelectedIndex();

        if (index != -1) {
            Photo p = this.dlmPhoto.get(index);
            GUIPhoto gui = new GUIPhoto(GUIImageManager.this, true, p.getFile());
            gui.setVisible(true);
        }
    }

    /* Load none filter. */
    private void rmiNoneActionPerformed(ActionEvent e) {
        rmiNone.setSelected(true);
        cbxFolders.setSelectedIndex(0);
    }

    /* Load filter by name. */
    private void rmiNameActionPerformed(ActionEvent evt) {
        GUINameFilter gui = new GUINameFilter(this);
        gui.setVisible(true);

        if (gui.getStatus() == EGUIFilters.APPLIED) {
            // Remove folder filter
            this.cbxFolders.setSelectedIndex(-1);

            // Set new status
            imageFilter = EFilter.NAME_FILTER;
            this.rmiName.setSelected(true);

            // Set filter resource
            this.img_filter.filter_aux = gui.getFilterName();

            // Call to the filter
            img_filter.applyFilter();

            // Show first image
            this.lstPhotos.setSelectedIndex(0);
        }

        gui.dispose();
    }

    /* Load filter by key. */
    private void rmiKeyActionPerformed(ActionEvent evt) {
        GUIKeyFilter gui = new GUIKeyFilter(this);
        gui.setVisible(true);

        if (gui.getStatus() == EGUIFilters.APPLIED) {
            // Remove folder filter
            this.cbxFolders.setSelectedIndex(-1);

            // Set new status
            imageFilter = EFilter.KEY_FILTER;
            this.rmiKey.setSelected(true);

            // Set filter resource
            this.img_filter.filter_aux = gui.getFilterName();

            // Call to the filter
            img_filter.applyFilter();

            // Show first image
            this.lstPhotos.setSelectedIndex(0);
        }

        gui.dispose();
    }

    /* Load filter by author. */
    private void rmiAuthorActionPerformed(ActionEvent evt) {
        GUIAuthorFilter gui = new GUIAuthorFilter(this);
        gui.setVisible(true);

        if (gui.getStatus() == EGUIFilters.APPLIED) {
            // Remove folder filter
            this.cbxFolders.setSelectedIndex(-1);

            // Set new status
            imageFilter = EFilter.AUTHOR_FILTER;
            this.rmiAuthor.setSelected(true);

            // Set filter resource
            this.img_filter.filter_aux = gui.getFilterName();

            // Call to the filter
            img_filter.applyFilter();

            // Show first image
            this.lstPhotos.setSelectedIndex(0);
        }

        gui.dispose();
    }

    /* Load filter by topic. */
    private void rmiTopicActionPerformed(ActionEvent evt) {
        GUITopicFilter gui = new GUITopicFilter(this);
        gui.setVisible(true);

        if (gui.getStatus() == EGUIFilters.APPLIED) {
            // Remove folder filter
            this.cbxFolders.setSelectedIndex(-1);

            // Set new status
            imageFilter = EFilter.TOPIC_FILTER;
            this.rmiTopic.setSelected(true);

            // Set filter resource
            this.img_filter.filter_aux = gui.getFilterName();

            // Call to the filter
            img_filter.applyFilter();

            // Show first image
            this.lstPhotos.setSelectedIndex(0);
        }

        gui.dispose();
    }

    /* Load filter by year. */
    private void rmiYearActionPerformed(ActionEvent evt) {
        GUIYearFilter gui = new GUIYearFilter(this);
        gui.setVisible(true);

        if (gui.getStatus() == EGUIFilters.APPLIED) {
            // Remove folder filter
            this.cbxFolders.setSelectedIndex(-1);

            // Set new status
            imageFilter = EFilter.YEAR_FILTER;
            this.rmiYear.setSelected(true);

            // Set filter resource
            this.img_filter.filter_aux = gui.getFilterName();

            // Call to the filter
            img_filter.applyFilter();

            // Show first image
            this.lstPhotos.setSelectedIndex(0);
        }

        gui.dispose();
    }

    /* Load mixed filter. */
    private void rmiMixedActionPerformed(ActionEvent evt) {
        GUIMixedFilter gui = new GUIMixedFilter(this);
        gui.setVisible(true);

        if (gui.getStatus() == EGUIFilters.APPLIED) {
            // Remove folder filter
            this.cbxFolders.setSelectedIndex(-1);

            // Set new status
            imageFilter = EFilter.MIXED_FILTER;
            this.rmiMixed.setSelected(true);

            // Set filter resource
            this.img_filter.filter_aux = gui.getFilter1();
            this.img_filter.filter_aux2 = gui.getFilter2();
            this.img_filter.and_or_aux = gui.getAndOr();

            // Call to the filter
            img_filter.applyFilter();

            // Show first image
            this.lstPhotos.setSelectedIndex(0);
        } else {
            this.rmiNone.setSelected(true);
        }

        gui.dispose();
    }

    /* Update. */
    private void mniUpdateActionPerformed(ActionEvent evt) {
        new GUIUpdate(this).setVisible(true);
    }

    /* Credits. */
    private void mniAboutActionPerformed(java.awt.event.ActionEvent evt) {
        new GUIAbout(GUIImageManager.this, true).setVisible(true);
    }
    //End of component listeners

    /* Print selected photo. */
    private void mniPrintActionPerformed(ActionEvent e) {
        Photo p;

        if ((p = lstPhotos.getSelectedValue()) != null) {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(new PhotoPrint(p.getFile()));

            if (job.printDialog()) {
                try {
                    job.print();
                } catch (PrinterException ex) {
                }
            }
        } else {
            // Show notification and hide
            lblNotif.setText("Ninguna foto seleccionada.");
            Timer t = new Timer(5000, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    lblNotif.setText("");
                }

            });
            t.start();
        }
    }

    /* Send selected photo. */
    GUIExportImages guiExport;

    private void mniExportActionPerformed(ActionEvent e) {
        Photo p;

        if ((p = lstPhotos.getSelectedValue()) != null) {
            if (guiExport == null) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        DefaultListModel<Photo> dlm = (DefaultListModel<Photo>) lstPhotos.getModel();
                        guiExport = new GUIExportImages(GUIImageManager.this, dlm.get(lstPhotos.getSelectedIndex()));
                        guiExport.setVisible(true);
                    }

                });
            } else {
                guiExport.setPhoto(p);
                guiExport.setVisible(true);
            }
        } else {
            // Show notification and hide
            lblNotif.setText("Ninguna foto seleccionada.");
            Timer t = new Timer(5000, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    lblNotif.setText("");
                }

            });
            t.start();
        }
    }

    // Variables declaration
    private javax.swing.JButton btnAddKeys;
    private javax.swing.JButton btnAddTopics;
    private javax.swing.JButton btnLeft;
    private javax.swing.JButton btnRemoveKeys;
    private javax.swing.JButton btnRemoveTopics;
    private javax.swing.JButton btnRight;
    private javax.swing.JComboBox<Author> cbxAuthors;
    protected javax.swing.JComboBox<Folder> cbxFolders;
    private javax.swing.JComboBox<Format> cbxFormat;
    private javax.swing.JComboBox<Key> cbxKeys;
    private javax.swing.JComboBox<Topic> cbxTopics;
    private javax.swing.JFormattedTextField ftxID;
    private javax.swing.JFormattedTextField ftxYear;
    private javax.swing.JScrollPane jspPhotos;
    private javax.swing.JLabel lblAuthor;
    private javax.swing.JLabel lblCurrentFolder;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblFolders;
    private javax.swing.JLabel lblFormat;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblKeys;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblNotif;
    protected javax.swing.JLabel lblNumImages;
    private javax.swing.JLabel lblPhoto;
    private javax.swing.JLabel lblThemes;
    private javax.swing.JLabel lblYear;
    private javax.swing.JList<Key> lstKeys;
    private javax.swing.JList<Photo> lstPhotos;
    private javax.swing.JList<Topic> lstTopics;
    private javax.swing.JMenuBar mbr;
    private javax.swing.JMenuItem mniUpdate;
    private javax.swing.JMenuItem mniAbout;
    private javax.swing.JMenuItem mniReloadImages;
    private javax.swing.JMenuItem mniLoadFolders;
    private javax.swing.JMenu mnuFilters;
    private javax.swing.JRadioButtonMenuItem rmiName;
    private javax.swing.JRadioButtonMenuItem rmiKey;
    private javax.swing.JRadioButtonMenuItem rmiTopic;
    private javax.swing.JRadioButtonMenuItem rmiAuthor;
    private javax.swing.JRadioButtonMenuItem rmiYear;
    private javax.swing.JRadioButtonMenuItem rmiMixed;
    private javax.swing.JRadioButtonMenuItem rmiNone;
    private javax.swing.JMenuItem mniPrint;
    private javax.swing.JMenuItem mniExport;
    private javax.swing.JMenu mnuHelp;
    private javax.swing.JMenu mnuTools;
    private javax.swing.JMenu mnuPhoto;
    private javax.swing.JPanel pnl;
    private javax.swing.JPanel pnlBottom;
    private javax.swing.JPanel pnlPhotos;
    private javax.swing.JPanel pnlPhotosData;
    private javax.swing.JScrollPane spDescription;
    private javax.swing.JScrollPane spKeys;
    private javax.swing.JScrollPane spTopics;
    private javax.swing.JToggleButton tbEditar;
    private javax.swing.JTabbedPane tbp;
    private javax.swing.JTextArea txaDescription;
    private javax.swing.JTextField txtName;
    // End of variables declaration

}
