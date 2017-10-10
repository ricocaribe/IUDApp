package com.lme.iudapp.interactor;


import com.lme.iudapp.model.User;

public interface MainViewInteractor {

    interface MainView {
        void showAlert();
        void showProgressDialog();
        void dismissProgressDialog();
        void showUserDetailFragment(User user);
        void refreshUserDetailFragment(User user);
        void showUsersListFragment();
    }

    interface MainPresenter {
        void setVista(MainView mainView);

    }
}
