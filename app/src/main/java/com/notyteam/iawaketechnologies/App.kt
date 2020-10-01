package com.notyteam.iawaketechnologies

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.notyteam.iawaketechnologies.dagger.components.AppComponent
import com.notyteam.iawaketechnologies.dagger.components.DaggerAppComponent
import com.notyteam.iawaketechnologies.dagger.modules.ApiModule
import com.notyteam.iawaketechnologies.dagger.modules.AppModule

class App : Application() {
    companion object {
        operator fun get(activity: Activity): App {
            return activity.application as App
        }

        operator fun get(context: Context): App {
            return context as App
        }
    }

    val appComponent: AppComponent by lazy {
        initDagger()
    }

    private fun initDagger(): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .apiModule(ApiModule())
            .build()

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}