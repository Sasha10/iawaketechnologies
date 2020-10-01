package com.notyteam.iawaketechnologies.dagger.components

import android.app.Application
import com.notyteam.iawaketechnologies.api.retfofit.BaseRepository
import com.notyteam.iawaketechnologies.api.retfofit.CallbackWrapper
import com.notyteam.iawaketechnologies.api.retfofit.apis.BaseApi
import com.notyteam.iawaketechnologies.base.mvvm.BaseViewModel
import com.notyteam.iawaketechnologies.dagger.modules.ApiModule
import com.notyteam.iawaketechnologies.dagger.modules.AppModule
import com.notyteam.iawaketechnologies.dagger.modules.FragmentModule
import com.notyteam.iawaketechnologies.dagger.modules.ViewModelFactoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class,
        ApiModule::class,
        ViewModelFactoryModule::class]
)
interface AppComponent {
    fun plus(fragmentModule: FragmentModule): FragmentComponent

    fun inject(app: Application)
    fun inject(target: CallbackWrapper<Any, Any>)
    fun inject(target: BaseApi)
    fun inject(target: BaseRepository)
    fun inject(target: BaseViewModel)
}