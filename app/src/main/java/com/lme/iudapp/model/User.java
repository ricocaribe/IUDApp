package com.lme.iudapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable{

    @SerializedName("name")
    public String name;
    @SerializedName("birthdate")
    public String birthdate;
    @SerializedName("id")
    public int id;

    public String getName() {
        return name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setId(int id) {
        this.id = id;
    }
}