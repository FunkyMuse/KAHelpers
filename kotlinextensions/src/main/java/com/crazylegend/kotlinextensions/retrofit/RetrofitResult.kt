package com.crazylegend.kotlinextensions.retrofit

import android.content.Context
import com.crazylegend.kotlinextensions.context.shortToast
import com.crazylegend.kotlinextensions.retrofit.NoConnectionException
import okhttp3.ResponseBody


/**
 * Created by CrazyLegenD on 2/8/19 to long live and prosper !
 */
sealed class RetrofitResult<out T> {

    data class Success<T>(val value:T) : RetrofitResult<T>()
    data class Error(val message: String, val exception: Exception?=null) : RetrofitResult<Nothing>()
    data class ApiError(private val context: Context, private val responseCode: Int,private val errorBody: ResponseBody?, private val showErrorByDefault:Boolean = true) : RetrofitResult<Nothing>(){

        private val throwable = Throwable(errorBody.toString())

        init {
            if (showErrorByDefault){
                showErrors(context)
            }
        }

        fun showErrors(context: Context){
            errorCode(context)
            noConnectionError(context)
        }

        private fun errorCode(context: Context) {
            when (responseCode) {
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

        private fun noConnectionError(context: Context) {
            if (throwable is NoConnectionException) {
                context.shortToast("No internet connection")
            }
        }
    }


}


/*
retrofit?.let {

    compositeDisposable.add(
        it.getPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                it.body()?.let {
                    handleResponse(RetrofitResult.Success(it))
                }

                handleResponse(RetrofitResult.ApiError(this, it.code(), it.errorBody(), true)) //or false if you don't want to show toasts

            }, {
                handleResponse(RetrofitResult.Error(it.message.toString(), Exception(it)))
            })
    )

}


private fun handleResponse(result: RetrofitResult<List<Model>>) {

        when (result) {
            is RetrofitResult.Success<List<Model>> -> {

               result.value.forEach {
                   Log.d("model", it.title.toString())
               }
            }

            is RetrofitResult.Error ->{
                result.exception?.printStackTrace()
            }

            is RetrofitResult.ApiError -> {
               // result.errorCode(this)
               // result.noConnectionError(this)
            }

        }.exhaustive // or without exhaustive if you don't want all methods called

    }


*/
