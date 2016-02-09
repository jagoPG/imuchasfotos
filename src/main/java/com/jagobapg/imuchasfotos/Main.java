/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * Main.java
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
package com.jagobapg.imuchasfotos;

import com.jagobapg.imuchasfotos.gui.GUIAddFolder;
import com.jagobapg.imuchasfotos.gui.GUIAddImages;
import com.jagobapg.imuchasfotos.gui.imanager.GUIImageManager;

import java.io.File;

import javax.swing.SwingUtilities;

import com.jagobapg.imuchasfotos.sqlite.DBQueries;

/**
 * First class that will be called in the program. It will check that the work
 * environment is created and the needed files exist. After that the initial
 * configuration will be run; select folders for the program. After that photos
 * will be copied to the program folder and added to the database.
 *
 * After a initial configuration the gui.imageManager.GUIImageManager will be
 * called.
 */
public class Main {

    // images storage
    public static final String IMAGES_FOLDER = "images" + File.separatorChar;

    public static void main(String[] args) {
        // create workspace if it does not exist
        File f = new File(IMAGES_FOLDER);
        if (!f.exists()) f.mkdir();

        // if no folders are found, launch the Folder explorer
        if (!DBQueries.INSTANCE.areFolders()) {
            GUIAddFolder gui = new GUIAddFolder(null, true);
            gui.setVisible(true);
        } else {
            GUIAddImages gui = new GUIAddImages(null, true);
            gui.setVisible(true);
        }

        // if there are folders in the database, launch de Image manager
        if (DBQueries.INSTANCE.areFolders()) {
            SwingUtilities.invokeLater(() -> {
                new GUIImageManager().setVisible(true);
            });
        }

    }
}
