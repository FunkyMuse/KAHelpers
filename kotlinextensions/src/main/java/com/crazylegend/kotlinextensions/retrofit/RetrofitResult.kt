package com.crazylegend.kotlinextensions.retrofit

import okhttp3.ResponseBody


/**
 * Created by CrazyLegenD on 2/8/19 to long live and prosper !
 */
sealed class RetrofitResult<out T> {

    data class Success<T>(val value: T) : RetrofitResult<T>() // handle UI changes when everything is loaded
    object Loading : RetrofitResult<Nothing>() // handle loading state
    object EmptyData : RetrofitResult<Nothing>() //same as no data except this one returns that no data was obtained from the server
    data class Error(val throwable: Throwable) : RetrofitResult<Nothing>() //this one gets thrown when there's an error on your side
    data class ApiError(val responseCode: Int, val errorBody: ResponseBody?) : RetrofitResult<Nothing>() //whenever the api throws an error

}

