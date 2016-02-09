/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * Key.java
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
 * Key words that will help to identifying the photo; e.g.:elements in the photo.
 * Several keys for each photo are needed.
 */
public final class Key {
    private long id;
    private String name;
    private static long lastID;

    /**
     * Create a blank key
     */
    public Key() {
        this.id = 0;
        this.name = null;
    }

    /**
     * Create a new key
     * @param id key ID
     * @param name key name
     */
    public Key(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Create a key from a database record
     * @param rs ResultSet
     */
    public Key(ResultSet rs) {
        try {
            this.setId(rs.getInt("id"));
            this.setName(rs.getString("name"));
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
    }

    /**
     * @return the idName
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param idClave the id to set
     */
    public void setId(long idClave) {
        this.id = idClave;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param nombreClave the name to set
     */
    public void setName(String nombreClave) {
        this.name = nombreClave;
    }    

    /**
     * @return the lastID
     */
    public static long getLastID() {
        return lastID;
    }

    /**
     * @param lastID the lastID to set
     */
    public static void setLastID(long lastID) {
        Key.lastID = lastID;
    }
}
