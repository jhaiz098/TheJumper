package com.mygame.states;

import com.mygame.GameState;
import com.mygame.GameStateManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class TitleState implements GameState {
    private final GameStateManager gsm;
    public TitleState(GameStateManager gsm) { this.gsm = gsm; }

    @Override public void update(double dt) {}

    @Override
    public void render(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,0,900,600);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 48));
        g.drawString("THE JUMPER", 280, 200);
        g.setFont(new Font("SansSerif", Font.PLAIN, 24));
        g.drawString("Press ENTER to continue", 320, 260);
        g.drawString("Press S for Settings, T for Tutorial", 280, 300);
    }

    @Override
    public void keyPressed(int key) {
        
    }

    @Override public void keyReleased(int key) {}
}
