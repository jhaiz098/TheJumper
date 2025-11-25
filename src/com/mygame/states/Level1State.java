package com.mygame.states;

import com.mygame.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Level1State extends BaseLevelState {

    private final GameStateManager gsm;
    private Player player;
    private BufferedImage[] tileset;

    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        
        // Load tileset (example: 16x16 tiles)
        tileset = TileLoader.loadTiles("/resources/sprites/world_tileset.png", 16, 16);

        // Initialize tile map (use inherited map)
        map = new java.util.ArrayList<>();
        if (tileset != null && tileset.length > 0) {
            // Ground at y=10
            for (int i = -3; i <= 8; i++) {
                addTile(i, 10, 0, 1, true); // inherited method
            }
            
            for(int x = 0; x < 10; x++) {
            	for(int y = 0; y < 13; y++) {
            		addTile(x, y, 160, -10, false);
            	}
            }

            // Vertical wall at x=5
            addTile(5, 9, 0, 1, true);
            addTile(5, 8, 1, 1, true);
        }

        // Create player
        player = new Player(100, 100, "/resources/sprites/knight.png");
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
