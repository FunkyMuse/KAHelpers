package com.crazylegend.retrofit

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by Hristijan on 1/23/19 to long live and prosper !
 */
object RetrofitClient {

    private var retrofit: Retrofit? = null

    var connectTimeout = 10L
    var readTimeout = 10L
    var writeTimeout = 10L
    var connectionTimeUnit = TimeUnit.SECONDS

    private val gsonConverter by lazy {
        GsonConverterFactory.create()
    }

    private val moshiConverter by lazy {
        MoshiConverterFactory.create()
    }

    inline fun moshiConverterFactoryBuilder(moshi: Moshi, builderCallback: MoshiConverterFactory.() -> Unit) = MoshiConverterFactory.create(moshi).also(builderCallback)

    inline fun moshiConverterFactoryBuilder(builderCallback: MoshiConverterFactory.() -> Unit) = MoshiConverterFactory.create().also(builderCallback)

    private val rxJavaAdapter by lazy {
        RxJava3CallAdapterFactory.create()
    }

    fun removeRetrofitInstance() {
        retrofit = null
    }

    fun customInstanceFactory(baseUrl: String, factory: Converter.Factory,
                              enableDebuggingInterceptor: Boolean = false,
                              okHttpClientConfig: OkHttpClient.Builder.() -> Unit = {}): Retrofit {
        val clientBuilder = buildClient(enableDebuggingInterceptor, okHttpClientConfig)
        doesRetrofitNeedsBuild(baseUrl) {
            retrofit = Retrofit.Builder().buildRetrofitWith(baseUrl, clientBuilder, factory).build()
        }
        return retrofit!!
    }


    fun customInstance(baseUrl: String, enableDebuggingInterceptor: Boolean = false,
                       okHttpClientConfig: OkHttpClient.Builder.() -> Unit = {},
                       builderCallback: Retrofit.Builder.() -> Unit = { }): Retrofit {
        val clientBuilder = buildClient(enableDebuggingInterceptor, okHttpClientConfig)
        doesRetrofitNeedsBuild(baseUrl) {
            retrofit = Retrofit.Builder().also { it.builderCallback() }.buildRetrofitWith(baseUrl, clientBuilder).build()
        }
        return retrofit!!
    }


    fun gsonInstanceRxJava(baseUrl: String, enableDebuggingInterceptor: Boolean = false, okHttpClientConfig: OkHttpClient.Builder.() -> Unit = {}): Retrofit {
        val clientBuilder = buildClient(enableDebuggingInterceptor, okHttpClientConfig)
        doesRetrofitNeedsBuild(baseUrl) {
            retrofit = Retrofit.Builder().buildRetrofitWith(baseUrl, clientBuilder, gsonConverter, rxJavaAdapter).build()
        }
        return retrofit!!
    }


    fun gsonInstanceCouroutines(baseUrl: String, enableDebuggingInterceptor: Boolean = false, okHttpClientConfig: OkHttpClient.Builder.() -> Unit = {}): Retrofit {
        val clientBuilder = buildClient(enableDebuggingInterceptor, okHttpClientConfig)
        doesRetrofitNeedsBuild(baseUrl) {
            retrofit = Retrofit.Builder().buildRetrofitWith(baseUrl, clientBuilder, gsonConverter).build()
        }
        return retrofit!!
    }

    fun moshiInstanceRxJava(baseUrl: String, enableDebuggingInterceptor: Boolean = false, okHttpClientConfig: OkHttpClient.Builder.() -> Unit = {}): Retrofit {
        val clientBuilder = buildClient(enableDebuggingInterceptor, okHttpClientConfig)
        doesRetrofitNeedsBuild(baseUrl) {
            retrofit = Retrofit.Builder().buildRetrofitWith(baseUrl, clientBuilder, moshiConverter, rxJavaAdapter).build()
        }
        return retrofit!!
    }


    fun moshiInstanceCoroutines(baseUrl: String, enableDebuggingInterceptor: Boolean = false, okHttpClientConfig: OkHttpClient.Builder.() -> Unit = {}): Retrofit {
        val clientBuilder = buildClient(enableDebuggingInterceptor, okHttpClientConfig)
        doesRetrofitNeedsBuild(baseUrl) {
            retrofit = Retrofit.Builder().buildRetrofitWith(baseUrl, clientBuilder, moshiConverter).build()
        }
        return retrofit!!
    }

    private fun Retrofit.Builder.buildRetrofitWith(baseUrl: String,
                                                   okHttpClient: OkHttpClient.Builder, converterFactory: Converter.Factory,
                                                   callAdapterFactory: CallAdapter.Factory): Retrofit.Builder {
        baseUrl(baseUrl).client(okHttpClient.build()).addConverterFactory(converterFactory).addCallAdapterFactory(callAdapterFactory)
        return this
    }

    private fun Retrofit.Builder.buildRetrofitWith(baseUrl: String, okHttpClient: OkHttpClient.Builder, converterFactory: Converter.Factory): Retrofit.Builder {
        baseUrl(baseUrl).client(okHttpClient.build()).addConverterFactory(converterFactory)
        return this
    }

    private fun Retrofit.Builder.buildRetrofitWith(baseUrl: String, okHttpClient: OkHttpClient.Builder): Retrofit.Builder {
        baseUrl(baseUrl).client(okHttpClient.build())
        return this
    }

    private fun OkHttpClient.Builder.addBasicInterceptors(loggingInterceptor: HttpLoggingInterceptor) {
        addInterceptor(loggingInterceptor)
        connectTimeout(connectTimeout, connectionTimeUnit)
        readTimeout(readTimeout, connectionTimeUnit)
        writeTimeout(writeTimeout, connectionTimeUnit)
    }

    private inline fun doesRetrofitNeedsBuild(baseUrl: String, function: () -> Unit) {
        if (retrofit == null) {
            function()
        } else {
            if (retrofit?.baseUrl()?.toString() != baseUrl) {
                function()
            }
        }
    }

    private inline fun buildClient(enableDebuggingInterceptor: Boolean, okHttpClientConfig: OkHttpClient.Builder.() -> Unit = {}): OkHttpClient.Builder {
        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level =
                if (enableDebuggingInterceptor) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        clientBuilder.apply {
            addBasicInterceptors(loggingInterceptor)
            okHttpClientConfig.invoke(this)
        }
        return clientBuilder
    }


}

