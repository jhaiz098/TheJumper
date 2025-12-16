package com.mygame;

import com.mygame.states.*;
import java.awt.Graphics;

public class GameStateManager {
    public static final int TITLE = 0;
    public static final int MAIN_MENU = 1;
    public static final int LEVEL_1 = 10;
    public static final int LEVEL_2 = 11;
    public static final int LEVEL_3 = 12;
    public static final int LEVEL_4 = 13;
    public static final int LEVEL_5 = 14;

    private GameState current;
    private GameState nextLevel;
    private boolean loading = false;

    public void setState(int id) {
        // Show loading immediately
        loading = true;
        current = new LoadingState(this);

        // Load level asynchronously
        new Thread(() -> {
            GameState level = null;
            switch (id) {
                case TITLE -> level = new TitleState(this);
                case MAIN_MENU -> level = new MainMenuState(this);
                case LEVEL_1 -> level = new Level1State(this);
                case LEVEL_2 -> level = new Level2State(this);
                case LEVEL_3 -> level = new Level3State(this);
                case LEVEL_4 -> level = new Level4State(this);
                case LEVEL_5 -> level = new Level5State(this);
            }
            nextLevel = level; // ready to switch
        }).start();
    }

    public void update(double dt) {
        // Swap from loading to real level once ready
        if (loading && nextLevel != null) {
            current = nextLevel;
            nextLevel = null;
            loading = false;
        }

        if (current != null) current.update(dt);
    }

    public void render(Graphics g) {
        if (current != null) current.render(g);
    }

    public void keyPressed(int key) {
        if (current != null) current.keyPressed(key);
    }

    public void keyReleased(int key) {
        if (current != null) current.keyReleased(key);
    }

    public void mouseMoved(int x, int y) {
        if (current != null) current.mouseMoved(x, y);
    }

    public void mousePressed(int x, int y) {
        if (current != null) current.mousePressed(x, y);
    }

    public GameState getState() { return current; }
}
