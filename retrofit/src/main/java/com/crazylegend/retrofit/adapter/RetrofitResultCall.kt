package com.crazylegend.retrofit.adapter

import com.crazylegend.retrofit.retrofitResult.RetrofitResult
import com.crazylegend.retrofit.retrofitResult.retrofitSubscribeList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class RetrofitResultCall<T>(private val proxy: Call<T>) : ResultCallDelegate<T, RetrofitResult<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<RetrofitResult<T>>) = proxy.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            callback.onResponse(this@RetrofitResultCall, Response.success(retrofitSubscribeList(response)))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            callback.onResponse(this@RetrofitResultCall, Response.success(RetrofitResult.Error(t)))
        }
    })

    override fun cloneImpl(): Call<RetrofitResult<T>> = RetrofitResultCall(proxy.clone())
}
