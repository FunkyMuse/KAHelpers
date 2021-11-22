package com.crazylegend.retrofit.viewstate

import androidx.lifecycle.SavedStateHandle
import com.crazylegend.retrofit.retrofitResult.RetrofitResult
import com.crazylegend.retrofit.retrofitResult.onSuccess
import okhttp3.ResponseBody

/**
 * Created by funkymuse on 11/20/21 to long live and prosper !
 */


fun <T> RetrofitResult<T>.asViewStatePayload(viewState: ViewStateContract<T>): RetrofitResult<T> {
    onSuccess {
        viewState.payload = it
    }
    return this
}


fun <T> ViewStateContract<T>.fromRetrofit(retrofitResult: RetrofitResult<T>): ViewStateContract<T> {
    retrofitResult.onSuccess {
        payload = it
    }
    return this
}

suspend fun <T> RetrofitResult<T>.asViewStatePayloadWithEvents(viewState: ViewStateContract<T>): RetrofitResult<T> {
    onSuccess {
        viewState.payload = it
    }
    viewState.emitEvent(this)
    return this
}


suspend fun <T> ViewStateContract<T>.fromRetrofitWithEvents(retrofitResult: RetrofitResult<T>): ViewStateContract<T> {
    retrofitResult.onSuccess {
        payload = it
    }
    emitEvent(retrofitResult)
    return this
}


private const val errorStateKey = "errorJSONKeyRetrofitResult"

fun SavedStateHandle.handleApiErrorFromSavedState(errorBody: ResponseBody?): String? {
    val json = errorBody?.string()
    if (json.isNullOrEmpty()) return get<String>(errorStateKey)
    this[errorStateKey] = json

    return get<String>(errorStateKey)
}

fun handleApiError(savedStateHandle: SavedStateHandle, errorBody: ResponseBody?): String? = savedStateHandle.handleApiErrorFromSavedState(errorBody)
