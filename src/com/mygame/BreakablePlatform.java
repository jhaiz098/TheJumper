package com.mygame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakablePlatform extends Platform {

    private boolean triggered = false;
    private boolean broken = false;
    private float shakeTime = 0f;
    private float breakDelay = 2f;
    private float shakeAmplitude = 3f;

    public BreakablePlatform(float startX, float startY, int width, int height,
                             BufferedImage sprite, float breakDelay) {
        super(startX, startY, startX, startY, width, height, sprite, 0f);
        this.breakDelay = breakDelay;
    }

    public void trigger() {
        if (!triggered && !broken) triggered = true;
    }

    @Override
    public void update(double dt) {
        super.update(dt);

        if (triggered && !broken) {
            shakeTime += dt;
            if (shakeTime >= breakDelay) broken = true;
        }
    }

    @Override
    public void draw(Graphics g, int camX, int camY) {
        if (!broken) {
            int shakeOffset = 0;
            if (triggered) shakeOffset = (int)(Math.sin(shakeTime * 35) * shakeAmplitude);

            g.translate(shakeOffset, 0);
            super.draw(g, camX, camY);
            g.translate(-shakeOffset, 0);
        }
    }

    @Override
    public Rectangle getCollider() {
        if (broken) return new Rectangle(0,0,0,0);
        return super.getCollider();
    }

    public boolean isPlayerOn(Rectangle playerBounds) {
        return !broken && super.getCollider().intersects(playerBounds);
    }
}
