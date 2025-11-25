package com.mygame.states;

import com.mygame.GameState;
import com.mygame.GameStateManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class TitleState implements GameState {
    private final GameStateManager gsm;
    public TitleState(GameStateManager gsm) { this.gsm = gsm; }

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

    	
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,0,900,600);
        g.setColor(Color.WHITE);
        g.setFont(customFont);
        g.drawString("THE JUMPER", 230, 200);
        g.setFont(new Font("SansSerif", Font.PLAIN, 24));
        g.drawString("Press ENTER to continue", 320, 350);
    }

    @Override
    public void keyPressed(int key) {
        if (key == KeyEvent.VK_ENTER) gsm.setState(GameStateManager.MAIN_MENU);
    }

    @Override public void keyReleased(int key) {}
}
