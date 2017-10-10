package com.lme.iudapp.view.fragments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.lme.iudapp.R;
import com.lme.iudapp.interactor.MainViewInteractor;
import com.lme.iudapp.interactor.UsersListInteractor;
import com.lme.iudapp.model.User;
import com.lme.iudapp.module.UsersListModule;
import com.lme.iudapp.view.adapters.UsersListAdapter;

import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;


public class UsersListFragment extends Fragment implements UsersListInteractor.UsersView {

    public String currentFilterTag;
    private MainViewInteractor.MainView mainView;
    private SwipeRefreshLayout users_refresh_layout;

    @Inject
    UsersListInteractor.UsersPresenter usersPresenter;
    private RecyclerView rvUsers;
    private UsersListAdapter usersListAdapter;

    public static UsersListFragment newInstance() {
        return new UsersListFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ObjectGraph objectGraph = ObjectGraph.create(new UsersListModule());
        objectGraph.inject(this);

        usersPresenter.setVista(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        rvUsers = view.findViewById(R.id.birthdates_rv);
        rvUsers.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvUsers.setLayoutManager(gridLayoutManager);
        usersListAdapter = new UsersListAdapter(this);

        currentFilterTag = getActivity().getResources().getString(R.string.default_spinner_tag);

        users_refresh_layout = view.findViewById(R.id.users_refresh_layout);

        users_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                usersPresenter.getAllUsers(currentFilterTag);
            }
        });

        AppCompatSpinner appCompatSpinner = view.findViewById(R.id.filterSpinner);
        appCompatSpinner.setSelection(0);
        appCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentFilterTag = parent.getItemAtPosition(position).toString();
                usersPresenter.getAllUsers(currentFilterTag);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainViewInteractor.MainView) {
            mainView = (MainViewInteractor.MainView) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mainView = null;
    }


    @Override
    public void showAlert() {
        mainView.showAlert();
    }


    @Override
    public void showProgressDialog() {
        mainView.showProgressDialog();
    }


    @Override
    public void dismissProgressDialog() {
        mainView.dismissProgressDialog();
        if(users_refresh_layout.isRefreshing()) users_refresh_layout.setRefreshing(false);
    }


    @Override
    public void goUserDetail(User user) {
        mainView.showUserDetailFragment(user);
    }


    @Override
    public void getUserInfo(int userId) {
        usersPresenter.getUser(userId);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    public void setUsersListAdapter(List<User> users) {
        usersListAdapter.setUsers(users);
        rvUsers.setAdapter(usersListAdapter);
    }
}
