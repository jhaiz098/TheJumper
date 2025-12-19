package com.mygame.states;

import static com.mygame.GameConstants.TILE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.mygame.ArrowTrap;
import com.mygame.Coin;
import com.mygame.Enemy;
import com.mygame.GameStateManager;
import com.mygame.Platform;
import com.mygame.Player;
import com.mygame.Saw;
import com.mygame.Spike;
import com.mygame.TileLoader;

public class Level2State extends BaseLevelState{
	
	private final GameStateManager gsm;
    private Player player;
    private BufferedImage[] tileset;
    
    private List<Platform> platforms;
    private List<Enemy> enemies;
    private List<Saw> saws;
    private List<Spike> spikes;
    private List<ArrowTrap> arrowTraps;
    
    private int goalX=53;
    private int goalY=9;
    
    public Level2State(GameStateManager gsm) {
    	this.gsm = gsm;
    	
    	levelNumber = 2;
    	setGoal(goalX * 16 * 3, goalY * 16 * 3, "/resources/sprites/goal.png");
    	
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
   	          59 * 16 * 3,
   	          9 * 16 * 3,
   	          ArrowTrap.Direction.LEFT,
   	          30f * 16 * 3,
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
        // SAWS
        // =========================
        saws = new ArrayList<>();

        try {
            BufferedImage sawSheet = ImageIO.read(getClass().getResource("/resources/sprites/Suriken.png"));

            List<Point> path1 = new ArrayList<>();
            path1.add(new Point(3 * TILE, 3 * TILE));
            path1.add(new Point(3 * TILE, 9 * TILE));

            List<Float> speeds1 = new ArrayList<>();
            speeds1.add(200f);
            speeds1.add(200f);

            saws.add(new Saw(
                path1,
                speeds1,
                sawSheet,
                32, 32, // sprite size
                3,      // scale
                0, 0,   // idle frames
                0, 7,    // move frames
                true
            ));
            
            List<Point> path2 = new ArrayList<>();
            path2.add(new Point(16 * TILE, 1 * TILE));
            path2.add(new Point(16 * TILE, 6 * TILE));

            List<Float> speeds2 = new ArrayList<>();
            speeds2.add(200f);
            speeds2.add(200f);

            saws.add(new Saw(
                path2,
                speeds2,
                sawSheet,
                32, 32, // sprite size
                3,      // scale
                0, 0,   // idle frames
                0, 7,    // move frames
                true
            ));
            
            List<Point> path3 = new ArrayList<>();
            path3.add(new Point(26 * TILE, -10 * TILE));
            path3.add(new Point(26 * TILE, 7 * TILE));
            path3.add(new Point(32 * TILE, 7 * TILE));

            List<Float> speeds3 = new ArrayList<>();
            speeds3.add(200f);
            speeds3.add(600f);
            speeds3.add(200f);

            saws.add(new Saw(
                path3,
                speeds3,
                sawSheet,
                32, 32, // sprite size
                3,      // scale
                0, 0,   // idle frames
                0, 7,    // move frames
                new Rectangle(27*16*3,5*16*3,6*16*3,3*16*3),
                false,
                true
            ));

        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    	enemies = new ArrayList<>();

        try {
        	BufferedImage greenSlimeSheet = ImageIO.read(
        		    getClass().getResource("/resources/sprites/slime_green.png")
        		);
        	
        	BufferedImage purpleSlimeSheet = ImageIO.read(
        		    getClass().getResource("/resources/sprites/slime_purple.png")
        		);

        		Enemy slime = new Enemy(
        		    10* TILE, 9 * TILE,     // start
        		    1 * TILE, 9 * TILE,     // end
        		    16, 16,                 // sprite size
        		    3,                      // scale
        		    greenSlimeSheet,
        		    4, 7,                   // idle frames
        		    4, 7,                   // move frames
        		    Enemy.PatrolType.HORIZONTAL,
        		    100f,                    // speed
        		    2.5f,                    // idle seconds
        		    1 // sprite facing right
        		);
//        		
        		Enemy slime2 = new Enemy(
            		    26* TILE, 7 * TILE,     // start
            		    32 * TILE, 7 * TILE,     // end
            		    16, 16,                 // sprite size
            		    3,                      // scale
            		    greenSlimeSheet,
            		    4, 7,                   // idle frames
            		    4, 7,                   // move frames
            		    Enemy.PatrolType.HORIZONTAL,
            		    100f,                    // speed
            		    2.5f,                    // idle seconds
            		    1 // sprite facing right
            		);
        		
        		Enemy slime3 = new Enemy(
            		    14* TILE, 9 * TILE,     // start
            		    19 * TILE, 9 * TILE,     // end
            		    16, 16,                 // sprite size
            		    3,                      // scale
            		    greenSlimeSheet,
            		    4, 7,                   // idle frames
            		    4, 7,                   // move frames
            		    Enemy.PatrolType.HORIZONTAL,
            		    100f,                    // speed
            		    2.5f,                    // idle seconds
            		    1 // sprite facing right
            		);
//        		
            enemies.add(slime);
            enemies.add(slime2);
            enemies.add(slime3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
        
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
//    	
    	spikes.add(new Spike(7,9,16,16,spikeSprite));
    	spikes.add(new Spike(24,9,16,16,spikeSprite));
    	spikes.add(new Spike(37,9,16,16,spikeSprite));
    	spikes.add(new Spike(44,7,16,16,spikeSprite));
    	
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

    	    plat16 = platformSheet.getSubimage(0, 0, 16, 16);
    	    plat32 = platformSheet.getSubimage(16, 0, 32, 16);
    	    
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
    		        16, 7, 
    		        16, 7,
    		        32, 16,
    		        plat32,
    		        0f
    		    )
    		);    		
    		
    	List<int[]> coinPositions = new ArrayList<>();
    	coinPositions.add(new int[]{8 * 16 * 3, 9 * 16 * 3});
    	coinPositions.add(new int[]{17 * 16 * 3, 5 * 16 * 3});
    	coinPositions.add(new int[]{19 * 16 * 3, 9 * 16 * 3});
    	coinPositions.add(new int[]{29 * 16 * 3, 5 * 16 * 3});
    	coinPositions.add(new int[]{24 * 16 * 3, 8 * 16 * 3});
    	coinPositions.add(new int[]{44 * 16 * 3, 6 * 16 * 3});
    	
    	// Add coins to the level
        addCoins(coinPositions, "/resources/sprites/coin.png"); 
    	
    	tileset = TileLoader.loadTiles("/resources/sprites/world_tileset.png", 16, 16);
    	
    	// Initialize tile map (use inherited map)
        map = new java.util.ArrayList<>();
        if (tileset != null && tileset.length > 0) {
        	addTile(-2,10,0,1,true);
        	addTile(-1,10,0,1,true);
        	addTile(-1,11,16,1,true);
        	addTile(-1,12,16,1,true);
        	addTile(-2,11,16,1,true);
        	
            // Ground at y=10
            for (int i = 1; i <= 10; i++) {
                addTile(i, 10, 0, 1, true); // inherited method
                addTile(i, 11, 16, 1, true); 
                if(i>2) addTile(i,12,16,1,true);
                if(i>7) addTile(i,13,16,1,true);
            }
            
            for (int i = 14; i <= 19; i++) {
                addTile(i, 10, 0, 1, true); // inherited method
                addTile(i, 11, 16, 1, true);
                if(i>=16 && i <=17) addTile(i,12,16,1,true);
            }
            
            for (int i = 23; i <= 39; i++) {
            	if(i>=23 && i<=24) addTile(i, 10, 0, 1, true); // inherited method
            	if(i>=34) addTile(i, 10, 0, 1, true); // inherited method
            	
                if(i>=26 && i<=32) addTile(i, 8, 0, 1, true); 
                if(i>=26 && i<=32) addTile(i, 9, 16, 1, true);
                if(i>=25 && i<=33) addTile(i, 10, 16, 1, true);
                
                if(i>=32) addTile(i, 11, 16, 1, true);
            }
            addTile(25,9,0,1,true);
            addTile(33,9,0,1,true);
            
            for(int i=43;i<=45;i++) {
            	addTile(i,8,0,16,true);
            }
            
            for(int i=49;i<=54;i++) {
            	addTile(i,10,0,16,true);
            }
            
          //BG
            for(int x=-15; x<60;x++) {
              	for(int y=13;y<25;y++) {
              		addTile(x,y,240,0,false);
              	}
              }
              
              for(int x=-15; x<60;x++) {
              	addTile(x,12,224,0,false);
              }
              
              for(int x=-15; x<60;x++) {
              	for(int y=5;y<12;y++) {
              		addTile(x,y,208,0,false);
              	}
              }
              
              for(int x=-15; x<60;x++) {
              	addTile(x,4,192 ,0,false);
              }
              
              for(int x=-15; x<60;x++) {
              	for(int y=-5;y<4;y++) {
              		addTile(x,y,176,0,false);
              	}
              }
            
        }
        
        // Create player
        player = new Player(-2 * 16 * 3, 7 * 16 * 3, "/resources/sprites/knight.png");
//        player = new Player(-26 * 16 * 3, -16 * 16 * 3, "/resources/sprites/knight.png");
    }
    
    @Override
    public void update(double dt) {
    	super.update(dt);
    	
        if (!paused && !levelFinished && !playerDead) {
        	int gained = player.update(dt, coins, map, coinSound);
        	collectedCoins += gained;
        	
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
        	
        	for (Platform p : platforms) {
        		p.update(dt);
        	    player.collideWithPlatform(p);
        	}
        	
        	// Saw logic
	         for (Saw s : saws) {
	        	    s.update(dt, player.getBounds());

	        	    if (!playerDead && player.getBounds().intersects(s.getBounds())) {
	        	        onPlayerDeath();
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
        
     // Draw arrow traps
        for (ArrowTrap trap : arrowTraps) {
            trap.draw(g, camX, camY);
        }
        
        for (Platform p : platforms) {
            p.draw(g, camX, camY);
        }
        
        for (Saw s : saws) {
            s.draw(g, camX, camY);
        }
        
        for (Coin coin : coins) {
            coin.drawAt(g, camX, camY);
        }
        
        for (Spike s : spikes) {
            s.draw(g, camX, camY);
        }
        
        // Enemies
        for (Enemy e : enemies) {
            e.draw(g, camX, camY);
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