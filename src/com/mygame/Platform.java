package com.mygame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Platform {
    private int x, y;
    private int width, height;
    private BufferedImage sprite;
    private Rectangle collider;

    public Platform(int x, int y, int width, int height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
        this.collider = new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g, int camX, int camY) {
        if (sprite != null) {
            g.drawImage(sprite, x - camX, y - camY, width, height, null);
        }

        // Debug hitbox
//        g.setColor(Color.RED);
//        g.drawRect(collider.x - camX, collider.y - camY, collider.width, collider.height);
    }

    public Rectangle getCollider() {
        return collider;
    }

    public void setPosition(int newX, int newY) {
        x = newX;
        y = newY;
        collider.setLocation(newX, newY);
    }
}
