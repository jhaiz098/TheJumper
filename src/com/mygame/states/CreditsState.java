package com.mygame.states;

import com.mygame.Button;
import com.mygame.GameState;
import com.mygame.GameStateManager;
import com.mygame.Sound;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class CreditsState implements GameState {

	private final GameStateManager gsm;
	private Button backButton;
	private BufferedImage bgImage;
	private Sound music;

	private int scrollY = 0;
	private final int MAX_SCROLL = 0;
	private final int MIN_SCROLL = -220;

	public CreditsState(GameStateManager gsm) {
	    this.gsm = gsm;

	    // Music (start ONLY once per state)
	    music = new Sound("/resources/music/time_for_adventure.wav");
	    music.loop();

	    try {
	        bgImage = ImageIO.read(
	            getClass().getResourceAsStream("/resources/sprites/title_bg.png")
	        );

	        BufferedImage backNormal = ImageIO.read(
	            getClass().getResourceAsStream("/resources/sprites/backButton.png")
	        );
	        BufferedImage backHover = ImageIO.read(
	            getClass().getResourceAsStream("/resources/sprites/backButtonHovered.png")
	        );

	        backButton = new Button(20, 20, backNormal, backHover, 4);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private void stopMusic() {
	    if (music != null) {
	        music.stop();
	        music = null;
	    }
	}


    @Override
    public void update(double dt) {}

    @Override
    public void render(Graphics g) {
        g.drawImage(bgImage, 0, 0, 900, 600, null);

        // Dark overlay
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, 900, 600);

        // Title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("CREDITS", 360, 80);

        int x = 260;
        int y = 150 + scrollY;
        int line = 30;

        g.setFont(new Font("Arial", Font.BOLD, 20));

        g.drawString("GAME DEVELOPMENT", x, y);
        y += line * 2;
        
        g.drawString("GROUP MEMBERS", x, y);
        y += line;

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        
        g.drawString("Fernandez, James Emmanuel P.", x, y);
        y += line;

        g.drawString("Marcos, Ashlee Sophia L.", x, y);
        y += line;

        g.drawString("Calzita, Ethan Gabriel C.", x, y);
        y += line;

        g.drawString("Vallar, LLamado T.", x, y);
        y += line * 2;

        g.setFont(new Font("Arial", Font.BOLD, 20));
        
        g.drawString("COURSE / YEAR / SECTION", x, y);
        y += line;

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        
        g.drawString("IT 373A - Event Driven Programming", x, y);
        y += line;
        
        g.drawString("BSIT 3A", x, y);
        y += line;

        g.drawString("Eastern Visayas State University", x, y);
        y += line * 2;
        
        g.setFont(new Font("Arial", Font.BOLD, 20));
        
        g.drawString("PROFESSOR", x, y);
        y += line;
        
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        
        g.drawString("PAÑA, WIT EXCELSIOR T.", x, y);
        y += line * 2;

        g.setFont(new Font("Arial", Font.BOLD, 20));
        
        g.drawString("TOOLS & TECHNOLOGIES", x, y);
        y += line;

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        
        g.drawString("Java, AWT, Custom Game Engine", x, y);
        y += line * 2;

        g.setFont(new Font("Arial", Font.BOLD, 30));
        
        g.drawString("THANK YOU FOR PLAYING!", x, y);

        // Scroll hint
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Use ↑ ↓ to scroll", 360, 560);

        backButton.draw(g);
    }

    @Override
    public void mouseMoved(int mx, int my) {
        backButton.updateHover(mx, my);
    }

    @Override
    public void mousePressed(int mx, int my) {
        if (backButton.isClicked(mx, my)) {
            stopMusic();
            gsm.setState(GameStateManager.TITLE);
        }
    }

    @Override
    public void keyPressed(int key) {
        if (key == KeyEvent.VK_ESCAPE) {
            stopMusic();
            gsm.setState(GameStateManager.TITLE);
        }

        if (key == KeyEvent.VK_DOWN) {
            scrollY -= 20;
            if (scrollY < MIN_SCROLL) scrollY = MIN_SCROLL;
        }

        if (key == KeyEvent.VK_UP) {
            scrollY += 20;
            if (scrollY > MAX_SCROLL) scrollY = MAX_SCROLL;
        }
    }


    @Override
    public void keyReleased(int key) {}
}
