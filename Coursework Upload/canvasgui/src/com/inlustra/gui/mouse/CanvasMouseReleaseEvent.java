/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.mouse;

import com.inlustra.gui.elements.CanvasElement;
import java.awt.event.MouseEvent;

/**
 *
 * @author Thomas
 */
public class CanvasMouseReleaseEvent extends CanvasMouseEvent {

    public CanvasMouseReleaseEvent(MouseEvent original) {
        super(original, -1, -1);
    }
}
