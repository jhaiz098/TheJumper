package com.mygame;


import static com.mygame.GameConstants.TILE;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Platform {

    private float x, y;
    private final float startX, startY;
    private final float endX, endY;
    
    private final int SCALE = 3;

    private int width, height;
    private BufferedImage sprite;

    private float speed;
    private boolean goingToEnd = true;

    private float lastX, lastY;

    public Platform(
            float startX, float startY,
            float endX,   float endY,
            int width, int height,
            BufferedImage sprite,
            float speed
    ) {
        this.x = startX * TILE;
        this.y = startY * TILE;

        this.startX = startX * TILE;
        this.startY = startY * TILE;
        this.endX = endX * TILE;
        this.endY = endY * TILE;

        this.width = width * SCALE;
        this.height = height * SCALE;
        this.sprite = sprite;
        this.speed = speed;
    }

    // 🔁 ping-pong movement
    public void update(double dt) {
        lastX = x;
        lastY = y;

        float targetX = goingToEnd ? endX : startX;
        float targetY = goingToEnd ? endY : startY;

        float dx = targetX - x;
        float dy = targetY - y;

        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance < 1f) {
            // reached target → swap direction
            goingToEnd = !goingToEnd;
            return;
        }

        // normalize
        float dirX = dx / distance;
        float dirY = dy / distance;

        x += dirX * speed * dt;
        y += dirY * speed * dt;
    }

    // 🎨 draw
    public void draw(Graphics g, int camX, int camY) {
        if (sprite != null) {
            g.drawImage(sprite,
                    (int) (x - camX),
                    (int) (y - camY),
                    width, height,
                    null);
        } else {
            g.setColor(Color.GRAY);
            g.fillRect(
                    (int) (x - camX),
                    (int) (y - camY),
                    width,
                    height
            );
        }
    }

    // 📦 collider
    public Rectangle getCollider() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    // 🚶 player riding support
    public float getDeltaX() {
        return x - lastX;
    }

    public float getDeltaY() {
        return y - lastY;
    }
}
