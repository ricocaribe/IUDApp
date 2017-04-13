package com.lme.iudapp.adaptadores;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lme.iudapp.MainActivity;
import com.lme.iudapp.R;
import com.lme.iudapp.Utilidades.Endpoints;
import com.lme.iudapp.entidades.Usuario;

import java.util.List;

/**
 * Adaptador para mostrar las comidas más pedidas en la sección "Inicio"
 */
public class UsersAdaptador extends RecyclerView.Adapter<UsersAdaptador.PersonViewHolder>{

    private List<Usuario> users;
    private Context context;
    private UserActions userActionsCallback;

    public interface UserActions {
        void getUserToUpdate(View v, int id);

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
            /*if (removeCallback != null) {
                removeCallback.getUser(view, id);
            }*/
            showEditAlert(userName.getText().toString());

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


    public void showEditAlert(String nombre_usuario){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        final EditText edittext = new EditText(context);
        alert.setTitle(context.getResources().getString(R.string.editar_nombre_usuario_titulo));
        alert.setMessage(context.getResources().getString(R.string.editar_nombre_usuario));
        edittext.setText(nombre_usuario);
        alert.setView(edittext);

        alert.setPositiveButton(context.getResources().getString(R.string.boton_aceptar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String YouEditTextValue = edittext.getText().toString();
                if (userActionsCallback != null) {
                    //removeCallback.updateUser(view, id);
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
