package com.mygame.states;

import com.mygame.Button;
import com.mygame.GameState;
import com.mygame.GameStateManager;
import com.mygame.Sound;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class HowToPlayState implements GameState {

	private int scrollY = 0;
    private final int MAX_SCROLL = 0;
    private final int MIN_SCROLL = -260; // adjust if text grows
    
    private final GameStateManager gsm;
    private Button backButton;
    private BufferedImage bgImage;
    private Sound music;

    public HowToPlayState(GameStateManager gsm) {
        this.gsm = gsm;

        // Background music
        music = new Sound("/resources/music/time_for_adventure.wav");
        if (music != null && !music.isPlaying()) music.loop();

        try {
            bgImage = ImageIO.read(
                getClass().getResourceAsStream("/resources/sprites/title_bg.png")
            );

            BufferedImage backNormal = ImageIO.read(
                getClass().getResourceAsStream("/resources/sprites/backButton.png")
            );
            BufferedImage backHover = ImageIO.read(
                getClass().getResourceAsStream("/resources/sprites/backButtonHovered.png")
            );

            backButton = new Button(20, 20, backNormal, backHover, 4);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopMusic() {
	    if (music != null) {
	        music.stop();
	        music = null;
	    }
	}

    @Override
    public void update(double dt) {}

    @Override
    public void render(Graphics g) {
        // Background
        g.drawImage(bgImage, 0, 0, 900, 600, null);

        // Dark overlay
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, 900, 600);

        // Title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("HOW TO PLAY", 300, 80);

        // Text content
        g.setFont(new Font("Arial", Font.PLAIN, 20));

        int x = 120;
        int y = 140 + scrollY;

        int line = 30;

        g.drawString("THE JUMPER is a 2D platformer focused on timing and precision.", x, y);
        y += line * 2;

        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("GOAL", x, y);
        y += line;

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("- Reach the goal flag to finish the level", x, y);
        y += line;
        g.drawString("- Collect coins to earn more stars", x, y);
        y += line;
        g.drawString("- Finish as fast as possible to beat high scores", x, y);

        y += line * 2;

        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("CONTROLS", x, y);
        y += line;

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("← / →, A / D    Move", x, y);
        y += line;
        g.drawString("SPACE / W    Jump", x, y);
        y += line;
        g.drawString("R        Restart Level", x, y);
        y += line;
        g.drawString("N        Next Level (after finishing)", x, y);
        y += line;
        g.drawString("ESC      Back to Menu", x, y);

        y += line * 2;

        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("DANGER", x, y);
        y += line;

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("- Avoid traps like saws and spikes", x, y);
        y += line;
        g.drawString("- Falling off the map will kill you", x, y);

        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Use ↑ ↓ to scroll", 360, 560);
        
        // Back button
        backButton.draw(g);
    }

    @Override
    public void mouseMoved(int mx, int my) {
        backButton.updateHover(mx, my);
    }

    @Override
    public void mousePressed(int mx, int my) {
        if (backButton.isClicked(mx, my)) {
            stopMusic();
            gsm.setState(GameStateManager.TITLE);
        }
    }

    @Override
    public void keyPressed(int key) {
        if (key == KeyEvent.VK_ESCAPE) {
            stopMusic();
            gsm.setState(GameStateManager.TITLE);
        }

        if (key == KeyEvent.VK_DOWN) {
            scrollY -= 20;
            if (scrollY < MIN_SCROLL) scrollY = MIN_SCROLL;
        }

        if (key == KeyEvent.VK_UP) {
            scrollY += 20;
            if (scrollY > MAX_SCROLL) scrollY = MAX_SCROLL;
        }
    }


    @Override
    public void keyReleased(int key) {}
}
