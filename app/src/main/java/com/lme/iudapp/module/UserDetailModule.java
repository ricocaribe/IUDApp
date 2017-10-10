package com.lme.iudapp.module;

import com.lme.iudapp.interactor.UserDetailInteractor;
import com.lme.iudapp.presenter.UserDetailPresenter;
import com.lme.iudapp.view.fragments.UserDetailFragment;

import dagger.Module;
import dagger.Provides;


@Module(injects = {UserDetailFragment.class})
public class UserDetailModule {

    @Provides
    public UserDetailInteractor.UserDetailPresenter provideUserDetailPresenter(){
        return new UserDetailPresenter();
    }
}
