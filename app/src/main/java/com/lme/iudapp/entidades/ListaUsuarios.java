package com.lme.iudapp.entidades;

/**
 * Created by 30043174 on 12/04/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListaUsuarios {

    @SerializedName("product")
    public List<Usuario> usuarios;

    public List<Usuario> getUsuarios() {
        return this.usuarios;
    }
}
