package com.mygame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Coin {
    private int x, y;
    private BufferedImage[] frames;
    private BufferedImage currentFrame;
    private int frameIndex;
    private long lastFrameTime;
    private final int FRAME_DELAY = 100;  // Milliseconds before changing frame
    private final int SPRITE_WIDTH = 16;  // Width of a frame
    private final int SPRITE_HEIGHT = 16; // Height of a frame
    private BufferedImage spriteSheet;

    public Coin(int x, int y, String spriteSheetPath) {
        this.x = x;
        this.y = y;
        this.frames = new BufferedImage[4];  // Assuming 4 frames for animation

        try {
            spriteSheet = ImageIO.read(getClass().getResource(spriteSheetPath));

            int sheetWidth = spriteSheet.getWidth();
            int sheetHeight = spriteSheet.getHeight();

            // Assuming all frames are in the first row (y = 0)
            for (int i = 0; i < 4; i++) {
                int xCoord = i * SPRITE_WIDTH;  // Change x-coordinate based on the column
                int yCoord = 0;  // Row is 0 since all frames are in the first row

                if (xCoord + SPRITE_WIDTH <= sheetWidth && yCoord + SPRITE_HEIGHT <= sheetHeight) {
                    frames[i] = spriteSheet.getSubimage(xCoord, yCoord, SPRITE_WIDTH, SPRITE_HEIGHT);
                } else {
                    System.out.println("Subimage out of bounds for frame: " + i);
                }
            }

            currentFrame = frames[0];  // Start with the first frame
        } catch (IOException e) {
            e.printStackTrace();
            currentFrame = new BufferedImage(SPRITE_WIDTH, SPRITE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        }
    }

    public void update() {
        // Update frame logic for animation
        if (System.currentTimeMillis() - lastFrameTime > FRAME_DELAY) {
            lastFrameTime = System.currentTimeMillis();
            frameIndex = (frameIndex + 1) % frames.length;
            currentFrame = frames[frameIndex];
        }
    }

    public void drawAt(Graphics g, int camX, int camY) {
        // Draw coin at its world coordinates with the camera offset applied
        g.drawImage(currentFrame, x - camX, y - camY, SPRITE_WIDTH * 3, SPRITE_HEIGHT * 3, null);  // Adjust scale as needed
        
        g.setColor(Color.RED);
        Rectangle hb = getBounds();
        g.drawRect(hb.x - camX, hb.y - camY, hb.width, hb.height);
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, SPRITE_WIDTH * 3, SPRITE_HEIGHT * 3);  // Adjust for scaling
    }

    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
