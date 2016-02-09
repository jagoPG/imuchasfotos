/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * GUIYearFilter.java
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/* This class provides the UI of the image filter by year. */
public class GUIYearFilter extends java.awt.Dialog {

    private static final long serialVersionUID = 6092938224223407821L;
    private EGUIFilters status;

    /**
     * Sets up the window but, its hided.
     *
     * @param parent
     */
    public GUIYearFilter(java.awt.Frame parent) {
        super(parent, true);
        initComponents();

        this.lblNotif.setText("");
    }

    private void initComponents() {
        pnlContent = new javax.swing.JPanel();
        lblDescr = new javax.swing.JLabel();
        txtYear = new javax.swing.JTextField();
        pnlButton = new javax.swing.JPanel();
        btnApply = new javax.swing.JButton();
        lblNotif = new javax.swing.JLabel();

        setResizable(false);
        setTitle("Filtro por año");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        lblDescr.setText("Filtrar imágenes por año.");
        pnlContent.add(lblDescr);

        txtYear.setMaximumSize(new java.awt.Dimension(200, 20));
        txtYear.setPreferredSize(new java.awt.Dimension(200, 20));
        txtYear.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNameFocusLost(evt);
            }
        });
        pnlContent.add(txtYear);

        add(pnlContent, java.awt.BorderLayout.CENTER);

        pnlButton.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        btnApply.setText("Aplicar");
        btnApply.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnApply.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnApplyActionListener(e);
            }

        });
        pnlButton.add(btnApply);

        lblNotif.setForeground(new java.awt.Color(255, 0, 0));
        lblNotif.setText("jLabel1");
        pnlButton.add(lblNotif);

        add(pnlButton, java.awt.BorderLayout.SOUTH);

        pack();
    }

    /* Hide the dialog. */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        this.status = EGUIFilters.WINDOW_CLOSED;
        setVisible(false);
    }

    /* Year constraints. */
    private void txtNameFocusLost(java.awt.event.FocusEvent evt) {
        String txt = this.txtYear.getText();

        try {
            Integer.parseInt(txt);
        } catch (NumberFormatException ex) {
            Timer t;
            this.txtYear.setText("");

            this.lblNotif.setText("Solo caracteres numéricos admitidos.");
            t = new Timer(5000, new ActionListener() { //Wait 5 sec. and delete notification.

                @Override
                public void actionPerformed(ActionEvent e) {
                    lblNotif.setText("");
                }

            });

            t.start();
        }

    }

    /* Apply filter. */
    private void btnApplyActionListener(ActionEvent e) {
        this.status = EGUIFilters.APPLIED;
        this.setVisible(false);
    }

    /**
     * Access to the name of the filter when the window is hided.
     *
     * @return name of the filter.
     */
    public String getFilterName() {
        return this.txtYear.getText();
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
    private javax.swing.JLabel lblDescr;
    private javax.swing.JLabel lblNotif;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JPanel pnlContent;
    private javax.swing.JTextField txtYear;
    // End of variables declaration
}
