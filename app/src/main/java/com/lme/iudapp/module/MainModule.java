package com.lme.iudapp.module;

import com.lme.iudapp.interactor.MainViewInteractor;
import com.lme.iudapp.presenter.MainViewPresenter;
import com.lme.iudapp.view.activities.MainActivity;

import dagger.Module;
import dagger.Provides;


@Module(injects = {MainActivity.class})
public class MainModule {

    @Provides
    public MainViewInteractor.MainPresenter provideMainViewPresenter(){
        return new MainViewPresenter();
    }
}
