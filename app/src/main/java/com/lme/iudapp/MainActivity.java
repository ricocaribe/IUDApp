package com.lme.iudapp;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;

import com.lme.iudapp.fragmentos.UsersFragmento;
import com.lme.iudapp.utilidades.Endpoints;
import com.lme.iudapp.entidades.Usuario;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    private EditText userBirthdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(onClickToCreate);
    }

    View.OnClickListener onClickToCreate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showCreateDialog();
        }
    };

    private class CreateUsersTask extends AsyncTask<Usuario, Void, Usuario> {

        UsersFragmento usersFragmento = (UsersFragmento) getSupportFragmentManager().findFragmentById(R.id.usersFragment);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (usersFragmento != null && usersFragmento.isInLayout()) {
                usersFragmento.showLoadingDialog();
            }
        }

        @Override
        protected Usuario doInBackground(Usuario... user) {
            try {
                return Endpoints.createUser(user[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Usuario result) {
            super.onPostExecute(result);
            if (usersFragmento != null && usersFragmento.isInLayout()) {
                usersFragmento.dismissLoadingDialog();
            }
            if(null!=result){
                if (usersFragmento != null && usersFragmento.isInLayout()) {
                    usersFragmento.getUsers();
                }
                Log.i("Usuario creado", result.getName());
            }
        }
    }

    public void showCreateDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle(getResources().getString(R.string.crear_usuario_titulo));

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View editDialoglayout = inflater.inflate(R.layout.dialog_layout, null);

        final EditText userName = (EditText) editDialoglayout.findViewById(R.id.edt_user_name);
        userName.setHint(getResources().getString(R.string.editar_nombre_usuario));
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
        userBirthdate.setHint(getResources().getString(R.string.editar_birthdate_usuario));
        userBirthdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        alert.setView(editDialoglayout);

        alert.setPositiveButton(getResources().getString(R.string.boton_aceptar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                UsersFragmento usersFragmento = (UsersFragmento) getSupportFragmentManager().findFragmentById(R.id.usersFragment);

                if(userName.getError()==null &&
                    !userName.getText().toString().equals("") &&
                    !userBirthdate.getText().toString().equals("") &&
                    usersFragmento != null && usersFragmento.isInLayout()){
                        Usuario usuario = new Usuario();
                        usuario.setName(userName.getText().toString());
                        usuario.setBirthdate(usersFragmento.dateToIsoConverter(userBirthdate.getText().toString()));
                        new CreateUsersTask().execute(usuario);
                }
                else {
                    if (usersFragmento != null && usersFragmento.isInLayout()) {
                        usersFragmento.showSnackBarError(getResources().getString(R.string.crear_usuario_error), false);
                    }
                }
            }
        });

        alert.setNegativeButton(getResources().getString(R.string.boton_cancelar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }


    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        userBirthdate.setText(sdf.format(myCalendar.getTime()));
    }
}
