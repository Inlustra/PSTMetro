/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pstmetro.gui.containers;

import com.inlustra.gui.containers.CanvasContainer;
import com.inlustra.gui.elements.CanvasElement;
import com.inlustra.gui.mouse.CanvasMouseDragEvent;
import com.inlustra.gui.mouse.CanvasMouseEvent;
import com.inlustra.gui.mouse.CanvasMouseReleaseEvent;
import com.pstmetro.dijkstra.DijkstraAlgorithm;
import com.pstmetro.metro.node.MetroConnection;
import com.pstmetro.metro.node.MetroMap;
import com.pstmetro.metro.node.MetroNode;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Thomas
 */
public abstract class MetroPlanner extends CanvasContainer {

    MetroMap metro;
    MetroNode source;
    MetroNode destination;
    DijkstraAlgorithm currentPlanner;
    List<MetroNode> currentJourney;

    /**
     *
     * @param metro
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public MetroPlanner(MetroMap metro, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.metro = metro;
        this.currentJourney = new LinkedList<>();
        for (MetroNode mn : metro.getNodes()) {
            addElement(mn);
        }
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseClicked(CanvasMouseEvent e) {
        CanvasElement el = getElementAt(e.getPoint());
        if (el instanceof MetroNode) {
            if (source == null) {
                source = (MetroNode) el;
                currentPlanner = new DijkstraAlgorithm(metro, source);
                onChooseJourneySource(currentPlanner);
            } else if (destination == null) {
                destination = (MetroNode) el;
                onChooseJourneyDest(currentPlanner.getJourney(destination));
            } else {
                destination = null;
                source = (MetroNode) el;
                currentPlanner = new DijkstraAlgorithm(metro, source);
                onChooseJourneySource(currentPlanner);
            }
            repaintAll();
        }
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseDragged(CanvasMouseDragEvent e) {
        for (CanvasElement el : elements) {
            el.move(e.getAmountDragged());
        }
        repaintAll();
    }

    /**
     *
     * @param e
     */
    @Override
    public void mousePressed(CanvasMouseEvent e) {
        for (CanvasElement el : elements) {
            el.startMove();
        }
    }

    /**
     *
     * @param evt
     */
    @Override
    public void mouseReleased(CanvasMouseReleaseEvent evt) {
        for (CanvasElement el : elements) {
            el.endMove();
        }
    }

    /**
     *
     * @param g2d
     * @param delta
     */
    @Override
    public void drawBackgroundElements(Graphics2D g2d, float delta) {
        super.drawBackgroundElements(g2d, delta); //To change body of generated methods, choose Tools | Templates.
        for (MetroConnection connections : metro.getConnections()) {
            connections.draw(g2d, delta);
        }
    }

    /**
     *
     * @param g2d
     * @param delta
     */
    @Override
    public void draw(Graphics2D g2d, float delta) {
        super.draw(g2d, delta);
    }

    /**
     *
     * @param planner
     */
    public abstract void onChooseJourneySource(DijkstraAlgorithm<MetroNode> planner);

    /**
     *
     * @param journey
     */
    public abstract void onChooseJourneyDest(List<MetroNode> journey);

    /**
     *
     */
    public abstract void onClearJourney();
}
