package com.mygame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.List;
import java.awt.event.KeyEvent;

public class Player {

    private int x, y;
    private float veloX, veloY;
    private float speed = 300;
    private boolean isJumping;

    private final float GRAVITY = 2500f;
    private final int JUMP_STRENGTH = -900;
    private final float MAX_FALL_SPEED = 600f;

    private BufferedImage spriteSheet;
    private BufferedImage currentFrame;
    private final int SPRITE_WIDTH = 32;
    private final int SPRITE_HEIGHT = 32;
    private final int SCALE = 3;

    // track movement keys internally
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public Player(int x, int y, String spriteSheetPath) {
        this.x = x;
        this.y = y;
        this.veloX = 0;
        this.veloY = 0;
        this.isJumping = false;

        try {
            spriteSheet = ImageIO.read(getClass().getResource(spriteSheetPath));
            currentFrame = spriteSheet.getSubimage(0, 0, SPRITE_WIDTH, SPRITE_HEIGHT);
        } catch (IOException e) {
            e.printStackTrace();
            currentFrame = new BufferedImage(SPRITE_WIDTH, SPRITE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        }
    }

    // input handling
    public void keyPressed(int key) {
        if (key == KeyEvent.VK_LEFT) leftPressed = true;
        if (key == KeyEvent.VK_RIGHT) rightPressed = true;
        if (key == KeyEvent.VK_SPACE) jump();
        updateVelocity();
    }

    public void keyReleased(int key) {
        if (key == KeyEvent.VK_LEFT) leftPressed = false;
        if (key == KeyEvent.VK_RIGHT) rightPressed = false;
        updateVelocity();
    }

    private void updateVelocity() {
        if (leftPressed && !rightPressed) veloX = -speed;
        else if (rightPressed && !leftPressed) veloX = speed;
        else veloX = 0;
    }

    public void jump() {
        if (!isJumping) {
            veloY = JUMP_STRENGTH;
            isJumping = true;
        }
    }

    public void update(double dt, List<Tile> map) {
        // Horizontal movement
        x += veloX * dt;

        if (map != null) {
            for (Tile t : map) {
                if (t.isSolid() && getBounds().intersects(t.getBounds())) {
                    if (veloX > 0) x = t.getBounds().x - (11 * SCALE + 10 * SCALE); // hitbox right
                    else if (veloX < 0) x = t.getBounds().x - 11 * SCALE + t.getBounds().width; // hitbox left
                    veloX = 0;
                }
            }
        }

        // Vertical movement
        veloY += GRAVITY * dt;
        if (veloY > MAX_FALL_SPEED) veloY = MAX_FALL_SPEED;
        y += veloY * dt;

        if (map != null) {
            for (Tile t : map) {
                if (t.isSolid() && getBounds().intersects(t.getBounds())) {
                    if (veloY > 0) { // falling
                        y = t.getBounds().y - (13 * SCALE + 15 * SCALE); // hitbox bottom
                        veloY = 0;
                        isJumping = false;
                    } else if (veloY < 0) { // jumping
                        y = t.getBounds().y - 13 * SCALE + t.getBounds().height; // hitbox top
                        veloY = 0;
                    }
                }
            }
        }
    }


    public void drawAt(Graphics g, int camX, int camY) {
        g.drawImage(currentFrame, x - camX, y - camY, SPRITE_WIDTH * SCALE, SPRITE_HEIGHT * SCALE, null);

        g.setColor(Color.RED);
        Rectangle hb = getBounds();
        g.drawRect(hb.x - camX, hb.y - camY, hb.width, hb.height);
    }

    public Rectangle getBounds() {
        int hitboxX = x + 11 * SCALE;
        int hitboxY = y + 13 * SCALE;
        int hitboxWidth = 10 * SCALE;
        int hitboxHeight = 15 * SCALE;
        return new Rectangle(hitboxX, hitboxY, hitboxWidth, hitboxHeight);
    }
    
    // Getters for camera
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return SPRITE_WIDTH * SCALE; }
    public int getHeight() { return SPRITE_HEIGHT * SCALE; }
}
