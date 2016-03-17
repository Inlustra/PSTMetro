/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pstmetro.metro.node;

import com.inlustra.gui.elements.centered.CenteredInnerElement;
import com.inlustra.gui.mouse.CanvasMouseDragEvent;
import com.inlustra.gui.mouse.CanvasMouseEvent;
import com.inlustra.gui.mouse.CanvasMouseListener;
import com.inlustra.gui.mouse.CanvasMouseReleaseEvent;
import com.inlustra.gui.mouse.MouseUtils;
import com.inlustra.gui.utils.BlendComposite;
import com.pstmetro.gui.containers.MetroCreator;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 *
 * Used as nodes in the Dijkstras algorithm these contain only the code required
 * to visually represent them.
 *
 * @author Thomas
 */
public class MetroNode extends CenteredInnerElement implements CanvasMouseListener {

    private String name;
    private Map<String, MetroLine> lines;
    private MetroLine onlyLine;
    private boolean editing;
    private int textRotation;
    /**
     * For use with scaling
     */
    public static int nodeWidth = 10;
    /**
     * For use with scaling
     */
    public static int nodeHeight = 10;
    /**
     * Used in the rotation of the text enabling the use of a Switch in the draw
     * code
     */
    public static final int NORTH = 0;
    /**
     * Used in the rotation of the text enabling the use of a Switch in the draw
     * code
     */
    public static final int NORTH_EAST = 1;
    /**
     * Used in the rotation of the text enabling the use of a Switch in the draw
     * code
     */
    public static final int WEST = 2;
    /**
     * Used in the rotation of the text enabling the use of a Switch in the draw
     * code
     */
    public static final int SOUTH = 3;
    /**
     * Used in the rotation of the text enabling the use of a Switch in the draw
     * code
     */
    public static final int EAST = 4;
    /**
     * Used in the rotation of the text enabling the use of a Switch in the draw
     * code
     */
    public static final int NORTH_WEST = 5;

    /**
     *
     * @param name Name of the MetroNode
     * @param textRotation initial rotation of the Text
     * @param x X location of the absolutely positioned container.
     * @param y Y location of the absolutely positioned container.
     * @param width Width of the absolutely positioned container.
     * @param height Height of the absolutely positioned container.
     */
    public MetroNode(String name, int textRotation, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.name = name;
        this.textRotation = textRotation;
        this.lines = new HashMap<>();
        this.editing = true;
    }

    /**
     *
     * @return the name of the station
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name the name of the station
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return This ensures that a single metroNode of this name exists by
     * multiplying thehashcode of the String name by a random prime number, this
     * gives the metroNode an ID or hashCode associated with the name but always
     * the same.
     */
    @Override
    public int hashCode() {
        return this.name.hashCode() * 37; //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Add line to the MetroNode (Used when becoming a Change Station)
     *
     * @param line the MetroLine you wish to add to the node.
     */
    public void addLine(MetroLine line) {
        if (line == null) {
            throw new RuntimeException("Attempted to add null Line to MetroNode: " + getName());
        }
        if (lines.isEmpty()) {
            onlyLine = line;
        } else {
            onlyLine = null;
        }
        lines.put(line.getName(), line);
    }

    /**
     *
     * Finds and retrieve the common line between 2 MetroNodes
     *
     * @param node A MetroNode with a common MetroLine
     * @return a MetroLine
     */
    public MetroLine getCommonLine(MetroNode node) {
        for (MetroLine line : this.lines.values()) {
            for (MetroLine line2 : node.getLines().values()) {
                if (line == line2) {
                    return line;
                }
            }
        }
        return null;
    }

    public MetroLine getLine(String name) {
        return lines.get(name);
    }

    /**
     *
     * @return Whether the node is on more than one line.
     */
    public boolean isLineChange() {
        return lines.size() > 1;
    }

    /**
     *
     * @param l
     * @return whether the this MetroNode is on the MetroLine l
     */
    public boolean changesToLine(MetroLine l) {
        return lines.containsValue(l);
    }

    public boolean changesToLine(String l) {
        return lines.containsKey(l);
    }

    public boolean onSameLine(MetroNode n) {
        for (MetroLine line : lines.values()) {
            if (n.changesToLine(line)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MetroNode other = (MetroNode) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    /**
     *
     * Creates the associated GradientPaint with the amount of lines the current
     * MetroNode has.
     *
     */
    public Paint getChangePaint(int x, int y, int endX, int endY) {
        if (lines.size() > 1) {
            float step = (float) 1 / (float) (lines.size() - 1);
            float[] fractions = new float[lines.size()];
            Color[] colors = new Color[lines.size()];
            int i = 0;
            for (MetroLine val : lines.values()) {
                fractions[i] = i * step;
                colors[i] = val.getColor();
                i++;
            }
            return new LinearGradientPaint(new Point(x, y), new Point(endX, endY), fractions, colors);
        } else {
            return getLine().getColor();
        }
    }

    /**
     *
     * Draws the MetroNode including text and gradient paints
     *
     */
    @Override
    public void draw(Graphics2D g2d, float delta) {
        if (!isLineChange()) {
            if (lines.size() == 1) {
                drawText(g2d, getX(), getY(), nodeWidth);
                drawNode(g2d, getX(), getY(), 1, getLine().getColor()); //XXX INEFFICIENT, EUGH HURTS ME.
            } else {
                drawText(g2d, getX(), getY(), nodeWidth);
                drawNode(g2d, getX(), getY(), 1, Color.black);
            }
        } else {
            drawChange(g2d, getX(), getY(), 2f);
            drawText(g2d, getX(), getY(), nodeWidth * 2);
        }
        g2d.setColor(Color.black);
        g2d.setComposite(BlendComposite.Average);
        g2d.setComposite(BlendComposite.Normal);
    }

    /**
     *
     * @return
     */
    public boolean hasLines() {
        return lines.size() > 0;
    }

    /**
     *
     * @param x
     * @param scale
     * @return
     */
    public int getScaledX(int x, float scale) {
        return (int) (x - (nodeWidth * scale) / 2);
    }

    /**
     *
     * @param y
     * @param scale
     * @return
     */
    public int getScaledY(int y, float scale) {
        return (int) (y - (nodeHeight * scale) / 2);
    }

    /**
     *
     * @param scale
     * @return
     */
    public int getScaledWidth(float scale) {
        return (int) (nodeWidth * scale);
    }

    /**
     *
     * @param scale
     * @return
     */
    public int getScaledHeight(float scale) {
        return (int) (nodeHeight * scale);
    }

    /**
     *
     * @param g2d
     * @param realX
     * @param realY
     * @param scale
     * @param color
     */
    public void drawNode(Graphics2D g2d, int realX, int realY, float scale, Color color) {
        g2d.setColor(color);
        g2d.fillRect(getScaledX(realX, scale), getScaledY(realY, scale), getScaledWidth(scale), getScaledHeight(scale));
    }

    /**
     *
     * @param g2d
     * @param x
     * @param y
     * @param scale
     */
    public void drawChange(Graphics2D g2d, int x, int y, float scale) {
        g2d.setColor(Color.white);
        g2d.fillOval(getScaledX(x, scale) - 1, getScaledY(y, scale) - 1, getScaledWidth(scale) + 2, getScaledHeight(scale) + 2);
        g2d.setPaint(getChangePaint(x, y, nodeWidth + x, nodeHeight + y));
        g2d.fillOval(getScaledX(x, scale), getScaledY(y, scale), getScaledWidth(scale), getScaledHeight(scale));

    }

    /**
     *
     * @param g2d
     * @param realX
     * @param realY
     * @param nodeSize
     */
    public void drawText(Graphics2D g2d, int realX, int realY, int nodeSize) {
        FontMetrics fm = g2d.getFontMetrics(g2d.getFont().deriveFont(10f));
        g2d.setFont(g2d.getFont().deriveFont(10f));
        g2d.setColor(Color.white);
        g2d.setComposite(BlendComposite.Difference);
        switch (textRotation) {
            case NORTH:
                g2d.rotate(Math.toRadians(270), getScaledX(realX, 1), getScaledY(realY, 1));
                g2d.drawString(name, getScaledX(realX, 1), getScaledY(realY, 1) + fm.getHeight() / 2);
                g2d.rotate(-Math.toRadians(270), getScaledX(realX, 1), getScaledY(realY, 1));
                break;
            case SOUTH:
                g2d.rotate(Math.toRadians(270), getScaledX(realX, 1), getScaledY(realY, 1));
                g2d.drawString(name, getScaledX(realX, 1) - (int) (fm.getStringBounds(name, g2d).getWidth()) - nodeSize, getScaledY(realY, 1) + fm.getHeight() / 2);
                g2d.rotate(-Math.toRadians(270), getScaledX(realX, 1), getScaledY(realY, 1));
                break;
            case EAST:
                g2d.drawString(name, getScaledX(realX, 1) - (int) (fm.getStringBounds(name, g2d).getWidth()), getScaledY(realY, 1) + fm.getHeight() / 2);
                break;
            case WEST:
                g2d.drawString(name, getScaledX(realX, 1) + nodeSize, getScaledY(realY, 1) + fm.getHeight() / 2);
                break;
            case NORTH_WEST:
                g2d.rotate(Math.toRadians(45), getScaledX(realX, 1), getScaledY(realY, 1));
                g2d.drawString(name, getScaledX(realX, 1) - (int) (fm.getStringBounds(name, g2d).getWidth()), getScaledY(realY, 1) + fm.getHeight() / 2);
                g2d.rotate(-Math.toRadians(45), getScaledX(realX, 1), getScaledY(realY, 1));
                break;
            case NORTH_EAST:
                g2d.rotate(Math.toRadians(315), getScaledX(realX, 1), getScaledY(realY, 1));
                g2d.drawString(name, getScaledX(realX, 1) + nodeSize, getScaledY(realY, 1) + fm.getHeight() / 2);
                g2d.rotate(-Math.toRadians(315), getScaledX(realX, 1), getScaledY(realY, 1));
                break;
        }
        g2d.setComposite(BlendComposite.Normal);
    }

    /**
     * Used to toggle the next text direction.
     */
    public void nextTextRotation() {
        textRotation++;
        if (textRotation > 5) {
            textRotation = 0;
        }
    }

    /**
     *
     * Allows the dragging of a MetroNode should the mouseDragged reach this
     * method.
     *
     */
    @Override
    public void mouseDragged(CanvasMouseDragEvent e) {
        MetroCreator parent = (MetroCreator) this.parent;
        this.x = parent.getGrid().snapToGridX(e.getNewElementPosition().x);
        this.y = parent.getGrid().snapToGridY(e.getNewElementPosition().y);
        repaintAll();
    }

    @Override
    public void mousePressed(CanvasMouseEvent e) {
    }

    @Override
    public void mouseClicked(CanvasMouseEvent e) {
    }

    @Override
    public void mouseReleased(CanvasMouseReleaseEvent e) {
    }

    /**
     * Overridden to allow the outputting of a MetroNodes details.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append("\t").
                append(getRealX()).append("\t").
                append(getRealY()).append("\t").
                append(getWidth()).append("\t").
                append(getHeight()).append("\t");
        if (!lines.isEmpty()) {
            Iterator<Entry<String, MetroLine>> i = lines.entrySet().iterator();
            boolean hasNext = true;
            Entry<String, MetroLine> entry = i.next();
            while (hasNext) {
                sb.append(entry.getKey());
                if (hasNext = i.hasNext()) {
                    sb.append(',');
                    entry = i.next();
                }
            }
            sb.append("\t");
        }
        sb.append(isLineChange()).append("\t")
                .append(textRotation).append("\t");
        return sb.append("\n").toString();
    }

    @Override
    public void mouseMoved(CanvasMouseEvent e) {
    }

    /**
     *
     * Toggles whether this MetroNode is currently being edited (Currently
     * limited to the name)
     */
    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    /**
     *
     * If a KeyEvent reaches this node and it is being edited, the name will
     * update depending on a users input.
     *
     */
    @Override
    public void onKeyPress(KeyEvent e) {
        if (editing) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (parent instanceof MetroCreator) {
                    for (MetroNode node : ((MetroCreator) parent).getMetro().getNodes()) {
                        if (node.getName().equals(this.getName())) {
                            this.setName("!!EXISTS!!");
                            repaint();
                            return;
                        }
                    }
                    editing = false;
                    ((MetroCreator) parent).getMetro().addNode(this);
                    ((MetroCreator) parent).addElement(this);
                    ((MetroCreator) parent).setMouseElement(null);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                if (this.getName().length() > 0) {
                    this.setName(this.getName().substring(0, this.getName().length() - 1));
                }
            } else if (Character.isLetterOrDigit(e.getKeyCode()) || e.getKeyCode() == KeyEvent.VK_SPACE) {
                this.setName(this.getName() + e.getKeyChar());
            }
        }
        repaint();
    }

    public Map<String, MetroLine> getLines() {
        return lines;
    }

    @Override
    public boolean clickThrough() {
        return false;
    }

    @Override
    public Cursor getMouseOverCursor() {
        return MouseUtils.HAND_CURSOR;
    }

    public MetroLine getLine() {
        if (onlyLine == null) {
            return lines.values().iterator().next();
        }
        return onlyLine;
    }
}
