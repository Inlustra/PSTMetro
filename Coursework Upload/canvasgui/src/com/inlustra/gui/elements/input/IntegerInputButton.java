/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inlustra.gui.elements.input;

import com.inlustra.gui.elements.heavy.CanvasButton;
import com.inlustra.gui.mouse.MouseUtils;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

/**
 *
 * @author Thomas
 */
public abstract class IntegerInputButton extends CanvasButton {

    float timePassed = 0;
    char caret = '|';
    private String previousText;

    public IntegerInputButton(String label, Color color, int x, int y) {
        super(label, color, x, y);
    }

    public IntegerInputButton(String label, Color color, Font font, int x, int y) {
        super(label, color, font, x, y);
    }

    public IntegerInputButton(String label, Color color, int x, int y, int width, int height) {
        super(label, color, x, y, width, height);
    }

    public IntegerInputButton(String label, Color color, Font font, int x, int y, int width, int height) {
        super(label, color, font, x, y, width, height);
        this.mouseOverCursor = new Cursor(Cursor.TEXT_CURSOR);
    }

    @Override
    public void onButtonPress(CanvasButton source) {
        previousText = getText();
        this.text = "|";
        requestKeyFocus(this);
        repaint();
    }

    @Override
    public void draw(Graphics2D g2d, float delta) {
        timePassed += delta;
        if (isFocussed() && (int) timePassed % 2 == 0) {
            if (!text.endsWith("|")) {
                text += '|';
            }
        } else {
            if (!isFocussed() && previousText != null && timedMessage == null && getText().isEmpty()) {
                this.text = previousText;
                previousText = null;
            } else if (!isFocussed() && (previousText == null ? (getText()) != null : !previousText.equals(getText())) && !getText().isEmpty()) {
                onSetInteger(Integer.parseInt(getText()));
                previousText = getText();
                text = getText();
            } else if (timedMessage != null) {
                if (timePassed - startTimedMessage > timedMessageTime) {
                    startTimedMessage = -1;
                    timedMessageTime = -1;
                    text = timedMessage;
                    timedMessage = null;
                }
            } else if (text.endsWith("|")) {
                text = text.replace("|", "");
            }
        }
        super.draw(g2d, delta); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onKeyType(KeyEvent e) {
        super.onKeyType(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onKeyPress(KeyEvent e) {
        super.onKeyPress(e); //To change body of generated methods, choose Tools | Templates.
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            onSetInteger(Integer.parseInt(getText()));
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (getText().length() > 0) {
                this.text = getText().substring(0, getText().length() - 1);
            }
        } else if (Character.isDigit(e.getKeyChar())) {
            this.text = getText() + e.getKeyChar();
        }
        repaint();
    }

    @Override
    public void onKeyRelease(KeyEvent e) {
        super.onKeyRelease(e); //To change body of generated methods, choose Tools | Templates.
    }

    public String getText() {
        return text.replaceAll(Pattern.quote("|"), "");
    }
    private float startTimedMessage;
    private int timedMessageTime;
    private String timedMessage;

    public void setTimedMessage(String text, int time, String defaultTo) {
        this.text = text;
        requestKeyFocus(getCanvasFrame());
        timedMessageTime = time;
        timedMessage = defaultTo;
        startTimedMessage = timePassed;
    }

    public abstract void onSetInteger(int integer);

    @Override
    public Cursor getMouseOverCursor() {
        return MouseUtils.TEXT_CURSOR; //To change body of generated methods, choose Tools | Templates.
    }
}
