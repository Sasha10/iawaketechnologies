package com.notyteam.iawaketechnologies.api.retfofit

import android.content.Context
import com.notyteam.iawaketechnologies.App
import com.notyteam.iawaketechnologies.R
import com.notyteam.iawaketechnologies.api.responses.BaseResponse
import com.notyteam.iawaketechnologies.utils.Failure
import com.notyteam.iawaketechnologies.utils.Success
import com.notyteam.iawaketechnologies.utils.Result
import io.reactivex.Single
import io.reactivex.observers.DisposableSingleObserver
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import retrofit2.Retrofit
import java.lang.ref.WeakReference
import java.net.ConnectException
import javax.inject.Inject

class CallbackWrapper<T, V>(
    private val contextWeakReference: WeakReference<Context>? = null,
    private var onResult:(Result<V>) -> Unit) : DisposableSingleObserver<T>(){

    companion object {
        const val ERROR_TO_CONNECT = 500
        const val ERROR_WRONG_SMS_CODE = 700
        const val ERROR_UNEXPECTED_CODE = 666
        const val ERROR_CONVERTING_DATA_CODE = 601
        const val ERROR_INTERNET_CONNECTION_CODE = 600
        const val ERROR_NOT_FOUND_CODE = 404
        const val ERROR_UNAUTHORIZED = 401
    }

    @Inject
    lateinit var retrofit: Retrofit

    var request: Single<T>? = null


    init {
        contextWeakReference?.get()?.let {
            App[it].appComponent.inject(this as CallbackWrapper<Any, Any>)
        }
    }

    override fun onSuccess(response: T) {

        onResult(Success(response as V))

  }


    override fun onError(t: Throwable) {
        val errorMessage: String
        val errorCode: Int
        if (t is IOException) {
            errorMessage = contextWeakReference?.get()?.getString(R.string.error_no_intenet_connection)!!
            errorCode = ERROR_INTERNET_CONNECTION_CODE
        } else if (t is HttpException) {
            when (t.code()){
                ERROR_UNAUTHORIZED -> {
                      //   onUnauthorized()
                    return
                }
                else -> {
                    val errorWrapper = ErrorWrapper()
                    val error = t.response()?.let { errorWrapper.parseError(retrofit, it) }
                    if (error != null) {
                        errorMessage = error.errorMessage?:""
                        if (error.statusCode?.isNotEmpty() == true)
                            errorCode = error.statusCode?.toInt()
                        else errorCode = t.code()
                    }
                    else {
                        errorMessage = /*contextWeakReference?.get()?.getString(R.string.error_unexpected)?:*/t.message.toString()
                        errorCode = ERROR_UNEXPECTED_CODE
                    }
                }
            }
        }
        else if (t is IllegalStateException) {
            errorMessage = contextWeakReference?.get()?.getString(R.string.error_converting_data)!!
            errorCode = ERROR_CONVERTING_DATA_CODE
        }
        else if (t is ConnectException) {
            errorMessage = contextWeakReference?.get()?.getString(R.string.error_converting_data)!!
            errorCode = ERROR_TO_CONNECT
        } else {
            errorMessage = /*contextWeakReference?.get()?.getString(R.string.error_unexpected)?:*/t.message.toString()
            errorCode = ERROR_UNEXPECTED_CODE
        }
        onResult(Failure(errorMessage, errorCode))
    }


    private inner class ErrorWrapper {
        fun parseError(retrofit: Retrofit, response: Response<*>): BaseResponse? {
            val converter: Converter<ResponseBody, BaseResponse> = retrofit
                    .responseBodyConverter(BaseResponse::class.java, emptyArray())
            try {
                response.errorBody()?.let {
                    return converter.convert(it)
                }
            } catch (e: Exception) {
                return BaseResponse()
            }
            return BaseResponse()
        }
    }
}