/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pstmetro.gui.containers;

import com.inlustra.gui.containers.AnimatedContainer;
import com.inlustra.gui.mouse.CanvasMouseDragEvent;
import com.inlustra.gui.mouse.CanvasMouseEvent;
import com.inlustra.gui.mouse.CanvasMouseReleaseEvent;
import com.pstmetro.metro.node.MetroConnection;
import com.pstmetro.metro.node.MetroLine;
import com.pstmetro.metro.node.MetroMap;
import com.pstmetro.metro.node.MetroNode;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

/**
 * The journey bar is used in displaying the journey to take after the user has
 * selected it.
 *
 * @author Thomas
 */
public class JourneyBar extends AnimatedContainer {

    private List<MetroNode> journey;
    private MetroNode source;
    private MetroMap map;
    private int border = 50;
    private BasicStroke journeyStroke = new BasicStroke(6);
    private BasicStroke none = new BasicStroke(0);

    /**
     * Create a new CanvasContainer while pulling in the MetroMap for use with
     * drawing.
     *
     * @param map MetroMap to be used for drawing and retrieving node details.
     * @param x X location of the absolutely positioned container.
     * @param y Y location of the absolutely positioned container.
     * @param width Width of the absolutely positioned container.
     * @param height Height of the absolutely positioned container.
     */
    public JourneyBar(MetroMap map, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.map = map;
        journey = new LinkedList<>();
        this.opaque = true;
        this.backgroundColor = new Color(1, 1, 1, 0.9f);
    }

    /**
     *
     * Sets the source of the Journey and repaints the GUI
     *
     * @param node Node to be used as source
     */
    public void setSource(MetroNode node) {
        this.source = node;
        repaint();
    }

    /**
     * Sets the Journey List that will be used in drawing the users current
     * journey.
     *
     * @param journey List containing all MetroNodes required for the journey.
     */
    public void setJourney(List<MetroNode> journey) {
        this.journey = journey;
        repaint();
    }

    /**
     * Removes the Joruney and source and repaints the JourneyBar.
     *
     */
    public void clearJourney() {
        this.source = null;
        this.journey.clear();
        repaint();
    }

    /**
     * Draws the background of the JourneyBar including all stations and the times to each
     * 
     *
     * @param g2d
     * @param delta
     */
    @Override
    public void drawBackground(Graphics2D g2d, float delta) {
        super.drawBackground(g2d, delta); //To change body of generated methods, choose Tools | Templates.
        if (source != null) {
            int centerY = 40;
            int startX = border;
            int endX = getWidth() - border;
            g2d.setFont(g2d.getFont().deriveFont(10f));
            if (journey.size() > 1) {
                MetroLine line = source.getCommonLine(journey.get(1));
                float scale = 1.5f + (2 / journey.size());
                int step = (endX - startX) / (journey.size() - 1);
                MetroNode previous = journey.get(0);
                g2d.setStroke(journeyStroke);
                g2d.setColor(line.getColor());
                g2d.drawLine(startX, centerY, startX + step, centerY);
                g2d.setStroke(none);
                previous.drawChange(g2d, startX, centerY, scale + 1);
                int previousChangeX = 0;
                int stops = 0;
                int cost = 0;
                int total = 0;
                cost += getConnectionTime(previous, journey.get(1));
                total += getConnectionTime(previous, journey.get(1));
                for (int i = 1; i < journey.size() - 1; i++) {
                    stops++;
                    MetroNode current = journey.get(i);
                    cost += getConnectionTime(current, journey.get(i + 1));
                    total += getConnectionTime(current, journey.get(i + 1));
                    if (!previous.onSameLine(journey.get(i + 1))) {
                        line = journey.get(i + 1).getLine();
                    }
                    g2d.setStroke(journeyStroke);
                    g2d.setColor(line.getColor());
                    g2d.drawLine(startX + (step * (i)), centerY, startX + (step * (i + 1)), centerY);
                    g2d.setStroke(none);
                    if (!previous.onSameLine(journey.get(i + 1))) {
                        g2d.setColor(Color.BLACK);
                        drawCenteredText(g2d, "Stops on line " + journey.get(i - 1).getCommonLine(journey.get(i)).getName() + ": " + stops, startX + (previousChangeX + (step * i)) / 2, centerY - 25);
                        drawCenteredText(g2d, "Average time: " + cost + " minutes", startX + (previousChangeX + (step * i)) / 2, centerY + 15);
                        previousChangeX = (step * i);
                        stops = 0;
                        cost = 0;
                        current.drawChange(g2d, startX + (step * i), centerY, scale + 1);
                        line = journey.get(i + 1).getLine();
                    }
                    g2d.setColor(Color.black);
                    previous = current;
                }
                stops++;
                drawCenteredText(g2d, "Total journey time: " + total + " minutes", width / 2, centerY + 25);
                drawCenteredText(g2d, "Average time: " + cost + " minutes", startX + (previousChangeX + endX) / 2, centerY + 15);
                drawCenteredText(g2d, "Stops on line " + journey.get(journey.size() - 2).getCommonLine(journey.get(journey.size() - 1)).getName() + ": " + stops, startX + (previousChangeX + endX) / 2, centerY - 20);
                journey.get(journey.size() - 1).drawChange(g2d, endX, centerY, scale + 1);
            } else if (source != null) {
                source.drawChange(g2d, startX, centerY, 2);
            }
            if (!journey.isEmpty()) {
                g2d.setFont(g2d.getFont().deriveFont((float) 10));
                int startY = 100;
                int time = 0;
                MetroNode previous = journey.get(0);
                g2d.setColor(Color.black);
                previous.drawChange(g2d, 120, startY - 10, 2);
                g2d.drawString(previous.getName(), 150, (startY) - 4);
                for (int i = 1; i < journey.size() - 1; i++) {
                    MetroNode node = journey.get(i);
                    if (!previous.onSameLine(journey.get(i + 1))) {
                        node.drawChange(g2d, 120, (startY + i * 23) - 7, 2);
                    } else {
                        node.drawNode(g2d, 120, (startY + i * 23) - 7, 1.5f, node.getLine().getColor());
                    }
                    g2d.setColor(Color.black);
                    g2d.drawString(node.getName(), 150, (startY + i * 23) - 4);
                    time += getConnectionTime(node, journey.get(i + 1));
                    g2d.drawString(time + " mins", 70, (startY + i * 23) - 12);
                    previous = node;
                }
                time += getConnectionTime(journey.get(journey.size() - 2), journey.get(journey.size() - 1));
                g2d.drawString(time + " mins", 70, (startY + (journey.size() - 1) * 23) - 12);
                journey.get(journey.size() - 1).drawChange(g2d, 120, (startY + (journey.size() - 1) * 23) - 7, 2);
                g2d.drawString(journey.get(journey.size() - 1).getName(), 150, (startY + (journey.size() - 1) * 23) - 4);
            }
        }
        g2d.setColor(new Color(0, 180, 255));
        g2d.drawLine(0, 1, width, 1);
    }

    /**
     *
     * Utility function used in the draw code.
     * 
     */
    public int getConnectionTime(MetroNode node, MetroNode destination) {
        for (MetroConnection connection : map.getConnections()) {
            if (connection.getSource() == node && connection.getDestination() == destination) {
                return connection.getCost();
            }
        }
        throw new RuntimeException("No Connection found!");
    }

    /**
     *
     * Utility function used to draw text centered at an X and Y location.
     */
    public void drawCenteredText(Graphics g, String text, int x, int y) {
        FontMetrics fm = g.getFontMetrics();
        int totalWidth = (fm.stringWidth(text));

        // Baseline
        int cx = x - (totalWidth / 2);
        g.setColor(Color.BLACK);

        g.drawString(text, cx, y + ((fm.getDescent() + fm.getAscent()) / 2));
    }
    int dragY = -1;

    @Override
    public void mouseDragged(CanvasMouseDragEvent e) {
        move(new Point(0, e.getAmountDragged().y));
    }

    @Override
    public void mousePressed(CanvasMouseEvent e) {
        startMove();
        dragY = e.getOriginal().getY();
        System.out.println(getY());
    }

    @Override
    public void mouseClicked(CanvasMouseEvent e) {
        super.mouseClicked(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(CanvasMouseReleaseEvent evt) {
        endMove();
        if (dragY < evt.getPoint().y && dragY != -1 && getY() > 100) {
            moveTo(0, parent.getHeight(), 400, new Runnable() {
                @Override
                public void run() {
                    clearJourney();
                    dragY = -1;
                }
            });
        }
    }

    @Override
    public void mouseMoved(CanvasMouseEvent e) {
        super.mouseMoved(e); //To change body of generated methods, choose Tools | Templates.
    }
}
