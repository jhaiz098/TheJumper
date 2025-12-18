package com.mygame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Arrow {

    private float x, y;
    private float vx, vy;
    private int width, height;
    private boolean active = true;

    private BufferedImage sprite;
    private double angle;

    public Arrow(
            float x, float y,
            float vx, float vy,
            BufferedImage sprite,
            int scale
    ) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.sprite = sprite;

        this.width = sprite.getWidth() * scale;
        this.height = sprite.getHeight() * scale;

        this.angle = Math.atan2(vy, vx);
    }

    public void update(double dt) {
        if (!active) return;

        x += vx * dt;
        y += vy * dt;
    }

    public void draw(Graphics g, int camX, int camY) {
        if (!active) return;

        Graphics2D g2 = (Graphics2D) g;

        AffineTransform old = g2.getTransform();
        g2.translate(x - camX + width / 2, y - camY + height / 2);
        g2.rotate(angle);
        g2.drawImage(sprite, -width / 2, -height / 2, width, height, null);
        g2.setTransform(old);
        
        g.setColor(Color.RED);
      Rectangle hb = getBounds();
      g.drawRect(hb.x - camX, hb.y - camY, hb.width, hb.height);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }
}
