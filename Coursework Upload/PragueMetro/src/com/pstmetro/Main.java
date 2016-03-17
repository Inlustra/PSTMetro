/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pstmetro;

import com.inlustra.gui.containers.CanvasContainer;
import com.inlustra.gui.CanvasWindow;
import com.inlustra.gui.containers.CanvasTopBar;
import com.inlustra.gui.containers.overlay.CanvasFadeMessageOverlay;
import com.inlustra.gui.elements.CanvasElement.CanvasPosition;
import com.inlustra.gui.elements.heavy.CanvasButton;
import com.inlustra.gui.elements.input.StringInputButton;
import com.inlustra.gui.utils.CanvasFont;
import com.inlustra.misc.Misc;
import com.pstmetro.dijkstra.DijkstraAlgorithm;
import com.pstmetro.gui.containers.JourneyBar;
import com.pstmetro.gui.containers.MetroPlanner;
import com.inlustra.gui.utils.CanvasImageUtils;
import com.pstmetro.io.MetroIO;
import com.inlustra.gui.elements.heavy.MoveableImage;
import com.pstmetro.gui.CreatorWindow;
import com.pstmetro.metro.node.MetroMap;
import com.pstmetro.metro.node.MetroNode;
import com.pstmetro.utils.MetroColor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;

/**
 *
 * @author Thomas
 */
public class Main {

    /**
     * Starts the application - {@link #createSelector See createSelector}.
     *
     * @param args
     */
    public static void main(String[] args) {
        createSelector();
    }

    /**
     * Creates the CanvasWindow used in selecting the MetroCreator GUI or the
     * MetroPlanner GUI. The GUI is created by instantiating a CanvasWindow
     * object and adding the required Components </br> A CanvasTopBar and all
     * the required CanvasButtons. </br> The Metro Systems are loaded from the
     * "/PSTMetro/Systems/" folder.
     *
     * @return the CanvasWindow associated with the GUI for convenience
     */
    public static CanvasWindow createSelector() {
        final CanvasWindow stationSelector = new CanvasWindow(500, 300);
        try {
            stationSelector.getFrame().getRootPane().setBorder(BorderFactory.createLineBorder(new Color(0, 180, 255)));
            CanvasTopBar topBar = new CanvasTopBar("PSTMetro", new Color(0, 180, 255),
                    Misc.getScaledImage(CanvasImageUtils.getImage("logo.png"), 30, 30),
                    0, 0, stationSelector.getWidth(), 30);
            stationSelector.addElement(topBar);
            topBar.addButton(new CanvasButton("x", new Color(0, 180, 255), 0, 0, 28, 30) {
                @Override
                public void onButtonPress(CanvasButton source) {
                    System.exit(0);
                }
            });
            if (!MetroIO.METRO_PATH.exists()) {
                MetroIO.METRO_PATH.mkdirs();
            }
            final File[] fileArray = MetroIO.METRO_PATH.listFiles();
            List<File> fileList = new ArrayList<>();
            for (File file : fileArray) {
                if (!file.getName().startsWith(".")) {
                    fileList.add(file);
                }
            }
            final File[] files = fileList.toArray(new File[fileList.size()]);
            int height = 60;
            for (int i = 0; i < files.length; i++) {
                final int index = i;
                stationSelector.addElement(new CanvasButton(files[i].getName(), ((i % 2 == 0) ? MetroColor.blue : MetroColor.darker),
                        CanvasFont.getFont("Walkway SemiBold.ttf").deriveFont((float) 60), 0,
                        30 + (height * i), stationSelector.getWidth() - height, height) {
                    @Override
                    public void onButtonPress(CanvasButton source) {
                        stationSelector.getFrame().setVisible(false);
                        CanvasWindow frame = createPlanner(files[index].getName());
                        frame.getFrame().addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                stationSelector.getFrame().setVisible(true);
                                stationSelector.repaintAll();
                            }
                        });
                    }
                });
                stationSelector.addElement(new CanvasButton(Misc.recolor(CanvasImageUtils.getImage("pencil.png"), Color.black, Color.white),
                        ((i % 2 == 0) ? MetroColor.edit : MetroColor.darkerEdit),
                        stationSelector.getWidth() - height,
                        30 + (height * i), height, height) {
                    @Override
                    public void onButtonPress(CanvasButton source) {
                        stationSelector.getFrame().setVisible(false);
                        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                        CanvasWindow frame = new CreatorWindow(files[index].getName(), 1000, 600);
                        frame.getFrame().addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                stationSelector.getFrame().setVisible(true);
                                stationSelector.repaintAll();
                            }
                        });
                    }
                });
            }
            StringInputButton sib = new StringInputButton("Create new", new Color(9, 178, 89),
                    CanvasFont.getFont("Walkway SemiBold.ttf").deriveFont((float) 60), 0,
                    30 + (height * files.length), stationSelector.getWidth(), height) {
                @Override
                public void onSetString(String string) {
                    if (!string.isEmpty()) {
                        try {
                            File folder = new File(MetroIO.METRO_PATH, string);
                            if (!folder.exists()) {
                                folder.mkdirs();
                                File f = new File(folder, "lines.txt");
                                f.createNewFile();
                                f = new File(folder, "nodes.txt");
                                f.createNewFile();
                                f = new File(folder, "connections.txt");
                                f.createNewFile();
                                f = new File(folder, "images.txt");
                                f.createNewFile();
                                f = new File(folder, "settings.txt");
                                f.createNewFile();
                            } else {
                                setTimedMessage("System Exists!", 2, "Create new");
                            }
                            stationSelector.getFrame().dispose();
                            createSelector();
                        } catch (Exception e) {
                            setTimedMessage("Error!", 2, "Create new");
                        }
                    }
                }
            };
            stationSelector.addElement(sib);
            stationSelector.getFrame().setLocationRelativeTo(null);
            stationSelector.getFrame().setVisible(true);
            stationSelector.repaintAll();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stationSelector;
    }

    /**
     * Creates the CanvasWindow used in creating the MetroPlanner GUI. The GUI
     * is created by instantiating a CanvasWindow object and adding the required
     * Components.
     *
     * @todo replace with a class
     *
     * @param metroName The System folder in which the Metro is located such as
     * "/PSTMetro/Systems/metroName/"
     * @return the CanvasWindow associated with the GUI for convenience
     *
     * @see com.pstmetro.io.MetroIO#readMetroLines readMetroLines
     * @see com.pstmetro.io.MetroIO#readMetroNodes readMetroNodes
     * @see com.pstmetro.io.MetroIO#readMetroConnections readMetroConnections
     * @see com.pstmetro.io.MetroIO#readImages readImages
     *
     */
    public static CanvasWindow createPlanner(String metroName) {
        final MetroMap metro = new MetroMap();
        File metroFolder = MetroIO.getMetroFolder(metroName);
        MetroIO.readMetroLines(metro, new File(metroFolder, "lines.txt"));
        MetroIO.readMetroNodes(metro, new File(metroFolder, "nodes.txt"));
        MetroIO.readMetroConnections(metro, new File(metroFolder, "connections.txt"));
        MoveableImage[] images = MetroIO.readImages(new File(metroFolder, "images.txt"), metroName);
        final CanvasWindow canvasFrame = new CanvasWindow(1000, 600);
        canvasFrame.getFrame().setLocationRelativeTo(null);
        canvasFrame.getFrame().setVisible(true);
        try {
            canvasFrame.setBackgroundColor(Color.darkGray);
            canvasFrame.getFrame().getRootPane().setBorder(BorderFactory.createLineBorder(new Color(0, 180, 255)));
            CanvasTopBar topBar = new CanvasTopBar("PSTMetro", new Color(0, 180, 255),
                    Misc.getScaledImage(CanvasImageUtils.getImage("logo.png"), 30, 30),
                    0, 0, canvasFrame.getWidth(), 30);
            topBar.addButton(new CanvasButton("x", new Color(0, 180, 255), 0, 0, 28, 30) {
                @Override
                public void onButtonPress(CanvasButton source) {
                    canvasFrame.getFrame().dispose();
                }
            });
            topBar.addButton(new CanvasButton("-", new Color(0, 180, 255), 0, 0, 28, 30) {
                @Override
                public void onButtonPress(CanvasButton source) {
                    JFrame frame = getCanvasFrame().getFrame();
                    frame.setState(JFrame.ICONIFIED);
                }
            });
            canvasFrame.addElement(topBar);
            final CanvasContainer planner = new CanvasContainer(0, 30, canvasFrame.getWidth(), canvasFrame.getHeight() + 60);
            final JourneyBar jb = new JourneyBar(metro, 0, planner.getHeight(), planner.getWidth(), planner.getHeight()*2);
            final MetroPlanner mp = new MetroPlanner(metro, 0, 0, planner.getWidth(), planner.getHeight() - 80) {
                @Override
                public void onChooseJourneySource(DijkstraAlgorithm<MetroNode> algorithm) {
                    jb.setSource(algorithm.getSource());
                    jb.moveTo(0, planner.getHeight() - 80 - 30 - 60, 200);
                }

                @Override
                public void onChooseJourneyDest(List<MetroNode> journey) {
                    jb.setJourney(journey);
                    jb.moveTo(0, 0, 200);
                    CanvasFadeMessageOverlay.displayMessage(canvasFrame, "Drag down to select another journey", 4, new Color(0,180,255));
                }

                @Override
                public void onClearJourney() {
                    jb.clearJourney();
                }
            };
            for (MoveableImage mi : images) {
                mi.setPosition(CanvasPosition.BACKGROUND);
                mp.addElement(mi);
            }
            planner.addElement(mp);
            planner.addElement(jb);
            canvasFrame.addElement(planner);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        canvasFrame.repaintAll();
        return canvasFrame;
    }
}
