package com.crazylegend.setofusefulkotlinextensions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.crazylegend.livedata.context
import com.crazylegend.retrofit.RetrofitClient
import com.crazylegend.retrofit.adapter.RetrofitResultAdapterFactory
import com.crazylegend.retrofit.apiCall
import com.crazylegend.retrofit.interceptors.ConnectivityInterceptor
import com.crazylegend.retrofit.retrofitStateInitialLoading
import com.crazylegend.setofusefulkotlinextensions.adapter.TestModel
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.ResponseBody
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create


/**
 * Created by hristijan on 8/26/19 to long live and prosper !
 */


/**
 * Template created by Hristijan to live long and prosper.
 */

class TestAVM(application: Application, private val savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {

    companion object {
        private const val errorStateKey = "errorJSONKey"
    }

    private val postsData = retrofitStateInitialLoading<List<TestModel>>()
    val posts = postsData.asStateFlow()

    fun getposts() {
        apiCall(postsData) {
            retrofit.getPostsAdapter()
        }
        /*postsData.value = RetrofitResult.Loading
        viewModelScope.launch {
            postsData.asNetworkBoundResource(
                    saveToDatabase = {
                        testRepo.insertList(it)
                    },
                    shouldLoadFromNetworkOnDatabaseCondition = {
                        it.isNullOrEmpty()
                    },
                    loadFromDatabase = {
                        testRepo.getAll()
                    },
                    loadFromNetwork = {
                        retrofit.getPostsAdapter()
                    })
        }*/
    }

    fun handleApiError(errorBody: ResponseBody?): String? {
        val json = errorBody?.string()
        if (json.isNullOrEmpty()) return savedStateHandle.get<String>(errorStateKey)
        savedStateHandle[errorStateKey] = json

        return savedStateHandle.get<String>(errorStateKey)
    }


    private val retrofit by lazy {
        RetrofitClient.customInstance(baseUrl = TestApi.API, true, builderCallback = {
            addCallAdapterFactory(RetrofitResultAdapterFactory())
            addConverterFactory(MoshiConverterFactory.create())
        }, okHttpClientConfig = {
            addInterceptor(ConnectivityInterceptor(context))
        }).create<TestApi>()
    }

    init {
        getposts()
    }

}



