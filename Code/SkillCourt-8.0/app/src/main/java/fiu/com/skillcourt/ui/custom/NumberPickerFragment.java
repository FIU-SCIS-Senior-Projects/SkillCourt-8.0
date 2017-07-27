package fiu.com.skillcourt.ui.custom;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import fiu.com.skillcourt.R;

/**
 * Created by pedrocarrillo on 11/3/16.
 */

public class NumberPickerFragment extends DialogFragment {

    NumericDialogListener onClickListener;
    NumberPicker numberPicker;
    int max = 0;

    public void setOnClickListener(NumericDialogListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        View numberPickerView = inflater.inflate(R.layout.layout_numeric_dialog, null);
        numberPicker = (NumberPicker) numberPickerView.findViewById(R.id.number_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(max);
        alert.setView(numberPickerView);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onClickListener.onNumberSelected(numberPicker.getValue());
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        return alert.create();
    }

    public void setupNumberPicker(int max) {
        this.max = max;
    }

    public void setValue(int value) {
        numberPicker.setValue(value);
    }

    public interface NumericDialogListener {
        void onNumberSelected(int number);
    }

}
