package com.crazylegend.setofusefulkotlinextensions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.crazylegend.retrofit.adapter.RetrofitResultAdapterFactory
import com.crazylegend.retrofit.retrofitResult.RetrofitResult
import com.crazylegend.setofusefulkotlinextensions.adapter.TestModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.ResponseBody
import retrofit2.Retrofit
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

    private val postsData = MutableStateFlow<RetrofitResult<List<TestModel>>>(RetrofitResult.Idle)
    val posts = postsData.asStateFlow()

    fun getposts() {
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
        with(Retrofit.Builder()){
            baseUrl(TestApi.API)
            addCallAdapterFactory(RetrofitResultAdapterFactory())
            addConverterFactory(MoshiConverterFactory.create())
            build().create<TestApi>()
        }
    }

    init {
        getposts()
    }

}



