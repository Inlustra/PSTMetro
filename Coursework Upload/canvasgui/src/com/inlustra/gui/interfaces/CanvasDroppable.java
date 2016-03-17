/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.interfaces;

import java.awt.dnd.DropTargetDropEvent;
import java.io.File;

/**
 *
 * @author Thomas
 */
public interface CanvasDroppable {

    public boolean acceptingDrops();

    public void onDrop(DropTargetDropEvent e, File f);
}
