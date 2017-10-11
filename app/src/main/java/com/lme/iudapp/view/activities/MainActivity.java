package com.lme.iudapp.view.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lme.iudapp.R;
import com.lme.iudapp.interactor.MainViewInteractor;
import com.lme.iudapp.model.User;
import com.lme.iudapp.module.MainModule;
import com.lme.iudapp.utils.DateDialog;
import com.lme.iudapp.utils.DateUtils;
import com.lme.iudapp.view.fragments.UserDetailFragment;
import com.lme.iudapp.view.fragments.UsersListFragment;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class MainActivity extends AppCompatActivity implements MainViewInteractor.MainView{


    @Inject
    MainViewInteractor.MainPresenter mainPresenter;

    private AlertDialog pdChecking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ObjectGraph objectGraph = ObjectGraph.create(new MainModule());
        objectGraph.inject(this);

        mainPresenter.setVista(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        showUsersListFragment();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_users_list, menu);
        MenuItem createUserItem = menu.findItem(R.id.action_create);

        createUserItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showCreateDialog();
                return false;
            }
        });

        return true;
    }


    @Override
    public void showUsersListFragment(){

        UsersListFragment usersListFragment = UsersListFragment.newInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainLayout, usersListFragment);
        transaction.commit();

    }


    @Override
    public void showUserDetailFragment(User user) {
        UserDetailFragment userDetailFragment = UserDetailFragment.newInstance(user);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainLayout, userDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void showAlert(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle(getResources().getString(R.string.app_name));
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showUsersListFragment();
                    }
                });
        alertDialog.show();
    }


    @Override
    public void showProgressDialog() {
        pdChecking = new AlertDialog.Builder(MainActivity.this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_bar, null);
        pdChecking.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pdChecking.setCancelable(false);
        pdChecking.setView(dialogView);
        pdChecking.show();
    }


    @Override
    public void dismissProgressDialog() {
        if(null!=pdChecking && pdChecking.isShowing()) pdChecking.cancel();
    }


    public void showCreateDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle(getResources().getString(R.string.txt_create_user_title));

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View editDialoglayout = inflater.inflate(R.layout.dialog_layout, null);

        alert.setView(editDialoglayout);
        alert.setPositiveButton(getResources().getString(R.string.txt_btn_create_user), null);

        final AlertDialog dialog = alert.create();
        dialog.show();

        final EditText userName = editDialoglayout.findViewById(R.id.edtUserName);

        final EditText userBirthdate = editDialoglayout.findViewById(R.id.edtUserBirthdate);
        userBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dialog=new DateDialog(v);
                dialog.show(getFragmentManager(), "DatePicker");
            }
        });

        Button acceptBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!userName.getText().toString().isEmpty() && !userBirthdate.getText().toString().isEmpty()) {
                    dialog.dismiss();
                    User user = new User();
                    user.setName(userName.getText().toString());
                    user.setBirthdate(DateUtils.readableDateToISO(userBirthdate.getText().toString()));
                    mainPresenter.createUser(user);
                }
                else Toast.makeText(getApplicationContext(), getResources().getString(R.string.txt_error_empty_username), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
