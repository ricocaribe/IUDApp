package com.lme.iudapp.view.fragments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.lme.iudapp.R;
import com.lme.iudapp.interactor.MainViewInteractor;
import com.lme.iudapp.interactor.UsersListInteractor;
import com.lme.iudapp.model.User;
import com.lme.iudapp.module.UsersListModule;
import com.lme.iudapp.view.adapters.UsersListAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemSelected;
import dagger.ObjectGraph;


public class UsersListFragment extends Fragment implements UsersListInteractor.UsersView {


    @Inject
    UsersListInteractor.UsersPresenter usersPresenter;

    @InjectView(R.id.usersListSwipeRefreshLayout)
    SwipeRefreshLayout usersListSwipeRefreshLayout;

    @InjectView(R.id.rvUsersList)
    RecyclerView rvUsersList;

    @InjectView(R.id.filterSpinner)
    AppCompatSpinner filterSpinner;

    private MainViewInteractor.MainView mainView;
    private UsersListAdapter usersListAdapter;
    public String currentFilterTag;

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
        ButterKnife.inject(this, view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvUsersList.setLayoutManager(gridLayoutManager);

        usersListAdapter = new UsersListAdapter(usersPresenter);

        currentFilterTag = getActivity().getResources().getString(R.string.txt_default_spinner_tag);

        usersListSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                usersPresenter.getAllUsers(currentFilterTag);
            }
        });

        filterSpinner.setSelection(0);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Cumpleaños");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

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
    public void showAlert(String message) {
        mainView.showAlert(message);
    }


    @Override
    public void showProgressDialog() {
        mainView.showProgressDialog();
    }


    @Override
    public void dismissProgressDialog() {
        mainView.dismissProgressDialog();
        if(usersListSwipeRefreshLayout.isRefreshing()) usersListSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void showUserDetail(User user) {
        mainView.showUserDetailFragment(user);
    }


    @Override
    public Context getContext() {
        return getActivity();
    }

    @OnItemSelected(R.id.filterSpinner)
    public void spinnerItemSelected(Spinner spinner, int position) {
        currentFilterTag = spinner.getItemAtPosition(position).toString();
        usersPresenter.getAllUsers(currentFilterTag);
    }


    public void setUsersListAdapter(List<User> users) {
        usersListAdapter.setUsers(users);
        rvUsersList.setAdapter(usersListAdapter);
    }
}
