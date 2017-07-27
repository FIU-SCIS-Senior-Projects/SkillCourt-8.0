package fiu.com.skillcourt.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.LinearLayout;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.SkillCourtApp;
import fiu.com.skillcourt.connection.ArduinoManager;
import fiu.com.skillcourt.interfaces.Constants;

/**
 * Created by pedrocarrillo on 10/14/16.
 */

public class ArduinosStartCommunicationFragment extends ArduinosCommunicationFragment {

    protected LinearLayout llGameLayout, llArduinoConnection;
    protected StartArduinoBroadcastReceiver startArduinoBroadcastReceiver = new StartArduinoBroadcastReceiver();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!ArduinoManager.getInstance().isConnected()) {
            ArduinoManager.getInstance().startConnection();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llGameLayout = (LinearLayout)view.findViewById(R.id.game_content);
        llArduinoConnection = (LinearLayout)view.findViewById(R.id.loading_arduinos);
        if (ArduinoManager.getInstance().isConnected()) {
            llGameLayout.setVisibility(View.VISIBLE);
            llArduinoConnection.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(startArduinoBroadcastReceiver, new IntentFilter(Constants.ARDUINOS_CONNECTED));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(startArduinoBroadcastReceiver);
    }

    public class StartArduinoBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            llGameLayout.setVisibility(View.VISIBLE);
            llArduinoConnection.setVisibility(View.GONE);
        }
    }

}
