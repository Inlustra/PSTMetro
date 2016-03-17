/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.elements.input;

import com.inlustra.gui.elements.CanvasElement;
import com.inlustra.gui.mouse.CanvasMouseDragEvent;
import com.inlustra.gui.mouse.CanvasMouseEvent;
import com.inlustra.gui.mouse.CanvasMouseListener;
import com.inlustra.gui.mouse.CanvasMouseReleaseEvent;
import com.inlustra.gui.mouse.MouseUtils;
import com.inlustra.gui.utils.CanvasFont;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Thomas
 */
public class TextInputElement extends CanvasElement implements CanvasMouseListener {

    String text = "dsfhdfh";
    Font font;

    public TextInputElement(int x, int y, int width, int height) {
        super(x, y, width, height);
        font = CanvasFont.getFont("Walkway SemiBold.ttf").deriveFont(50);
    }
    float timePassed = 0;

    @Override
    public void draw(Graphics2D g2d, float delta) {
        timePassed += delta;
        FontMetrics fm = g2d.getFontMetrics();
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, width, height);
        g2d.setFont(font);
        Rectangle2D bounds = fm.getStringBounds(text, g2d);
        g2d.setColor(Color.black);
        if ((int) timePassed % 2 == 0) {
            System.out.println(timePassed + " " + (timePassed % 2));
            g2d.drawString("|", (int) bounds.getWidth() + 5, (int) ((height - bounds.getHeight()) / 2 + fm.getAscent()) - fm.getDescent() / 2);
        } else {
        }
        g2d.drawString(text, 5, 5);
    }

    @Override
    public void mouseMoved(CanvasMouseEvent e) {
    }

    @Override
    public void mouseDragged(CanvasMouseDragEvent e) {
    }

    @Override
    public void mousePressed(CanvasMouseEvent e) {
    }

    @Override
    public void mouseClicked(CanvasMouseEvent e) {
        requestKeyFocus(this);
    }

    @Override
    public void mouseReleased(CanvasMouseReleaseEvent e) {
    }

    @Override
    public boolean clickThrough() {
        return false;
    }

    @Override
    public Cursor getMouseOverCursor() {
        return MouseUtils.TEXT_CURSOR;
    }
}
