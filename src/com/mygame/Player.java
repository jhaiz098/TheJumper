package com.mygame;

//import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;


public class Player {
	
	private int x, y;
	private float veloX, veloY = 0;
	private float speed = 300;
	private boolean isJumping;
	
	private final float GRAVITY = 2500f;
	private final int JUMP_STRENGTH = -850;
	private final float MAX_FALL_SPEED = 600f;
	
	private BufferedImage spriteSheet;
	private BufferedImage currentFrame;
	private final int SPRITE_WIDTH = 32;
	private final int SPRITE_HEIGHT = 32;
	private final int SCALE = 3;
	
	
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
	
	public void update(double dt, java.util.List<Tile> map) {
	    // horizontal movement
	    x += veloX * dt;
	    for (Tile t : map) {
	        if (t.isSolid() && getBounds().intersects(t.getBounds())) {
	            if (veloX > 0) { // moving right
	                // move player so hitbox.right == tile.left
	                x = t.getBounds().x - (11*SCALE + 10*SCALE); // hitboxX + width
	            } else if (veloX < 0) { // moving left
	                // move player so hitbox.left == tile.right
	                x = t.getBounds().x - 11*SCALE + t.getBounds().width;
	            }
	            veloX = 0;
	        }
	    }

	    // vertical movement
	    veloY += GRAVITY * dt;
	    if (veloY > MAX_FALL_SPEED) veloY = MAX_FALL_SPEED;
	    y += veloY * dt;
	    for (Tile t : map) {
	        if (t.isSolid() && getBounds().intersects(t.getBounds())) {
	            if (veloY > 0) { // falling
	                y = t.getBounds().y - (13*SCALE + 15*SCALE); // hitboxY + height
	                veloY = 0;
	                isJumping = false;
	            } else if (veloY < 0) { // jumping up
	                y = t.getBounds().y - 13*SCALE + t.getBounds().height; // hitboxY + ...
	                veloY = 0;
	            }
	        }
	    }
	}

	
	public void draw(Graphics g) {
	    g.drawImage(currentFrame, x, y, SPRITE_WIDTH * SCALE, SPRITE_HEIGHT * SCALE, null);
	    
	    // draw hitbox in red
	    g.setColor(Color.RED);
	    g.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
	}

	
	public Rectangle getBounds() {
	    int hitboxX = x + 11 * SCALE;
	    int hitboxY = y + 13 * SCALE;
	    int hitboxWidth = 10 * SCALE;
	    int hitboxHeight = 15 * SCALE;

	    return new Rectangle(hitboxX, hitboxY, hitboxWidth, hitboxHeight);
	}
	
	public void isJumping(boolean bool) {
		this.isJumping = bool;
	}
}
