package com.lme.iudapp.interactor;


import com.lme.iudapp.model.User;

public interface UserDetailInteractor {

    interface UserDetailView {
        void showAlert();
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
