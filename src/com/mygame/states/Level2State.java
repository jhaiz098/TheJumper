package com.mygame.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.mygame.Coin;
import com.mygame.GameStateManager;
import com.mygame.Platform;
import com.mygame.Player;
import com.mygame.Sound;
import com.mygame.TileLoader;

public class Level2State extends BaseLevelState{

	private final GameStateManager gsm;
    private Player player;
    private BufferedImage[] tileset;
    
    private List<Platform> platforms;
    
    private int goalX=53;
    private int goalY=-3;
    
    public Level2State(GameStateManager gsm) {
    	this.gsm = gsm;
    	
    	platforms = new ArrayList<>();
    	
    	BufferedImage platformSheet = null;

    	try {
    	    platformSheet = ImageIO.read(
    	        getClass().getResource("/resources/sprites/platforms.png")
    	    );
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}

    	if (platformSheet == null) {
    	    throw new RuntimeException("platforms.png not found!");
    	}

    	BufferedImage plat16 = platformSheet.getSubimage(0, 0, 16, 8);
    	BufferedImage plat32 = platformSheet.getSubimage(16, 0, 32, 8);

    	int SCALE = 3;

    	// ground platform
    	platforms.add(
    	    new Platform(
    	        39 * 16 * SCALE,
    	        -1 * 16 * SCALE,
    	        32 * SCALE,
    	        8 * SCALE,
    	        plat32
    	    )
    	);

    	// mid-air platform
    	platforms.add(
    	    new Platform(
    	        43 * 16 * SCALE,
    	        -1 * 16 * SCALE,
    	        32 * SCALE,
    	        8 * SCALE,
    	        plat32
    	    )
    	);


//    	music = new Sound("/resources/music/8-bit-game-158815.wav");
    	
    	tileset = TileLoader.loadTiles("/resources/sprites/world_tileset.png", 16, 16);
    	
    	List<int[]> coinPositions = new ArrayList<>();
        coinPositions.add(new int[]{14 * 16 * 3, 4 * 16 * 3});
        coinPositions.add(new int[]{31 * 16 * 3, 4 * 16 * 3});
        coinPositions.add(new int[]{21 * 16 * 3, 10 * 16 * 3});

        setGoal(goalX * 16 * 3, goalY * 16 * 3, "/resources/sprites/goal.png");
        
        // Add coins to the level
        addCoins(coinPositions, "/resources/sprites/coin.png");
    	
    	// Initialize tile map (use inherited map)
        map = new java.util.ArrayList<>();
        if (tileset != null && tileset.length > 0) {
            // Ground at y=10
            for (int i = 0; i <= 12; i++) {
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
            
            for(int i=32;i<35;i++) {
            	addTile(i,9,0,1,true);
            }
            
            addTile(38,6, 0,1,true);
            addTile(39,6, 0,1,true);
            
            addTile(35,3,0,1,true);

            addTile(32,1,0,1,true);
            
            addTile(31,5,0,1,true);
            
            addTile(37,1,0,1,true);
            
            addTile(36,-1,0,1,true);
            
            for(int x=48; x<55;x++) {
            	addTile(x,-2,0,1,true);
            }
            
            //BG
//            for(int x=-15; x<50;x++) {
//            	for(int y=13;y<25;y++) {
//            		addTile(x,y,240,0,false);
//            	}
//            }
//            
//            for(int x=-15; x<50;x++) {
//            	addTile(x,12,224,0,false);
//            }
//            
//            for(int x=-15; x<50;x++) {
//            	for(int y=5;y<12;y++) {
//            		addTile(x,y,208,0,false);
//            	}
//            }
//            
//            for(int x=-15; x<50;x++) {
//            	addTile(x,4,192 ,0,false);
//            }
//            
//            for(int x=-15; x<50;x++) {
//            	for(int y=-5;y<4;y++) {
//            		addTile(x,y,176,0,false);
//            	}
//            }
            
        }

        // Create player
        player = new Player(3 * 16 *3, 6 * 16 *3, "/resources/sprites/knight.png");
    }
    
    @Override
    public void update(double dt) {
    	super.update(dt);
    	
        if (!paused && !levelFinished && !playerDead) {
        	int gained = player.update(dt, coins, map, coinSound);
        	collectedCoins += gained;


        	for (Platform p : platforms) {
        	    player.collideWithPlatform(p);
        	}


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
        
        if (goal != null) {
            goal.drawAt(g, camX, camY);
        }

        for (Platform p : platforms) {
            p.draw(g, camX, camY);
        }
        
        // Draw the player
        player.drawAt(g, camX, camY);
        
        renderUI(g);
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
    	stopMusic();
    	gsm.setState(GameStateManager.LEVEL_2);
    }

    @Override
    protected void nextLevel() {
    	stopMusic();
    	gsm.setState(GameStateManager.LEVEL_3);
    }

    @Override
    protected void goToLevelSelect() {
    	stopMusic();
    	gsm.setState(GameStateManager.MAIN_MENU);
    }
}
