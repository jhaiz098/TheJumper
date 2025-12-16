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

public class MainMenuState implements GameState {

    private final GameStateManager gsm;

    private Button backButton;
    private BufferedImage bgImage = null;
    
    private Sound music;
    
    public MainMenuState(GameStateManager gsm) {
        this.gsm = gsm;
        
        music = new Sound("/resources/music/time_for_adventure.wav");
        
        if (music != null && !music.isPlaying()) {
            music.loop();
            System.out.println("Menu music started");
        }
        
        BufferedImage backButtonSprite = null,
        			backHoveredButtonSprite = null;
        
        try {
        	backButtonSprite = ImageIO.read(getClass().getResourceAsStream("/resources/sprites/backButton.png"));

        	backHoveredButtonSprite = ImageIO.read(getClass().getResourceAsStream("/resources/sprites/backButtonHovered.png"));
            
            bgImage = ImageIO.read(getClass().getResourceAsStream("/resources/sprites/title_bg.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        backButton = new Button(10, 10, backButtonSprite, backHoveredButtonSprite, 4);
    }
    
    private void stopMusic() {
        if (music != null) music.stop();
    }
    
    @Override
    public void update(double dt) {
        // empty for now
    }

    @Override
    public void render(Graphics g) {
        // empty for now
    	g.drawImage(bgImage, 0, 0, 900, 600, null);
        
    	
    	
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 24));
        g.drawString("Press number 1-10 to select corresponding level.", 180, 200);
        
        backButton.draw(g);
    }

    @Override
    public void mouseMoved(int mx, int my) {
        backButton.updateHover(mx, my);
    }
    
    @Override
    public void mousePressed(int mx, int my) {
        if (backButton.isClicked(mx, my)) gsm.setState(GameStateManager.TITLE);
    }
    
    @Override
    public void keyPressed(int key) {
    	if (key == KeyEvent.VK_1) {
    		gsm.setState(GameStateManager.LEVEL_1);
    		stopMusic();
    	}
        if (key == KeyEvent.VK_2) gsm.setState(GameStateManager.LEVEL_2);
        if (key == KeyEvent.VK_3) gsm.setState(GameStateManager.LEVEL_3);
        if (key == KeyEvent.VK_4) gsm.setState(GameStateManager.LEVEL_4);
        if (key == KeyEvent.VK_5) gsm.setState(GameStateManager.LEVEL_5);
//        if (key == KeyEvent.VK_6) gsm.setState(GameStateManager.LEVEL_6);
//        if (key == KeyEvent.VK_7) gsm.setState(GameStateManager.LEVEL_7);
//        if (key == KeyEvent.VK_8) gsm.setState(GameStateManager.LEVEL_8);
//        if (key == KeyEvent.VK_9) gsm.setState(GameStateManager.LEVEL_9);
//        if (key == KeyEvent.VK_0) gsm.setState(GameStateManager.LEVEL_10);
    }

    @Override
    public void keyReleased(int key) {
        // empty for now
    }
}
