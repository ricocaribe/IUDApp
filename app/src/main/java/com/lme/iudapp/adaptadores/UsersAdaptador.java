package com.lme.iudapp.adaptadores;

/**
 * Created by josemariaricoperez on 09/01/16.
 */

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lme.iudapp.R;
import com.lme.iudapp.entidades.Usuario;

import java.util.List;

/**
 * Adaptador para mostrar las comidas más pedidas en la sección "Inicio"
 */
public class UsersAdaptador extends RecyclerView.Adapter<UsersAdaptador.PersonViewHolder>{

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView userName;
        TextView userBirthdate;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.user_cv);
            userName = (TextView)itemView.findViewById(R.id.user_name);
            userBirthdate = (TextView)itemView.findViewById(R.id.user_birthdate);
        }
    }

    List<Usuario> users;

    public UsersAdaptador(List<Usuario> users){
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
        holder.userName.setText("Ernesto");//users.get(position).userName
        holder.userBirthdate.setText("12/04/1976");
    }

    @Override
    public int getItemCount() {
//        return users.size();
        return 8;
    }
}
