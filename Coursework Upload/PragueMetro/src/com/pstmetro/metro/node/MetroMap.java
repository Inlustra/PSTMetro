package com.pstmetro.metro.node;

import com.pstmetro.dijkstra.DijkstraGraph;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Thomas
 */
public class MetroMap implements DijkstraGraph {

    Set<MetroNode> nodes;
    Set<MetroConnection> connections;
    Set<MetroLine> lines;

    /**
     *
     */
    public MetroMap() {
        nodes = new HashSet();
        connections = new HashSet();
        lines = new HashSet<>();
    }

    /**
     *
     * @param line
     */
    public void addNode(String line) {
        if (line.contains("\t")) {
            String[] split = line.split("\t");
            MetroNode node = new MetroNode(split[0].trim(), Integer.parseInt(split[7]), Integer.parseInt(split[1]), Integer.parseInt(split[2]),
                    Integer.parseInt(split[3]), Integer.parseInt(split[4]));
            if (split[5].contains(",")) {
                String[] allChanges = split[5].split(",");
                for (String change : allChanges) {
                    node.addLine(getLine(change));
                }
            } else {
                if (!split[5].isEmpty()) {
                    node.addLine(getLine(split[5]));
                }
            }
            addNode(node);
        }
    }

    /**
     *
     * @param node
     */
    public void addNode(MetroNode node) {
        this.nodes.add(node);
    }

    /**
     *
     * @param line
     */
    public void addConnection(String line) {
        if (line.contains("\t")) {
            String[] split = line.split("\t");
            MetroNode sourceNode = getNode(split[0].trim());
            MetroNode destNode = getNode(split[1].trim());
            addConnection(new MetroConnection(sourceNode, destNode, getLine(split[3].trim()), Integer.parseInt(split[2])));
        }
    }

    /**
     *
     * @param mc
     */
    public void addConnection(MetroConnection mc) {
        this.connections.add(mc);
    }

    /**
     *
     * @param line
     */
    public void addLine(String line) {
        if (line.contains("\t")) {
            String[] split = line.split("\t");
            String[] colors = split[1].split(",");
            this.lines.add(new MetroLine(split[0].trim(), new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]))));
        }
    }

    /**
     *
     * @param line
     */
    public void addLine(MetroLine line) {
        this.lines.add(line);
    }

    /**
     *
     * @param line
     * @return
     */
    public MetroNode[] removeLine(MetroLine line) {
        List<MetroNode> removes = new ArrayList<>();
        this.lines.remove(line);
        Iterator<MetroNode> nodeIt = this.nodes.iterator();
        while (nodeIt.hasNext()) {
            MetroNode mn = nodeIt.next();
            if (mn.getLines().containsValue(line)) {
                mn.getLines().remove(line.getName());
                if (mn.getLines().isEmpty()) {
                    Iterator<MetroConnection> connectionIt = this.connections.iterator();
                    while (connectionIt.hasNext()) {
                        MetroConnection connection = connectionIt.next();
                        if (connection.getSource() == mn || connection.getDestination() == mn) {
                            connectionIt.remove();
                        }
                    }
                    removes.add(mn);
                    nodeIt.remove();
                }
            }
        }
        return removes.toArray(new MetroNode[removes.size()]);
    }

    /**
     *
     * @param mc
     */
    public void removeConnection(MetroConnection mc) {
        this.connections.remove(mc);
    }

    /**
     *
     * @param mn
     */
    public void removeNode(MetroNode mn) {
        this.nodes.remove(mn);
        Iterator<MetroConnection> connectionIt = this.connections.iterator();
        while (connectionIt.hasNext()) {
            MetroConnection connection = connectionIt.next();
            if (connection.getSource() == mn || connection.getDestination() == mn) {
                connectionIt.remove();
            }
        }
    }

    /**
     *
     * @param name
     * @return
     */
    public MetroNode getNode(String name) {
        for (MetroNode node : nodes) {
            if (node.getName().equals(name)) {
                return node;
            }
        }
        return null;
    }

    /**
     *
     * @param name
     * @return
     */
    public MetroLine getLine(String name) {
        for (MetroLine line : lines) {
            if (line.getName().equals(name)) {
                return line;
            }
        }
        return null;
    }

    /**
     *
     * @return
     */
    @Override
    public MetroConnection[] getConnections() {
        return connections.toArray(new MetroConnection[connections.size()]);
    }

    /**
     *
     * @return
     */
    public MetroLine[] getLines() {
        return lines.toArray(new MetroLine[lines.size()]);
    }

    /**
     *
     * @return
     */
    @Override
    public MetroNode[] getNodes() {
        return nodes.toArray(new MetroNode[nodes.size()]);
    }
}
