package com.crazylegend.retrofit.adapter

import com.crazylegend.retrofit.apiresult.ApiResult
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 * Created by crazy on 1/6/21 to long live and prosper !
 */

internal class ApiResultAdapter(private val type: Type) : CallAdapter<Type, Call<ApiResult<Type>>> {

    override fun responseType(): Type = type

    override fun adapt(call: Call<Type>): Call<ApiResult<Type>> = ApiResultCall(call)
}