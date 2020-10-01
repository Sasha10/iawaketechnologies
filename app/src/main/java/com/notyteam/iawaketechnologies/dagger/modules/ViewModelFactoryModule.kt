package com.notyteam.iawaketechnologies.dagger.modules

import androidx.lifecycle.ViewModelProvider
import com.notyteam.iawaketechnologies.base.mvvm.DaggerViewModelFactory
import com.notyteam.iawaketechnologies.dagger.scopes.FragmentScope
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    @FragmentScope
    abstract fun bindViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory
}