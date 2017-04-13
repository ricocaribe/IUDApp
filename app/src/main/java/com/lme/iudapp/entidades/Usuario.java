package com.lme.iudapp.entidades;

import com.google.gson.annotations.SerializedName;

public class Usuario {

    @SerializedName("name")
    public String name;
    @SerializedName("birthdate")
    public String birthdate;
    @SerializedName("id")
    public String id;

    public String getName() {
        return this.name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getId() {
        return id;
    }

}