/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * PnlKeyManager.java
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

import com.jagobapg.imuchasfotos.gui.imanager.GUIImageManager.EGUIManagerState;

import java.awt.BorderLayout;
import java.text.NumberFormat;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;

import com.jagobapg.imuchasfotos.dto.Key;
import com.jagobapg.imuchasfotos.sqlite.DBManipulation;
import com.jagobapg.imuchasfotos.sqlite.DBQueries;

/* This class has the key panel manager. Data related to keys is managed from here. This panel is going to be added
 * into GUIImageManager.java */
public class PnlKeyManager extends JPanel {

    private static final long serialVersionUID = -5265874556528594970L;
    private final GUIImageManager parent;

    /**
     *
     * @param parent
     */
    public PnlKeyManager(GUIImageManager parent) {
        this.parent = parent;
        initComponents();
    }

    public void initComponents() {
        pnlKeys = new javax.swing.JPanel();
        spKeysKM = new javax.swing.JScrollPane();
        lstKeysKM = new javax.swing.JList<Key>();
        pnlKeysData = new javax.swing.JPanel();
        lblKeyID = new javax.swing.JLabel();
        ftxKeyID = new javax.swing.JFormattedTextField(NumberFormat.getNumberInstance());
        lblKeyName = new javax.swing.JLabel();
        txtKeyName = new javax.swing.JFormattedTextField();
        btnNewKey = new javax.swing.JButton();
        btnSaveKey = new javax.swing.JButton();
        btnModifyKey = new javax.swing.JButton();
        btnDeleteKey = new javax.swing.JButton();
        btnCancelKey = new javax.swing.JButton();

        setLayout(new BorderLayout());
        add(pnlKeys, BorderLayout.CENTER);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                pnlKeysComponentHidden(evt);
            }

            public void componentShown(java.awt.event.ComponentEvent evt) {
                pnlKeysComponentShown(evt);
            }
        });

        lstKeysKM.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstKeysKMValueChanged(evt);
            }
        });
        spKeysKM.setViewportView(lstKeysKM);

        pnlKeysData.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblKeyID.setText("Identificador");

        ftxKeyID.setEditable(false);

        lblKeyName.setText("Nombre");

        txtKeyName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtKeyNameKeyTyped(evt);
            }
        });

        btnNewKey.setText("Nuevo");
        btnNewKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewKeyActionPerformed(evt);
            }
        });

        btnSaveKey.setText("Guardar");
        btnSaveKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveKeyActionPerformed(evt);
            }
        });

        btnModifyKey.setText("Modificar");
        btnModifyKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifyKeyActionPerformed(evt);
            }
        });

        btnDeleteKey.setText("Eliminar");
        btnDeleteKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteKeyActionPerformed(evt);
            }
        });

        btnCancelKey.setText("Cancelar");
        btnCancelKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelKeyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlKeysDataLayout = new javax.swing.GroupLayout(pnlKeysData);
        pnlKeysData.setLayout(pnlKeysDataLayout);
        pnlKeysDataLayout.setHorizontalGroup(
                pnlKeysDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 586, Short.MAX_VALUE)
                .addGroup(pnlKeysDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlKeysDataLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlKeysDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlKeysDataLayout.createSequentialGroup()
                                                .addGroup(pnlKeysDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblKeyID)
                                                        .addComponent(lblKeyName))
                                                .addGap(18, 18, 18)
                                                .addGroup(pnlKeysDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(ftxKeyID, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtKeyName, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(pnlKeysDataLayout.createSequentialGroup()
                                                .addComponent(btnNewKey)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnSaveKey)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnModifyKey)
                                                .addGap(4, 4, 4)
                                                .addComponent(btnCancelKey)
                                                .addGap(6, 6, 6)
                                                .addComponent(btnDeleteKey)))
                                .addContainerGap(226, Short.MAX_VALUE)))
        );
        pnlKeysDataLayout.setVerticalGroup(
                pnlKeysDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 149, Short.MAX_VALUE)
                .addGroup(pnlKeysDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlKeysDataLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlKeysDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblKeyID)
                                        .addComponent(ftxKeyID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlKeysDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblKeyName)
                                        .addComponent(txtKeyName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlKeysDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnNewKey)
                                        .addComponent(btnSaveKey)
                                        .addComponent(btnModifyKey)
                                        .addComponent(btnCancelKey)
                                        .addComponent(btnDeleteKey))
                                .addContainerGap(21, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout pnlKeysLayout = new javax.swing.GroupLayout(pnlKeys);
        pnlKeys.setLayout(pnlKeysLayout);
        pnlKeysLayout.setHorizontalGroup(
                pnlKeysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlKeysLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(spKeysKM, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlKeysData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
        );
        pnlKeysLayout.setVerticalGroup(
                pnlKeysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlKeysLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlKeysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(pnlKeysData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(spKeysKM, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(44, Short.MAX_VALUE))
        );
    }

    /**
     * ************************ KEY TAB. ********************************
     */
    private EGUIManagerState keyManagerState;
    private DefaultListModel<Key> dlmKeyKM;

    /* Refresh the items behaviour from the tab. */
    private void refreshKeyTab() {
        this.btnNewKey.setEnabled(keyManagerState == EGUIManagerState.CONSULT);
        this.btnCancelKey.setEnabled(keyManagerState != EGUIManagerState.CONSULT);
        this.btnModifyKey.setEnabled(keyManagerState == EGUIManagerState.CONSULT && !this.dlmKeyKM.isEmpty());
        this.btnSaveKey.setEnabled(keyManagerState != EGUIManagerState.CONSULT);
        this.btnDeleteKey.setEnabled(keyManagerState == EGUIManagerState.CONSULT && !this.dlmKeyKM.isEmpty());
        this.lstKeysKM.setEnabled(keyManagerState == EGUIManagerState.CONSULT);
        this.txtKeyName.setEditable(keyManagerState != EGUIManagerState.CONSULT);

        if (keyManagerState == EGUIManagerState.NEW) {
            // Clear fields
            this.lstKeysKM.setSelectedIndex(-1);
            this.txtKeyName.setText("");
        }
    }

    /* Load data from the selected element of the format list. */
    private void loadSelectedKeyData() {
        int index = this.lstKeysKM.getSelectedIndex();

        if (index != -1) {
            // Load information
            Key k = this.dlmKeyKM.get(index);
            this.ftxKeyID.setValue(k.getId());
            this.txtKeyName.setText(k.getName());
        }
    }

    /* Load components: Load JList and state. */
    private void pnlKeysComponentShown(java.awt.event.ComponentEvent evt) {
        // Fill the list
        Key[] aKey = DBQueries.INSTANCE.getKeys();
        this.dlmKeyKM = new DefaultListModel<Key>();
        this.lstKeysKM.setModel(this.dlmKeyKM);

        for (Key a : aKey) {
            this.dlmKeyKM.addElement(a);
        }

        // Set default mode
        if (this.dlmKeyKM.isEmpty()) {
            this.keyManagerState = EGUIManagerState.NEW;
        } else {
            this.keyManagerState = EGUIManagerState.CONSULT;
        }

        if (!this.dlmKeyKM.isEmpty()) {
            // Load default value
            this.lstKeysKM.setSelectedIndex(0);
        }

        refreshKeyTab();
    }

    /* Remove components. */
    private void pnlKeysComponentHidden(java.awt.event.ComponentEvent evt) {
        this.txtKeyName.setText("");

        if (this.dlmKeyKM != null) {
            this.dlmKeyKM.removeAllElements();
        }
    }

    /* Load data from the selected object. */
    private void lstKeysKMValueChanged(javax.swing.event.ListSelectionEvent evt) {
        int index = this.lstKeysKM.getSelectedIndex();

        if (index == -1 && !this.dlmKeyKM.isEmpty()) {
            // Load default value
            this.lstKeysKM.setSelectedIndex(0);
        }

        loadSelectedKeyData();
    }

    /* Swap to new key state. */
    private void btnNewKeyActionPerformed(java.awt.event.ActionEvent evt) {
        // Update status
        this.keyManagerState = EGUIManagerState.NEW;
        refreshKeyTab();
    }

    /* Save key. */
    private void btnSaveKeyActionPerformed(java.awt.event.ActionEvent evt) {
        // Delete useless spaces
        String name = txtKeyName.getText().trim();

        if (!name.equals("") && this.keyManagerState == EGUIManagerState.NEW) {
            // New key
            if (!DBQueries.INSTANCE.existKey(name)) {
                Key a;
                // Save into database
                DBManipulation.INSTANCE.insertKey(name);

                // Load the new item
                a = DBQueries.INSTANCE.getLastInsertedKey();
                dlmKeyKM.addElement(a);
                lstKeysKM.setSelectedIndex(dlmKeyKM.getSize() - 1);
            } else {
                this.parent.setNotification("Clave ya existente.");
            }
        } else if (!name.equals("") && this.keyManagerState == EGUIManagerState.EDIT) {
            // Modify key
            if (!DBQueries.INSTANCE.existKey(name)) {
                int index = lstKeysKM.getSelectedIndex();
                Key a = dlmKeyKM.get(index);

                // Update name from the list
                a.setName(name);

                // Update database.
                DBManipulation.INSTANCE.updateKeyName(a.getId(), name);
            } else {
                this.parent.setNotification("Clave ya existente.");
            }
        } else {
            this.parent.setNotification("No ha escrito una clave correcta.");
        }

        // Update state
        this.keyManagerState = EGUIManagerState.CONSULT;
        refreshKeyTab();
    }

    /* Swap to modify key state. */
    private void btnModifyKeyActionPerformed(java.awt.event.ActionEvent evt) {
        // Update status
        this.keyManagerState = EGUIManagerState.EDIT;
        refreshKeyTab();
    }

    /* Cancel operation. */
    private void btnCancelKeyActionPerformed(java.awt.event.ActionEvent evt) {
        // Update status
        this.keyManagerState = EGUIManagerState.CONSULT;
        refreshKeyTab();
        loadSelectedKeyData();
    }

    /* Delete key from JList and DB. */
    private void btnDeleteKeyActionPerformed(java.awt.event.ActionEvent evt) {
        int index = lstKeysKM.getSelectedIndex();

        if (index != -1) {
            // Delete item
            Key a = dlmKeyKM.get(index);
            DBManipulation.INSTANCE.deleteKey(a.getId());
            this.dlmKeyKM.remove(index);

            // Move the pointer of the list and update list
            if (index == 0 && dlmKeyKM.size() > 1) {
                lstKeysKM.setSelectedIndex(index);
                this.keyManagerState = EGUIManagerState.CONSULT;
            } else if (index > 0) {
                lstKeysKM.setSelectedIndex(--index);
                this.keyManagerState = EGUIManagerState.CONSULT;
            } else if (dlmKeyKM.isEmpty()) {
                this.lstKeysKM.setSelectedIndex(-1);
                this.keyManagerState = EGUIManagerState.NEW;
            }
        }

        // Update status
        refreshKeyTab();
    }

    /* Limit of characters. */
    private void txtKeyNameKeyTyped(java.awt.event.KeyEvent evt) {
        String txt = this.txtKeyName.getText();
        if (txt.length() > 50) {
            txt = txt.substring(0, 50);
            this.txtKeyName.setText(txt);
            this.parent.setNotification("Caracteres máximos: 50");
        }
    }

    private javax.swing.JLabel lblKeyID;
    private javax.swing.JFormattedTextField ftxKeyID;
    private javax.swing.JLabel lblKeyName;
    private javax.swing.JFormattedTextField txtKeyName;
    private javax.swing.JScrollPane spKeysKM;
    private javax.swing.JList<Key> lstKeysKM;
    private javax.swing.JPanel pnlKeys;
    private javax.swing.JPanel pnlKeysData;
    private javax.swing.JButton btnSaveKey;
    private javax.swing.JButton btnNewKey;
    private javax.swing.JButton btnModifyKey;
    private javax.swing.JButton btnDeleteKey;
    private javax.swing.JButton btnCancelKey;
}