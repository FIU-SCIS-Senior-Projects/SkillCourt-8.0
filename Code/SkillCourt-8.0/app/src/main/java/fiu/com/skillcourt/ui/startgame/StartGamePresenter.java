package fiu.com.skillcourt.ui.startgame;


import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import fiu.com.skillcourt.connection.Arduino;
import fiu.com.skillcourt.connection.ArduinoManager;
import fiu.com.skillcourt.entities.Game;
import fiu.com.skillcourt.game.SkillCourtGame;
import fiu.com.skillcourt.game.SkillCourtManager;
import fiu.com.skillcourt.interfaces.ArduinoSkillCourtInteractor;
import fiu.com.skillcourt.interfaces.SkillCourtInteractor;

/**
 * @author  pedrocarrillo on 9/22/16.
 */

public class StartGamePresenter implements SkillCourtInteractor, ArduinoSkillCourtInteractor {

    private StartGameView view;
    private SkillCourtGame skillCourtGame;
    private ArduinoManager arduinoManager;

    private List<Integer> randomNumbers = new ArrayList<>();

    private boolean isCustomSequence = false;
//    private HashMap<String, String> customSteps = new HashMap<>();
    private List<String> customSteps;
    private int stepCounter = 1;

    public StartGamePresenter(StartGameView view, List<String> customSteps) {
        this.view = view;
        skillCourtGame = SkillCourtManager.getInstance().getGame();
        skillCourtGame.setSkillCourtInteractor(this);
        arduinoManager = ArduinoManager.getInstance();
        arduinoManager.setArduinoSkillCourtInteractor(this);
        if (customSteps.isEmpty()) {
            isCustomSequence = false;
            randomNumbers.add(1);
            if (arduinoManager.getArduinos().size() > 1) {
                for (int i = 1; i < arduinoManager.getArduinos().size(); i++) {
                    randomNumbers.add(0);
                }
            } else {
                randomNumbers.add(0);
            }
        } else {
            isCustomSequence = true;
            this.customSteps = customSteps;
        }
    }

    public void playAgain() {
        skillCourtGame.restartGame();
        view.updateResult(skillCourtGame.getTotalHits(), skillCourtGame.getGreenHits(), skillCourtGame.getRedPad(), skillCourtGame.getScore(), skillCourtGame.getAccuracy());
        view.setupInitGame();
        updateArduinosStatus();
        view.setProgressTotal(skillCourtGame.getGameTimeTotal() * 1000);
    }

    public void startGame() {
        view.setupInitGame();
        skillCourtGame.startGame();
        updateArduinosStatus();
        view.setProgressTotal(skillCourtGame.getGameTimeTotal() * 1000);
    }

    @Override
    public void onSecond(String time, long seconds) {
        if (view != null) view.setTimerText(time);
    }

    @Override
    public void onMillisecond(long milliseconds) {
        if (view != null) {
            view.changeProgressView(milliseconds);
        }
    }

    @Override
    public void onTimeObjective() {
        if(skillCourtGame.getGameMode() == SkillCourtGame.GameMode.BEAT_TIMER) {
            Log.e("time", "onTimeObjective");
            updateArduinosStatus();
        }
    }

    @Override
    public void onMessageReceived(Arduino.TYPE_LIGHT currentStatus, String message) {
        if(skillCourtGame.isRunning()) {
            if (currentStatus == Arduino.TYPE_LIGHT.GREEN) {
                SkillCourtManager.getInstance().addGreenPoint();
            } else {
                SkillCourtManager.getInstance().addRedPoint();
            }
            Log.e("points", " greenhits " + SkillCourtManager.getInstance().getGame().getGreenHits());
            Log.e("points", " score " + SkillCourtManager.getInstance().getGame().getScore());
            Log.e("points", "acc "+ SkillCourtManager.getInstance().getGame().getAccuracy());
            SkillCourtGame sk = SkillCourtManager.getInstance().getGame();
            if (view != null) {
                view.updateResult(skillCourtGame.getTotalHits(), skillCourtGame.getGreenHits(), skillCourtGame.getRedPad(), skillCourtGame.getScore(), skillCourtGame.getAccuracy());
            }
            if (skillCourtGame.getGameMode() == SkillCourtGame.GameMode.HIT_MODE) {
                updateArduinosStatus();
            }
        }
    }

    private void updateArduinosStatus() {
        if (!isCustomSequence) {
            Collections.shuffle(randomNumbers);
            if (arduinoManager.getArduinos().size() > 1) {
                for (int i = 0; i < randomNumbers.size(); i++) {
                    Arduino.TYPE_LIGHT type = randomNumbers.get(i) == 0 ? Arduino.TYPE_LIGHT.RED : Arduino.TYPE_LIGHT.GREEN;
                    arduinoManager.getArduinos().get(i).setStatus(type);
                }
            } else {
                Arduino.TYPE_LIGHT type = randomNumbers.get(0) == 0 ? Arduino.TYPE_LIGHT.RED : Arduino.TYPE_LIGHT.GREEN;
                arduinoManager.getArduinos().get(0).setStatus(type);
            }
        } else {
            int currentStep = stepCounter;
            Log.e("size", "" + customSteps.size() + " " + currentStep + " " + customSteps.get(currentStep));
            for (int i = 0; i < arduinoManager.getArduinos().size(); i++) {
                String arduinoGreen = customSteps.get(currentStep);
                Log.e("testSequences", arduinoGreen + " is going to be green");
                if (arduinoGreen != null) {
                    if (arduinoGreen.equalsIgnoreCase(String.valueOf((i + 1)))) {
                        arduinoManager.getArduinos().get(i).setStatus(Arduino.TYPE_LIGHT.GREEN);
                    } else {
                        arduinoManager.getArduinos().get(i).setStatus(Arduino.TYPE_LIGHT.RED);
                    }
                }
            }
            stepCounter++;
            if ((stepCounter + 1) % (customSteps.size()+1) == 0) stepCounter = 0;
        }
    }

    public void onPause() {
        view = null;
        skillCourtGame.pause();
    }

    public void onStop() {
        view = null;
        skillCourtGame.pause();
    }

    public void onResume(StartGameView view) {
        this.view = view;
//        skillCourtGame.resume();
    }

    @Override
    public void onFinish() {
        view.setupFinishGame();
        sendSignalArduinoFinish();
        gameFisnished();
        view.setTimerText("TIME's up!");
    }

    private void sendSignalArduinoFinish() {
        if (arduinoManager.getArduinos().size() > 1) {
            for (int i = 0; i < randomNumbers.size(); i++) {
                arduinoManager.getArduinos().get(i).setStatus(Arduino.TYPE_LIGHT.FINISH);
            }
        } else {
            arduinoManager.getArduinos().get(0).setStatus(Arduino.TYPE_LIGHT.FINISH);
        }
    }

    private void gameFisnished() {

    }


    public void cancelGame() {
        skillCourtGame.cancelGame();
    }

    public void saveFirebase() {
        Game game = new Game(skillCourtGame.getScore(), skillCourtGame.getGreenPad(), skillCourtGame.getRedPad(), skillCourtGame.getTotalHits(), skillCourtGame.getTimeObjective(), skillCourtGame.getGreenHits(), skillCourtGame.getGameTimeTotal(), skillCourtGame.getAccuracy(), skillCourtGame.getGameMode().toString(), new Date().getTime());
        view.saveFirebase(game);
    }

}
