/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.utils;

import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Thomas
 */
public class CanvasImageUtils {

    public static BufferedImage getImage(String image) {
        try {
            if (!image.contains("/") && !image.contains("\\")) {
                return makeCompatible(ImageIO.read(CanvasImageUtils.class.getClassLoader().getResource("resources/images/" + image)));
            } else {
                return makeCompatible(ImageIO.read(CanvasImageUtils.class.getClassLoader().getResource(image)));
            }
        } catch (IOException ex) {
            Logger.getLogger(CanvasImageUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public static BufferedImage getImage(File file) {
        try {
            return makeCompatible(ImageIO.read(file));
        } catch (IOException ex) {
            Logger.getLogger(CanvasImageUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    private static BufferedImage makeCompatible(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.drawRenderedImage(image, new AffineTransform()); //or some other drawImage function
        g.dispose();

        return result;
    }
}
