/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.elements;

import com.inlustra.gui.elements.CanvasElement;

/**
 *
 * @author Thomas
 */
public abstract class InnerElement extends CanvasElement {

    public InnerElement(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public InnerElement(CanvasPosition position, int x, int y, int width, int height) {
        super(position, x, y, width, height);
    }
    
}
