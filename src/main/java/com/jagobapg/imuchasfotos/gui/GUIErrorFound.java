/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * GUIErrorFound.java
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
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.ImageIcon;

/**
 * JFrame which will show an error message to the user. A descriptive text will
 * be shown, this information is useful for developers to find a solution to an
 * existing problem. Called from anywhere where an error can happen easily.
 */
public class GUIErrorFound extends javax.swing.JFrame {

    private static final long serialVersionUID = -4576642630449287155L;

    /**
     * Creates new form ErrorFound
     */
    public GUIErrorFound(int errorCode, Exception message) {
        String error;

        initComponents();
        this.editLabels();

        error = "Error Code: " + errorCode + "\n" + GUIErrorFound.getErrorMessage(message);
        this.txaError.setText(error);
    }

    private void editLabels() {
        ImageIcon image = new ImageIcon(this.getClass().getClassLoader().getResource("images/dont panic.png"));
        Image image_resized = image.getImage().getScaledInstance(260, 190, Image.SCALE_SMOOTH);
        this.lblImage.setIcon(new ImageIcon(image_resized));

        this.lblText.setText(LanguageController.INSTANCE.getString("unexpected_error"));
    }

    public static String getErrorMessage(Exception ex) {
        if (ex != null) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                return sw.toString();
            } catch (Exception ex2) {
                ex.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private void initComponents() {
        lblImage = new javax.swing.JLabel();
        lblText = new javax.swing.JLabel();
        jspError = new javax.swing.JScrollPane();
        txaError = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(LanguageController.INSTANCE.getString("error_found"));
        setResizable(false);

        lblImage.setPreferredSize(new java.awt.Dimension(260, 190));

        lblText.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        lblText.setText("");

        txaError.setEditable(false);
        txaError.setColumns(20);
        txaError.setRows(5);
        jspError.setViewportView(txaError);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jspError))
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGap(62, 62, 62)
                                                        .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .addComponent(lblText, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(0, 23, Short.MAX_VALUE)))
                        .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblText, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jspError, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
        );

        pack();
    }

    // Variables declaration
    private javax.swing.JScrollPane jspError;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblText;
    private javax.swing.JTextArea txaError;
    // End of variables declaration
}
