package com.mygame;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {

    public Clip clip;

    public Sound(String path) {
        try {
            URL url = getClass().getResource(path);
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip == null) return;
        if (clip.isRunning()) clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }
    
    public void loop() {
        if (clip == null) return;
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    
    public void stop() {
        if (clip != null) clip.stop();
    }
    
    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }
    
}
