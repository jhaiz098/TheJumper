package com.mygame.states;

import com.mygame.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

public abstract class BaseLevelState implements GameState {

    protected List<Tile> map;
    protected List<Coin> coins;  // List of coins in the level

    protected boolean showFinishUI = false;
    
    protected Goal goal;

    public int levelNumber = 0;
 
    // High score for the level
    protected double highScoreTime = Double.MAX_VALUE;
    protected int highScoreStars = 0;
    protected boolean newHighScore = false;
    
    protected int maxCoins = 0;
    protected int collectedCoins = 0;

    // ===== TIMER =====
    protected double levelTime = 0.0;   // seconds
    protected boolean timerRunning = true;

    
    protected boolean levelFinished = false;
    protected int starsEarned = 0;

    protected int killY = 1000;

    protected boolean playerDead = false;
    protected boolean paused = false;

    
    private int cachedOriginX = 0;
    private int cachedOriginY = 0;

    private BufferedImage cachedMap = null;
    private boolean needsRedraw = true;
    
    protected Sound levelCompleteSound = new Sound("/resources/sounds/level-up-431472.wav");
    protected Sound deathSound = new Sound("/resources/sounds/8-bit-video-game-lose-sound-version-4-145477.wav");
    
    protected Sound coinSound = new Sound("/resources/sounds/coin.wav");
    protected Sound music;
    
    protected boolean initialized = false;
    
    public BaseLevelState() {
        map = new ArrayList<>();
        coins = new ArrayList<>();
        
        music = new Sound("/resources/music/8-bit-game-158815.wav");
    }
    public void onEnter() {
        if (music != null) {
            music.stop(); // reset
            music.loop();
//            System.out.println("Music started");
        } else {
            System.out.println("Music is null!");
        }
    }


    protected void stopMusic() {
        if (music != null) music.stop();
        if (levelCompleteSound != null) levelCompleteSound.stop();
        if (deathSound != null) deathSound.stop();
    }

    
    protected void setGoal(int x, int y, String spritePath) {
        goal = new Goal(x, y, spritePath);
    }

    protected void finalizeLevel() {
        // ⭐ STAR CALCULATION
        if (maxCoins == 0) {
            starsEarned = 3;
        } else {
            collectedCoins = maxCoins - coins.size();
            float ratio = (float) collectedCoins / (float) maxCoins;

            if (ratio >= 1.0f) starsEarned = 3;
            else if (ratio >= 0.66f) starsEarned = 2;
            else if (ratio >= 0.33f) starsEarned = 1;
            else starsEarned = 0;
        }

        levelFinished = true;
        showFinishUI = true;
        timerRunning = false;

        if (levelCompleteSound != null) levelCompleteSound.play();

        // ===== HIGH SCORE LOGIC =====
        loadHighScore();

        boolean isBetter = false;
        if (starsEarned > highScoreStars) isBetter = true;
        else if (starsEarned == highScoreStars && levelTime < highScoreTime) isBetter = true;

        newHighScore = isBetter; // set the congratulatory flag

        if (isBetter) {
            // Save automatically with default name
            highScoreStars = starsEarned;
            highScoreTime = levelTime;
            saveHighScore();
        }
    }
    
    private void loadHighScore() {
        java.io.File file = new java.io.File("scores", "level" + levelNumber + "_score.txt");
        if (!file.exists()) return;
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            highScoreTime = Double.parseDouble(br.readLine());
            highScoreStars = Integer.parseInt(br.readLine());
        } catch (Exception e) {
            highScoreTime = Double.MAX_VALUE;
            highScoreStars = 0;
        }
    }



    private void saveHighScore() {
        try {
            java.io.File dir = new java.io.File("scores");
            if (!dir.exists()) dir.mkdirs();

            java.io.File file = new java.io.File(dir, "level" + levelNumber + "_score.txt");
            try (java.io.PrintWriter pw = new java.io.PrintWriter(file)) {
                pw.println(highScoreTime);
                pw.println(highScoreStars);
            }
            
//            System.out.println("Saving score to: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    
    protected void onPlayerDeath() {
        playerDead = true;
        paused = true;
        showFinishUI = true;
        timerRunning = false;

        if (deathSound != null) deathSound.play();
    }
    
    protected void setKillY(int y) {
        killY = y;
    }
    
    // Add a tile to the map (same as before)
    protected void addTile(int tx, int ty, int tileIndex, int zIndex, boolean solid) {
        BufferedImage[] tileset = TileLoader.loadTiles("/resources/sprites/world_tileset.png", 16, 16);
        if (tileset != null && tileset.length > tileIndex) {
            map.add(new Tile(tx, ty, tileset[tileIndex], zIndex, solid));
            needsRedraw = true; // map changed → redraw next frame
        }
    }

    // Method to add a coin at a specific position
    protected void addCoin(int x, int y, String spriteSheetPath) {
        Coin coin = new Coin(x, y, spriteSheetPath);  // Create a new coin
        coins.add(coin);  // Add it to the list of coins
    }
    
    // In BaseLevelState
    protected void addCoins(List<int[]> positions, String spriteSheetPath) {
        for (int[] pos : positions) {
            addCoin(pos[0], pos[1], spriteSheetPath);
        }
        maxCoins = positions.size();
    }


    private void rebuildMapImage() {
        if (map.isEmpty()) return;

        map.sort((a, b) -> Integer.compare(a.getZ(), b.getZ()));

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (Tile t : map) {
            int x = t.getScreenX();
            int y = t.getScreenY();
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x + Tile.PIXEL);
            maxY = Math.max(maxY, y + Tile.PIXEL);
        }

        int width = Math.max(1, maxX - minX);
        int height = Math.max(1, maxY - minY);

        cachedMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = cachedMap.createGraphics();

        for (Tile t : map) {
            if (t.sprite == null) continue;
            g2.drawImage(t.sprite,
                t.getScreenX() - minX,
                t.getScreenY() - minY,
                Tile.PIXEL, Tile.PIXEL,
                null
            );
        }

        g2.dispose();

        cachedOriginX = minX;
        cachedOriginY = minY;

        needsRedraw = false;
    }

    protected void renderTiles(Graphics g, int camX, int camY) {
        if (needsRedraw || cachedMap == null) {
            rebuildMapImage();
        }

        if (cachedMap == null) return;

        int drawX = cachedOriginX - camX;
        int drawY = cachedOriginY - camY;
        g.drawImage(cachedMap, drawX, drawY, null);
    }
    protected abstract void restartLevel();
    protected abstract void nextLevel();
    protected abstract void goToLevelSelect();
    
    @Override
    public void keyPressed(int key) {
        // ESC always works
        if (key == KeyEvent.VK_ESCAPE) {
            goToLevelSelect();
        }

        // R always works (restart)
        if (key == KeyEvent.VK_R) {
            restartLevel();
        }

        // N only works if the level is finished (not dead)
        if (key == KeyEvent.VK_N && levelFinished) {
            nextLevel();
        }
    }


    @Override
    public void keyReleased(int key) {}
    
    @Override
    public void update(double dt) {
        if (!initialized) {
            initialized = true;
            onEnter();
        }

        // ⏱️ Update timer ONLY while playing
        if (!paused && !levelFinished && !playerDead && timerRunning) {
            levelTime += dt;
        }

        for (Coin coin : coins) {
            coin.update();
        }
    }

    protected void renderUI(Graphics g) {
        // ⏱ Timer
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString(String.format("Time: %.2f", levelTime), 750, 30);

        // 🪙 Coins
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Coins: " + collectedCoins + " / " + maxCoins, 10, 30);

        // 🏁 Finish UI
        if (showFinishUI) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, 900, 600);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 26));

            if (playerDead) {
                g.drawString("YOU DIED", 380, 200);
            } else {
                g.drawString("LEVEL COMPLETE", 330, 150);
                g.setFont(new Font("Arial", Font.BOLD, 22));
                g.drawString("Stars: " + starsEarned + " / 3", 360, 200);
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                g.drawString(String.format("Time: %.2f seconds", levelTime), 340, 240);
            }
            
            if (levelFinished && !playerDead) {
                g.setFont(new Font("Arial", Font.PLAIN, 18));
                g.drawString("BEST RECORD", 360, 300);
                g.drawString("Stars: " + Math.max(highScoreStars, starsEarned), 360, 330);
                g.drawString(String.format("Time: %.2f", Math.min(highScoreTime, levelTime)), 360, 360);
            }

        }
        
        if (showFinishUI && levelFinished && !playerDead) {
            if (newHighScore) {
                g.setFont(new Font("Arial", Font.BOLD, 24));
                g.setColor(Color.YELLOW);
                g.drawString("🎉 NEW HIGH SCORE! 🎉", 300, 270);
            }

            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.setColor(Color.WHITE);
            g.drawString("BEST:", 360, 300);
            g.drawString("Stars: " + highScoreStars, 360, 330);
            g.drawString(String.format("Time: %.2f", highScoreTime), 360, 360);
        }
        
        // 🎮 Controls hint
        if (showFinishUI) {
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.setColor(Color.WHITE);

            if (levelFinished) {
            	
                g.drawString(
                    "ESC - Level Select   |   R - Restart Level   |   N - Next Level",
                    300, 500
                );
            } else if (playerDead) {
                g.drawString(
                    "ESC - Level Select   |   R - Restart Level",
                    330, 500
                );
            }
        }

    }
}
