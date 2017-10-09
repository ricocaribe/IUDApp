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


    private void removeUser(int userId) {

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


    @Override
    public void getUserToRemove(final int userId) {

        usersView.showProgressDialog();

        IudApiClient.apiService().create(IudApiInterface.class).getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> usersResponse) {

                removeUser(userId);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                usersView.dismissProgressDialog();
                usersView.showAlert();
                call.cancel();
                t.printStackTrace();
            }
        });
    }


    private void updateUser(User user) {

        IudApiClient.apiService().create(IudApiInterface.class).updateUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> usersResponse) {

                usersView.dismissProgressDialog();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                usersView.dismissProgressDialog();
                usersView.showAlert();
                call.cancel();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void getUserToUpdate(final int userId) {

        usersView.showProgressDialog();

        IudApiClient.apiService().create(IudApiInterface.class).getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> usersResponse) {

                updateUser(usersResponse.body());

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                usersView.dismissProgressDialog();
                usersView.showAlert();
                call.cancel();
                t.printStackTrace();
            }
        });
    }
}
