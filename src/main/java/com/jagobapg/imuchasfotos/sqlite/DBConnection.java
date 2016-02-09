/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * DBConnection.java
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

import com.jagobapg.imuchasfotos.gui.GUIErrorFound;
import com.jagobapg.imuchasfotos.gui.utilities.OSOperations;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.jagobapg.imuchasfotos.dto.Log;
import com.jagobapg.imuchasfotos.gui.GUIUpdate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stablish connection with the database. Now its running under SQLite and the
 * file is located into the program folder. Only one connection will be managed;
 * the first time getConexion() is called the Connection will be established,
 * until closeConexion() is invoked.
 */
public final class DBConnection {

    private static Connection cnn;

    /**
     * Connection to the database.
     *
     * @return Connection object.
     */
    public static Connection getConexion() {
        if (DBConnection.cnn == null) { //Open a new connection.
            try {
                Class.forName("org.sqlite.JDBC");
                cnn = DriverManager.getConnection("jdbc:sqlite:iMuchasFotos.s3db");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,
                        "Faltan ficheros de programa.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                Log.createLog(ex.getMessage());
            } catch (ClassNotFoundException ex) {
                try {
                    new GUIErrorFound(-1, ex).setVisible(true);
                    Log.createLog(ex.getMessage());
                    OSOperations.openWebpage(new URI(GUIUpdate.SW_DOWNLOAD_URL));
                } catch (URISyntaxException ex1) {
                    Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }

        return cnn;
    }

    /**
     * Close the connection
     *
     */
    public static void closeConexion() {
        if (cnn != null) {
            try {
                cnn.close();
            } catch (SQLException ex) {
                new GUIErrorFound(ex.getErrorCode(), ex).setVisible(true);
            }
        }
    }

    /**
     * Test of the DBConnection.java class.
     */
    public static void main(String[] args) throws SQLException {
        java.sql.Statement stm = DBConnection.getConexion().createStatement();
        stm.close();
    }
}
