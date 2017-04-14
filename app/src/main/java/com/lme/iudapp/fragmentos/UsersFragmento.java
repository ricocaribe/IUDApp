package com.lme.iudapp.fragmentos;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lme.iudapp.MainActivity;
import com.lme.iudapp.R;
import com.lme.iudapp.utilidades.Endpoints;
import com.lme.iudapp.adaptadores.UsersAdaptador;
import com.lme.iudapp.entidades.Usuario;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class UsersFragmento extends Fragment implements UsersAdaptador.UserActions{

    private RecyclerView users_rv;
    private SwipeRefreshLayout users_refresh_layout;
    private UsersAdaptador usersAdaptador;

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
                // Refresh items
                new GetUsersTask().execute();
            }
        });

        return view;
    }

    @Override
    public void getUserToUpdate(View v, int id, String nombre, String birthdate) {
        new GetUserUpdateTask().execute(id);
    }

    @Override
    public void getUserToRemove(View v, int id) {
        new GetUserRemoveTask().execute(id);
    }

    public void getUsers(){
        new GetUsersTask().execute();
    }


    private class GetUsersTask extends AsyncTask<Void, Void, ArrayList<Usuario>> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());
            progress.setTitle(getResources().getString(R.string.cargando));
            progress.setMessage(getResources().getString(R.string.espere_cargando));
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected ArrayList<Usuario> doInBackground(Void... params) {

            try {
                return Endpoints.getUsers();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<Usuario> result) {
            super.onPostExecute(result);
            if(null!=result){
                usersAdaptador = null;
                usersAdaptador = new UsersAdaptador(result, getActivity());
                usersAdaptador.setUserActionsClickListener(UsersFragmento.this);
                users_rv.setAdapter(usersAdaptador);
                users_rv.getAdapter().notifyDataSetChanged();
                progress.dismiss();

                users_refresh_layout.setRefreshing(false);
            }
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
                Log.i("Usuario existe->remove", result.getName());
                new RemoveUsersTask().execute(result.getId());
            }
        }
    }


    private class RemoveUsersTask extends AsyncTask<Integer, Void, Usuario> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Usuario doInBackground(Integer... userId) {

            try {
                return Endpoints.removeUser(userId[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Usuario result) {
            super.onPostExecute(result);
            if(null!=result){
                Log.i("Usuario borrado", result.getName());
                new GetUsersTask().execute();
            }
        }
    }


    private class GetUserUpdateTask extends AsyncTask<Integer, Void, Usuario> {

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
                Log.i("Usuario existe->update", result.getName());
                new UpdateUserTask().execute(result);
            }
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
                Log.i("Usuario borrado", result.getName());
                new GetUsersTask().execute();
            }
        }
    }
}
