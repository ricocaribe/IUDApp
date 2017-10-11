package com.lme.iudapp.presenter;


import com.lme.iudapp.R;
import com.lme.iudapp.api.IudApiClient;
import com.lme.iudapp.api.IudApiInterface;
import com.lme.iudapp.interactor.MainViewInteractor;
import com.lme.iudapp.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainViewPresenter implements MainViewInteractor.MainPresenter {

    private MainViewInteractor.MainView mainView;


    @Override
    public void setVista(MainViewInteractor.MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void createUser(User user) {
        mainView.showProgressDialog();

        IudApiClient.apiService().create(IudApiInterface.class).createUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> usersResponse) {

                mainView.dismissProgressDialog();

                if (usersResponse.isSuccessful()) mainView.showUserDetailFragment(usersResponse.body());
                else mainView.showAlert(usersResponse.message());

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mainView.dismissProgressDialog();
                mainView.showAlert(mainView.getContext().getResources().getString(R.string.error_something_wrong));
                call.cancel();
                t.printStackTrace();
            }
        });
    }
}
