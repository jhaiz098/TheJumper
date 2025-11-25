package com.mygame.states;

import com.mygame.GameState;
import com.mygame.GameStateManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class MainMenuState implements GameState {

    private final GameStateManager gsm;

    public MainMenuState(GameStateManager gsm) {
        this.gsm = gsm;
    }

    @Override
    public void update(double dt) {
        // empty for now
    }

    @Override
    public void render(Graphics g) {
        // empty for now
    	g.setColor(Color.DARK_GRAY);
        g.fillRect(0,0,900,600);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 24));
        g.drawString("Press number 1-10 to select corresponding level.", 180, 200);
    }

    @Override
    public void keyPressed(int key) {
    	if (key == KeyEvent.VK_1) gsm.setState(GameStateManager.LEVEL_1);
        if (key == KeyEvent.VK_2) gsm.setState(GameStateManager.LEVEL_2);
    }

    @Override
    public void keyReleased(int key) {
        // empty for now
    }
}
