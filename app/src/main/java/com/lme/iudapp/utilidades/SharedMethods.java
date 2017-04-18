package com.lme.iudapp.utilidades;

import android.view.View;
import android.widget.EditText;

import com.lme.iudapp.entidades.User;

public interface SharedMethods {
    void getUserToUpdate(View v, User user);

    void getUserToRemove(View v, int id);

    String dateToIsoConverter(String date);

    boolean isValidUser(EditText userName, EditText userBirthdate);

    void saveTempUser(User user);

    void removeTempUser();
}
