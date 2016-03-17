/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.elements.heavy;

import com.inlustra.gui.elements.CanvasElement;
import com.inlustra.gui.utils.BlendComposite;
import com.inlustra.gui.utils.CanvasImageUtils;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;

/**
 *
 * @author Thomas
 */
public class CanvasImage extends CanvasElement {

    Image img;
    File image;

    public CanvasImage(File image, int x, int y) {
        super(x, y, CanvasImageUtils.getImage(image).getWidth(), CanvasImageUtils.getImage(image).getHeight());
        this.image = image;
        this.img = CanvasImageUtils.getImage(image);
    }

    @Override
    public void draw(Graphics2D g2d, float delta) {
        g2d.setComposite(BlendComposite.Normal);
        g2d.drawImage(img, 0, 0, width, height, null);
    }

    public Image getImg() {
        return img;
    }

    public File getImage() {
        return image;
    }
}
