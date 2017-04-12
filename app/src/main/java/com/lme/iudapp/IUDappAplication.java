package com.lme.iudapp;

import android.app.Application;

import com.lme.iudapp.Utilidades.Endpoints;

public class IUDappAplication extends Application {

    private Endpoints mainEndpoints;

    public Endpoints getEndpoints() {
        return mainEndpoints;
    }

    public void setEndpoints(Endpoints mainEndpoints) {
        this.mainEndpoints = mainEndpoints;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Endpoints mainEndpoints = new Endpoints();
        setEndpoints(mainEndpoints);
    }
}
