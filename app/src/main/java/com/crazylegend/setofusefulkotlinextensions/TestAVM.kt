package com.crazylegend.setofusefulkotlinextensions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.crazylegend.common.randomUUIDstring
import com.crazylegend.kotlinextensions.viewmodel.context
import com.crazylegend.retrofit.adapter.RetrofitResultAdapterFactory
import com.crazylegend.retrofit.interceptors.ConnectivityInterceptor
import com.crazylegend.retrofit.randomPhotoIndex
import com.crazylegend.retrofit.retrofitResult.RetrofitResult
import com.crazylegend.retrofit.viewstate.ViewState
import com.crazylegend.retrofit.viewstate.ViewStateContract
import com.crazylegend.retrofit.viewstate.asViewStatePayloadWithEvents
import com.crazylegend.setofusefulkotlinextensions.adapter.TestModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create


/**
 * Created by hristijan on 8/26/19 to long live and prosper !
 */


/**
 * Template created by Hristijan to live long and prosper.
 */

class TestAVM(application: Application, val savedStateHandle: SavedStateHandle) : AndroidViewModel(application),
        ViewStateContract<List<TestModel>> by ViewState() {

    val posts: StateFlow<RetrofitResult<List<TestModel>>> = data

    fun getPosts() {
        viewModelScope.launch {
            setLoading()
            delay(2000) //we're fetching from API some additional delay here
            fetchPosts()
        }
    }

    fun getRandomPosts() {
        viewModelScope.launch {
            setLoading()
            delay(2000) //simulate that we're fetching from API some artificial delay
            RetrofitResult.Success(listOf(
                    TestModel(randomUUIDstring, randomPhotoIndex, randomUUIDstring, randomPhotoIndex),
                    TestModel(randomUUIDstring, randomPhotoIndex, randomUUIDstring, randomPhotoIndex),
                    TestModel(randomUUIDstring, randomPhotoIndex, randomUUIDstring, randomPhotoIndex),
                    TestModel(randomUUIDstring, randomPhotoIndex, randomUUIDstring, randomPhotoIndex),
            )).asViewStatePayloadWithEvents(this@TestAVM)
        }
    }

    private suspend fun setLoading() {
        RetrofitResult.Loading.asViewStatePayloadWithEvents(this)
    }

    private suspend fun fetchPosts() {
        retrofit.getPosts().asViewStatePayloadWithEvents(this)
    }


    private val retrofit by lazy {
        with(Retrofit.Builder()) {
            client(with(OkHttpClient.Builder()) {
                addInterceptor(ConnectivityInterceptor(context))
                build()
            })
            baseUrl(TestApi.API)
            addCallAdapterFactory(RetrofitResultAdapterFactory())
            addConverterFactory(MoshiConverterFactory.create())
            build().create<TestApi>()
        }
    }

    init {
        getPosts()
    }

}

