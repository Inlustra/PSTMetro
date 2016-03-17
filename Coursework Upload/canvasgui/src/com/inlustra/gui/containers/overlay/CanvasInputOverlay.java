/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.containers.overlay;

import com.inlustra.gui.elements.input.TextInputElement;

/**
 *
 * @author Thomas
 */
public class CanvasInputOverlay extends CanvasOverlay {

    public CanvasInputOverlay(int x, int y, int width, int height) {
        super(x, y, width, height);
        int textBoxWidth = width / 2;
        int textBoxHeight = 30;
        addElement(new TextInputElement((width / 2) - (textBoxWidth / 2), (height / 2) - (textBoxHeight / 2), textBoxWidth, textBoxHeight));
    }
}
