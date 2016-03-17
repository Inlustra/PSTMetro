/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.containers.overlay;

import com.inlustra.gui.elements.heavy.CanvasButton;
import java.awt.Color;

/**
 *
 * @author Thomas
 */
public abstract class CanvasYesNoOverlay extends CanvasOverlay {

    public CanvasYesNoOverlay(String message, int x, int y, int width, int height) {
        super(x, y, width, height);
        int bigBoxWidth = width / 2;
        int textBoxWidth = 80;
        int textBoxHeight = 50;
        int outerWidth = width / 2;
        int btnx = outerWidth / 2;
        final CanvasYesNoOverlay overlay = this;
        final CanvasButton yesbtn = new CanvasButton("Yes", new Color(0, 180, 255), btnx / 2 - textBoxWidth / 2 + outerWidth / 2, (height / 2) - (textBoxHeight / 2), textBoxWidth, textBoxHeight) {
            @Override
            public void onButtonPress(CanvasButton source) {
                overlay.parent.removeElement(overlay);
                onClickYes();
            }
        };
        final CanvasButton nobtn = new CanvasButton("No", new Color(255, 25, 66), btnx + btnx / 2 - textBoxWidth / 2 + outerWidth / 2, (height / 2) - (textBoxHeight / 2), textBoxWidth, textBoxHeight) {
            @Override
            public void onButtonPress(CanvasButton source) {
                overlay.parent.removeElement(overlay);
                onClickNo();
            }
        };
        final CanvasButton lbl = new CanvasButton(message, new Color(0, 0, 0, 0), 0, (height / 2) - (textBoxHeight / 2) - textBoxHeight - 5, width, textBoxHeight) {
            @Override
            public void onButtonPress(CanvasButton source) {
            }
        };
        addElement(yesbtn);
        addElement(nobtn);
        addElement(lbl);
    }

    @Override
    public boolean clickThrough() {
        return false; //To change body of generated methods, choose Tools | Templates.
    }

    public abstract void onClickYes();

    public abstract void onClickNo();
}
