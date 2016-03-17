/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.elements.heavy;

import com.inlustra.gui.elements.CanvasElement;
import com.inlustra.gui.mouse.CanvasMouseDragEvent;
import com.inlustra.gui.mouse.CanvasMouseEvent;
import com.inlustra.gui.mouse.CanvasMouseListener;
import com.inlustra.gui.mouse.CanvasMouseReleaseEvent;
import com.inlustra.gui.mouse.MouseUtils;
import com.inlustra.gui.utils.BlendComposite;
import com.inlustra.gui.utils.CanvasFont;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Thomas
 */
public abstract class CanvasButton extends CanvasElement implements CanvasMouseListener {

    protected Image image;
    protected String text;
    protected Font font;
    protected BlendComposite textBlendType = BlendComposite.Normal;
    protected Color foregroundColor = Color.white;

    public CanvasButton(String label, Color color, int x, int y) {
        this(label, color, CanvasFont.getFont("Walkway SemiBold.ttf").deriveFont(30f), x, y);
    }

    public CanvasButton(String label, Color color, Font font, int x, int y) {
        this(label, color, font, x, y,
                (int) CanvasFont.getFontBounds(font, label).getWidth() + 10,
                (int) CanvasFont.getFontBounds(font, label).getHeight());
    }

    public CanvasButton(String label, Color color, int x, int y, int width, int height) {
        this(label, color, CanvasFont.getFont("Walkway SemiBold.ttf").deriveFont((float) height), x, y,
                width, height);
    }

    public CanvasButton(Image image, Color color, int x, int y, int width, int height) {
        super(CanvasPosition.FOREGROUND, x, y, width, height);
        this.mouseOverCursor = new Cursor(Cursor.HAND_CURSOR);
        this.text = "";
        this.image = image;
        this.backgroundColor = color;
    }

    public CanvasButton(String label, Color color, Font font, int x, int y, int width, int height) {
        super(CanvasPosition.FOREGROUND, x, y, width, height);
        this.mouseOverCursor = new Cursor(Cursor.HAND_CURSOR);
        this.text = label;
        this.font = font;
        this.backgroundColor = color;
    }

    @Override
    public void draw(Graphics2D g2d, float delta) {
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, width, height);
        if (!text.isEmpty()) {
            FontMetrics fm = g2d.getFontMetrics(font);
            g2d.setColor(foregroundColor);
            g2d.setFont(font);
            Rectangle2D bounds = fm.getStringBounds(text, g2d);
            g2d.setComposite(textBlendType);
            g2d.drawString(text, (int) ((width - bounds.getWidth()) / 2.0), (int) ((height - bounds.getHeight()) / 2 + fm.getAscent()) - fm.getDescent() / 2);
            g2d.setComposite(BlendComposite.Normal);
        }
        if (image != null) {
            g2d.drawImage(image, 5, 5, width - 10, height - 10, null);
        }
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public BlendComposite getTextBlendType() {
        return textBlendType;
    }

    public void setTextBlendType(BlendComposite textBlendType) {
        this.textBlendType = textBlendType;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public abstract void onButtonPress(CanvasButton source);

    @Override
    public void mouseDragged(CanvasMouseDragEvent e) {
    }

    @Override
    public void mousePressed(CanvasMouseEvent e) {
    }

    @Override
    public void mouseClicked(CanvasMouseEvent e) {
        requestKeyFocus(this);
        onButtonPress(this);
    }

    @Override
    public void mouseReleased(CanvasMouseReleaseEvent e) {
    }

    @Override
    public void mouseMoved(CanvasMouseEvent e) {
    }

    @Override
    public boolean clickThrough() {
        return false;
    }

    @Override
    public Cursor getMouseOverCursor() {
        return MouseUtils.HAND_CURSOR;
    }
}
