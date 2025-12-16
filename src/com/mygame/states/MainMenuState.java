package com.mygame.states;

import com.mygame.Button;
import com.mygame.GameState;
import com.mygame.GameStateManager;
import com.mygame.Sound;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class MainMenuState implements GameState {

    private final GameStateManager gsm;
    private List<Button> levelButtons;
    private Button backButton;
    private BufferedImage bgImage;
    private Sound music;

    public MainMenuState(GameStateManager gsm) {
        this.gsm = gsm;

        // Start menu music
        music = new Sound("/resources/music/time_for_adventure.wav");
        if (music != null && !music.isPlaying()) music.loop();

        // Load back button images
        BufferedImage backNormal = null, backHover = null;
        try {
            backNormal = ImageIO.read(getClass().getResourceAsStream("/resources/sprites/backButton.png"));
            backHover = ImageIO.read(getClass().getResourceAsStream("/resources/sprites/backButtonHovered.png"));
            bgImage = ImageIO.read(getClass().getResourceAsStream("/resources/sprites/title_bg.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        backButton = new Button(10, 10, backNormal, backHover, 4);

        // Level buttons (pixel art spritesheets)
        levelButtons = new ArrayList<>();
        int startX = 180, startY = 250;
        int spacing = 120; // enough to avoid overlap
        String[] levelSprites = {
            "/resources/sprites/lvl1Btn.png",
            "/resources/sprites/lvl2Btn.png",
            "/resources/sprites/lvl3Btn.png",
            "/resources/sprites/lvl4Btn.png",
            "/resources/sprites/lvl5Btn.png"
        };

        for (int i = 0; i < levelSprites.length; i++) {
            try {
                BufferedImage lvlSheet = ImageIO.read(getClass().getResourceAsStream(levelSprites[i]));
                // Take first frame (0,0,16x16) and scale by 4
                BufferedImage lvlBtn = lvlSheet.getSubimage(0, 0, 16, 16);
                levelButtons.add(new Button(startX + i * spacing, startY, lvlBtn, lvlBtn, 4));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void stopMusic() {
        if (music != null) music.stop();
    }

    @Override
    public void update(double dt) {}

    @Override
    public void render(Graphics g) {
        g.drawImage(bgImage, 0, 0, 900, 600, null);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("LEVEL SELECTOR", 250, 150);

        // Draw back button
        backButton.draw(g);

        // Draw level buttons
        for (int i = 0; i < levelButtons.size(); i++) {
            levelButtons.get(i).draw(g);
            // Draw level text
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
//            g.drawString("LEVEL " + (i + 1),
//                    levelButtons.get(i).getBounds().x + 8,
//                    levelButtons.get(i).getBounds().y + 35);
        }
    }

    @Override
    public void mouseMoved(int mx, int my) {
        backButton.updateHover(mx, my);
        for (Button b : levelButtons) b.updateHover(mx, my);
    }

    @Override
    public void mousePressed(int mx, int my) {
        if (backButton.isClicked(mx, my)) gsm.setState(GameStateManager.TITLE);

        for (int i = 0; i < levelButtons.size(); i++) {
            if (levelButtons.get(i).isClicked(mx, my)) {
                gsm.setState(GameStateManager.LEVEL_1 + i);
                stopMusic();
            }
        }
    }

    @Override
    public void keyPressed(int key) {
        if (key >= 49 && key <= 53) { // keys 1-5
            gsm.setState(GameStateManager.LEVEL_1 + (key - 49));
            stopMusic();
        }
    }

    @Override
    public void keyReleased(int key) {}
}
