/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * RDeleteFolder.java
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

import com.jagobapg.imuchasfotos.dto.Folder;
import com.jagobapg.imuchasfotos.sqlite.DBManipulation;
import com.jagobapg.imuchasfotos.sqlite.DBQueries;

/**
 * This class is called from gui.GUIImageManager.java. Removes temporal folders.
 */
public class RDeleteFolders implements Runnable {

    @Override
    public void run() {
        Folder[] folders = DBQueries.INSTANCE.getFolders();
        File folder;

        for (Folder f : folders) {
            folder = new File(f.getName());

            if (!folder.exists()) {
                DBManipulation.INSTANCE.deletePhotosFromDir(f.getId());
                DBManipulation.INSTANCE.deleteFolder(f.getId());
                OSOperations.removeDirectory(folder.getAbsolutePath());
            }
        }

    }

}
