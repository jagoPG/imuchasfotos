
/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * PnlAuthorManager.java
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
package com.jagobapg.imuchasfotos.gui.imanager;

import java.awt.BorderLayout;
import java.text.NumberFormat;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;

import com.jagobapg.imuchasfotos.dto.Author;
import com.jagobapg.imuchasfotos.sqlite.DBManipulation;
import com.jagobapg.imuchasfotos.sqlite.DBQueries;
import com.jagobapg.imuchasfotos.gui.imanager.GUIImageManager.EGUIManagerState;

/* This class has the author panel manager. Data related to authors is managed from here. This panel is going to be added
 * into GUIImageManager.java */
public class PnlAuthorManager extends JPanel {

    private static final long serialVersionUID = -3462644760325413551L;
    private GUIImageManager parent;

    public PnlAuthorManager(GUIImageManager parent) {
        this.parent = parent;
        initComponents();
    }

    public void initComponents() {
        pnlAuthors = new javax.swing.JPanel();
        spAuthorsAM = new javax.swing.JScrollPane();
        lstAuthorsAM = new javax.swing.JList<Author>();
        pnlAuthorData = new javax.swing.JPanel();
        lblAuthorID = new javax.swing.JLabel();
        ftxAuthorID = new javax.swing.JFormattedTextField(NumberFormat.getNumberInstance());
        lblAuthorName = new javax.swing.JLabel();
        txtAuthorName = new javax.swing.JTextField();
        btnNewAuthor = new javax.swing.JButton();
        btnSaveAuthor = new javax.swing.JButton();
        btnModifyAuthor = new javax.swing.JButton();
        btnCancelAuthor = new javax.swing.JButton();
        btnDeleteAuthor = new javax.swing.JButton();

        setLayout(new BorderLayout());
        add(pnlAuthors, BorderLayout.CENTER);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                pnlAuthorsComponentHidden(evt);
            }

            public void componentShown(java.awt.event.ComponentEvent evt) {
                pnlAuthorsComponentShown(evt);
            }
        });

        lstAuthorsAM.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstAuthorsAMValueChanged(evt);
            }
        });
        spAuthorsAM.setViewportView(lstAuthorsAM);

        pnlAuthorData.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblAuthorID.setText("Identificador");

        ftxAuthorID.setEditable(false);

        lblAuthorName.setText("Nombre");

        txtAuthorName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAuthorNameKeyTyped(evt);
            }
        });

        btnNewAuthor.setText("Nuevo");
        btnNewAuthor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewAuthorActionPerformed(evt);
            }
        });

        btnSaveAuthor.setText("Guardar");
        btnSaveAuthor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveAuthorActionPerformed(evt);
            }
        });

        btnModifyAuthor.setText("Modificar");
        btnModifyAuthor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifyAuthorActionPerformed(evt);
            }
        });

        btnCancelAuthor.setText("Cancelar");
        btnCancelAuthor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelAuthorActionPerformed(evt);
            }
        });

        btnDeleteAuthor.setText("Eliminar");
        btnDeleteAuthor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteAuthorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlAuthorDataLayout = new javax.swing.GroupLayout(pnlAuthorData);
        pnlAuthorData.setLayout(pnlAuthorDataLayout);
        pnlAuthorDataLayout.setHorizontalGroup(
                pnlAuthorDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlAuthorDataLayout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(txtAuthorName, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(244, Short.MAX_VALUE))
                .addGroup(pnlAuthorDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlAuthorDataLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlAuthorDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlAuthorDataLayout.createSequentialGroup()
                                                .addComponent(lblAuthorID)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ftxAuthorID, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(lblAuthorName)
                                        .addGroup(pnlAuthorDataLayout.createSequentialGroup()
                                                .addComponent(btnNewAuthor)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnSaveAuthor)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnModifyAuthor)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnCancelAuthor)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnDeleteAuthor)))
                                .addContainerGap(225, Short.MAX_VALUE)))
        );
        pnlAuthorDataLayout.setVerticalGroup(
                pnlAuthorDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlAuthorDataLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(txtAuthorName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(72, Short.MAX_VALUE))
                .addGroup(pnlAuthorDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlAuthorDataLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlAuthorDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblAuthorID)
                                        .addComponent(ftxAuthorID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(16, 16, 16)
                                .addComponent(lblAuthorName)
                                .addGap(18, 18, 18)
                                .addGroup(pnlAuthorDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnNewAuthor)
                                        .addComponent(btnSaveAuthor)
                                        .addComponent(btnModifyAuthor)
                                        .addComponent(btnCancelAuthor)
                                        .addComponent(btnDeleteAuthor))
                                .addContainerGap(28, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout pnlAuthorsLayout = new javax.swing.GroupLayout(pnlAuthors);
        pnlAuthors.setLayout(pnlAuthorsLayout);
        pnlAuthorsLayout.setHorizontalGroup(
                pnlAuthorsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlAuthorsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(spAuthorsAM, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlAuthorData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
        );
        pnlAuthorsLayout.setVerticalGroup(
                pnlAuthorsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlAuthorsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlAuthorsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(pnlAuthorData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(spAuthorsAM, javax.swing.GroupLayout.PREFERRED_SIZE, 602, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(46, Short.MAX_VALUE))
        );

    }

    /**
     * ************************ AUTHOR TAB. ********************************
     */
    private EGUIManagerState authorManagerState;
    private DefaultListModel<Author> dlmAuthorAM;

    /* Refresh the items behaviour from the tab. */
    private void refreshAuthorTab() {
        this.btnNewAuthor.setEnabled(authorManagerState == EGUIManagerState.CONSULT);
        this.btnCancelAuthor.setEnabled(authorManagerState != EGUIManagerState.CONSULT);
        this.btnModifyAuthor.setEnabled(authorManagerState == EGUIManagerState.CONSULT && !this.dlmAuthorAM.isEmpty());
        this.btnSaveAuthor.setEnabled(authorManagerState != EGUIManagerState.CONSULT);
        this.btnDeleteAuthor.setEnabled(authorManagerState == EGUIManagerState.CONSULT && !this.dlmAuthorAM.isEmpty());
        this.lstAuthorsAM.setEnabled(authorManagerState == EGUIManagerState.CONSULT);
        this.txtAuthorName.setEditable(authorManagerState != EGUIManagerState.CONSULT);

        if (authorManagerState == EGUIManagerState.NEW) {
            // Clear fields
            this.lstAuthorsAM.setSelectedIndex(-1);
            this.txtAuthorName.setText("");
        }
    }

    /* Load data from the selected element of the format list. */
    private void loadSelectedAuthorData() {
        int index = this.lstAuthorsAM.getSelectedIndex();

        if (index != -1) {
            // Load information
            Author a = this.dlmAuthorAM.get(index);
            this.ftxAuthorID.setValue(a.getId());
            this.txtAuthorName.setText(a.getName());
        }
    }

    /* Load author's panel items: JList and state. */
    private void pnlAuthorsComponentShown(java.awt.event.ComponentEvent evt) {
        // Fill the list
        Author[] aAuthor = DBQueries.INSTANCE.getAuthors();
        this.dlmAuthorAM = new DefaultListModel<Author>();
        this.lstAuthorsAM.setModel(this.dlmAuthorAM);

        for (Author a : aAuthor) {
            this.dlmAuthorAM.addElement(a);
        }

        // Set default mode
        if (this.dlmAuthorAM.isEmpty()) {
            this.authorManagerState = EGUIManagerState.NEW;
        } else {
            this.authorManagerState = EGUIManagerState.CONSULT;
        }

        if (!this.dlmAuthorAM.isEmpty()) {
            // Load default value
            this.lstAuthorsAM.setSelectedIndex(0);
        }

        refreshAuthorTab();
    }

    /* Clear components. */
    private void pnlAuthorsComponentHidden(java.awt.event.ComponentEvent evt) {
        this.txtAuthorName.setText("");

        if (this.dlmAuthorAM != null) {
            this.dlmAuthorAM.removeAllElements();
        }
    }

    /* Load selected author. */
    private void lstAuthorsAMValueChanged(javax.swing.event.ListSelectionEvent evt) {
        int index = this.lstAuthorsAM.getSelectedIndex();

        if (index == -1 && !this.dlmAuthorAM.isEmpty()) {
            // Load default value
            this.lstAuthorsAM.setSelectedIndex(0);
        }

        loadSelectedAuthorData();
    }

    /* New author. */
    private void btnNewAuthorActionPerformed(java.awt.event.ActionEvent evt) {
        // Update state
        this.authorManagerState = EGUIManagerState.NEW;
        refreshAuthorTab();
    }

    /* Save author. */
    private void btnSaveAuthorActionPerformed(java.awt.event.ActionEvent evt) {
        // Delete useless spaces
        String name = txtAuthorName.getText().trim();

        // New author
        if (!name.equals("") && this.authorManagerState == EGUIManagerState.NEW) {
            if (!DBQueries.INSTANCE.existAuthor(name)) {
                Author a;

                // Save into database
                DBManipulation.INSTANCE.insertAuthor(name);

                // Load the new item
                a = DBQueries.INSTANCE.getLastInsertedAuthor();
                dlmAuthorAM.addElement(a);
                lstAuthorsAM.setSelectedIndex(dlmAuthorAM.getSize() - 1);
            } else {
                this.parent.setNotification("Autor ya existente.");
            }
        } else if (!name.equals("") && this.authorManagerState == EGUIManagerState.EDIT) {
            // Modify author
            if (!DBQueries.INSTANCE.existAuthor(name)) {
                int index = lstAuthorsAM.getSelectedIndex();
                Author a = dlmAuthorAM.get(index);

                // Update name from the list
                a.setName(name);

                // Update database
                DBManipulation.INSTANCE.updateAuthorName(a.getId(), name);
            } else {
                this.parent.setNotification("Autor ya existente.");
            }
        } else {
            this.parent.setNotification("No ha escrito un autor correcto.");
        }

        // Update state
        this.authorManagerState = EGUIManagerState.CONSULT;
        refreshAuthorTab();
    }

    /* Update author information. */
    private void btnModifyAuthorActionPerformed(java.awt.event.ActionEvent evt) {
        // Update state
        this.authorManagerState = EGUIManagerState.EDIT;
        refreshAuthorTab();
    }

    /* Cancel operation. */
    private void btnCancelAuthorActionPerformed(java.awt.event.ActionEvent evt) {
        // Update state
        this.authorManagerState = EGUIManagerState.CONSULT;
        refreshAuthorTab();
        loadSelectedAuthorData();
    }

    /* Delete author from DB and list. */
    private void btnDeleteAuthorActionPerformed(java.awt.event.ActionEvent evt) {
        int index = lstAuthorsAM.getSelectedIndex();

        if (index != -1) {
            // Delete item
            Author a = dlmAuthorAM.get(index);
            DBManipulation.INSTANCE.deleteAuthor(a.getId());
            this.dlmAuthorAM.remove(index);

            // Move the pointer of the list and update list
            if (index == 0 && dlmAuthorAM.size() > 1) {
                lstAuthorsAM.setSelectedIndex(index);
                this.authorManagerState = EGUIManagerState.CONSULT;
            } else if (index > 0) {
                lstAuthorsAM.setSelectedIndex(--index);
                this.authorManagerState = EGUIManagerState.CONSULT;
            } else if (dlmAuthorAM.isEmpty()) {
                this.lstAuthorsAM.setSelectedIndex(-1);
                this.authorManagerState = EGUIManagerState.NEW;
            }
        }

        // Update state
        refreshAuthorTab();
    }

    /* Limit of characters. */
    private void txtAuthorNameKeyTyped(java.awt.event.KeyEvent evt) {
        String txt = this.txtAuthorName.getText();

        if (txt.length() > 60) {
            txt = txt.substring(0, 60);
            this.txtAuthorName.setText(txt);
            this.parent.setNotification("Caracteres máximos: 50");
        }
    }

    private javax.swing.JPanel pnlAuthorData;
    private javax.swing.JPanel pnlAuthors;
    private javax.swing.JLabel lblAuthorID;
    private javax.swing.JFormattedTextField ftxAuthorID;
    private javax.swing.JLabel lblAuthorName;
    private javax.swing.JTextField txtAuthorName;
    private javax.swing.JList<Author> lstAuthorsAM;
    private javax.swing.JScrollPane spAuthorsAM;
    private javax.swing.JButton btnSaveAuthor;
    private javax.swing.JButton btnNewAuthor;
    private javax.swing.JButton btnDeleteAuthor;
    private javax.swing.JButton btnModifyAuthor;
    private javax.swing.JButton btnCancelAuthor;
}
