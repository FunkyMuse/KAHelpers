package com.crazylegend.setofusefulkotlinextensions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.crazylegend.kotlinextensions.coroutines.makeApiCallLiveData
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.retrofit.RetrofitClient
import com.crazylegend.kotlinextensions.retrofit.RetrofitResult
import com.crazylegend.setofusefulkotlinextensions.adapter.TestModel
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create


/**
 * Created by hristijan on 8/26/19 to long live and prosper !
 */

/**
 * Template created by Hristijan to live long and prosper.
 */

class TestAVM(application: Application, testModel: TestModel, key:Int, string: String) : AndroidViewModel(application) {

    private val postsData: MediatorLiveData<RetrofitResult<List<TestModel>>> = MediatorLiveData()
    val posts: LiveData<RetrofitResult<List<TestModel>>> = postsData


    fun getposts() {
        makeApiCallLiveData(postsData) { retrofit?.getPosts() }
    }

    init {
        debug("KEY $testModel")
        debug("KEY $key")
        debug("KEY $string")
        getposts()
    }


    private val retrofit by lazy {
        RetrofitClient.customInstance(application, TestApi.API, false, {
        }){
            addConverterFactory(MoshiConverterFactory.create())
            this
        }?.create<TestApi>()
    }


}



