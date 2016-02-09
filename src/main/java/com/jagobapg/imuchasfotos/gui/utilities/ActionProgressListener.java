/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * ActionProgressListener.java
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

/** This class provides the methods needed for managing the behaviour
 * of a JProgressBar.
 */
public interface ActionProgressListener {
    
    /**
     * Set the number operations to be processed.
     * @param operations Number of operations to be performed.
     */
    public void setNumberOperations(long operations);
    
    /**
     * Operation is performed.
     */
    public void operationDone();
 
    /**
     * Set a description about the operation.
     * @param text description.
     */
    public void setInformationText(String text);
    
    /**
     * Execute something after finish the operation.
     * @param txt Informative text.
     */
    public void finished(String txt);
    
}
