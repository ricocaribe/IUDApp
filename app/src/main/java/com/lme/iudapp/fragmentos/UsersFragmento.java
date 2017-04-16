package com.lme.iudapp.fragmentos;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lme.iudapp.R;
import com.lme.iudapp.utilidades.Endpoints;
import com.lme.iudapp.adaptadores.UsersAdaptador;
import com.lme.iudapp.entidades.Usuario;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class UsersFragmento extends Fragment implements UsersAdaptador.UserActions{

    private RecyclerView users_rv;
    private SwipeRefreshLayout users_refresh_layout;
    private ProgressDialog progress;
    private Usuario lastEditedUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        users_rv = (RecyclerView) view.findViewById(R.id.birthdates_rv);
        users_rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        users_rv.setLayoutManager(llm);
        users_refresh_layout = (SwipeRefreshLayout) view.findViewById(R.id.users_refresh_layout);

        new GetUsersTask().execute();

        users_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();
            }
        });

        return view;
    }

    @Override
    public void showMessage(String errorMessage) {
        showSnackBar(errorMessage, false);
    }

    @Override
    public void getUserToUpdate(View v, Usuario user, Usuario lastUser) {
        lastEditedUser = lastUser;
        new GetUserUpdateTask().execute(user);
    }

    @Override
    public void getUserToRemove(View v, int id) {
        new GetUserRemoveTask().execute(id);
    }

    @Override
    public String dateToIsoConverter(String date) {
        return readableDateToISO(date);
    }

    @Override
    public boolean sameUsers(Usuario user1, Usuario user2){
        return user1.getId()==user2.getId() &&
                user1.getName().equals(user2.getName()) &&
                user1.getBirthdate().equals(user2.getBirthdate());
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

    public void getUsers(){
        new GetUsersTask().execute();
    }

    private class GetUsersTask extends AsyncTask<Void, Void, ArrayList<Usuario>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingDialog();
        }

        @Override
        protected ArrayList<Usuario> doInBackground(Void... params) {
            return Endpoints.getUsers();
        }

        @Override
        protected void onPostExecute(ArrayList<Usuario> result) {
            super.onPostExecute(result);
            dismissLoadingDialog();
            if(null!=result){
                UsersAdaptador usersAdaptador = new UsersAdaptador(result, getActivity());
                usersAdaptador.setUserActionsClickListener(UsersFragmento.this);
                users_rv.setAdapter(usersAdaptador);
                users_rv.getAdapter().notifyDataSetChanged();
                users_refresh_layout.setRefreshing(false);
            }
            else showErrorAlert();

        }
    }


    private class GetUserRemoveTask extends AsyncTask<Integer, Void, Usuario> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Usuario doInBackground(Integer... userId) {

            try {
                return Endpoints.getUser(userId[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Usuario result) {
            super.onPostExecute(result);
            if(null!=result){
                Log.i(getClass().getSimpleName(), String.format("Usuario existe, borramos: %s", result.getName()));
                new RemoveUsersTask().execute(result.getId());
            }
            else showErrorAlert();
        }
    }


    private class RemoveUsersTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... userId) {

            try {
                Endpoints.removeUser(userId[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.i(getClass().getSimpleName(), "Usuario borrado");
            getUsers();
        }
    }


    private class GetUserUpdateTask extends AsyncTask<Usuario, Void, Usuario> {

        Usuario userToUpdate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Usuario doInBackground(Usuario... user) {

            try {
                userToUpdate = user[0];
                return Endpoints.getUser(user[0].getId());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Usuario result) {
            super.onPostExecute(result);
            if(null!=result){
                Log.i(getClass().getSimpleName(), String.format("Usuario existe, actualizamos: %s", result.getName()));
                new UpdateUserTask().execute(userToUpdate);
            }
            else showErrorAlert();
        }
    }


    private class UpdateUserTask extends AsyncTask<Usuario, Void, Usuario> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Usuario doInBackground(Usuario... user) {

            try {
                return Endpoints.updateUser(user[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Usuario result) {
            super.onPostExecute(result);
            if(null!=result){
                Log.i(getClass().getSimpleName(), String.format("Usuario actualizado a: %s", result.getName()));
                getUsers();
                if(!sameUsers(result, lastEditedUser)) showSnackBar("Usuario editado", true);
            }
            else showErrorAlert();
        }
    }


    public void showSnackBar(String errorMessage, boolean action){
        View parentLayout = getActivity().findViewById(R.id.usersFragment);
        Snackbar snackbar = Snackbar.make(parentLayout, errorMessage, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.RED);
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.BLACK);
        if (action) {
            snackbarView.setBackgroundColor(Color.GRAY);
            textView.setTextColor(Color.WHITE);
            snackbar.setAction(getActivity().getResources().getString(R.string.editar_btn_deshacer), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new GetUserUpdateTask().execute(lastEditedUser);
                }
            });
        }


        snackbar.show();
    }


    public void showErrorAlert(){
        new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getResources().getString(R.string.app_name))
                .setMessage(getActivity().getResources().getString(R.string.mensaje_error_conn))
                .setPositiveButton(getActivity().getResources().getString(R.string.mensaje_error_refresh), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getUsers();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public void showLoadingDialog(){
        progress = new ProgressDialog(getActivity());
        progress.setTitle(getResources().getString(R.string.cargando));
        progress.setMessage(getResources().getString(R.string.espere_cargando));
        progress.setCancelable(false);
        progress.show();
    }


    public void dismissLoadingDialog(){
        if(null!=progress) progress.dismiss();
    }
}
