package com.mygame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Program extends JPanel {

	private Player player;
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	
	public Program() {
		setPreferredSize(new Dimension(800,600));
		setBackground(Color.CYAN);
		
		player = new Player(0, 0, "/resources/sprites/knight.png");
		
		final double dt = 1.0/60.0;
		
		new Thread(() -> {
			
			while(true) {
				
				player.update(dt);
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
