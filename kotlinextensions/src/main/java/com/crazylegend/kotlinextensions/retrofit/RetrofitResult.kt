package com.crazylegend.kotlinextensions.retrofit

import android.content.Context
import com.crazylegend.kotlinextensions.context.shortToast
import okhttp3.ResponseBody


/**
 * Created by CrazyLegenD on 2/8/19 to long live and prosper !
 */
sealed class RetrofitResult<out T> {

    data class Success<T>(val value: T) : RetrofitResult<T>()
    object Loading : RetrofitResult<Nothing>()
    object NoData : RetrofitResult<Nothing>() //this should be initial, before the data is loaded
    object EmptyData : RetrofitResult<Nothing>()
    data class Error(val message: String, val exception: Exception? = null, val throwable: Throwable) : RetrofitResult<Nothing>()
    data class ApiError<T>(val responseCode: Int, val errorBody: ResponseBody?, val callModel: T? = null) : RetrofitResult<T>()
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
                    .observeOn(AndroidSchedulers.mainThreadScheduler())
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
