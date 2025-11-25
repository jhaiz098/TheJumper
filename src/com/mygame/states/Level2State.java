package com.mygame.states;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.mygame.GameStateManager;
import com.mygame.Player;
import com.mygame.TileLoader;

public class Level2State extends BaseLevelState{

	private final GameStateManager gsm;
    private Player player;
    private BufferedImage[] tileset;
    
    public Level2State(GameStateManager gsm) {
    	this.gsm = gsm;
    	
    	tileset = TileLoader.loadTiles("/resources/sprites/world_tileset.png", 16, 16);
    	
    	// Initialize tile map (use inherited map)
        map = new java.util.ArrayList<>();
        if (tileset != null && tileset.length > 0) {
            // Ground at y=10
            for (int i = -3; i <= 8; i++) {
                addTile(i, 10, 0, 1, true); // inherited method
            }

            // Vertical wall at x=5
            addTile(5, 9, 0, 1, true);
            addTile(5, 8, 0, 1, true);
            addTile(7, 8, 0, 1,true);
            addTile(7, 8, 0, 1,true);
            addTile(7, 8, 0, 1,true);
        }

        // Create player
        player = new Player(50, 100, "/resources/sprites/knight.png");
    }
    
    @Override
    public void update(double dt) {
        player.update(dt, map);
    }

    @Override
    public void render(Graphics g) {
        int camX = player.getX() - 450;
        int camY = player.getY() - 300;

        renderTiles(g, camX, camY);   // inherited from BaseLevelState
        player.drawAt(g, camX, camY);
    }

    @Override
    public void keyPressed(int key) {
        player.keyPressed(key);
    }

    @Override
    public void keyReleased(int key) {
        player.keyReleased(key);
    }
}
