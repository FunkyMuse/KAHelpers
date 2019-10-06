package com.crazylegend.setofusefulkotlinextensions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.crazylegend.kotlinextensions.coroutines.makeApiCallAsync
import com.crazylegend.kotlinextensions.handlers.ViewState
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

    val viewStateResult = postsData.hookViewStateResult()


    private val retrofit by lazy {
        RetrofitClient.moshiInstanceCoroutines(application, TestApi.API).create<TestApi>()
    }


    init {
        makeApiCallAsync(postsData) {
            retrofit?.getPosts()
        }
    }

    fun <T> MutableLiveData<RetrofitResult<T>>.hookViewStateResult(): LiveData<ViewState>? {

        return Transformations.switchMap(this) {
            doTransformations(it)
        }
        /* viewStateResult.value = ViewState.InitialState(initialVisibility, initialText)
         onLoading {
             viewStateResult.value = ViewState.Loading
         }
         onApiError { _, _ ->
             viewStateResult.value = ViewState.Failure(failureVisibility, failureText)
         }
         onCallError {
             viewStateResult.value = ViewState.Failure(failureVisibility, failureText)
         }
         onSuccess {
             viewStateResult.value = ViewState.Success(successVisibility, successText)
         }
         onEmptyData {
             viewStateResult.value = ViewState.Failure(failureVisibility, failureText)
         }*/
    }

    private fun <T> doTransformations(retrofitResult: RetrofitResult<T>?): LiveData<ViewState> {

        return when (retrofitResult) {
            is RetrofitResult.Success -> {
                liveDataOf {
                    ViewState.Success()
                }
            }
            RetrofitResult.Loading -> {

                liveDataOf {
                    ViewState.Loading
                }
            }
            RetrofitResult.EmptyData -> {
                liveDataOf {
                    ViewState.Failure()
                }
            }
            is RetrofitResult.Error -> {
                liveDataOf {
                    ViewState.Failure()
                }
            }
            is RetrofitResult.ApiError -> {
                liveDataOf {
                    ViewState.Failure()
                }
            }
            null -> {
                liveDataOf {
                    ViewState.InitialState()
                }
            }
        }
    }
}



