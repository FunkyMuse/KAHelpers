package com.crazylegend.kotlinextensions.retrofit

import android.content.Context
import com.crazylegend.kotlinextensions.isNull
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
    
    fun removeRetrofitInstance(){
        retrofit = null
    }

    fun gsonInstanceRxJava(context: Context, baseUrl: String, enableInterceptor: Boolean = false,
                           connectTimeout: Long = 60, readTimeout: Long = 60, writeTimeout: Long = 60,
                           connectionTimeUnit: TimeUnit = TimeUnit.SECONDS, okHttpClientConfig: OkHttpClient.Builder.() -> Unit = {}): Retrofit? {

        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level =
                if (enableInterceptor) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE


        clientBuilder.apply {
            addInterceptors(loggingInterceptor, ConnectivityInterceptor(context), connectTimeout, readTimeout, writeTimeout, connectionTimeUnit)
            okHttpClientConfig.invoke(this)
        }

        if (retrofit.isNull) {
            retrofit = buildRetrofit(baseUrl, clientBuilder, GsonConverterFactory.create(), RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))

        } else {
            retrofit?.baseUrl()?.let {
                if (it.toString() != baseUrl) {
                    retrofit = buildRetrofit(baseUrl, clientBuilder, GsonConverterFactory.create(), RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                }
            }
        }

        return retrofit

    }


    fun gsonInstanceCouroutines(context: Context, baseUrl: String, enableInterceptor: Boolean = false,
                                connectTimeout: Long = 60, readTimeout: Long = 60, writeTimeout: Long = 60,
                                connectionTimeUnit: TimeUnit = TimeUnit.SECONDS, okHttpClientConfig: OkHttpClient.Builder.() -> Unit = {}): Retrofit? {

        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level =
                if (enableInterceptor) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE


        clientBuilder.apply {
            addInterceptors(loggingInterceptor, ConnectivityInterceptor(context), connectTimeout, readTimeout, writeTimeout, connectionTimeUnit)
            okHttpClientConfig.invoke(this)
        }

        if (retrofit.isNull) {
            retrofit = buildRetrofit(baseUrl, clientBuilder, GsonConverterFactory.create())

        } else {
            retrofit?.baseUrl()?.let {
                if (it.toString() != baseUrl) {
                    retrofit = buildRetrofit(baseUrl, clientBuilder, GsonConverterFactory.create())
                }
            }
        }

        return retrofit

    }

    fun moshiInstanceRxJava(context: Context, baseUrl: String, enableInterceptor: Boolean = false,
                            connectTimeout: Long = 60, readTimeout: Long = 60, writeTimeout: Long = 60,
                            connectionTimeUnit: TimeUnit = TimeUnit.SECONDS, okHttpClientConfig: OkHttpClient.Builder.() -> Unit = {}): Retrofit? {

        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level =
                if (enableInterceptor) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE


        clientBuilder.apply {
            addInterceptors(loggingInterceptor, ConnectivityInterceptor(context), connectTimeout, readTimeout, writeTimeout, connectionTimeUnit)
            okHttpClientConfig.invoke(this)

        }

        if (retrofit.isNull) {
            retrofit = buildRetrofit(baseUrl, clientBuilder, MoshiConverterFactory.create(), RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))

        } else {
            retrofit?.baseUrl()?.let {

                if (it.toString() != baseUrl) {
                    retrofit = buildRetrofit(baseUrl, clientBuilder, MoshiConverterFactory.create(), RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                }
            }
        }

        return retrofit

    }


    fun moshiInstanceCoroutines(context: Context, baseUrl: String, enableInterceptor: Boolean = false,
                                connectTimeout: Long = 60, readTimeout: Long = 60, writeTimeout: Long = 60,
                                connectionTimeUnit: TimeUnit = TimeUnit.SECONDS, okHttpClientConfig: OkHttpClient.Builder.() -> Unit = {}): Retrofit? {

        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level =
                if (enableInterceptor) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE


        clientBuilder.apply {
            addInterceptors(loggingInterceptor, ConnectivityInterceptor(context), connectTimeout, readTimeout, writeTimeout, connectionTimeUnit)
            okHttpClientConfig.invoke(this)

        }

        if (retrofit.isNull) {
            retrofit = buildRetrofit(baseUrl, clientBuilder, MoshiConverterFactory.create())

        } else {
            retrofit?.baseUrl()?.let {

                if (it.toString() != baseUrl) {
                    retrofit = buildRetrofit(baseUrl, clientBuilder, MoshiConverterFactory.create())
                }
            }
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

    private fun OkHttpClient.Builder.addInterceptors(loggingInterceptor: HttpLoggingInterceptor, connectivityInterceptor: ConnectivityInterceptor, connectTimeout: Long, readTimeout: Long, writeTimeout: Long, timeUnit: TimeUnit = TimeUnit.SECONDS) {
        addInterceptor(loggingInterceptor)
        addInterceptor(connectivityInterceptor)
        connectTimeout(connectTimeout, timeUnit)
        readTimeout(readTimeout, timeUnit)
        writeTimeout(writeTimeout, timeUnit)
    }


}

