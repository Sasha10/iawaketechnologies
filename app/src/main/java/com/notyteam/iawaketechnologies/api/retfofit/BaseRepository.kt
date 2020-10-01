package com.notyteam.iawaketechnologies.api.retfofit

import android.content.Context
import com.notyteam.iawaketechnologies.App
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import java.lang.ref.WeakReference
import javax.inject.Inject
import com.notyteam.iawaketechnologies.utils.Result

open class BaseRepository @Inject constructor(
        val context: Context,
        val retrofit: Retrofit) {

    init {
        App[context].appComponent.inject(this)
    }

    fun <T, V> initRequestSingleForResult(request: Single<T>,
                                          onResult: (Result<V>) -> Unit): Single<T> {
        val callback = CallbackWrapper<T, V>(WeakReference(context), onResult)
        val requestSingle = request.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                callback.onSuccess(it)
            }
            .doOnError {
                callback.onError(it)
            }
        callback.request = requestSingle
        return requestSingle
    }

}
