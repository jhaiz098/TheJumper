package com.mygame.states;

import com.mygame.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

public class Level1State extends BaseLevelState {

    private final GameStateManager gsm;
    private Player player;
    private BufferedImage[] tileset;
    private Coin coin;  // Coin should be a class-level variable

    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;

        // Load tileset (example: 16x16 tiles)
        tileset = TileLoader.loadTiles("/resources/sprites/world_tileset.png", 16, 16);
        
        // Initialize coin
        // Define coin positions (x, y)
        List<int[]> coinPositions = new ArrayList<>();
        coinPositions.add(new int[]{100, 300});
        coinPositions.add(new int[]{200, 350});
        coinPositions.add(new int[]{300, 400});
        
        setGoal(500, 400, "/resources/sprites/goal.png");

        // Add coins to the level
        addCoins(coinPositions, "/resources/sprites/coin.png");
        
        // Initialize tile map (use inherited map)
        map = new java.util.ArrayList<>();
        if (tileset != null && tileset.length > 0) {
            // Ground at y=10
            for (int i = -2; i <= 8; i++) {
                addTile(i, 10, 0, 1, true); // inherited method
            }
            
            addTile(0,7,0,1,true);
            
            for (int x = -2; x < 9; x++) {
                for (int y = 11; y < 13; y++) {
                    addTile(x, y, 16, 1, true);
                }
            }
            
            for (int x = 9; x < 15; x++) {
                for (int y = 12; y < 13; y++) {
                    addTile(x, y, 0, 1, true);
                }
            }
            
            for (int x = 8; x < 15; x++) {
                for (int y = 13; y < 14; y++) {
                    addTile(x, y, 16, 1, true);
                }
            }
            
            for (int x = 18; x < 23; x++) {
                for (int y = 12; y < 13; y++) {
                    addTile(x, y, 0, 1, true);
                }
            }
            
            for (int x = 23; x < 32; x++) {
                for (int y = 11; y < 12; y++) {
                    addTile(x, y, 0, 1, true);
                }
            }
            
            for (int x = 18; x < 24; x++) {
                for (int y = 13; y < 14; y++) {
                    addTile(x, y, 16, 1, true);
                }
            }
            
            for (int x = 23; x < 32; x++) {
                for (int y = 12; y < 13; y++) {
                    addTile(x, y, 16, 1, true);
                }
            }
        }

        // Create player
        player = new Player(100, 100, "/resources/sprites/knight.png");
    }

    @Override
    public void update(double dt) {

        if (!levelFinished) {
            player.update(dt, coins, map);

            // Check goal collision
            if (goal != null && player.getBounds().intersects(goal.getBounds())) {
                finalizeLevel();
//                player.disableControl(); // new method (below)
            }
        }

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
        
        renderTiles(g, camX, camY);

        for (Coin coin : coins) {
            coin.drawAt(g, camX, camY);
        }
        
        // Draw the number of coins
        g.setColor(Color.WHITE); // Set text color to white (you can change this)
        g.setFont(new Font("Arial", Font.BOLD, 20)); // Set font size and style
        String coinCountText = "Coins: " + coins.size(); // Get the coin count
        g.drawString(coinCountText, 10, 30); // Draw the coin count at the top-left corner
        
        if (goal != null) {
            goal.drawAt(g, camX, camY);
        }

        if (levelFinished) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 28));
            g.drawString("STARS: " + starsEarned + " / 3", 380, 200);
        }

        
        // Draw the player
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
    	gsm.setState(GameStateManager.LEVEL_1);
    }

    @Override
    protected void nextLevel() {
    	gsm.setState(GameStateManager.LEVEL_2);
    }

    @Override
    protected void goToLevelSelect() {
    	gsm.setState(GameStateManager.MAIN_MENU);
    }


}
