/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pstmetro.gui;

import com.inlustra.gui.CanvasWindow;
import com.inlustra.gui.containers.CanvasSideBar;
import com.inlustra.gui.containers.CanvasTopBar;
import com.inlustra.gui.containers.overlay.CanvasYesNoOverlay;
import com.inlustra.gui.elements.heavy.CanvasButton;
import com.inlustra.gui.elements.heavy.MoveableImage;
import com.inlustra.gui.utils.CanvasImageUtils;
import com.inlustra.misc.Misc;
import com.pstmetro.Main;
import com.pstmetro.gui.containers.MetroCreator;
import com.pstmetro.gui.containers.MetroLineCreator;
import com.pstmetro.io.MetroIO;
import com.pstmetro.metro.node.MetroLine;
import com.pstmetro.metro.node.MetroMap;
import com.pstmetro.metro.node.MetroNode;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;

/**
 *
 * @author Thomas
 */
public class CreatorWindow extends CanvasWindow {

    /**
     * Gui components
     */
    final CanvasSideBar sideBar;
    final CanvasTopBar topBar;
    final MetroCreator mc;
    /**
     * A simple list used in the creation of the sidebar.
     */
    private HashMap<MetroLine, CanvasButton[]> sidebarButtonList = new HashMap<>();

    /**
     * Creates the CanvasWindow used in creating the MetroCreator GUI. The GUI
     * is created by instantiating a CanvasWindow object and adding the required
     * Components. This class is rather messy as the basis of the CanvasGUI is
     * absolute positioning and therefore could not be helped.
     *
     * @param metroName The System folder in which the Metro is located such as
     * "/PSTMetro/Systems/metroName/"
     * @param width Width of the CreatorWindow
     * @param height Height of the CreatorWindow
     *
     * @see com.pstmetro.io.MetroIO#readMetroLines readMetroLines
     * @see com.pstmetro.io.MetroIO#readMetroNodes readMetroNodes
     * @see com.pstmetro.io.MetroIO#readMetroConnections readMetroConnections
     * @see com.pstmetro.io.MetroIO#readImages readImages
     *
     */
    public CreatorWindow(String metroName, int width, int height) {
        super(width, height);
        final CanvasWindow cf = this;
        final MetroMap metro = new MetroMap();
        final File metroFolder = MetroIO.getMetroFolder(metroName);
        MetroIO.readMetroLines(metro, new File(metroFolder, "lines.txt"));
        MetroIO.readMetroNodes(metro, new File(metroFolder, "nodes.txt"));
        MetroIO.readMetroConnections(metro, new File(metroFolder, "connections.txt"));
        MoveableImage[] images = MetroIO.readImages(new File(metroFolder, "images.txt"), metroName);
        Properties props = MetroIO.readProperties(new File(metroFolder, "settings.txt"));
        this.getFrame().setLocationRelativeTo(null);
        this.getFrame().setVisible(true);
        this.setBackgroundColor(Color.darkGray);
        this.getFrame().getRootPane().setBorder(BorderFactory.createLineBorder(new Color(0, 180, 255)));
        Image image = null;
        try {
            image = Misc.getScaledImage(CanvasImageUtils.getImage("logo.png"), 30, 30);
        } catch (IOException ex) {
            Logger.getLogger(CreatorWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        topBar = new CanvasTopBar("PSTMetro", new Color(0, 180, 255),
                image,
                0, 0, this.getWidth(), 30);

        this.addElement(topBar);
        mc = new MetroCreator(metroName, props, metro, 50, 30, this.getWidth() - 50, this.getHeight());

        sideBar = new CanvasSideBar(0, 30, 50, this.getHeight());
        sideBar.addButton(new CanvasButton(Misc.recolor(CanvasImageUtils.getImage("change.png"), Color.black, Color.white), new Color(0, 180, 255), 0, 0, 50, 50) {
            @Override
            public void onButtonPress(CanvasButton source) {
                cf.addElement(new MetroLineCreator(0, 0, cf.getWidth(), cf.getHeight()) {
                    @Override
                    public void createdLine(final MetroLine line) {
                        mc.getMetro().addLine(line);
                        addLineButtons(line);
                        cf.removeElement(this);
                    }
                });
            }
        });
        sideBar.addButton(mc.setToggleRotatingButton(new CanvasButton(Misc.recolor(CanvasImageUtils.getImage("arrow.png"), Color.black, Color.white), new Color(0, 180, 255), 0, 0, 50, 50) {
            @Override
            public void onButtonPress(CanvasButton source) {
                mc.toggleRotate();
            }
        }));
        sideBar.addButton(mc.setToggleDeletingButton(new CanvasButton(Misc.recolor(CanvasImageUtils.getImage("delete.png"), Color.black, Color.white), new Color(0, 180, 255), 0, 0, 50, 50) {
            @Override
            public void onButtonPress(CanvasButton source) {
                mc.toggleDelete();
            }
        }));
        for (final MetroLine line : metro.getLines()) {
            addLineButtons(line);
        }
        topBar.addButton(new CanvasButton("x", new Color(0, 180, 255), 0, 0, 28, 30) {
            @Override
            public void onButtonPress(CanvasButton source) {
                try {
                    MetroIO.saveMetroNodes(new File(metroFolder, "nodes.txt"), metro);
                    MetroIO.saveMetroConnections(new File(metroFolder, "connections.txt"), metro);
                    MetroIO.saveMetroLines(new File(metroFolder, "lines.txt"), metro);
                    MetroIO.saveSettings(mc);
                    MetroIO.saveImages(mc);
                    cf.getFrame().dispose();
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        topBar.addButton(new CanvasButton("-", new Color(0, 180, 255), 0, 0, 28, 30) {
            @Override
            public void onButtonPress(CanvasButton source) {
                JFrame frame = getCanvasFrame().getFrame();
                frame.setState(JFrame.ICONIFIED);
            }
        });
        for (MoveableImage mi : images) {
            mi.setPosition(CanvasPosition.BACKGROUND);
            mc.addElement(mi);
        }
        this.addElement(sideBar);
        this.addElement(mc);
        this.repaintAll();
    }

    /**
     * Creates 2 CanvasButtons and adds them to the CanvasSidebar. </br>
     * These buttons are used to add MetroNodes to the corresponding MetroLine.
     *
     * @param line The MetroLine used to get the Color of the button and create
     * nodes associated with this line.
     */
    public void addLineButtons(final MetroLine line) {
        final CanvasButton addConnection;
        final CanvasWindow cw = this;
        final CanvasButton addNode = new CanvasButton(Misc.recolor(CanvasImageUtils.getImage("node.png"), Color.black, Color.white), line.getColor(), 0, 0, 50, 50) {
            @Override
            public void onButtonPress(CanvasButton source) {
                if (!mc.isDeleting()) {
                    MetroNode mn = new MetroNode("", MetroNode.EAST, 0, 0, 10, 10);
                    mn.addLine(line);
                    mc.setMouseElement(mn);
                } else {
                    CanvasYesNoOverlay overlay = new CanvasYesNoOverlay("Are you sure you want to remove: " + line.getName(), 0, 0, cw.getWidth(), cw.getHeight()) {
                        @Override
                        public void onClickYes() {
                            mc.removeLine(line);
                            removeLineButtons(line);
                        }

                        @Override
                        public void onClickNo() {
                        }
                    };
                    cw.addElement(overlay);
                }
            }
        };
        addConnection = new CanvasButton(Misc.recolor(CanvasImageUtils.getImage("two-way.png"), Color.black, Color.white), line.getColor(), 0, 0, 50, 50) {
            @Override
            public void onButtonPress(CanvasButton source) {
                mc.setConnectionLine(line);
            }
        };
        CanvasButton[] btns = {addNode, addConnection};
        sidebarButtonList.put(line, btns);
        sideBar.addButton(addNode);
        sideBar.addButton(addConnection);
    }

    /**
     *
     * @param ml The MetroLine to remove the associated CanvasButtons from the
     * CanvasSidebar.
     */
    public void removeLineButtons(MetroLine ml) {
        sideBar.removeButton(sidebarButtonList.get(ml)[0]);
        sideBar.removeButton(sidebarButtonList.get(ml)[1]);
    }
}
