package com.mygame;

//import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Player {
	
	private int x, y;
	private float veloX, veloY = 0;
	private float speed = 300;
	private boolean isJumping;
	
	private final float GRAVITY = 900f;
	private final int JUMP_STRENGTH = -450;
	private final int GROUND_LEVEL = 500;
	private final float MAX_FALL_SPEED = 500f;
	
	private BufferedImage spriteSheet;
	private BufferedImage currentFrame;
	private final int SPRITE_WIDTH = 32;
	private final int SPRITE_HEIGHT = 32;
	
	
	public Player(int x, int y, String spriteSheetPath) {
		this.x = x;
		this.y = y;
		this.veloX = 0;
		this.veloY = 0;
		this.isJumping = false;
		
		try {
            spriteSheet = ImageIO.read(getClass().getResource(spriteSheetPath));
            // Extract the first frame (top-left 32x32)
            currentFrame = spriteSheet.getSubimage(0, 0, SPRITE_WIDTH, SPRITE_HEIGHT);
        } catch (IOException e) {
            e.printStackTrace();
            currentFrame = new BufferedImage(SPRITE_WIDTH, SPRITE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        }	
	}
	
	public void moveLeft() { veloX = -speed; }
	public void moveRight() { veloX = speed; }
	public void stop() { veloX = 0; }
	public void jump() {
		if(!isJumping) {
			veloY = JUMP_STRENGTH;
			isJumping = true;
		}
	}
	
	public void update(double dt) {
		
        // apply gravity
        veloY += GRAVITY * dt;
        
        if (veloY > MAX_FALL_SPEED) {
        	veloY = MAX_FALL_SPEED;
        }
        
        // update pos
        x += veloX * dt;
        y += veloY * dt;

        // ground collision
        if (y >= GROUND_LEVEL) {
            y = GROUND_LEVEL;
            veloY = 0;
            isJumping = false;
        }
    }

	
	public void draw(Graphics g) {
		g.drawImage(currentFrame, x, y, SPRITE_WIDTH * 3, SPRITE_HEIGHT * 3, null);
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, SPRITE_WIDTH * 3, SPRITE_HEIGHT * 3);
	}
	
	public void isJumping(boolean bool) {
		this.isJumping = bool;
	}
}
