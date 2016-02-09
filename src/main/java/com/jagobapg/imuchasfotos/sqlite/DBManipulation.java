/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 *
 * DBManipulation.java
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
package com.jagobapg.imuchasfotos.sqlite;

import com.jagobapg.imuchasfotos.dto.Photo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class has SQL instructions to manipulate data of the database.
 */
public enum DBManipulation {
    INSTANCE;

    private final Logger logger = LogManager.getLogger(DBManipulation.class);

    /**
     * Insert a folder into the database. It checks if exists.
     * @param f a list of folders.
     * @return operation success.
     */
    public boolean insertFolders(final String[] f) {
        boolean success = true;
        String cmd;
        PreparedStatement pst;

        for (String folder : f) {
            if (!DBQueries.INSTANCE.existsFolder(folder)) {
                try {
                    // add to the database
                    cmd = "insert into FOLDER (id, name) values (?,?);";
                    pst = DBConnection.getConexion().prepareStatement(cmd);
                    pst.setLong(1, IDGenerator.genFolderID());
                    pst.setString(2, folder);
                    pst.executeUpdate();
                    pst.close();
                } catch (SQLException ex) {
                    success = false; // error happened
                    logger.error(ex.getMessage());
                }
            }
        }

        return success;
    }

    /**
     * Delete all folders from database.
     * @param idFolder id of the folder to delete
     * @return true if success.
     */
    public boolean deleteFolder(long idFolder) {
        boolean success = false;
        PreparedStatement pstUpdate;
        String cmd = "delete from folder where id=?;";

        try {
            pstUpdate = DBConnection.getConexion().prepareStatement(cmd);
            pstUpdate.setLong(1, idFolder);
            pstUpdate.executeUpdate();
            pstUpdate.close();
            success = true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return success;
    }

    /**
     * Insert a photo into the database. Needed fields: name, file and idDir.
     * @param p photo to be inserted.
     * @return operation success.
     */
    public boolean insertPhoto(final Photo p) {
        boolean success;
        long id;
        PreparedStatement pst;
        String cmd = "insert into photo (id, name, file, idDir) values (?, ?, ?, ?);";

        try {
            id = IDGenerator.genPhotoID();
            pst = DBConnection.getConexion().prepareStatement(cmd);
            pst.setLong(1, id);
            pst.setString(2, p.getName());
            pst.setString(3, p.getFile());
            pst.setLong(4, p.getIdDir());
            pst.executeUpdate();

            success = true;
            pst.close();
        } catch (SQLException ex) {
            success = false;
            logger.error(ex.getMessage());
        }

        return success;
    }

    /**
     * Delete all photos related to a folder
     * @param idFolder
     * @return
     */
    public boolean deletePhotosFromDir(final long idFolder) {
        boolean success = false;
        PreparedStatement pstUpdate;
        String cmd = "delete from photo where idDir=?";

        try {
            pstUpdate = DBConnection.getConexion().prepareStatement(cmd);
            pstUpdate.setLong(1, idFolder);
            pstUpdate.executeUpdate();
            pstUpdate.close();
            success = true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return success;
    }

    /**
     * Delete a photo from the database.
     * @param idPhoto ID from the photo wanted to delete.
     * @return true if success.
     */
    public boolean deletePhoto(final long idPhoto) {
        boolean success = false;
        PreparedStatement pstUpdate;
        String cmd = "delete from photo where id=?";

        try {
            pstUpdate = DBConnection.getConexion().prepareStatement(cmd);
            pstUpdate.setLong(1, idPhoto);
            pstUpdate.executeUpdate();
            pstUpdate.close();
            success = true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return success;
    }

    /**
     * Changes a photo name.
     * @param photoID ID of the photo.
     * @param photoName New name.
     * @return true if success.
     */
    public boolean updatePhotoName(final long photoID, final String photoName) {
        PreparedStatement pstUpdate;
        String cmd;

        try {
            cmd = "update PHOTO set name=? where id=?;";
            pstUpdate = DBConnection.getConexion().prepareStatement(cmd);
            pstUpdate.setString(1, photoName);
            pstUpdate.setLong(2, photoID);
            pstUpdate.executeUpdate();
            pstUpdate.close();
            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Changes a photo year.
     * @param photoID ID of the photo.
     * @param year New year.
     * @return true if success.
     */
    public boolean updatePhotoYear(final long photoID, final int year) {
        String cmd;
        PreparedStatement pstUpdate;

        try {
            cmd = "update PHOTO set year=? where id=?;";
            pstUpdate = DBConnection.getConexion().prepareStatement(cmd);
            pstUpdate.setInt(1, year);
            pstUpdate.setLong(2, photoID);
            pstUpdate.executeUpdate();
            pstUpdate.close();
            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Changes a photo author.
     * @param photoID ID of the photo.
     * @param authorID New author.
     * @return true if success.
     */
    public boolean updatePhotoAuthor(final long photoID, final long authorID) {
        String cmd;
        PreparedStatement pstUpdate;

        try {
            cmd = "update PHOTO set idAuthor=? where id=?;";
            pstUpdate = DBConnection.getConexion().prepareStatement(cmd);
            pstUpdate.setLong(1, authorID);
            pstUpdate.setLong(2, photoID);
            pstUpdate.executeUpdate();
            pstUpdate.close();
            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

        /**
     * Changes a photo description.
     * @param photoID ID of the photo.
     * @param description New description.
     * @return true if success.
     */
    public boolean updatePhotoDescription(final long photoID, final String description) {
        String cmd;
        PreparedStatement pstUpdate;

        try {
            cmd = "update PHOTO set description=? where id=?;";
            pstUpdate = DBConnection.getConexion().prepareStatement(cmd);
            pstUpdate.setString(1, description);
            pstUpdate.setLong(2, photoID);
            pstUpdate.executeUpdate();
            pstUpdate.close();
            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Changes a author name.
     * @param idAuthor id of the author wanted to change.
     * @param name new name.
     * @return true if success.
     */
    public boolean updateAuthorName(final long idAuthor, final String name) {
        String cmd;
        PreparedStatement pstm;

        try {
            cmd = "update AUTHOR set name=? where id=?;";
            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setString(1, name);
            pstm.setLong(2, idAuthor);
            pstm.executeUpdate();
            pstm.close();
            
            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

     /**
     * Changes a photo format.
     * @param photoID ID of the photo.
     * @param formatID New format.
     * @return true if success.
     */
    public boolean updatePhotoFormat(final long photoID, final long formatID) {
        String cmd;
        PreparedStatement pstUpdate;

        try {
            cmd = "update PHOTO set idFormat=? where id=?;";
            pstUpdate = DBConnection.getConexion().prepareStatement(cmd);
            pstUpdate.setLong(1, formatID);
            pstUpdate.setLong(2, photoID);
            pstUpdate.executeUpdate();
            pstUpdate.close();
            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Changes a photo description.
     * @param photoID ID of the photo.
     * @param description New description.
     * @return true if success.
     */
    public boolean updatePhotoFormat(final long photoID, final String description) {
        String cmd;
        PreparedStatement pstUpdate;

        try {
            cmd = "update PHOTO set description=? where id=?;";
            pstUpdate = DBConnection.getConexion().prepareStatement(cmd);
            pstUpdate.setString(1, description);
            pstUpdate.setLong(2, photoID);
            pstUpdate.executeUpdate();
            pstUpdate.close();
            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Insert a PHOTO_TOPIC into the database.
     * @param idPhoto photo ID.
     * @param idTopic topic ID.
     * @return true if success.
     */
    public boolean insertPhotoTopic(final long idPhoto, final long idTopic) {
        String cmd;
        PreparedStatement pstm;
        
        try {
            cmd = "insert into PHOTO_TOPIC (idPhoto, idTopic) VALUES (?,?);";
            pstm = DBConnection.getConexion().prepareStatement(cmd);

            pstm.setLong(1, idPhoto);
            pstm.setLong(2, idTopic);
            pstm.executeUpdate();
            pstm.close();

            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Delete a PHOTO_TOPIC from the database.
     * @param idPhoto photo ID.
     * @param idTopic topic ID.
     * @return true if success.
     */
    public boolean deletePhotoTopic(final long idPhoto, final long idTopic) {
        String cmd;
        PreparedStatement pstm;

        try {
            cmd = "delete from PHOTO_TOPIC where idPhoto=? AND idTopic=?;";
            pstm = DBConnection.getConexion().prepareStatement(cmd);

            pstm.setLong(1, idPhoto);
            pstm.setLong(2, idTopic);
            pstm.executeUpdate();
            pstm.close();

            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Insert a PHOTO_KEY into the database.
     * @param idPhoto photo ID.
     * @param idKey topic ID.
     * @return true if success.
     */
    public boolean insertPhotoKey(final long idPhoto, final long idKey) {
        String cmd;
        PreparedStatement pstm;
        try {
            cmd = "insert into PHOTO_KEY (idPhoto, idKey) VALUES (?,?);";
            pstm = DBConnection.getConexion().prepareStatement(cmd);

            pstm.setLong(1, idPhoto);
            pstm.setLong(2, idKey);
            pstm.executeUpdate();
            pstm.close();

            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Delete a PHOTO_KEY from the database.
     * @param idPhoto photo ID.
     * @param idKey topic ID.
     * @return true if success.
     */
    public boolean deletePhotoKey(final long idPhoto, final long idKey) {
        String cmd;
        PreparedStatement pstm;

        try {
            cmd = "delete from PHOTO_KEY where idPhoto=? AND idKey=?;";
            pstm = DBConnection.getConexion().prepareStatement(cmd);

            pstm.setLong(1, idPhoto);
            pstm.setLong(2, idKey);
            pstm.executeUpdate();
            pstm.close();

            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Insert an author into the database.
     * @param name author's name.
     * @return true if success.
     */
    public boolean insertAuthor(final String name) {
        String cmd;
        PreparedStatement pstm;

        try {
            cmd = "insert into AUTHOR (id, name) VALUES (?,?)";
            pstm = DBConnection.getConexion().prepareStatement(cmd);

            pstm.setLong(1, IDGenerator.genAuthorID());
            pstm.setString(2, name);
            pstm.executeUpdate();
            pstm.close();

            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Delete an author from the database and searches its ID through the database.
     * @param authorID author to delete.
     * @return true if success.
     */
    public boolean deleteAuthor(final long authorID) {
        String cmd;
        PreparedStatement pstm;

        try {
            // Delete author from the photos
            cmd = "update PHOTO set idAuthor=NULL WHERE idAuthor=?";
            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setLong(1, authorID);
            pstm.executeUpdate();
            pstm.close();

            // Delete author
            cmd = "delete from AUTHOR where id=?;";
            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setLong(1, authorID);
            pstm.executeUpdate();
            pstm.close();

            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Insert an format into the database.
     * @param name format name.
     * @return true if success.
     */
    public boolean insertFormat(final String name) {
        String cmd;
        PreparedStatement pstm;

        try {
            cmd = "insert into FORMAT (id, name) VALUES (?,?)";

            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setLong(1, IDGenerator.genFormatID());
            pstm.setString(2, name);
            pstm.executeUpdate();
            pstm.close();

            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Delete a format from the database and searches its ID through the database.
     * @param idFormat format to delete.
     * @return true if success.
     */
    public boolean deleteFormat(final long idFormat) {
        String cmd;
        PreparedStatement pstm;

        try {
            // delete format from photo
            cmd = "update PHOTO set idFormat=NULL where idFormat=?";
            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setLong(1, idFormat);
            pstm.executeUpdate();
            pstm.close();

            // delete format
            cmd = "delete from FORMAT where id=?";
            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setLong(1, idFormat);
            pstm.executeUpdate();
            pstm.close();

            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Changes a format name.
     * @param idFormat id of the format wanted to change.
     * @param name new name.
     * @return true if success.
     */
    public boolean updateFormatName(final long idFormat, final String name) {
        String cmd;
        PreparedStatement pstm;

        try {
            cmd = "update FORMAT set name=? where id=?;";
            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setString(1, name);
            pstm.setLong(2, idFormat);
            pstm.executeUpdate();
            pstm.close();
            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Insert an key into the database.
     * @param name key's name.
     * @return true if success.
     */
    public boolean insertKey(final String name) {
        String cmd;
        PreparedStatement pstm;

        try {
            cmd = "insert into KEY (id, name) VALUES (?,?)";
            pstm = DBConnection.getConexion().prepareStatement(cmd);

            pstm.setLong(1, IDGenerator.genKeyID());
            pstm.setString(2, name);
            pstm.executeUpdate();
            pstm.close();

            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Delete a key from the database and searches its ID through the database.
     * @param idKey key to delete.
     * @return true if success.
     */
    public boolean deleteKey(final long idKey) {
        String cmd;
        PreparedStatement pstm;

        try {
            // delete key from photo
            cmd = "delete from PHOTO_KEY where idKey=?";
            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setLong(1, idKey);
            pstm.executeUpdate();
            pstm.close();

            // Delete key
            cmd = "delete from KEY where id=?";
            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setLong(1, idKey);
            pstm.executeUpdate();
            pstm.close();

            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Changes a key name.
     * @param idKey id of the key wanted to change.
     * @param name new name.
     * @return true if success.
     */
    public boolean updateKeyName(final long idKey, final String name) {
        String cmd;
        PreparedStatement pstm;

        try {
            cmd = "update KEY set name=? where id=?;";
            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setString(1, name);
            pstm.setLong(2, idKey);
            pstm.executeUpdate();
            pstm.close();
            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Insert an topic into the database.
     * @param name topic's name.
     * @return true if success.
     */
    public boolean insertTopic(final String name) {
        String cmd;
        PreparedStatement pstm;

        try {
            cmd = "insert into TOPIC (id, topic, subtopic) VALUES (?,?,?)";
            pstm = DBConnection.getConexion().prepareStatement(cmd);

            pstm.setLong(1, IDGenerator.genTopicID());
            pstm.setString(2, name);
            pstm.setString(3, "");
            pstm.executeUpdate();
            pstm.close();

            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Changes a topic name.
     * @param oldTopic id of the topic wanted to change.
     * @param newTopic new name for the topic.
     * @param subtopic new name for the subtopic.
     * @return true if success.
     */
    public boolean updateTopicName(final String oldTopic, final String newTopic, final String subtopic) {
        String cmd;
        PreparedStatement pstm;

        try {
            // Change topic and subtopics names
            cmd = "update TOPIC set topic=?, subtopic=? where topic=?;";
            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setString(1, newTopic);
            pstm.setString(2, subtopic);
            pstm.setString(3, oldTopic);
            pstm.executeUpdate();
            pstm.close();

            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Delete a topic and all related data from tables.
     * @param idTopic id of the topic to delete.
     * @param topicName name of the topic to delete.
     * @return true if success.
     */
    public boolean deleteTopic(final long idTopic, final String topicName) {
        String cmd;
        PreparedStatement pstm;

        try {
            // Delete theme from PHOTO_TOPIC
            cmd = "delete from PHOTO_TOPIC where idTopic=?;";
            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setLong(1, idTopic);
            pstm.executeUpdate();
            pstm.close();

            // Delete subtopic
            cmd = "delete from TOPIC where topic=?;";
            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setString(1, topicName);
            pstm.executeUpdate();
            pstm.close();

            // Delete topic
            cmd = "delete from TOPIC where id=?;";
            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setLong(1, idTopic);
            pstm.executeUpdate();
            pstm.close();

            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Insert an subtopic into the database.
     * @param topic topic's name.
     * @param subtopic subtopic's name.
     * @return true if success.
     */
    public boolean insertSubtopic(final String topic, final String subtopic) {
        String cmd;
        PreparedStatement pstm;

        try {
            cmd = "insert into TOPIC (id, topic, subtopic) VALUES (?,?,?)";

            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setLong(1, IDGenerator.genTopicID());
            pstm.setString(2, topic);
            pstm.setString(3, subtopic);
            pstm.executeUpdate();
            pstm.close();

            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Changes a subtopic name.
     * @param idTopic id of the topic wanted to change.
     * @param subtopic new name for the subtopic.
     * @return true if success.
     */
    public boolean updateSubtopicName(final long idTopic, final String subtopic) {
        String cmd;
        PreparedStatement pstm;

        try {
            cmd = "update TOPIC set subtopic=? where id=?;";
            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setString(1, subtopic);
            pstm.setLong(2, idTopic);
            pstm.executeUpdate();
            pstm.close();
            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Delete a subtopic and all related data from tables.
     * @param idTopic id of the subtopic to delete.
     * @return true if success.
     */
    public boolean deleteSubtopic(final long idTopic) {
        String cmd;
        PreparedStatement pstm;

        try {
            // delete topic from PHOTO_TOPIC
            cmd = "delete from PHOTO_TOPIC where idTopic=?;";
            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setLong(1, idTopic);
            pstm.executeUpdate();
            pstm.close();

            return true;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }
}
