package com.mygame.states;

import com.mygame.GameState;
import com.mygame.GameStateManager;
import com.mygame.Player;
import com.mygame.Tile;

import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;

public class Level1State implements GameState {

    private final GameStateManager gsm;
    
    private Player player;
    private List<Tile> map = new ArrayList<>();

    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        // initialize level-specific objects here (player, map, etc.)
        
        player = new Player(100, 50, "/resources/sprites/knight.png");
        
        
    }

    @Override
    public void update(double dt) {
        // update player, tiles, enemies, etc.
    	
    	player.update(dt, map);
    }

    @Override
    public void render(Graphics g) {
        // draw background, tiles, player, enemies, etc.
    	
    	player.drawAt(g, 0, 0);
    }

    @Override
    public void keyPressed(int key) {
        // handle player input
    	
    	player.keyPressed(key);
    }

    @Override
    public void keyReleased(int key) {
        // handle player input release
    	
    	player.keyReleased(key);
    }
}
