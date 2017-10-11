package com.lme.iudapp.interactor;

import android.content.Context;

import com.lme.iudapp.model.User;

import java.util.List;

public interface UsersListInteractor {

    interface UsersView {
        Context getContext();
        void showAlert(String message);
        void showProgressDialog();
        void dismissProgressDialog();
        void setUsersListAdapter(List<User> users);
        void goUserDetail(User user);
        void getUserInfo(int userId);
    }

    interface UsersPresenter {
        void setVista(UsersView usersView);
        void getAllUsers(String filter);
        void getUser(int id);
    }
}
