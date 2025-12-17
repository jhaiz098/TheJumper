package com.mygame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy {

    public enum State {
        IDLE,
        MOVING
    }

    public enum PatrolType {
        HORIZONTAL,
        VERTICAL
    }

    private float x, y;
    private final float startX, startY;
    private final float endX, endY;

    private final int spriteW, spriteH;
    private final int scale;
    private final int width, height;

    private final float speed;
    private final float idleDuration;
    private float idleTimer = 0f;

    private boolean goingToEnd = true;

    private State state = State.MOVING;
    private final PatrolType patrolType;

    private BufferedImage[] idleFrames;
    private BufferedImage[] moveFrames;
    private BufferedImage currentFrame;

    private int animIndex = 0;
    private long lastFrameTime = 0;
    private static final int FRAME_DELAY = 120;

    public Enemy(
            float startX, float startY,
            float endX, float endY,
            int spriteW, int spriteH,
            int scale,
            BufferedImage spriteSheet,
            int idleFrameCount,
            int moveFrameCount,
            PatrolType patrolType,
            float speed,
            float idleDuration
    ) {
        this.x = startX;
        this.y = startY;

        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

        this.spriteW = spriteW;
        this.spriteH = spriteH;
        this.scale = scale;

        this.width = spriteW * scale;
        this.height = spriteH * scale;

        this.speed = speed;
        this.idleDuration = idleDuration;
        this.patrolType = patrolType;

        loadAnimations(spriteSheet, idleFrameCount, moveFrameCount);
        currentFrame = idleFrames[0];
    }

    // 🎞 Load idle & move frames
    private void loadAnimations(BufferedImage sheet, int idleCount, int moveCount) {
        idleFrames = new BufferedImage[idleCount];
        moveFrames = new BufferedImage[moveCount];

        for (int i = 0; i < idleCount; i++) {
            idleFrames[i] = sheet.getSubimage(
                    i * spriteW, 0,
                    spriteW, spriteH
            );
        }

        for (int i = 0; i < moveCount; i++) {
            moveFrames[i] = sheet.getSubimage(
                    i * spriteW, spriteH,
                    spriteW, spriteH
            );
        }
    }

    // 🔁 Update enemy logic
    public void update(double dt) {

        if (state == State.IDLE) {
            idleTimer += dt;
            animate(idleFrames);

            if (idleTimer >= idleDuration) {
                idleTimer = 0;
                animIndex = 0;
                state = State.MOVING;
                goingToEnd = !goingToEnd;
            }
            return;
        }

        // MOVING
        float targetX = goingToEnd ? endX : startX;
        float targetY = goingToEnd ? endY : startY;

        float dx = targetX - x;
        float dy = targetY - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance < 1f) {
            state = State.IDLE;
            animIndex = 0;
            return;
        }

        float dirX = dx / distance;
        float dirY = dy / distance;

        if (patrolType == PatrolType.HORIZONTAL) {
            x += dirX * speed * dt;
        } else {
            y += dirY * speed * dt;
        }

        animate(moveFrames);
    }

    // 🎞 Animation handler
    private void animate(BufferedImage[] frames) {
        if (System.currentTimeMillis() - lastFrameTime >= FRAME_DELAY) {
            lastFrameTime = System.currentTimeMillis();
            animIndex = (animIndex + 1) % frames.length;
            currentFrame = frames[animIndex];
        }
    }

    // 🎨 Draw
    public void draw(Graphics g, int camX, int camY) {
        g.drawImage(
                currentFrame,
                (int) (x - camX),
                (int) (y - camY),
                width,
                height,
                null
        );
    }

    // 📦 Collider
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
