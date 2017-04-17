package com.lme.iudapp.adaptadores;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.lme.iudapp.R;
import com.lme.iudapp.entidades.User;
import com.lme.iudapp.utilidades.SharedMethods;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.PersonViewHolder>{

    private EditText userBirthdate;
    private List<User> users;
    private Context context;
    private SharedMethods sharedMethodsCallback;

    public void setUserActionsClickListener(SharedMethods listener) {
        this.sharedMethodsCallback = listener;
    }

    public UsersAdapter(List<User> users, Context c){
        this.context = c;
        this.users = users;
    }

    class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView userName;
        TextView userBirthdate;
        int id;

        PersonViewHolder(View itemView) {
            super(itemView);
            userName = (TextView)itemView.findViewById(R.id.user_name);
            userBirthdate = (TextView)itemView.findViewById(R.id.user_birthdate);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            showEditAlert(users.get(getAdapterPosition()));

        }

        @Override
        public boolean onLongClick(final View view) {
            new AlertDialog.Builder(context)
                    .setTitle(context.getResources().getString(R.string.alert_delete_user_title))
                    .setMessage(context.getResources().getString(R.string.alert_delete_user_message))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (sharedMethodsCallback != null) sharedMethodsCallback.getUserToRemove(view, id);

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_menu_delete)
                    .show();
            return true;
        }
    }


    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_element, parent, false);
        return new PersonViewHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.userName.setText(users.get(position).getName());
        holder.userBirthdate.setText(ISOToReadableDate(users.get(position).getBirthdate()));
        holder.id = users.get(position).getId();
    }


    @Override
    public int getItemCount() {
        return users.size();
    }


    private void showEditAlert(final User user){

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(context.getResources().getString(R.string.editar_usuario_titulo));

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View editDialoglayout = inflater.inflate(R.layout.dialog_layout, null);

        final EditText userName = (EditText) editDialoglayout.findViewById(R.id.edt_user_name);
        userName.setText(user.getName());
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
                    userName.setError(context.getResources().getString(R.string.editar_nombre_usuario_error));
                } else {
                    userName.setError(null);
                }
            }
        });

        userBirthdate = (EditText) editDialoglayout.findViewById(R.id.edt_user_birthdate);
        userBirthdate.setHint(context.getResources().getString(R.string.editar_birthdate_usuario));
        userBirthdate.setText(ISOToReadableDate(user.getBirthdate()));
        userBirthdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                String[] date = userBirthdate.getText().toString().split("/");
                datePickerDialog.updateDate(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
                datePickerDialog.show();
            }
        });

        alert.setView(editDialoglayout);
        alert.setPositiveButton(context.getResources().getString(R.string.boton_aceptar), null);
        alert.setNegativeButton(context.getResources().getString(R.string.boton_cancelar), null);

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

            if (sharedMethodsCallback != null && sharedMethodsCallback.isValidUser(userName, userBirthdate)) {
                dialog.dismiss();
                user.setName(userName.getText().toString());
                user.setBirthdate(sharedMethodsCallback.dateToIsoConverter(userBirthdate.getText().toString()));
                sharedMethodsCallback.getUserToUpdate(v, user);
            }
        }
    }

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


    private Calendar myCalendar = Calendar.getInstance();

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            userBirthdate.setText(sdf.format(myCalendar.getTime()));
        }
    };
}
