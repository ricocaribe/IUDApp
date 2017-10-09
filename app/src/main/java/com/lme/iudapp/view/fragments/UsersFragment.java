package com.lme.iudapp.view.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lme.iudapp.R;
import com.lme.iudapp.interactor.UsersInteractor;
import com.lme.iudapp.model.User;
import com.lme.iudapp.module.UsersModule;
import com.lme.iudapp.view.adapters.UsersAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.ObjectGraph;


public class UsersFragment extends Fragment implements UsersInteractor.UsersView {

    private SwipeRefreshLayout users_refresh_layout;
    public String currentFilterTag;
    public User tempUser;

    @Inject
    UsersInteractor.UsersPresenter usersPresenter;
    private ProgressDialog pdChecking;
    private RecyclerView rvUsers;
    private UsersAdapter usersAdapter;

    public static UsersFragment newInstance() {
        return new UsersFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ObjectGraph objectGraph = ObjectGraph.create(new UsersModule());
        objectGraph.inject(this);

        usersPresenter.setVista(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pdChecking = new ProgressDialog(getActivity());

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        rvUsers = (RecyclerView) view.findViewById(R.id.birthdates_rv);
        rvUsers.setHasFixedSize(true);
        LinearLayoutManager llManagerUsers = new LinearLayoutManager(getActivity());
        rvUsers.setLayoutManager(llManagerUsers);
        usersAdapter = new UsersAdapter(this);

        usersPresenter.getAllUsers();


        users_refresh_layout = (SwipeRefreshLayout) view.findViewById(R.id.users_refresh_layout);

        users_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //usersPresenter.getAllUsers(currentFilterTag);
            }
        });

        currentFilterTag = getActivity().getResources().getString(R.string.default_spinner_tag);

        /*AppCompatSpinner appCompatSpinner = (AppCompatSpinner) view.findViewById(R.id.filterSpinner);
        appCompatSpinner.setSelection(0);
        appCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentFilterTag = parent.getItemAtPosition(position).toString();
                getUsers(currentFilterTag);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        return view;
    }

    @Override
    public void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getResources().getString(R.string.app_name));
        alertDialog.setMessage(getResources().getString(R.string.error_something_wrong));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        usersPresenter.getAllUsers();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void showProgressDialog() {
        pdChecking.setCancelable(false);
        pdChecking.setMessage(getResources().getString(R.string.tv_getting_users));
        pdChecking.show();
    }


    @Override
    public void dismissProgressDialog() {
        if(null!=pdChecking && pdChecking.isShowing()) pdChecking.dismiss();
    }

    @Override
    public void setUsersAdapter(List<User> users) {
        usersAdapter.setUsers(users);
        rvUsers.setAdapter(usersAdapter);
    }

    @Override
    public void refreshUsers() {
        usersPresenter.getAllUsers();
    }

    @Override
    public void showRemoveUserDialog(final int userId) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.eliminar_usuario_titulo))
                .setMessage(getResources().getString(R.string.eliminar_usuario_mensaje))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        usersPresenter.getUserToRemove(userId);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_menu_delete)
                .show();
    }



    public void saveTempUser(User user) {
        tempUser = new User();
        tempUser.setId(user.getId());
        tempUser.setName(user.getName());
        tempUser.setBirthdate(user.getBirthdate());
    }

    public void removeTempUser() {
        tempUser = null;
    }


    /*public ArrayList<User> filterUsers(ArrayList<User> userList, String letter){
        ArrayList<User> filteredUsers = new ArrayList<>();
        assert userList != null;
        for (int i = 0; i<userList.size(); i++){
            if (userList.get(i).getName().toUpperCase().startsWith(letter)) filteredUsers.add(userList.get(i));
        }
        return filteredUsers;
    }*/





}
