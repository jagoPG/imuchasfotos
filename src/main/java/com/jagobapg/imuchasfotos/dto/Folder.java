/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * Folder.java
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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Folders added into the program, where Photos are stored. Only parent folder of
 * the images are registered. Children folders are checked and its images are
 * stored into the program, but its no need of register them into the database. 
 */
public class Folder {

    private long id;
    private String name;
    private static long lastID;

    /**
     * Create new folder
     * @param id folder ID
     * @param name folder path
     */
    public Folder(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Create a new folder from database record
     * @param rs ResultSet
     */
    public Folder(ResultSet rs) {
        try {
            this.setId(rs.getLong("id"));
            this.setName(rs.getString("name"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    /**
     * @return the lastID
     */
    public static long getLastID() {
        return lastID;
    }

    /**
     * @param aLastID the lastID to set
     */
    public static void setLastID(long aLastID) {
        lastID = aLastID;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    
}
