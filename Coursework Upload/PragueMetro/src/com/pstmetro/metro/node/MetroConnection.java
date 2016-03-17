/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pstmetro.metro.node;

import com.inlustra.gui.utils.BlendComposite;
import com.pstmetro.dijkstra.DijkstraConnection;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Thomas
 */
public class MetroConnection implements DijkstraConnection<MetroNode> {

    private MetroNode source;
    private MetroNode destination;
    private MetroLine line;
    private BasicStroke none = new BasicStroke(0);
    private BasicStroke thick = new BasicStroke(5);
    private int time;

    /**
     *
     * Whilst not necessarily required, each the nodes will require a connection
     * from both directions. Useful should the train break down going in 1
     * direction.
     *
     * @param source A MetroNode the train will be departing from
     * @param destination A MetroNode the train will arrive at
     * @param line
     * @param time An integer value giving the amount of time required to use
     * this connection
     */
    public MetroConnection(MetroNode source, MetroNode destination, MetroLine line, int time) {
        assert source != null : "Source is null ";
        assert destination != null : "Destination is null for: " + source.getName();
        this.source = source;
        this.destination = destination;
        this.time = time;
        this.line = line;
    }

    /**
     *
     * @return the source node associated with this 1 way connection.
     */
    @Override
    public MetroNode getSource() {
        return source;
    }

    /**
     *
     * Sets the source of the MetroConnection
     *
     * @param source a MetroNode
     */
    public void setSource(MetroNode source) {
        this.source = source;
    }

    /**
     *
     * @return the destination of this 1 way MetroConnection
     */
    @Override
    public MetroNode getDestination() {
        return destination;
    }

    /**
     *
     * Sets the destination of the MetroConnection
     *
     * @param destination
     */
    public void setDestination(MetroNode destination) {
        this.destination = destination;
    }

    /**
     *
     * @return the cost of using this connection.
     */
    @Override
    public int getCost() {
        return time;
    }

    /**
     *
     * @return the Line the MetroConnection currently falls under.
     */
    public MetroLine getLine() {
        return line;
    }

    /**
     *
     * Draws the MetroConnection line including times represented by dots.
     *
     * @param g2d
     * @param delta
     */
    public void draw(Graphics2D g2d, float delta) {
        g2d.setColor(getLine().getColor());
        g2d.setStroke(thick);
        g2d.drawLine(getSource().getX(), getSource().getY(),
                getDestination().getX(), getDestination().getY());
        g2d.setStroke(none);
        // g2d.setColor(Color.black);
        g2d.setComposite(BlendComposite.Add);
        for (int i = 1; i < getCost() + 1; i++) {
            g2d.fillOval(getLineSegmentX(i) - 1, getLineSegmentY(i) - 1, 3, 3);
            g2d.fillOval(getLineSegmentX(i) - 1, getLineSegmentY(i) - 1, 3, 3);
        }
        g2d.setComposite(BlendComposite.Normal);
    }

    /**
     *
     * returns the X of the segment divided by the time.
     *
     */
    public int getLineSegmentX(int segment) {
        return (getSource().getX() + (int) ((double) (getDestination().getX() - getSource().getX()) / (double) (time + 1)) * segment);
    }

    /**
     *
     * returns the Y of the segment divided by the time.
     *
     */
    public int getLineSegmentY(int segment) {
        return (getSource().getY() + (int) ((double) (getDestination().getY() - getSource().getY()) / (double) (time + 1)) * segment);
    }

    /**
     *
     * Used in the intersection of the Mouse.
     *
     */
    public boolean intersects(Rectangle2D mousePoint) {
        Line2D intersection = new Line2D.Float(source.getX(), source.getY(), destination.getX(), destination.getY());
        return intersection.intersects(mousePoint);
    }

    @Override
    public String toString() {
        return getSource().getName() + "\t" + getDestination().getName() + "\t"
                + getCost() + "\t" + (getLine() == null ? "" : getLine().getName()) + "\n";
    }
}
