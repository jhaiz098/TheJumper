package com.mygame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Tile {
    public static final int SCALE = 3; // tile scaling factor

    private int x, y;
    private BufferedImage sprite;
    private boolean solid; // true = player collides

    public Tile(int x, int y, BufferedImage sprite, boolean solid) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.solid = solid;
    }

    public void draw(Graphics g) {
        g.drawImage(sprite, x, y, sprite.getWidth() * SCALE, sprite.getHeight() * SCALE, null);
        
        g.setColor(Color.RED);
	    g.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, sprite.getWidth() * SCALE, sprite.getHeight() * SCALE);
    }

    public boolean isSolid() {
        return solid;
    }
}