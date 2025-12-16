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
    private int goalX=29;
    private int goalY=10;
    
    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        
        music = new Sound("/resources/music/8-bit-game-158815.wav");
        
        killY = 1500;
        
        // Load tileset (example: 16x16 tiles)
        tileset = TileLoader.loadTiles("/resources/sprites/world_tileset.png", 16, 16);
        
        // Initialize coin
        // Define coin positions (x, y)
        List<int[]> coinPositions = new ArrayList<>();
        coinPositions.add(new int[]{16 *16 * 3, 9 *16 * 3});
        coinPositions.add(new int[]{8 *16 * 3, 8 *16 * 3});
        coinPositions.add(new int[]{23 *16 * 3, 8 *16 * 3});
        
        setGoal(goalX * 16 * 3, goalY * 16 * 3, "/resources/sprites/goal.png");

        // Add coins to the level
        addCoins(coinPositions, "/resources/sprites/coin.png");
        
        // Initialize tile map (use inherited map)
        map = new java.util.ArrayList<>();
        if (tileset != null && tileset.length > 0) {
            // Ground at y=10
            for (int i = -2; i <= 8; i++) {
                addTile(i, 10, 0, 1, true); // inherited method
            }
            
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
            
            
            //BG
            for(int x=-15; x<40;x++) {
            	for(int y=13;y<25;y++) {
            		addTile(x,y,240,0,false);
            	}
            }
            
            for(int x=-15; x<40;x++) {
            	addTile(x,12,224,0,false);
            }
            
            for(int x=-15; x<40;x++) {
            	for(int y=5;y<12;y++) {
            		addTile(x,y,208,0,false);
            	}
            }
            
            for(int x=-15; x<40;x++) {
            	addTile(x,4,192 ,0,false);
            }
            
            for(int x=-15; x<40;x++) {
            	for(int y=-5;y<4;y++) {
            		addTile(x,y,176,0,false);
            	}
            }
        }

        // Create player
        player = new Player(3 * 16 * 3, 6 * 16 * 3, "/resources/sprites/knight.png");
    }

    @Override
    public void update(double dt) {
    	super.update(dt);
    	
        if (!paused && !levelFinished && !playerDead) {
            player.update(dt, coins, map, coinSound);

            if (goal != null && player.getBounds().intersects(goal.getBounds())) {
                finalizeLevel();
            }
        }

        for (Coin coin : coins) {
            coin.update();
        }

        if (!playerDead && player.getY() > killY) {
            onPlayerDeath();
        }
    }


    protected void playerDied() {
        // Option 1: Restart level immediately
        restartLevel();

        // Option 2: Show “death” animation/UI before restarting
        // levelFinished = true; // optionally block player input
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
        String coinCountText = "Coins Remaining: " + coins.size(); // Get the coin count
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
        

        if (showFinishUI && playerDead) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, 900, 600);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 26));
            g.drawString("YOU DIED", 380, 200);

            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString(
                "ESC - Level Select   |   R - Restart Level",
                300, 500
            );
        }

    }

    @Override
    public void keyPressed(int key) {
        // Let the base handle level navigation first
        super.keyPressed(key);

        // Only pass input to the player if not paused or level not finished
        if (!paused && !levelFinished && !playerDead) {
            player.keyPressed(key);
        }
    }



    @Override
    public void keyReleased(int key) {
        player.keyReleased(key);
    }
    
    @Override
    protected void restartLevel() {
        stopMusic();
        gsm.setState(GameStateManager.LEVEL_1);
    }

    @Override
    protected void goToLevelSelect() {
        stopMusic();
        gsm.setState(GameStateManager.MAIN_MENU);
    }

    @Override
    protected void nextLevel() {
        stopMusic();
        gsm.setState(GameStateManager.LEVEL_2);
    }



}
