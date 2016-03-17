/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.mouse;

import com.inlustra.gui.elements.CanvasElement;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 *
 * @author Thomas
 */
public class CanvasMouseDragEvent extends CanvasMouseEvent {

    MouseEvent initialPress;
    Point initialPressPoint;
    Point elementInitialPoint;
    Point amountDragged;
    Point newElementPosition;

    public CanvasMouseDragEvent(MouseEvent original, MouseEvent initialPress, int offsetX, int offsetY, int elementOffsetX, int elementOffsetY) {
        super(original, offsetX, offsetY);
        this.initialPress = initialPress;
        this.elementInitialPoint = new Point(elementOffsetX, elementOffsetY);
        this.initialPressPoint = new Point(initialPress.getPoint().x - offsetX, initialPress.getPoint().y - offsetY);
        this.amountDragged = new Point(getDragDistance(point.x, initialPressPoint.x, 1), getDragDistance(point.y, initialPressPoint.y, 1));
        this.newElementPosition = new Point(elementInitialPoint.x + amountDragged.x, elementInitialPoint.y + amountDragged.y);
    }

    public Point getNewElementPosition() {
        return newElementPosition;
    }

    public Point getElementOffset() {
        return elementInitialPoint;
    }

    public Point getAmountDragged() {
        return amountDragged;
    }

    public MouseEvent getInitialPress() {
        return initialPress;
    }

    public Point getInitialPressPoint() {
        return initialPressPoint;
    }

    private int getDragDistance(int larger, int smaller, int snapSize) {
        int halfway = snapSize / 2;
        int drag = larger - smaller;
        drag += (drag < 0) ? -halfway : halfway;
        drag = (drag / snapSize) * snapSize;

        return drag;
    }
}
