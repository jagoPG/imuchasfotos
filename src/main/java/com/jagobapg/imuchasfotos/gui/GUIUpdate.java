/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * GUIUpdate.java
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
package com.jagobapg.imuchasfotos.gui;

import com.jagobapg.imuchasfotos.gui.utilities.LanguageController;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import javax.swing.JPanel;

import com.jagobapg.imuchasfotos.gui.utilities.OSOperations;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/* This class launches the update manager. The update manager is called iUpdate.jar and is a separate program
 * because the files to update have to be overwritten. */
public class GUIUpdate extends JDialog {
    
    private final Logger logger = LogManager.getLogger(GUIUpdate.class);

    private static final long serialVersionUID = -6464011567883332087L;
    
    // iMuchasFotos version
    public static final String SW_CURRENT_VERSION = "2.2016";
    public static final String SW_DOWNLOAD_URL = "http://imuchasfotos.sourceforge.net";
    public static final String SW_VERSION = "http://www.jagobapg.eu/projects/imuchasfotos/version";
    
    public GUIUpdate(Frame parent) {
        super(parent, true);
        initComponents(parent);

        softwareVersion();
        checkUpdate();
    }

    /* Load design. */
    private void initComponents(Frame parent) {
        setTitle(LanguageController.INSTANCE.getString("update"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        setUndecorated(true);

        this.pnlCurrentVersion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.lblCurrentVersion = new JLabel(LBL_CURRENT_VERSION);
        this.pnlMostRecentVersion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.lblNextVersion = new JLabel(LBL_RECENT_VERSION);
        this.pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.btnUpdate = new JButton(LanguageController.INSTANCE.getString("update"));
        this.btnClose = new JButton(LanguageController.INSTANCE.getString("close"));

        /* Top JPanel. */
        this.add(pnlCurrentVersion, BorderLayout.NORTH);
        this.pnlCurrentVersion.add(lblCurrentVersion);
        this.pnlCurrentVersion.setBorder(BorderFactory.createEtchedBorder());

        /* Centre JPanel. */
        this.add(pnlMostRecentVersion, BorderLayout.CENTER);
        this.pnlMostRecentVersion.add(lblNextVersion);
        this.pnlMostRecentVersion.setBorder(BorderFactory.createEtchedBorder());

        /* Bottom JPanel. */
        this.add(pnlButtons, BorderLayout.SOUTH);
        this.pnlButtons.add(btnUpdate);
        this.pnlButtons.add(btnClose);
        this.btnUpdate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnUpdateActionListener(e);
            }

        });

        this.btnClose.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnCloseActionListener(e);
            }

        });

        if (parent != null) {
            this.setLocation(parent.getX() + 10, parent.getY() + 10);
        }

        pack();
    }

    /* Software version. */
    private void softwareVersion() {
        this.lblCurrentVersion.setText(LBL_CURRENT_VERSION + SW_CURRENT_VERSION);
    }

    /* Check next version. */
    private void checkUpdate() {
        try {
            URL url = new URL(SW_VERSION);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String newVersion = reader.readLine();
            reader.close();

            this.lblNextVersion.setText(LBL_RECENT_VERSION + newVersion);
            this.btnUpdate.setEnabled(!newVersion.equals(SW_CURRENT_VERSION));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /* Close window. */
    private void btnCloseActionListener(ActionEvent e) {
        dispose();
    }

    /* Open software webpage*/
    private void btnUpdateActionListener(ActionEvent e) {
        try {
            OSOperations.openWebpage(new URI(SW_DOWNLOAD_URL));
        } catch (URISyntaxException ex) {
            logger.error(ex.getMessage());
        }
    }

    //Variable declaration
    private JPanel pnlCurrentVersion;
    private JLabel lblCurrentVersion;
    private static final String LBL_CURRENT_VERSION = LanguageController.INSTANCE.getString("actual_version") + ": ";
    private JPanel pnlMostRecentVersion;
    private JLabel lblNextVersion;
    private static final String LBL_RECENT_VERSION = LanguageController.INSTANCE.getString("recent_version") + ": ";
    private JPanel pnlButtons;
    private JButton btnUpdate;
    private JButton btnClose;
    //End of variable declaration
}
