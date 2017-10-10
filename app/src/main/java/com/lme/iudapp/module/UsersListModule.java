package com.lme.iudapp.module;

import com.lme.iudapp.interactor.UsersListInteractor;
import com.lme.iudapp.presenter.UsersListPresenter;
import com.lme.iudapp.view.fragments.UsersListFragment;

import dagger.Module;
import dagger.Provides;


@Module(injects = {UsersListFragment.class})
public class UsersListModule {

    @Provides
    public UsersListInteractor.UsersPresenter provideUsersListPresenter(){
        return new UsersListPresenter();
    }
}
