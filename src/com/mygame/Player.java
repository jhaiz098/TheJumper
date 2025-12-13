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
    private boolean canJump;

    private final float GRAVITY = 2500f;
    private final int JUMP_STRENGTH = -900;
    private final float MAX_FALL_SPEED = 600f;

    private BufferedImage spriteSheet;
    private BufferedImage[] walkFrames;
    private BufferedImage[] idleFrames;
    private BufferedImage currentFrame;
    
    private int walkFrameIndex = 0; // separate index for walk animation
    private int idleFrameIndex = 0; // separate index for idle animation
    private long lastFrameTime = 0;
    private final int FRAME_DELAY = 100;  // Milliseconds to wait before changing frame

    private final int SPRITE_WIDTH = 32;
    private final int SPRITE_HEIGHT = 32;
    private final int SCALE = 3;

    // track movement keys internally
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    
    // Track if the player is facing left or right
    private boolean facingLeft = false;

    public Player(int x, int y, String spriteSheetPath) {
        this.x = x;
        this.y = y;
        this.veloX = 0;
        this.veloY = 0;
        this.isJumping = false;

        try {
            spriteSheet = ImageIO.read(getClass().getResource(spriteSheetPath));

            // Load walking frames
            walkFrames = new BufferedImage[16];  
            int frameIndex = 0;  // This will help in filling the walkFrames array correctly.

            for (int o = 2; o < 4; o++) {  // Rows 2 and 3 (y = 2, 3)
                for (int i = 0; i < 8; i++) {  // Columns 0-7 (x = 0 to 7)
                    walkFrames[frameIndex] = spriteSheet.getSubimage(i * SPRITE_WIDTH, o * SPRITE_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
                    frameIndex++;  // Increment to the next frame index
                }
            }


            // Load idle frames
            idleFrames = new BufferedImage[4]; 
            for (int i = 0; i < 4; i++) {
                idleFrames[i] = spriteSheet.getSubimage(i * SPRITE_WIDTH, 0, SPRITE_WIDTH, SPRITE_HEIGHT);
            }
            
            currentFrame = idleFrames[0];  // Set the initial frame to idle
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

    public void update(double dt, List<Coin> coins, List<Tile> map) {
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

        // Update facing direction
        if (veloX > 0) facingLeft = false; // facing right
        else if (veloX < 0) facingLeft = true; // facing left

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

        // Check for collisions with coins
        for (Coin coin : coins) {
            if (getBounds().intersects(coin.getBounds())) {
                // If collision detected, remove the coin from the list
                coins.remove(coin);
                break;  // Remove only one coin per frame
            }
        }
        
        // Animation logic: change frame based on movement
        if (System.currentTimeMillis() - lastFrameTime > FRAME_DELAY) {
            lastFrameTime = System.currentTimeMillis();

            if (isJumping) {
                // If jumping, show the jumping frame (frame 0 from row 2)
                currentFrame = spriteSheet.getSubimage(0, 2 * SPRITE_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
            } else {
                // If not jumping, handle walking or idle frames
                if (veloX != 0) {
                    // Walking animation: Loop through walk frames
                    currentFrame = walkFrames[walkFrameIndex];
                    walkFrameIndex = (walkFrameIndex + 1) % walkFrames.length;  // Loop through walk frames (index 0-7)
                } else {
                    // Idle animation: Loop through idle frames
                    currentFrame = idleFrames[idleFrameIndex];
                    idleFrameIndex = (idleFrameIndex + 1) % idleFrames.length;  // Loop through idle frames (index 0-3)
                }
            }
        }
    }


    public void drawAt(Graphics g, int camX, int camY) {
        // Flip the sprite if the player is facing left
        Graphics2D g2d = (Graphics2D) g;
        if (facingLeft) {
            g2d.drawImage(currentFrame, x - camX + SPRITE_WIDTH * SCALE, y - camY, -SPRITE_WIDTH * SCALE, SPRITE_HEIGHT * SCALE, null);
        } else {
            g2d.drawImage(currentFrame, x - camX, y - camY, SPRITE_WIDTH * SCALE, SPRITE_HEIGHT * SCALE, null);
        }

        g.setColor(Color.RED);
        Rectangle hb = getBounds();
//        g.drawRect(hb.x - camX, hb.y - camY, hb.width, hb.height);
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
