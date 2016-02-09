/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 *
 * DBQueries.java
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

import com.jagobapg.imuchasfotos.dto.Log;
import com.jagobapg.imuchasfotos.dto.Topic;
import com.jagobapg.imuchasfotos.dto.Folder;
import com.jagobapg.imuchasfotos.dto.Key;
import com.jagobapg.imuchasfotos.dto.Format;
import com.jagobapg.imuchasfotos.dto.Photo;
import com.jagobapg.imuchasfotos.dto.Author;
import com.jagobapg.imuchasfotos.gui.GUIErrorFound;
import com.jagobapg.imuchasfotos.gui.imanager.ImageManagerFilter.EFilter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** This class has SQL instructions to make queries to the database. */
public enum DBQueries {
    INSTANCE;

    private final Logger logger = LogManager.getLogger(DBQueries.class);

    /**
     * Folder is searched in the database.
     * @param folder the path of the folder.
     * @return folder exists.
     */
    public boolean existsFolder(final String folder) {
        String query = "select * from FOLDER where name=?;";
        boolean exists = false;
        PreparedStatement pst;

        try {
            pst = DBConnection.getConexion().prepareStatement(query);
            pst.setString(1, folder);
            ResultSet rs = pst.executeQuery();
            exists = rs.next();

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return exists;
    }

    /**
     * Get the ID of a folder.
     * @param dir path of a folder.
     * @return ID. If folder does not exist -1 value is returned.
     */
    public long getFolderId(final String dir) {
    	String query = "select id from folder where name=?;";
    	long id = -1;
        PreparedStatement pst;

    	try {
            pst = DBConnection.getConexion().prepareStatement(query);
            pst.setString(1, dir);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
            	id = rs.getLong("id");
            }

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            id = -1;
            logger.error(ex.getMessage());
        }

    	return id;
    }

    /**
     * Check in the database if there are folders.
     * @return areImages
     */
    public boolean areFolders() {
        boolean areFolders =  false;
        String query = "select * from FOLDER;";
        Statement stm;
        ResultSet rs;

        try {
            stm = DBConnection.getConexion().createStatement();
            rs = stm.executeQuery(query);

            areFolders  = rs.next();

            rs.close();
            stm.close();
        } catch (SQLException ex) {
            areFolders  = false;
            logger.error(ex.getMessage());
        }
        return areFolders ;
    }

    /**
     * Get the ID from the parent folder of a children folder.
     * @param path
     * @return ID of the folder.
     */
    public long getSubfolderID(final String path) {
        long id = 0;
        String cmd;
        Statement stm;
        ResultSet rs;
        boolean found;
        String parent;

        try {
            // 1. Get all folders
            cmd = "select * from FOLDER;";
            stm = DBConnection.getConexion().createStatement();
            rs = stm.executeQuery(cmd);

            // Compare with the path received as arg
            found = false;
            while (rs.next() && !found) {
                parent = rs.getString("name");
                if (path.contains(parent)) {
                    found = true;
                    id = rs.getInt("id");
                }
            }

            rs.close();
            stm.close();
        } catch (SQLException ex) {
            id = -1;
            logger.error(ex.getMessage());
        }

        return id;
    }

    /**
     * Get all folders from database.
     * @return all folders.
     */
    public ArrayList<String> getFolderNames() {
        ArrayList<String> al = new ArrayList<String>();
        String cmd;
        Statement stm;
        ResultSet rs;

        try {
            cmd = "select * from FOLDER;";
            stm = DBConnection.getConexion().createStatement();
            rs = stm.executeQuery(cmd);

            while (rs.next()) {
                al.add(rs.getString("name"));
            }

            rs.close();
            stm.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return al;
    }

    /**
     * Get all folders from the database.
     * @return array of folders.
     */
    public Folder[] getFolders() {
        ArrayList<Folder> al = new ArrayList<Folder>();
        String cmd = "select * from FOLDER order by name;";
        Statement stmLoadFolders;
        ResultSet rsFolders;

        try {
            stmLoadFolders = DBConnection.getConexion().createStatement();
            rsFolders = stmLoadFolders.executeQuery(cmd);

            while (rsFolders.next())
                al.add(new Folder(rsFolders));

            stmLoadFolders.close();
            rsFolders.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return al.toArray(new Folder[al.size()]);
    }

    /**
     * Check if a photo is in the database.
     * @param path path of the image.
     * @return true if exists
     */
    public boolean existPhoto(final String path) {
        String cmd = "select id from PHOTO where file like ?;";
        boolean exists = false;
        PreparedStatement pst;
        ResultSet rs;

        try {
            pst = DBConnection.getConexion().prepareStatement(cmd);
            pst.setString(1, path);
            rs = pst.executeQuery();

            exists = rs.next();
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return exists;
    }

    //NO FILTER
    /**
     * Retrieves from the database a quantity of photos from the desired ID.
     * @param quantity number of photos.
     * @param fromId the first id to retrieve.
     * @return an array of photos of size 'quantity'.
     */
    public Photo[] getPhotosFromID(final int quantity, final long fromId) {
        Photo[] aPhoto = new Photo[quantity];
        String cmd;
        PreparedStatement pst;
        ResultSet rs;

        try {
            // get all images
            cmd = "select * from PHOTO where id > ? order by id asc limit ?;";
            pst = DBConnection.getConexion().prepareStatement(cmd);
            pst.setLong(1, fromId);
            pst.setInt(2, quantity);

            rs = pst.executeQuery();

            for (int i = 0; i < quantity && rs.next(); i++)
                aPhoto[i] = new Photo(rs);


            rs.close();
            pst.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return aPhoto;
    }

    /**
     * Retrieves from the database a quantity of photos that are before the desired ID.
     * @param quantity number of photos.
     * @param fromId the first id to retrieve.
     * @return an array of photos of size 'quantity'.
     */
    public Photo[] getPhotosBeforeID(final int quantity, final long fromId) {
        Photo[] aPhoto = new Photo[quantity];
        String cmd;
        PreparedStatement pst;
        ResultSet rs;

        try {
            /* Get all images. */
            cmd = "select * from PHOTO where id < ? order by id desc, name asc limit ?;";
            pst = DBConnection.getConexion().prepareStatement(cmd);
            pst.setLong(1, fromId);
            pst.setInt(2, quantity);

            rs = pst.executeQuery();

            for (int i = 0; i < quantity && rs.next(); i++)
                aPhoto[i] = new Photo(rs);

            // order array by ID
            for (int i = 0; i < aPhoto.length; i++) {
                for (int j = 0; j < aPhoto.length-1; j++) {
                    if (aPhoto[i].getId() < aPhoto[j].getId()) {
                        Photo temp = aPhoto[i];
                        aPhoto[i] = aPhoto[j];
                        aPhoto[j] = temp;
                    }
                }
            }

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return aPhoto;
    }

    /**
     * Get topics from a photo.
     * @param id photo id.
     * @return Topic list.
     */
    public Topic[] getPhotoThemes(final long id) {
        ArrayList<Topic> alTopic = new ArrayList<Topic>();
        String cmd = "select * from TOPIC where id IN (select idTopic from PHOTO_TOPIC where idPhoto=?);";
        PreparedStatement pst;
        ResultSet rs;

        try {
            pst = DBConnection.getConexion().prepareStatement(cmd);
            pst.setLong(1, id);
            rs = pst.executeQuery();

            while (rs.next())
                alTopic.add(new Topic(rs));

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }


        return alTopic.toArray(new Topic[alTopic.size()]);
    }

     /**
     * Get keys from a photo.
     * @param id photo id.
     * @return Key list.
     */
    public Key[] getPhotoKeys(final long id) {
        ArrayList<Key> alKey = new ArrayList<Key>();
        String cmd = "select * from KEY where id IN (select idKey from PHOTO_KEY where idPhoto=?);";
        PreparedStatement pst;
        ResultSet rs;

        try {
            pst = DBConnection.getConexion().prepareStatement(cmd);
            pst.setLong(1, id);
            rs = pst.executeQuery();

            while (rs.next())
                alKey.add(new Key(rs));

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return alKey.toArray(new Key[alKey.size()]);
    }

    /**
     * Calculates the number of folders of photos.
     * @param quantity Number of photos per page.
     * @return number of folders. Default: 1.
     */
    public int getNumberFoldersPhoto(final int quantity) {
        String cmd = "select count(id) from PHOTO;";
        int num = 0;
        Statement stm;
        ResultSet rs;

        try {
            stm = DBConnection.getConexion().createStatement();
            rs = stm.executeQuery(cmd);

            if (rs.next()) {
                num = rs.getInt(1);
                num = num / quantity;
                if (rs.getInt(1) % quantity != 0)
                    ++num;
            }
        } catch (SQLException ex) {
             Log.createLog(ex.getMessage());
             logger.error(ex.getMessage());
        }

        return (num == 0)?1:num;
    }

    /**
     * Get the number of photos in the database, no filters.
     * @return number of photos.
     */
    public int getSizeAllPhotos() {
        String cmd = "select count(id) from PHOTO;";
        int num = 0;
        Statement stm;
        ResultSet rs;

        try {
            stm = DBConnection.getConexion().createStatement();
            rs = stm.executeQuery(cmd);

            if (rs.next())
                num = rs.getInt(1);
        } catch (SQLException ex) {
            Log.createLog(ex.getMessage());
            logger.error(ex.getMessage());
        }

        return num;
    }
    //End of NO FILTER.

    /**
     * Get all authors from the database.
     * @return array of authors.
     */
    public Author[] getAuthors() {
        ArrayList<Author> al = new ArrayList<Author>();
        String cmd = "select * from AUTHOR order by name;";
        Statement stmAuthor;
        ResultSet rsAuthor;

        try {
            stmAuthor = DBConnection.getConexion().createStatement();
            rsAuthor = stmAuthor.executeQuery(cmd);

            while (rsAuthor.next())
                al.add(new Author(rsAuthor));

            rsAuthor.close();
            stmAuthor.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return al.toArray(new Author[al.size()]);
    }

    /**
     * Checks if a author exists.
     * @param authorName name of the author.
     * @return true if exists.
     */
    public boolean existAuthor(final String authorName) {
        String cmd = "select * from AUTHOR WHERE name=?;";
        boolean exist = false;
        PreparedStatement pstm;
        ResultSet rs;

        try {
            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setString(1, authorName);
            rs = pstm.executeQuery();

            exist = rs.next();
            rs.close();
            pstm.close();
        } catch (SQLException ex) {
            new GUIErrorFound(ex.getErrorCode(), ex).setVisible(true);
            logger.error(ex.getMessage());
        }

        return exist;
    }

    /**
     * Get the last inserted author.
     * @return Author.
     */
    public Author getLastInsertedAuthor() {
        String cmd = "select * from AUTHOR where id=(select max(id) from AUTHOR);";
        Author f = null;
        Statement stm;
        ResultSet rs;

        try {
            stm = DBConnection.getConexion().createStatement();
            rs = stm.executeQuery(cmd);

            if (rs.next())
                f = new Author(rs);

            rs.close();
            stm.close();
        } catch(SQLException ex) {
            logger.error(ex.getMessage());
        }

        return f;
    }

    /**
     * Get all topics from the database.
     * @return array of topics.
     */
    public Topic[] getTopics() {
        ArrayList<Topic> al = new ArrayList<Topic>();
        String cmd = "select * from TOPIC order by topic, id;";
        Statement stmTopic;
        ResultSet rsTopic;

        try {
            stmTopic = DBConnection.getConexion().createStatement();
            rsTopic = stmTopic.executeQuery(cmd);

            while (rsTopic.next())
                al.add(new Topic(rsTopic));

            rsTopic.close();
            stmTopic.close();
        } catch (SQLException ex) {
            new GUIErrorFound(ex.getErrorCode(), ex).setVisible(true);
            logger.error(ex.getMessage());
        }

        return al.toArray(new Topic[al.size()]);
    }

     /**
     * Checks if a topic exists.
     * @param topicName name of the topic.
     * @return true if exists.
     */
    public boolean existTopic(final String topicName) {
        String cmd = "select * from TOPIC WHERE topic=?;";
        boolean exist = false;
        PreparedStatement pstm;
        ResultSet rs;

        try {
            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setString(1, topicName);
            rs = pstm.executeQuery();

            exist = rs.next();
            rs.close();
            pstm.close();
        } catch (SQLException ex) {
            new GUIErrorFound(ex.getErrorCode(), ex).setVisible(true);
            logger.error(ex.getMessage());
        }

        return exist;
    }

     /**
     * Checks if a subtopic exists.
     * @param topicName name of topic.
     * @param subtopicName name of the subtopic.
     * @return true if exists.
     */
    public boolean existSubtopic(final String topicName, final String subtopicName) {
        String cmd = "select * from TOPIC WHERE topic=? and subtopic=?;";
        boolean exist = false;
        PreparedStatement pstm;
        ResultSet rs;

        try {
            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setString(1, topicName);
            pstm.setString(2, subtopicName);
            rs = pstm.executeQuery();

            exist = rs.next();
            rs.close();
            pstm.close();
        } catch (SQLException ex) {
            new GUIErrorFound(ex.getErrorCode(), ex).setVisible(true);
            logger.error(ex.getMessage());
        }

        return exist;
    }

    /**
     * Get the last inserted topic.
     * @return Topic.
     */
    public Topic getLastInsertedTopic() {
        String cmd = "select * from TOPIC where id=(select max(id) from TOPIC);";
        Topic f = null;
        Statement stm;
        ResultSet rs;

        try {
            stm = DBConnection.getConexion().createStatement();
            rs = stm.executeQuery(cmd);

            if(rs.next())
                f = new Topic(rs);

            rs.close();
            stm.close();
        } catch(SQLException ex) {
            logger.error(ex.getMessage());
        }

        return f;
    }

    /**
     * Get all formats from the database.
     * @return array of formats.
     */
    public Format[] getFormats() {
        ArrayList<Format> al = new ArrayList<Format>();
        String cmd = "select * from FORMAT order by name;";
        Statement stmFormats;
        ResultSet rsFormats;

        try {
            stmFormats = DBConnection.getConexion().createStatement();
            rsFormats = stmFormats.executeQuery(cmd);

            while (rsFormats.next())
                al.add(new Format(rsFormats));

            rsFormats.close();
            stmFormats.close();
        } catch (SQLException ex) {
            new GUIErrorFound(ex.getErrorCode(), ex).setVisible(true);
            logger.error(ex.getMessage());
        }

        return al.toArray(new Format[al.size()]);
    }

    /**
     * Get the last inserted format.
     * @return Format.
     */
    public Format getLastInsertedFormat() {
        String cmd = "select * from FORMAT where id=(select max(id) from FORMAT);";
        Format f = null;
        Statement stm;
        ResultSet rs;

        try {
            stm = DBConnection.getConexion().createStatement();
            rs = stm.executeQuery(cmd);

            if (rs.next())
                f = new Format(rs);

            rs.close();
            stm.close();
        } catch(SQLException ex) {
            logger.error(ex.getMessage());
        }

        return f;
    }

    /**
     * Checks if a format exists.
     * @param formatName name of the format.
     * @return true if exists.
     */
    public boolean existFormat(final String formatName) {
        String cmd = "select * from FORMAT WHERE name=?;";
        boolean exist = false;
        PreparedStatement pstm;
        ResultSet rs;

        try {
            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setString(1, formatName);
            rs = pstm.executeQuery();

            exist = rs.next();
            rs.close();
            pstm.close();
        } catch (SQLException ex) {
            new GUIErrorFound(ex.getErrorCode(), ex).setVisible(true);
            logger.error(ex.getMessage());
        }
        return exist;
    }

    /**
     * Get all keys from the database.
     * @return array of keys.
     */
    public Key[] getKeys() {
        String cmd = "select * from KEY order by name;";
        ArrayList<Key> al = new ArrayList<Key>();
        Statement stmKeys;
        ResultSet rsKeys;

        try {
            stmKeys = DBConnection.getConexion().createStatement();
            rsKeys = stmKeys.executeQuery(cmd);

            while (rsKeys.next())
                al.add(new Key(rsKeys));


            rsKeys.close();
            stmKeys.close();
        } catch (SQLException ex) {
            new GUIErrorFound(ex.getErrorCode(), ex).setVisible(true);
            logger.error(ex.getMessage());
        }

        return al.toArray(new Key[al.size()]);
    }

     /**
     * Checks if a key exists.
     * @param keyName name of the key.
     * @return true if exists.
     */
    public boolean existKey(final String keyName) {
        String cmd = "select * from KEY WHERE name=?;";
        boolean exist = false;
        PreparedStatement pstm;
        ResultSet rs;

        try {
            pstm = DBConnection.getConexion().prepareStatement(cmd);
            pstm.setString(1, keyName);
            rs = pstm.executeQuery();

            exist = rs.next();
            rs.close();
            pstm.close();
        } catch (SQLException ex) {
            new GUIErrorFound(ex.getErrorCode(), ex).setVisible(true);
            logger.error(ex.getMessage());
        }
        return exist;
    }

    /**
     * Get the last inserted key.
     * @return Key.
     */
    public Key getLastInsertedKey() {
        String cmd = "select * from KEY where id=(select max(id) from KEY);";
        Key f = null;
        Statement stm;
        ResultSet rs;

        try {
            stm = DBConnection.getConexion().createStatement();
            rs = stm.executeQuery(cmd);

            if (rs.next())
                f = new Key(rs);

            rs.close();
            stm.close();
        } catch(SQLException ex) {
            logger.error(ex.getMessage());
        }

        return f;
    }

    //FOLDER FILTER.
    /**
     * Retrieves from the database a quantity of photos from the desired IDFolder from
     * the photo ID back.
     * @param quantity number of photos.
     * @param fromId the first id to retrieve.
     * @param idFolder id from the photo's folder.
     * @return an array of photos of size 'quantity'.
     */
    public Photo[] getPhotosBeforeID_folderFilter(final int quantity, final long fromId, final long idFolder) {
        Photo[] aPhoto = new Photo[quantity];
        String cmd;
        PreparedStatement pst;
        ResultSet rs;

        try {
            // get all images
            cmd = "select * from PHOTO where id < ? AND idDir=? order by id desc, name asc limit ?;";
            pst = DBConnection.getConexion().prepareStatement(cmd);
            pst.setLong(1, fromId);
            pst.setLong(2, idFolder);
            pst.setInt(3, quantity);

            rs = pst.executeQuery();

            for (int i = 0; i < quantity && rs.next(); i++) {
                aPhoto[i] = new Photo(rs);
            }

            // order array by ID
            for (int i = 0; i < aPhoto.length; i++) {
                for (int j = 0; j < aPhoto.length-1; j++) {
                    if (aPhoto[i].getId() < aPhoto[j].getId()) {
                        Photo temp = aPhoto[i];
                        aPhoto[i] = aPhoto[j];
                        aPhoto[j] = temp;
                    }
                }
            }

            rs.close();
            pst.close();
        } catch (SQLException ex) {
             logger.error(ex.getMessage());
        }

        return aPhoto;
    }

    /**
     * Retrieves from the database a quantity of photos from the desired IDFolder from
     * the photo ID forward.
     * @param quantity number of photos.
     * @param fromId the first id to retrieve.
     * @param idFolder id from the photo's folder.
     * @return an array of photos of size 'quantity'.
     */
    public Photo[] getPhotosFromID_folderFilter(final int quantity, final long fromId, final long idFolder) {
        Photo[] aPhoto = new Photo[quantity];
        String cmd;
        PreparedStatement pst;
        ResultSet rs;

        try {
            // Get all images
            cmd = "select * from PHOTO where id > ? and idDir=? order by id asc limit ?;";
            pst = DBConnection.getConexion().prepareStatement(cmd);
            pst.setLong(1, fromId);
            pst.setLong(2, idFolder);
            pst.setInt(3, quantity);

            rs = pst.executeQuery();

            for (int i = 0; i < quantity && rs.next(); i++)
                aPhoto[i] = new Photo(rs);

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return aPhoto;
    }

    /**
     * Calculates the number of folders of photos. Folder filter count.
     * @param quantity Number of photos per page.
     * @param idFolder Folder which contains photos.
     * @return number of folders. Default: 1.
     */
    public int getNumberFolders_folderFilter(final int quantity, final long idFolder) {
        String cmd = "select count(id) from PHOTO where idDir=?;";
        int num = 0;
        PreparedStatement stm;
        ResultSet rs;

        try {
            stm = DBConnection.getConexion().prepareStatement(cmd);
            stm.setLong(1, idFolder);
            rs = stm.executeQuery();

            if (rs.next()) {
                num = rs.getInt(1);
                num = num / quantity;
                if (rs.getInt(1) % quantity != 0)
                    ++num;
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return (num == 0)?1:num;
    }

    /**
     * Number of images in a folder.
     * @param idFolder ID of the folder.
     * @return number of images.
     */
    public int getSize_folderFilter(final long idFolder) {
        String cmd = "select count(id) from PHOTO where idDir=?;";
        int num = 0;
        PreparedStatement stm;
        ResultSet rs;

        try {
            stm = DBConnection.getConexion().prepareStatement(cmd);
            stm.setLong(1, idFolder);
            rs = stm.executeQuery();

            if (rs.next())
                num = rs.getInt(1);

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return num;
    }

    /**
     * Retrieves from the database a quantity of photos with the desired name from
     * the photo ID back.
     * @param quantity number of photos.
     * @param fromId the first id to retrieve.
     * @param name name of the filter.
     * @return an array of photos of size 'quantity'.
     */
    public Photo[] getPhotosBeforeID_nameFilter(final int quantity, final long fromId,
            final String name) {
        Photo[] aPhoto = new Photo[quantity];
        String cmd;
        PreparedStatement pst;
        ResultSet rs;
        String str;

        try {
            // get all images
            cmd = "select * from PHOTO where id < ? AND name like ? order by id desc, name asc limit ?;";
            str = "%" + name + "%";

            pst = DBConnection.getConexion().prepareStatement(cmd);
            pst.setLong(1, fromId);
            pst.setString(2, str);
            pst.setInt(3, quantity);

            rs = pst.executeQuery();

            for (int i = 0; i < quantity && rs.next(); i++)
                aPhoto[i] = new Photo(rs);

            // order array by ID
            for (int i = 0; i < aPhoto.length; i++) {
                for (int j = 0; j < aPhoto.length-1; j++) {
                    if (aPhoto[i].getId() < aPhoto[j].getId()) {
                        Photo temp = aPhoto[i];
                        aPhoto[i] = aPhoto[j];
                        aPhoto[j] = temp;
                    }
                }
            }

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return aPhoto;
    }
    //End of FOLDER FILTER.

    //NAME FILTER.
    /**
     * Retrieves from the database a quantity of photos with the desired name from
     * the photo ID forward.
     * @param quantity number of photos.
     * @param fromId the first id to retrieve.
     * @param name of the image.
     * @return an array of photos of size 'quantity'.
     */
    public Photo[] getPhotosFromID_nameFilter(final int quantity, final long fromId,
            final String name) {
    	Photo[] aPhoto = new Photo[quantity];
        String cmd;
        PreparedStatement pst;
        ResultSet rs;
        String str;

        try {
            // get all images
            cmd = "select * from PHOTO where id > ? AND name like ? order by id asc LIMIT ?;";
            str = "%" + name + "%";
            pst = DBConnection.getConexion().prepareStatement(cmd);
            pst.setLong(1, fromId);
            pst.setString(2, str);
            pst.setInt(3, quantity);

            rs = pst.executeQuery();

            for (int i = 0; i < quantity && rs.next(); i++)
                aPhoto[i] = new Photo(rs);

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return aPhoto;
    }

    /**
     * Calculates the number of folders of photos. Name filter count.
     * @param quantity Number of photos per page.
     * @param name filter by this name.
     * @return number of folders. Default: 1.
     */
    public int getNumberFolders_nameFilter(final int quantity, final String name) {
        PreparedStatement stm;
        ResultSet rs;
        String cmd = "select count(id) from PHOTO where name like ?;";
        int num = 0;
        String str;

        str = "%" + name + "%";
        try {
            stm = DBConnection.getConexion().prepareStatement(cmd);
            stm.setString(1, str);
            rs = stm.executeQuery();

            if (rs.next()) {
                num = rs.getInt(1);
                num = num / quantity;
                if (rs.getInt(1) % quantity != 0)
                    ++num;
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return (num == 0)?1:num;
    }

    /**
     * Number of images with a certain name.
     * @param name filter by name.
     * @return number of images.
     */
    public int getSize_nameFilter(final String name) {
        PreparedStatement stm;
        ResultSet rs;
        String cmd = "select count(id) from PHOTO where name like ?;";
        int num = 0;
        String str;

        str = "%" + name + "%";
        try {
            stm = DBConnection.getConexion().prepareStatement(cmd);
            stm.setString(1, str);
            rs = stm.executeQuery();

            if (rs.next())
                num = rs.getInt(1);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return num;
    }

   //End of NAME FILTER.

    //KEY FILTER.
    /**
     * Retrieves from the database a quantity of photos with the desired key from
     * the photo ID back.
     * @param quantity number of photos.
     * @param fromId the first id to retrieve.
     * @param id Key ID.
     * @return an array of photos of size 'quantity'.
     */
    public Photo[] getPhotosBeforeID_keyFilter(final int quantity, final long fromId, final long id) {
        Photo[] aPhoto = new Photo[quantity];
        PreparedStatement pst;
        ResultSet rs;
        String cmd;

        try {
            // get all images
            cmd = "select * from photo where id IN (select idPhoto from PHOTO_KEY where idKey=? AND idPhoto < ?) order by id asc LIMIT ?;";

            pst = DBConnection.getConexion().prepareStatement(cmd);
            pst.setLong(1, id);
            pst.setLong(2, fromId);
            pst.setInt(3, quantity);

            rs = pst.executeQuery();

            for (int i = 0; i < quantity && rs.next(); i++) {
                aPhoto[i] = new Photo(rs);
            }

            // order array by ID
            for (int i = 0; i < aPhoto.length; i++) {
                for (int j = 0; j < aPhoto.length-1; j++) {
                    if (aPhoto[i].getId() < aPhoto[j].getId()) {
                        Photo temp = aPhoto[i];
                        aPhoto[i] = aPhoto[j];
                        aPhoto[j] = temp;
                    }
                }
            }

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return aPhoto;
    }


    /**
     * Retrieves from the database a quantity of photos with the desired key from
     * the photo ID forward.
     * @param quantity number of photos.
     * @param fromId the first id to retrieve.
     * @param id Key ID.
     * @return an array of photos of size 'quantity'.
     */
    public Photo[] getPhotosFromID_keyFilter(final int quantity, final long fromId, final long id) {
        PreparedStatement pst;
        ResultSet rs;
        Photo[] aPhoto = new Photo[quantity];
        String cmd;

        try {
            // get all images
            cmd = "select * from photo where id IN (select idPhoto from PHOTO_KEY where idKey=? AND idPhoto > ?) order by id asc LIMIT ?;";
            pst = DBConnection.getConexion().prepareStatement(cmd);
            pst.setLong(1, id);
            pst.setLong(2, fromId);
            pst.setInt(3, quantity);

            rs = pst.executeQuery();

            for (int i = 0; i < quantity && rs.next(); i++)
                aPhoto[i] = new Photo(rs);

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return aPhoto;
    }

    /**
     * Calculates the number of folders of photos. Key filter count.
     * @param quantity Number of photos per page.
     * @param id Key ID.
     * @return number of folders. Default: 1.
     */
    public int getNumberFolders_keyFilter(final int quantity, final long id) {
        PreparedStatement stm;
        ResultSet rs;
        String cmd = "select count(idPhoto) from PHOTO_KEY where idKey=?;";
        int num = 0;

        try {
            stm = DBConnection.getConexion().prepareStatement(cmd);
            stm.setLong(1, id);
            rs = stm.executeQuery();

            if (rs.next()) {
                num = rs.getInt(1);
                num = num / quantity;
                if(rs.getInt(1) % quantity != 0)
                    ++num;
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return (num == 0)?1:num;
    }

    /**
     * Number of images with a certain key.
     * @param id Key ID.
     * @return number of images.
     */
    public int getSize_keyFilter(final long id) {
        PreparedStatement stm;
        ResultSet rs;
        String cmd = "select count(idPhoto) from PHOTO_KEY where idKey=?;";
        int num = 0;

        try {
            stm = DBConnection.getConexion().prepareStatement(cmd);
            stm.setLong(1, id);
            rs = stm.executeQuery();

            if (rs.next())
                num = rs.getInt(1);

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return num;
    }
    //End of KEY FILTER.

    //AUTHOR FILTER
    /**
     * Retrieves from the database a quantity of photos with the desired author from
     * the photo ID back.
     * @param quantity number of photos.
     * @param fromId the first id to retrieve.
     * @param id Key ID.
     * @return an array of photos of size 'quantity'.
     */
    public Photo[] getPhotosBeforeID_authorFilter(final int quantity, final long fromId, final long id) {
        Photo[] aPhoto = new Photo[quantity];
        PreparedStatement pst;
        ResultSet rs;
        String cmd;

        try {
            // get all images
            cmd = "select * from photo where idAuthor=? AND id < ? order by id asc LIMIT ?;";

            pst = DBConnection.getConexion().prepareStatement(cmd);
            pst.setLong(1, id);
            pst.setLong(2, fromId);
            pst.setInt(3, quantity);

            rs = pst.executeQuery();

            for (int i = 0; i < quantity && rs.next(); i++)
                aPhoto[i] = new Photo(rs);

            // order array by ID
            for (int i = 0; i < aPhoto.length; i++) {
                for (int j = 0; j < aPhoto.length-1; j++) {
                    if (aPhoto[i].getId() < aPhoto[j].getId()) {
                        Photo temp = aPhoto[i];
                        aPhoto[i] = aPhoto[j];
                        aPhoto[j] = temp;
                    }
                }
            }

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return aPhoto;
    }


    /**
     * Retrieves from the database a quantity of photos of the desired author from
     * the photo ID forward.
     * @param quantity number of photos.
     * @param fromId the first id to retrieve.
     * @param id Author ID.
     * @return an array of photos of size 'quantity'.
     */
    public Photo[] getPhotosFromID_authorFilter(final int quantity, final long fromId, final long id) {
    	Photo[] aPhoto = new Photo[quantity];
        PreparedStatement pst;
        ResultSet rs;
        String cmd;

        try {
            // get all images
            cmd = "select * from PHOTO where idAuthor=? AND id > ? order by id asc LIMIT ?;";

            pst = DBConnection.getConexion().prepareStatement(cmd);
            pst.setLong(1, id);
            pst.setLong(2, fromId);
            pst.setInt(3, quantity);

            rs = pst.executeQuery();

            for (int i = 0; i < quantity && rs.next(); i++)
                aPhoto[i] = new Photo(rs);

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return aPhoto;
    }

    /**
     * Calculates the number of folders of photos. Author filter count.
     * @param quantity Number of photos per page.
     * @param id Author ID.
     * @return number of folders. Default: 1.
     */
    public int getNumberFolders_authorFilter(final int quantity, final long id) {
        String cmd = "select count(id) from PHOTO where idAuthor=?;";
        int num = 0;
        PreparedStatement stm;
        ResultSet rs;

        try {
            stm = DBConnection.getConexion().prepareStatement(cmd);
            stm.setLong(1, id);
            rs = stm.executeQuery();

            if (rs.next()) {
                num = rs.getInt(1);
                num = num / quantity;
                if(rs.getInt(1) % quantity != 0)
                    ++num;
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return (num == 0)?1:num;
    }

    /**
     * Number of images with a certain author.
     * @param id Author ID.
     * @return number of images.
     */
    public int getSize_authorFilter(final long id) {
        String cmd = "select count(id) from PHOTO where idAuthor=?;";
        int num = 0;
        PreparedStatement stm;
        ResultSet rs;

        try {
            stm = DBConnection.getConexion().prepareStatement(cmd);
            stm.setLong(1, id);
            rs = stm.executeQuery();

            if (rs.next())
                num = rs.getInt(1);
        } catch (SQLException ex) {
             logger.error(ex.getMessage());
        }

        return num;
    }
    //End of AUTHOR FILTER

    //TOPIC FILTER
    /**
     * Retrieves from the database a quantity of photos with the desired topic from
     * the photo ID back.
     * @param quantity number of photos.
     * @param fromId the first id to retrieve.
     * @param id Topic ID.
     * @return an array of photos of size 'quantity'.
     */
    public Photo[] getPhotosBeforeID_topicFilter(final int quantity, final long fromId, final long id) {
        Photo[] aPhoto = new Photo[quantity];
        PreparedStatement pst;
        ResultSet rs;
        String cmd;

        try {
            // get all images
            cmd = "select * from photo where id IN (select idPhoto from PHOTO_TOPIC "
                    + "where idTopic=? AND idPhoto < ?) order by id asc LIMIT ?;";

            pst = DBConnection.getConexion().prepareStatement(cmd);
            pst.setLong(1, id);
            pst.setLong(2, fromId);
            pst.setInt(3, quantity);

            rs = pst.executeQuery();

            for (int i = 0; i < quantity && rs.next(); i++)
                aPhoto[i] = new Photo(rs);

            // order array by ID
            for (int i = 0; i < aPhoto.length; i++) {
                for (int j = 0; j < aPhoto.length-1; j++) {
                    if (aPhoto[i].getId() < aPhoto[j].getId()) {
                        Photo temp = aPhoto[i];
                        aPhoto[i] = aPhoto[j];
                        aPhoto[j] = temp;
                    }
                }
            }

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return aPhoto;
    }


    /**
     * Retrieves from the database a quantity of photos of the desired topic from
     * the photo ID forward.
     * @param quantity number of photos.
     * @param fromId the first id to retrieve.
     * @param id Topic ID.
     * @return an array of photos of size 'quantity'.
     */
    public Photo[] getPhotosFromID_topicFilter(final int quantity, final long fromId, final long id) {
    	Photo[] aPhoto = new Photo[quantity];
        PreparedStatement pst;
        ResultSet rs;
        String cmd;

        try {
            // get all images
            cmd = "select * from photo where id IN (select idPhoto from PHOTO_TOPIC where idTopic=? AND idPhoto > ?) order by id asc LIMIT ?;";

            pst = DBConnection.getConexion().prepareStatement(cmd);
            pst.setLong(1, id);
            pst.setLong(2, fromId);
            pst.setInt(3, quantity);

            rs = pst.executeQuery();

            for (int i = 0; i < quantity && rs.next(); i++)
                aPhoto[i] = new Photo(rs);

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return aPhoto;
    }

    /**
     * Calculates the number of folders of photos. Topic filter count.
     * @param quantity Number of photos per page.
     * @param id Topic ID.
     * @return number of folders. Default: 1.
     */
    public int getNumberFolders_topicFilter(final int quantity, final long id) {
        String cmd = "select count(idPhoto) from PHOTO_TOPIC where idTopic=?;";
        int num = 0;
        PreparedStatement stm;
        ResultSet rs;

        try {
            stm = DBConnection.getConexion().prepareStatement(cmd);
            stm.setLong(1, id);
            rs = stm.executeQuery();

            if (rs.next()) {
                num = rs.getInt(1);
                num = num / quantity;
                if(rs.getInt(1) % quantity != 0)
                    ++num;
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return (num == 0)?1:num;
    }

    /**
     * Number of images with a certain topic.
     * @param id Topic ID.
     * @return number of images.
     */
    public int getSize_topicFilter(final long id) {
        String cmd = "select count(idPhoto) from PHOTO_TOPIC where idTopic=?;";
        int num = 0;
        PreparedStatement stm;
        ResultSet rs;

        try {
            stm = DBConnection.getConexion().prepareStatement(cmd);
            stm.setLong(1, id);
            rs = stm.executeQuery();

            if (rs.next())
                num = rs.getInt(1);

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return num;
    }
    //End of TOPIC FILTER

  //YEAR FILTER
    /**
     * Retrieves from the database a quantity of photos of the desired year from
     * the photo ID back.
     * @param quantity number of photos.
     * @param fromId the first id to retrieve.
     * @param year year of the photo.
     * @return an array of photos of size 'quantity'.
     */
    public Photo[] getPhotosBeforeID_yearFilter(final int quantity, final long fromId, final int year) {
        Photo[] aPhoto = new Photo[quantity];
        PreparedStatement stm;
        ResultSet rs;
        String cmd;

        try {
            // get all images
            cmd = "select * from PHOTO where id < ? and year=? order by id asc LIMIT ?;";

            PreparedStatement pst = DBConnection.getConexion().prepareStatement(cmd);
            pst.setLong(1, fromId);
            pst.setLong(2, year);
            pst.setInt(3, quantity);

            rs = pst.executeQuery();

            for (int i = 0; i < quantity && rs.next(); i++)
                aPhoto[i] = new Photo(rs);

            // order array by ID
            for (int i = 0; i < aPhoto.length; i++) {
                for (int j = 0; j < aPhoto.length-1; j++) {
                    if (aPhoto[i].getId() < aPhoto[j].getId()) {
                        Photo temp = aPhoto[i];
                        aPhoto[i] = aPhoto[j];
                        aPhoto[j] = temp;
                    }
                }
            }

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return aPhoto;
    }


    /**
     * Retrieves from the database a quantity of photos of the desired year from
     * the photo ID forward.
     * @param quantity number of photos.
     * @param fromId the first id to retrieve.
     * @param year year of the photo.
     * @return an array of photos of size 'quantity'.
     */
    public Photo[] getPhotosFromID_yearFilter(final int quantity, final long fromId, final int year) {
    	Photo[] aPhoto = new Photo[quantity];
        PreparedStatement pst;
        ResultSet rs;
        String cmd;

        try {
            // get all images
            cmd = "select * from PHOTO where id > ? and year = ? order by id asc LIMIT ?;";
            pst = DBConnection.getConexion().prepareStatement(cmd);
            pst.setLong(1, fromId);
            pst.setLong(2, year);
            pst.setInt(3, quantity);

            rs = pst.executeQuery();

            for (int i = 0; i < quantity && rs.next(); i++)
                aPhoto[i] = new Photo(rs);

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return aPhoto;
    }

    /**
     * Calculates the number of folders of photos. Topic filter count.
     * @param quantity Number of photos per page.
     * @param year year of the photo.
     * @return number of folders. Default: 1.
     */
    public int getNumberFolders_yearFilter(final int quantity, final int year) {
        String cmd = "select count(id) from PHOTO where year=?;";
        int num = 0;
        PreparedStatement stm;
        ResultSet rs;

        try {
            stm = DBConnection.getConexion().prepareStatement(cmd);
            stm.setLong(1, year);
            rs = stm.executeQuery();

            if (rs.next()) {
                num = rs.getInt(1);
                num = num / quantity;

                if (rs.getInt(1) % quantity != 0)
                    ++num;
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return (num == 0) ? 1 : num;
    }

    /**
     * Number of images with a certain topic.
     * @param year year of the photo.
     * @return number of images.
     */
    public int getSize_yearFilter(final int year) {
        String cmd = "select count(id) from PHOTO where year=?;";
        int num = 0;
        PreparedStatement stm;
        ResultSet rs;

        try {
            stm = DBConnection.getConexion().prepareStatement(cmd);
            stm.setLong(1, year);
            rs = stm.executeQuery();

            if (rs.next())
                num = rs.getInt(1);

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return num;
    }
    //End of YEAR FILTER

    //MIXED FILTER


    /**
     * Generates a query for a mixed filter.
     * @param filter1 first filter: TOPIC, KEY or FILTER.
     * @param and_or acomplish one condition or both.
     * @param filter2 second filter: TOPIC, KEY or FILTER.
     * @return the query.
     */
    private static String genMixedQuery(final EFilter filter1, final EFilter and_or,
            final EFilter filter2, final EFilter query) {
    	String cmd = "select * from PHOTO where ";

    	// Clause 1
    	switch (query) {
    	case FROM_ID_QUERY:
            switch (filter1) {
            case TOPIC_FILTER:
                cmd += "id IN (select idPhoto from PHOTO_TOPIC where idTopic=? AND idPhoto > ?) ";
                break;
            case KEY_FILTER:
                cmd += "id IN (select idPhoto from PHOTO_KEY where idKey=? AND idPhoto > ?) ";
                break;
            case AUTHOR_FILTER:
                cmd += "idAuthor=? AND id > ? ";
                break;
            default:
                // nothing
            }
            break;
    	case TO_ID_QUERY:
            switch (filter1) {
            case TOPIC_FILTER:
                cmd += "id IN (select idPhoto from PHOTO_TOPIC where idTopic=? AND idPhoto < ?) ";
                break;
            case KEY_FILTER:
                cmd += "id IN (select idPhoto from PHOTO_KEY where idKey=? AND idPhoto < ?) ";
                break;
            case AUTHOR_FILTER:
                cmd += "idAuthor=? AND id < ? ";
                break;
            default:
                // nothing
            }
            break;
        case COUNT_PHOTO_QUERY:
            cmd = "select count(*) from photo where ";
            switch(filter1) {
            case TOPIC_FILTER:
        	cmd += "id IN (select idPhoto from PHOTO_TOPIC where idTopic=?) ";
        	break;
            case KEY_FILTER:
        	cmd += "id IN (select idPhoto from PHOTO_KEY where idKey=?) ";
        	break;
            case AUTHOR_FILTER:
        	cmd += "idAuthor=? ";
        	break;
            default:
                // nothing
            }
            break;
        default:
            // nothing
    	}

    	if (and_or == EFilter.AND) {
            cmd += "AND ";
    	} else {
            cmd += "OR ";
    	}

    	// Clause 2
    	switch (query) {
    	case FROM_ID_QUERY:
            switch(filter2) {
            case TOPIC_FILTER:
                cmd += "id IN (select idPhoto from PHOTO_TOPIC where idTopic=? AND idPhoto > ?) order by id asc LIMIT ?;";
        	break;
            case KEY_FILTER:
        	cmd += "id IN (select idPhoto from PHOTO_KEY where idKey=? AND idPhoto > ?) order by id asc LIMIT ?;";
        	break;
            case AUTHOR_FILTER:
        	cmd += "idAuthor=? AND id > ? order by id asc LIMIT ?;";
        	break;
            default:
                // nothing
        }
    	break;
    	case TO_ID_QUERY:
            switch(filter2) {
            case TOPIC_FILTER:
        	cmd += "id IN (select idPhoto from PHOTO_TOPIC where idTopic=? AND idPhoto < ?) order by id asc LIMIT ?;";
        	break;
            case KEY_FILTER:
        	cmd += "id IN (select idPhoto from PHOTO_KEY where idKey=? AND idPhoto < ?) order by id asc LIMIT ?;";
        	break;
            case AUTHOR_FILTER:
        	cmd += "idAuthor=? AND id < ? order by id asc LIMIT ?;";
        	break;
            default:
                // nothing
            }
            break;
        case COUNT_PHOTO_QUERY:
            switch(filter2) {
            case TOPIC_FILTER:
        	cmd += "id IN (select idPhoto from PHOTO_TOPIC where idTopic=?);";
        	break;
            case KEY_FILTER:
        	cmd += "id IN (select idPhoto from PHOTO_KEY where idKey=?);";
        	break;
            case AUTHOR_FILTER:
        	cmd += "idAuthor=?;";
        	break;
            default:
                // nothing
        }
        break;
        default:
            // nothing
    	}

    	return cmd;
    }

    /**
     * Retrieves from the database a quantity of photos of the desired filters from
     * the photo ID back.
     * @param quantity maximum number of results.
     * @param fromId from the ID back.
     * @param clause1 first filter.
     * @param and_or fullfil one or two conditions.
     * @param clause2 second filter.
     * @return Array of photos.
     */
    public Photo[] getPhotosBeforeID_mixedFilter(final int quantity, final long fromId, final Object clause1,
                                                        final EFilter and_or, final Object clause2) {
        ArrayList<Photo> alPhoto = new ArrayList<Photo>();
        PreparedStatement pst;
        ResultSet rs;
        String cmd;
        Photo[] aPhoto;

        try {
            EFilter f1 = null;
            EFilter f2 = null;
            long id1 = 0;
            long id2 = 0;

            if (clause1 instanceof Topic) {
            	f1 = EFilter.TOPIC_FILTER;
            	id1 = ((Topic) clause1).getId();
            } else if (clause1 instanceof Key) {
            	f1 = EFilter.KEY_FILTER;
            	id1 = ((Key) clause1).getId();
            } else if (clause1 instanceof Author) {
            	f1 = EFilter.AUTHOR_FILTER;
            	id1 = ((Author) clause1).getId();
            }

            if (clause2 instanceof Topic) {
            	f2 = EFilter.TOPIC_FILTER;
            	id2 = ((Topic) clause2).getId();
            } else if (clause2 instanceof Key) {
            	f2 = EFilter.KEY_FILTER;
            	id2 = ((Key) clause2).getId();
            } else if (clause2 instanceof Author) {
            	f2 = EFilter.AUTHOR_FILTER;
            	id2 = ((Author) clause2).getId();
            }

            cmd = genMixedQuery(f1, and_or, f2, EFilter.TO_ID_QUERY);

            // get all images
            pst = DBConnection.getConexion().prepareStatement(cmd);
            pst.setLong(1, id1);
            pst.setLong(2, fromId);
            pst.setLong(3, id2);
            pst.setLong(4, fromId);
            pst.setInt(5, quantity);

            rs = pst.executeQuery();

            for (int i = 0; i < quantity && rs.next(); i++)
                alPhoto.add(new Photo(rs));


            aPhoto = alPhoto.toArray(new Photo[alPhoto.size()]);
            // order array by ID
            for (int i = 0; i < aPhoto.length-1; i++) {
                for (int j = i + 1; j < aPhoto.length; j++) {
                    if (aPhoto[i].getId() < aPhoto[j].getId()) {
                        Photo temp = aPhoto[i];
                        aPhoto[i] = aPhoto[j];
                        aPhoto[j] = temp;
                    }
                }
            }

            rs.close();
            pst.close();
            return aPhoto;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return null;
        }
    }


    /**
     * Retrieves from the database a quantity of photos of the desired filters from
     * the photo ID forward.
     * @param quantity maximum number of results.
     * @param fromId from the ID back.
     * @param clause1 first filter.
     * @param and_or fullfil one or two conditions.
     * @param clause2 second filter.
     * @return Array of photos.
     */
    public Photo[] getPhotosFromID_mixedFilter(final int quantity, final long fromId, final Object clause1,
                                                      final EFilter and_or, final Object clause2) {
    	ArrayList<Photo> alPhoto = new ArrayList<Photo>();
        PreparedStatement pst;
        ResultSet rs;
        String cmd;
        Photo[] aPhoto;

        EFilter f1 = null;
        EFilter f2 = null;
        long id1 = 0;
        long id2 = 0;

        try {
            if (clause1 instanceof Topic) {
            	f1 = EFilter.TOPIC_FILTER;
            	id1 = ((Topic) clause1).getId();
            } else if (clause1 instanceof Key) {
            	f1 = EFilter.KEY_FILTER;
            	id1 = ((Key) clause1).getId();
            } else if (clause1 instanceof Author) {
            	f1 = EFilter.AUTHOR_FILTER;
            	id1 = ((Author) clause1).getId();
            }

            if (clause2 instanceof Topic) {
            	f2 = EFilter.TOPIC_FILTER;
            	id2 = ((Topic) clause2).getId();
            } else if (clause2 instanceof Key) {
            	f2 = EFilter.KEY_FILTER;
            	id2 = ((Key) clause2).getId();
            } else if (clause2 instanceof Author) {
            	f2 = EFilter.AUTHOR_FILTER;
            	id2 = ((Author) clause2).getId();
            }

            cmd = genMixedQuery(f1, and_or, f2, EFilter.FROM_ID_QUERY);

            // get all images
            pst = DBConnection.getConexion().prepareStatement(cmd);
            pst.setLong(1, id1);
            pst.setLong(2, fromId);
            pst.setLong(3, id2);
            pst.setLong(4, fromId);
            pst.setInt(5, quantity);

            rs = pst.executeQuery();

            for (int i = 0; i < quantity && rs.next(); i++)
            	alPhoto.add(new Photo(rs));

            aPhoto = alPhoto.toArray(new Photo[alPhoto.size()]);

            // order array by ID
            for (int i = 0; i < aPhoto.length-1; i++) {
                for (int j = i + 1; j < aPhoto.length; j++) {
                    if (aPhoto[i].getId() < aPhoto[j].getId()) {
                        Photo temp = aPhoto[i];
                        aPhoto[i] = aPhoto[j];
                        aPhoto[j] = temp;
                    }
                }
            }

            rs.close();
            pst.close();
            return aPhoto;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return null;
        }
    }

    /**
     * Calculates the number of folders of photos. Topic filter count.
     * @param quantity Number of photos per page.
     * @param clause1
     * @param and_or
     * @param clause2
     * @return number of folders. Default: 1.
     */
    public int getNumberFolders_mixedFilter(final int quantity, final Object clause1, final EFilter and_or,
                                                   final Object clause2) {
	int num = getSize_mixedFilter(clause1, and_or, clause2);
	int aux = 0;

	if (num > 0) {
            aux = num / quantity;
            if (num % quantity != 0)
		++aux;
	}

	return (aux == 0) ? 1 : aux;
    }

    /**
     * Number of images with a certain topic.
     * @param clause1
     * @param and_or
     * @param clause2
     * @return number of images.
     */
    public int getSize_mixedFilter(final Object clause1, final EFilter and_or, final Object clause2) {
    	String cmd;
        PreparedStatement stm;
        ResultSet rs;
        int num;

        EFilter f1 = null;
    	EFilter f2 = null;
    	long id1 = 0;
    	long id2 = 0;

        if (clause1 instanceof Topic) {
            f1 = EFilter.TOPIC_FILTER;
            id1 = ((Topic) clause1).getId();
        } else if (clause1 instanceof Key) {
            f1 = EFilter.KEY_FILTER;
            id1 = ((Key) clause1).getId();
        } else if (clause1 instanceof Author) {
            f1 = EFilter.AUTHOR_FILTER;
            id1 = ((Author) clause1).getId();
        }

        if (clause2 instanceof Topic) {
            f2 = EFilter.TOPIC_FILTER;
            id2 = ((Topic) clause2).getId();
        } else if (clause2 instanceof Key) {
            f2 = EFilter.KEY_FILTER;
            id2 = ((Key) clause2).getId();
        } else if (clause2 instanceof Author) {
            f2 = EFilter.AUTHOR_FILTER;
            id2 = ((Author) clause2).getId();
        }

        cmd = genMixedQuery(f1, and_or, f2, EFilter.COUNT_PHOTO_QUERY);
        num = 0;
        try {
            stm = DBConnection.getConexion().prepareStatement(cmd);
            stm.setLong(1, id1);
            stm.setLong(2, id2);
            rs = stm.executeQuery();

            if (rs.next())
                num = rs.getInt(1);

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return num;
    }
    //End of MIXED FILTER
}
