package com.mygame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;

public class Program extends JPanel {

	private Player player;
	
	public Program() {
		setPreferredSize(new Dimension(800,600));
		setBackground(Color.CYAN);
		
		player = new Player(0, 0, "/resources/sprites/knight.png");
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
