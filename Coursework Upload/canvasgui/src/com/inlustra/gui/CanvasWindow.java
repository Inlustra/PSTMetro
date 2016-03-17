/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui;

import com.inlustra.gui.containers.CanvasContainer;
import com.inlustra.gui.elements.CanvasElement;
import com.inlustra.gui.interfaces.CanvasDroppable;
import com.inlustra.gui.mouse.CanvasMouseDragEvent;
import com.inlustra.gui.mouse.CanvasMouseEvent;
import com.inlustra.gui.mouse.CanvasMouseReleaseEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Thomas
 */
public class CanvasWindow extends CanvasContainer {

    private long startTime = System.nanoTime();
    private List<CanvasElement> dirtyElements;
    private List<CanvasElement> dirtyQueue;
    private static Object dirtyLock;
    private CanvasFrame frame;
    private DoubleBufferedCanvas canvas;
    private CanvasElement focussedElement;
    private Thread repaintThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    if (canvas != null) {
                        if (!isHidden()) {
                            repaint();
                        }
                    }
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CanvasWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    });

    public CanvasWindow(int width, int height) {
        super(0, 0, width, height);
        dirtyLock = new Object();
        dirtyElements = new LinkedList<>();
        dirtyQueue = new LinkedList<>();
        this.canvas = new DoubleBufferedCanvas(true) {
            @Override
            public void paintBuffer(Graphics bufferGraphics) {
                float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
                startTime = System.nanoTime();
                synchronized (dirtyLock) {
                    dirtyElements.addAll(dirtyQueue);
                    dirtyQueue.clear();

                    Iterator<CanvasElement> i = dirtyElements.iterator();
                    while (i.hasNext()) {
                        CanvasElement element = i.next();
                        try {
                            if (!element.isHidden()) {
                                Rectangle2D elementBounds = element.getCanvasRectangle();
                                Graphics2D containedGraphics = (Graphics2D) bufferGraphics.create((int) elementBounds.getX(), (int) elementBounds.getY(),
                                        (int) elementBounds.getWidth(), (int) elementBounds.getHeight());
                                element.draw(containedGraphics, deltaTime);
                            }
                            i.remove();
                        } catch (Exception e) {
                            System.out.println("Element likely nulled!");
                        }
                    }
                }

            }
        };
        canvas.setDropTarget(new DropTarget() {
            @Override
            public synchronized void dragEnter(DropTargetDragEvent dtde) {
                CanvasElement cc = getElementAt(dtde.getLocation());
                if (cc != null && cc instanceof CanvasDroppable) {
                    CanvasDroppable cd = (CanvasDroppable) cc;
                    if (cd.acceptingDrops()) {
                        dtde.acceptDrag(dtde.getDropAction());
                    }
                } else {
                    dtde.rejectDrag();
                }
            }

            @Override
            public synchronized void dragOver(DropTargetDragEvent dtde) {
                CanvasElement cc = getElementAt(dtde.getLocation());
                if (cc != null && cc instanceof CanvasDroppable) {
                    CanvasDroppable cd = (CanvasDroppable) cc;
                    if (cd.acceptingDrops()) {
                        dtde.acceptDrag(dtde.getDropAction());
                    }
                } else {
                    dtde.rejectDrag();
                }
            }

            @Override
            public synchronized void dropActionChanged(DropTargetDragEvent dtde) {
                CanvasElement cc = getElementAt(dtde.getLocation());
                if (cc != null && cc instanceof CanvasDroppable) {
                    CanvasDroppable cd = (CanvasDroppable) cc;
                    if (cd.acceptingDrops()) {
                        dtde.acceptDrag(dtde.getDropAction());
                    }
                } else {
                    dtde.rejectDrag();
                }
            }

            @Override
            public synchronized void drop(DropTargetDropEvent dtde) {
                CanvasElement cc = getElementAt(dtde.getLocation());
                if (cc != null && cc instanceof CanvasDroppable) {
                    CanvasDroppable cd = (CanvasDroppable) cc;
                    if (cd.acceptingDrops()) {
                        try {
                            dtde.acceptDrop(DnDConstants.ACTION_COPY);
                            List<File> droppedFiles = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                            for (File file : droppedFiles) {
                                cd.onDrop(dtde, file);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                }
            }
        });
        frame = new CanvasFrame();

        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.add(canvas);

        canvas.setBounds(0, 0, width, height);
        canvas.setBackground(Color.white);
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePressed(new CanvasMouseEvent(e, 0, 0));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                mouseClicked(new CanvasMouseEvent(e, 0, 0));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseReleased(new CanvasMouseReleaseEvent(e));
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e); //To change body of generated methods, choose Tools | Templates.
            }
        });
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (initialPress != null) {
                    mouseDragged(new CanvasMouseDragEvent(e, initialPress, x, y, draggedElementInitialx, draggedElementInitialy));
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseMoved(new CanvasMouseEvent(e, 0, 0)); //To change body of generated methods, choose Tools | Templates.
            }
        });

        canvas.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (focussedElement != null) {
                    focussedElement.onKeyType(e);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (focussedElement != null) {
                    focussedElement.onKeyPress(e);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (focussedElement != null) {
                    focussedElement.onKeyRelease(e);
                }
            }
        });
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e); //To change body of generated methods, choose Tools | Templates.
                System.out.println("Opened");
                repaintAll();
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                super.windowDeiconified(e); //To change body of generated methods, choose Tools | Templates.
                System.out.println("deicon");
            }

            @Override
            public void windowStateChanged(WindowEvent e) {
                super.windowStateChanged(e); //To change body of generated methods, choose Tools | Templates.
                System.out.println("stateChange");
            }
        });
        repaintThread.start();
    }

    @Override
    public void requestKeyFocus(CanvasElement e) {
        if (focussedElement != null) {
            focussedElement.setFocus(false);
        }
        focussedElement = e;
        focussedElement.setFocus(true);
    }

    public void markDirty(CanvasElement... elements) {
        synchronized (dirtyLock) {
            dirtyQueue.addAll(Arrays.asList(elements));
            canvas.repaint();
        }
    }

    public DoubleBufferedCanvas getCanvas() {
        return canvas;
    }

    public JFrame getFrame() {
        return frame;
    }

    @Override
    public void repaintElement(CanvasElement... element) {
        markDirty(element);
    }

    @Override
    public void repaint() {
        repaintElement(this);
    }

    @Override
    public void repaintAll() {
        markDirty(elements.toArray(new CanvasElement[elements.size()]));
    }
}
