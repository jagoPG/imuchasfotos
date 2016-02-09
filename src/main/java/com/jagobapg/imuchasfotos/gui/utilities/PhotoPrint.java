/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * PhotoPrint.java
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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import javax.swing.ImageIcon;

/**
 * This class manages the printing process of a photo.
 */
public class PhotoPrint implements Printable {

    private Image photo;

    public PhotoPrint(Image photo) {
        this.photo = photo;
    }

    public PhotoPrint(String path) {
        this.photo = new ImageIcon(path).getImage();
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
            throws PrinterException {
        ImageIcon ii;
        int width, height, new_width, new_height;

        // Fit photo to the page
        ii = new ImageIcon(photo);

        width = ii.getIconWidth();
        height = ii.getIconHeight();
        new_width = (int) pageFormat.getWidth();
        new_height = (int) pageFormat.getHeight();

        // Initial scaling
        while ((width > new_width && width > 1) && (height > new_height && height > 1)) {
            --width;
            --height;
        }

        // If it has not been enough, a final scale is performed
        if (width > new_width) {
            width = (int) new_width;
        }

        if (height > new_height) {
            height = (int) new_height;
        }

        if (pageIndex == 0) {
            graphics.drawImage(photo, 0, 0, width, height, null);
            return PAGE_EXISTS;
        } else {
            return NO_SUCH_PAGE;
        }

    }

}
