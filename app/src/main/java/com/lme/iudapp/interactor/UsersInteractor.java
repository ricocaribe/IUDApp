package com.lme.iudapp.interactor;

import com.lme.iudapp.model.User;

import java.util.List;

public interface UsersInteractor {

    interface UsersView {
        void showAlert();
        void showProgressDialog();
        void dismissProgressDialog();
        void setUsersAdapter(List<User> users);
        void refreshUsers();
        void showRemoveUserDialog(int userId);

    }

    interface UsersPresenter {
        void setVista(UsersView usersView);
        void getAllUsers();
        void removeUser(int id);

    }
}
