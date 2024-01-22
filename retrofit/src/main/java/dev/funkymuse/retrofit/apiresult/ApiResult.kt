package dev.funkymuse.retrofit.apiresult

import okhttp3.ResponseBody


/**
 * man this aged well
 */
sealed class ApiResult<out T> {

    data class Success<T>(val value: T) : ApiResult<T>() {
        val isValueAListAndNullOrEmpty get() = value is List<*> && value.isEmpty()
        val isValueAListAndNotNullOrEmpty get() = value is List<*> && value.isNotEmpty()
    } // handle UI changes when everything is loaded

    data object Loading : ApiResult<Nothing>() // handle loading state
    data class Error(val throwable: Throwable) :
        ApiResult<Nothing>() //this one gets thrown when there's an error on your side or an error we throw from http

    data class ApiError(val responseCode: Int, val errorBody: ResponseBody?) : ApiResult<Nothing>() //whenever the api throws an error
    data object Idle : ApiResult<Nothing>()
}

