/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.mouse;

import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 *
 * @author Thomas
 */
public class CanvasMouseEvent {

    MouseEvent original;
    Point point;
    int offsetX;
    int offsetY;

    public CanvasMouseEvent(MouseEvent original, int offsetX, int offsetY) {
        this.original = original;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        point = new Point(original.getPoint().x - offsetX, original.getPoint().y - offsetY);
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public Point getPoint() {
        return point;
    }

    public MouseEvent getOriginal() {
        return original;
    }
}
