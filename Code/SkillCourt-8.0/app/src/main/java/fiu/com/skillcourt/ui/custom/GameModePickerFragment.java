package fiu.com.skillcourt.ui.custom;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fiu.com.skillcourt.R;

/**
 * Created by pedrocarrillo on 10/4/16.
 */

public class GameModePickerFragment extends DialogFragment {

    DialogInterface.OnClickListener onClickListener;

    public void setOnClickListener(DialogInterface.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pick_game_mode)
                .setItems(R.array.game_modes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (onClickListener!=null) {
                            onClickListener.onClick(dialog, which);
                        }
                    }
                });
        return builder.create();
    }

}
