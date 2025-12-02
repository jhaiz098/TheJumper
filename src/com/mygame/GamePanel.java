package com.mygame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements Runnable, MouseMotionListener, MouseListener {
    public static final int WIDTH = 900, HEIGHT = 600;
    public final GameStateManager gsm;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        gsm = new GameStateManager();
        gsm.setState(GameStateManager.TITLE);

        // Key listener
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                GameState state = gsm.getState();
                if (state != null) state.keyPressed(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                GameState state = gsm.getState();
                if (state != null) state.keyReleased(e.getKeyCode());
            }
        });

        // Mouse listeners
        addMouseMotionListener(this);
        addMouseListener(this);

        new Thread(this).start();
    }

    @Override
    public void run() {
        final double dt = 1.0 / 60.0;
        while (true) {
            GameState state = gsm.getState();
            if (state != null) state.update(dt);
            repaint();
            try { Thread.sleep(16); } catch (InterruptedException ignored) {}
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        GameState state = gsm.getState();
        if (state != null) state.render(g);
    }

    // MouseMotionListener
    @Override
    public void mouseMoved(MouseEvent e) {
        GameState state = gsm.getState();
        if (state != null) state.mouseMoved(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        GameState state = gsm.getState();
        if (state != null) state.mouseMoved(e.getX(), e.getY());
    }

    // MouseListener
    @Override
    public void mousePressed(MouseEvent e) {
        GameState state = gsm.getState();
        if (state != null) state.mousePressed(e.getX(), e.getY());
    }

    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
