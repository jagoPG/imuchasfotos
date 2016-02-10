/*
 * Jagoba Pérez Copyright 2016
 * This program is distributed under the terms of the GNU General Public License
 * 
 * LanguageController.java
 * 
 * This file is part of Image Manager.
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

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public enum LanguageController {
    INSTANCE;
    
    private final HashMap<String, Locale> langSupported;
    private ResourceBundle translation;
    
    /**
     * Create a new Language Controller for managing different languages in the application.
     * @param language language
     */
    LanguageController() {
        langSupported = new HashMap();
        langSupported.put("Spanish", new Locale("Spanish", "Spain", "es"));
        langSupported.put("English", Locale.ENGLISH);
        
    }
    
    /**
     * Set a new language to the application
     * @param language language
     */
    public void setLanguage(String language) {
        translation = ResourceBundle.getBundle("language", langSupported.get(language));
    } 
    
    /**
     * Get a string from a translation file
     * @param key key of the string
     * @return associated string
     */
    public String getString(String key) {
        return translation.getString(key);
    }
    
}
