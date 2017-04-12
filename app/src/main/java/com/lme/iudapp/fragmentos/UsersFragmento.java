package com.lme.iudapp.fragmentos;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lme.iudapp.IUDappAplication;
import com.lme.iudapp.R;
import com.lme.iudapp.Utilidades.Endpoints;
import com.lme.iudapp.adaptadores.UsersAdaptador;

/**
 * A placeholder fragment containing a simple view.
 */
public class UsersFragmento extends Fragment {

    public UsersFragmento() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView users_rv = (RecyclerView)container.findViewById(R.id.birthdates_rv);
        users_rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        users_rv.setLayoutManager(llm);

        Endpoints endpoints = ((IUDappAplication) getActivity().getApplication()).getEndpoints();

//        UsersAdaptador usersAdaptador = new UsersAdaptador(endpoints);
//        users_rv.setAdapter(usersAdaptador);

        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
