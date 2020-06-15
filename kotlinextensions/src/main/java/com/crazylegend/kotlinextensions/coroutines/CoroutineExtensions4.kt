package com.crazylegend.kotlinextensions.coroutines

import com.crazylegend.kotlinextensions.databaseResult.DBResult
import com.crazylegend.kotlinextensions.databaseResult.databaseError
import com.crazylegend.kotlinextensions.databaseResult.databaseSubscribe
import com.crazylegend.kotlinextensions.databaseResult.databaseSubscribeList
import com.crazylegend.kotlinextensions.retrofit.retrofitResult.RetrofitResult
import com.crazylegend.kotlinextensions.retrofit.retrofitResult.retrofitCallError
import com.crazylegend.kotlinextensions.retrofit.retrofitResult.retrofitSubscribe
import com.crazylegend.kotlinextensions.retrofit.retrofitResult.retrofitSubscribeList
import retrofit2.Response


/**
 * Created by crazy on 6/15/20 to long live and prosper !
 */

suspend fun <T> apiCallList(
        includeEmptyData: Boolean = false,
        apiCall: suspend () -> Response<T>?): RetrofitResult<T> {

    return withIOContext {
        try {
            retrofitSubscribeList(apiCall(), includeEmptyData)
        } catch (t: Throwable) {
            withMainContext {
                retrofitCallError(t)
            }
        }
    }
}

suspend fun <T> apiCall(
        apiCall: suspend () -> Response<T>?): RetrofitResult<T> {
    return withIOContext {
        try {
            retrofitSubscribe(apiCall())
        } catch (t: Throwable) {
            withMainContext {
                retrofitCallError(t)
            }
        }
    }
}

suspend fun <T> dbCallList(
        includeEmptyData: Boolean = false,
        dbCall: suspend () -> T?): DBResult<T> {

    return withIOContext {
        try {
            databaseSubscribeList(dbCall(), includeEmptyData)
        } catch (t: Throwable) {
            withMainContext {
                databaseError(t)
            }
        }
    }
}

suspend fun <T> dbCall(dbCall: suspend () -> T?): DBResult<T> {
    return withIOContext {
        try {
            databaseSubscribe(dbCall())
        } catch (t: Throwable) {
            withMainContext {
                databaseError(t)
            }
        }
    }
}


