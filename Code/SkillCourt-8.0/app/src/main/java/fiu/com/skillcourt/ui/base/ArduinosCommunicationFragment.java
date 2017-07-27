package fiu.com.skillcourt.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;

import fiu.com.skillcourt.interfaces.Constants;
import fiu.com.skillcourt.ui.dashboard.MainDashboardActivity;

/**
 * Created by pedrocarrillo on 10/16/16.
 */

public class ArduinosCommunicationFragment extends BaseFragment {

    protected ArduinoErrorBroadcastReceiver arduinoErrorBroadcastReceiver = new ArduinoErrorBroadcastReceiver();
    private boolean errorThrown = false;

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(arduinoErrorBroadcastReceiver, new IntentFilter(Constants.ARDUINOS_DISCONNECTED));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(arduinoErrorBroadcastReceiver);
    }

    public class ArduinoErrorBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent.hasExtra(Constants.errorMessageKey)) {
                String error = intent.getStringExtra(Constants.errorMessageKey);
                showErrorDialog(error);
            }
        }
    }

    protected void showErrorDialog(String error) {
        if (!errorThrown) {
            errorThrown = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(error);
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    terminateActivity();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }



    protected void terminateActivity() {
        fragmentListener.closeActivity();
        Intent intent = new Intent(getActivity(), MainDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
