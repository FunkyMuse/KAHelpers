package com.crazylegend.setofusefulkotlinextensions

import com.crazylegend.retrofit.apiresult.ApiResult
import com.crazylegend.setofusefulkotlinextensions.adapter.TestModel
import retrofit2.http.GET


/**
 * Created by hristijan on 8/26/19 to long live and prosper !
 */
interface TestApi {


    @GET("posts")
    suspend fun getPosts(): ApiResult<List<TestModel>>

    companion object {
        const val API = "https://jsonplaceholder.typicode.com/"
    }
}