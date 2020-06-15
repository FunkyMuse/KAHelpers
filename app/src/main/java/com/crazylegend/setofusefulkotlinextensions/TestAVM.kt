package com.crazylegend.setofusefulkotlinextensions

import android.app.Application
import androidx.lifecycle.*
import com.crazylegend.kotlinextensions.coroutines.apiCallList
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.retrofit.RetrofitClient
import com.crazylegend.kotlinextensions.retrofit.retrofitResult.RetrofitResult
import com.crazylegend.kotlinextensions.retrofit.retrofitResult.getSuccess
import com.crazylegend.kotlinextensions.rx.clearAndDispose
import com.crazylegend.setofusefulkotlinextensions.adapter.TestModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.create


/**
 * Created by hristijan on 8/26/19 to long live and prosper !
 */

/**
 * Template created by Hristijan to live long and prosper.
 */

class TestAVM(application: Application, testModel: TestModel, key: Int, string: String) : AndroidViewModel(application) {

    private val postsData: MediatorLiveData<RetrofitResult<List<TestModel>>> = MediatorLiveData()
    val posts: LiveData<RetrofitResult<List<TestModel>>> = postsData

    private val filteredPostsData: MutableLiveData<List<TestModel>> = MutableLiveData()
    val filteredPosts: LiveData<List<TestModel>> = filteredPostsData

    private val compositeDisposable = CompositeDisposable()

    fun getposts() {
        /*compositeDisposable.makeApiCallListSingle(postsData, true) {
            retrofit2.getPostsRx()
        }*/

        viewModelScope.launch {
            val value =  apiCallList(true){
                Response.success<List<TestModel>>(emptyList())
            }
            postsData.value = value
        }

        //makeApiCallLiveData(postsData) { retrofit?.getPosts() }
    }

    fun filterBy(query: String) {
        filteredPostsData.value = postsData.getSuccess?.filter {
            it.title.contains(query, true)
        }
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clearAndDispose()
    }

    private val retrofit by lazy {
        RetrofitClient.moshiInstanceCoroutines(application, TestApi.API, false).create<TestApi>()
    }
    private val retrofit2 by lazy {
        RetrofitClient.moshiInstanceRxJava(application, TestApi.API, false).create<TestApi>()
    }


    init {
        debug("KEY $testModel")
        debug("KEY $key")
        debug("KEY $string")
        getposts()
    }
}



