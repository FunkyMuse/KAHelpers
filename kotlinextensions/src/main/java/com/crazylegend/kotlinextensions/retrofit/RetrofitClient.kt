package com.crazylegend.kotlinextensions.retrofit

import android.content.Context
import com.crazylegend.kotlinextensions.isInDebugMode
import com.crazylegend.kotlinextensions.isNull
import com.crazylegend.kotlinextensions.retrofit.interceptors.ConnectivityInterceptor
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
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

    var connectTimeout = 30L
    var readTimeout = 30L
    var writeTimeout = 30L
    var connectionTimeUnit = TimeUnit.SECONDS

    private val gsonConverter by lazy {
        GsonConverterFactory.create()
    }

    private val moshiConverter by lazy {
        MoshiConverterFactory.create()
    }

    private val rxJavaAdapter by lazy {
        RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
    }


    fun removeRetrofitInstance() {
        retrofit = null
    }

    fun gsonInstanceRxJava(context: Context, baseUrl: String, enableInterceptor: Boolean = isInDebugMode, okHttpClientConfig: OkHttpClient.Builder.() -> Unit = {}): Retrofit? {
        val clientBuilder = buildClient(context, enableInterceptor, okHttpClientConfig)
        doesRetrofitNeedsBuild(baseUrl) {
            retrofit = buildRetrofit(baseUrl, clientBuilder, gsonConverter, rxJavaAdapter)
        }
        return retrofit
    }


    fun gsonInstanceCouroutines(context: Context, baseUrl: String, enableInterceptor: Boolean = isInDebugMode, okHttpClientConfig: OkHttpClient.Builder.() -> Unit = {}): Retrofit? {
        val clientBuilder = buildClient(context, enableInterceptor, okHttpClientConfig)
        doesRetrofitNeedsBuild(baseUrl) {
            retrofit = buildRetrofit(baseUrl, clientBuilder, gsonConverter)
        }
        return retrofit
    }

    fun moshiInstanceRxJava(context: Context, baseUrl: String, enableInterceptor: Boolean = isInDebugMode, okHttpClientConfig: OkHttpClient.Builder.() -> Unit = {}): Retrofit? {
        val clientBuilder = buildClient(context, enableInterceptor, okHttpClientConfig)
        doesRetrofitNeedsBuild(baseUrl) {
            retrofit = buildRetrofit(baseUrl, clientBuilder, moshiConverter, rxJavaAdapter)
        }
        return retrofit
    }


    fun moshiInstanceCoroutines(context: Context, baseUrl: String, enableInterceptor: Boolean = isInDebugMode, okHttpClientConfig: OkHttpClient.Builder.() -> Unit = {}): Retrofit? {
        val clientBuilder = buildClient(context, enableInterceptor, okHttpClientConfig)
        doesRetrofitNeedsBuild(baseUrl) {
            retrofit = buildRetrofit(baseUrl, clientBuilder, moshiConverter)
        }

        return retrofit

    }

    private fun buildRetrofit(baseUrl: String, okHttpClient: OkHttpClient.Builder, converterFactory: Converter.Factory, callAdapterFactory: CallAdapter.Factory): Retrofit? {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient.build())
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .build()
    }

    private fun buildRetrofit(baseUrl: String, okHttpClient: OkHttpClient.Builder, converterFactory: Converter.Factory): Retrofit? {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient.build())
                .addConverterFactory(converterFactory)
                .build()
    }

    private fun OkHttpClient.Builder.addBasicInterceptors(loggingInterceptor: HttpLoggingInterceptor, connectivityInterceptor: ConnectivityInterceptor) {
        addInterceptor(loggingInterceptor)
        addInterceptor(connectivityInterceptor)
        connectTimeout(connectTimeout, connectionTimeUnit)
        readTimeout(readTimeout, connectionTimeUnit)
        writeTimeout(writeTimeout, connectionTimeUnit)
    }

    private fun doesRetrofitNeedsBuild(baseUrl: String, function: () -> Unit) {
        if (retrofit.isNull) {
            function()
        } else {
            if (retrofit?.baseUrl()?.toString() != baseUrl) {
                function()
            }
        }
    }

    private fun buildClient(context: Context, enableInterceptor: Boolean, okHttpClientConfig: OkHttpClient.Builder.() -> Unit = {}): OkHttpClient.Builder {
        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level =
                if (enableInterceptor) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        clientBuilder.apply {
            addBasicInterceptors(loggingInterceptor, ConnectivityInterceptor(context))
            okHttpClientConfig.invoke(this)
        }
        return clientBuilder
    }


}

