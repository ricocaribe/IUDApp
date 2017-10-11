package com.lme.iudapp.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;


@SuppressLint("ValidFragment")
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    EditText tvUserBirthdate;
    int userDefaultYear;
    int userDefaultMonth;
    int userDefaultDay;

    public DateDialog(View view) {
        this.tvUserBirthdate= (EditText) view;
        setDefaultDate();
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), this, userDefaultYear, userDefaultMonth, userDefaultDay);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String date=dayOfMonth + "/" + (monthOfYear+1) + "/" + year;
        tvUserBirthdate.setText(date);
    }

    private void setDefaultDate(){
        String[] date = tvUserBirthdate.getText().toString().split("/");
        if(date.length>1){
            this.userDefaultYear = Integer.parseInt(date[2]);
            this.userDefaultMonth = Integer.parseInt(date[1]) - 1;
            this.userDefaultDay = Integer.parseInt(date[0]);
        }
        else {
            final Calendar c = Calendar.getInstance();
            this.userDefaultYear = c.get(Calendar.YEAR);
            this.userDefaultMonth = c.get(Calendar.MONTH);
            this.userDefaultDay = c.get(Calendar.DAY_OF_MONTH);
        }
    }
}
