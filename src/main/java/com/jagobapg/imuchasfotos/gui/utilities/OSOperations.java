/*
 * Consulted mkyong.com for writing this class.
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * OSOperations.java
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
package com.jagobapg.imuchasfotos.gui.utilities;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import com.jagobapg.imuchasfotos.dto.Log;

/**
 * This class provides command support for different operative systems.
 */
public final class OSOperations {

    private static final String OS = System.getProperty("os.name").toLowerCase(); //Operative System of the machine.

    /**
     * Opens a web page with the default navigator of the user.
     *
     * @param uri URI
     */
    public static void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                Log.createLog(e.getMessage());
            }
        }
    }

    /**
     * Copy a file.
     *
     * @param org origin path.
     * @param dst destiny path.
     * @return true if success.
     */
    public static boolean copyFile(String org, String dst) {
        String cmd = "";
        try {
            if (isWindows()) {
                cmd = "cmd /c copy \"" + org + "\" \"" + dst + "\"";
                Runtime.getRuntime().exec(cmd);
            } else if (isMac() || isUnix() || isSolaris()) {
                cmd = "cp -f \"" + org + "\" \"" + dst + "\"";
                Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", cmd});
            }
        } catch (IOException ex) {
            return false;
            /* Not launched properly. */ }

        return true; //Properly launched.
    }

    /**
     * Remove a file.
     *
     * @param dir File to be removed.
     * @return
     */
    public static boolean removeFile(String dir) {
        String cmd = "";
        try {
            if (isWindows()) {
                cmd = "cmd /c del \"" + dir + "\"";
                Runtime.getRuntime().exec(cmd);
            } else if (isMac() || isUnix() || isSolaris()) {
                cmd = "rm \"" + dir + "\"";
                Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", cmd});
            }
        } catch (IOException ex) {
            return false;
            /* Not launched properly. */ }

        return true; //Properly launched.
    }

    /**
     * Remove a directory.
     *
     * @param dir Directory to be removed.
     * @return
     */
    public static boolean removeDirectory(String dir) {
        String cmd = "";
        try {
            if (isWindows()) {
                cmd = "cmd /c rmdir /S /Q \"" + dir + "\"";
                Runtime.getRuntime().exec(cmd);
            } else if (isMac() || isUnix() || isSolaris()) {
                cmd = "rm -rf \"" + dir + "\"";
                Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", cmd});
            }
        } catch (IOException ex) {
            return false;
            /* Not launched properly. */ }

        return true; //Properly launched.
    }

    /**
     * Detected OS is windows system.
     *
     * @return is windows.
     */
    public static boolean isWindows() {
        return (OS.contains("win"));
    }

    /**
     * Detected OS is macintosh system.
     *
     * @return is Mac.
     */
    public static boolean isMac() {
        return (OS.contains("mac"));
    }

    /**
     * Detected OS is unix based system.
     *
     * @return is unix based.
     */
    public static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0);
    }

    /**
     * Detected OS is solaris system.
     *
     * @return is solaris.
     */
    public static boolean isSolaris() {
        return (OS.contains("sunos"));
    }
}
