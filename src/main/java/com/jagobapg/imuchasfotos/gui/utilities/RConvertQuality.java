/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * RConvertQuality.java
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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.jagobapg.imuchasfotos.dto.Log;
import com.jagobapg.imuchasfotos.dto.Photo;

/* This class resizes a list of images to a desired quality:
 * HIGH: Real size.
 * MEDIUM: Maximum 1280px
 * LOW: 512x512 */
public class RConvertQuality implements Runnable {

    public enum EQuality {
        LOW, MEDIUM, HIGH
    }
    private Photo[] aPhotos;
    private EQuality quality;
    private File[] aConvertedPhotos;

    /**
     * Constructs a RConvertQuality object.
     *
     * @param aPhotos photos to convert.
     */
    public RConvertQuality(Photo[] aPhotos, EQuality quality) {
        this.aPhotos = aPhotos;
        this.quality = quality;
    }

    @Override
    public void run() {
        switch (this.quality) {
            case LOW:
                convertLowQuality();
                break;
            case MEDIUM:
                convertMediumQuality();
                break;
            case HIGH:
                convertHighQuality();
                break;
        }
    }

    /**
     * Convert the photos into low quality photos.
     */
    private void convertLowQuality() {
        this.aConvertedPhotos = new File[aPhotos.length];
        Photo currentPhoto;
        ImageIcon ii;
        Image image;
        BufferedImage bi;
        File output;

        for (int i = 0; i < aPhotos.length; i++) {
            try {
                currentPhoto = aPhotos[i];
                ii = new ImageIcon(currentPhoto.getFile());
                image = ii.getImage().getScaledInstance(512,
                        512,
                        Image.SCALE_SMOOTH);
                ii = new ImageIcon(image);
                bi = toBufferedImage(ii.getImage());
                output = new File("temp" + File.separatorChar + i + ".jpg");
                ImageIO.write(bi, "jpg", output);
                this.aConvertedPhotos[i] = output;
            } catch (IOException e) {
                Log.createLog(e.getMessage());
            }

        }
    }

    /**
     * Convert the photos into high quality photos.
     */
    private void convertMediumQuality() {
        this.aConvertedPhotos = new File[aPhotos.length];
        Photo currentPhoto;
        ImageIcon ii;
        Image image;
        BufferedImage bi;
        File output;

        for (int i = 0; i < aPhotos.length; i++) {
            try {
                currentPhoto = aPhotos[i];

                ii = new ImageIcon(currentPhoto.getFile());
                ii = new ImageIcon(setMediumQualityResolution(ii));

                bi = toBufferedImage(ii.getImage());
                output = new File("temp" + File.separatorChar + i + ".jpg");
                ImageIO.write(bi, "jpg", output);
                this.aConvertedPhotos[i] = output;
            } catch (IOException e) {
                Log.createLog(e.getMessage());
            }

        }
    }

    /**
     * Convert the photos into medium quality photos.
     */
    private void convertHighQuality() {
        this.aConvertedPhotos = new File[aPhotos.length];
        Photo currentPhoto;
        ImageIcon ii;
        Image image;
        BufferedImage bi;
        File output;

        for (int i = 0; i < aPhotos.length; i++) {
            try {
                currentPhoto = aPhotos[i];
                image = new ImageIcon(
                        currentPhoto.getFile()).getImage();
                bi = toBufferedImage(image);
                output = new File("temp/" + i + ".jpg");
                ImageIO.write(bi, "jpg", output);
                this.aConvertedPhotos[i] = output;
            } catch (IOException e) {
                Log.createLog(e.getMessage());
            }

        }
    }

    /**
     * Source: Sri Harsha Chilakapati Converts a given Image into a
     * BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(Image img) {
        BufferedImage bimage;
        Graphics2D bGr;

        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        bimage = new BufferedImage(img.getWidth(null),
                img.getHeight(null),
                BufferedImage.TYPE_INT_RGB);

        // Draw the image on to the buffered image
        bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    /**
     * Scale a image to reduce its quality. The image longest side will be of
     * 1024px.
     *
     * @param ii Image to transform.
     * @return transformed image.
     */
    public static Image setMediumQualityResolution(ImageIcon ii) {
        final double MAX_PIXELS = 1024;

        if (ii.getIconWidth() > MAX_PIXELS && ii.getIconHeight() > MAX_PIXELS) {
            double scale;
            int x = 0, y = 0;

            if (ii.getIconWidth() > ii.getIconHeight()) {
                scale = MAX_PIXELS / ii.getIconWidth();
                x = (int) MAX_PIXELS;
                y = (int) (ii.getIconHeight() * scale);
            } else {
                scale = MAX_PIXELS / ii.getIconHeight();
                y = (int) MAX_PIXELS;
                x = (int) (ii.getIconWidth() * scale);
            }

            return ii.getImage().getScaledInstance(x, y, Image.SCALE_SMOOTH);
        }
        // If not use original resolution.
        return ii.getImage();
    }

    /**
     * Return the converted files.
     *
     * @return files converted in the desired quality.
     */
    public File[] getConvertedPhotos() {
        return this.aConvertedPhotos;
    }

}
