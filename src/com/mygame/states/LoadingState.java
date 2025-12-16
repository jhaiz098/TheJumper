package com.mygame.states;

import com.mygame.*;
import java.awt.*;

public class LoadingState implements GameState {
    private GameStateManager gsm;

    public LoadingState(GameStateManager gsm) {
        this.gsm = gsm;
    }

    @Override
    public void update(double dt) {
        // Nothing to update, just waiting for nextLevel to be ready
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 900, 600);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Loading...", 350, 300);
    }

    @Override
    public void keyPressed(int key) {}
    @Override
    public void keyReleased(int key) {}
    @Override
    public void mouseMoved(int x, int y) {}
    @Override
    public void mousePressed(int x, int y) {}
}
