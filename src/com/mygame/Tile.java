package com.mygame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {

    public static final int TILE_SIZE = 16;  // raw pixel size from spritesheet
    public static final int SCALE = 3;
    public static final int PIXEL = TILE_SIZE * SCALE;

    public int tx, ty; // tile coordinates in the world
    BufferedImage sprite;
    private boolean solid;

    public Tile(int tx, int ty, BufferedImage sprite, boolean solid) {
        this.tx = tx;
        this.ty = ty;
        this.sprite = sprite;
        this.solid = solid;
    }

    public boolean isSolid() { return solid; }

    public int getScreenX() { return tx * PIXEL; }
    public int getScreenY() { return ty * PIXEL; }

    public Rectangle getBounds() {
        return new Rectangle(getScreenX(), getScreenY(), PIXEL, PIXEL);
    }

    public void draw(Graphics g, int camX, int camY) {
        g.drawImage(sprite, getScreenX() - camX, getScreenY() - camY, PIXEL, PIXEL, null);

        // optional: draw hitbox
        g.setColor(Color.RED);
        Rectangle hb = getBounds();
        g.drawRect(hb.x - camX, hb.y - camY, hb.width, hb.height);
    }
}
