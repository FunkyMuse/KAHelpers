package com.crazylegend.kotlinextensions.retrofit

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by Hristijan on 1/23/19 to long live and prosper !
 */
object RetrofitClient {

    private var retrofit: Retrofit? = null

    fun gsonInstance(context: Context, baseUrl: String, enableInterceptor: Boolean = false): Retrofit? {

        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (enableInterceptor) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE


        clientBuilder.apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(RetryRequestInterceptor(context))
            addInterceptor(ConnectivityInterceptor(context))
           // authenticator(TokenAuthenticator(context))
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(100, TimeUnit.SECONDS)
            writeTimeout(100, TimeUnit.SECONDS)
        }

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
        }

        return retrofit

    }

    fun moshiInstance(context: Context, baseUrl: String, enableInterceptor: Boolean = false): Retrofit? {

        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (enableInterceptor) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE


        clientBuilder.apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(RetryRequestInterceptor(context))
            addInterceptor(ConnectivityInterceptor(context))
            // authenticator(TokenAuthenticator(context))
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(100, TimeUnit.SECONDS)
            writeTimeout(100, TimeUnit.SECONDS)
        }

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(clientBuilder.build())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }

        return retrofit

    }


}