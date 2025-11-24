package com.mygame;

import java.awt.Graphics;

public interface GameState {
    void update(double dt);
    void render(Graphics g);
    void keyPressed(int key);
    void keyReleased(int key);
}
