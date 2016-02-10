/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * RInsertImageLabel.java
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
package com.jagobapg.imuchasfotos.gui.utilities;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * This class inserts a image into a JLabel in a new Thread.
 */
public class RInsertImageLabel implements Runnable {

    private final JLabel lbl;
    private final String src;
    private final int new_width;
    private final int new_height;

    /**
     * Adjust a photo to a JLabel: remove text and resize image.
     *
     * @param lbl Label where the photo is going to be set.
     * @param src Path of the photo.
     * @param new_width New width.
     * @param new_height New height
     */
    public RInsertImageLabel(final JLabel lbl, final String src, final int new_width, final int new_height) {
        this.lbl = lbl;
        this.src = src;
        this.new_width = new_width;
        this.new_height = new_height;
    }

    @Override
    public void run() {
        insertPhotoJLabel(lbl, src, new_width, new_height);
    }

    /**
     * Sets a resized ImageIcon into a JLabel.
     *
     * @param lbl JLabel to be changed.
     * @param src Source of the image.
     * @param new_width desired width.
     * @param new_height desired height.
     */
    private void insertPhotoJLabel(final JLabel lbl, final String src, final int new_width, final int new_height) {
        ImageIcon ii;
        int width, height;

        lbl.setText("");
        ii = new ImageIcon(src);

        width = ii.getIconWidth();
        height = ii.getIconHeight();

        // Initial scaling
        while ((width > new_width && width > 1) && (height > new_height && height > 1)) {
            --width;
            --height;
        }

        // If it has not been enough, a final scale is performed
        if (width > new_width) {
            width = (int) lbl.getSize().getWidth();
        }

        if (height > new_height) {
            height = (int) lbl.getSize().getHeight();
        }

        lbl.setIcon(new ImageIcon(ii.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
    }

}
