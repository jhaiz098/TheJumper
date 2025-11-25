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
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,0,900,600);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 48));
        g.drawString("THE JUMPER", 280, 200);
        g.setFont(new Font("SansSerif", Font.PLAIN, 24));
        g.drawString("Press ENTER to continue", 320, 260);
        g.drawString("Press 1-10 for Levels 1-10", 350, 300);
    }

    @Override
    public void keyPressed(int key) {
//        if (key == KeyEvent.VK_ENTER) gsm.setState(GameStateManager.LEVEL_SELECT);
//        if (key == KeyEvent.VK_S) gsm.setState(GameStateManager.SETTINGS);
//        if (key == KeyEvent.VK_T) gsm.setState(GameStateManager.TUTORIAL);

        if (key == KeyEvent.VK_ENTER) gsm.setState(GameStateManager.MAIN_MENU);
        if (key == KeyEvent.VK_1) gsm.setState(GameStateManager.LEVEL_1);
        if (key == KeyEvent.VK_2) gsm.setState(GameStateManager.LEVEL_2);
    }

    @Override public void keyReleased(int key) {}
}
