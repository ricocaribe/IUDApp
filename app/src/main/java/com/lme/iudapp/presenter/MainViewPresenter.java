package com.lme.iudapp.presenter;


import com.lme.iudapp.api.IudApiClient;
import com.lme.iudapp.api.IudApiInterface;
import com.lme.iudapp.interactor.MainInteractor;
import com.lme.iudapp.interactor.UsersInteractor;
import com.lme.iudapp.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewPresenter implements MainInteractor.MainPresenter {

    private MainInteractor.MainView mainView;


    @Override
    public void setVista(MainInteractor.MainView mainView) {
        this.mainView = mainView;
    }


    @Override
    public void createUser(User user) {

        mainView.showProgressDialog();

        IudApiClient.apiService().create(IudApiInterface.class).createUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> usersResponse) {

                mainView.refreshUsersFragment();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mainView.dismissProgressDialog();
                mainView.showAlert();
                call.cancel();
                t.printStackTrace();
            }
        });
    }
}
