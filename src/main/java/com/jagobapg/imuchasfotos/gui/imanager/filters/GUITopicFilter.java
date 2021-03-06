/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * GUITopicFilter.java
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
package com.jagobapg.imuchasfotos.gui.imanager.filters;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Timer;

import com.jagobapg.imuchasfotos.dto.Topic;
import com.jagobapg.imuchasfotos.gui.utilities.LanguageController;
import com.jagobapg.imuchasfotos.sqlite.DBQueries;

/* This class provides the UI of the image filter by name. */
public class GUITopicFilter extends java.awt.Dialog {

    private static final long serialVersionUID = 1824605499456728267L;
    private EGUIFilters status;

    /**
     * Sets up the window but, its hided.
     *
     * @param parent
     */
    public GUITopicFilter(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
    }

    private void initComponents() {
        DefaultComboBoxModel<Topic> dcm;

        pnlContent = new javax.swing.JPanel();
        lblDescr = new javax.swing.JLabel(LanguageController.INSTANCE.getString("filter_images_by_topic"));
        cbxTopic = new javax.swing.JComboBox<Topic>();
        pnlButton = new javax.swing.JPanel();
        btnApply = new javax.swing.JButton(LanguageController.INSTANCE.getString("apply"));
        lblNotif = new javax.swing.JLabel("");

        setResizable(false);
        setTitle(LanguageController.INSTANCE.getString("filter_by_topic"));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        pnlContent.add(lblDescr);

        cbxTopic.setPreferredSize(new java.awt.Dimension(210, 20));
        dcm = new DefaultComboBoxModel<Topic>(DBQueries.INSTANCE.getTopics());
        cbxTopic.setModel(dcm);
        pnlContent.add(cbxTopic);

        add(pnlContent, java.awt.BorderLayout.CENTER);

        pnlButton.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        btnApply.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnApplyActionListener(e);
            }

        });

        btnApply.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        pnlButton.add(btnApply);

        lblNotif.setForeground(Color.RED);
        pnlButton.add(lblNotif);

        add(pnlButton, java.awt.BorderLayout.SOUTH);

        pack();
    }

    /**
     * Hide the dialog
     * @param evt WindowEvent
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        this.status = EGUIFilters.WINDOW_CLOSED;
        setVisible(false);
    }

    /**
     * Apply filter
     * @param e  ActionEvent
     */
    private void btnApplyActionListener(ActionEvent e) {
        if (this.cbxTopic.getSelectedItem() != null) {
            this.status = EGUIFilters.APPLIED;
            this.setVisible(false);
        } else {
            this.lblNotif.setText(LanguageController.INSTANCE.getString("select_topic"));
            Timer t = new Timer(5000, new ActionListener() { //Wait 5 sec. and delete notification.

                @Override
                public void actionPerformed(ActionEvent e) {
                    lblNotif.setText("");
                }

            });

            t.start();
        }
    }

    /**
     * Access to the name of the filter when the window is hided.
     * @return name of the filter.
     */
    public Topic getFilterName() {
        return (Topic) this.cbxTopic.getSelectedItem();
    }

    /**
     * Get window status. Window status is used for knowing if the window has
     * been closed or the filter button has been pressed.
     * @return status
     */
    public EGUIFilters getStatus() {
        return this.status;
    }
    // Variables declaration
    private javax.swing.JButton btnApply;
    private javax.swing.JLabel lblDescr;
    private javax.swing.JLabel lblNotif;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JPanel pnlContent;
    private javax.swing.JComboBox<Topic> cbxTopic;
    // End of variables declaration
}
