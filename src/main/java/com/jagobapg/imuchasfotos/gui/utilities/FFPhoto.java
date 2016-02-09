/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * FFPhoto.java
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

import java.io.File;

import javax.swing.filechooser.FileFilter;

/* This class sets the available photo formats. */
public class FFPhoto extends FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.getName().contains(".")) {
            String ext = f.getName().substring(f.getName().indexOf("."));
            if ((ext.equalsIgnoreCase(".jpg") || ext.equalsIgnoreCase(".jpeg")
                    || ext.equalsIgnoreCase(".gif") || ext.equalsIgnoreCase(".png"))) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Photo filter";
    }

}
