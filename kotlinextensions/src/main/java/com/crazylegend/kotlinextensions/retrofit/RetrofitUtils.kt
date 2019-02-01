package com.crazylegend.kotlinextensions.retrofit

import retrofit2.Retrofit


/**
 * Created by Hristijan on 1/25/19 to long live and prosper !
 */


inline fun <reified T> Retrofit?.create(): T? {
    return this?.create(T::class.java)
}

/* example
private  val retrofit by lazy {
    RetrofitClient.getGsonRetrofit(context, BASE_URL).create<RetrofitInterface>()
}*/
