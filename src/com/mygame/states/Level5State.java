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

public class Level5State extends BaseLevelState {

    private final GameStateManager gsm;
    private Player player;
    private BufferedImage[] tileset;


    private int goalX = 49;
    private int goalY = -1;

    public Level5State(GameStateManager gsm) {
        this.gsm = gsm;
        
        levelNumber = 5;
        setGoal(goalX * TILE, goalY * TILE, "/resources/sprites/goal.png");
        // --- COINS ---
        List<int[]> coinPositions = new ArrayList<>();
        
        int startX = 8;
        int baseY = -1;

        for (int i = 0; i < 7; i++) {
            int yOffset = -(i <= 3 ? i : 6 - i);
            coinPositions.add(new int[]{(startX + i) * TILE, (baseY + yOffset) * TILE});
        }
        
        startX = 15;

        for (int i = 0; i < 7; i++) {
            int yOffset = -(i <= 3 ? i : 6 - i);
            coinPositions.add(new int[]{(startX + i) * TILE, (baseY + yOffset) * TILE});
        }
        
        startX = 15+7;

        for (int i = 0; i < 7; i++) {
            int yOffset = -(i <= 3 ? i : 6 - i);
            coinPositions.add(new int[]{(startX + i) * TILE, (baseY + yOffset) * TILE});
        }
        
        startX = 15+7+7;

        for (int i = 0; i < 7; i++) {
            int yOffset = -(i <= 3 ? i : 6 - i);
            coinPositions.add(new int[]{(startX + i) * TILE, (baseY + yOffset) * TILE});
        }
        startX = 15+7+7+7;

        for (int i = 0; i < 7; i++) {
            int yOffset = -(i <= 3 ? i : 6 - i);
            coinPositions.add(new int[]{(startX + i) * TILE, (baseY + yOffset) * TILE});
        }
        
        startX = 15+7+7+7+7;

        for (int i = 0; i < 7; i++) {
            int yOffset = -(i <= 3 ? i : 6 - i);
            coinPositions.add(new int[]{(startX + i) * TILE, (baseY + yOffset) * TILE});
        }

        
        addCoins(coinPositions, "/resources/sprites/coin.png");

        // --- TILES ---
        tileset = TileLoader.loadTiles("/resources/sprites/world_tileset.png", 16, 16);
        map = new ArrayList<>();
        if (tileset != null) {
            // Ground
            for (int x = -5; x < 50; x++) addTile(x, 0, 0, 1, true);
            for (int x = -5; x < 50; x++) addTile(x, -8, 16, 1, true);
            for(int y=-8;y<1;y++) {
            	addTile(-5,y,16,1,true);
            }
            for(int y=-8;y<1;y++) {
            	addTile(50,y,16,1,true);
            }
            
            //BG
            for(int x=-5; x<51;x++) {
            	for(int y=-8;y<1;y++) {
            		addTile(x,y,176,0,false);
            	}
            }
            
            finalizeMap();
        }


        // --- PLAYER ---
        player = new Player(0 * TILE, -3 * TILE, "/resources/sprites/knight.png");
    }

    @Override
    public void update(double dt) {
        super.update(dt);

        if (!paused && !levelFinished && !playerDead) {
            int gained = player.update(dt, coins, map, coinSound);
            collectedCoins += gained;

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

        for (Coin coin : coins) coin.drawAt(g, camX, camY);

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
        gsm.setState(GameStateManager.LEVEL_5);
    }

    @Override
    protected void nextLevel() {
        stopMusic();
        gsm.setState(GameStateManager.MAIN_MENU);
    }

    @Override
    protected void goToLevelSelect() {
        stopMusic();
        gsm.setState(GameStateManager.MAIN_MENU);
    }
}
