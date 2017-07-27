package fiu.com.skillcourt.game;

import android.util.Log;

import fiu.com.skillcourt.interfaces.CountdownInterface;
import fiu.com.skillcourt.interfaces.SkillCourtInteractor;

/**
 * Created by pedrocarrillo on 9/22/16.
 */

public class SkillCourtGame implements CountdownInterface {

    private int score;
    private int greenPad;
    private int redPad;
    private boolean isRunning = false;
    private float totalHits = 0;
    private int timeObjective = 3; // IN SECONDS
    private float greenHits;
    private int gameTimeTotal = 10;

    long currentTime = gameTimeTotal * 1000;
    long previousSecond = Long.MAX_VALUE;

    private GameMode gameMode = GameMode.HIT_MODE;

    public enum GameMode {
        BEAT_TIMER,
        HIT_MODE
    }

    private CountDownTimer countDownTimer;
    private SkillCourtInteractor skillCourtInteractor;

    public int getScore() {
        return score;
    }

    public void addPoint(int point) {
        score += point;
    }

    public void subtractPoint(int point) {
        score -= point;
    }

    public void addGreen(int point) {
        greenPad += point;
    }

    public void addRed(int point) {
        redPad += point;
    }

    public void addHit(int point) {
        totalHits += 1;
    }

    public int getGreen() {
        return greenPad;
    }

    public void greenHit() {
        greenHits++;
    }

    public void totalHit() {
        totalHits++;
    }

    public int getAccuracy() {
        return Math.round((greenHits/totalHits) * 100);
    }

    public int getGreenPad() {
        return greenPad;
    }

    public int getRedPad() {
        return redPad;
    }

    public float getTotalHits() {
        return totalHits;
    }

    public int getTimeObjective() {
        return timeObjective;
    }

    public float getGreenHits() {
        return greenHits;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setGameTimeTotal(int seconds) {
        gameTimeTotal = seconds;
        currentTime = gameTimeTotal * 1000;
    }

    public int getGameTimeTotal() {
        return gameTimeTotal;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void startGame() {
        resetVariable();
        isRunning = true;
        countDownTimer = new CountDownTimer(currentTime, 1, this);
        countDownTimer.start();
    }

    private void resetVariable() {
        score = 0;
        greenPad = 0;
        redPad = 0;
        isRunning = false;
        totalHits = 0;
        greenHits = 0;
        currentTime = gameTimeTotal * 1000;
        previousSecond = Long.MAX_VALUE;
    }

    public void cancelGame() {
        isRunning = false;
        countDownTimer.cancel();
    }

    public void pause() {
        cancelGame();
    }

    public void resume() {
        startGame();
    }

    public void restartGame() {
        resetVariable();
        startGame();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        currentTime = millisUntilFinished;
        int second = Math.round((float)millisUntilFinished / 1000.0f);
        long minutes = (second / 60);
        long seconds = second % 60;
        if (seconds != 0 && seconds < previousSecond) {
            previousSecond = seconds;
            if (gameMode == GameMode.BEAT_TIMER) {
                if (seconds % timeObjective == 0) {
                    skillCourtInteractor.onTimeObjective();
                }
            }
        }
        String time = String.format("%02d:%02d", minutes, seconds);
        skillCourtInteractor.onSecond(time, second);
        skillCourtInteractor.onMillisecond(Math.round((float)millisUntilFinished));
    }

    @Override
    public void onFinish() {
        isRunning = false;
        skillCourtInteractor.onFinish();
    }

    public SkillCourtGame() {

    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public void setTimeObjective(int timeObjective) {
        this.timeObjective = timeObjective;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setSkillCourtInteractor(SkillCourtInteractor skillCourtInteractor) {
        this.skillCourtInteractor = skillCourtInteractor;
    }

    public SkillCourtGame(SkillCourtInteractor skillCourtInteractor) {
        this.skillCourtInteractor = skillCourtInteractor;
    }

}
