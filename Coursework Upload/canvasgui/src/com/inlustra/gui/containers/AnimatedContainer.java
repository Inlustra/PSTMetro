/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.containers;

import com.inlustra.gui.containers.CanvasContainer;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author Thomas
 */
public class AnimatedContainer extends CanvasContainer {

    public AnimatedContainer(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
    long startTime;

    public void moveTo(final int targetX, final int targetY, final int time) {
        moveTo(targetX, targetY, time, null);
    }

    public void moveTo(final int targetX, final int targetY, final int time, final Runnable callBack) {
        startMove();
        Timer timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long duration = System.currentTimeMillis() - startTime;
                float progress = (float) duration / (float) time;
                if (progress > 1f) {
                    progress = 1f;
                    ((Timer) (e.getSource())).stop();
                    if (callBack != null) {
                        callBack.run();
                    }
                }
                setX(getInitialPosition().x + (int) Math.round((targetX - getInitialPosition().x) * progress));
                setY(getInitialPosition().y + (int) Math.round((targetY - getInitialPosition().y) * progress));
                System.out.println(getInitialPosition().y + (int) Math.round((targetY - getInitialPosition().y) * progress));
                repaintAll();
            }
        });
        startTime = System.currentTimeMillis();
        timer.start();
    }
}
