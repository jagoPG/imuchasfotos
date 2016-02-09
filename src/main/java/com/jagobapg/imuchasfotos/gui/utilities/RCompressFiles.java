/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * RCompressFiles.java
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.jagobapg.imuchasfotos.dto.Log;

/**
 * This class manages the compress process done in gui.GUIExportImages.java
 */
public class RCompressFiles implements Runnable {

    private String zipFileName;
    private File[] aFiles;

    public RCompressFiles(String zipFileName, File[] aFiles) {
        this.zipFileName = zipFileName;
        this.aFiles = aFiles;
    }

    @Override
    public void run() {
        FileOutputStream fos;
        ZipOutputStream zos;

        try {
            fos = new FileOutputStream("temp" + File.separatorChar + zipFileName);
            zos = new ZipOutputStream(fos);

            for (File image : aFiles) {
                if (image.exists()) {
                    ZipEntry ze;
                    FileInputStream fis;
                    byte[] buffer;
                    int len;

                    // new entry in the archive
                    ze = new ZipEntry(image.getName());
                    zos.putNextEntry(ze);
                    fis = new FileInputStream(image);

                    buffer = new byte[1024];
                    while ((len = fis.read(buffer)) != -1) {
                        // write in the archive
                        zos.write(buffer, 0, len);
                    }

                    fis.close();
                    zos.closeEntry();
                }
            }

            zos.finish();
            zos.close();
        } catch (FileNotFoundException e) {
            Log.createLog(e.getMessage());
        } catch (IOException e) {
            Log.createLog(e.getMessage());
        }
    }

    public String getCompressedFileName() {
        return this.zipFileName;
    }

}
