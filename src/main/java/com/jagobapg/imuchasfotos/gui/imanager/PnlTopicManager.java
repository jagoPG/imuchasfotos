/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * PnlTopicManager.java
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

import com.jagobapg.imuchasfotos.dto.Topic;
import com.jagobapg.imuchasfotos.sqlite.DBManipulation;
import com.jagobapg.imuchasfotos.sqlite.DBQueries;
import com.jagobapg.imuchasfotos.gui.imanager.GUIImageManager.EGUIManagerState;

/**
 * This class has the topic panel manager. Data related to topics is managed
 * from here. This panel is going to be added into GUIImageManager.java
 */
public class PnlTopicManager extends JPanel {

    private static final long serialVersionUID = 6158919380559722642L;
    private GUIImageManager parent;

    public PnlTopicManager(GUIImageManager parent) {
        this.parent = parent;
        initComponents();
    }

    public void initComponents() {
        pnlTopic = new javax.swing.JPanel();
        jspTopicTM = new javax.swing.JScrollPane();
        lstTopicTM = new javax.swing.JList<Topic>();
        pnlTopicData = new javax.swing.JPanel();
        lblSubtopicName = new javax.swing.JLabel();
        lblTopicName = new javax.swing.JLabel();
        txtTopicName = new javax.swing.JTextField();
        ftxTopicId = new javax.swing.JFormattedTextField(NumberFormat.getNumberInstance());
        btnModifyTopic = new javax.swing.JButton();
        btnNewSubtopic = new javax.swing.JButton();
        txtSubtopicName = new javax.swing.JTextField();
        lblIDTopic = new javax.swing.JLabel();
        btnCancelTopic = new javax.swing.JButton();
        btnSaveTopic = new javax.swing.JButton();
        btnNewTopic = new javax.swing.JButton();
        btnDeleteTopic = new javax.swing.JButton();

        setLayout(new BorderLayout());
        add(pnlTopic, BorderLayout.CENTER);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                pnlTopicComponentHidden(evt);
            }

            public void componentShown(java.awt.event.ComponentEvent evt) {
                pnlTopicComponentShown(evt);
            }
        });

        lstTopicTM.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstTopicTMValueChanged(evt);
            }
        });
        jspTopicTM.setViewportView(lstTopicTM);

        pnlTopicData.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblSubtopicName.setText("Subtema");

        lblTopicName.setText("Tema");

        txtTopicName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTopicNameKeyTyped(evt);
            }
        });

        ftxTopicId.setEditable(false);

        btnModifyTopic.setText("Modificar");
        btnModifyTopic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifyTopicActionPerformed(evt);
            }
        });

        btnNewSubtopic.setText("Nuevo Subtema");
        btnNewSubtopic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewSubtopicActionPerformed(evt);
            }
        });

        txtSubtopicName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSubtopicNameKeyTyped(evt);
            }
        });

        lblIDTopic.setText("Identificador");

        btnCancelTopic.setText("Cancelar");
        btnCancelTopic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelTopicActionPerformed(evt);
            }
        });

        btnSaveTopic.setText("Guardar");
        btnSaveTopic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveTopicActionPerformed(evt);
            }
        });

        btnNewTopic.setText(" Nuevo Tema");
        btnNewTopic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewTopicActionPerformed(evt);
            }
        });

        btnDeleteTopic.setText("Eliminar");
        btnDeleteTopic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteTopicActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlTopicDataLayout = new javax.swing.GroupLayout(pnlTopicData);
        pnlTopicData.setLayout(pnlTopicDataLayout);
        pnlTopicDataLayout.setHorizontalGroup(
                pnlTopicDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlTopicDataLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlTopicDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(pnlTopicDataLayout.createSequentialGroup()
                                        .addComponent(btnNewTopic)
                                        .addGap(2, 2, 2)
                                        .addComponent(btnSaveTopic)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnModifyTopic)
                                        .addGap(3, 3, 3)
                                        .addComponent(btnCancelTopic)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnDeleteTopic))
                                .addGroup(pnlTopicDataLayout.createSequentialGroup()
                                        .addGroup(pnlTopicDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(lblSubtopicName)
                                                .addComponent(lblTopicName)
                                                .addComponent(lblIDTopic))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(pnlTopicDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtTopicName, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(ftxTopicId, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(txtSubtopicName, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(btnNewSubtopic))
                        .addContainerGap(172, Short.MAX_VALUE))
        );
        pnlTopicDataLayout.setVerticalGroup(
                pnlTopicDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlTopicDataLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(pnlTopicDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(ftxTopicId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblIDTopic))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlTopicDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtTopicName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblTopicName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlTopicDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtSubtopicName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblSubtopicName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlTopicDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnNewTopic)
                                .addComponent(btnSaveTopic)
                                .addComponent(btnModifyTopic)
                                .addComponent(btnCancelTopic)
                                .addComponent(btnDeleteTopic))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNewSubtopic)
                        .addGap(226, 226, 226))
        );

        javax.swing.GroupLayout pnlTopicLayout = new javax.swing.GroupLayout(pnlTopic);
        pnlTopic.setLayout(pnlTopicLayout);
        pnlTopicLayout.setHorizontalGroup(
                pnlTopicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlTopicLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jspTopicTM, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlTopicData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
        );
        pnlTopicLayout.setVerticalGroup(
                pnlTopicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlTopicLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(pnlTopicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(pnlTopicData, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jspTopicTM, javax.swing.GroupLayout.PREFERRED_SIZE, 601, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(48, Short.MAX_VALUE))
        );

    }

    /**
     * ************************ Topic TAB. ********************************
     */
    private EGUIManagerState topicManagerState;
    private DefaultListModel<Topic> dlmTopicTM;

    /* Refresh the items behaviour from the tab. */
    private void refreshTopicTab() {
        this.btnNewTopic.setEnabled(topicManagerState == EGUIManagerState.CONSULT);
        this.btnNewSubtopic.setEnabled(topicManagerState == EGUIManagerState.CONSULT && !isSubTopic_lstTopicTM());
        this.btnCancelTopic.setEnabled(topicManagerState != EGUIManagerState.CONSULT);
        this.btnModifyTopic.setEnabled(topicManagerState == EGUIManagerState.CONSULT && !this.dlmTopicTM.isEmpty());
        this.btnSaveTopic.setEnabled(topicManagerState != EGUIManagerState.CONSULT);
        this.btnDeleteTopic.setEnabled(topicManagerState == EGUIManagerState.CONSULT && !this.dlmTopicTM.isEmpty());
        this.lstTopicTM.setEnabled(topicManagerState == EGUIManagerState.CONSULT);

        if (topicManagerState == EGUIManagerState.NEW) {
            // Clear fields
            this.txtTopicName.setEditable(true);
            this.lstTopicTM.setSelectedIndex(-1);
            this.txtTopicName.setText("");
            this.txtSubtopicName.setEditable(false);
        } else if (topicManagerState == EGUIManagerState.NEW_SUBHTHEME) {
            this.txtTopicName.setEditable(false);
            this.txtSubtopicName.setEditable(true);
            this.txtSubtopicName.setText("");
        } else if (topicManagerState == EGUIManagerState.EDIT) {
            if (isSubTopic_lstTopicTM()) {
                this.txtTopicName.setEditable(false);
                this.txtSubtopicName.setEditable(true);
            } else {
                this.txtTopicName.setEditable(true);
                this.txtSubtopicName.setEditable(false);
            }
        } else {
            this.txtTopicName.setEditable(false);
            this.txtSubtopicName.setEditable(false);
        }
    }

    /* Load data from the selected element of the format list. */
    private void loadSelectedTopicData() {
        int index = this.lstTopicTM.getSelectedIndex();

        if (index != -1) {
            // Load information
            Topic t = this.dlmTopicTM.get(index);
            this.ftxTopicId.setValue(t.getId());
            this.txtTopicName.setText(t.getTopic());
            this.txtSubtopicName.setText((t.getSubtopic() == null) ? "" : t.getSubtopic());
        } else {
            this.ftxTopicId.setValue(null);
            this.txtTopicName.setText("");
            this.txtSubtopicName.setText("");
        }
    }

    /**
     * Checks if selected element in the list is a subtopic.
     *
     * @return true if is a subtopic.
     */
    private boolean isSubTopic_lstTopicTM() {
        int index = this.lstTopicTM.getSelectedIndex();

        if (index != -1) {
            Topic topic = this.dlmTopicTM.get(index);
            return (!topic.getSubtopic().equals(""));
        } else {
            return false;
        }
    }

    /* Load topics list. */
    private void loadTopics_lstTopicTM() {
        this.dlmTopicTM.clear();
        Topic[] aTopic = DBQueries.INSTANCE.getTopics();

        for (Topic a : aTopic) {
            this.dlmTopicTM.addElement(a);
        }
    }

    /* Load list and initial state of the tab. */
    private void pnlTopicComponentShown(java.awt.event.ComponentEvent evt) {
        // Fill the list
        this.dlmTopicTM = new DefaultListModel<Topic>();
        this.lstTopicTM.setModel(this.dlmTopicTM);

        loadTopics_lstTopicTM();

        // Set default mode
        if (this.dlmTopicTM.isEmpty()) {
            this.topicManagerState = EGUIManagerState.NEW;
        } else {
            this.topicManagerState = EGUIManagerState.CONSULT;
        }

        if (!this.dlmTopicTM.isEmpty()) {
            // Load default value
            this.lstTopicTM.setSelectedIndex(0);
        }

        refreshTopicTab();
    }

    /* Clear all components. */
    private void pnlTopicComponentHidden(java.awt.event.ComponentEvent evt) {
        this.txtTopicName.setText("");
        this.txtSubtopicName.setText("");

        if (this.dlmTopicTM != null) {
            this.dlmTopicTM.removeAllElements();
        }
    }

    /* Swap to new topic mode. */
    private void btnNewTopicActionPerformed(java.awt.event.ActionEvent evt) {
        // Update state
        this.topicManagerState = EGUIManagerState.NEW;
        refreshTopicTab();
    }

    /* Swap to new subtopic mode. */
    private void btnNewSubtopicActionPerformed(java.awt.event.ActionEvent evt) {
        // Update state
        this.topicManagerState = EGUIManagerState.NEW_SUBHTHEME;
        refreshTopicTab();
    }

    /* Save topic in DB and JList. */
    private void btnSaveTopicActionPerformed(java.awt.event.ActionEvent evt) {
        // Delete useless spaces
        String topic = txtTopicName.getText().trim();

        // Delete useless spaces
        String subtopic = txtSubtopicName.getText().trim();

        // New topic
        if (!topic.equals("") && this.topicManagerState == EGUIManagerState.NEW) {
            if (!DBQueries.INSTANCE.existTopic(topic)) {
                Topic t;

                // Save into database
                DBManipulation.INSTANCE.insertTopic(topic);

                // Load the new item
                t = DBQueries.INSTANCE.getLastInsertedTopic();
                dlmTopicTM.addElement(t);
                lstTopicTM.setSelectedIndex(dlmTopicTM.getSize() - 1);
            } else {
                this.parent.setNotification("Tema ya existente.");
            }
        } else if (!subtopic.equals("") && this.topicManagerState == EGUIManagerState.NEW_SUBHTHEME) {
            // New subtopic
            if (!DBQueries.INSTANCE.existSubtopic(topic, subtopic)) {
                Topic t;

                // Save into database
                DBManipulation.INSTANCE.insertSubtopic(topic, subtopic);

                // Load the new item
                t = DBQueries.INSTANCE.getLastInsertedTopic();
                dlmTopicTM.add(this.lstTopicTM.getSelectedIndex() + 1, t);
                lstTopicTM.setSelectedIndex(dlmTopicTM.getSize() - 1);
            } else {
                this.parent.setNotification("Subtema ya existente.");
            }
        } else if (!topic.equals("") && subtopic.equals("") && this.topicManagerState == EGUIManagerState.EDIT) {
            // Modify topic
            if (!DBQueries.INSTANCE.existTopic(topic)) {
                int index = lstTopicTM.getSelectedIndex();
                Topic t = dlmTopicTM.get(index);

                // Update database
                DBManipulation.INSTANCE.updateTopicName(t.getTopic(), topic, subtopic);

                // Update name from the list
                t.setTopic(topic);
                t.setSubtopic(subtopic);
            } else {
                this.parent.setNotification("Tema ya existente.");
            }
        } else if (!subtopic.equals("") && this.topicManagerState == EGUIManagerState.EDIT) {
            // Modify subtopic
            if (!DBQueries.INSTANCE.existSubtopic(topic, subtopic)) {
                int index = lstTopicTM.getSelectedIndex();
                Topic t = dlmTopicTM.get(index);

                // Update database
                DBManipulation.INSTANCE.updateTopicName(t.getTopic(), topic, subtopic);

                // Update name from the list
                t.setTopic(topic);
                t.setSubtopic(subtopic);
            } else {
                this.parent.setNotification("Subtema ya existente.");
            }
        } else {
            this.parent.setNotification("No ha escrito un tema correcto.");
        }

        // Update status
        this.topicManagerState = EGUIManagerState.CONSULT;
        refreshTopicTab();
        loadSelectedTopicData();
    }

    /* Swap to edit mode. */
    private void btnModifyTopicActionPerformed(java.awt.event.ActionEvent evt) {
        // Update status
        this.topicManagerState = EGUIManagerState.EDIT;
        refreshTopicTab();
    }

    /* Swap to consult mode. */
    private void btnCancelTopicActionPerformed(java.awt.event.ActionEvent evt) {
        // Update status
        this.topicManagerState = EGUIManagerState.CONSULT;
        refreshTopicTab();
        loadSelectedTopicData();
    }

    /* Delete element from the list. */
    private void btnDeleteTopicActionPerformed(java.awt.event.ActionEvent evt) {
        int index = this.lstTopicTM.getSelectedIndex();

        if (index != -1) {
            // Delete item
            Topic t = this.dlmTopicTM.get(index);

            if (isSubTopic_lstTopicTM()) {
                DBManipulation.INSTANCE.deleteSubtopic(t.getId());
            } else {
                String temp;
                Topic subt;

                DBManipulation.INSTANCE.deleteTopic(t.getId(), t.getTopic());

                temp = t.getTopic();

                if (this.dlmTopicTM.size() > 1) {
                    subt = this.dlmTopicTM.get(index + 1);
                    while (index + 1 < this.dlmTopicTM.size() && temp.equals(subt.getTopic())) {
                        this.dlmTopicTM.remove(index + 1);
                        if (index + 2 < this.dlmTopicTM.size()) {
                            subt = this.dlmTopicTM.get(index + 1);
                        }
                    }
                }
            }
            dlmTopicTM.remove(index);
            lstTopicTM.updateUI();

            // Move the pointer of the list and update list
            if (index == 0 && dlmTopicTM.size() > 1) {
                lstTopicTM.setSelectedIndex(index);
                this.topicManagerState = EGUIManagerState.CONSULT;
            } else if (index > 0) {
                lstTopicTM.setSelectedIndex(--index);
                this.topicManagerState = EGUIManagerState.CONSULT;
            } else if (dlmTopicTM.isEmpty()) {
                lstTopicTM.setSelectedIndex(-1);
                this.topicManagerState = EGUIManagerState.NEW;
            }
        }

        // Update status
        this.topicManagerState = EGUIManagerState.CONSULT;
        refreshTopicTab();
    }

    /* Select an item from the list. */
    private void lstTopicTMValueChanged(javax.swing.event.ListSelectionEvent evt) {
        int index = this.lstTopicTM.getSelectedIndex();

        if (index == -1 && !this.dlmTopicTM.isEmpty()) {
            // Load default value
            this.lstTopicTM.setSelectedIndex(0);
        }

        loadSelectedTopicData();
        refreshTopicTab();
    }

    /* Limit of topic characters. */
    private void txtTopicNameKeyTyped(java.awt.event.KeyEvent evt) {
        String txt = this.txtTopicName.getText();

        if (txt.length() > 255) {
            txt = txt.substring(0, 60);
            this.txtTopicName.setText(txt);
            this.parent.setNotification("Caracteres máximos: 50");
        }
    }

    /* Limit of subtopic characters. */
    private void txtSubtopicNameKeyTyped(java.awt.event.KeyEvent evt) {
        String txt = this.txtSubtopicName.getText();
        if (txt.length() > 60) {
            txt = txt.substring(0, 255);
            this.txtSubtopicName.setText(txt);
            this.parent.setNotification("Caracteres máximos: 50");
        }
    }

    private javax.swing.JPanel pnlTopic;
    private javax.swing.JPanel pnlTopicData;
    private javax.swing.JList<Topic> lstTopicTM;
    private javax.swing.JScrollPane jspTopicTM;
    private javax.swing.JLabel lblIDTopic;
    private javax.swing.JFormattedTextField ftxTopicId;
    private javax.swing.JLabel lblTopicName;
    private javax.swing.JTextField txtTopicName;
    private javax.swing.JLabel lblSubtopicName;
    private javax.swing.JTextField txtSubtopicName;
    private javax.swing.JButton btnSaveTopic;
    private javax.swing.JButton btnModifyTopic;
    private javax.swing.JButton btnNewSubtopic;
    private javax.swing.JButton btnNewTopic;
    private javax.swing.JButton btnCancelTopic;
    private javax.swing.JButton btnDeleteTopic;

}
