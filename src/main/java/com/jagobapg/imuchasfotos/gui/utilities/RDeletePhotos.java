/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * RDeletePhotos.java
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

import com.jagobapg.imuchasfotos.dto.Photo;
import com.jagobapg.imuchasfotos.sqlite.DBManipulation;
import com.jagobapg.imuchasfotos.sqlite.DBQueries;

/* This class checks photos that are in the 'image' folder but have been removed
 * from the database. Photos that are not in the database are removed from
 * the 'image' folder. */
public class RDeletePhotos implements Runnable {

    @Override
    public void run() {
        File f;
        Photo[] aPhotos;

        for (int i = 0; i <= DBQueries.INSTANCE.getSizeAllPhotos(); i = i + 50) {
            aPhotos = DBQueries.INSTANCE.getPhotosFromID(50, i);

            for (int index = 0; index < aPhotos.length && aPhotos[index] != null; index++) {
                f = new File(aPhotos[index].getFile());
                if (!f.exists()) {
                    DBManipulation.INSTANCE.deletePhoto(aPhotos[index].getId());
                }
            }
        }
    }

}
