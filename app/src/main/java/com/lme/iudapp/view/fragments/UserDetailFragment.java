package com.lme.iudapp.view.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.lme.iudapp.R;
import com.lme.iudapp.interactor.MainViewInteractor;
import com.lme.iudapp.interactor.UserDetailInteractor;
import com.lme.iudapp.model.User;
import com.lme.iudapp.module.UserDetailModule;
import com.lme.iudapp.utils.DateDialog;
import com.lme.iudapp.utils.DateUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import dagger.ObjectGraph;


public class UserDetailFragment extends Fragment implements UserDetailInteractor.UserDetailView {

    @Inject
    UserDetailInteractor.UserDetailPresenter userDetailPresenter;

    @InjectView(R.id.mainDetailLayout)
    FrameLayout mainDetailLayout;

    @InjectView(R.id.tvUserDetailBirthdate)
    EditText tvUserDetailBirthdate;

    @InjectView(R.id.tvUserDetailName)
    EditText tvUserDetailName;

    private MainViewInteractor.MainView mainView;
    private static final String ARG_USER = "user";
    private User user;
    private User tempUser;
    private Menu menu;
    private Snackbar snackbarUndo;


    public static UserDetailFragment newInstance(User user) {
        UserDetailFragment userDetailFragment = new UserDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        userDetailFragment.setArguments(args);
        return userDetailFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ObjectGraph objectGraph = ObjectGraph.create(new UserDetailModule());
        objectGraph.inject(this);

        userDetailPresenter.setVista(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        user = (User) getArguments().get(ARG_USER);

        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);
        ButterKnife.inject(this, view);

        tvUserDetailName.setText(user.getName());
        tvUserDetailBirthdate.setText(DateUtils.ISOToReadableDate(user.getBirthdate()));

        return view;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_user_detail, menu);

        this.menu = menu;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                showRemoveUserDialog(user.getId());
                break;
            case R.id.action_edit:
                enableFields();
                saveTempUser(user);
                break;
            case R.id.action_close:
                disableFields();
                restoreUserDetails(false);
                break;
            case R.id.action_save:
                disableFields();
                saveUser();
                break;
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Detalle");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainViewInteractor.MainView) {
            mainView = (MainViewInteractor.MainView) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mainView = null;
        if(snackbarUndo!=null) snackbarUndo.dismiss();
    }


    @Override
    public void showAlert(String message) {
        mainView.showAlert(message);
    }


    @Override
    public void showProgressDialog() {
        mainView.showProgressDialog();
    }


    @Override
    public void dismissProgressDialog() {
        mainView.dismissProgressDialog();
    }


    @Override
    public void refreshUsersListView() {
        mainView.showUsersListFragment();
    }


    @Override
    public void showSnackbar(){
        snackbarUndo = Snackbar
                .make(mainDetailLayout, getResources().getString(R.string.txt_user_edited), Snackbar.LENGTH_LONG)
                .setAction(getResources().getString(R.string.txt_btn_undo_edit).toUpperCase(), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userDetailPresenter.updateUser(tempUser, true);
                        restoreUserDetails(true);
                    }
                });
        View snackBarView = snackbarUndo.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryLight));
        snackbarUndo.show();
    }


    @OnClick(R.id.tvUserDetailBirthdate)
    public void showDatePicker(){
        DateDialog dialog=new DateDialog(tvUserDetailBirthdate);
        dialog.show(getActivity().getFragmentManager(), "DatePicker");
    }


    public void showRemoveUserDialog(final int userId) {
    new AlertDialog.Builder(getActivity())
            .setTitle(getResources().getString(R.string.txt_delete_user_title))
            .setMessage(getResources().getString(R.string.txt_delete_user_message))
            .setPositiveButton(getResources().getString(R.string.txt_btn_delete_user), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    userDetailPresenter.removeUser(userId);
                }
            })
            .setIcon(android.R.drawable.ic_menu_delete)
            .show();
    }


    private void saveUser(){
        if(!tvUserDetailName.getText().toString().isEmpty() && !tvUserDetailBirthdate.getText().toString().isEmpty()) {
            user.setName(tvUserDetailName.getText().toString());
            user.setBirthdate(DateUtils.readableDateToISO(tvUserDetailBirthdate.getText().toString()));
            userDetailPresenter.updateUser(user, false);
        }
        else {
            tvUserDetailName.setText(user.getName());
            Toast.makeText(getActivity(), getResources().getString(R.string.txt_error_empty_username), Toast.LENGTH_SHORT).show();
        }
    }


    private void enableFields(){
        menu.findItem(R.id.action_save).setVisible(true);
        menu.findItem(R.id.action_edit).setVisible(false);
        menu.findItem(R.id.action_close).setVisible(true);
        tvUserDetailName.setEnabled(true);
        tvUserDetailName.requestFocus();
        tvUserDetailBirthdate.setEnabled(true);
    }


    private void disableFields(){
        menu.findItem(R.id.action_close).setVisible(false);
        menu.findItem(R.id.action_save).setVisible(false);
        menu.findItem(R.id.action_edit).setVisible(true);
        tvUserDetailName.setEnabled(false);
        tvUserDetailName.clearFocus();
        tvUserDetailBirthdate.setEnabled(false);
    }


    public void saveTempUser(User user) {
        tempUser = null;
        tempUser = new User();
        tempUser.setId(user.getId());
        tempUser.setName(user.getName());
        tempUser.setBirthdate(user.getBirthdate());
    }


    public void restoreUserDetails(boolean undo) {
        if(undo){
            String name = tempUser.getName();
            user.setName(name);
            user.setBirthdate(tempUser.getBirthdate());
            tvUserDetailName.setText(name);
            tvUserDetailBirthdate.setText(DateUtils.ISOToReadableDate(tempUser.getBirthdate()));
        }
        else {
            tvUserDetailName.setText(user.getName());
            tvUserDetailBirthdate.setText(DateUtils.ISOToReadableDate(user.getBirthdate()));
        }

    }
}
