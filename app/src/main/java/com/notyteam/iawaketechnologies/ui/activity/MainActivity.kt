package com.notyteam.iawaketechnologies.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.notyteam.iawaketechnologies.R
import com.notyteam.iawaketechnologies.base.BaseActivity
import com.notyteam.iawaketechnologies.ui.fragments.main.MainFragment
import com.notyteam.iawaketechnologies.ui.viewmodels.MainViewModel
import com.notyteam.iawaketechnologies.utils.injectViewModel


class MainActivity : BaseActivity<MainViewModel>() {
    companion object {
        fun start(baseActivity: Activity) {
            baseActivity.startActivity(
                Intent(baseActivity, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
            Animatoo.animateZoom(baseActivity)
        }
    }

    override fun layout() = R.layout.activity_main

    override fun initialization() {
        fragmentManager.replaceFragment(MainFragment())
    }

    override fun provideViewModel(viewModelFactory: ViewModelProvider.Factory): MainViewModel {
        return injectViewModel(viewModelFactory)
    }

}