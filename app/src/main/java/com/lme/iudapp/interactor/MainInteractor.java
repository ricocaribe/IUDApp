package com.lme.iudapp.interactor;


import com.lme.iudapp.model.User;

public interface MainInteractor {

    interface MainView {
        void showAlert();
        void showProgressDialog();
        void dismissProgressDialog();
        void refreshUsersFragment();

    }

    interface MainPresenter {
        void setVista(MainView mainView);
        void createUser(User user);
    }
}
