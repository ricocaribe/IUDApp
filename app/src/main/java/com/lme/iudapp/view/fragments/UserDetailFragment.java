package com.lme.iudapp.view.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.lme.iudapp.R;
import com.lme.iudapp.interactor.MainViewInteractor;
import com.lme.iudapp.interactor.UserDetailInteractor;
import com.lme.iudapp.model.User;
import com.lme.iudapp.module.UserDetailModule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import dagger.ObjectGraph;


public class UserDetailFragment extends Fragment implements UserDetailInteractor.UserDetailView {

    @Inject
    UserDetailInteractor.UserDetailPresenter userDetailPresenter;

    private User user;
    public static final String ARG_USER = "user";
    private MainViewInteractor.MainView mainView;
    private EditText userBirthdate;
//    private Calendar myCalendar = Calendar.getInstance();

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user_detail, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                showRemoveUserDialog(user.id);
                break;
            case R.id.action_edit:
                userDetailPresenter.updateUser(user);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        user = (User) getArguments().get(ARG_USER);

        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);

        TextView superheroDetailName = (TextView) view.findViewById(R.id.tvUserDetailName);
        superheroDetailName.setText(user.name);

        TextView superheroDetailHeight = (TextView) view.findViewById(R.id.tvUserDetailBirthdate);
        superheroDetailHeight.setText(user.birthdate);

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
    public void refreshUserView() {
        mainView.refreshUserDetailFragment(user);
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


    private void showEditAlert(final User user){

//        sharedMethodsCallback.saveTempUser(user);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(getActivity().getResources().getString(R.string.editar_usuario_titulo));

        LayoutInflater inflater = (LayoutInflater) (getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        View editDialoglayout = inflater.inflate(R.layout.dialog_layout, null);

        final EditText userName = (EditText) editDialoglayout.findViewById(R.id.edt_user_name);
        userName.setText(user.name);
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
                    userName.setError((getActivity().getResources().getString(R.string.editar_nombre_usuario_error)));
                } else {
                    userName.setError(null);
                }
            }
        });

        userBirthdate = (EditText) editDialoglayout.findViewById(R.id.edt_user_birthdate);
        userBirthdate.setHint(getActivity().getResources().getString(R.string.edt_fecha_usuario));
        userBirthdate.setText(ISOToReadableDate(user.birthdate));
        userBirthdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH));
//                String[] date = userBirthdate.getText().toString().split("/");
//                datePickerDialog.updateDate(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
//                datePickerDialog.show();
            }
        });

        alert.setView(editDialoglayout);
        alert.setPositiveButton(getActivity().getResources().getString(R.string.btn_aceptar), null);
        alert.setNegativeButton(getActivity().getResources().getString(R.string.btn_cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //sharedMethodsCallback.removeTempUser();
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();
        Button acceptBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        acceptBtn.setOnClickListener(new AcceptDialogBtn(dialog, user, userName));
    }


    private class AcceptDialogBtn implements View.OnClickListener {
        private final Dialog dialog;
        private final User user;
        private final EditText userName;

        AcceptDialogBtn(Dialog dialog, User user, EditText userName) {
            this.dialog = dialog;
            this.user = user;
            this.userName = userName;
        }

        @Override
        public void onClick(View v) {

            if (isValidUser(userName, userBirthdate)) {
                    user.setName(userName.getText().toString());
                    user.setBirthdate(dateToIsoConverter(userBirthdate.getText().toString()));
            }

            dialog.dismiss();
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
//            myCalendar.set(Calendar.YEAR, year);
//            myCalendar.set(Calendar.MONTH, monthOfYear);
//            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//            String myFormat = "MM/dd/yyyy";
//            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//            userBirthdate.setError(null);
//            userBirthdate.setText(sdf.format(myCalendar.getTime()));
        }
    };

    private String ISOToReadableDate(String isoDate){
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        try {
            Date date = originalFormat.parse(isoDate);
            return targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
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

//   private Calendar myCalendar = Calendar.getInstance();
//
//    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//        @Override
//        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            myCalendar.set(Calendar.YEAR, year);
//            myCalendar.set(Calendar.MONTH, monthOfYear);
//            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//            String myFormat = "MM/dd/yyyy";
//            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//            userBirthdate.setText(sdf.format(myCalendar.getTime()));
//        }
//    };
}
