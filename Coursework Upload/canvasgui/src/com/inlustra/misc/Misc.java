/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.misc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author Thomas
 */
public class Misc {

    public static boolean isWithin(Point position, int x, int y, int width, int height) {
        if (position.getX() > x && position.getX() < x + width) {
            if (position.getY() > y && position.getY() < y + height) {
                return true;
            }
        }
        return false;
    }

    public static float lerp(float start, float target, float duration, float timeSinceStart) {
        float value = start;
        if (timeSinceStart > 0.0f && timeSinceStart < duration) {
            final float range = target - start;
            final float percent = timeSinceStart / duration;
            value = start + (range * percent);
        } else if (timeSinceStart >= duration) {
            value = target;
        }
        return value;
    }

    public static float ease(float start, float target, float duration, float timeSinceStart) {
        float value = start;
        if (timeSinceStart > 0.0f && timeSinceStart < duration) {
            final float range = target - start;
            final float percent = timeSinceStart / (duration / 2.0f);
            if (percent < 1.0f) {
                value = start + ((range / 2.0f) * percent * percent * percent);
            } else {
                final float shiftedPercent = percent - 2.0f;
                value = start + ((range / 2.0f)
                        * ((shiftedPercent * shiftedPercent * shiftedPercent) + 2.0f));
            }
        } else if (timeSinceStart >= duration) {
            value = target;
        }
        return value;
    }

    public static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        double scaleX = (double) width / imageWidth;
        double scaleY = (double) height / imageHeight;
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        return bilinearScaleOp.filter(
                image,
                new BufferedImage(width, height, image.getType()));
    }

    public static BufferedImage recolor(BufferedImage img, Color oldColor, Color newColor) {
        BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        int oldRGB = oldColor.getRGB();
        int newRGB = newColor.getRGB();
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int imgRGB = img.getRGB(x, y);
                if (imgRGB == oldRGB) {
                    bi.setRGB(x, y, newRGB);
                } else {
                    bi.setRGB(x, y, imgRGB);
                }
            }
        }
        return bi;
    }

    public static int getRandomWithin(int Min, int Max) {
        return Min + (int) (Math.random() * ((Max - Min) + 1));
    }
}
