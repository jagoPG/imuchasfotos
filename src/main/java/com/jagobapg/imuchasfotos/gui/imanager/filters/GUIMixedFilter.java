/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * GUIMixedFilter.java
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

import com.jagobapg.imuchasfotos.gui.imanager.ImageManagerFilter.EFilter;
import com.jagobapg.imuchasfotos.gui.utilities.LanguageController;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import com.jagobapg.imuchasfotos.sqlite.DBQueries;

/* This class provides the UI of the image filter by several filters. */
public class GUIMixedFilter extends java.awt.Dialog {

    private static final long serialVersionUID = -7954788825118590295L;
    private EGUIFilters status;

    private static final String TOPIC = LanguageController.INSTANCE.getString("topic");
    private static final String KEY = LanguageController.INSTANCE.getString("key");
    private static final String AUTHOR = LanguageController.INSTANCE.getString("author");
    private static final String AND = LanguageController.INSTANCE.getString("and");
    private static final String OR = LanguageController.INSTANCE.getString("or");

    private void initComponents() {
        pnlContent = new javax.swing.JPanel();
        cbxClause1 = new javax.swing.JComboBox<Object>();
        cbxClause1_value = new javax.swing.JComboBox<Object>();
        cbxAndOr = new javax.swing.JComboBox<Object>();
        cbxClause2 = new javax.swing.JComboBox<Object>();
        cbxClause2_value = new javax.swing.JComboBox<Object>();
        pnlButton = new javax.swing.JPanel();
        btnApply = new javax.swing.JButton(LanguageController.INSTANCE.getString("apply"));
        lblNotif = new javax.swing.JLabel("");

        setResizable(false);
        setTitle(LanguageController.INSTANCE.getString("mixed_filter"));
        setSize(new Dimension(800, 100));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        pnlContent.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnlContent.setLayout(new javax.swing.BoxLayout(pnlContent, javax.swing.BoxLayout.LINE_AXIS));

        cbxClause1.setModel(new javax.swing.DefaultComboBoxModel<Object>(new String[]{TOPIC, KEY, AUTHOR}));
        pnlContent.add(cbxClause1);

        cbxClause1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                loadComboBox1();
            }

        });

        pnlContent.add(cbxClause1_value);

        cbxAndOr.setModel(new javax.swing.DefaultComboBoxModel<Object>(new String[]{AND, OR}));
        pnlContent.add(cbxAndOr);

        cbxClause2.setModel(new javax.swing.DefaultComboBoxModel<Object>(new String[]{TOPIC, KEY, AUTHOR}));
        pnlContent.add(cbxClause2);

        cbxClause2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                loadComboBox2();

            }

        });

        pnlContent.add(cbxClause2_value);

        add(pnlContent, java.awt.BorderLayout.CENTER);

        pnlButton.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        btnApply.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnApply.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnApplyActionListener(e);
            }

        });
        pnlButton.add(btnApply);

        lblNotif.setForeground(new java.awt.Color(255, 0, 0));
        pnlButton.add(lblNotif);

        add(pnlButton, java.awt.BorderLayout.SOUTH);

        loadComboBox1();
        loadComboBox2();
    }

    /**
     * Load ComboBox of first search clause
     */
    private void loadComboBox1() {
        String value = (String) cbxClause1.getSelectedItem();

        if (value.equals(TOPIC)) {
            cbxClause1_value.setModel(new javax.swing.DefaultComboBoxModel<Object>(DBQueries.INSTANCE.getTopics()));
        } else if (value.equals(KEY)) {
            cbxClause1_value.setModel(new javax.swing.DefaultComboBoxModel<Object>(DBQueries.INSTANCE.getKeys()));
        } else if (value.equals(AUTHOR)) {
            cbxClause1_value.setModel(new javax.swing.DefaultComboBoxModel<Object>(DBQueries.INSTANCE.getAuthors()));
        }
    }

    /**
     * Load ComboBox of second search clause
     */
    private void loadComboBox2() {
        String value = (String) cbxClause2.getSelectedItem();

        if (value.equals(TOPIC)) {
            cbxClause2_value.setModel(new javax.swing.DefaultComboBoxModel<Object>(DBQueries.INSTANCE.getTopics()));
        } else if (value.equals(KEY)) {
            cbxClause2_value.setModel(new javax.swing.DefaultComboBoxModel<Object>(DBQueries.INSTANCE.getKeys()));
        } else if (value.equals(AUTHOR)) {
            cbxClause2_value.setModel(new javax.swing.DefaultComboBoxModel<Object>(DBQueries.INSTANCE.getAuthors()));
        }
    }

    /**
     * Sets up the window but, its hided
     *
     * @param parent
     */
    public GUIMixedFilter(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
    }

    /**
     * Hide the dialog
     *
     * @param evt WindowEvent
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        this.status = EGUIFilters.WINDOW_CLOSED;
        setVisible(false);
    }

    /**
     * Apply search filter
     *
     * @param e ActionEvent
     */
    private void btnApplyActionListener(ActionEvent e) {
        if (this.cbxClause1_value.getSelectedItem() != null && this.cbxClause2_value.getSelectedItem() != null) {
            this.status = EGUIFilters.APPLIED;
            this.setVisible(false);
        } else {
            Timer t;
            this.lblNotif.setText(LanguageController.INSTANCE.getString("values_must_select"));
            t = new Timer(5000, new ActionListener() { //Wait 5 sec. and delete notification.

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
     *
     * @return name of the filter.
     */
    public Object getFilter1() {
        return this.cbxClause1_value.getSelectedItem();
    }

    /**
     * Access to the name of the filter when the window is hided.
     *
     * @return name of the filter.
     */
    public Object getFilter2() {
        return this.cbxClause2_value.getSelectedItem();
    }

    /**
     * Access to the name of the filter when the window is hided.
     *
     * @return and/or filter.
     */
    public EFilter getAndOr() {
        if (this.cbxAndOr.getSelectedItem().equals(AND)) {
            return EFilter.AND;
        } else {
            return EFilter.OR;
        }
    }

    /**
     * Get window status. Window status is used for knowing if the window has
     * been closed or the filter button has been pressed.
     *
     * @return status
     */
    public EGUIFilters getStatus() {
        return this.status;
    }
    // Variables declaration
    private javax.swing.JButton btnApply;
    private javax.swing.JComboBox<Object> cbxAndOr;
    private javax.swing.JComboBox<Object> cbxClause1;
    private javax.swing.JComboBox<Object> cbxClause1_value;
    private javax.swing.JComboBox<Object> cbxClause2;
    private javax.swing.JComboBox<Object> cbxClause2_value;
    private javax.swing.JLabel lblNotif;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JPanel pnlContent;
    // End of variables declaration
}
