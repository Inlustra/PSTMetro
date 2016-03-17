/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

/**
 *
 * @author Thomas
 */
public abstract class DoubleBufferedCanvas extends Canvas {

    private int bufferWidth;
    private int bufferHeight;
    private Image bufferImage;
    private Graphics bufferGraphics;
    private boolean ANTI_ALIAS = false;

    public DoubleBufferedCanvas() {
        super();
    }

    public DoubleBufferedCanvas(boolean ANTI_ALIAS) {
        this();
        this.ANTI_ALIAS = ANTI_ALIAS;
    }

    private void resetBuffer() {
        // always keep track of the image size
        bufferWidth = getSize().width;
        bufferHeight = getSize().height;

        //    clean up the previous image
        if (bufferGraphics != null) {
            bufferGraphics.dispose();
            bufferGraphics = null;
        }
        if (bufferImage != null) {
            bufferImage.flush();
            bufferImage = null;
        }
        System.gc();

        //    create the new image with the size of the panel
        bufferImage = createImage(bufferWidth, bufferHeight);
        bufferGraphics = bufferImage.getGraphics();
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        //    checks the buffersize with the current panelsize
        //    or initialises the image with the first paint
        if (bufferWidth != getSize().width
                || bufferHeight != getSize().height
                || bufferImage == null || bufferGraphics == null) {
            resetBuffer();
        }
        if (bufferGraphics != null) {
            //this clears the offscreen image, not the onscreen one
          //  bufferGraphics.clearRect(0, 0, bufferWidth, bufferHeight);

            //calls the paintbuffer method with 
            //the offscreen graphics as a param
            if (ANTI_ALIAS) {
                ((Graphics2D) bufferGraphics).setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                ((Graphics2D) bufferGraphics).setRenderingHint(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            }
            paintBuffer(bufferGraphics);

            //we finaly paint the offscreen image onto the onscreen image
            g.drawImage(bufferImage, 0, 0, this);
        }

    }

    public abstract void paintBuffer(Graphics bufferGraphics);
}
