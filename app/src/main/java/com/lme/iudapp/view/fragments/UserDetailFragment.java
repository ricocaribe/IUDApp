package com.lme.iudapp.view.fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.lme.iudapp.R;
import com.lme.iudapp.interactor.MainViewInteractor;
import com.lme.iudapp.interactor.UserDetailInteractor;
import com.lme.iudapp.model.User;
import com.lme.iudapp.module.UserDetailModule;
import com.lme.iudapp.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import dagger.ObjectGraph;


public class UserDetailFragment extends Fragment implements UserDetailInteractor.UserDetailView {

    @Inject
    UserDetailInteractor.UserDetailPresenter userDetailPresenter;

    private User user;
    public static final String ARG_USER = "user";
    private MainViewInteractor.MainView mainView;
    private Calendar myCalendar = Calendar.getInstance();
    private EditText tvUserDetailBirthdate;
    private MenuItem saveItem;
    private EditText tvUserDetailName;

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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_user_detail, menu);

        saveItem = menu.findItem(R.id.action_save);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                showRemoveUserDialog(user.id);
                break;
            case R.id.action_save:
                saveUser();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        user = (User) getArguments().get(ARG_USER);

        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);

        tvUserDetailName = view.findViewById(R.id.tvUserDetailName);
        tvUserDetailName.setText(user.name);
        tvUserDetailName.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                saveItem.setVisible(true);
            }
        });

        tvUserDetailBirthdate = view.findViewById(R.id.tvUserDetailBirthdate);
        tvUserDetailBirthdate.setText(DateUtils.ISOToReadableDate(user.birthdate));
        tvUserDetailBirthdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                String[] date = tvUserDetailBirthdate.getText().toString().split("/");
                datePickerDialog.updateDate(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
                datePickerDialog.show();
            }
        });

        return view;
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
    }


    @Override
    public void showAlert() {
        mainView.showAlert();
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

    public void showRemoveUserDialog(final int userId) {
    new AlertDialog.Builder(getActivity())
            .setTitle(getResources().getString(R.string.eliminar_usuario_titulo))
            .setMessage(getResources().getString(R.string.eliminar_usuario_mensaje))
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    userDetailPresenter.removeUser(userId);
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            })
            .setIcon(android.R.drawable.ic_menu_delete)
            .show();
    }


    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            tvUserDetailBirthdate.setText(sdf.format(myCalendar.getTime()));
            saveItem.setVisible(true);
        }
    };


    private void saveUser(){
        if(!tvUserDetailName.getText().toString().isEmpty() && !tvUserDetailBirthdate.getText().toString().isEmpty()) {
            user.name = tvUserDetailName.getText().toString();
            user.birthdate = DateUtils.dateToIsoConverter(tvUserDetailBirthdate.getText().toString());
            userDetailPresenter.updateUser(user);
        }
        else Toast.makeText(getActivity(), getResources().getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show();
    }


    //    public void saveTempUser(User user) {
//        tempUser = new User();
//        tempUser.setId(user.getId());
//        tempUser.setName(user.getName());
//        tempUser.setBirthdate(user.getBirthdate());
//    }
//
//    public void removeTempUser() {
//        tempUser = null;
//    }
}
