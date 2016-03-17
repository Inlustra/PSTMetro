/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.containers.overlay;

import com.inlustra.gui.containers.CanvasContainer;
import com.inlustra.gui.utils.BlendComposite;
import com.inlustra.gui.utils.CanvasFont;
import com.inlustra.misc.Misc;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Thomas
 */
public class CanvasFadeMessageOverlay extends CanvasOverlay {

    String message;
    Color foregroundColor;
    Font font;
    float alpha = 255;
    float timePassed = 0;
    int fadeTime;

    public CanvasFadeMessageOverlay(String message, int fadeTime, Color color, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.message = message;
        this.fadeTime = fadeTime;
        foregroundColor = color;
        font = CanvasFont.getFont("Walkway SemiBold.ttf").deriveFont((float) 50);
        font.deriveFont(Font.BOLD);
    }

    @Override
    public void draw(Graphics2D g2d, float delta) {
        timePassed += delta;
        alpha = Misc.ease(1, 0, fadeTime, timePassed);
        if (alpha == 0) {
            parent.removeElement(this);
        }
        FontMetrics fm = g2d.getFontMetrics(font);
        g2d.setColor(new Color(foregroundColor.getRed(), foregroundColor.getGreen(), foregroundColor.getBlue(), 255));
        g2d.setFont(font);
        Rectangle2D bounds = fm.getStringBounds(message, g2d);
        g2d.setComposite(BlendComposite.Average.derive(alpha));
        g2d.drawString(message, (int) ((width - bounds.getWidth()) / 2.0), (int) (((height / 2) - bounds.getHeight()) + fm.getAscent()) - fm.getDescent());
        g2d.setComposite(BlendComposite.Normal);
    }

    public static void displayMessage(CanvasContainer parent, String message) {
        displayMessage(parent, message, 2, Color.BLACK);
    }

    public static void displayMessage(CanvasContainer parent, String message, int fadeTimeSeconds, Color color) {
        parent.addElement(new CanvasFadeMessageOverlay(message, fadeTimeSeconds, color, 0, 0, parent.getWidth(), parent.getHeight()));
    }
}
