package com.lme.iudapp.presenter;


import com.lme.iudapp.api.IudApiClient;
import com.lme.iudapp.api.IudApiInterface;
import com.lme.iudapp.interactor.UserDetailInteractor;
import com.lme.iudapp.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailPresenter implements UserDetailInteractor.UserDetailPresenter {

    private UserDetailInteractor.UserDetailView userDetailView;

    @Override
    public void setVista(UserDetailInteractor.UserDetailView userDetailView) {
        this.userDetailView = userDetailView;
    }


    @Override
    public void removeUser(int userId) {

        userDetailView.showProgressDialog();

        IudApiClient.apiService().create(IudApiInterface.class).removeUser(userId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> usersResponse) {

                userDetailView.dismissProgressDialog();

                userDetailView.refreshUsersListView();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                userDetailView.dismissProgressDialog();
                userDetailView.showAlert();
                call.cancel();
                t.printStackTrace();
            }
        });
    }


    @Override
    public void updateUser(User user, final boolean undo) {

        userDetailView.showProgressDialog();

        IudApiClient.apiService().create(IudApiInterface.class).updateUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> usersResponse) {

                userDetailView.dismissProgressDialog();

                if(!undo) userDetailView.showSnackbar();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                userDetailView.dismissProgressDialog();
                userDetailView.showAlert();
                call.cancel();
                t.printStackTrace();
            }
        });
    }
}
