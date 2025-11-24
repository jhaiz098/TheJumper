package com.mygame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TileLoader {

    // Load all tiles from a spritesheet
    public static BufferedImage[] loadTiles(String path, int tileWidth, int tileHeight) {
        try {
            BufferedImage sheet = ImageIO.read(TileLoader.class.getResource(path));
            int cols = sheet.getWidth() / tileWidth;
            int rows = sheet.getHeight() / tileHeight;
            BufferedImage[] tiles = new BufferedImage[cols * rows];
            int index = 0;
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++) {
                    tiles[index++] = sheet.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
                }
            }
            return tiles;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
