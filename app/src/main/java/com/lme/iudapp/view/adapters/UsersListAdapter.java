package com.lme.iudapp.view.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lme.iudapp.R;
import com.lme.iudapp.interactor.UsersListInteractor;
import com.lme.iudapp.model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.PersonViewHolder> {

    //    private EditText userBirthdate;
    private List<User> users;
    private UsersListInteractor.UsersView usersView;

    public UsersListAdapter(UsersListInteractor.UsersView usersView) {
        this.usersView = usersView;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView userName;
        TextView userBirthdate;
        int id;

        PersonViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userBirthdate = (TextView) itemView.findViewById(R.id.user_birthdate);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            usersView.getUserInfo(users.get(getAdapterPosition()).id);
        }
    }


    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new PersonViewHolder(v);
    }


    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.userName.setText(users.get(position).name);
        holder.userBirthdate.setText(ISOToReadableDate(users.get(position).birthdate));
        holder.id = users.get(position).id;
    }


    @Override
    public int getItemCount() {
        return users.size();
    }


    private String ISOToReadableDate(String isoDate) {
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
}
