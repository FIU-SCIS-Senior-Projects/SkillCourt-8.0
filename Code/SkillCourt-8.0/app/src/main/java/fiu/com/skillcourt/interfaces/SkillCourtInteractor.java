package fiu.com.skillcourt.interfaces;

/**
 * Created by pedrocarrillo on 9/22/16.
 */

public interface SkillCourtInteractor {

    void onSecond(String time, long seconds);
    void onMillisecond(long milliseconds);
    void onTimeObjective();
    void onFinish();

}
