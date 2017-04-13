package com.lme.iudapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;

import com.lme.iudapp.Utilidades.Endpoints;
import com.lme.iudapp.adaptadores.UsersAdaptador;
import com.lme.iudapp.entidades.Usuario;
import com.lme.iudapp.fragmentos.UsersFragmento;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario usuario = new Usuario();
                usuario.setName("usuario3");
                usuario.setBirthdate("1957-04-05T00:00:00");
                new CreateUsersTask().execute(usuario);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Añadir cumpleaños", null).show();
            }
        });
    }

    private class CreateUsersTask extends AsyncTask<Usuario, Void, Usuario> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
            if(null!=result){
                Log.i("Usuario creado", result.getName());
            }
        }
    }
}
