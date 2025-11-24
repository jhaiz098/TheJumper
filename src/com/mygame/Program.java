package com.mygame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class Program extends JPanel {

	private Player player;
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	
	
	private BufferedImage[] tileImages;
	private List<Tile> map = new ArrayList<>();
	
	private final int[][] level = {
	        {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25},
	        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	        {0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
	        {0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0},
	        {0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
	    };
	
	public Program() {
		setPreferredSize(new Dimension(800,600));
		setBackground(Color.CYAN);
		
		player = new Player(0, 0, "/resources/sprites/knight.png");
		
		tileImages = TileLoader.loadTiles("/resources/sprites/world_tileset.png", 16, 16);
		
		if (tileImages != null) {
		    for (int row = 0; row < level.length; row++) {
		        for (int col = 0; col < level[row].length; col++) {
		            int tileIndex = level[row][col];
		            if (tileIndex > 0) {
		                boolean solid = (tileIndex == 1); // only tile type 1 is solid
		                map.add(new Tile(col * 16 * Tile.SCALE, row * 16 * Tile.SCALE, tileImages[tileIndex - 1], solid));
		            }
		        }
		    }
		}
		
		final double dt = 1.0/60.0;
		
		new Thread(() -> {
			
			while(true) {
				
				player.update(dt, map);
				repaint();
				
				try { Thread.sleep(16); } catch (Exception e) {}
			}
		}).start();
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
					case KeyEvent.VK_LEFT -> leftPressed = true;
					case KeyEvent.VK_RIGHT -> rightPressed = true;
					case KeyEvent.VK_SPACE -> player.jump();
				}
				updatePlayerVelocity();
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT -> leftPressed = false;
				case KeyEvent.VK_RIGHT -> rightPressed = false;
				}
				updatePlayerVelocity();
			}
			
			private void updatePlayerVelocity() {
				if (leftPressed && !rightPressed) {
		            player.moveLeft();
		        } else if (rightPressed && !leftPressed) {
		            player.moveRight();
		        } else {
		            player.stop();
		        }
			}
		});
		
		setFocusable(true);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for (Tile t : map) {
			t.draw(g);
		}
		
		player.draw(g);
		
	}
	
	
	public static void main(String[] args) {
		JFrame window = new JFrame("The Jumper");
		Program panel = new Program();
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(panel);
		window.pack();
		window.setLocationRelativeTo(null);
        window.setResizable(false);
		window.setVisible(true);
	}

}
