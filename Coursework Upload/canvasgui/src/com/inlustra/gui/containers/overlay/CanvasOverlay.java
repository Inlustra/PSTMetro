/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.containers.overlay;

import com.inlustra.gui.containers.CanvasContainer;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Thomas
 */
public class CanvasOverlay extends CanvasContainer {

    public CanvasOverlay(int x, int y, int width, int height) {
        super(CanvasPosition.OVERLAY, x, y, width, height);
        this.opaque = true;
        this.backgroundColor = new Color(0, 0, 0, 0.5f);
    }

    @Override
    public void drawBackground(Graphics2D g2d, float delta) {
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, width, height);
    }

    @Override
    public boolean clickThrough() {
        return true; //To change body of generated methods, choose Tools | Templates.
    }
}
