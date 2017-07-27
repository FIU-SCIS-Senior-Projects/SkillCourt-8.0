package fiu.com.skillcourt.interfaces;

import fiu.com.skillcourt.connection.Arduino;

/**
 * Created by pedrocarrillo on 10/17/16.
 */

public interface ArduinoSkillCourtInteractor {

    void onMessageReceived(Arduino.TYPE_LIGHT currentStatus, String message);

}
