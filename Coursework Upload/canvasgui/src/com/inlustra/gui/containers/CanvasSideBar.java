/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.containers;

import com.inlustra.gui.elements.CanvasElement;
import com.inlustra.gui.elements.heavy.CanvasButton;
import com.inlustra.gui.mouse.CanvasMouseDragEvent;
import com.inlustra.gui.mouse.CanvasMouseEvent;
import com.inlustra.gui.mouse.CanvasMouseReleaseEvent;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author Thomas
 */
public class CanvasSideBar extends CanvasContainer {

    public int currentButtonY;

    public CanvasSideBar(int x, int y, int width, int height) {
        super(x, y, width, height);
        currentButtonY = 0;
    }

    public void addButton(CanvasButton button) {
        button.setWidth(width - 1);
        button.setY(currentButtonY);
        currentButtonY += button.getHeight();
        addElement(button);
    }

    public void addSpace(int space) {
        currentButtonY += space;
    }

    public void removeButton(CanvasButton button) {
        for (CanvasElement element : elements) {
            if (element.getY() > button.getY()) {
                currentButtonY -= button.getY();
                element.setY(element.getY() - button.getHeight());
            }
        }
        elements.remove(button);
    }

    @Override
    public void draw(Graphics2D g2d, float delta) {
        super.draw(g2d, delta); //To change body of generated methods, choose Tools | Templates.
        g2d.setColor(new Color(0, 180, 255));
        g2d.drawLine(width - 1, 0, width - 1, height);
    }

    @Override
    public void mouseDragged(CanvasMouseDragEvent e) {
        if (currentButtonY > this.getHeight()) {
            for (CanvasElement element : elements) {
                element.move(new Point(0, e.getAmountDragged().y));
            }
        }
    }

    @Override
    public void mousePressed(CanvasMouseEvent e) {
        if (currentButtonY > this.getHeight()) {
            for (CanvasElement el : elements) {
                el.startMove();
            } //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Override
    public void mouseReleased(CanvasMouseReleaseEvent evt) {
        super.mouseReleased(evt); //To change body of generated methods, choose Tools | Templates.
        for (CanvasElement el : elements) {
            el.endMove();
        }
    }
}
