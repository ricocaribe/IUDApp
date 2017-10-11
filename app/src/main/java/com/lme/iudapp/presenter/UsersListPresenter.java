package com.lme.iudapp.presenter;


import com.lme.iudapp.R;
import com.lme.iudapp.api.IudApiClient;
import com.lme.iudapp.api.IudApiInterface;
import com.lme.iudapp.interactor.UsersListInteractor;
import com.lme.iudapp.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersListPresenter implements UsersListInteractor.UsersPresenter {

    private UsersListInteractor.UsersView usersView;


    @Override
    public void setVista(UsersListInteractor.UsersView usersView) {
        this.usersView = usersView;
    }

    @Override
    public void getAllUsers(final String filter) {
        usersView.showProgressDialog();

        IudApiClient.apiService().create(IudApiInterface.class).getAllusers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> usersResponse) {

                usersView.dismissProgressDialog();

                if (usersResponse.isSuccessful()) usersView.setUsersListAdapter(filterUsers(usersResponse.body(), filter));
                else usersView.showAlert(usersResponse.message());

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                usersView.dismissProgressDialog();
                usersView.showAlert(usersView.getContext().getResources().getString(R.string.txt_error_something_wrong));
                call.cancel();
                t.printStackTrace();
            }
        });
    }


    @Override
    public void getUser(final int userId) {

        usersView.showProgressDialog();

        IudApiClient.apiService().create(IudApiInterface.class).getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> usersResponse) {

                usersView.dismissProgressDialog();

                if (usersResponse.isSuccessful()) usersView.goUserDetail(usersResponse.body());
                else usersView.showAlert(usersResponse.message());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                usersView.dismissProgressDialog();
                usersView.showAlert(usersView.getContext().getResources().getString(R.string.txt_error_something_wrong));
                call.cancel();
                t.printStackTrace();
            }
        });
    }


    private List<User> filterUsers(List<User> userList, String filter){
        if(!filter.equals(usersView.getContext().getResources().getString(R.string.default_spinner_tag))){
            List<User> filteredUsers = new ArrayList<>();
            assert userList != null;
            for (int i = 0; i<userList.size(); i++){
                if (userList.get(i).name.toUpperCase().startsWith(filter)) filteredUsers.add(userList.get(i));
            }
            return filteredUsers;
        }

        return userList;
    }
}
