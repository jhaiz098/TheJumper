package com.mygame.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.mygame.Coin;
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
    	
    	List<int[]> coinPositions = new ArrayList<>();
        coinPositions.add(new int[]{100, 300});
        coinPositions.add(new int[]{200, 350});
        coinPositions.add(new int[]{300, 400});

        // Add coins to the level
        addCoins(coinPositions, "/resources/sprites/coin.png");
    	
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
            
            //BG
            for(int x=-15; x<50;x++) {
            	for(int y=13;y<25;y++) {
            		addTile(x,y,240,0,false);
            	}
            }
            
            for(int x=-15; x<50;x++) {
            	addTile(x,12,224,0,false);
            }
            
            for(int x=-15; x<50;x++) {
            	for(int y=5;y<12;y++) {
            		addTile(x,y,208,0,false);
            	}
            }
            
            for(int x=-15; x<50;x++) {
            	addTile(x,4,192 ,0,false);
            }
            
            for(int x=-15; x<50;x++) {
            	for(int y=-5;y<4;y++) {
            		addTile(x,y,176,0,false);
            	}
            }
            
        }

        // Create player
        player = new Player(3 * 16 *3, 6 * 16 *3, "/resources/sprites/knight.png");
    }
    
    @Override
    public void update(double dt) {
        player.update(dt, coins, map);  // Pass the coins list to the player

        // Render coins (they will be updated automatically)
        for (Coin coin : coins) {
            coin.update();
        }
    }


    @Override
    public void render(Graphics g) {
        int camX = player.getX() - 450;
        int camY = player.getY() - 300;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 900, 600);
        
        renderTiles(g, camX, camY);   // inherited from BaseLevelState
        
        for (Coin coin : coins) {
            coin.drawAt(g, camX, camY);
        }
        
        // Draw the number of coins
        g.setColor(Color.WHITE); // Set text color to white (you can change this)
        g.setFont(new Font("Arial", Font.BOLD, 20)); // Set font size and style
        String coinCountText = "Coins: " + coins.size(); // Get the coin count
        g.drawString(coinCountText, 10, 30); // Draw the coin count at the top-left corner
        
        player.drawAt(g, camX, camY);
    }

    @Override
    public void keyPressed(int key) {
    	super.keyPressed(key);
        player.keyPressed(key);
    }

    @Override
    public void keyReleased(int key) {
        player.keyReleased(key);
    }
    
    @Override
    protected void restartLevel() {
    	gsm.setState(GameStateManager.LEVEL_2);
    }

    @Override
    protected void nextLevel() {
    	gsm.setState(GameStateManager.LEVEL_3);
    }

    @Override
    protected void goToLevelSelect() {
    	gsm.setState(GameStateManager.MAIN_MENU);
    }
}
