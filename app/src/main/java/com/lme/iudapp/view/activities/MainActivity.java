package com.lme.iudapp.view.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.lme.iudapp.R;
import com.lme.iudapp.interactor.MainInteractor;
import com.lme.iudapp.model.User;
import com.lme.iudapp.module.MainModule;
import com.lme.iudapp.utils.Endpoints;
import com.lme.iudapp.utils.ServerException;
import com.lme.iudapp.view.fragments.UsersFragment;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class MainActivity extends AppCompatActivity implements MainInteractor.MainView{

    @Inject
    MainInteractor.MainPresenter mainPresenter;
    private ProgressDialog pdChecking;
    private EditText userBirthdate;
    private Calendar myCalendar = Calendar.getInstance();
    private FrameLayout main_layout;
    private UsersFragment usersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ObjectGraph objectGraph = ObjectGraph.create(new MainModule());
        objectGraph.inject(this);

        mainPresenter.setVista(this);

        main_layout = (FrameLayout) findViewById(R.id.mainLayout);

        pdChecking = new ProgressDialog(MainActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(onClickToCreate);


        showUsersFragment();
    }

    View.OnClickListener onClickToCreate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showCreateDialog();
        }
    };


    private void showUsersFragment(){

        usersFragment = UsersFragment.newInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainLayout, usersFragment);
        transaction.commit();
    }


    public void showCreateDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle(getResources().getString(R.string.crear_usuario_titulo));

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View editDialoglayout = inflater.inflate(R.layout.dialog_layout, null);

        final EditText userName = (EditText) editDialoglayout.findViewById(R.id.edt_user_name);
        userName.setHint(getResources().getString(R.string.edt_nombre_usuario));
        userName.setError(getString(R.string.editar_nombre_usuario_error));
        userName.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (userName.getText().toString().length() <= 0) {
                    userName.setError(getResources().getString(R.string.editar_nombre_usuario_error));
                } else {
                    userName.setError(null);
                }
            }
        });

        userBirthdate = (EditText) editDialoglayout.findViewById(R.id.edt_user_birthdate);
        userBirthdate.setHint(getResources().getString(R.string.edt_fecha_usuario));
        userBirthdate.setError(getString(R.string.editar_birthdate_usuario_error));
        userBirthdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        alert.setView(editDialoglayout);
        alert.setPositiveButton(getResources().getString(R.string.btn_aceptar), null);
        alert.setNegativeButton(getResources().getString(R.string.btn_cancelar), null);

        AlertDialog dialog = alert.create();
        dialog.show();
        Button acceptBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        acceptBtn.setOnClickListener(new AcceptDialogBtn(dialog, userName));

    }

    private class AcceptDialogBtn implements View.OnClickListener {
        private final Dialog dialog;
        private final EditText userName;

        AcceptDialogBtn(Dialog dialog, EditText userName) {
            this.dialog = dialog;
            this.userName = userName;
        }

        @Override
        public void onClick(View v) {

            if(isValidUser(userName, userBirthdate)) {
                User user = new User();
                user.setName(userName.getText().toString());
                user.setBirthdate(dateToIsoConverter(userBirthdate.getText().toString()));
                dialog.dismiss();
                mainPresenter.createUser(user);
            }
        }
    }

    public boolean isValidUser(EditText userName, EditText userBirthdate) {
        return userName.getError()==null && userBirthdate.getError()==null;
    }

    public String dateToIsoConverter(String date) {
        return readableDateToISO(date);
    }

    private String readableDateToISO(String isoDate){
        SimpleDateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

        try {
            Date date = originalFormat.parse(isoDate);
            return targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            userBirthdate.setError(null);
            userBirthdate.setText(sdf.format(myCalendar.getTime()));
        }
    };

    @Override
    public void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(getResources().getString(R.string.app_name));
        alertDialog.setMessage(getResources().getString(R.string.error_something_wrong));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showUsersFragment();
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
    public void refreshUsersFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.detach(usersFragment);
        transaction.attach(usersFragment);
        transaction.commit();
    }
}
