package com.mygame;

public class HighScore {
    public String name;
    public double time;
    public int stars;

    public HighScore(String name, double time, int stars) {
        this.name = name;
        this.time = time;
        this.stars = stars;
    }

    public boolean isBetterThan(double newTime, int newStars) {
        if (newStars > stars) return true;
        if (newStars == stars && newTime < time) return true;
        return false;
    }
}
