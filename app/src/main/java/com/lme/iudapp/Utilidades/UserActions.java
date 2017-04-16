package com.lme.iudapp.utilidades;

import android.view.View;

import com.lme.iudapp.entidades.Usuario;

public interface UserActions {
    void showAlert(int errorCode);

    void showEditedBar();

    void getUserToUpdate(View v, Usuario user, Usuario lastEditedUser);

    void getUserToRemove(View v, int id);

    String dateToIsoConverter(String date);

    boolean sameUsers(Usuario user1, Usuario user2);

}
