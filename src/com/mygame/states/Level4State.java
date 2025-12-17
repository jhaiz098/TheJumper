package com.mygame.states;

import static com.mygame.GameConstants.TILE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.mygame.BreakablePlatform;
import com.mygame.Coin;
import com.mygame.GameStateManager;
import com.mygame.Platform;
import com.mygame.Player;
import com.mygame.Sound;
import com.mygame.Spike;
import com.mygame.TileLoader;

public class Level4State extends BaseLevelState {

    private final GameStateManager gsm;
    private Player player;
    private BufferedImage[] tileset;

    private List<Platform> platforms;
    private List<Spike> spikes;
    private List<BreakablePlatform> breakablePlatforms;

    private int goalX = 15;
    private int goalY = 2;
    
    private boolean landed = false;

    public Level4State(GameStateManager gsm) {
        this.gsm = gsm;
        
        setKillY(3000);
        
        setGoal(goalX * TILE, goalY * TILE, "/resources/sprites/goal.png");
        
        // --- SPIKES ---
        BufferedImage spikeSprite;
        try {
            BufferedImage spikeSheet = ImageIO.read(getClass().getResource("/resources/sprites/spike.png"));
            spikeSprite = spikeSheet.getSubimage(0, 0, 16, 16);
        } catch (Exception e) {
            e.printStackTrace();
            spikeSprite = null;
        }

        spikes = new ArrayList<>();
        spikes.add(new Spike(-6, 19, 16, 16, spikeSprite));
        spikes.add(new Spike(6, 25, 16, 16, spikeSprite));

        // --- PLATFORMS ---
        platforms = new ArrayList<>();
        BufferedImage platformSheet, plat16, plat32;
        try {
            platformSheet = ImageIO.read(getClass().getResource("/resources/sprites/platforms.png"));
            plat16 = platformSheet.getSubimage(0, 16, 16, 16);
            plat32 = platformSheet.getSubimage(16, 16, 32, 16);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        breakablePlatforms = new ArrayList<>();
        BufferedImage breakableSprite16 = plat16; 
        BufferedImage breakableSprite32 = plat32;

        // Example: a platform at (6, 3) in tiles
        breakablePlatforms.add(new BreakablePlatform(1, 8, 32, 16, breakableSprite32,0.5f));
        breakablePlatforms.add(new BreakablePlatform(14, 7, 16, 16, breakableSprite16,0.5f));
        breakablePlatforms.add(new BreakablePlatform(10, 10, 16, 16, breakableSprite16,0.5f));
        breakablePlatforms.add(new BreakablePlatform(14, 13, 16, 16, breakableSprite16,0.5f));
        breakablePlatforms.add(new BreakablePlatform(10, 4, 16, 16, breakableSprite16,0.5f));
        

        
        

//        // Static safe platforms
        platforms.add(new Platform(10, 15,10, 20, 32, 16, plat32, 100f)); // Start
        platforms.add(new Platform(12,25,12, 20, 32, 16, plat32, 100f));
        platforms.add(new Platform(-3, 17, -3,17 , 32, 16, plat32, 0));
        platforms.add(new Platform(-7, 20, -3,17 , 32, 16, plat32, 0));
        
        platforms.add(new Platform(-5, 29, 4,28, 16, 16, plat16, 0));
        
        platforms.add(new Platform(10, 25, 4,28, 16, 16, plat16, 0));
        
        platforms.add(new Platform(-4,26,-4, 22, 16, 16, plat16, 100f));
        platforms.add(new Platform(15,26,15, 26, 16, 16, plat16, 0));
        

        // --- COINS ---
        List<int[]> coinPositions = new ArrayList<>();
        coinPositions.add(new int[]{0 * TILE, 14 * TILE});
        coinPositions.add(new int[]{-7 * TILE, 19 * TILE});
        coinPositions.add(new int[]{10 * TILE, 23 * TILE});
        coinPositions.add(new int[]{15 * TILE, 24 * TILE});
        coinPositions.add(new int[]{-5 * TILE, 27 * TILE});
        coinPositions.add(new int[]{6 * TILE, 24 * TILE});
        addCoins(coinPositions, "/resources/sprites/coin.png");

        // --- TILES ---
        tileset = TileLoader.loadTiles("/resources/sprites/world_tileset.png", 16, 16);
        map = new ArrayList<>();
        if (tileset != null) {
            // Ground
            for (int x = -1; x < 8; x++) addTile(x, 15, 2, 1, true);
            
            for(int x =0;x<9;x++) {
            	for(int y=16;y<23;y++) {
            		addTile(x,y,18,1,true);
            	}
            }
            
            for(int y= 0;y<16;y++) {addTile(8,y,18,1,true);}
            for(int y= 0;y<32; y++) {addTile(-8, y,18,1,true);}            
            for(int y=16;y<23;y++) {addTile(-1,y,18,1,true);}
            for(int x=-1;x<5;x++) {addTile(x,23,18,1,true);}
            for(int x=-7; x<16;x++) {addTile(x,31,2,1,true);}
            for(int y=0; y<32;y++) {addTile(16,y,18,1,true);}
            for(int x=14;x<16;x++) {
            	for(int y=29;y<32;y++) {addTile(x,y,18,1,true);}
            }
            
            for(int x=-7;x<16;x++) {
            	addTile(x,0,18,1,true);
            }
            
            for(int x=-2; x<7;x++) {addTile(x,27,2,1,true);}
            addTile(6,26,2,1,true);
            
            addTile(15,3,2,1,true);
            addTile(14,3,2,1,true);
            addTile(13 ,3,2,1,true);
            
            //BG
            for(int x=-8;x<17;x++) {
            	for(int y=0;y<10;y++) {
            		addTile(x,y,176,0,false);
            	}
            }
            
            for(int x=-8;x<17;x++) {addTile(x,10,192,0,false);}
            
            for(int x = -8; x<17;x++) {
            	for(int y=11;y<32;y++) {
            		addTile(x,y,208,0,false);
            	}
            }
        }


        // --- PLAYER ---
        player = new Player(1 * TILE, 5 * TILE, "/resources/sprites/knight.png");
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

            for (BreakablePlatform bp : breakablePlatforms) {
                if (bp.isPlayerOn(player.getBounds())) bp.trigger();
                player.collideWithPlatform(bp);
                bp.update(dt);
            }



            
            for (Spike s : spikes) {
                if (!playerDead && player.getBounds().intersects(s.getBounds())) {
                    onPlayerDeath();
                    break;
                }
            }

            if (goal != null && player.getBounds().intersects(goal.getBounds())) finalizeLevel();
        }

        for (Coin coin : coins) coin.update();
        if (!playerDead && player.getY() > killY) onPlayerDeath();
    }

    @Override
    public void render(Graphics g) {
        int camX = player.getX() - 450;
        int camY = player.getY() - 300;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 900, 600);

        renderTiles(g, camX, camY);

        for (Platform p : platforms) p.draw(g, camX, camY);
        for (Coin coin : coins) coin.drawAt(g, camX, camY);
        for (Spike s : spikes) s.draw(g, camX, camY);
        for (BreakablePlatform bp : breakablePlatforms) {
            if (bp.getCollider().width > 0 && bp.getCollider().height > 0) {
                player.collideWithPlatform(bp); // might need overloading in your player class
                bp.draw(g, camX, camY);
            }
        }


        if (goal != null) goal.drawAt(g, camX, camY);

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
        gsm.setState(GameStateManager.LEVEL_4);
    }

    @Override
    protected void nextLevel() {
        stopMusic();
        gsm.setState(GameStateManager.LEVEL_5);
    }

    @Override
    protected void goToLevelSelect() {
        stopMusic();
        gsm.setState(GameStateManager.MAIN_MENU);
    }
}
