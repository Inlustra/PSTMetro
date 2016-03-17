/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.elements.heavy;

import com.inlustra.gui.elements.heavy.CanvasImage;
import com.inlustra.gui.mouse.CanvasMouseDragEvent;
import com.inlustra.gui.mouse.CanvasMouseEvent;
import com.inlustra.gui.mouse.CanvasMouseListener;
import com.inlustra.gui.mouse.CanvasMouseReleaseEvent;
import com.inlustra.gui.mouse.MouseUtils;
import java.awt.Cursor;
import java.awt.Image;
import java.io.File;

/**
 *
 * @author Thomas
 */
public class MoveableImage extends CanvasImage implements CanvasMouseListener {

    public MoveableImage(File image, int x, int y) {
        super(image, x, y);
    }

    @Override
    public void mouseMoved(CanvasMouseEvent e) {
    }

    @Override
    public void mouseDragged(CanvasMouseDragEvent e) {
        this.x = e.getNewElementPosition().x;
        this.y = e.getNewElementPosition().y;
        repaintAll();
    }

    @Override
    public void mousePressed(CanvasMouseEvent e) {
    }

    @Override
    public void mouseClicked(CanvasMouseEvent e) {
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
        return MouseUtils.HAND_CURSOR;
    }
}
