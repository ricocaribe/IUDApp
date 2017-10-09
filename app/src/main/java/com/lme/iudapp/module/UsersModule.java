package com.lme.iudapp.module;

import com.lme.iudapp.interactor.UsersInteractor;
import com.lme.iudapp.presenter.UsersFragmentPresenter;
import com.lme.iudapp.view.fragments.UsersFragment;

import dagger.Module;
import dagger.Provides;


@Module(injects = {UsersFragment.class})
public class UsersModule {

    @Provides
    public UsersInteractor.UsersPresenter provideUsersFragmentPresenter(){
        return new UsersFragmentPresenter();
    }
}
