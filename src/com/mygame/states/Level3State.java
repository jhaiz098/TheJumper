package com.mygame.states;

import static com.mygame.GameConstants.TILE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.mygame.Coin;
import com.mygame.GameStateManager;
import com.mygame.Platform;
import com.mygame.Player;
import com.mygame.Spike;
import com.mygame.TileLoader;

public class Level3State extends BaseLevelState{
	
	private final GameStateManager gsm;
    private Player player;
    private BufferedImage[] tileset;
    
    private List<Platform> platforms;
    
    private List<Spike> spikes;
    
    private int goalX=-33;
    private int goalY=-15;
    
    public Level3State(GameStateManager gsm) {
    	this.gsm = gsm;
    	
    	BufferedImage spikeSprite;
    	
    	setGoal(goalX * 16 * 3, goalY * 16 * 3, "/resources/sprites/goal.png");
    	
    	try {
    	    BufferedImage spikeSheet = ImageIO.read(
    	        getClass().getResource("/resources/sprites/spike.png")
    	    );

    	    spikeSprite = spikeSheet.getSubimage(0, 0, 16, 16);

    	} catch (Exception e) {
    	    e.printStackTrace();
    	    spikeSprite = null;
    	}

    	spikes = new ArrayList<>();

    	int SCALE = 3;

    	spikes.add(new Spike(
    	    5,
    	    -2,
    	    16,
    	    16,
    	    spikeSprite
    	));
    	
    	spikes.add(new Spike(
        	    12,
        	    -12,
        	    16,
        	    16,
        	    spikeSprite
        	));
    	
    	spikes.add(new Spike(
        	    -23,
        	    -15,
        	    16,
        	    16,
        	    spikeSprite
        	));
    	
    	spikes.add(new Spike(-3,-15,16,16,spikeSprite));
    	spikes.add(new Spike(-9,-15,16,16,spikeSprite));
    	spikes.add(new Spike(-15,-15,16,16,spikeSprite));
    	
    	platforms = new ArrayList<>();

    	BufferedImage platformSheet;
    	BufferedImage plat16;
    	BufferedImage plat32;
    	BufferedImage plat16x2;
    	BufferedImage plat32x2;

    	try {
    	    platformSheet = ImageIO.read(
    	        getClass().getResource("/resources/sprites/platforms.png")
    	    );

    	    plat16 = platformSheet.getSubimage(0, 48, 16, 16);
    	    plat32 = platformSheet.getSubimage(16, 48, 32, 16);
    	    
    	    plat16x2= platformSheet.getSubimage(0, 32, 16, 16);
    	    plat32x2 = platformSheet.getSubimage(16, 32, 32, 16);

    	} catch (Exception e) {
    	    e.printStackTrace();
    	    return;
    	}


    	if (platformSheet == null) {
    	    throw new RuntimeException("platforms.png not found!");
    	}

    		platforms.add(
    		    new Platform(
    		        1, 7, 
    		        1, 7,
    		        32, 16,
    		        plat32,
    		        0f
    		    )
    		);
    		
    		platforms.add(
        		    new Platform(
        		        7, 7, 
        		        7, 7,
        		        32, 
        		        16,
        		        plat32,
        		        0f
        		    )
        		);
    		
    		
    		
    		platforms.add(
        		    new Platform(
        		    	-2, 1,
        		        2, 1,
        		        32, 
        		        16,
        		        plat32,
        		        100f 
        		    )
        		);
    		
    		platforms.add(
        		    new Platform(
        		    	9, -13,
        		        9, -3,
        		        32, 
        		        16,
        		        plat32,
        		        100f 
        		    )
        		);
    		
    		platforms.add(
        		    new Platform(
        		    	-20, -14,
        		        1, -14,
        		        32, 
        		        16,
        		        plat32x2,
        		        100f 
        		    )
        		);
    		
    		platforms.add(
        		    new Platform(
        		    	-3, -17,
        		        -3, -17,
        		        16, 
        		        16,
        		        plat16x2,
        		        0f 
        		    )
        		);
    		
    		platforms.add(
        		    new Platform(
        		    	-9, -17,
        		        -9, -17,
        		        16, 
        		        16,
        		        plat16x2,
        		        0f
        		    )
        		);
    		
    		platforms.add(
        		    new Platform(
        		    	-15, -17,
        		        -15, -17,
        		        16, 
        		        16,
        		        plat16x2,
        		        0f
        		    )
        		);
    		
    		
    		
    		
    		
    	List<int[]> coinPositions = new ArrayList<>();
    	coinPositions.add(new int[]{4 * 16 * 3, -2 * 16 * 3});
    	coinPositions.add(new int[]{4 * 16 * 3, 3 * 16 * 3});
    	coinPositions.add(new int[]{12 * 16 * 3, -8 * 16 * 3});
    	coinPositions.add(new int[]{-3 * 16 * 3, -18 * 16 * 3});
    	coinPositions.add(new int[]{-9 * 16 * 3, -18 * 16 * 3});
    	coinPositions.add(new int[]{-15 * 16 * 3, -18 * 16 * 3});
    	
    	// Add coins to the level
        addCoins(coinPositions, "/resources/sprites/coin.png"); 
    	
    	tileset = TileLoader.loadTiles("/resources/sprites/world_tileset.png", 16, 16);
    	
    	// Initialize tile map (use inherited map)
        map = new java.util.ArrayList<>();
        if (tileset != null && tileset.length > 0) {
            // Ground at y=10
            for (int i = 1; i <= 8; i++) {
                addTile(i, 10, 6, 1, true); // inherited method
            }
            
            for (int x = 1; x <= 8; x++) {
            	for(int y = 11; y<15; y++) {            		
            		addTile(x, y, 22, 1, true); // inherited method
            	}
            }
            
            addTile(4,4,6,1,true);
            addTile(5,4,6,1,true);
            
            addTile(3,-1,6,1,true);
            addTile(4,-1,6,1,true);
            addTile(5,-1,6,1,true);
            addTile(6,-1,6,1,true);
            
            addTile(12,-7, 23,1,true);
            addTile(15,-9, 23,1,true);
            
            addTile(12, -11, 23,1,true);
            addTile(13, -11, 23,1,true);
            
            addTile(5, -14, 4,1,true);
            addTile(6, -14, 4,1,true);
            
            addTile(-3, -14, 4, 1, true);
            
            addTile(-9, -14, 4, 1, true);
            
            addTile(-15, -14, 4, 1, true);
            
            for(int x=-25;x<-22;x++) {
            	addTile(x, -14, 4,1,true);
            }
            
            addTile(-26,-14,27,1,true);
            addTile(-27,-14,26,1,true);
            addTile(-28,-14,25,1,true);
            
            for(int x=-35; x<-28;x++) {
            	addTile(x,-14,4,1,true);
            }
          //BG
          for(int x=-10; x<20;x++) {
          	for(int y=5;y<15;y++) {
          		addTile(x,y,176,0,false);
          	}
          }
//          
          for(int x=-10; x<20;x++) {
          	addTile(x,4,160,0,false);
          }
//          
          for(int x=-10; x<20;x++) {
          	for(int y=16;y<18;y++) {
          		addTile(x,y,208,0,false);
          	}
          }
//          
          for(int x=-10; x<20;x++) {
          	addTile(x,15 ,192 ,0,false);
          }
          
          for(int x=-40;x<50;x++) {
        	  for(int y=-20;y<4;y++) {
        		  addTile(x,y,144,0,false);
        	  }
          }
            
        }
        
        // Create player
        player = new Player(4 * 16 * 3, 5 * 16 * 3, "/resources/sprites/knight.png");
//        player = new Player(-26 * 16 * 3, -16 * 16 * 3, "/resources/sprites/knight.png");
    }
    
    @Override
    public void update(double dt) {
    	super.update(dt);
    	
        if (!paused && !levelFinished && !playerDead) {
        	int gained = player.update(dt, coins, map, coinSound);
        	collectedCoins += gained;


        	for (Platform p : platforms) {
        		p.update(dt);
        	    player.collideWithPlatform(p);
        	}
        	
        	for (Spike s : spikes) {
        	    if (!playerDead && player.getBounds().intersects(s.getBounds())) {
        	        onPlayerDeath();
        	        break;
        	    }
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
        
        
        renderTiles(g, camX, camY);   // inherited from BaseLevelState
        
        for (Platform p : platforms) {
            p.draw(g, camX, camY);
        }
        
        for (Coin coin : coins) {
            coin.drawAt(g, camX, camY);
        }
        
        for (Spike s : spikes) {
            s.draw(g, camX, camY);
        }

        if (goal != null) {
            goal.drawAt(g, camX, camY);
        }
        
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
    	gsm.setState(GameStateManager.LEVEL_3);
    }

    @Override
    protected void nextLevel() {
    	stopMusic();
    	gsm.setState(GameStateManager.LEVEL_4);
    }

    @Override
    protected void goToLevelSelect() {
    	stopMusic();
    	gsm.setState(GameStateManager.MAIN_MENU);
    }
	
}