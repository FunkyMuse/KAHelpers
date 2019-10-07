package com.crazylegend.setofusefulkotlinextensions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.crazylegend.kotlinextensions.coroutines.makeApiCallAsync
import com.crazylegend.kotlinextensions.handlers.ViewState
import com.crazylegend.kotlinextensions.handlers.hookViewStateResult
import com.crazylegend.kotlinextensions.livedata.liveDataOf
import com.crazylegend.kotlinextensions.retrofit.*


/**
 * Created by hristijan on 8/26/19 to long live and prosper !
 */

/**
 * Template created by Hristijan to live long and prosper.
 */

class TestAVM(application: Application) : AndroidViewModel(application) {


    private val postsData: MutableLiveData<RetrofitResult<List<TestModel>>> = MutableLiveData()
    val posts: LiveData<RetrofitResult<List<TestModel>>> = postsData

    val viewStateResult = postsData.hookViewStateResult(
            loadingText = "LOADING", loadingVisibility = true,
            failureText = "Failed to load", failureVisibility = false,
            successText = "Loaded", successVisibility = false
    )


    private val retrofit by lazy {
        RetrofitClient.moshiInstanceCoroutines(application, TestApi.API, true).create<TestApi>()
    }


    init {
        makeApiCallAsync(postsData) {
            retrofit?.getPosts()
        }
    }

}



