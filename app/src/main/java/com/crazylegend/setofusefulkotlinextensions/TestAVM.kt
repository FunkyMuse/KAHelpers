package com.crazylegend.setofusefulkotlinextensions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.crazylegend.common.randomUUIDstring
import com.crazylegend.kotlinextensions.viewmodel.context
import com.crazylegend.retrofit.adapter.ApiResultAdapterFactory
import com.crazylegend.retrofit.apiresult.ApiResult
import com.crazylegend.retrofit.interceptors.ConnectivityInterceptor
import com.crazylegend.retrofit.randomPhotoIndex
import com.crazylegend.retrofit.viewstate.ViewState
import com.crazylegend.retrofit.viewstate.ViewStateContract
import com.crazylegend.retrofit.viewstate.asViewStatePayloadWithEvents
import com.crazylegend.setofusefulkotlinextensions.adapter.TestModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create


/**
 * Created by hristijan on 8/26/19 to long live and prosper !
 */
class TestAVM(application: Application, val savedStateHandle: SavedStateHandle) : AndroidViewModel(application),
        ViewStateContract<List<TestModel>> by ViewState() {

    sealed class TestAVMIntent {
        object GetPosts : TestAVMIntent()
        object GetRandomPosts : TestAVMIntent()
    }

    fun sendEvent(testAVMIntent: TestAVMIntent) {
        viewModelScope.launch { intents.emit(testAVMIntent) }
    }

    private val intents = MutableSharedFlow<TestAVMIntent>()
    val posts: StateFlow<ApiResult<List<TestModel>>> = data

    private fun getPosts() {
        viewModelScope.launch {
            setLoading()
            delay(2000) //we're fetching from API some additional delay here
            fetchPosts()
        }
    }

    private fun getRandomPosts() {
        viewModelScope.launch {
            setLoading()
            delay(2000) //simulate that we're fetching from API some artificial delay
            ApiResult.Success(listOf(
                    TestModel(randomUUIDstring, randomPhotoIndex, randomUUIDstring, randomPhotoIndex),
                    TestModel(randomUUIDstring, randomPhotoIndex, randomUUIDstring, randomPhotoIndex),
                    TestModel(randomUUIDstring, randomPhotoIndex, randomUUIDstring, randomPhotoIndex),
                    TestModel(randomUUIDstring, randomPhotoIndex, randomUUIDstring, randomPhotoIndex),
            )).asViewStatePayloadWithEvents(this@TestAVM)
        }
    }

    private suspend fun setLoading() {
        ApiResult.Loading.asViewStatePayloadWithEvents(this)
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
            addCallAdapterFactory(ApiResultAdapterFactory())
            addConverterFactory(MoshiConverterFactory.create())
            build().create<TestApi>()
        }
    }

    init {
        getPosts()
        viewModelScope.launch {
            intents.collect { testAVMIntent ->
                handleIntents(testAVMIntent)
            }
        }
    }

    private fun handleIntents(testAVMIntent: TestAVMIntent) {
        when(testAVMIntent){
            TestAVMIntent.GetPosts -> getPosts()
            TestAVMIntent.GetRandomPosts -> getRandomPosts()
        }
    }

}

