package com.crazylegend.retrofit.adapter

import com.crazylegend.retrofit.apiresult.ApiResult
import com.crazylegend.retrofit.apiresult.apiResultSubscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class ApiResultCall<T>(private val proxy: Call<T>) : ResultCallDelegate<T, ApiResult<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<ApiResult<T>>) = proxy.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            callback.onResponse(this@ApiResultCall, Response.success(apiResultSubscribe(response)))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            callback.onResponse(this@ApiResultCall, Response.success(ApiResult.Error(t)))
        }
    })

    override fun cloneImpl(): Call<ApiResult<T>> = ApiResultCall(proxy.clone())
}
