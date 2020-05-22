package com.jukic.zavrsni;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;



public class SimpleTimePickerDialog extends AppCompatDialogFragment {

    private NumberPicker minutesPicker;
    private NumberPicker secondsPicker;
    private SimpleTimePickerDialogListener simpleTimePickerDialogListener;
    private int viewID;

    public SimpleTimePickerDialog(View v){
        EditText et = (EditText) v;
        viewID = et.getId();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.time_picker_layout,null);
        builder.setView(view)
                .setTitle(R.string.pick_time)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int minutes = minutesPicker.getValue();
                        int seconds = secondsPicker.getValue();
                        simpleTimePickerDialogListener.applyData(viewID,minutes,seconds);
                    }
                });

        minutesPicker = view.findViewById(R.id.minutesPicker);
        minutesPicker.setMaxValue(60);

        secondsPicker = view.findViewById(R.id.secondsPicker);
        secondsPicker.setMaxValue(59);

        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            simpleTimePickerDialogListener = (SimpleTimePickerDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement SimpleTimePickerDialogListener");
        }
    }

    public interface SimpleTimePickerDialogListener{
        void applyData(int id,int minutes, int seconds);
    }

}
