package com.mygame.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.mygame.GameStateManager;
import com.mygame.Player;
import com.mygame.TileLoader;

public class Level3State extends BaseLevelState{
	
	private final GameStateManager gsm;
    private Player player;
    private BufferedImage[] tileset;
    
    public Level3State(GameStateManager gsm) {
    	this.gsm = gsm;
    	
    	tileset = TileLoader.loadTiles("/resources/sprites/world_tileset.png", 16, 16);
    	
    	// Initialize tile map (use inherited map)
        map = new java.util.ArrayList<>();
        if (tileset != null && tileset.length > 0) {
            // Ground at y=10
            for (int i = -3; i <= 12; i++) {
                addTile(i, 10, 0, 1, true); // inherited method
            }
            
            for(int i=7; i<11; i++) {
            	addTile(i,7,0,1,true);
            }
            
            for(int i=16;i<18;i++) {
            	addTile(i,9,0,1,true);
            }
            
            for(int i=21;i<22;i++) {
            	addTile(i,11,0,1,true);
            }
            
            for(int i=27;i<31;i++) {
            	addTile(i,11,0,1,true);
            }
            
            for(int i=32;i<38;i++) {
            	addTile(i,9,0,1,true);
            }
            
        }
        
        // Create player
        player = new Player(50, 100, "/resources/sprites/knight.png");
    }
    
    @Override
    public void update(double dt) {
//        player.update(dt, map);
    }

    @Override
    public void render(Graphics g) {
        int camX = player.getX() - 450;
        int camY = player.getY() - 300;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 900, 600);
        
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