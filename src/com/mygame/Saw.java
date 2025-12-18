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
    
    private boolean returningToStart = false;
    
    // =========================
    // SIZE
    // =========================
    private int spriteW, spriteH;
    private int scale;
    private int width, height;

    private boolean loop = true;        // loops patrol by default
    private boolean reactivate = false; // can re-trigger when using activation zone
    private boolean finished = false;   // reached end of path if not looping
    
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
    // TRIGGER
    // =========================
    private Rectangle activationZone; // optional trigger area
    private boolean active = true;    // default: normal saw

    // =========================
    // CONSTRUCTOR (normal saw)
    // =========================
    public Saw(List<Point> patrolPoints, List<Float> speeds, BufferedImage spriteSheet,
               int spriteW, int spriteH, int scale, int idleStart, int idleEnd,
               int moveStart, int moveEnd, boolean loop) {
        initSaw(patrolPoints, speeds, spriteSheet, spriteW, spriteH, scale, idleStart, idleEnd, moveStart, moveEnd);
        this.active = true;
        this.loop = loop;
    }

    // =========================
    // CONSTRUCTOR (triggered saw)
    // =========================
    public Saw(List<Point> patrolPoints, List<Float> speeds, BufferedImage spriteSheet,
            int spriteW, int spriteH, int scale, int idleStart, int idleEnd,
            int moveStart, int moveEnd, Rectangle activationZone, boolean loop, boolean reactivate) {
     initSaw(patrolPoints, speeds, spriteSheet, spriteW, spriteH, scale, idleStart, idleEnd, moveStart, moveEnd);
     this.activationZone = activationZone;
     this.active = false;
     this.loop = loop;
     this.reactivate = reactivate;
 }

    // =========================
    // COMMON INIT
    // =========================
    private void initSaw(
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

        Point start = patrolPoints.get(0);
        this.x = start.x;
        this.y = start.y;

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
    public void update(double dt, Rectangle playerBounds) {

        // Trigger check
        if (!active && activationZone != null) {
            if (playerBounds.intersects(activationZone)) {
                active = true;
                finished = false;
                returningToStart = false;
                currentTarget = 0;
            } else {
                animate(idleFrames, 1f);
                return;
            }
        }

        if (!active || finished) return;

        Point target = patrolPoints.get(currentTarget);

        float dx = target.x - x;
        float dy = target.y - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float speed = speeds.get(currentTarget);
        float moveDist = speed * (float) dt;

        if (distance <= moveDist) {
            x = target.x;
            y = target.y;

            // ===== NORMAL PATH =====
            if (!returningToStart) {
                if (currentTarget == patrolPoints.size() - 1) {
                    if (loop) {
                        currentTarget = 0;
                    } else {
                        // start returning to first point
                        returningToStart = true;
                        currentTarget = 0;
                    }
                } else {
                    currentTarget++;
                }
            }
            // ===== RETURNING TO START =====
            else {
                // reached first point → stop
                finished = true;
                active = false;
                returningToStart = false;
            }

        } else {
            float dirX = dx / distance;
            float dirY = dy / distance;
            x += dirX * moveDist;
            y += dirY * moveDist;
        }

        animate(moveFrames, speed / 50f);
    }



    // =========================
    // ANIMATE WITH SPEED
    // =========================
    private void animate(BufferedImage[] frames, float speed) {
        long adjustedDelay = (long) (FRAME_DELAY / speed);
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
        // Optional debug: show activation zone
        if (!active && activationZone != null) {
            g.setColor(new Color(255, 0, 0, 80));
            g.fillRect(activationZone.x - camX, activationZone.y - camY, activationZone.width, activationZone.height);
        }
    }

    // =========================
    // COLLIDER
    // =========================
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
    
    public void setActivationZone(Rectangle zone) {
        this.activationZone = zone;
    }

    private Rectangle getActivationZone() {
        if (activationZone != null) return activationZone;
        // Default: saw is always active if zone not set
        return new Rectangle((int)x, (int)y, width, height);
    }

}
