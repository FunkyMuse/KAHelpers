package com.crazylegend.kotlinextensions.codestyle

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.crazylegend.kotlinextensions.retrofit.RetrofitClient
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit


/**
 * Created by hristijan on 7/30/19 to long live and prosper !
 */
abstract class AbstractAVMClients<out T>(application: Application, private val service: Class<T>,
                                         connectTimeout:Long = 60, readTimeout:Long = 60, writeTimeout:Long = 60,
                                         connectionTimeUnit: TimeUnit = TimeUnit.SECONDS) : AndroidViewModel(application) {

    abstract fun setBaseUrl(): String
    abstract fun enableInterceptor(): Boolean

    val compositeDisposable = CompositeDisposable()

    val moshiRX by lazy {
        RetrofitClient.moshiInstanceRxJava(application, setBaseUrl(), enableInterceptor(), connectTimeout, readTimeout, writeTimeout, connectionTimeUnit)?.create(service)
    }

    val gsonRX by lazy {
        RetrofitClient.gsonInstanceRxJava(application, setBaseUrl(), enableInterceptor(), connectTimeout, readTimeout, writeTimeout, connectionTimeUnit)?.create(service)
    }

    val moshiCoroutines by lazy {
        RetrofitClient.moshiInstanceCoroutines(application, setBaseUrl(), enableInterceptor(), connectTimeout, readTimeout, writeTimeout, connectionTimeUnit)?.create(service)
    }

    val gsonCoroutines by lazy {
        RetrofitClient.gsonInstanceCouroutines(application, setBaseUrl(), enableInterceptor(), connectTimeout, readTimeout, writeTimeout, connectionTimeUnit)?.create(service)
    }


    override fun onCleared() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
        super.onCleared()
    }

}