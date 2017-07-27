package fiu.com.skillcourt.entities;

import java.util.Date;

import fiu.com.skillcourt.game.SkillCourtGame;

/**
 * Created by pedrocarrillo on 11/13/16.
 */

public class Game {

    private int score;
    private int greenPad;
    private int redPad;
    private float totalHits;
    private int timeObjective;
    private float greenHits;
    private int gameTimeTotal;
    private int accuracy;
    private long date;

    private String gameMode;

    public Game(int score, int greenPad, int redPad, float totalHits, int timeObjective, float greenHits, int gameTimeTotal, int accuracy, String gameMode, long date) {
        this.score = score;
        this.greenPad = greenPad;
        this.redPad = redPad;
        this.totalHits = totalHits;
        this.timeObjective = timeObjective;
        this.greenHits = greenHits;
        this.gameTimeTotal = gameTimeTotal;
        this.gameMode = gameMode;
        this.accuracy = accuracy;
        this.date = date;
    }

    public Game() {

    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getGreenPad() {
        return greenPad;
    }

    public void setGreenPad(int greenPad) {
        this.greenPad = greenPad;
    }

    public int getRedPad() {
        return redPad;
    }

    public void setRedPad(int redPad) {
        this.redPad = redPad;
    }

    public float getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(float totalHits) {
        this.totalHits = totalHits;
    }

    public int getTimeObjective() {
        return timeObjective;
    }

    public void setTimeObjective(int timeObjective) {
        this.timeObjective = timeObjective;
    }

    public float getGreenHits() {
        return greenHits;
    }

    public void setGreenHits(float greenHits) {
        this.greenHits = greenHits;
    }

    public int getGameTimeTotal() {
        return gameTimeTotal;
    }

    public void setGameTimeTotal(int gameTimeTotal) {
        this.gameTimeTotal = gameTimeTotal;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }
}
