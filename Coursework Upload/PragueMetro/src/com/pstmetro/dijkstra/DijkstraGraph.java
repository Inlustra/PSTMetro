/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pstmetro.dijkstra;

/**
 *
 * This interface details a container for use with a DijkstraAlgorithm that will
 * contain the connections and nodes. While not necessary, implementing a
 * Dijkstra Graph gives us an idea of what will be contained in the algorithm.
 *
 * @param <V> The type of the Nodes, in this case a
 * DijkstraGraph&lt;MetroNode&gt; would be created.
 * @author Thomas
 */
public interface DijkstraGraph<V> {

    /**
     * The array of DijkstraConnections to be used with the DijkstraAlgorithm.
     *
     * @return
     */
    public DijkstraConnection<V>[] getConnections();

    /**
     * The array of DijkstraNodes to be used with the DijkstraAlgorithm of type
     * V.
     *
     * @return
     */
    public V[] getNodes();
}
