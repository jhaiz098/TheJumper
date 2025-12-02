package com.mygame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Button {

    private int x, y;              // screen position
    private int width, height;     // drawn size
    private BufferedImage sprite;  // button image
    private boolean hovered = false;

    public Button(int x, int y, BufferedImage sprite, int scale) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;

        if (sprite != null) {
            this.width = sprite.getWidth() * scale;
            this.height = sprite.getHeight() * scale;
        } else {
            this.width = 100;  // fallback
            this.height = 40;
        }
    }

    // Collider rectangle
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Call this from mouse-move event
    public void updateHover(int mouseX, int mouseY) {
        hovered = getBounds().contains(mouseX, mouseY);
    }

    // Call this when mouse is clicked
    public boolean isClicked(int mouseX, int mouseY) {
        return getBounds().contains(mouseX, mouseY);
    }

    // Draw the button
    public void draw(Graphics g) {
        // Draw image
        if (sprite != null) {
            g.drawImage(sprite, x, y, width, height, null);
        } else {
            g.setColor(Color.GRAY);
            g.fillRect(x, y, width, height);
        }

        // Draw hover effect (optional)
        if (hovered) {
            g.setColor(new Color(255, 255, 255, 90));
            g.fillRect(x, y, width, height);
        }

        // Debug collider (always red)
        g.setColor(Color.RED);
        g.drawRect(x, y, width, height);
    }
}
