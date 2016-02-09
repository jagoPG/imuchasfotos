/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * Photo.java
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

import com.jagobapg.imuchasfotos.sqlite.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that will hold the primary information about the photo. All photos will be
 * manipulated in the program folder. This will allow portability to the program.
 */
public final class Photo {
    private long id;
    private String name, description;
    private String file;
    private long idAuthor, idFormat, idDir;
    private int year;
    private static long lastID;

    /**
     * Create new blank photo
     */
    public Photo() {
        this.id = 0;
        this.year = 0;
        this.name = null;
        this.description = null;
        this.file = null;
        this.idAuthor = 0;
        this.idFormat = 0;
        this.idDir = 0;
    }

    /**
     * Create a new photo
     * @param idFoto photo ID
     * @param anioFoto year
     * @param nombreFoto name
     * @param descrFoto description
     * @param fichero path
     * @param carpeta folder organization purposes
     * @param idAutor author ID
     * @param idFormato format ID
     */
    public Photo(long idFoto, int anioFoto, String nombreFoto,
            String descrFoto, String fichero, String carpeta, 
            long idAutor, long idFormato) {
        this.setId(idFoto);
        this.setYear(anioFoto);
        this.setName(nombreFoto);
        this.setDescription(descrFoto);
        this.setFile(fichero);
        this.setIdAuthor(idAutor);
        this.setIdFormat(idFormato);
        this.setIdDir(idDir);
    }

    /**
     * Retrieve a photo from database record
     * @param rs ResultSet
     */
    public Photo(ResultSet rs) {
        try {
            this.setId(rs.getInt("id"));
            this.setName(rs.getString("name"));
            this.setYear(rs.getInt("year"));
            this.setDescription(rs.getString("description"));
            this.setFile(rs.getString("file"));
            this.setIdAuthor(rs.getInt("idAuthor"));
            this.setIdFormat(rs.getInt("idFormat"));
            this.setIdDir(rs.getInt("idDir"));
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    /**
     * @return el ID y el Name de la foto
     */
    public String toString() {
        return this.getName();
    }
    
    public int compareTo(Photo p) {
    	if(this.getId() > p.getId()) {
    		return 1;
    	} else if(this.getId() < p.getId()) {
    		return -1;
    	} else {
    		return 0;
    	}
    }

    /**
     * Gets all information of an image from a database
     * @param database
     * @param idFoto
     * @return null if does not exist
     */
    public static Photo getInformation(String database, long idFoto) {
        PreparedStatement pstImage = null;
        ResultSet rsImage = null;
        Photo image = null;
        
        try {
            pstImage = DBConnection.getConexion().prepareStatement("select * from foto where idFoto=?");
            pstImage.setLong(1, idFoto);
            rsImage = pstImage.executeQuery();
            
            if(rsImage.next()) {
                image = new Photo(rsImage);
            }
            
            rsImage.close();
            pstImage.close();
        } catch (SQLException ex) {
            Logger.getLogger(Photo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return image;
    }
        
    /**
     * Gets all theme IDs
     * @return null if there are no themes
     */
    public int[] getIdTemas(String database) {
        int[] temas_id = null;
        try {            
            PreparedStatement pstThemes = DBConnection.getConexion().prepareStatement("select idTema from fototema where idFoto=?");
            pstThemes.setLong(1, this.getId());
            pstThemes.executeQuery();
            
            ResultSet rsThemes = pstThemes.executeQuery();            
            
            if(rsThemes.last()) {
                int elements = rsThemes.getRow();
                rsThemes.first();                
                temas_id = new int[elements];
                
                for(int i = 0; i < elements; i++) {
                    temas_id[i] = rsThemes.getInt("idTema");
                    rsThemes.next();
                }
                rsThemes.close();
                pstThemes.close();
                
                return temas_id;
            } else {
                rsThemes.close();
                pstThemes.close();
                
                return null;
            }            
        } catch (SQLException ex) {
            Logger.getLogger(Photo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }        
    }
    
    /**
     * Gets all key IDs
     * @param database
     * @return null if there is no keys
     */
    public int[] getIdClaves(String database) {
        int[] claves_id = null;
        try {
            PreparedStatement pstThemes = DBConnection.getConexion().prepareStatement("select idClave from fotoclave where idFoto=?");
            pstThemes.setLong(1, this.getId());
            pstThemes.executeQuery();
            
            ResultSet rsThemes = pstThemes.executeQuery();            
            
            if(rsThemes.last()) {
                int elements = rsThemes.getRow();
                rsThemes.first();                
                claves_id = new int[elements];
                
                for(int i = 0; i < elements; i++) {
                    claves_id[i] = rsThemes.getInt("idClave");
                    rsThemes.next();
                }
                rsThemes.close();
                pstThemes.close();
                
                return claves_id;
            } else {
                rsThemes.close();
                pstThemes.close();
                
                return null;
            }            
        } catch (SQLException ex) {
            Logger.getLogger(Photo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * Gets the autor's name
     * @param database
     * @return null if there is no authors assigned
     */
    public String getNameAutor(String database) {
        String author = null;
        try {
            PreparedStatement pstAuthor = DBConnection.getConexion().prepareStatement("select nombreAutor from autor where idAutor=?");
            pstAuthor.setLong(1, this.idAuthor);
            ResultSet rsAuthor = pstAuthor.executeQuery();
            
            if(rsAuthor.next()) {
                author = rsAuthor.getString("nombreAutor");
            }
            
            rsAuthor.close();
            pstAuthor.close();
        } catch (SQLException ex) {
            Logger.getLogger(Photo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return author;
    }
    
    /**
     * Gets the format's name
     * @param database
     * @return null if there is no format assigned
     */
    public String getNameFormato(String database) {
        String format = null;
        try {
            PreparedStatement pstFormat = DBConnection.getConexion().prepareStatement("select nombreFormato from formato where idFormato=?");
            pstFormat.setLong(1, this.idFormat);
            ResultSet rsFormat = pstFormat.executeQuery();
            
            if(rsFormat.next()) {
                format = rsFormat.getString("nombreFormato");
            }
            
            rsFormat.close();
            pstFormat.close();
        } catch (SQLException ex) {
            Logger.getLogger(Photo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return format;
    }
    
    /**
     * Gets the theme's and subtheme's IDs
     * @param database
     * @return 
     */
    public Topic[] getTemas(String database) {
        Topic[] themes = null;
        try {
            PreparedStatement pstThemes = DBConnection.getConexion().prepareStatement("select * from tema where idTema=any(select idTema from fototema where idFoto=?) order by nombreTema");
            pstThemes.setLong(1, this.id);
            ResultSet rsThemes = pstThemes.executeQuery();
            
            if(rsThemes.last()) {
                int elements = rsThemes.getRow();
                themes = new Topic[elements];
                rsThemes.first();
                
                for(int i = 0; i < elements; i++) {
                    themes[i] = new Topic(rsThemes);
                    rsThemes.next();
                }
            }
            
            rsThemes.close();
            pstThemes.close();
        } catch (SQLException ex) {
            Logger.getLogger(Photo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return themes;
    }
    
    /**
     * Gets the key's IDs
     * @param database
     * @return 
     */
    public Key[] getClaves(String database) {
        Key[] keys = null;
        try {
            PreparedStatement pstKeys = DBConnection.getConexion().prepareStatement("select * from clave where idClave=any(select idClave from fotoclave where idFoto=?) order by idClave");
            pstKeys.setLong(1, this.id);
            ResultSet rsKeys = pstKeys.executeQuery();
            
            if(rsKeys.last()) {
                int elements = rsKeys.getRow();
                keys = new Key[elements];
                rsKeys.first();
                
                for(int i = 0; i < elements; i++) {
                    keys[i] = new Key(rsKeys);
                    rsKeys.next();
                }
            }
            
            rsKeys.close();
            pstKeys.close();
        } catch (SQLException ex) {
            Logger.getLogger(Photo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return keys;
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
        this.id=id;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param nombre the name to set
     */
    public void setName(String nombre) {
        this.name = nombre;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the file
     */
    public String getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * @return the idAuthor
     */
    public long getIdAuthor() {
        return idAuthor;
    }

    /**
     * @param idAuthor the idAuthor to set
     */
    public void setIdAuthor(long idAuthor) {
        this.idAuthor = idAuthor;
    }

    /**
     * @return the idFormat
     */
    public long getIdFormat() {
        return idFormat;
    }

    /**
     * @param idFormat the idFormat to set
     */
    public void setIdFormat(long idFormat) {
        this.idFormat = idFormat;
    }

    /**
     * @return the idDir
     */
    public long getIdDir() {
        return idDir;
    }

    /**
     * @param idDir the idDir to set
     */
    public void setIdDir(long idDir) {
        this.idDir = idDir;
    }

    /**
     * @return the lastID
     */
    public static long getLastID() {
        return Photo.lastID;
    }

    /**
     * @param lastID the lastID to set
     */
    public static void setLastID(long lastID) {
        Photo.lastID = lastID;
    }

    
}
