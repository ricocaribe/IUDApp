package com.lme.iudapp.presenter;


import com.lme.iudapp.interactor.MainViewInteractor;


public class MainViewPresenter implements MainViewInteractor.MainPresenter {

    private MainViewInteractor.MainView mainView;


    @Override
    public void setVista(MainViewInteractor.MainView mainView) {
        this.mainView = mainView;
    }

}
