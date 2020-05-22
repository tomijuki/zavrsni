package com.jukic.zavrsni;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.jukic.zavrsni.run.RunListActivity;

public class ConfirmExerciseDialog extends AppCompatDialogFragment {

    private ConfirmExerciseDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.exercise_dialog_title)
                .setMessage(R.string.exercise_message)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onConfirmed();
                    }
                });
        return builder.create();
    }

    public interface ConfirmExerciseDialogListener{
        void onConfirmed();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ConfirmExerciseDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement ConfirmExerciseDialogListener.");
        }

    }
}
