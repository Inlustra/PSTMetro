/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.mouse;

import java.awt.Cursor;

/**
 *
 * @author Thomas
 */
public interface CanvasMouseListener {

    public void mouseMoved(CanvasMouseEvent e);

    public void mouseDragged(CanvasMouseDragEvent e);

    public void mousePressed(CanvasMouseEvent e);

    public void mouseClicked(CanvasMouseEvent e);

    public void mouseReleased(CanvasMouseReleaseEvent e);
    
    public boolean clickThrough();

    public Cursor getMouseOverCursor();
    
}
