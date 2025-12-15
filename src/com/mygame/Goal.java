package com.mygame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Goal {
    private int x, y;
    private BufferedImage sprite;
    private final int SIZE = 48;

    public Goal(int x, int y, String spritePath) {
        this.x = x;
        this.y = y;

        try {
            sprite = ImageIO.read(getClass().getResource(spritePath));
        } catch (IOException e) {
            sprite = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        }
    }

    public void drawAt(Graphics g, int camX, int camY) {
        g.drawImage(sprite, x - camX, y - camY, SIZE, SIZE, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }
}
