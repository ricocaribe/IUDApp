package com.lme.iudapp.fragmentos;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lme.iudapp.R;
import com.lme.iudapp.Utilidades.Endpoints;
import com.lme.iudapp.adaptadores.UsersAdaptador;
import com.lme.iudapp.entidades.Usuario;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class UsersFragmento extends Fragment {

    private RecyclerView users_rv;
    private ProgressDialog progress;
    private SwipeRefreshLayout users_refresh_layout;

    public UsersFragmento() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        users_rv = (RecyclerView) view.findViewById(R.id.birthdates_rv);
        users_rv.setHasFixedSize(true);
        //registerForContextMenu(users_rv);

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


    private class GetUsersTask extends AsyncTask<Void, Void, ArrayList<Usuario>> {

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
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                users_rv.setLayoutManager(llm);
                UsersAdaptador usersAdaptador = new UsersAdaptador(result);
                users_rv.setAdapter(usersAdaptador);
                users_rv.getAdapter().notifyDataSetChanged();
                progress.dismiss();

                users_refresh_layout.setRefreshing(false);
            }
        }
    }
}
