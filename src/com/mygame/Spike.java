package com.mygame;

import static com.mygame.GameConstants.TILE;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Spike {

    private int x, y;
    private int width, height;
    private BufferedImage sprite;

    private static final int COLLIDER_HEIGHT = 3; // pixels (sprite space)
    private static final int COLLIDER_Y = 13;
    private static final int SCALE = 3;

    public Spike(int x, int y, int width, int height, BufferedImage sprite) {
        this.x = x * TILE;
        this.y = y * TILE;
        this.width = width * SCALE;
        this.height = height * SCALE;
        this.sprite = sprite;
    }

    public void draw(Graphics g, int camX, int camY) {
        if (sprite != null) {
            g.drawImage(sprite,
                x - camX,
                y - camY,
                width,
                height,
                null
            );
        }

        // 🔴 DEBUG collider
//        Rectangle r = getBounds();
//        g.setColor(Color.RED);
//        g.drawRect(r.x - camX, r.y - camY, r.width, r.height);
//        
//        Rectangle hb = getBounds();
//        g.drawRect(hb.x - camX, hb.y - camY, hb.width, hb.height);
    }

    // ✅ Collider only at the TOP of the spike (3px)
    public Rectangle getBounds() {
        int colliderHeight = COLLIDER_HEIGHT * SCALE;

        return new Rectangle(
            x,
            y + 39,
            width,
            colliderHeight
        );
    }
}