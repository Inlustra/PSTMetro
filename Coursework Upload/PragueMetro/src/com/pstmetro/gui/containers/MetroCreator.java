/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pstmetro.gui.containers;

import com.inlustra.gui.containers.CanvasContainer;
import com.inlustra.gui.containers.overlay.CanvasFadeMessageOverlay;
import com.inlustra.gui.elements.CanvasElement;
import com.inlustra.gui.elements.heavy.CanvasButton;
import com.inlustra.gui.interfaces.CanvasDroppable;
import com.inlustra.gui.mouse.CanvasMouseDragEvent;
import com.inlustra.gui.mouse.CanvasMouseEvent;
import com.inlustra.gui.mouse.CanvasMouseReleaseEvent;
import com.pstmetro.io.MetroIO;
import com.inlustra.gui.elements.heavy.MoveableImage;
import com.pstmetro.metro.node.MetroConnection;
import com.pstmetro.metro.node.MetroLine;
import com.pstmetro.metro.node.MetroMap;
import com.pstmetro.metro.node.MetroNode;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas
 */
public class MetroCreator extends CanvasContainer implements CanvasDroppable {

    private String name;
    private MetroGrid grid;
    private MetroMap metro;
    private Properties settings;
    private CanvasElement mouseElement;
    private MetroNode connected;
    private MetroLine connectionLine;
    private Point mouse;
    private boolean deleting = false;
    private boolean rotating = false;
    private BasicStroke none = new BasicStroke(0);
    private BasicStroke thick = new BasicStroke(3);
    private static Color blue = new Color(0, 180, 255);
    private static Color darker = new Color(255, 25, 66);
    private static Color edit = new Color(19, 131, 178);

    /**
     *
     * @param name
     * @param props 
     * @param metro
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public MetroCreator(String name, Properties props, MetroMap metro, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.name = name;
        this.metro = metro;
        this.settings = props;
        if (props != null) {
            this.grid = new MetroGrid(Integer.parseInt(props.getProperty("gridSize", "10")),
                    Integer.parseInt(props.getProperty("offsetX", "0")),
                    Integer.parseInt(props.getProperty("offsetY", "0")));
        } else {
            this.grid = new MetroGrid(10, 0, 0);
        }
        for (MetroNode mn : metro.getNodes()) {
            addElement(mn);
        }
    }

    /**
     *
     * @return
     */
    public MetroMap getMetro() {
        return metro;
    }

    /**
     *
     * @return
     */
    public MetroLine getConnectionLine() {
        return connectionLine;
    }

    /**
     *
     * @param connectionLine
     */
    public void setConnectionLine(MetroLine connectionLine) {
        this.connectionLine = connectionLine;
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseMoved(CanvasMouseEvent e) {
        super.mouseMoved(e);
        mouse = e.getPoint();
        if (mouseElement != null) {
            if (!mouseElement.isFocussed()) {
                mouseElement.show();
                mouseElement.setX(grid.snapToGridX(e.getPoint().x));
                mouseElement.setY(grid.snapToGridY(e.getPoint().y));
                repaintAll();
            }
        }
        if (connected != null) {
            repaintAll();
        }
    }

    /**
     *
     * @param e
     */
    @Override
    public void mousePressed(CanvasMouseEvent e) {
        if (mouseElement != null) {
            placeElement();
        } else if (getElementAt(e.getPoint()) == null) {
            for (CanvasElement el : elements) {
                el.startMove();
            }
        } else {
            super.mousePressed(e);
        }
    }

    /**
     *
     * @param evt
     */
    @Override
    public void mouseReleased(CanvasMouseReleaseEvent evt) {
        super.mouseReleased(evt); //To change body of generated methods, choose Tools | Templates.
        for (CanvasElement el : elements) {
            el.endMove();
        }
        grid.endMoveGrid();
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseClicked(CanvasMouseEvent e) {
        if (mouseElement != null) {
            placeElement();
            CanvasFadeMessageOverlay.displayMessage(this, "Type the name of your station.");
        } else {
            CanvasElement clickedElement = getElementAt(e.getPoint());
            if (deleting) {
                this.removeElement(clickedElement);
                if (clickedElement instanceof MetroNode) {
                    metro.removeNode((MetroNode) clickedElement);
                } else if (clickedElement == null) {
                    Rectangle2D r2d = new Rectangle2D.Float(e.getPoint().x - 2, e.getPoint().y - 2, 4, 4);
                    for (MetroConnection connection : metro.getConnections()) {
                        if (connection.intersects(r2d)) {
                            metro.removeConnection(connection);
                        }
                    }
                }
            } else if (rotating) {
                if (clickedElement instanceof MetroNode) {
                    ((MetroNode) clickedElement).nextTextRotation();
                } else if (clickedElement == null) {
                }
            } else if (connected == null && connectionLine != null && clickedElement instanceof MetroNode) {
                connected = (MetroNode) clickedElement;
                requestKeyFocus(this);
            } else {
                if (clickedElement instanceof MetroNode) {
                    if (connected == clickedElement) {
                        connected = null;
                        return;
                    }
                    if (connectionLine == null) {
                        CanvasFadeMessageOverlay.displayMessage(this, "Select a connection line first!", 2, Color.RED);
                        return;
                    }
                    if (!connected.changesToLine(connectionLine)) {
                        connected.addLine(connectionLine);
                    }
                    if (!((MetroNode) clickedElement).changesToLine(connectionLine)) {
                        ((MetroNode) clickedElement).addLine(connectionLine);
                    }

                    metro.addConnection(new MetroConnection(connected, (MetroNode) clickedElement, connectionLine, 2));
                    metro.addConnection(new MetroConnection((MetroNode) clickedElement, connected, connectionLine, 2));
                    connected = null;
                    repaintAll();
                }
            }
        }
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseDragged(CanvasMouseDragEvent e) {
        if (draggedElement == null && mouseElement == null) {
            grid.moveGrid(-e.getAmountDragged().x, -e.getAmountDragged().y);
            settings.setProperty("offsetX", "" + grid.getGridX());
            settings.setProperty("offsetY", "" + grid.getGridY());
            for (CanvasElement el : elements) {
                el.move(e.getAmountDragged());
            }
        } else {
            super.mouseDragged(e); //To change body of generated methods, choose Tools | Templates.
        }
    }

    private void placeElement() {
        requestKeyFocus(mouseElement);
    }

    /**
     *
     * @param e
     */
    public void setMouseElement(CanvasElement e) {
        if (mouseElement != null) {
            removeElement(mouseElement);
        }
        mouseElement = e;
        if (e != null) {
            addElement(e);
        }
    }

    /**
     *
     * @param g2d
     * @param delta
     */
    @Override
    public void drawBackground(Graphics2D g2d, float delta) {
        super.drawBackground(g2d, delta);
        int gridAmtX = (this.getWidth() / grid.gridSize) + 1;
        int gridAmtY = (this.getHeight() / grid.gridSize) + 1;
        g2d.setColor(new Color(240, 240, 240));
        for (int x = 0; x < gridAmtX; x++) {
            g2d.drawLine(grid.getOffsetX() + (x * grid.gridSize) - grid.getGridX(), 0, grid.getOffsetX() + (x * grid.gridSize) - grid.getGridX(), this.getHeight());
        }
        for (int y = 0; y < gridAmtY; y++) {
            g2d.drawLine(0, grid.getOffsetY() + (y * grid.gridSize) - grid.getGridY(), this.getWidth(), grid.getOffsetY() + (y * grid.gridSize) - grid.getGridY());
        }

    }

    /**
     *
     * @param g2d
     * @param delta
     */
    @Override
    public void drawBackgroundElements(Graphics2D g2d, float delta) {
        super.drawBackgroundElements(g2d, delta);
        for (MetroConnection connection : metro.getConnections()) {
            connection.draw(g2d, delta);
        }
        if (connected != null) {
            g2d.setColor(connectionLine.getColor());
            g2d.setStroke(thick);
            g2d.drawLine(connected.getX(), connected.getY(),
                    mouse.x, mouse.y);
            g2d.setStroke(none);
        } //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param e
     */
    @Override
    public void onKeyPress(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) //To change body of generated methods, choose Tools | Templates.
        {
            connected = null;
        }
        repaintAll();
    }

    /**
     *
     * @return
     */
    @Override
    public boolean acceptingDrops() {
        return true;
    }

    /**
     *
     * @param e
     * @param f
     */
    @Override
    public void onDrop(DropTargetDropEvent e, File f) {
        if (f.getName().endsWith("jpg") || f.getName().endsWith("png") || f.getName().endsWith("bmp")) {
            File imageFolder = new File(MetroIO.getMetroFolder(name), "images");
            imageFolder.mkdirs();
            File image = new File(imageFolder, f.getName());
            if (!image.exists()) {
                try {
                    Files.copy(FileSystems.getDefault().getPath(f.getPath()), FileSystems.getDefault().getPath(image.getPath()));
                } catch (IOException ex) {
                    Logger.getLogger(MetroCreator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            addCanvasImage(image, e.getLocation().x - this.getX(), e.getLocation().y - this.getY());
        }
    }

    /**
     *
     * @param img
     * @param x
     * @param y
     */
    public void addCanvasImage(File img, int x, int y) {
        MoveableImage cimg = new MoveableImage(img, x, y);
        cimg.setPosition(CanvasPosition.BACKGROUND);
        this.addElement(cimg);
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public boolean isDeleting() {
        return deleting;
    }

    /**
     *
     * @return
     */
    public boolean isRotating() {
        return rotating;
    }
    CanvasButton rotateBtn;
    CanvasButton deleteBtn;

    /**
     *
     * @param btn
     * @return
     */
    public CanvasButton setToggleRotatingButton(CanvasButton btn) {
        this.rotateBtn = btn;
        return btn;

    }

    /**
     *
     */
    public void toggleRotate() {
        rotating = !rotating;
        rotateBtn.setBackgroundColor(rotating ? edit : blue);
        if (deleting) {
            deleting = false;
            deleteBtn.setBackgroundColor(deleting ? darker : blue);
        }
    }

    /**
     *
     */
    public void toggleDelete() {
        deleting = !deleting;
        deleteBtn.setBackgroundColor(deleting ? darker : blue);
        if (rotating) {
            rotating = false;
            rotateBtn.setBackgroundColor(rotating ? edit : blue);
        }
    }

    /**
     *
     * @param btn
     * @return
     */
    public CanvasButton setToggleDeletingButton(CanvasButton btn) {
        this.deleteBtn = btn;
        return btn;
    }

    /**
     *
     * @param ml
     */
    public void removeLine(MetroLine ml) {
        MetroNode[] removes = metro.removeLine(ml);
        for (MetroNode mn : removes) {
            removeElement(mn);
        }
    }

    /**
     *
     * @return
     */
    public MetroGrid getGrid() {
        return grid;
    }

    /**
     *
     * @return
     */
    public Properties getSettings() {
        return settings;
    }

    /**
     *
     */
    public class MetroGrid {

        /**
         *
         */
        public int gridSize = 10;
        private int gridX = 0;
        private int gridY = 0;
        private int initialGridX = -1;
        private int initialGridY = -1;

        /**
         *
         * @param gridSize
         * @param offsetX
         * @param offsetY
         */
        public MetroGrid(int gridSize, int offsetX, int offsetY) {
            this.gridSize = gridSize;
            this.gridX = offsetX;
            this.gridY = offsetY;
        }

        /**
         *
         * @param gridSize
         * @param coord
         * @return
         */
        public int snapToGridX(int gridSize, int coord) {
            return coord - (coord % gridSize) - (gridX % gridSize);
        }

        /**
         *
         * @param coord
         * @return
         */
        public int snapToGridX(int coord) {
            return coord - (coord % gridSize) - (gridX % gridSize);
        }

        /**
         *
         * @param gridSize
         * @param coord
         * @return
         */
        public int snapToGridY(int gridSize, int coord) {
            return coord - (coord % gridSize) - (gridY % gridSize);
        }

        /**
         *
         * @param coord
         * @return
         */
        public int snapToGridY(int coord) {
            return coord - (coord % gridSize) - (gridY % gridSize);
        }

        /**
         *
         * @param x
         * @param y
         */
        public void moveGrid(int x, int y) {
            if (initialGridX == -1 || initialGridY == -1) {
                initialGridX = gridX;
                initialGridY = gridY;
            }
            gridX = initialGridX + x;
            gridY = initialGridY + y;
        }

        /**
         *
         */
        public void endMoveGrid() {
            initialGridX = -1;
            initialGridY = -1;
        }

        /**
         *
         * @return
         */
        public int getGridX() {
            return gridX;
        }

        /**
         *
         * @return
         */
        public int getGridY() {
            return gridY;
        }

        /**
         *
         * @return
         */
        public int getOffsetX() {
            return grid.getGridX() - (grid.getGridX() % grid.gridSize);
        }

        /**
         *
         * @return
         */
        public int getOffsetY() {
            return grid.getGridY() - (grid.getGridY() % grid.gridSize);
        }
    }
}
