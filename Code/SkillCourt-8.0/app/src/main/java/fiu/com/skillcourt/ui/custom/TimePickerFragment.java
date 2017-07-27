package fiu.com.skillcourt.ui.custom;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

/**
 * Created by pedrocarrillo on 10/3/16.
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private int minutes = 0;
    private int hour = 0;

    public void setOnTimerPickerDialog(TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        this.onTimeSetListener = onTimeSetListener;
    }

    public void setPreviousTime(int minutes, int hour) {
        this.minutes = minutes;
        this.hour = hour;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), this, hour, minutes,true);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minutes = minute;
        onTimeSetListener.onTimeSet(view,hourOfDay,minute);
    }
}