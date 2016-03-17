/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pstmetro.io;

import com.inlustra.gui.elements.CanvasElement;
import com.inlustra.gui.utils.CanvasImageUtils;
import com.pstmetro.gui.containers.MetroCreator;
import com.inlustra.gui.elements.heavy.MoveableImage;
import com.pstmetro.metro.node.MetroConnection;
import com.pstmetro.metro.node.MetroMap;
import com.pstmetro.metro.node.MetroLine;
import com.pstmetro.metro.node.MetroNode;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas
 */
public class MetroIO {

    /**
     *
     */
    public static File METRO_PATH = new File("PSTMetro/Systems");

    /**
     *
     * @param metroName
     * @return
     */
    public static File getMetroFolder(String metroName) {
        File file = new File(METRO_PATH, metroName);
        if (file.exists()) {
            return file;
        }
        return null;
    }

    /**
     *
     * @param metroName
     * @return
     */
    public static File getMetroImageFolder(String metroName) {
        File file = new File(METRO_PATH, metroName);
        if (file.exists()) {
            file = new File(file, "images/");
            file.mkdirs();
            return file;
        }
        return null;
    }

    /**
     *
     * @param map
     * @param file
     * @return
     */
    public static MetroMap readMetroNodes(MetroMap map, File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || !line.contains("\t")) {
                    continue;
                }
                try {
                    map.addNode(line);
                } catch (Exception e) {
                    System.err.println("Couldn't read node: " + line);
                    System.err.println(e.getMessage());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MetroIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }

    /**
     *
     * @param map
     * @param file
     * @return
     */
    public static MetroMap readMetroConnections(MetroMap map, File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || !line.contains("\t")) {
                    continue;
                }
                try {
                    map.addConnection(line);
                } catch (Exception e) {
                    System.err.println("Couldn't read connection: " + line);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MetroIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }

    /**
     *
     * @param map
     * @param file
     * @return
     */
    public static MetroMap readMetroLines(MetroMap map, File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || !line.contains("\t")) {
                    continue;
                }
                try {
                    map.addLine(line);
                } catch (Exception e) {
                    System.err.println("Couldn't read line: " + line);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MetroIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }

    /**
     *
     * @param metroName
     * @param imageName
     * @return
     */
    public static Image getMetroImage(String metroName, String imageName) {
        return CanvasImageUtils.getImage(new File(getMetroImageFolder(metroName), imageName));
    }

    /**
     *
     * @param file
     * @param metroName
     * @return
     */
    public static MoveableImage[] readImages(File file, String metroName) {
        List<MoveableImage> images = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || !line.contains("\t")) {
                    continue;
                }
                try {
                    String[] split = line.split("\t");
                    images.add(new MoveableImage(new File(getMetroImageFolder(metroName), split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])));
                } catch (Exception e) {
                    System.err.println("Couldn't read line: " + line);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MetroIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return images.toArray(new MoveableImage[images.size()]);
    }

    /**
     *
     * @param file
     * @return
     */
    public static Properties readProperties(File file) {
        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream(file));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }

    /**
     *
     * @param file
     * @param metro
     * @throws IOException
     */
    public static void saveMetroNodes(File file, MetroMap metro) throws IOException {
        renewFile(file);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (MetroNode node : metro.getNodes()) {
                if (node.hasLines()) {
                    bw.write(node.toString());
                } else {
                    System.err.println("Node had no lines: " + node.getName());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MetroIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param file
     * @param metro
     * @throws IOException
     */
    public static void saveMetroConnections(File file, MetroMap metro) throws IOException {
        renewFile(file);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (MetroConnection connection : metro.getConnections()) {
                bw.write(connection.toString());
            }
        } catch (IOException ex) {
            Logger.getLogger(MetroIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param file
     * @param metro
     * @throws IOException
     */
    public static void saveMetroLines(File file, MetroMap metro) throws IOException {
        renewFile(file);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (MetroLine line : metro.getLines()) {
                bw.write(line.toString());
            }
        } catch (IOException ex) {
            Logger.getLogger(MetroIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void renewFile(File file) throws IOException {
        if (file.exists()) {
            file.delete();
        } else {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
    }

    /**
     *
     * @param mc
     */
    public static void saveImages(MetroCreator mc) {
        try {
            File images = new File(getMetroFolder(mc.getName()), "images.txt");
            renewFile(images);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(images))) {
                for (CanvasElement ce : mc.getElements()) {
                    if (ce instanceof MoveableImage) {
                        MoveableImage mi = (MoveableImage) ce;
                        bw.write(mi.getImage().getName() + "\t" + mi.getX() + "\t" + mi.getY() + "\n");
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(MetroIO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(MetroIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param mc
     */
    public static void saveSettings(MetroCreator mc) {
        try {
            File settings = new File(getMetroFolder(mc.getName()), "settings.txt");
            renewFile(settings);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(settings))) {
                mc.getSettings().store(bw, null);
            } catch (IOException ex) {
                Logger.getLogger(MetroIO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(MetroIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
