/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * Author.java
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
 * Author of a Photo. The only currently needed data is the name.
 */
public final class Author {
    private long id;
    private String name;
    private static long lastID;

    /**
     * Create new blank author
     */
    public Author() {
        this.id = 0;
        this.name = null;
    }

    /**
     * Create a new author
     * @param idAutor author ID
     * @param nombreAutor author name
     */
    public Author(long idAutor, String nombreAutor) {
        this.setId(idAutor);
        this.setName(nombreAutor);
    }

    /**
     * Create a new author from database record
     * @param rs ResultSet
     */
    public Author(ResultSet rs) {
        try {
            this.setId(rs.getInt("id"));
            this.setName(rs.getString("name"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Current author is equals to another. Compare ID.
     * @param a author.
     * @return true if equals.
     */
    public boolean equals(Author a) {
        return this.getId() == a.getId();
    }
    
    /**
     * @return el ID y el nombre del autor
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
     * @param idAutor the id to set
     */
    public void setId(long idAutor) {
        this.id = idAutor;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param nombreAutor the name to set
     */
    public void setName(String nombreAutor) {
        this.name = nombreAutor;
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
}
