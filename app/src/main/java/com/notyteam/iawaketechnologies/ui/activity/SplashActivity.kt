package com.notyteam.iawaketechnologies.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.notyteam.iawaketechnologies.R
import com.notyteam.iawaketechnologies.base.BaseActivity
import com.notyteam.iawaketechnologies.ui.viewmodels.MainViewModel
import com.notyteam.iawaketechnologies.utils.injectViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class SplashActivity : BaseActivity<MainViewModel>() {
    override fun layout() = R.layout.activity_splash

    companion object {
        fun start(baseActivity: Activity) {
            baseActivity.startActivity(
                Intent(baseActivity, SplashActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }
    }

    override fun initialization() {

        emulateWaitTimer()
    }

    @SuppressLint("CheckResult")
    private fun emulateWaitTimer() {

        Observable.timer(1500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                MainActivity.start(this)
                finish()
            }
    }

    override fun provideViewModel(viewModelFactory: ViewModelProvider.Factory): MainViewModel {
        return injectViewModel(viewModelFactory)
    }
}