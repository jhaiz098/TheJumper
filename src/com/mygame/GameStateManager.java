package com.mygame;

import com.mygame.states.*;

public class GameStateManager {
    public static final int TITLE = 0;
    public static final int MAIN_MENU = 1;
    public static final int LEVEL_1 = 10;
    public static final int LEVEL_2 = 11;
    public static final int LEVEL_3 = 12;
    public static final int LEVEL_4 = 13;
    public static final int LEVEL_5 = 14;
    public static final int LEVEL_6 = 15;
    public static final int LEVEL_7 = 16;
    public static final int LEVEL_8 = 17;
    public static final int LEVEL_9 = 18;
    public static final int LEVEL_10 = 19;

    private GameState current;

    public void setState(int id) {
        switch (id) {
            case TITLE -> current = new TitleState(this);
            case MAIN_MENU -> current = new MainMenuState(this);
            case LEVEL_1 -> current = new Level1State(this);
            case LEVEL_2 -> current = new Level2State(this);
            case LEVEL_3 -> current = new Level3State(this);
//            case LEVEL_4 -> current = new Level4State(this);
//            case LEVEL_5 -> current = new Level5State(this);
//            case LEVEL_6 -> current = new Level6State(this);
//            case LEVEL_7 -> current = new Level7State(this);
//            case LEVEL_8 -> current = new Level8State(this);
//            case LEVEL_9 -> current = new Level9State(this);
//            case LEVEL_10 -> current = new Leve10State(this);
            // ...
        }
    }

    public GameState getState() { return current; }
    
    public void mouseMoved(int x, int y) {
        if (current != null) current.mouseMoved(x, y);
    }

    public void mousePressed(int x, int y) {
        if (current != null) current.mousePressed(x, y);
    }

}

