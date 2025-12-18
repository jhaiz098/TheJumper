package com.mygame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ArrowTrap {

    // =========================
    // ENUM
    // =========================
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

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
    // BEHAVIOR
    // =========================
    private Direction direction;
    private float detectionRange;
    private float bulletSpeed;
    private float fireCooldown;
    private float fireTimer = 0f;

    // =========================
    // ANIMATION
    // =========================
    private BufferedImage[] idleFrames;
    private BufferedImage[] shootFrames;
    private BufferedImage currentFrame;

    private int animIndex = 0;
    private long lastFrameTime = 0;
    private static final int FRAME_DELAY = 120;

    private boolean shooting = false;

    // =========================
    // PROJECTILES
    // =========================
    private BufferedImage arrowSprite;
    private ArrayList<Arrow> arrows = new ArrayList<>();

    // =========================
    // DEBUG
    // =========================
    private boolean debug = true;

    // =========================
    // CONSTRUCTOR
    // =========================
    public ArrowTrap(
            int x, int y,
            Direction direction,
            float detectionRange,
            float bulletSpeed,
            float fireCooldown,

            BufferedImage trapSheet,
            int spriteW, int spriteH,
            int scale,

            int idleStart, int idleEnd,
            int shootStart, int shootEnd,

            BufferedImage arrowSprite
    ) {
        this.x = x;
        this.y = y;

        this.direction = direction;
        this.detectionRange = detectionRange;
        this.bulletSpeed = bulletSpeed;
        this.fireCooldown = fireCooldown;

        this.spriteW = spriteW;
        this.spriteH = spriteH;
        this.scale = scale;
        this.width = spriteW * scale;
        this.height = spriteH * scale;

        this.arrowSprite = arrowSprite;

        loadFrames(trapSheet, idleStart, idleEnd, shootStart, shootEnd);
        currentFrame = idleFrames[0];
    }

    // =========================
    // LOAD FRAMES
    // =========================
    private void loadFrames(
            BufferedImage sheet,
            int idleStart, int idleEnd,
            int shootStart, int shootEnd
    ) {
        int columns = sheet.getWidth() / spriteW;

        idleFrames = extractFrames(sheet, idleStart, idleEnd, columns);
        shootFrames = extractFrames(sheet, shootStart, shootEnd, columns);
    }

    private BufferedImage[] extractFrames(
            BufferedImage sheet,
            int start, int end,
            int columns
    ) {
        int count = end - start + 1;
        BufferedImage[] frames = new BufferedImage[count];

        for (int i = 0; i < count; i++) {
            int frame = start + i;
            int col = frame % columns;
            int row = frame / columns;

            frames[i] = sheet.getSubimage(
                    col * spriteW,
                    row * spriteH,
                    spriteW,
                    spriteH
            );
        }
        return frames;
    }

    // =========================
    // UPDATE
    // =========================
    public void update(double dt, Rectangle playerBounds) {

        fireTimer += dt;

        boolean playerDetected = getDetectionBox().intersects(playerBounds);

        if (playerDetected && fireTimer >= fireCooldown) {
            shooting = true;
            fireArrow();
            fireTimer = 0;
            animIndex = 0;
        }

        if (shooting) {
            animate(shootFrames);

            if (animIndex == shootFrames.length - 1) {
                shooting = false;
                animIndex = 0;
            }
        } else {
            animate(idleFrames);
        }

        for (int i = 0; i < arrows.size(); i++) {
            Arrow a = arrows.get(i);
            a.update(dt);

            // remove inactive arrows
            if (!a.isActive()) {
                arrows.remove(i);
                i--;
            }
        }

    }

    // =========================
    // FIRE
    // =========================
    private void fireArrow() {

        float vx = 0, vy = 0;

        switch (direction) {
            case RIGHT -> vx = bulletSpeed;
            case LEFT -> vx = -bulletSpeed;
            case UP -> vy = -bulletSpeed;
            case DOWN -> vy = bulletSpeed;
        }

        arrows.add(new Arrow(
                x + width / 2,
                y + height / 2 - ((height/2) / 2),
//                x,
//                y,
                vx,
                vy,
                arrowSprite,
                scale
        ));
    }

    // =========================
    // ANIMATION
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

        Graphics2D g2 = (Graphics2D) g;

        double angle = switch (direction) {
            case RIGHT -> 0;
            case DOWN -> Math.PI / 2;
            case LEFT -> Math.PI;
            case UP -> -Math.PI / 2;
        };

        AffineTransform old = g2.getTransform();
        g2.translate(x - camX + width / 2, y - camY + height / 2);
        g2.rotate(angle);
        g2.drawImage(currentFrame, -width / 2, -height / 2, width, height, null);
        g2.setTransform(old);

        if (debug) {
            Rectangle r = getDetectionBox();
            g2.setColor(new Color(255, 0, 0, 80));
            g2.fillRect(r.x - camX, r.y - camY, r.width, r.height);
        }

        for (Arrow a : arrows) {
            a.draw(g, camX, camY);
        }
    }

    // =========================
    // DETECTION BOX
    // =========================
    private Rectangle getDetectionBox() {
        return switch (direction) {
            case RIGHT -> new Rectangle((int)(x + width), (int)y, (int)detectionRange, height);
            case LEFT -> new Rectangle((int)(x - detectionRange), (int)y, (int)detectionRange, height);
            case UP -> new Rectangle((int)x, (int)(y - detectionRange), width, (int)detectionRange);
            case DOWN -> new Rectangle((int)x, (int)(y + height), width, (int)detectionRange);
        };
    }
    
	 // =========================
	 // COLLISION WITH PLAYER
	 // =========================
	 public boolean checkPlayerHit(Rectangle playerBounds) {
	
	     for (Arrow a : arrows) {
	         if (!a.isActive()) continue;
	
	         if (a.getBounds().intersects(playerBounds)) {
	             a.deactivate(); // remove arrow after hit
	             return true;    // player was hit
	         }
	     }
	     return false;
	 }

}
