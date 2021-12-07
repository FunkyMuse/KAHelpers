package com.crazylegend.retrofit.retrofitResult

import com.crazylegend.retrofit.apiresult.ApiResult
import com.crazylegend.retrofit.apiresult.apiResultSubscribe
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

/**
 * Created by crazy on 1/12/21 to long live and prosper !
 */
@RunWith(JUnit4::class)
class ApiResultExtensionsTest {

    @Test
    fun testListResponseNotEmpty() {
        val response = apiResultSubscribe(Response.success(emptyList<Any>()))
        assert(response is ApiResult.Success)
    }
}