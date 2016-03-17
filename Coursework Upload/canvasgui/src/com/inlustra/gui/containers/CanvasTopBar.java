/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.containers;

import com.inlustra.gui.mouse.CanvasMouseListener;
import com.inlustra.gui.elements.heavy.CanvasButton;
import com.inlustra.gui.mouse.CanvasMouseDragEvent;
import com.inlustra.gui.mouse.CanvasMouseEvent;
import com.inlustra.gui.mouse.CanvasMouseReleaseEvent;
import com.inlustra.gui.utils.CanvasFont;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

/**
 *
 * @author Thomas
 */
public class CanvasTopBar extends CanvasContainer implements CanvasMouseListener {

    private String title;
    private Color foregroundColor;
    private Font font = CanvasFont.getFont("font.ttf");
    private Point screenClick;
    private Point initialLocation;
    private Dimension snapSize = new Dimension(1, 1);
    private int currentButtonX = this.width;
    private Image image;

    public CanvasTopBar(String title, int x, int y, int width, int height) {
        this(title, Color.black, null, x, y, width, height);
    }

    public CanvasTopBar(String title, Image img, int x, int y, int width, int height) {
        this(title, Color.black, img, x, y, width, height);
    }

    public CanvasTopBar(String title, Color color, Image img, int x, int y, int width, int height) {
        super(CanvasPosition.OVERLAY, x, y, width, height);
        this.image = img;
        this.title = title;
        this.foregroundColor = color;
        this.font.deriveFont((float) 30);
    }

    public void addButton(CanvasButton button) {
        button.setHeight(height - 1);
        button.setX(currentButtonX -= button.getWidth());
        addElement(button);
    }

    @Override
    public void draw(Graphics2D g2d, float delta) {
        super.draw(g2d, delta); //To change body of generated methods, choose Tools | Templates.
        g2d.setFont(font.deriveFont((float) 30));
        int currentX = 0;
        if (image != null) {
            g2d.drawImage(image, 0, 0, image.getWidth(null), image.getHeight(null), null);
            currentX += image.getWidth(null);
        }
        g2d.setColor(foregroundColor);
        g2d.drawString(title, currentX, 25);
        g2d.setColor(new Color(0, 180, 255));
        g2d.drawLine(0, height - 1, width, height - 1);
    }

    @Override
    public void mousePressed(CanvasMouseEvent e) {
        super.mousePressed(e); //To change body of generated methods, choose Tools | Templates.
        screenClick = e.getOriginal().getLocationOnScreen();
        initialLocation = getCanvasFrame().getFrame().getLocation();

    }

    @Override
    public void mouseDragged(CanvasMouseDragEvent e) {
        int xMoved = getDragDistance(e.getOriginal().getXOnScreen(), screenClick.x, snapSize.width);
        int yMoved = getDragDistance(e.getOriginal().getYOnScreen(), screenClick.y, snapSize.height);
        getCanvasFrame().getFrame().setLocation(initialLocation.x + xMoved, initialLocation.y + yMoved); //To change body of generated methods, choose Tools | Templates.    
        repaintAll();
    }

    private int getDragDistance(int larger, int smaller, int snapSize) {
        int halfway = snapSize / 2;
        int drag = larger - smaller;
        drag += (drag < 0) ? -halfway : halfway;
        drag = (drag / snapSize) * snapSize;

        return drag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

    }

    @Override
    public void mouseReleased(CanvasMouseReleaseEvent e) {
    }

    @Override
    public void mouseMoved(CanvasMouseEvent e) {
    }
}
