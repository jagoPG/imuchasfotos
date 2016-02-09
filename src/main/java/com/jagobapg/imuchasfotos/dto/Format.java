/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * Format.java
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
 * Physical format of the photo, e.g.: Black and white, A4, landscape... Actually
 * one format for each photo is needed.
 */
public final class Format {

    private long id;
    private String name;
    private static long lastID;

    /**
     * Create a new blank format
     */
    public Format() {
        this.id = 0;
        this.name = "";
    }

    /**
     * Create a new format ID
     * @param idFormato format ID
     * @param nombreFormato format name
     */
    public Format(long idFormato, String nombreFormato) {
        this.setId(idFormato);
        this.setName(nombreFormato);
    }

    /**
     * Create a new format from database
     * @param rs ResultSet
     */
    public Format(ResultSet rs) {
        try {
            this.setId(rs.getInt("id"));
            this.setName(rs.getString("name"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @return el ID y Nombre del formato
     */
    @Override
    public String toString() {
        return this.getName();
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
        Format.lastID = lastID;
    }

}
