package com.mygame.states;

import com.mygame.Button;
import com.mygame.GameState;
import com.mygame.GameStateManager;
import com.mygame.Sound;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class TitleState implements GameState {
    private final GameStateManager gsm;
    
    private Button playButton;
    private Button howToPlayButton;
    private Button creditsButton;
    private Button quitButton;
    
    private Sound music;
    
    private BufferedImage bgImage = null;
    
    public TitleState(GameStateManager gsm) {
    	this.gsm = gsm;
    	
    	music = new Sound("/resources/music/time_for_adventure.wav");

    	if (music != null && !music.isPlaying()) {
            music.loop();
//            System.out.println("Menu music started");
        }
    	
    	BufferedImage playButtonSprite = null,
    			howToPlayButtonSprite = null,
    			creditsButtonSprite = null,
    			quitButtonSprite = null,
    			
    			playHoveredButtonSprite = null,
    			howToPlayHoveredButtonSprite = null,
    			creditsHoveredButtonSprite = null,
    			quitHoveredButtonSprite = null;
        try {
        	playButtonSprite = ImageIO.read(getClass().getResourceAsStream("/resources/sprites/playButton.png"));
            howToPlayButtonSprite = ImageIO.read(getClass().getResourceAsStream("/resources/sprites/howToPlayButton.png"));
        	creditsButtonSprite = ImageIO.read(getClass().getResourceAsStream("/resources/sprites/creditsButton.png"));
            quitButtonSprite = ImageIO.read(getClass().getResourceAsStream("/resources/sprites/quitButton.png"));

            playHoveredButtonSprite = ImageIO.read(getClass().getResourceAsStream("/resources/sprites/playButtonHovered.png"));
            howToPlayHoveredButtonSprite = ImageIO.read(getClass().getResourceAsStream("/resources/sprites/howToPlayButtonHovered.png"));
            creditsHoveredButtonSprite = ImageIO.read(getClass().getResourceAsStream("/resources/sprites/creditsButtonHovered.png"));
            quitHoveredButtonSprite = ImageIO.read(getClass().getResourceAsStream("/resources/sprites/quitButtonHovered.png"));
            
            bgImage = ImageIO.read(getClass().getResourceAsStream("/resources/sprites/title_bg.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        playButton = new Button(350, 300, playButtonSprite, playHoveredButtonSprite, 4);
        howToPlayButton = new Button(350, 360, howToPlayButtonSprite, howToPlayHoveredButtonSprite, 4);
        creditsButton = new Button(350, 420, creditsButtonSprite, creditsHoveredButtonSprite, 4);
        quitButton = new Button(350, 480, quitButtonSprite, quitHoveredButtonSprite, 4);
        
    }
    
    @Override
    public void mouseMoved(int mx, int my) {
        playButton.updateHover(mx, my);
        howToPlayButton.updateHover(mx, my);
        creditsButton.updateHover(mx, my);
        quitButton.updateHover(mx, my);
    }

    @Override
    public void mousePressed(int mx, int my) {
        if (playButton.isClicked(mx, my)) {
        	gsm.setState(GameStateManager.MAIN_MENU);
        	stopMusic();
        }
        if (howToPlayButton.isClicked(mx, my)) {
        	gsm.setState(GameStateManager.HOW_TO_PLAY);
        	stopMusic();
        }
        if (creditsButton.isClicked(mx, my)) {
        	gsm.setState(GameStateManager.CREDITS);
        	stopMusic();
        }
        if (quitButton.isClicked(mx, my)) System.exit(0);
    }

    private void stopMusic() {
	    if (music != null) {
	        music.stop();
	        music = null;
	    }
	}
    
    @Override public void update(double dt) {}

    @Override
    public void render(Graphics g) {
    	
    	Font customFont = null;
    	try {
    	    customFont = Font.createFont(
    	            Font.TRUETYPE_FONT,
    	            getClass().getResourceAsStream("/resources/fonts/PixelOperator8-Bold.ttf")
    	    );

    	    customFont = customFont.deriveFont(Font.BOLD, 64f); // size 48

    	    g.setFont(customFont);

    	} catch (Exception e) {
    	    e.printStackTrace();
    	    // fallback
    	    g.setFont(new Font("SansSerif", Font.BOLD, 48));
    	}
    	
    	g.drawImage(bgImage, 0, 0, 900, 600, null);

        g.setColor(Color.WHITE);
        g.setFont(customFont);
        g.drawString("THE JUMPER", 150, 200);
        
        playButton.draw(g);
        howToPlayButton.draw(g);
        creditsButton.draw(g);
        quitButton.draw(g);
    }

    @Override public void keyPressed(int key) {}

    @Override public void keyReleased(int key) {}
}
