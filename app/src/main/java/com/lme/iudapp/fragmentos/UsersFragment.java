package com.lme.iudapp.fragmentos;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
import android.widget.EditText;

import com.lme.iudapp.R;
import com.lme.iudapp.entidades.User;
import com.lme.iudapp.utilidades.Endpoints;
import com.lme.iudapp.adaptadores.UsersAdapter;
import com.lme.iudapp.utilidades.ServerException;
import com.lme.iudapp.utilidades.SharedMethods;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class UsersFragment extends Fragment implements SharedMethods {

    private RecyclerView users_rv;
    private SwipeRefreshLayout users_refresh_layout;
    private ProgressDialog progress;

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
    public void getUserToUpdate(View v, User user) {
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
    public boolean isValidUser(EditText userName, EditText userBirthdate) {
        return userName.getError()==null && userBirthdate.getError()==null;
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

    private class GetUsersTask extends AsyncTask<Void, Void, ArrayList<User>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingDialog();
        }

        @Override
        protected ArrayList<User> doInBackground(Void... params) {
            try {
                return Endpoints.getUsers(getActivity());
            } catch (ServerException e) {
                e.printStackTrace();
                showErrorAlert(e.getMessage());
                return null;
            }
            catch (IOException e) {
                e.printStackTrace();
                showErrorAlert(getActivity().getResources().getString(R.string.mensaje_error_conn));
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<User> result) {
            super.onPostExecute(result);
            dismissLoadingDialog();
            if(null!=result){
                UsersAdapter usersAdapter = new UsersAdapter(result, getActivity());
                usersAdapter.setUserActionsClickListener(UsersFragment.this);
                users_rv.setAdapter(usersAdapter);
                users_rv.getAdapter().notifyDataSetChanged();
                users_refresh_layout.setRefreshing(false);
            }
        }
    }


    private class GetUserRemoveTask extends AsyncTask<Integer, Void, User> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(Integer... userId) {

            try {
                return Endpoints.getUser(userId[0], getActivity());
            } catch (ServerException e) {
                e.printStackTrace();
                showErrorAlert(e.getMessage());
                return null;
            }
            catch (IOException e) {
                e.printStackTrace();
                showErrorAlert(getActivity().getResources().getString(R.string.mensaje_error_conn));
                return null;
            }
        }

        @Override
        protected void onPostExecute(User result) {
            super.onPostExecute(result);
            if(null!=result){
                Log.i(getClass().getSimpleName(), String.format("User existe, borramos: %s", result.getName()));
                new RemoveUsersTask().execute(result.getId());
            }
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
                Endpoints.removeUser(userId[0], getActivity());
            } catch (ServerException e) {
                e.printStackTrace();
                showErrorAlert(e.getMessage());
                return null;
            }
            catch (IOException e) {
                e.printStackTrace();
                showErrorAlert(getActivity().getResources().getString(R.string.mensaje_error_conn));
                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.i(getClass().getSimpleName(), "User borrado");
            getUsers();
        }
    }


    private class GetUserUpdateTask extends AsyncTask<User, Void, User> {

        User userToUpdate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(User... user) {

            try {
                userToUpdate = user[0];
                return Endpoints.getUser(user[0].getId(), getActivity());
            } catch (ServerException e) {
                e.printStackTrace();
                showErrorAlert(e.getMessage());
                return null;
            }
            catch (IOException e) {
                e.printStackTrace();
                showErrorAlert(getActivity().getResources().getString(R.string.mensaje_error_conn));
                return null;
            }
        }

        @Override
        protected void onPostExecute(User result) {
            super.onPostExecute(result);
            if(null!=result){
                Log.i(getClass().getSimpleName(), String.format("User existe, actualizamos: %s", result.getName()));
                new UpdateUserTask().execute(userToUpdate);
            }
        }
    }


    private class UpdateUserTask extends AsyncTask<User, Void, User> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(User... user) {

            try {
                return Endpoints.updateUser(user[0], getActivity());
            } catch (ServerException e) {
                e.printStackTrace();
                showErrorAlert(e.getMessage());
                return null;
            }
            catch (IOException e) {
                e.printStackTrace();
                showErrorAlert(getActivity().getResources().getString(R.string.mensaje_error_conn));
                return null;
            }
        }

        @Override
        protected void onPostExecute(User result) {
            super.onPostExecute(result);
            if(null!=result){
                Log.i(getClass().getSimpleName(), String.format("User actualizado a: %s", result.getName()));
                getUsers();
            }
        }
    }


    public void showErrorAlert(final String error){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(getActivity())
                        .setTitle(getActivity().getResources().getString(R.string.app_name))
                        .setMessage(error)
                        .setPositiveButton(getResources().getString(R.string.mensaje_error_refresh), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                getUsers();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
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
