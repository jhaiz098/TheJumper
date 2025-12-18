package com.mygame;

import java.io.*;

public class HighScoreManager {

    private static final String DIR = "scores/";

    public static HighScore load(int level) {
        File file = new File(DIR + "level" + level + ".txt");
        if (!file.exists()) {
            return new HighScore("NONE", Double.MAX_VALUE, 0);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String name = br.readLine();
            double time = Double.parseDouble(br.readLine());
            int stars = Integer.parseInt(br.readLine());
            return new HighScore(name, time, stars);
        } catch (Exception e) {
            return new HighScore("NONE", Double.MAX_VALUE, 0);
        }
    }

    public static void save(int level, HighScore score) {
        new File(DIR).mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(
                DIR + "level" + level + ".txt"))) {
            pw.println(score.name);
            pw.println(score.time);
            pw.println(score.stars);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
