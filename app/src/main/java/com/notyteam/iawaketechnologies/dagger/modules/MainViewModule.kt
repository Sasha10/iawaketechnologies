package com.notyteam.iawaketechnologies.dagger.modules

import androidx.lifecycle.ViewModel
import com.notyteam.iawaketechnologies.ui.viewmodels.MainViewModel
import com.notyteam.iawaketechnologies.dagger.ViewModelKey
import com.notyteam.iawaketechnologies.dagger.scopes.FragmentScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModule {
    @Binds
    @FragmentScope
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindAuthActivityViewModel(model: MainViewModel): ViewModel
}