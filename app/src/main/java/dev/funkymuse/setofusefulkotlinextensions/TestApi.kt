package dev.funkymuse.setofusefulkotlinextensions

import dev.funkymuse.retrofit.apiresult.ApiResult
import dev.funkymuse.setofusefulkotlinextensions.adapter.TestModel
import retrofit2.http.GET


interface TestApi {


    @GET("posts")
    suspend fun getPosts(): ApiResult<List<TestModel>>

    companion object {
        const val API = "https://jsonplaceholder.typicode.com/"
    }
}