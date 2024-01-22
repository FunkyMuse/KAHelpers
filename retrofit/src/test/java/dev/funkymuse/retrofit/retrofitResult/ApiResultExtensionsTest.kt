package dev.funkymuse.retrofit.retrofitResult

import dev.funkymuse.retrofit.apiresult.ApiResult
import dev.funkymuse.retrofit.apiresult.apiResultSubscribe
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response


@RunWith(JUnit4::class)
class ApiResultExtensionsTest {

    @Test
    fun testListResponseNotEmpty() {
        val response = apiResultSubscribe(Response.success(emptyList<Any>()))
        assert(response is ApiResult.Success)
    }
}