package com.mygame.states;

import static com.mygame.GameConstants.TILE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.mygame.Coin;
import com.mygame.GameStateManager;
import com.mygame.Platform;
import com.mygame.Player;
import com.mygame.Spike;
import com.mygame.TileLoader;

public class Level5State extends BaseLevelState {

    private final GameStateManager gsm;
    private Player player;
    private BufferedImage[] tileset;

    private List<Platform> platforms;
    private List<Spike> spikes;

    private int goalX = 12; // tile-based
    private int goalY = -14;

    public Level5State(GameStateManager gsm) {
        this.gsm = gsm;

        setGoal(goalX * TILE, goalY * TILE, "/resources/sprites/goal.png");

        // --- SPIKES ---
        BufferedImage spikeSprite;
        try {
            BufferedImage spikeSheet = ImageIO.read(getClass().getResource("/resources/sprites/spike.png"));
            spikeSprite = spikeSheet.getSubimage(0, 0, 16, 16);
        } catch (Exception e) {
            e.printStackTrace();
            spikeSprite = null;
        }

        spikes = new ArrayList<>();
        // Spike lines
        for (int x = 3; x <= 5; x++) spikes.add(new Spike(x, 5, 16, 16, spikeSprite));
        for (int x = 7; x <= 9; x++) spikes.add(new Spike(x, 2, 16, 16, spikeSprite));
        spikes.add(new Spike(6, -2, 16, 16, spikeSprite));
        spikes.add(new Spike(10, -5, 16, 16, spikeSprite));
        spikes.add(new Spike(12, -8, 16, 16, spikeSprite));
        spikes.add(new Spike(8, -12, 16, 16, spikeSprite));

        // --- PLATFORMS ---
        platforms = new ArrayList<>();
        BufferedImage platformSheet, plat16, plat32;
        try {
            platformSheet = ImageIO.read(getClass().getResource("/resources/sprites/platforms.png"));
            plat16 = platformSheet.getSubimage(0, 48, 16, 16);
            plat32 = platformSheet.getSubimage(16, 48, 32, 16);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Static safe platforms
        platforms.add(new Platform(1, 6, 1, 6, 2, 1, plat32, 0f)); // bottom-left
        platforms.add(new Platform(4, 3, 4, 3, 2, 1, plat32, 0f)); // middle

        // Moving horizontal platform
        platforms.add(new Platform(2, 4, 6, 4, 2, 1, plat32, 120f)); // horizontal ping-pong

        // Moving vertical platform
        platforms.add(new Platform(10, -2, 10, 2, 2, 1, plat32, 100f)); // vertical ping-pong

        // Big risky moving platform
        platforms.add(new Platform(8, -6, 12, -6, 4, 1, plat32, 150f));

        // --- COINS ---
        List<int[]> coinPositions = new ArrayList<>();
        coinPositions.add(new int[]{2 * TILE, 5 * TILE});
        coinPositions.add(new int[]{3 * TILE, 4 * TILE});
        coinPositions.add(new int[]{5 * TILE, 4 * TILE});
        coinPositions.add(new int[]{6 * TILE, -2 * TILE});
        coinPositions.add(new int[]{10 * TILE, -5 * TILE});
        coinPositions.add(new int[]{12 * TILE, -8 * TILE});
        addCoins(coinPositions, "/resources/sprites/coin.png");

        // --- TILES ---
        tileset = TileLoader.loadTiles("/resources/sprites/world_tileset.png", 16, 16);
        map = new ArrayList<>();
        if (tileset != null) {
            for (int x = -5; x < 20; x++) addTile(x, 7, 6, 1, true); // ground
            for (int x = -5; x < 20; x++) addTile(x, 8, 22, 1, true); // wall blocks
            for (int x = -5; x < 20; x++) addTile(x, -15, 6, 0, false); // BG
        }

        // --- PLAYER ---
        player = new Player(1 * TILE, 5 * TILE, "/resources/sprites/knight.png");
    }

    @Override
    public void update(double dt) {
        super.update(dt);

        if (!paused && !levelFinished && !playerDead) {
            int gained = player.update(dt, coins, map, coinSound);
            collectedCoins += gained;

            for (Platform p : platforms) {
                p.update(dt);
                player.collideWithPlatform(p);
            }

            for (Spike s : spikes) {
                if (!playerDead && player.getBounds().intersects(s.getBounds())) {
                    onPlayerDeath();
                    break;
                }
            }

            if (goal != null && player.getBounds().intersects(goal.getBounds())) {
                finalizeLevel();
            }
        }

        for (Coin coin : coins) coin.update();

        if (!playerDead && player.getY() > killY) onPlayerDeath();
    }

    @Override
    public void render(Graphics g) {
        int camX = player.getX() - 450;
        int camY = player.getY() - 300;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 900, 600);

        renderTiles(g, camX, camY);

        for (Platform p : platforms) p.draw(g, camX, camY);
        for (Coin coin : coins) coin.drawAt(g, camX, camY);
        for (Spike s : spikes) s.draw(g, camX, camY);

        if (goal != null) goal.drawAt(g, camX, camY);

        player.drawAt(g, camX, camY);

        renderUI(g);
    }

    @Override
    public void keyPressed(int key) {
        super.keyPressed(key);
        player.keyPressed(key);
    }

    @Override
    public void keyReleased(int key) {
        player.keyReleased(key);
    }

    @Override
    protected void restartLevel() {
        stopMusic();
        gsm.setState(GameStateManager.LEVEL_4);
    }

    @Override
    protected void nextLevel() {
        stopMusic();
        gsm.setState(GameStateManager.LEVEL_5);
    }

    @Override
    protected void goToLevelSelect() {
        stopMusic();
        gsm.setState(GameStateManager.MAIN_MENU);
    }
}
