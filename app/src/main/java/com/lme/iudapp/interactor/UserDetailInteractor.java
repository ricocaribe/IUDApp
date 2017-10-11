package com.lme.iudapp.interactor;


import android.content.Context;

import com.lme.iudapp.model.User;

public interface UserDetailInteractor {

    interface UserDetailView {
        Context getContext();
        void showAlert(String message);
        void showProgressDialog();
        void dismissProgressDialog();
        void refreshUsersListView();
        void showSnackbar();
    }

    interface UserDetailPresenter {
        void setVista(UserDetailView userDetailView);
        void removeUser(int id);
        void updateUser(User user, boolean undo);
    }
}
