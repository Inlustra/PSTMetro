package com.pstmetro.dijkstra;

import com.pstmetro.generics.ArrayMap;
import com.pstmetro.generics.ArraySet;
import com.pstmetro.generics.NodeLinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @param <V> The type of the Node, in this case a
 * DijkstraAlgorithm&lt;MetroNode&gt; would be created.
 * @author Thomas
 */
public class DijkstraAlgorithm<V> {

    /**
     * This map will be used in tracing back the steps of an optimal path to a
     * node.
     */
    private Map<V, V> previousNodes;
    /**
     * This map will be used in tracing back the steps of an optimal path to a
     * node.
     */
    private Map<V, Integer> previousTimes;
    /**
     * This set is used in the generation of the optimal routes to each node and
     * is discarded upon termination of the algorithm.
     */
    private Set<V> uncheckedNodes;
    /**
     * This set is used in the generation of the optimal routes to each node and
     * is discarded upon termination of the algorithm.
     */
    private Set<V> checkedNodes;
    /**
     * The source of the DijkstraAlgorithm of type V
     */
    protected V source;
    /**
     * All nodes used in the calculation of the DijkstraAlgorithm.
     */
    protected V[] nodes;
    /**
     * All connections along with costs used in the calculation of the
     * DijkstraAlgorithm.
     */
    protected DijkstraConnection<V>[] connections;

    /**
     *
     * A Generic "Greedy" Path-finding algorithm. </br>
     * This algorithm is greedy in the sense that it makes a locally optimal
     * choice at each step and does not take into account the global
     * structure.</br>
     * The constructor instantiates the required maps and adds local pointers to
     * the arrays of Connections and Nodes.
     *
     * @param map The DijkstraGraph used to acquire the array of nodes and array
     * of connections
     * @param source The Initial node
     *
     * @see #run() Run(), the start of the algorithm.
     */
    public DijkstraAlgorithm(DijkstraGraph<V> map, V source) {
        this.nodes = map.getNodes();
        this.connections = map.getConnections();
        this.source = source;

        this.previousNodes = new ArrayMap<>();
        this.previousTimes = new ArrayMap<>();
        this.uncheckedNodes = new ArraySet<>();
        this.checkedNodes = new ArraySet<>();

        this.uncheckedNodes.add(source);
        run();
    }

    /**
     * The bulk of the algorithm. This code is run once, when this method
     * terminates we can assume the Algorithm has finished. <br/>
     *
     * <b>Note: Please view this in the HTML Javadoc as the Netbeans Autoformat
     * tool doesn't manage this very well.</b><br/><br/>
     *
     * The loop will continue while:
     * <pre> (uncheckedNodes.size() > 0) </pre> The list is not iterated upon,
     * this ensures that we can add and remove from the list without throwing a
     * <a
     * href="http://docs.oracle.com/javase/7/docs/api/java/util/ConcurrentModificationException.html">
     * ConcurrentModificationException</a></br>
     * <br/>
     * 1. The node with the quickest previously tested connection is returned
     * via: {@link #getClosest(java.util.Set) getClosest(Set<V>)}
     * <pre> V node = getClosest(uncheckedNodes);</pre> 2. This node is removed
     * from the uncheckedNodes and added to the checkedNodes in order to prevent
     * the algorithm from re-checking this node in the next step.
     * <pre> checkedNodes.add(node);
     * uncheckedNodes.remove(node); </pre> 3. The nodes neighbours that have not
     * yet been checked are gathered via
     * {@link #getUncheckedConnections(Object) getUncheckedConnections(V)}</br>
     * 4. The neighbours are then checked against any previous assigned
     * time/value from checking one of this nodes neighbours</br>
     * if this node is found to be faster than the previous assigned value:
     * <pre> getFastestPreviousTime(currentNode) > getFastestPreviousTime(node) + getCostOfConnection(node, currentNode)</pre>
     * See:
     * {@link #getFastestPreviousTime(Object) getFastestPreviousTime(V)}, {@link #getCostOfConnection(Object,Object) getCostOfConnection(V,V)}
     * </br>
     * 5. The previous times and nodes are replaced with the new values.</br>
     * 6. Return to the loop until the uncheckedNodes Set is empty.</br>
     * </br>
     * When this method has completed, the user is able to any quickest journey
     * pull from the {@link #getJourney(Object) getJourney(V)} method
     *
     *
     */
    private void run() {
        while (uncheckedNodes.size() > 0) {
            V node = getClosest(uncheckedNodes);
            checkedNodes.add(node);
            uncheckedNodes.remove(node);
            checkNode(node);
        }
    }

    /**
     *
     * This method chooses the next node from the uncheckedNodes set. </br>
     * if the set contains no nodes that have been previously checked, the first
     * iterated node will be chosen.
     *
     * @param nodes The set of nodes from which we will get the node with the
     * shortest path.
     * @return The node with the lowest previously checked connection time.
     */
    private V getClosest(Set<V> nodes) {
        V closest = null;
        for (V node : nodes) {
            if (closest == null) {
                closest = node;
            } else {
                if (getFastestPreviousTime(node) < getFastestPreviousTime(closest)) {
                    closest = node;
                }
            }
        }
        return closest;
    }

    /**
     *
     * Loops through all the connections to the desired node, if the time to
     * this node + all previous times from this node is lower than any
     * previously saved value, the value and previousNode is replaced.
     *
     * @param node
     */
    private void checkNode(V node) {
        List<V> l = getUncheckedConnections(node);
        for (V currentNode : l) {
            if (getFastestPreviousTime(currentNode) > getFastestPreviousTime(node) + getCostOfConnection(node, currentNode)) {
                previousNodes.put(currentNode, node);
                previousTimes.put(currentNode, getFastestPreviousTime(node) + getCostOfConnection(node, currentNode));
                uncheckedNodes.add(currentNode);
            }
        }
    }

    /**
     *
     * Loops through all the connections, if the connection between the nodes is
     * found, it is returned.
     *
     * @param node The source node.
     * @param destination the destination node.
     * @return the cost of the connection
     *
     * @throws RuntimeException, if no connection is found connecting the 2
     * nodes.
     */
    protected int getCostOfConnection(V node, V destination) {
        for (DijkstraConnection connection : connections) {
            if (connection.getSource().equals(node)
                    && connection.getDestination().equals(destination)) {
                return connection.getCost();
            }
        }
        throw new RuntimeException("No Connection found - At all...");
    }

    /**
     *
     * Checks for a previously saved time it would take to reach this node. if
     * it is found, it is returned, else, Integer.MAX_VALUE is returned.
     *
     * @param node
     * @return Either a previously saved value or Integer.MAX_VALUE
     */
    protected int getFastestPreviousTime(V node) {
        if (previousTimes.containsKey(node)) {
            return previousTimes.get(node);
        } else {
            return Integer.MAX_VALUE;
        }
    }

    /**
     *
     * returns the unchecked connections where this node is the source.
     *
     * @param source The node that is the source of the connections.
     * @return a List of nodes.
     */
    protected List<V> getUncheckedConnections(V source) {
        List<V> l = new ArrayList<>();
        for (DijkstraConnection<V> conn : connections) {
            if (conn.getSource() == source && !checkedNodes.contains(conn.getDestination())) {
                l.add(conn.getDestination());
            }
        }
        return l;
    }

    /**
     *
     * Returns the Journey from source to the target. This is achieved by
     * creating a linked list that will serve as the order of the Path to take,
     * we then add to this list by retrieving from the map of nodes and continue
     * this process until the current step becomes null. The LinkedList is then
     * reversed giving our path from start to finish.
     *
     * @param target the destination node.
     * @return a List of the nodes required to traverse in order to arrive at
     * the target node.
     */
    public NodeLinkedList<V> getJourney(V target) {
        NodeLinkedList<V> path = new NodeLinkedList<>();
        V step = target;
        // Check if a path exists
        if (previousNodes.get(step) == null) {
            return null;
        }
        path.add(step);
        while (previousNodes.get(step) != null) {
            step = previousNodes.get(step);
            path.add(step);
        }
        // Put it into the correct order
        path.reverse();
        return path;
    }

    /**
     * Returns the source of this current algorithm.
     *
     * @return the source of this current algorithm.
     */
    public V getSource() {
        return source;
    }
}
