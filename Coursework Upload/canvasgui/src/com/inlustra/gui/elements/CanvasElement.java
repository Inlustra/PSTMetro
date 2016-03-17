/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.elements;

import com.inlustra.gui.containers.CanvasContainer;
import com.inlustra.gui.CanvasWindow;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Thomas
 */
public abstract class CanvasElement implements Comparable<CanvasElement> {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean hidden;
    protected CanvasContainer parent;
    protected Color backgroundColor;
    protected CanvasPosition position;
    protected Cursor mouseOverCursor;
    protected boolean hasFocus = false;

    public CanvasElement(int x, int y, int width, int height) {
        this(CanvasPosition.FOREGROUND, x, y, width, height);
    }

    public CanvasElement(CanvasPosition position, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.position = position;
        this.hidden = false;
        this.backgroundColor = Color.white;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getRealX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRealY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setParent(CanvasContainer parent) {
        this.parent = parent;
    }

    public CanvasPosition getPosition() {
        return position;
    }

    public void setPosition(CanvasPosition cp) {
        this.position = cp;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public int compareTo(CanvasElement o) {
        return position.ordinal() - o.position.ordinal();
    }

    public void repaint() {
        parent.repaintElement(this);
    }

    public void repaintAll() {
        parent.repaintAll();
    }

    public CanvasWindow getCanvasFrame() {
        if (this instanceof CanvasWindow) {
            return (CanvasWindow) this;
        }
        CanvasElement ce = this;
        while ((ce = ce.parent) != null) {
            if (ce instanceof CanvasWindow) {
                return (CanvasWindow) ce;
            }
        }
        return null;
    }
    protected Point initialPosition;

    public void startMove() {
        initialPosition = new Point(this.x, this.y);
    }

    public void move(Point amount) {
        if(initialPosition == null) {
            startMove();
        }
        this.x = initialPosition.x + amount.x;
        this.y = initialPosition.y + amount.y;
    }

    public void endMove() {
        initialPosition = null;
    }

    public Point getInitialPosition() {
        if (initialPosition == null) {
            throw new RuntimeException("Start move wasn't called");
        }
        return initialPosition;
    }

    public CanvasElement getRootElement() {
        if (this instanceof CanvasWindow) {
            return this;
        }
        CanvasElement ce = this;
        CanvasElement predecessor = this;
        while ((ce = ce.parent) != null) {
            if (ce instanceof CanvasWindow) {
                return predecessor;
            }
            predecessor = ce;
        }
        return predecessor;
    }

    public Rectangle2D getCanvasRectangle() {
        int x = this.x;
        int y = this.y;
        CanvasElement ce = this;
        while ((ce = ce.parent) != null) {
            x += ce.x;
            y += ce.y;
        }
        return new Rectangle(x, y, width, height);
    }

    public boolean isHidden() {
        return hidden;
    }

    public void hide() {
        this.hidden = true;
        repaint();
    }

    public void show() {
        this.hidden = false;
        repaint();
    }

    public abstract void draw(Graphics2D g2d, float delta);

    public void onKeyType(KeyEvent e) {
    }

    public void onKeyPress(KeyEvent e) {
    }

    public void onKeyRelease(KeyEvent e) {
    }

    public void requestKeyFocus(CanvasElement e) {
        parent.requestKeyFocus(e);
    }

    public void setFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;
    }

    public boolean isFocussed() {
        return hasFocus;
    }

    public void setMouseOverCursor(Cursor mouseOverCursor) {
        this.mouseOverCursor = mouseOverCursor;
    }

    /**
     *
     * @author Thomas
     */
    public static enum CanvasPosition {

        BACKGROUND, FOREGROUND, OVERLAY;
    }

    public CanvasContainer getParent() {
        return parent;
    }
}
