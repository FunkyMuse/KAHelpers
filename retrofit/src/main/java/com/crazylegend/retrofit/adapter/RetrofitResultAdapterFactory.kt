package com.crazylegend.retrofit.adapter

import com.crazylegend.retrofit.retrofitResult.RetrofitResult
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by crazy on 1/7/21 to long live and prosper !
 */
class RetrofitResultAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? = when (getRawType(returnType)) {
        Call::class.java -> {
            val callType = getParameterUpperBound(0, returnType as ParameterizedType)
            when (getRawType(callType)) {
                RetrofitResult::class.java -> {
                    val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                    RetrofitResultAdapter(resultType)
                }
                else -> null
            }
        }
        else -> null
    }
}