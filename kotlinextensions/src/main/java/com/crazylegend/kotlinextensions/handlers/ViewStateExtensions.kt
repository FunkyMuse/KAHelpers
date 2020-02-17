package com.crazylegend.kotlinextensions.handlers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.crazylegend.kotlinextensions.livedata.liveDataOf
import com.crazylegend.kotlinextensions.retrofit.RetrofitResult


/**
 * Created by hristijan on 10/7/19 to long live and prosper !
 * Used to hook onto the retrofit result and observe its changes
 */


fun ViewState.onInitialState(callback: (isVisible: Boolean, text: String) -> Unit = { _, _ -> }) {
    if (this is ViewState.InitialState) {
        callback(isVisible, text)
    }
}


fun ViewState.onLoading(callback: (isVisible: Boolean, text: String) -> Unit = { _, _ -> }) {
    if (this is ViewState.Loading) {
        callback(isVisible, text)
    }
}


fun ViewState.onFailure(callback: (isVisible: Boolean, text: String) -> Unit = { _, _ -> }) {
    if (this is ViewState.Failure) {
        callback(isVisible, text)
    }
}


fun ViewState.onSuccess(callback: (isVisible: Boolean, text: String) -> Unit = { _, _ -> }) {
    if (this is ViewState.Success) {
        callback(isVisible, text)
    }
}

/**
 * Usage
 *
 *
it.handle({ //initial state
isVisible, text ->
}, {//loading
isVisible, text ->
}, { //success
isVisible, text ->
}, {//failure
isVisible, text ->

})
 *
 *
 * @receiver ViewState
 * @param initialState Function2<[@kotlin.ParameterName] Boolean, [@kotlin.ParameterName] String, Unit>
 * @param loading Function2<[@kotlin.ParameterName] Boolean, [@kotlin.ParameterName] String, Unit>
 * @param success Function2<[@kotlin.ParameterName] Boolean, [@kotlin.ParameterName] String, Unit>
 * @param failure Function2<[@kotlin.ParameterName] Boolean, [@kotlin.ParameterName] String, Unit>
 */

fun ViewState.handle(initialState: (isVisible: Boolean, text: String) -> Unit,
                     loading: (isVisible: Boolean, text: String) -> Unit,
                     success: (isVisible: Boolean, text: String) -> Unit,
                     failure: (isVisible: Boolean, text: String) -> Unit) {
    when (this) {
        is ViewState.InitialState -> {
            initialState.invoke(isVisible, text)
        }
        is ViewState.Loading -> {
            loading.invoke(isVisible, text)

        }
        is ViewState.Success -> {
            success.invoke(isVisible, text)
        }
        is ViewState.Failure -> {
            failure.invoke(isVisible, text)
        }
    }
}


fun <T> MutableLiveData<RetrofitResult<T>>.hookViewStateResult(
        loadingText: String, loadingVisibility: Boolean,
        failureText: String, failureVisibility: Boolean,
        successText: String, successVisibility: Boolean
): LiveData<ViewState>? {

    return Transformations.switchMap(this) {
        doTransformations(loadingText, loadingVisibility, failureText, failureVisibility, successText, successVisibility, it)
    }
}


fun <T> LiveData<RetrofitResult<T>>.hookViewStateResult(
        loadingText: String, loadingVisibility: Boolean,
        failureText: String, failureVisibility: Boolean,
        successText: String, successVisibility: Boolean
): LiveData<ViewState>? {

    return Transformations.switchMap(this) {
        doTransformations(loadingText, loadingVisibility, failureText, failureVisibility, successText, successVisibility, it)
    }
}


private fun <T> doTransformations(
        loadingText: String, loadingVisibility: Boolean,
        failureText: String, failureVisibility: Boolean,
        successText: String, successVisibility: Boolean, retrofitResult: RetrofitResult<T>?): LiveData<ViewState> {

    return when (retrofitResult) {
        is RetrofitResult.Success -> {
            liveDataOf {
                ViewState.Success(successVisibility, successText)
            }
        }
        RetrofitResult.Loading -> {

            liveDataOf {
                ViewState.Loading(loadingVisibility, loadingText)
            }
        }
        RetrofitResult.EmptyData -> {
            liveDataOf {
                ViewState.Failure(failureVisibility, failureText)
            }
        }
        is RetrofitResult.Error -> {
            liveDataOf {
                ViewState.Failure(failureVisibility, failureText)
            }
        }
        is RetrofitResult.ApiError -> {
            liveDataOf {
                ViewState.Failure(failureVisibility, failureText)
            }
        }
        null -> {
            liveDataOf {
                ViewState.InitialState()
            }
        }
    }
}