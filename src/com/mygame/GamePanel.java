package com.mygame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements Runnable {
    private static final int WIDTH = 900, HEIGHT = 600;
    private final GameStateManager gsm;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        gsm = new GameStateManager();
        gsm.setState(GameStateManager.TITLE);

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
}
