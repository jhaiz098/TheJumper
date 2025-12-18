package com.mygame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Saw {

    // =========================
    // POSITION
    // =========================
    private float x, y;

    // =========================
    // SIZE
    // =========================
    private int spriteW, spriteH;
    private int scale;
    private int width, height;

    // =========================
    // ANIMATION
    // =========================
    private BufferedImage[] idleFrames;
    private BufferedImage[] moveFrames;
    private BufferedImage currentFrame;

    private int animIndex = 0;
    private long lastFrameTime = 0;
    private static final int FRAME_DELAY = 100;

    // =========================
    // PATROL POINTS
    // =========================
    private List<Point> patrolPoints = new ArrayList<>();
    private List<Float> speeds = new ArrayList<>();
    private int currentTarget = 0;

    // =========================
    // CONSTRUCTOR
    // =========================
    public Saw(
            List<Point> patrolPoints,
            List<Float> speeds,
            BufferedImage spriteSheet,
            int spriteW, int spriteH,
            int scale,
            int idleStart, int idleEnd,
            int moveStart, int moveEnd
    ) {
        if (patrolPoints.size() != speeds.size() || patrolPoints.size() < 2) {
            throw new IllegalArgumentException("Patrol points and speeds must match and have at least 2 points");
        }

        this.patrolPoints = patrolPoints;
        this.speeds = speeds;
        this.spriteW = spriteW;
        this.spriteH = spriteH;
        this.scale = scale;
        this.width = spriteW * scale;
        this.height = spriteH * scale;

        // Start at first patrol point
        Point start = patrolPoints.get(0);
        this.x = start.x;
        this.y = start.y;

        // Load animations
        idleFrames = extractFrames(spriteSheet, idleStart, idleEnd);
        moveFrames = extractFrames(spriteSheet, moveStart, moveEnd);
        currentFrame = idleFrames[0];
    }

    // =========================
    // FRAME EXTRACTION
    // =========================
    private BufferedImage[] extractFrames(BufferedImage sheet, int start, int end) {
        int columns = sheet.getWidth() / spriteW;
        int count = end - start + 1;
        BufferedImage[] frames = new BufferedImage[count];

        for (int i = 0; i < count; i++) {
            int frame = start + i;
            int col = frame % columns;
            int row = frame / columns;

            frames[i] = sheet.getSubimage(col * spriteW, row * spriteH, spriteW, spriteH);
        }
        return frames;
    }

    // =========================
    // UPDATE
    // =========================
    public void update(double dt) {
        Point target = patrolPoints.get(currentTarget);

        float dx = target.x - x;
        float dy = target.y - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        float speed = speeds.get(currentTarget);
        float moveDist = speed * (float)dt;

        if (distance <= moveDist) {
            // Snap to target
            x = target.x;
            y = target.y;
            // Move to next patrol point
            currentTarget = (currentTarget + 1) % patrolPoints.size();
        } else {
            // Move toward target
            float dirX = dx / distance;
            float dirY = dy / distance;
            x += dirX * moveDist;
            y += dirY * moveDist;
        }

        // Animate using current speed
        animate(moveFrames, speed / 50f);
    }



	 // =========================
	 // ANIMATE WITH SPEED
	 // =========================
	 private void animate(BufferedImage[] frames, float speed) {
	     // speed = 1 → normal speed, higher → faster
	     long adjustedDelay = (long)(FRAME_DELAY / speed);
	
	     if (System.currentTimeMillis() - lastFrameTime >= adjustedDelay) {
	         lastFrameTime = System.currentTimeMillis();
	         animIndex = (animIndex + 1) % frames.length;
	         currentFrame = frames[animIndex];
	     }
	 }


    // =========================
    // DRAW
    // =========================
    public void draw(Graphics g, int camX, int camY) {
        g.drawImage(currentFrame, (int)(x - camX), (int)(y - camY), width, height, null);
    }

    // =========================
    // COLLIDER
    // =========================
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
}
