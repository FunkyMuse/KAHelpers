package com.crazylegend.kotlinextensions.retrofit

import android.content.Context
import com.crazylegend.kotlinextensions.context.shortToast
import retrofit2.Response

/**
 * Created by Hristijan on 1/25/19 to long live and prosper !
 */
object APIError {

    fun <T> errorCode(context: Context, response: Response<T>) {
        when (response.code()) {
            400 -> {

                // bad request
                context.shortToast("Bad Request")
            }

            401 -> {
                // unauthorized
                context.shortToast("Unauthorized")
            }

            404 -> {
                // not found
                context.shortToast("Not found")
            }

            408 -> {
                // time out
                context.shortToast("Time out")
            }

            422 -> {
                // account exists
                context.shortToast("Account with that email already exists")
            }

            500 -> {
                // internal server error
                context.shortToast("Server error")
            }

            502 -> {
                // bad gateway
                context.shortToast("Bad gateway")
            }
            504 -> {
                // gateway timeout
                context.shortToast("Gateway timeout")
            }
        }

    }

    fun showError(context: Context, t: Throwable) {
        if (t is NoConnectionException) {
            context.shortToast("No internet connection")
        } else {
            context.shortToast(t.message.toString())
        }
    }

}