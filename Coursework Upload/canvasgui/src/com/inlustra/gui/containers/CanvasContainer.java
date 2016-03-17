/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.containers;

import com.inlustra.gui.elements.InnerElement;
import com.inlustra.gui.elements.CanvasElement;
import com.inlustra.gui.mouse.CanvasMouseDragEvent;
import com.inlustra.gui.mouse.CanvasMouseEvent;
import com.inlustra.gui.mouse.CanvasMouseListener;
import com.inlustra.gui.mouse.CanvasMouseReleaseEvent;
import com.inlustra.misc.Misc;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Thomas
 */
public class CanvasContainer extends CanvasElement implements CanvasMouseListener {

    protected List<CanvasElement> elements;
    protected List<CanvasElement> removeQueue;
    protected List<CanvasElement> addQueue;
    protected CanvasElement draggedElement;
    protected int draggedElementInitialx;
    protected int draggedElementInitialy;
    protected MouseEvent initialPress;
    protected boolean opaque;

    public CanvasContainer(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.elements = new LinkedList<>();
        this.removeQueue = new LinkedList<>();
        this.addQueue = new LinkedList<>();
        this.opaque = true;
    }

    public CanvasContainer(CanvasPosition pos, int x, int y, int width, int height) {
        super(pos, x, y, width, height);
        this.elements = new LinkedList<>();
        this.removeQueue = new LinkedList<>();
        this.addQueue = new LinkedList<>();
        this.opaque = true;
    }

    public void addElement(CanvasElement element) {
        this.addQueue.add(element);
    }

    public void removeElement(CanvasElement element) {
        this.removeQueue.add(element);
    }

    public CanvasElement getElementAt(Point position) {
        List<CanvasElement> reverseElements = new LinkedList<>();
        for (CanvasElement element : this.elements) {
            reverseElements.add(element);
        }
        Collections.reverse(reverseElements);
        for (CanvasElement element : reverseElements) {
            if (element.isHidden()) {
                continue;
            }
            if (element instanceof CanvasMouseListener) {
                if (((CanvasMouseListener) element).clickThrough()) {
                    continue;
                }
            }
            if (Misc.isWithin(position, element.getRealX(), element.getRealY(), element.getWidth(), element.getHeight())) {
                return element;
            }
        }
        return null;
    }

    public List<CanvasElement> getElements() {
        return elements;
    }

    public CanvasElement getDraggedElement() {
        return draggedElement;
    }

    public MouseEvent getInitialPress() {
        return initialPress;
    }

    public void repaintElement(CanvasElement... element) {
        parent.repaintElement(element);
    }

    public boolean hasElement(CanvasElement element) {
        return elements.contains(element);
    }

    public void drawBackground(Graphics2D g2d, float delta) {
        if (isOpaque()) {
            g2d.setColor(backgroundColor);
            g2d.fillRect(0, 0, width, height);
        }
    }

    @Override
    public void repaint() {
        if (isOpaque()) {
            parent.repaintElement(this);
        } else {
            parent.repaintElement(parent);
        }
    }

    @Override
    public void hide() {
        this.hidden = true;
        for (CanvasElement ce : elements) {
            ce.hide();
        }
        repaint();
    }

    @Override
    public void show() {
        this.hidden = false;
        for (CanvasElement ce : elements) {
            ce.show();
        }
        repaint();
    }

    @Override
    public void draw(Graphics2D g2d, float delta) {
        if (removeQueue.size() > 0 || addQueue.size() > 0) {
            Iterator<CanvasElement> removeIterator = removeQueue.iterator();
            while (removeIterator.hasNext()) {
                elements.remove(removeIterator.next());
                removeIterator.remove();
            }
            Iterator<CanvasElement> addIterator = addQueue.iterator();
            while (addIterator.hasNext()) {
                CanvasElement el = addIterator.next();
                el.setParent(this);
                elements.add(el);
                addIterator.remove();
            }
            Collections.sort(elements);
        }
        drawBackground(g2d, delta);
        drawBackgroundElements(g2d, delta);
        drawChildren(g2d, delta);
    }

    public void drawBackgroundElements(Graphics2D g2d, float delta) {
        for (CanvasElement ce : elements) {
            if (ce.getPosition() == CanvasPosition.BACKGROUND) {
                if (!ce.isHidden()) {
                    if (!(ce instanceof InnerElement)) {
                        ce.draw((Graphics2D) g2d.create(ce.getX(), ce.getY(), ce.getWidth(), ce.getHeight()), delta);
                    } else {
                        ce.draw(g2d, delta);
                    }
                }
            }
        }
    }

    public void drawChildren(Graphics2D g2d, float delta) {
        for (CanvasElement ce : elements) {
            if (ce.getPosition() != CanvasPosition.BACKGROUND) {
                if (!ce.isHidden()) {
                    if (ce.getX() + ce.getWidth() > 0 && ce.getX() < this.getWidth() && ce.getY() + ce.getHeight() > 0 && ce.getY() < this.getHeight()) {
                        if (!(ce instanceof InnerElement)) {
                            ce.draw((Graphics2D) g2d.create(ce.getX(), ce.getY(), ce.getWidth(), ce.getHeight()), delta);
                        } else {
                            ce.draw(g2d, delta);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mouseDragged(CanvasMouseDragEvent e) {
        if (draggedElement != null) {
            ((CanvasMouseListener) draggedElement).mouseDragged(new CanvasMouseDragEvent(e.getOriginal(), e.getInitialPress(),
                    e.getOffsetX() + draggedElement.getX(), e.getOffsetY() + draggedElement.getY(), draggedElementInitialx, draggedElementInitialy));
        }
    }

    @Override
    public void mousePressed(CanvasMouseEvent e) {
        CanvasElement element = getElementAt(e.getPoint());
        if (element instanceof CanvasMouseListener) {
            CanvasMouseListener mouseEl = (CanvasMouseListener) element;
            mouseEl.mousePressed(new CanvasMouseEvent(e.getOriginal(), e.getOffsetX() + element.getX(), e.getOffsetY() + element.getY()));
            draggedElement = element;
            draggedElementInitialx = draggedElement.getX();
            draggedElementInitialy = draggedElement.getY();
            initialPress = e.getOriginal();
        }
    }

    @Override
    public void mouseClicked(CanvasMouseEvent e) {
        CanvasElement element = getElementAt(e.getPoint());
        if (element instanceof CanvasMouseListener) {
            CanvasMouseListener mouseEl = (CanvasMouseListener) element;
            mouseEl.mouseClicked(new CanvasMouseEvent(e.getOriginal(), e.getOffsetX() + element.getX(), e.getOffsetY() + element.getY()));
        }
    }

    @Override
    public void mouseReleased(CanvasMouseReleaseEvent evt) {
        for (CanvasElement e : elements) {
            if (e instanceof CanvasMouseListener) {
                ((CanvasMouseListener) e).mouseReleased(evt);
            }
        }
        draggedElement = null;
        initialPress = null;
    }

    @Override
    public void mouseMoved(CanvasMouseEvent e) {
        CanvasElement cc = getElementAt(e.getPoint());
        if (cc instanceof CanvasMouseListener) {
            CanvasMouseListener mouseEl = (CanvasMouseListener) cc;
            if (!(cc instanceof CanvasContainer)) {
                getCanvasFrame().getFrame().setCursor(mouseEl.getMouseOverCursor());
            }
            mouseEl.mouseMoved(new CanvasMouseEvent(e.getOriginal(), e.getOffsetX() + cc.getX(), e.getOffsetY() + cc.getY()));
        } else if (cc == null) {
            getCanvasFrame().getFrame().setCursor(getMouseOverCursor());
        }
    }

    public boolean isOpaque() {
        return opaque;
    }

    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
    }

    @Override
    public boolean clickThrough() {
        return false;
    }

    @Override
    public Cursor getMouseOverCursor() {
        return Cursor.getDefaultCursor();
    }
}
