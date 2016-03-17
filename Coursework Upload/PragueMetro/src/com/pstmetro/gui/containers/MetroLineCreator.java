/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pstmetro.gui.containers;

import com.inlustra.gui.containers.overlay.CanvasOverlay;
import com.inlustra.gui.elements.heavy.CanvasButton;
import com.inlustra.gui.elements.input.IntegerInputButton;
import com.inlustra.gui.elements.input.StringInputButton;
import com.inlustra.gui.utils.BlendComposite;
import com.pstmetro.metro.node.MetroLine;
import java.awt.Color;

/**
 *
 * @author Thomas
 */
public abstract class MetroLineCreator extends CanvasOverlay {

    int red = 0;
    int green = 180;
    int blue = 255;
    String lineName = "Line Name";

    /**
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public MetroLineCreator(int x, int y, int width, int height) {
        super(x, y, width, height);
        int bigBoxWidth = width / 2;
        int textBoxWidth = 120;
        int textBoxHeight = 50;
        int outerWidth = width / 2;
        int btnx = outerWidth / 3;
        final StringInputButton inputString = new StringInputButton("Line Name", new Color(0, 0, 0, 0), width / 2 - bigBoxWidth / 2, (height / 2) - (textBoxHeight / 2) - textBoxHeight - 5, bigBoxWidth, textBoxHeight) {
            @Override
            public void onSetString(String string) {
                lineName = string;
            }
        };
        final CanvasButton btn = new CanvasButton("Create Line", new Color(red, green, blue), width / 2 - bigBoxWidth / 2, height - textBoxHeight, bigBoxWidth, textBoxHeight) {
            @Override
            public void onButtonPress(CanvasButton source) {
                createdLine(new MetroLine(inputString.getText(), new Color(red, green, blue)));
            }
        };
        addElement(btn);
        addElement(inputString);
        IntegerInputButton inputRed = new IntegerInputButton("" + red, new Color(red, 0, 0), btnx - textBoxWidth / 2 + outerWidth / 3, (height / 2) - (textBoxHeight / 2), textBoxWidth, textBoxHeight) {
            @Override
            public void onSetInteger(int integer) {
                if (integer < 256) {
                    red = integer;
                } else {
                    text = "255";
                    red = 255;
                }
                setBackgroundColor(new Color(red, 0, 0));
                btn.setBackgroundColor(new Color(red, green, blue));

            }
        };
        addElement(inputRed);
        IntegerInputButton inputGreen = new IntegerInputButton("" + green, new Color(0, green, 0), 
                ((btnx * 2)) - textBoxWidth / 2 + outerWidth / 3, (height / 2) - (textBoxHeight / 2), textBoxWidth, textBoxHeight) {
            @Override
            public void onSetInteger(int integer) {
                if (integer < 256) {
                    green = integer;
                } else {
                    text = "255";
                    green = 255;
                }
                setBackgroundColor(new Color(0, green, 0));
                btn.setBackgroundColor(new Color(red, green, blue));
            }
        };
        addElement(inputGreen);
        IntegerInputButton inputBlue = new IntegerInputButton("" + blue, new Color(0, 0, blue), (btnx * 3) - textBoxWidth / 2 + outerWidth / 3, (height / 2) - (textBoxHeight / 2), textBoxWidth, textBoxHeight) {
            @Override
            public void onSetInteger(int integer) {
                if (integer < 256) {
                    blue = integer;
                } else {
                    text = "255";
                    blue = 255;
                }
                setBackgroundColor(new Color(0, 0, blue));
                btn.setBackgroundColor(new Color(red, green, blue));
            }
        };
        addElement(inputBlue);
    }

    /**
     *
     * @return
     */
    @Override
    public boolean clickThrough() {
        return false; //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param line
     */
    public abstract void createdLine(MetroLine line);
}
