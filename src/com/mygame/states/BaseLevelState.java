package com.mygame.states;

import com.mygame.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

public abstract class BaseLevelState implements GameState {

    protected List<Tile> map;
    protected List<Coin> coins;  // List of coins in the level

    private int cachedOriginX = 0;
    private int cachedOriginY = 0;

    private BufferedImage cachedMap = null;
    private boolean needsRedraw = true;

    public BaseLevelState() {
        map = new ArrayList<>();
        coins = new ArrayList<>();  // Initialize the coins list
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
    }

    @Override
    public void keyPressed(int key) {}
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
        for (Coin coin : coins) {
            coin.drawAt(g, 0, 0);  // Draw each coin
        }
    }
}
