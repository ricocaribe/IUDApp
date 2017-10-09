package com.lme.iudapp.view.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import com.lme.iudapp.R;
import com.lme.iudapp.model.User;
import com.lme.iudapp.utils.Endpoints;
import com.lme.iudapp.utils.ServerException;
import com.lme.iudapp.view.fragments.UsersFragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    private EditText userBirthdate;
    private UsersFragment usersFragment;
    private Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(onClickToCreate);

        usersFragment = (UsersFragment) getSupportFragmentManager().findFragmentById(R.id.usersFragment);
    }

    View.OnClickListener onClickToCreate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //showCreateDialog();
        }
    };

    /*private class CreateUsersTask extends AsyncTask<User, Void, User> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(User... user) {
            try {
                return Endpoints.createUser(user[0], MainActivity.this);
            } catch (ServerException e) {
                e.printStackTrace();
                usersFragment.showErrorAlert(e.getMessage());
                return null;
            }
            catch (IOException e) {
                e.printStackTrace();
                usersFragment.showErrorAlert(getResources().getString(R.string.mensaje_error_conn));
                return null;
            }
        }

        @Override
        protected void onPostExecute(User result) {
            super.onPostExecute(result);
            usersFragment.getUsers(usersFragment.currentFilterTag);
            Log.i("User creado", result.getName());
        }
    }*/

    /*public void showCreateDialog(){
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

    }*/

    /*private class AcceptDialogBtn implements View.OnClickListener {
        private final Dialog dialog;
        private final EditText userName;

        AcceptDialogBtn(Dialog dialog, EditText userName) {
            this.dialog = dialog;
            this.userName = userName;
        }

        @Override
        public void onClick(View v) {

            if(usersFragment.isValidUser(userName, userBirthdate)) {
                User user = new User();
                user.setName(userName.getText().toString());
                user.setBirthdate(usersFragment.dateToIsoConverter(userBirthdate.getText().toString()));
                dialog.dismiss();
                new CreateUsersTask().execute(user);
            }
        }
    }*/


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
}
