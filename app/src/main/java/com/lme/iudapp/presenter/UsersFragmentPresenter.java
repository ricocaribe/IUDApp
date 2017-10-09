package com.lme.iudapp.presenter;


import com.lme.iudapp.api.IudApiClient;
import com.lme.iudapp.api.IudApiInterface;
import com.lme.iudapp.interactor.UsersInteractor;
import com.lme.iudapp.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersFragmentPresenter implements UsersInteractor.UsersPresenter {

    private UsersInteractor.UsersView usersView;


    @Override
    public void setVista(UsersInteractor.UsersView usersView) {
        this.usersView = usersView;
    }

    @Override
    public void getAllUsers() {
        usersView.showProgressDialog();

        IudApiClient.apiService().create(IudApiInterface.class).getAllusers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> usersResponse) {

                usersView.dismissProgressDialog();

                if (null != usersResponse.body()) {
                    usersView.setUsersAdapter(usersResponse.body());
                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                usersView.dismissProgressDialog();
                usersView.showAlert();
                call.cancel();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void removeUser(int userId) {
        usersView.showProgressDialog();

        IudApiClient.apiService().create(IudApiInterface.class).removeUser(userId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> usersResponse) {

                usersView.dismissProgressDialog();

                usersView.refreshUsers();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                usersView.dismissProgressDialog();
                usersView.showAlert();
                call.cancel();
                t.printStackTrace();
            }
        });
    }
}
