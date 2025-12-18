package com.mygame.states;

import static com.mygame.GameConstants.TILE;
import com.mygame.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Level1State extends BaseLevelState {

    // === CORE ===
    private final GameStateManager gsm;
    private Player player;

    // === WORLD ===
    private BufferedImage[] tileset;
    private List<Enemy> enemies;
    private List<Platform> platforms;
    private List<Spike> spikes;
    private List<ArrowTrap> arrowTraps;

    // === LEVEL DATA ===
    private int goalX = 32;
    private int goalY = -2;

    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;

        // Kill player if they fall too far
        killY = 1500;
        
        // =========================
        // LOAD TILESET
        // =========================
        tileset = TileLoader.loadTiles(
                "/resources/sprites/world_tileset.png",
                16, 16
        );
     // =========================
     // ARROW TRAP (SHOOTER)
     // =========================
        
     arrowTraps = new ArrayList<>();
        
     // Declare sprites
     BufferedImage trapSheet = null;
     BufferedImage arrowSprite = null;

     try {
         // Load trap sprite sheet (idle + shooting animation)
         trapSheet = ImageIO.read(
                 getClass().getResource("/resources/sprites/Shooter_Arrow_Trap.png")
         );

         // Load arrow projectile sprite
         arrowSprite = ImageIO.read(
                 getClass().getResource("/resources/sprites/arrow.png")
         );

     } catch (IOException e) {
         e.printStackTrace();
     }

	  // =========================
	  // ADD ARROW TRAPS (EASY)
	  // =========================
	  arrowTraps.add(new ArrowTrap(
	          50 * 16 * 3,
	          10 * 16 * 3,
	          ArrowTrap.Direction.LEFT,
	          15f * 16 * 3,
	          600f,
	          1.5f,
	          trapSheet,
	          16, 16,
	          3,
	          5, 5,
	          0, 5,
	          arrowSprite
	  ));
	
	  arrowTraps.add(new ArrowTrap(
	          31 * 16 * 3,
	          2 * 16 * 3,
	          ArrowTrap.Direction.LEFT,
	          17f * 16 * 3,
	          600f,
	          1.5f,
	          trapSheet,
	          16, 16,
	          3,
	          5, 5,
	          0, 5,
	          arrowSprite
	  ));




        // =========================
        // GOAL
        // =========================
        setGoal(
                goalX * 16 * 3,
                goalY * 16 * 3,
                "/resources/sprites/goal.png"
        );
        
        // =========================
        // SPIKES
        // =========================
        BufferedImage spikeSprite;
    	
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

    	spikes.add(new Spike(22,11,16,16,spikeSprite));
    	spikes.add(new Spike(39,0,16,16,spikeSprite));
    	spikes.add(new Spike(8,3,16,16,spikeSprite));
    	spikes.add(new Spike(23,3,16,16,spikeSprite));
        
        // =========================
        // PLATFORMS
        // =========================
        platforms = new ArrayList<>();

    	BufferedImage platformSheet;
    	BufferedImage plat16;
    	BufferedImage plat32;

    	try {
    	    platformSheet = ImageIO.read(
    	        getClass().getResource("/resources/sprites/platforms.png")
    	    );

    	    plat16 = platformSheet.getSubimage(0, 0, 16, 16);
    	    plat32 = platformSheet.getSubimage(16, 0, 32, 16);

    	} catch (Exception e) {
    	    e.printStackTrace();
    	    return;
    	}


    	if (platformSheet == null) {
    	    throw new RuntimeException("platforms.png not found!");
    	}
    	
    	platforms.add(
    		    new Platform(
    		        5,5, 
    		        5,5,
    		        16,
    		        16,
    		        plat16,
    		        0f
    		    )
    		);
    	
    	platforms.add(
    		    new Platform(
    		        1,7, 
    		        1,7,
    		        32,
    		        16,
    		        plat32,
    		        100f
    		    )
    		);
    	
    	platforms.add(
    		    new Platform(
    		        16,12, 
    		        20,12,
    		        32,
    		        16,
    		        plat32,
    		        100f
    		    )
    		);
    	
    	platforms.add(
    		    new Platform(
    		        27,12, 
    		        23,12,
    		        32,
    		        16,
    		        plat32,
    		        100f
    		    )
    		);
    	
    	platforms.add(
    		    new Platform(
    		        42,9, 
    		        42,1,
    		        32,
    		        16,
    		        plat32,
    		        100f
    		    )
    		);
    	
    	platforms.add(
    		    new Platform(
    		        31,6, 
    		        31,6,
    		        32,
    		        16,
    		        plat32,
    		        0f
    		    )
    		);
    	
    	platforms.add(
    		    new Platform(
    		        34,8, 
    		        34,8,
    		        32,
    		        16,
    		        plat32,
    		        0f
    		    )
    		);
        
        // =========================
        // COINS
        // =========================
        List<int[]> coinPositions = new ArrayList<>();
        coinPositions.add(new int[]{22 * 16 * 3, 9 * 16 * 3});
        coinPositions.add(new int[]{8 * 16 * 3, 9 * 16 * 3});
        coinPositions.add(new int[]{42 * 16 * 3, 5 * 16 * 3});

        addCoins(coinPositions, "/resources/sprites/coin.png");

        // =========================
        // TILE MAP
        // =========================
        map = new ArrayList<>();

        if (tileset != null) {

            // Main ground
            for (int x = -2; x <= 8; x++) {
                addTile(x, 10, 0, 1, true);
            }

            for (int x = -2; x < 9; x++) {
                for (int y = 11; y < 13; y++) {
                    addTile(x, y, 16, 1, true);
                }
            }
            
            for(int x=6;x<14;x++) {
            	addTile(x,4,0,1,true);
            }
            
            for(int x=14;x<=19;x++) {
            	addTile(x,3,0,1,true);
            }
            
            for(int x=20;x<=25;x++) {
            	addTile(x,4,0,1,true);
            }
            
            for(int x=26;x<=27;x++) {
            	addTile(x,5,0,1,true);
            }
            
            for(int x=6;x<=25;x++) {
            	addTile(x,5,16,1,true);
            }
            
            for(int x=14;x<=19;x++) {
            	addTile(x,4,16,1,true);
            }
            
            for(int x=12;x<=27;x++) {
            	addTile(x,6,16,1,true);
            }

            // Platforms (mid section)
            for (int x = 9; x < 15; x++) {
                addTile(x, 12, 0, 1, true);
            }

            for (int x = 8; x < 15; x++) {
                addTile(x, 13, 16, 1, true);
            }
            
            addTile(22,12,0,1,true);
            
            for (int x = 30; x < 35; x++) {
                addTile(x, 12, 0, 1, true);
            }
            
            for (int x = 30; x <= 35; x++) {
                addTile(x, 13, 16, 1, true);
            }
            
            for (int x = 35; x <= 45; x++) {
                addTile(x, 11, 0, 1, true);
            }
            
            for (int x = 35; x <= 45; x++) {
                addTile(x, 12, 16, 1, true);
            }
            
            for (int x = 36; x <= 39; x++) {
                addTile(x, 1, 0, 1, true);
            }
            
            for (int x = 31; x <= 35; x++) {
                addTile(x, -1, 0, 1, true);
            }
            
            for (int x = 31; x <= 35; x++) {
            	for(int y=0;y<=1;y++) {            		
            		addTile(x, y, 16, 1, true);
            	}
            }
            
            for (int x = 32; x <= 39; x++) {
                addTile(x, 2, 16, 1, true);
            }
            
            for (int x = 34; x <= 39; x++) {
                addTile(x, 3, 16, 1, true);
            }
        }

        // =========================
        // ENEMIES
        // =========================
        enemies = new ArrayList<>();

        try {
        	BufferedImage greenSlimeSheet = ImageIO.read(
        		    getClass().getResource("/resources/sprites/slime_green.png")
        		);
        	
        	BufferedImage purpleSlimeSheet = ImageIO.read(
        		    getClass().getResource("/resources/sprites/slime_purple.png")
        		);

        		Enemy slime = new Enemy(
        		    14* TILE, 11 * TILE,     // start
        		    9 * TILE, 11 * TILE,     // end
        		    16, 16,                 // sprite size
        		    3,                      // scale
        		    greenSlimeSheet,
        		    4, 7,                   // idle frames
        		    4, 7,                   // move frames
        		    Enemy.PatrolType.HORIZONTAL,
        		    80f,                    // speed
        		    2.5f,                    // idle seconds
        		    1 // sprite facing right
        		);
        		
        		Enemy slime2 = new Enemy(
            		    30* TILE, 11 * TILE,     // start
            		    34 * TILE, 11 * TILE,     // end
            		    16, 16,                 // sprite size
            		    3,                      // scale
            		    purpleSlimeSheet,
            		    4, 7,                   // idle frames
            		    4, 7,                   // move frames
            		    Enemy.PatrolType.HORIZONTAL,
            		    80f,                    // speed
            		    2.5f,                    // idle seconds
            		    1 // sprite facing right
            		);
        		
        		Enemy slime3 = new Enemy(
            		    43* TILE, 10 * TILE,     // start
            		    36 * TILE, 10 * TILE,     // end
            		    16, 16,                 // sprite size
            		    3,                      // scale
            		    greenSlimeSheet,
            		    4, 7,                   // idle frames
            		    4, 7,                   // move frames
            		    Enemy.PatrolType.HORIZONTAL,
            		    80f,                    // speed
            		    2.5f,                    // idle seconds
            		    1 // sprite facing right
            		);
        		
        		Enemy slime4 = new Enemy(
            		    14* TILE, 2 * TILE,     // start
            		    19 * TILE, 2* TILE,     // end
            		    16, 16,                 // sprite size
            		    3,                      // scale
            		    greenSlimeSheet,
            		    4, 7,                   // idle frames
            		    4, 7,                   // move frames
            		    Enemy.PatrolType.HORIZONTAL,
            		    80f,                    // speed
            		    2.5f,                    // idle seconds
            		    1 // sprite facing right
            		);



            enemies.add(slime);
            enemies.add(slime2);
            enemies.add(slime3);
            enemies.add(slime4);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================
        // PLAYER
        // =========================
        player = new Player(
                3 * 16 * 3,
                6 * 16 * 3,
                "/resources/sprites/knight.png"
        );
    }

    // ======================================================
    // UPDATE
    // ======================================================
    @Override
    public void update(double dt) {
        super.update(dt);

        if (!paused && !levelFinished && !playerDead) {

            // Update player
            int gained = player.update(dt, coins, map, coinSound);
            collectedCoins += gained;
            
            // Platforms
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
            
	         // =========================
	         // UPDATE ARROW TRAPS
	         // =========================
	         for (ArrowTrap trap : arrowTraps) {
	             trap.update(dt, player.getBounds());
	
	             // Arrow hits player
	             if (!playerDead && trap.checkPlayerHit(player.getBounds())) {
	                 onPlayerDeath();
	                 break;
	             }
	         }

            
		      // =========================
		      // ARROW → PLAYER COLLISION
		      // =========================
		      for (ArrowTrap trap : arrowTraps) {
		          if (!playerDead && trap.checkPlayerHit(player.getBounds())) {
		              onPlayerDeath();
		              break; // stop checking after death
		          }
		      }

            
            // Update enemies
            for (Enemy e : enemies) {
                e.update(dt);

                // Simple collision → death
                if (player.getBounds().intersects(e.getBounds())) {
                    onPlayerDeath();
                }
            }

            // Goal check
            if (goal != null && player.getBounds().intersects(goal.getBounds())) {
                finalizeLevel();
            }
        }

        // Update coins
        for (Coin coin : coins) {
            coin.update();
        }

        // Kill zone
        if (!playerDead && player.getY() > killY) {
            onPlayerDeath();
        }
    }

    // ======================================================
    // RENDER
    // ======================================================
    @Override
    public void render(Graphics g) {
        int camX = player.getX() - 450;
        int camY = player.getY() - 300;

        // Clear screen
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 900, 600);

        // World
        renderTiles(g, camX, camY);

        // Coins
        for (Coin coin : coins) {
            coin.drawAt(g, camX, camY);
        }
        
        // Draw arrow traps
        for (ArrowTrap trap : arrowTraps) {
            trap.draw(g, camX, camY);
        }

        // Spikes
        for (Spike s : spikes) {
            s.draw(g, camX, camY);
        }
        
        // Platforms
        for (Platform p : platforms) {
            p.draw(g, camX, camY);
        }
        
        // Enemies
        for (Enemy e : enemies) {
            e.draw(g, camX, camY);
        }

        // Goal
        if (goal != null) {
            goal.drawAt(g, camX, camY);
        }

        // Player
        player.drawAt(g, camX, camY);

        // UI
        renderUI(g);
    }

    // ======================================================
    // INPUT
    // ======================================================
    @Override
    public void keyPressed(int key) {
        super.keyPressed(key);

        if (!paused && !levelFinished && !playerDead) {
            player.keyPressed(key);
        }
    }

    @Override
    public void keyReleased(int key) {
        player.keyReleased(key);
    }

    // ======================================================
    // STATE TRANSITIONS
    // ======================================================
    @Override
    protected void restartLevel() {
        stopMusic();
        gsm.setState(GameStateManager.LEVEL_1);
    }

    @Override
    protected void nextLevel() {
        stopMusic();
        gsm.setState(GameStateManager.LEVEL_2);
    }

    @Override
    protected void goToLevelSelect() {
        stopMusic();
        gsm.setState(GameStateManager.MAIN_MENU);
    }
}
