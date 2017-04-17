package com.lme.iudapp.utilidades;

import android.view.View;
import android.widget.EditText;

import com.lme.iudapp.entidades.Usuario;

public interface SharedMethods {
    void getUserToUpdate(View v, Usuario user, Usuario lastEditedUser);

    void getUserToRemove(View v, int id);

    String dateToIsoConverter(String date);

    boolean isValidUser(EditText userName, EditText userBirthdate);
}
