package com.lme.iudapp.adaptadores;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lme.iudapp.R;
import com.lme.iudapp.entidades.Usuario;

import java.util.List;


public class UsersAdaptador extends RecyclerView.Adapter<UsersAdaptador.PersonViewHolder>{

    private List<Usuario> users;
    private Context context;
    private UserActions userActionsCallback;

    public interface UserActions {
        void getUserToUpdate(View v, int id, String nombre, String birthdate);

        void getUserToRemove(View v, int id);
    }

    public void setUserActionsClickListener(UserActions listener) {
        this.userActionsCallback = listener;
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
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
            Log.i("onClick", id + "");
            showEditAlert(id, userName.getText().toString(), userBirthdate.getText().toString(), view);

        }

        @Override
        public boolean onLongClick(final View view) {
            Log.i("onLongClick", id + "");
            PopupMenu popup = new PopupMenu(context, view, Gravity.CENTER);
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_delete:
                            // read the listItemPosition here
                            if (userActionsCallback != null) {
                                userActionsCallback.getUserToRemove(view, id);
                            }
                            return true;
                        default:
                            return false;
                    }
                }
            });
            popup.inflate(R.menu.menu_main);
            popup.show();
            return true;
        }
    }


    public UsersAdaptador(List<Usuario> users, Context c){
        this.context = c;
        this.users = users;
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
        holder.userBirthdate.setText(users.get(position).getBirthdate());
        holder.id = users.get(position).getId();
    }


    @Override
    public int getItemCount() {
        return users.size();
    }


    public void showEditAlert(final int id, final String nombre_usuario, final String birthdate_usuario, final View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(context.getResources().getString(R.string.editar_usuario_titulo));

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View editDialoglayout = inflater.inflate(R.layout.dialog_layout, null);

        final EditText userName = (EditText) editDialoglayout.findViewById(R.id.edt_user_name);
        userName.setText(nombre_usuario);
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
                    userName.setError("Nombre vacío");
                } else {
                    userName.setError(null);
                }
            }
        });

        final EditText userBirthdate = (EditText) editDialoglayout.findViewById(R.id.edt_user_birthdate);
        userBirthdate.setText(birthdate_usuario);
        userBirthdate.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (userBirthdate.getText().toString().length() <= 0) {
                    userBirthdate.setError("Fecha de nacimiento vacía");
                } else {
                    userBirthdate.setError(null);
                }
            }
        });

        alert.setView(editDialoglayout);

        alert.setPositiveButton(context.getResources().getString(R.string.boton_aceptar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (userActionsCallback != null) {
                    userActionsCallback.getUserToUpdate(v, id, userName.getText().toString(), userBirthdate.getText().toString());
                }
            }
        });

        alert.setNegativeButton(context.getResources().getString(R.string.boton_cancelar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }
}
