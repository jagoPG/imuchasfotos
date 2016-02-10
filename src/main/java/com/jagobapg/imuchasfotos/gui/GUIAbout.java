/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * GUIAbout.java
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

import com.jagobapg.imuchasfotos.gui.utilities.LanguageController;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Class which will hold information about the developers involved into this
 * software and other related data, license path.
 *
 * Called from gui.imageManager.GUIImageManager
 */
public class GUIAbout extends javax.swing.JDialog {

    private static final long serialVersionUID = 5589819772250797805L;

    /**
     * Creates new form GUIAbout
     *
     * @param parent Parent component.
     * @param modal Focus and hold this window.
     */
    public GUIAbout(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        loadPhoto();
    }

    /* Load JLabel photo. */
    private void loadPhoto() {
        ImageIcon image = new ImageIcon(this.getClass().getClassLoader().getResource("images/jagoba perez.png"));
        Image image_resized = image.getImage().getScaledInstance(149, 160, Image.SCALE_SMOOTH);
        this.lblLogo.setIcon(new ImageIcon(image_resized));
    }

    private void initComponents() {
        pnl = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        txa = new javax.swing.JTextArea();
        btnClose = new javax.swing.JButton();

        setLocation(100, 100);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        pnl.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(204, 204, 204)));

        txa.setColumns(20);
        txa.setLineWrap(true);
        txa.setRows(5);
        txa.setText(LanguageController.INSTANCE.getString("about_descr"));
        txa.setEditable(false);
        txa.setWrapStyleWord(true);

        btnClose.setText(LanguageController.INSTANCE.getString("close"));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlLayout = new javax.swing.GroupLayout(pnl);
        pnl.setLayout(pnlLayout);
        pnlLayout.setHorizontalGroup(
                pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnClose))
                        .addGap(18, 18, 18)
                        .addComponent(txa, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlLayout.setVerticalGroup(
                pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(pnlLayout.createSequentialGroup()
                                        .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnClose)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(pnl);

        pack();
    }

    /* Dispose window. */
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    // Variables declaration
    private javax.swing.JButton btnClose;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JPanel pnl;
    private javax.swing.JTextArea txa;
    // End of variables declaration
}
