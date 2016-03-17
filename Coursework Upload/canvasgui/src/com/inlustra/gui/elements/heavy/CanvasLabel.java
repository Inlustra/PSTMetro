/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.elements.heavy;

import com.inlustra.gui.elements.CanvasElement;
import com.inlustra.gui.utils.BlendComposite;
import com.inlustra.gui.utils.CanvasFont;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 *
 * @author Thomas
 */
public class CanvasLabel extends CanvasElement {

    private String text;
    private Color color;
    private Font font;
    private Image image;

    public CanvasLabel(String label, int x, int y) {
        this(label, null, CanvasFont.getFont("Walkway SemiBold.ttf"), x, y);
    }

    public CanvasLabel(String label, Image img, int x, int y) {
        this(label, img, CanvasFont.getFont("Walkway SemiBold.ttf"), x, y);
    }

    public CanvasLabel(String label, Image image, Font font, int x, int y) {
        this(label, Color.black, image, font, x, y);
    }

    public CanvasLabel(String label, Color color, Image image, int x, int y) {
        this(label, color, image, CanvasFont.getFont("Walkway SemiBold.ttf"), x, y);
    }

    public CanvasLabel(String label, Color color, Image image, Font font, int x, int y) {
        super(CanvasPosition.FOREGROUND, x, y,
                (int) CanvasFont.getFontBounds(font, label).getWidth(),
                (int) CanvasFont.getFontBounds(font, label).getHeight());
        this.text = label;
        this.font = font;
        this.image = image;
        this.color = color;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setText(String label) {
        this.text = label;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setFontSize(float size) {
        this.font = font.deriveFont(size);
    }

    public String getText() {
        return text;
    }

    public Font getFont() {
        return font;
    }

    @Override
    public void draw(Graphics2D g2d, float delta) {
        FontMetrics fm = g2d.getFontMetrics(font);
        g2d.setFont(font);
        int currentX = 0;
        if (image != null) {
            g2d.drawImage(image, 0, 0, image.getWidth(null), image.getHeight(null), null);
            currentX += image.getWidth(null);
        }
        g2d.setColor(color);
        g2d.drawString(text, currentX, 0);
    }
}
