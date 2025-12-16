package com.mygame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Button {

    private int x, y;              // screen position
    private int width, height;     // drawn size
    private BufferedImage normalSprite;  // normal button image
    private BufferedImage hoverSprite;   // sprite when hovered
    private boolean hovered = false;

    public Button(int x, int y, BufferedImage normalSprite, BufferedImage hoverSprite, int scale) {
        this.x = x;
        this.y = y;
        this.normalSprite = normalSprite;
        this.hoverSprite = hoverSprite;

        if (normalSprite != null) {
            this.width = normalSprite.getWidth() * scale;
            this.height = normalSprite.getHeight() * scale;
        } else {
            this.width = 100;  // fallback
            this.height = 40;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void updateHover(int mouseX, int mouseY) {
        hovered = getBounds().contains(mouseX, mouseY);
    }

    public boolean isClicked(int mouseX, int mouseY) {
        return getBounds().contains(mouseX, mouseY);
    }

    public void draw(Graphics g) {
        BufferedImage spriteToDraw = hovered && hoverSprite != null ? hoverSprite : normalSprite;

        if (spriteToDraw != null) {
            g.drawImage(spriteToDraw, x, y, width, height, null);
        } else {
            g.setColor(Color.GRAY);
            g.fillRect(x, y, width, height);
        }

        // Debug collider (always red)
//        g.setColor(Color.RED);
//        g.drawRect(x, y, width, height);
    }
}
