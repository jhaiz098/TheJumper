package com.mygame.states;

import com.mygame.GameState;
import com.mygame.GameStateManager;

import java.awt.Graphics;

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
    }

    @Override
    public void keyPressed(int key) {
        // empty for now
    }

    @Override
    public void keyReleased(int key) {
        // empty for now
    }
}
