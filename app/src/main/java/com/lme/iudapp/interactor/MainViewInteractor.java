package com.lme.iudapp.interactor;


import android.content.Context;

import com.lme.iudapp.model.User;

public interface MainViewInteractor {

    interface MainView {
        Context getContext();
        void showAlert(String message);
        void showProgressDialog();
        void dismissProgressDialog();
        void showUserDetailFragment(User user);
        void showUsersListFragment();
    }

    interface MainPresenter {
        void setVista(MainView mainView);
        void createUser(User user);
    }
}
