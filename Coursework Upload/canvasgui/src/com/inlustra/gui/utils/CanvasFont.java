/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Thomas
 */
public class CanvasFont {

    private static final Font SERIF_FONT = new Font("serif", Font.PLAIN, 24);

    public static Font getFont(String name) {
        Font font = null;
        if (name == null) {
            return SERIF_FONT;
        }
        try {
           font = Font.createFont(Font.TRUETYPE_FONT, CanvasFont.class.getClassLoader().getResourceAsStream("resources/font/"+name));
        } catch (FontFormatException | IOException ex) {
            ex.printStackTrace();
            System.err.println(name + " not loaded.  Using serif font.");
            font = SERIF_FONT;
        }
        return font;
    }

    public static Rectangle2D getFontBounds(Font font, String text) {
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        return font.getStringBounds(text, frc);
    }
}
