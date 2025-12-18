package com.mygame.states;

import static com.mygame.GameConstants.TILE;

import java.awt.Color;
import java.awt.Font;
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
import com.mygame.Sound;
import com.mygame.Spike;
import com.mygame.TileLoader;

public class Level2State extends BaseLevelState{

	private final GameStateManager gsm;
    private Player player;
    private BufferedImage[] tileset;
    
    private List<Platform> platforms;
    private List<Enemy> enemies;
    private List<Spike> spikes;
    private List<Saw> saws;
    private List<ArrowTrap> arrowTraps;

    private int goalX=56;
    private int goalY=-2;
    
    public Level2State(GameStateManager gsm) {
    	this.gsm = gsm;
    	
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
        		    7* TILE, 9 * TILE,     // start
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
        		
            enemies.add(slime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
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
   	          35 * 16 * 3,
   	          10 * 16 * 3,
   	          ArrowTrap.Direction.LEFT,
   	          14f * 16 * 3,
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
            path1.add(new Point(-12 * TILE, 1 * TILE));
            path1.add(new Point(-12 * TILE, 9 * TILE));
            path1.add(new Point(6 * TILE, 9 * TILE));
            path1.add(new Point(6 * TILE, 1 * TILE));

            List<Float> speeds1 = new ArrayList<>();
            speeds1.add(100f);
            speeds1.add(700f);
            speeds1.add(400f);
            speeds1.add(300f);

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
            path2.add(new Point(17 * TILE, -11 * TILE));
            path2.add(new Point(27 * TILE, -1 * TILE));
            path2.add(new Point(30 * TILE, -4 * TILE));
            path2.add(new Point(27 * TILE, -7 * TILE));
            path2.add(new Point(31 * TILE, -10 * TILE));

            List<Float> speeds2 = new ArrayList<>();
            speeds2.add(300f);
            speeds2.add(700f);
            speeds2.add(300f);
            speeds2.add(300f);
            speeds2.add(300f);

            saws.add(new Saw(
                path2,
                speeds2,
                sawSheet,
                32, 32, // sprite size
                3,      // scale
                0, 0,   // idle frames
                0, 7,    // move frames
                new Rectangle(27*16*3,-1*16*3,1*16*3,1*16*3),
                false,
                true
            ));

        } catch (Exception e) {
            e.printStackTrace();
        }
    	
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

    	spikes.add(new Spike(9,6,16,16,spikeSprite));
    	spikes.add(new Spike(0,9,16,16,spikeSprite));
        
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
    		        27,0, 
    		        27,0,
    		        16,
    		        16,
    		        plat16,
    		        0f
    		    )
    		);

    		platforms.add(
    		    new Platform(
    		        27,-6, 
    		        27,-6,
    		        16, 
    		        16,
    		        plat16,
    		        0f
    		    )
    		);

    		platforms.add(
        		    new Platform(
        		        30,-3, 
        		        30,-3,
        		        16, 
        		        16,
        		        plat16,
        		        0f
        		    )
        		);
    		
    		platforms.add(
        		    new Platform(
        		        39,-9, 
        		        39,-1,
        		        32, 
        		        16,
        		        plat32,
        		        100f
        		    )
        		);
    	
    	tileset = TileLoader.loadTiles("/resources/sprites/world_tileset.png", 16, 16);
    	
    	List<int[]> coinPositions = new ArrayList<>();
        coinPositions.add(new int[]{1 * 16 * 3, 8 * 16 * 3});
        coinPositions.add(new int[]{12 * 16 * 3, 3 * 16 * 3});
        coinPositions.add(new int[]{31 * 16 * 3, 7 * 16 * 3});
        coinPositions.add(new int[]{31 * 16 * 3, 2 * 16 * 3});
        coinPositions.add(new int[]{21 * 16 * 3, 10 * 16 * 3});

        setGoal(goalX * 16 * 3, goalY * 16 * 3, "/resources/sprites/goal.png");
        
        // Add coins to the level
        addCoins(coinPositions, "/resources/sprites/coin.png");
    	
    	// Initialize tile map (use inherited map)
        map = new java.util.ArrayList<>();
        if (tileset != null && tileset.length > 0) {
            // Ground at y=10
            for (int i = -9; i <= 12; i++) {
                addTile(i, 10, 0, 1, true); // inherited method
                addTile(i,11,16,1,true);
                if(i>=-6) addTile(i,12,16,1,true);
                if(i>=6) addTile(i,13,16,1,true);
            }
            
            for(int i=6; i<11; i++) {
            	addTile(i,7,0,1,true);
            }
            
            for(int i=16;i<18;i++) {
            	addTile(i,9,0,1,true);
            	for(int y=10;y<12;y++) {            		
            		addTile(i,y,16,1,true);
            	}
            }
            
            addTile(17,12,16,1,true);

            addTile(21,11,0,1,true);
            addTile(21,12,16,1,true);
            addTile(21,13,16,1,true);
            
            for(int i=27;i<33;i++) {
            	addTile(i,11,0,1,true);
            	addTile(i,12,16,1,true);
            	if(i>27) addTile(i,13,16,1,true);
            	if(i>28&&i<32) addTile(i,14,16,1,true);
            	if(i>29&&i<32) addTile(i,15,16,1,true);
            }
            addTile(31,16,16,1,true);
            
            for(int i=35;i<39;i++) {
            	addTile(i,9,0,1,true);
            	if(i>=35 && i<=36) {
            		addTile(i,12,16,1,true);
            	}
            	
            	for(int y=10;y<=11;y++) {
            		if(y==10 && i == 35) continue;
            		else addTile(i,y,16,1,true);
            	}
            }
            addTile(35,13,16,1,true);
            addTile(38,12,16,1,true);
            
            for(int x=41;x<=43;x++) {
            	addTile(x,6, 0,1,true);
            	for(int y=7;y<=10;y++) {
            		if(y==7) addTile(x,y, 16,1,true);
            		if(y>=8 && y<=9 && x > 41) addTile(x,y,16,1,true);
            		if(y>9) addTile(43,y,16,1,true);
            	}
            }
            
            for(int i = 35; i<=36; i++) {
            	addTile(i,4,0,1,true);
            	addTile(i,5,16,1,true);
            }
            
            for(int i=27;i<35;i++) {
            	addTile(i,3,0,1,true);
            	addTile(i,4,16,1,true);
            }
            
            addTile(30,-8,0,1,true);
            for(int x=31;x<=36;x++) {
            	addTile(x,-9,0,1,true);
        		addTile(x,-8,16,1,true);
        		if(x>=33) addTile(x,-7,16,1,true);
        		if(x>=34) addTile(x,-6,16,1,true);
        		if(x>=36) addTile(x,-5,16,1,true);
            }
            
            for(int x=43;x<=44;x++) {
            	addTile(x,-1,0,1,true);
            	addTile(x,0,16,1,true);
            	if(x==43) addTile(x,1,16,1,true);
            }
            
            for(int y=-1;y<=3;y++) { 
            	addTile(47,-1,0,1,true);
            	if(y>=0) addTile(47,y,16,1,true);
            }
            
            for(int x=52;x<58;x++) {
            	addTile(x,-1,0,1,true);
            	for(int y=0;y<3;y++) {
            		if(y<2) addTile(x,y,16,1,true);
            		if(y>1 && x<54) addTile(x,y,16,1,true);
            	}
            }
            
            //BG
            for(int x=-23; x<70;x++) {
            	for(int y=13;y<25;y++) {
            		addTile(x,y,240,0,false);
            	}
            }
            
            for(int x=-23; x<70;x++) {
            	addTile(x,12,224,0,false);
            }
            
            for(int x=-23; x<70;x++) {
            	for(int y=5;y<12;y++) {
            		addTile(x,y,208,0,false);
            	}
            }
            
            for(int x=-23; x<70;x++) {
            	addTile(x,4,192 ,0,false);
            }
            
            for(int x=-23; x<70;x++) {
            	for(int y=-5;y<4;y++) {
            		addTile(x,y,176,0,false);
            	}
            }
            
            for(int x=-23; x<70;x++) {
            	addTile(x,-6,160 ,0,false);
            }
            
            for(int x=-23; x<70;x++) {
            	for(int y=-20;y<-6;y++) {
            		addTile(x,y,144,0,false);
            	}
            }
            
        }

        // Create player
        player = new Player(-6 * 16 *3, 6 * 16 *3, "/resources/sprites/knight.png");
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
        	
            for (Coin coin : coins) {
                coin.update();
            }
            
            if (goal != null && player.getBounds().intersects(goal.getBounds())) {
                finalizeLevel();
            }
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
        
        // Draw arrow traps
        for (ArrowTrap trap : arrowTraps) {
            trap.draw(g, camX, camY);
        }
        
        // Spikes
        for (Spike s : spikes) {
            s.draw(g, camX, camY);
        }
        
        // Enemies
        for (Enemy e : enemies) {
            e.draw(g, camX, camY);
        }
        
        for (Saw s : saws) {
            s.draw(g, camX, camY);
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
