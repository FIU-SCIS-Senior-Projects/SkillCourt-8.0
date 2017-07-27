package fiu.com.skillcourt.ui.startgame;

import fiu.com.skillcourt.entities.Game;

/**
 * Created by pedrocarrillo on 9/22/16.
 */

public interface StartGameView {

    void setupInitGame();
    void setupFinishGame();
    void setTimerText(String time);
    void setProgressTotal(int seconds);
    void changeProgressView(long seconds);
    void updateResult(float totalHits, float greenHits, final float redHits, int score, int accuracy);
    void saveFirebase(Game game);

}
