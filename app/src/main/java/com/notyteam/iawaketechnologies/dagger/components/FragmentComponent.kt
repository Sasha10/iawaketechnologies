package com.notyteam.iawaketechnologies.dagger.components

import com.notyteam.iawaketechnologies.base.BaseActivity
import com.notyteam.iawaketechnologies.base.BaseFragment
import com.notyteam.iawaketechnologies.base.mvvm.BaseViewModel
import com.notyteam.iawaketechnologies.dagger.modules.FragmentModule
import com.notyteam.iawaketechnologies.dagger.modules.MainViewModule
import com.notyteam.iawaketechnologies.dagger.scopes.FragmentScope
import dagger.Subcomponent

@FragmentScope
@Subcomponent(
    modules = [FragmentModule::class,
        MainViewModule::class]
)
interface FragmentComponent {
    fun inject(target: BaseActivity<BaseViewModel>)
    fun inject(target: BaseFragment<BaseViewModel>)
}