package com.crazylegend.setofusefulkotlinextensions

import android.app.Application
import androidx.lifecycle.*
import com.crazylegend.kotlinextensions.livedata.context
import com.crazylegend.retrofit.RetrofitClient
import com.crazylegend.retrofit.adapter.RetrofitResultAdapterFactory
import com.crazylegend.retrofit.interceptors.ConnectivityInterceptor
import com.crazylegend.retrofit.retrofitResult.RetrofitResult
import com.crazylegend.retrofit.retrofitResult.asNetworkBoundResource
import com.crazylegend.rx.clearAndDispose
import com.crazylegend.setofusefulkotlinextensions.adapter.TestModel
import com.crazylegend.setofusefulkotlinextensions.db.TestRepo
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    private val testRepo = TestRepo(application)

    private val postsData: MutableStateFlow<RetrofitResult<List<TestModel>>> = MutableStateFlow(RetrofitResult.EmptyData)
    val posts = postsData.asStateFlow()

    private val filteredPostsData: MutableLiveData<List<TestModel>> = MutableLiveData()
    val filteredPosts: LiveData<List<TestModel>> = filteredPostsData

    private val compositeDisposable = CompositeDisposable()


    //val apiTest =  apiCallAsFlow { retrofit.getPosts() }

    fun getposts() {
        postsData.value = RetrofitResult.Loading
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
        }
    }


    fun filterBy(query: String) {
        /*filteredPostsData.value = postsData.getSuccess?.filter {
            it.title.contains(query, true)
        }*/
    }


    fun handleApiError(errorBody: ResponseBody?): String? {
        val json = errorBody?.string()
        if (json.isNullOrEmpty()) return savedStateHandle.get<String>(errorStateKey)
        savedStateHandle[errorStateKey] = json

        return savedStateHandle.get<String>(errorStateKey)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clearAndDispose()
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

    inline fun <T> MutableStateFlow<RetrofitResult<T>>.withLoading(function: () -> Unit) {
        value = RetrofitResult.Loading
        function()
    }


}



