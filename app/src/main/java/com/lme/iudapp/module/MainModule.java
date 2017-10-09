package com.lme.iudapp.module;

import com.lme.iudapp.interactor.MainInteractor;
import com.lme.iudapp.presenter.MainActivityPresenter;
import com.lme.iudapp.view.activities.MainActivity;

import dagger.Module;
import dagger.Provides;


@Module(injects = {MainActivity.class})
public class MainModule {

    @Provides
    public MainInteractor.MainPresenter provideMainActivityPresenter(){
        return new MainActivityPresenter();
    }
}
