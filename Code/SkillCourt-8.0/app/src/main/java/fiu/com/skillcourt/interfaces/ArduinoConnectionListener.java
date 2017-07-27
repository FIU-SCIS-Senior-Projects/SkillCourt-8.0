package fiu.com.skillcourt.interfaces;

import fiu.com.skillcourt.connection.Arduino;
import fiu.com.skillcourt.connection.ArduinoConnectionHandler;

/**
 * Created by pedrocarrillo on 10/14/16.
 */

public interface ArduinoConnectionListener {

    void onConnect(ArduinoConnectionHandler arduinoConnectionHandler);
    void onError(String error);
    void onMessageReceived(Arduino.TYPE_LIGHT currentStatus, String message);

}
