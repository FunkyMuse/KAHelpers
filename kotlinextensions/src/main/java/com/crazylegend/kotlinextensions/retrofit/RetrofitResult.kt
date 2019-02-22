package com.crazylegend.kotlinextensions.retrofit

import android.content.Context
import com.crazylegend.kotlinextensions.context.shortToast
import com.crazylegend.kotlinextensions.retrofit.NoConnectionException
import okhttp3.ResponseBody


/**
 * Created by CrazyLegenD on 2/8/19 to long live and prosper !
 */
sealed class RetrofitResult<out T> {

    data class Success<T>(val value: T) : RetrofitResult<T>()
    object Loading : RetrofitResult<Nothing>()
    object NoData : RetrofitResult<Nothing>()
    data class Error(val message: String, val exception: Exception?=null, val throwable: Throwable) : RetrofitResult<Nothing>()
    data class ApiError(private val context: Context, private val responseCode: Int, private val errorBody: ResponseBody?, private val showErrorByDefault:Boolean = true) : RetrofitResult<Nothing>(){

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
 init {
        response.value = RetrofitResult.NoData
    }

    fun fetchData() {
        response.value = RetrofitResult.Loading

        retrofit?.let {
            compositeDisposable.add(
                it.getPosts()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ res ->

                        if (res.isSuccessful){

                            res?.body()?.let {
                                response.value = RetrofitResult.Success(it)
                            }

                        } else {
                            response.value = RetrofitResult.ApiError(getApplication(), res.code(), res.errorBody(), true)
                        }

                    }, {
                        response.value = RetrofitResult.Error(it.message.toString(), Exception(it), it)
                    })
            )
        }
    }


 liveData.getFetchedData().observe(this, Observer { result ->

            when (result) {
                is RetrofitResult.Success<List<Model>> -> {
                    handleSuccess(result.value)
                }

                is RetrofitResult.Error -> {
                    handleError(result)
                }

                is RetrofitResult.NoData -> {
                    handleNoData()
                }

                is RetrofitResult.Loading -> {
                    loadingDataHandle()
                }

                is RetrofitResult.ApiError -> {
                    showApiErrors(result)
                }

            }.exhaustive

        })


*/
