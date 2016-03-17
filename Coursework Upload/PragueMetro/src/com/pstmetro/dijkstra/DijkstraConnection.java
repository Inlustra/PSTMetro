/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pstmetro.dijkstra;

/**
 *
 * This interface details a 1 way connection from the source to the destination
 * on a DijkstraMap. Implementing Generics allows us to specify any type of
 * node. Making this class an interface allows us to perhaps add visual
 * artifacts to the connections if they are necessary.
 *
 * @param <V> The type of the Nodes, in this case a
 * DijkstraConnection&lt;MetroNode&gt; would be created.
 * @author Thomas
 */
public interface DijkstraConnection<V> {

    /**
     * The source of the connection.
     *
     * @return The source node of type V.
     */
    public V getSource();

    /**
     * The destination of the connection.
     *
     * @return The destination node of type V.
     */
    public V getDestination();

    /**
     * The cost of this connection
     *
     * @return An integer value representing the cost of using this connection.
     */
    public int getCost();
}
