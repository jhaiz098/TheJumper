package com.mygame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Program extends JPanel {

    private Player player;
    private List<Tile> map = new ArrayList<>();
    private BufferedImage[] tiles;

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private int camX = 0;
    private int camY = 0;

    private static final int PANEL_WIDTH = 900;
    private static final int PANEL_HEIGHT = 600;

    public Program() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.CYAN);
        setFocusable(true);

        // --- Load player ---
        player = new Player(100, 50, "/resources/sprites/knight.png");

        // --- Load tileset ---
        tiles = TileLoader.loadTiles("/resources/sprites/world_tileset.png", 16, 16);

        if (tiles != null && tiles.length > 0) {
            // Horizontal floor (example)
            addTile(-3, 10, 0, true);
            addTile(-2, 10, 0, true);
            addTile(-1, 10, 0, true);
            addTile(0, 10, 0, true);
            addTile(1, 10, 0, true);
            addTile(2, 10, 0, true);
            addTile(3, 10, 0, true);
            addTile(4, 10, 0, true);
            addTile(5, 10, 0, true);
            
            addTile(0, 0, 0, true);
            
            addTile(1, 5, 0, true);
            addTile(-1, 3, 0, true);
            addTile(-2, 2, 0, true);
            
            // Vertical wall
            addTile(3, 7, 0, true);
            addTile(5, 8, 0, true);
            addTile(5, 9, 0, true);
        }

        // --- Input handling ---
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int kc = e.getKeyCode();
                if (kc == KeyEvent.VK_LEFT) leftPressed = true;
                if (kc == KeyEvent.VK_RIGHT) rightPressed = true;
                if (kc == KeyEvent.VK_SPACE) player.jump();
                updatePlayerMovement();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int kc = e.getKeyCode();
                if (kc == KeyEvent.VK_LEFT) leftPressed = false;
                if (kc == KeyEvent.VK_RIGHT) rightPressed = false;
                updatePlayerMovement();
            }

            private void updatePlayerMovement() {
                if (leftPressed && !rightPressed) player.moveLeft();
                else if (rightPressed && !leftPressed) player.moveRight();
                else player.stop();
            }
        });

        // --- Game loop ---
        new Thread(() -> {
            final double dt = 1.0 / 60.0;
            while (true) {
                player.update(dt, map);

                // camera follows player
                camX = player.getX() - PANEL_WIDTH / 2 + player.getWidth() / 2;
                camY = player.getY() - PANEL_HEIGHT / 2 + player.getHeight() / 2;

                repaint();
                try { Thread.sleep(16); } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw tiles relative to camera
        for (Tile t : map) {
            t.draw(g, camX, camY);
        }

        // draw player relative to camera
        player.drawAt(g, camX, camY);
    }

    private void addTile(int x, int y, int tileIndex, boolean solid) {
        map.add(new Tile(x, y, tiles[tileIndex], solid));
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("The Jumper - Camera Example");
        Program panel = new Program();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(panel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);

        panel.requestFocusInWindow();
    }
}
