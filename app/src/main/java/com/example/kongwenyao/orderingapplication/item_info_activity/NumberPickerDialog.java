package com.example.kongwenyao.orderingapplication.item_info_activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.NumberPicker;

import com.example.kongwenyao.orderingapplication.R;

public class NumberPickerDialog extends DialogFragment {

    private NumberPicker.OnValueChangeListener valueChangeListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Create new number picker widget
        final NumberPicker numberPicker = new NumberPicker(getActivity());
        numberPicker.setValue(1);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BEFORE_DESCENDANTS); //Turn off keyboard
        numberPicker.setBackgroundColor(getContext().getColor(R.color.colorWhite));

        //Create a dialog fragment
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
        alertDialog.setTitle("Choose an amount");
        alertDialog.setMessage("Pick the amount to be added to cart: ");

        //Set dialog positive button
        alertDialog.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                valueChangeListener.onValueChange(numberPicker, numberPicker.getValue(), numberPicker.getValue());
            }
        });

        //Set dialog negative button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //Add number picker to alert dialog
        alertDialog.setView(numberPicker);
        return alertDialog.create();
    }

    //Setter for valueChangeListener
    public void setValueChangeListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }
}
