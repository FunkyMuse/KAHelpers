package dev.funkymuse.retrofit.adapter

import dev.funkymuse.retrofit.apiresult.ApiResult
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

internal class ApiResultAdapter(private val type: Type) : CallAdapter<Type, Call<ApiResult<Type>>> {

    override fun responseType(): Type = type

    override fun adapt(call: Call<Type>): Call<ApiResult<Type>> = ApiResultCall(call)
}