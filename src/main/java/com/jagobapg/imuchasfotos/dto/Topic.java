/*
 * Jagoba Pérez Copyright 2014 
 * This program is distributed under the terms of the GNU General Public License
 * 
 * Topic.java
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
 * Topic is what is the photo about.
 */
public final class Topic {
    private long id;
    private String topic;
    private String subtopic;
    private static long lastID;

    /**
     * Create blank topic
     */
    public Topic() {
        this.id = 0;
        this.topic = "";
    }

    /**
     * Create a new topic
     * @param idTema topic ID
     * @param nombreTema topic name
     */
    public Topic(long idTema, String nombreTema) {
        this.id = idTema;
        this.topic = nombreTema;
    }

    /**
     * Retrieve a topic from database
     * @param rs
     */
    public Topic(ResultSet rs) {
        try {
            this.setId(rs.getInt("id"));
            this.setTopic(rs.getString("topic"));
            this.setSubtopic(rs.getString("subtopic"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get parent theme from another theme
     * @return parent Tema
     */
    public Topic getParentTema() {
        Topic parent = null;
        String cmd = "select * from TOPIC where topic=? and subtopic='' or subtopic=NULL";
        if(this.subtopic.trim().equals("")) {
            parent = new Topic(this.id, this.topic);
        } else {
            try {
                PreparedStatement pstParent = DBConnection.getConexion().prepareStatement(cmd);
                pstParent.setString(1, this.topic);
                ResultSet rsParent = pstParent.executeQuery();
                if(rsParent.next()) {
                    parent = new Topic(rsParent);
                }
                
                pstParent.close();
                rsParent.close();
            } catch (SQLException ex) {
                Logger.getLogger(Topic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return parent;
    }

    /**
     * Is a subtopic or a topic, depends on the filled variables.
     * @return true if is subtopic.
     */
    public boolean isSubtopic() {
        return !(this.getSubtopic() == null || this.getSubtopic().equals(""));
    }
    
    /**
     * @return el id y el nombre del tema
     */
    @Override
    public String toString() {
        if(this.isSubtopic()) {
            return "    " + this.getSubtopic();
        } else {
            return this.getTopic();
        }
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
     * @return the topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * @param topic the topic to set
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * @return the subtopic
     */
    public String getSubtopic() {
        return subtopic;
    }

    /**
     * @param subtopic the subtopic to set
     */
    public void setSubtopic(String subtopic) {
        this.subtopic = subtopic;
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
        Topic.lastID = lastID;
    }
}
