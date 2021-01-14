package com.crazylegend.setofusefulkotlinextensions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crazylegend.retrofit.RetrofitClient
import com.crazylegend.retrofit.adapter.RetrofitResultAdapterFactory
import com.crazylegend.retrofit.retrofitResult.RetrofitResult
import com.crazylegend.retrofit.retrofitResult.asNetworkBoundResource
import com.crazylegend.rx.clearAndDispose
import com.crazylegend.setofusefulkotlinextensions.adapter.TestModel
import com.crazylegend.setofusefulkotlinextensions.db.TestRepo
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create


/**
 * Created by hristijan on 8/26/19 to long live and prosper !
 */

/**
 * Template created by Hristijan to live long and prosper.
 */

class TestAVM(application: Application) : AndroidViewModel(application) {

    private val testRepo = TestRepo(application)

    /*private val postsData: MediatorLiveData<RetrofitResult<List<TestModel>>> = MediatorLiveData()
    val posts: LiveData<RetrofitResult<List<TestModel>>> = postsData*/

    private val postsData: MutableStateFlow<RetrofitResult<List<TestModel>>> = MutableStateFlow(RetrofitResult.EmptyData)
    val posts: MutableStateFlow<RetrofitResult<List<TestModel>>> = postsData

    private val filteredPostsData: MutableLiveData<List<TestModel>> = MutableLiveData()
    val filteredPosts: LiveData<List<TestModel>> = filteredPostsData

    private val compositeDisposable = CompositeDisposable()


    //val apiTest =  apiCallAsFlow { retrofit.getPosts() }

    fun getposts() {
        postsData.value = RetrofitResult.Loading

        viewModelScope.launch {
            postsData.asNetworkBoundResource(saveToDatabase = {
                testRepo.insertList(it)
            }, shouldLoadFromNetworkOnDatabaseCondition = {
                it.isNullOrEmpty()
            }, loadFromDatabase = {
                testRepo.getAll()
            }, loadFromNetwork = {
                retrofit.getPostsAdapter()
            })
        }
    }


    fun filterBy(query: String) {
        /*filteredPostsData.value = postsData.getSuccess?.filter {
            it.title.contains(query, true)
        }*/
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clearAndDispose()
    }

    private val retrofit by lazy {
        RetrofitClient.customInstance(application, TestApi.API, true, builderCallback = {
            addCallAdapterFactory(RetrofitResultAdapterFactory())
            addConverterFactory(MoshiConverterFactory.create())
            this
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


