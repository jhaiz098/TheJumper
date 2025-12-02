package com.mygame.states;

import com.mygame.Button;
import com.mygame.GameState;
import com.mygame.GameStateManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class TitleState implements GameState {
    private final GameStateManager gsm;
    
    private Button playButton;
    private Button quitButton;
    
    public TitleState(GameStateManager gsm) {
    	this.gsm = gsm;
    	
    	BufferedImage buttonSprite = null;
        try {
            buttonSprite = ImageIO.read(getClass().getResourceAsStream("/resources/sprites/button.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        playButton = new Button(350, 300, buttonSprite, 4);
        quitButton = new Button(350, 380, buttonSprite, 4);
        
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

    	    customFont = customFont.deriveFont(Font.BOLD, 48f); // size 48

    	    g.setFont(customFont);

    	} catch (Exception e) {
    	    e.printStackTrace();
    	    // fallback
    	    g.setFont(new Font("SansSerif", Font.BOLD, 48));
    	}
    	
    	Font customFont2 = null;
    	try {
    	    customFont2 = Font.createFont(
    	            Font.TRUETYPE_FONT,
    	            getClass().getResourceAsStream("/resources/fonts/PixelOperator8-Bold.ttf")
    	    );

    	    customFont2 = customFont2.deriveFont(Font.BOLD, 20f); // size 48

    	    g.setFont(customFont2);

    	} catch (Exception e) {
    	    e.printStackTrace();
    	    // fallback
    	    g.setFont(new Font("SansSerif", Font.BOLD, 20));
    	}

    	
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,0,900,600);
        g.setColor(Color.WHITE);
        g.setFont(customFont);
        g.drawString("THE JUMPER", 230, 200);
        g.setFont(new Font("SansSerif", Font.PLAIN, 24));
        g.drawString("Press ENTER to continue", 320, 350);
        
        playButton.draw(g);
        quitButton.draw(g);
        
        g.setFont(customFont2);
        g.setColor(Color.WHITE);
        
        g.drawString("Play", 410,336);
        g.drawString("Quit", 410,336 + 80);
    }

    
    
    @Override
    public void keyPressed(int key) {
        if (key == KeyEvent.VK_ENTER) gsm.setState(GameStateManager.MAIN_MENU);
    }

    @Override public void keyReleased(int key) {}
}
