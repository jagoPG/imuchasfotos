/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * GUIPhoto.java
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

import com.jagobapg.imuchasfotos.gui.utilities.RConvertQuality;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/*
 * JDialog window which loads a rescalable image to be shown to the user.
 * Called from gui.imageManager.GUIImageManager
 */
public class GUIPhoto extends java.awt.Dialog {

    private static final long serialVersionUID = -3114155956835682295L;

    /**
     * Creates new form GUIPhoto
     *
     * @param parent
     * @param modal
     * @param image_src
     */
    public GUIPhoto(java.awt.Frame parent, boolean modal, String image_src) {
        super(parent, modal);

        ImageIcon ii;
        int x, y;
        TrackPanel pnlImage;

        initComponents();

        // Set up window
        ii = new ImageIcon(image_src);
        x = ii.getIconWidth();
        y = ii.getIconHeight();
        ii = new ImageIcon(RConvertQuality.setMediumQualityResolution(ii));

        setSize(new Dimension(ii.getIconWidth(), ii.getIconHeight()));
        setMaximumSize(new Dimension(x, y));
        this.lblSize.setText("Tamaño original: " + x + "x" + y);

        // Load image
        pnlImage = new TrackPanel(ii.getImage());
        this.add(pnlImage, BorderLayout.CENTER);
    }

    private void initComponents() {
        pnlButton = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblSize = new javax.swing.JLabel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        pnlButton.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        pnlButton.setLayout(new javax.swing.BoxLayout(pnlButton, javax.swing.BoxLayout.LINE_AXIS));

        btnClose.setText("Cerrar");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        pnlButton.add(btnClose);
        pnlButton.add(filler1);

        lblSize.setText("jLabel1");
        pnlButton.add(lblSize);

        add(pnlButton, java.awt.BorderLayout.SOUTH);

        pack();
    }

    /* Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        setVisible(false);
        dispose();
    }

    /* Close window. */
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    // Variables declaration
    private javax.swing.JButton btnClose;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel lblSize;
    private javax.swing.JPanel pnlButton;
    // End of variables declaration

}

/* Source: community.oracle.com. 
 * Panel that holds the image and updates it when a change is made on the window.
 */
class TrackPanel extends JPanel {

    private static final long serialVersionUID = 8972849270123748003L;
    private final Image track;

    public TrackPanel(Image i) {
        this.track = i;
        setPreferredSize(new Dimension(300, 200));
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(track, 0, 0, getWidth(), getHeight(), this);
    }

}
