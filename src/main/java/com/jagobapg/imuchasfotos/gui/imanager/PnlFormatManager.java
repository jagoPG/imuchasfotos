/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * PnlFormatManager.java
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

import com.jagobapg.imuchasfotos.gui.imanager.GUIImageManager.EGUIManagerState;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;

import com.jagobapg.imuchasfotos.dto.Format;
import com.jagobapg.imuchasfotos.sqlite.DBManipulation;
import com.jagobapg.imuchasfotos.sqlite.DBQueries;

/* This class has the format panel manager. Data related to photo formats is managed from here. This panel is going to be added
 * into GUIImageManager.java */
public class PnlFormatManager extends JPanel {

    private static final long serialVersionUID = 1308065716243163641L;
    private final GUIImageManager parent;

    public PnlFormatManager(GUIImageManager parent) {
        this.parent = parent;
        initComponents();
    }

    public void initComponents() {
        pnlFormat = new javax.swing.JPanel();
        spFormatFM = new javax.swing.JScrollPane();
        lstFormatFM = new javax.swing.JList<Format>();
        pnlFormatData = new javax.swing.JPanel();
        lblFormatID = new javax.swing.JLabel();
        ftxFormatID = new javax.swing.JFormattedTextField(NumberFormat.getNumberInstance());
        lblFormatName = new javax.swing.JLabel();
        txtFormatName = new javax.swing.JTextField();
        btnNewFormat = new javax.swing.JButton();
        btnModifyFormat = new javax.swing.JButton();
        btnSaveFormat = new javax.swing.JButton();
        btnCancelFormat = new javax.swing.JButton();
        btnDeleteFormat = new javax.swing.JButton();

        setLayout(new BorderLayout());
        add(pnlFormat, BorderLayout.CENTER);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                pnlFormatComponentHidden(evt);
            }

            public void componentShown(java.awt.event.ComponentEvent evt) {
                pnlFormatComponentShown(evt);
            }
        });

        lstFormatFM.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstFormatFMValueChanged(evt);
            }
        });
        spFormatFM.setViewportView(lstFormatFM);

        pnlFormatData.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblFormatID.setText("Identificador");

        ftxFormatID.setEditable(false);

        lblFormatName.setText("Nombre");

        txtFormatName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFormatNameKeyTyped(evt);
            }
        });

        btnNewFormat.setText("Nuevo");
        btnNewFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewFormatActionPerformed(evt);
            }
        });

        btnModifyFormat.setText("Modificar");
        btnModifyFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifyFormatActionPerformed(evt);
            }
        });

        btnSaveFormat.setText("Guardar");
        btnSaveFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveFormatActionPerformed(evt);
            }
        });

        btnCancelFormat.setText("Cancelar");
        btnCancelFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelFormatActionPerformed(evt);
            }
        });

        btnDeleteFormat.setText("Eliminar");
        btnDeleteFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteFormatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFormatDataLayout = new javax.swing.GroupLayout(pnlFormatData);
        pnlFormatData.setLayout(pnlFormatDataLayout);
        pnlFormatDataLayout.setHorizontalGroup(
                pnlFormatDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlFormatDataLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlFormatDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(pnlFormatDataLayout.createSequentialGroup()
                                        .addComponent(lblFormatName)
                                        .addGap(41, 41, 41)
                                        .addComponent(txtFormatName, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(pnlFormatDataLayout.createSequentialGroup()
                                        .addComponent(btnNewFormat)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnSaveFormat)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnModifyFormat)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnCancelFormat)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnDeleteFormat)))
                        .addContainerGap(201, Short.MAX_VALUE))
                .addGroup(pnlFormatDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlFormatDataLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblFormatID)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ftxFormatID, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(444, Short.MAX_VALUE)))
        );
        pnlFormatDataLayout.setVerticalGroup(
                pnlFormatDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlFormatDataLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(pnlFormatDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtFormatName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblFormatName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlFormatDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnNewFormat)
                                .addComponent(btnSaveFormat)
                                .addComponent(btnModifyFormat)
                                .addComponent(btnCancelFormat)
                                .addComponent(btnDeleteFormat))
                        .addContainerGap(38, Short.MAX_VALUE))
                .addGroup(pnlFormatDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlFormatDataLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlFormatDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblFormatID)
                                        .addComponent(ftxFormatID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(108, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout pnlFormatLayout = new javax.swing.GroupLayout(pnlFormat);
        pnlFormat.setLayout(pnlFormatLayout);
        pnlFormatLayout.setHorizontalGroup(
                pnlFormatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlFormatLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(spFormatFM, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlFormatData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
        );
        pnlFormatLayout.setVerticalGroup(
                pnlFormatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlFormatLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlFormatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(pnlFormatData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(spFormatFM, javax.swing.GroupLayout.PREFERRED_SIZE, 602, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(46, Short.MAX_VALUE))
        );

    }

    /**
     * ************************ FORMAT TAB. ********************************
     */
    private EGUIManagerState formatManagerState;
    private DefaultListModel<Format> dlmFormatFM;

    /* Refresh the items behaviour from the tab. */
    private void refreshFormatTab() {
        this.btnNewFormat.setEnabled(formatManagerState == EGUIManagerState.CONSULT);
        this.btnCancelFormat.setEnabled(formatManagerState != EGUIManagerState.CONSULT);
        this.btnModifyFormat.setEnabled(formatManagerState == EGUIManagerState.CONSULT && !this.dlmFormatFM.isEmpty());
        this.btnSaveFormat.setEnabled(formatManagerState != EGUIManagerState.CONSULT);
        this.btnDeleteFormat.setEnabled(formatManagerState == EGUIManagerState.CONSULT && !this.dlmFormatFM.isEmpty());
        this.lstFormatFM.setEnabled(formatManagerState == EGUIManagerState.CONSULT);
        this.txtFormatName.setEditable(formatManagerState != EGUIManagerState.CONSULT);

        if (formatManagerState == EGUIManagerState.NEW) {
            // Clear fields
            this.lstFormatFM.setSelectedIndex(-1);
            this.txtFormatName.setText("");
        }
    }

    /* Load data from the selected element of the format list. */
    private void loadSelectedFormatData() {
        int index = this.lstFormatFM.getSelectedIndex();

        if (index != -1) {
            // Load information
            Format f = this.dlmFormatFM.get(index);
            this.ftxFormatID.setValue(f.getId());
            this.txtFormatName.setText(f.getName());
        }
    }

    /* Load components. */
    private void pnlFormatComponentShown(java.awt.event.ComponentEvent evt) {
        // Fill the list
        Format[] aFormat = DBQueries.INSTANCE.getFormats();
        this.dlmFormatFM = new DefaultListModel<Format>();
        this.lstFormatFM.setModel(this.dlmFormatFM);

        for (Format f : aFormat) {
            this.dlmFormatFM.addElement(f);
        }

        // Set default mode
        if (this.dlmFormatFM.isEmpty()) {
            this.formatManagerState = EGUIManagerState.NEW;
        } else {
            this.formatManagerState = EGUIManagerState.CONSULT;
        }

        if (!this.dlmFormatFM.isEmpty()) {
            // Load default value.
            this.lstFormatFM.setSelectedIndex(0);
        }

        refreshFormatTab();
    }

    /* Clear components. */
    private void pnlFormatComponentHidden(java.awt.event.ComponentEvent evt) {
        this.txtFormatName.setText("");

        if (this.dlmFormatFM != null) {
            this.dlmFormatFM.removeAllElements();
        }
    }

    /* Load information of selected format. */
    private void lstFormatFMValueChanged(javax.swing.event.ListSelectionEvent evt) {
        int index = this.lstFormatFM.getSelectedIndex();

        if (index == -1 && !this.dlmFormatFM.isEmpty()) {
            // Load default value
            this.lstFormatFM.setSelectedIndex(0);
        }

        loadSelectedFormatData();
    }

    /* New format. */
    private void btnNewFormatActionPerformed(java.awt.event.ActionEvent evt) {
        // Update state
        this.formatManagerState = EGUIManagerState.NEW;
        refreshFormatTab();
    }

    /* Save changes. */
    private void btnSaveFormatActionPerformed(java.awt.event.ActionEvent evt) {
        // Delete useless spaces
        String name = txtFormatName.getText().trim();

        if (!name.equals("") && this.formatManagerState == EGUIManagerState.NEW) {
            // New format
            if (!DBQueries.INSTANCE.existFormat(name)) {
                Format f;

                // Save into database
                DBManipulation.INSTANCE.insertFormat(name);

                // Load the new item
                f = DBQueries.INSTANCE.getLastInsertedFormat();
                dlmFormatFM.addElement(f);
                lstFormatFM.setSelectedIndex(dlmFormatFM.getSize() - 1);
            } else {
                this.parent.setNotification("Formato ya existente.");
            }
        } else if (!name.equals("") && this.formatManagerState == EGUIManagerState.EDIT) {
            // Modify format
            if (!DBQueries.INSTANCE.existFormat(name)) {
                int index = lstFormatFM.getSelectedIndex();
                Format f = dlmFormatFM.get(index);

                // Update name from the list
                f.setName(name);

                // Update database
                DBManipulation.INSTANCE.updateFormatName(f.getId(), name);
            } else {
                this.parent.setNotification("Formato ya existente.");
            }
        } else {
            this.parent.setNotification("No ha escrito un formato correcto.");
        }

        // Update status
        this.formatManagerState = EGUIManagerState.CONSULT;
        refreshFormatTab();
    }

    /* Change state to modify. */
    private void btnModifyFormatActionPerformed(java.awt.event.ActionEvent evt) {
        // Update state
        this.formatManagerState = EGUIManagerState.EDIT;
        refreshFormatTab();
    }

    /* Change state to cancel. */
    private void btnCancelFormatActionPerformed(java.awt.event.ActionEvent evt) {
        // Update state
        this.formatManagerState = EGUIManagerState.CONSULT;
        refreshFormatTab();
        loadSelectedFormatData();
    }

    /* Delete item from the list and database. */
    private void btnDeleteFormatActionPerformed(java.awt.event.ActionEvent evt) {
        int index = lstFormatFM.getSelectedIndex();

        if (index != -1) {
            // Delete item
            Format f = dlmFormatFM.get(index);
            DBManipulation.INSTANCE.deleteFormat(f.getId());
            this.dlmFormatFM.remove(index);

            // Move the pointer of the list and update list
            if (index == 0 && dlmFormatFM.size() > 1) {
                lstFormatFM.setSelectedIndex(index);
                this.formatManagerState = EGUIManagerState.CONSULT;
            } else if (index > 0) {
                lstFormatFM.setSelectedIndex(--index);
                this.formatManagerState = EGUIManagerState.CONSULT;
            } else if (dlmFormatFM.isEmpty()) {
                this.lstFormatFM.setSelectedIndex(-1);
                this.formatManagerState = EGUIManagerState.NEW;
            }
        }

        // Update state
        refreshFormatTab();
    }

    /* Limit of characters. */
    private void txtFormatNameKeyTyped(java.awt.event.KeyEvent evt) {
        String txt = this.txtFormatName.getText();
        if (txt.length() > 50) {
            txt = txt.substring(0, 50);
            this.txtFormatName.setText(txt);
            this.parent.setNotification("Caracteres máximos: 50");
        }
    }

    private javax.swing.JPanel pnlFormat;
    private javax.swing.JPanel pnlFormatData;
    private javax.swing.JLabel lblFormatID;
    private javax.swing.JFormattedTextField ftxFormatID;
    private javax.swing.JLabel lblFormatName;
    private javax.swing.JTextField txtFormatName;
    private javax.swing.JScrollPane spFormatFM;
    private javax.swing.JList<Format> lstFormatFM;
    private javax.swing.JButton btnSaveFormat;
    private javax.swing.JButton btnNewFormat;
    private javax.swing.JButton btnModifyFormat;
    private javax.swing.JButton btnDeleteFormat;
    private javax.swing.JButton btnCancelFormat;
}
