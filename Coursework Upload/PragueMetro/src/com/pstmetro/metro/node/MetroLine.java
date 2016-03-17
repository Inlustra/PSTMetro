/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pstmetro.metro.node;

import java.awt.Color;

/**
 *
 * @author Thomas
 */
public class MetroLine {
    /**
     *  The name of the MetroLine
     */
    public String name;
    /**
     *  The Color of the MetroLine
     */
    public Color color;

    /**
     *
     * @param name
     * @param color
     */
    public MetroLine(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    /**
     *
     * @return the name of the MetroLine
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the color of the MetroLine
     */
    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return getName() + "\t" + getColor().getRed() + "," + getColor().getGreen() + "," + getColor().getBlue() + "\n";
    }    
}
