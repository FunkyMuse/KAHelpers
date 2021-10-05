package com.crazylegend.retrofit.retrofitResult

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

/**
 * Created by crazy on 1/12/21 to long live and prosper !
 */
@RunWith(JUnit4::class)
class RetrofitResultExtensionsTest {

    @Test
    fun testListResponseNotEmpty() {
        val response = retrofitSubscribe(Response.success(emptyList<Any>()))
        assert(response is RetrofitResult.Success)
    }
}