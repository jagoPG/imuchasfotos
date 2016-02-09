/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * Log.java
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
package com.jagobapg.imuchasfotos.dto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.GregorianCalendar;

/**
 *  Save a log file with the errors.
 */
public class Log {

    public static void createLog(String error) {
        File f;
        File f_new;
        ObjectOutputStream oos;
        GregorianCalendar gc;
        String fecha;

        try {
            f = new File("error.log");
            f_new = new File("error.temp");
            oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(f_new)));

            // previous error existed
            if(f.exists()) {
                String cadena;
                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)));
                
                try {
                    while((cadena = ois.readUTF()) != null) {
                        oos.writeUTF(cadena);
                    }
                } catch(EOFException ex) {
                    ois.close();
                }
            }

            // new error
            gc = new GregorianCalendar();
            fecha = gc.get(GregorianCalendar.DAY_OF_MONTH) + "/" + gc.get(GregorianCalendar.MONTH) + "/" + gc.get(GregorianCalendar.YEAR) +
                    "(" + gc.get(GregorianCalendar.HOUR) + ":" + gc.get(GregorianCalendar.MINUTE) + ":" + gc.get(GregorianCalendar.SECOND) + "):";
            oos.writeUTF(fecha + error);
            oos.flush();
            oos.close();
            
            // rename file
            f.delete();
            f_new.renameTo(f);            
        } catch (FileNotFoundException ex) {
            f = new File("error.log");
            try { f.createNewFile(); } catch (IOException ex1) { }
        } catch (IOException ex) { ex.printStackTrace(); }        
    }
    
}
