package com.funkymuse.setofusefulkotlinextensions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.funkymuse.common.randomUUIDstring
import com.funkymuse.kotlinextensions.viewmodel.context
import com.funkymuse.retrofit.adapter.ApiResultAdapterFactory
import com.funkymuse.retrofit.apiresult.ApiResult
import com.funkymuse.retrofit.interceptors.ConnectivityInterceptor
import com.funkymuse.retrofit.randomPhotoIndex
import com.funkymuse.retrofit.viewstate.event.ViewStatefulEvent
import com.funkymuse.retrofit.viewstate.state.ViewState
import com.funkymuse.retrofit.viewstate.state.asViewStatePayloadWithEvents
import com.funkymuse.setofusefulkotlinextensions.adapter.TestModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create


class TestAVM(
    application: Application,
    val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val viewEventProvider = ViewEventProvider()
    val viewState = ViewState<List<TestModel>>(viewEventProvider)

    //use delegation to avoid having these properties since the view event provider can be injected easily
    val viewEvent = viewEventProvider.viewStatefulEvent
    //

    sealed class TestAVMIntent {
        object GetPosts : TestAVMIntent()
        object GetRandomPosts : TestAVMIntent()
    }

    fun sendEvent(testAVMIntent: TestAVMIntent) {
        viewModelScope.launch { intents.emit(testAVMIntent) }
    }

    private val intents = MutableSharedFlow<TestAVMIntent>()
    val posts: StateFlow<ViewStatefulEvent> = viewState.viewState

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
            ApiResult.Success(
                listOf(
                    TestModel(randomUUIDstring, randomPhotoIndex, randomUUIDstring, randomPhotoIndex),
                    TestModel(randomUUIDstring, randomPhotoIndex, randomUUIDstring, randomPhotoIndex),
                    TestModel(randomUUIDstring, randomPhotoIndex, randomUUIDstring, randomPhotoIndex),
                    TestModel(randomUUIDstring, randomPhotoIndex, randomUUIDstring, randomPhotoIndex),
                )
            ).asViewStatePayloadWithEvents(viewState)
        }
    }

    private suspend fun setLoading() {
        ApiResult.Loading.asViewStatePayloadWithEvents(viewState)
    }

    private suspend fun fetchPosts() {
        retrofit.getPosts().asViewStatePayloadWithEvents(viewState)
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
        when (testAVMIntent) {
            TestAVMIntent.GetPosts -> getPosts()
            TestAVMIntent.GetRandomPosts -> getRandomPosts()
        }
    }

}

