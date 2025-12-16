package com.mygame.states;

import com.mygame.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

public abstract class BaseLevelState implements GameState {

    protected List<Tile> map;
    protected List<Coin> coins;  // List of coins in the level

    protected boolean showFinishUI = false;
    
    protected Goal goal;

    protected int maxCoins = 0;
    protected int collectedCoins = 0;

    protected boolean levelFinished = false;
    protected int starsEarned = 0;

    
    
    private int cachedOriginX = 0;
    private int cachedOriginY = 0;

    private BufferedImage cachedMap = null;
    private boolean needsRedraw = true;

    public BaseLevelState() {
        map = new ArrayList<>();
        coins = new ArrayList<>();  // Initialize the coins list
    }

    protected void setGoal(int x, int y, String spritePath) {
        goal = new Goal(x, y, spritePath);
    }

    protected void finalizeLevel() {
        if (maxCoins == 0) {
            starsEarned = 3;
            levelFinished = true;
            showFinishUI = true; // ← ADD
            return;
        }

        collectedCoins = maxCoins - coins.size();
        float ratio = (float) collectedCoins / (float) maxCoins;

        if (ratio >= 1.0f) starsEarned = 3;
        else if (ratio >= 0.66f) starsEarned = 2;
        else if (ratio >= 0.33f) starsEarned = 1;
        else starsEarned = 0;

        levelFinished = true;
        showFinishUI = true; // ← ADD
    }
    

    
    // Add a tile to the map (same as before)
    protected void addTile(int tx, int ty, int tileIndex, int zIndex, boolean solid) {
        BufferedImage[] tileset = TileLoader.loadTiles("/resources/sprites/world_tileset.png", 16, 16);
        if (tileset != null && tileset.length > tileIndex) {
            map.add(new Tile(tx, ty, tileset[tileIndex], zIndex, solid));
            needsRedraw = true; // map changed → redraw next frame
        }
    }

    // Method to add a coin at a specific position
    protected void addCoin(int x, int y, String spriteSheetPath) {
        Coin coin = new Coin(x, y, spriteSheetPath);  // Create a new coin
        coins.add(coin);  // Add it to the list of coins
    }
    
    // In BaseLevelState
    protected void addCoins(List<int[]> positions, String spriteSheetPath) {
        for (int[] pos : positions) {
            addCoin(pos[0], pos[1], spriteSheetPath);
        }
        maxCoins = positions.size();
    }


    private void rebuildMapImage() {
        if (map.isEmpty()) return;

        map.sort((a, b) -> Integer.compare(a.getZ(), b.getZ()));

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (Tile t : map) {
            int x = t.getScreenX();
            int y = t.getScreenY();
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x + Tile.PIXEL);
            maxY = Math.max(maxY, y + Tile.PIXEL);
        }

        int width = Math.max(1, maxX - minX);
        int height = Math.max(1, maxY - minY);

        cachedMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = cachedMap.createGraphics();

        for (Tile t : map) {
            if (t.sprite == null) continue;
            g2.drawImage(t.sprite,
                t.getScreenX() - minX,
                t.getScreenY() - minY,
                Tile.PIXEL, Tile.PIXEL,
                null
            );
        }

        g2.dispose();

        cachedOriginX = minX;
        cachedOriginY = minY;

        needsRedraw = false;
    }

    protected void renderTiles(Graphics g, int camX, int camY) {
        if (needsRedraw || cachedMap == null) {
            rebuildMapImage();
        }

        if (cachedMap == null) return;

        int drawX = cachedOriginX - camX;
        int drawY = cachedOriginY - camY;
        g.drawImage(cachedMap, drawX, drawY, null);
        
        // Always show controls hint AFTER level is finished
        if (showFinishUI) {
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.setColor(new Color(255, 255, 255, 255));
            g.drawString("ESC - Level Select   |   R - Restart Level   |   N - Next Level", 300, 500);
        }

    }
    protected abstract void restartLevel();
    protected abstract void nextLevel();
    protected abstract void goToLevelSelect();
    @Override
    public void keyPressed(int key) {
//        if (!levelFinished) return;

        if (key == KeyEvent.VK_ESCAPE) {
            goToLevelSelect();
        }

        if (key == KeyEvent.VK_R) {
            restartLevel();
        }
        
        if (key == KeyEvent.VK_N) {
            nextLevel();
        }
    }
    
    

    
    @Override
    public void keyReleased(int key) {}
    @Override
    public void update(double dt) {
        for (Coin coin : coins) {
            coin.update();  // Update each coin's animation
        }
    }

    @Override
    public void render(Graphics g) {
        // Draw the tiles
        renderTiles(g, 0, 0); // You already have this method for tiles rendering

        // Draw the coins (they will be updated automatically)
        for (Coin coin : coins) {
            coin.drawAt(g, 0, 0);  // Draw each coin
        }

        // Draw the number of coins
        g.setColor(Color.WHITE); // Set text color to white (you can change this)
        g.setFont(new Font("Arial", Font.BOLD, 20)); // Set font size and style
        String coinCountText = "Coins: " + collectedCoins + " / " + maxCoins;
        g.drawString(coinCountText, 10, 30); // Draw the coin count at the top-left corner
        
        if (levelFinished) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, 900, 600);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 26));
            g.drawString("LEVEL COMPLETE", 330, 150);

            g.setFont(new Font("Arial", Font.BOLD, 22));
            g.drawString("Stars: " + starsEarned + " / 3", 360, 200);

            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Press ESC - Level Select", 310, 260);
            g.drawString("Press R - Retry Level", 330, 290);
        }

    }

}
