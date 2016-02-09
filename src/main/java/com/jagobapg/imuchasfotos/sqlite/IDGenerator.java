/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * IDGenerator.java
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
package com.jagobapg.imuchasfotos.sqlite;

import com.jagobapg.imuchasfotos.dto.Folder;
import com.jagobapg.imuchasfotos.dto.Topic;
import com.jagobapg.imuchasfotos.dto.Key;
import com.jagobapg.imuchasfotos.dto.Format;
import com.jagobapg.imuchasfotos.dto.Photo;
import com.jagobapg.imuchasfotos.dto.Author;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class which will manage the ID creation of the new records in the database. Each time
 * a new record is going to be added, this class will be called and will return the
 * next available ID.
 * Class called from gui.utilities.DBManipulation
 */
public final class IDGenerator {
    
    /**
     * Generate author id for the database.
     * @return new id.
     */
    public static long genAuthorID() {
        String query = "select max(id) from AUTHOR;";
        long id = 0;
        
        if(Author.getLastID() == 0) {
            try {
                Statement stm = DBConnection.getConexion().createStatement();
                ResultSet rs = stm.executeQuery(query);

                if(rs.next()) { //Get ID.
                    id = rs.getLong(1);
                } else { //No ID in the database.
                    id = 0;
                }
                
                id = id + 1; //Next ID.
            } catch (SQLException ex) {
                id = -1; //Error.
            }
            
            Author.setLastID(id);
        } else {
            id = Author.getLastID() + 1;
            Author.setLastID(id);
        }
        
        return id;
    }
    
    /**
     * Generate format id for the database.
     * @return new id.
     */
    public static long genFormatID() {
        String query = "select max(id) from FORMAT;";
        long id = 0;
        
        if(Format.getLastID() == 0) {
            try {
                Statement stm = DBConnection.getConexion().createStatement();
                ResultSet rs = stm.executeQuery(query);

                if(rs.next()) { //Get ID.
                    id = rs.getLong(1);
                } else { //No ID in the database.
                    id = 0;
                }
                
                id = id + 1; //Next ID.
            } catch (SQLException ex) {
                id = -1; //Error.
            }
            
            Format.setLastID(id);
        } else {
            id = Format.getLastID() + 1;
            Format.setLastID(id);
        }
        
        return id;
    }
    
    /**
     * Generate key id for the database.
     * @return new id.
     */
    public static long genKeyID() {
        String query = "select max(id) from KEY;";
        long id = 0;
        
        if(Key.getLastID() == 0) {
            try {
                Statement stm = DBConnection.getConexion().createStatement();
                ResultSet rs = stm.executeQuery(query);

                if(rs.next()) { //Get ID.
                    id = rs.getLong(1);
                } else { //No ID in the database.
                    id = 0;
                }
                
                id = id + 1; //Next ID.
            } catch (SQLException ex) {
                ex.printStackTrace();
                id = -1; //Error.
            }
            
            Key.setLastID(id);
        } else {
            id = Key.getLastID() + 1;
            Key.setLastID(id);
        }
        
        return id;
    }

    /**
     * Generate photo id for the database.
     * @return new id.
     */
    public static long genPhotoID() {
        String query = "select max(id) from PHOTO;";
        long id = 0;
        
        if(Photo.getLastID() == 0) {
            try {
                Statement stm = DBConnection.getConexion().createStatement();
                ResultSet rs = stm.executeQuery(query);

                if(rs.next()) { //Get ID.
                    id = rs.getLong(1);
                } else { //No ID in the database.
                    id = 0;
                }
                
                id = id + 1; //Next ID.
            } catch (SQLException ex) {
                id = -1; //Error.
            }
            
            Photo.setLastID(id);
        } else {
            id = Photo.getLastID() + 1;
            Photo.setLastID(id);
        }
        
        return id;
    }
    
    /**
     * Generate topic id for the database.
     * @return new id.
     */
    public static long genTopicID() {
        String query = "select max(id) from TOPIC;";
        long id = 0;
        
        if(Topic.getLastID() == 0) {
            try {
                Statement stm = DBConnection.getConexion().createStatement();
                ResultSet rs = stm.executeQuery(query);

                if(rs.next()) { //Get ID.
                    id = rs.getLong(1);
                } else { //No ID in the database.
                    id = 0;
                }
                
                id = id + 1; //Next ID.
            } catch (SQLException ex) {
                id = -1; //Error.
            }
            
            Topic.setLastID(id);
        } else {
            id = Topic.getLastID() + 1;
            Topic.setLastID(id);
        }
        
        return id;
    }
    
    /**
     * Generate folder id for the database.
     * @return new id.
     */
    public static long genFolderID() {
        String query = "select max(id) from FOLDER;";
        long id = 0;
        
        if(Folder.getLastID() == 0) {
            try {
                Statement stm = DBConnection.getConexion().createStatement();
                ResultSet rs = stm.executeQuery(query);

                if(rs.next()) { //Get ID.
                    id = rs.getLong(1);
                } else { //No ID in the database.
                    id = 0;
                }
                
                id = id + 1; //Next ID.
            } catch (SQLException ex) {
                id = -1; //Error.
            }
            
            Folder.setLastID(id);
        } else {
            id = Folder.getLastID() + 1;
            Folder.setLastID(id);
        }
        
        return id;
    }
}
