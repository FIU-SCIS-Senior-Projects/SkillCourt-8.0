package fiu.com.skillcourt.game;

import fiu.com.skillcourt.interfaces.CountdownInterface;

/**
 * Created by pedrocarrillo on 9/22/16.
 */

public class CountDownTimer extends android.os.CountDownTimer {

    public CountdownInterface gameTimerViewInteractor;

    public CountDownTimer(long howLong, long timelapse, CountdownInterface gameTimerViewInteractor) {
        super(howLong, timelapse);
        this.gameTimerViewInteractor = gameTimerViewInteractor;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        gameTimerViewInteractor.onTick(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        gameTimerViewInteractor.onFinish();
    }

}
