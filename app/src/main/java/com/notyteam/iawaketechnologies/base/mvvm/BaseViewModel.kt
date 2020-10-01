package com.notyteam.iawaketechnologies.base.mvvm

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.notyteam.iawaketechnologies.App
import com.notyteam.iawaketechnologies.api.retfofit.apis.BaseApi
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class BaseViewModel(val app: Application) : AndroidViewModel(app) {
    @Inject
    lateinit var api: BaseApi

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    @Inject
    lateinit var toast: Toast

    init {
        App[app].appComponent.inject(this)
    }

    abstract fun onClear()

    override fun onCleared() {
        super.onCleared()
        onClear()
        compositeDisposable.clear()
    }

    private fun Disposable.bindToLifeCycle() {
        compositeDisposable.add(this)
    }

    protected fun <T> Single<T>.call() {
        this.subscribe({
        }, {

        }).also { it.bindToLifeCycle() }
    }
}