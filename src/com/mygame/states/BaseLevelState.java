package com.mygame.states;

import com.mygame.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

public abstract class BaseLevelState implements GameState {

    protected List<Tile> map;

    public BaseLevelState() {
        map = new ArrayList<>();
    }

    protected void addTile(int tx, int ty, int tileIndex, int zIndex, boolean solid) {
        BufferedImage[] tileset = TileLoader.loadTiles("/resources/sprites/world_tileset.png", 16, 16);
        if (tileset != null && tileset.length > tileIndex) {
            map.add(new Tile(tx, ty, tileset[tileIndex], zIndex, solid));
        }
    }


    protected void renderTiles(Graphics g, int camX, int camY) {
        // sort tiles by z-index (lowest drawn first)
        map.sort((a, b) -> Integer.compare(a.getZ(), b.getZ()));

        for (Tile t : map) {
            t.draw(g, camX, camY);
        }
    }

    @Override
    public void keyPressed(int key) {}

    @Override
    public void keyReleased(int key) {}

    @Override
    public void update(double dt) {}

    @Override
    public void render(Graphics g) {}
}
