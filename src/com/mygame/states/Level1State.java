package com.mygame.states;

import com.mygame.GameState;
import com.mygame.GameStateManager;

import java.awt.Graphics;

public class Level1State implements GameState {

    private final GameStateManager gsm;

    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        // initialize level-specific objects here (player, map, etc.)
    }

    @Override
    public void update(double dt) {
        // update player, tiles, enemies, etc.
    }

    @Override
    public void render(Graphics g) {
        // draw background, tiles, player, enemies, etc.
    }

    @Override
    public void keyPressed(int key) {
        // handle player input
    }

    @Override
    public void keyReleased(int key) {
        // handle player input release
    }
}
