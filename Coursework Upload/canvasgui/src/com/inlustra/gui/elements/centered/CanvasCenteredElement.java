/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.elements.centered;

import com.inlustra.gui.elements.CanvasElement;
import com.inlustra.gui.elements.CanvasElement;

/**
 *
 * @author Thomas
 */
public abstract class CanvasCenteredElement extends CanvasElement {

    public CanvasCenteredElement(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public int getX() {
        return x - width / 2; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getY() {
        return y - height / 2; //To change body of generated methods, choose Tools | Templates.
    }

    public int getRealX() {
        return x;
    }
    public int getRealY() {
        return y;
    }
}
