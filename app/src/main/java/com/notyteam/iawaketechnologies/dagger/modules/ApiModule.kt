package com.notyteam.iawaketechnologies.dagger.modules

import android.app.Application
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.notyteam.iawaketechnologies.BuildConfig
import com.notyteam.iawaketechnologies.api.retfofit.api_interface.ProgramsApiInterface
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiModule {
    companion object {
        private const val CONNECTION_TIMEOUT_SEC = 30L
        private const val END_POINT = BuildConfig.SERVER_URL
    }

    @Provides
    @Singleton
    fun provideHttpCache(application: Application): Cache {
        val cacheSize = 10L * 1024 * 1024
        return Cache(application.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder().apply {
            cache(cache)

            val cookieManager = CookieManager()
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
            cookieJar(JavaNetCookieJar(cookieManager))

            if (BuildConfig.DEBUG) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(httpLoggingInterceptor)
            }

            connectTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            readTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            writeTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)

            addInterceptor { chain ->
                var request = chain.request()

                val builder = request.newBuilder()
                builder.header("Content-Type", "application/json")

                val response = chain.proceed(builder.build())
                response
            }
        }.build()
    }

    private fun bodyToString(request: RequestBody?): String {
        try {
            val buffer = Buffer()
            if (request != null)
                request.writeTo(buffer)
            else
                return ""
            return buffer.readUtf8()
        } catch (e: IOException) {
            return "did not work"
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(END_POINT)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    @Provides
    @Singleton
    fun provideApiImplementation(retrofit: Retrofit): ProgramsApiInterface {
        return retrofit.create(ProgramsApiInterface::class.java)
    }
}