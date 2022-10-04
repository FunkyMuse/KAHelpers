package com.crazylegend.retrofit.apiresult

import okhttp3.ResponseBody


/**
 * Created by CrazyLegenD on 2/8/19 to long live and prosper !
 * man this aged well
 */
sealed class ApiResult<out T> {


    data class Success<T>(val value: T) : ApiResult<T>() {
        val isValueAListAndNullOrEmpty get() = value is List<*> && value.isEmpty()
        val isValueAListAndNotNullOrEmpty get() = value is List<*> && value.isNotEmpty()
    } // handle UI changes when everything is loaded

    object Loading : ApiResult<Nothing>() // handle loading state
    data class Error(val throwable: Throwable) :
        ApiResult<Nothing>() //this one gets thrown when there's an error on your side or an error we throw from http

    data class ApiError(val responseCode: Int, val errorBody: ResponseBody?) : ApiResult<Nothing>() //whenever the api throws an error
    object Idle : ApiResult<Nothing>()
}

