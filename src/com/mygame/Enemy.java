package com.mygame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy {

    // =========================
    // ENUMS
    // =========================
    public enum State { IDLE, MOVING }
    public enum PatrolType { HORIZONTAL, VERTICAL }

    // =========================
    // POSITION
    // =========================
    private float x, y;
    private float startX, startY;
    private float endX, endY;

    // =========================
    // SIZE
    // =========================
    private int spriteW, spriteH;
    private int scale;
    private int width, height;

    // =========================
    // MOVEMENT
    // =========================
    private float speed;
    private PatrolType patrolType;
    private boolean goingToEnd = true;

    // =========================
    // STATE
    // =========================
    private State state = State.MOVING;
    private float idleDuration;
    private float idleTimer = 0f;

    // =========================
    // ANIMATION
    // =========================
    private BufferedImage[] idleFrames;
    private BufferedImage[] moveFrames;
    private BufferedImage currentFrame;

    private int animIndex = 0;
    private long lastFrameTime = 0;
    private static final int FRAME_DELAY = 120;

    // =========================
    // FACING
    // =========================
    private int defaultFacing;  // 1 = right, -1 = left
    private int facing = 1;     // current facing

    // =========================
    // CONSTRUCTOR
    // =========================
    public Enemy(
            int startX, int startY,
            int endX, int endY,
            int spriteW, int spriteH,
            int scale,
            BufferedImage sheet,
            int idleStart, int idleEnd,
            int moveStart, int moveEnd,
            PatrolType patrolType,
            float speed,
            float idleDuration,
            int defaultFacing // 1 or -1
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

        this.patrolType = patrolType;
        this.speed = speed;
        this.idleDuration = idleDuration;

        this.defaultFacing = (defaultFacing >= 0) ? 1 : -1;
        this.facing = this.defaultFacing;

        if (idleEnd < idleStart || moveEnd < moveStart) {
            throw new IllegalArgumentException("Invalid animation frame range");
        }

        loadFrames(sheet, idleStart, idleEnd, moveStart, moveEnd);
        currentFrame = idleFrames[0];
    }

    // =========================
    // LOAD ANIMATION FRAMES
    // =========================
    private void loadFrames(BufferedImage sheet, int idleStart, int idleEnd, int moveStart, int moveEnd) {
        int columns = sheet.getWidth() / spriteW;
        int idleCount = idleEnd - idleStart + 1;
        int moveCount = moveEnd - moveStart + 1;

        idleFrames = new BufferedImage[idleCount];
        moveFrames = new BufferedImage[moveCount];

        for (int i = 0; i < idleCount; i++) {
            int frame = idleStart + i;
            int col = frame % columns;
            int row = frame / columns;
            idleFrames[i] = sheet.getSubimage(col * spriteW, row * spriteH, spriteW, spriteH);
        }

        for (int i = 0; i < moveCount; i++) {
            int frame = moveStart + i;
            int col = frame % columns;
            int row = frame / columns;
            moveFrames[i] = sheet.getSubimage(col * spriteW, row * spriteH, spriteW, spriteH);
        }
    }

    // =========================
    // UPDATE
    // =========================
    public void update(double dt) {

        if (state == State.IDLE) {
            idleTimer += dt;
            animate(idleFrames);
            returnToMoveIfNeeded();
            return;
        }

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

        // Update facing if horizontal
        if (patrolType == PatrolType.HORIZONTAL) {
            facing = (dx >= 0) ? defaultFacing : -defaultFacing;
            x += (dx / distance) * speed * dt;
        } else {
            y += (dy / distance) * speed * dt;
            // optional: flip vertically if using a vertical sprite
        }

        animate(moveFrames);
    }

    private void returnToMoveIfNeeded() {
        idleTimer += 1; // you can use dt here if passed
        if (idleTimer >= idleDuration) {
            idleTimer = 0;
            animIndex = 0;
            state = State.MOVING;
            goingToEnd = !goingToEnd;
        }
    }

    // =========================
    // ANIMATION HANDLER
    // =========================
    private void animate(BufferedImage[] frames) {
        if (System.currentTimeMillis() - lastFrameTime >= FRAME_DELAY) {
            lastFrameTime = System.currentTimeMillis();
            animIndex = (animIndex + 1) % frames.length;
            currentFrame = frames[animIndex];
        }
    }

    // =========================
    // DRAW
    // =========================
    public void draw(Graphics g, int camX, int camY) {
        Graphics2D g2d = (Graphics2D) g;
        int drawX = (int) (x - camX);
        int drawY = (int) (y - camY);

        if (facing == 1) {
            g2d.drawImage(currentFrame, drawX, drawY, width, height, null);
        } else {
            // flip horizontally
            g2d.drawImage(currentFrame, drawX + width, drawY, -width, height, null);
        }
    }

    // =========================
    // COLLIDER
    // =========================
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
