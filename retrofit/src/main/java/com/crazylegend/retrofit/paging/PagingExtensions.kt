package com.crazylegend.retrofit.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crazylegend.retrofit.retrofitResult.RetrofitResult
import com.crazylegend.retrofit.retrofitResult.retrofitSubscribe
import com.crazylegend.retrofit.retrofitResult.retrofitSubscribeList
import okhttp3.ResponseBody
import retrofit2.Response


/**
 * Created by hristijan on 8/1/19 to long live and prosper !
 */

fun PagingStateResult.handle(
        loading: () -> Unit,
        loadingMore: () -> Unit,
        cantLoadMore: () -> Unit,
        emptyData: () -> Unit,
        callError: (throwable: Throwable) -> Unit = { _ -> },
        apiError: (errorBody: ResponseBody?, responseCode: Int) -> Unit = { _, _ -> },
        success: () -> Unit
) {

    when (this) {
        PagingStateResult.Loading -> {
            loading()
        }
        PagingStateResult.LoadingMore -> {
            loadingMore()
        }
        PagingStateResult.CantLoadMore -> {
            cantLoadMore()
        }
        PagingStateResult.PagingSuccess -> {
            success()
        }
        PagingStateResult.EmptyData -> {
            emptyData()
        }
        is PagingStateResult.Error -> {
            callError(throwable)
        }
        is PagingStateResult.ApiError -> {
            apiError(errorBody, responseCode)
        }
    }
}


fun MutableLiveData<PagingStateResult>.getSuccess(action: () -> Unit = {}) {
    value?.let {
        when (it) {
            is PagingStateResult.PagingSuccess -> {
                action()
            }
        }
    }
}

fun LiveData<PagingStateResult>.getSuccess(action: () -> Unit = { }) {
    value?.let {
        when (it) {
            is PagingStateResult.PagingSuccess -> {
                action()
            }
        }
    }
}


fun MutableLiveData<PagingStateResult>.onCantLoadMore(action: () -> Unit = {}) {
    value?.let {
        when (it) {
            is PagingStateResult.CantLoadMore -> {
                action()
            }
        }
    }
}

fun LiveData<PagingStateResult>.onCantLoadMore(action: () -> Unit = {}) {
    value?.let {
        when (it) {
            is PagingStateResult.CantLoadMore -> {
                action()
            }
        }
    }
}

fun MutableLiveData<PagingStateResult>.callError(throwable: Throwable) {
    value = PagingStateResult.Error(throwable)
}

fun MutableLiveData<PagingStateResult>.callErrorPost(throwable: Throwable) {
    postValue(PagingStateResult.Error(throwable))
}

fun MutableLiveData<PagingStateResult>.success() {
    value = PagingStateResult.PagingSuccess
}

fun MutableLiveData<PagingStateResult>.successPost() {
    postValue(PagingStateResult.PagingSuccess)
}

fun MutableLiveData<PagingStateResult>.apiError(code: Int, errorBody: ResponseBody?) {
    value = PagingStateResult.ApiError(code, errorBody)
}


fun MutableLiveData<PagingStateResult>.apiErrorPost(code: Int, errorBody: ResponseBody?) {
    postValue(PagingStateResult.ApiError(code, errorBody))
}


fun MutableLiveData<PagingStateResult>.loading() {
    value = PagingStateResult.Loading
}

fun MutableLiveData<PagingStateResult>.loadingMore() {
    value = PagingStateResult.LoadingMore
}

fun MutableLiveData<PagingStateResult>.emptyData() {
    value = PagingStateResult.EmptyData
}

fun MutableLiveData<PagingStateResult>.loadingPost() {
    postValue(PagingStateResult.Loading)
}

fun MutableLiveData<PagingStateResult>.loadingMorePost() {
    postValue(PagingStateResult.LoadingMore)
}

fun MutableLiveData<PagingStateResult>.emptyDataPost() {
    postValue(PagingStateResult.EmptyData)
}


fun MutableLiveData<PagingStateResult>.cantLoadMorePost() {
    postValue(PagingStateResult.CantLoadMore)
}

fun MutableLiveData<PagingStateResult>.cantLoadMore() {
    value = PagingStateResult.CantLoadMore
}

fun <T> MutableLiveData<PagingStateResult>.subscribe(response: Response<T>?) {
    value = retrofitSubscribe(response).toPaging()
}

fun <T> MutableLiveData<PagingStateResult>.subscribePost(response: Response<T>?) {
    postValue(retrofitSubscribe(response).toPaging())
}


fun <T> MutableLiveData<PagingStateResult>.subscribeList(response: Response<T>?, includeEmptyData: Boolean = false) {
    value = retrofitSubscribeList(response, includeEmptyData).toPaging()
}

private fun <T> RetrofitResult<T>.toPaging() =
        when (this) {
            is RetrofitResult.Success -> PagingStateResult.PagingSuccess
            RetrofitResult.Loading -> PagingStateResult.Loading
            RetrofitResult.EmptyData -> PagingStateResult.CantLoadMore
            is RetrofitResult.Error -> PagingStateResult.Error(throwable)
            is RetrofitResult.ApiError -> PagingStateResult.ApiError(responseCode, errorBody)
        }


fun <T> MutableLiveData<PagingStateResult>.subscribeListPost(response: Response<T>?, includeEmptyData: Boolean = false) {
    postValue(retrofitSubscribeList(response, includeEmptyData).toPaging())
}


fun PagingStateResult.onLoading(function: () -> Unit = {}) {
    if (this is PagingStateResult.Loading) function()
}

fun PagingStateResult.onLoadingMore(function: () -> Unit = {}) {
    if (this is PagingStateResult.LoadingMore) function()
}


fun PagingStateResult.onEmptyData(function: () -> Unit = {}) {
    if (this is PagingStateResult.EmptyData) function()
}

fun PagingStateResult.onCallError(function: (throwable: Throwable) -> Unit = { _ -> }) {
    if (this is PagingStateResult.Error) {
        function(throwable)
    }
}

fun PagingStateResult.onApiError(function: (errorBody: ResponseBody?, responseCode: Int) -> Unit = { _, _ -> }) {
    if (this is PagingStateResult.ApiError) {
        function(errorBody, responseCode)
    }
}

fun PagingStateResult.onSuccess(function: () -> Unit = {}) {
    if (this is PagingStateResult.PagingSuccess) function()

}

fun PagingStateResult.onCantLoadMore(function: () -> Unit = {}) {
    if (this is PagingStateResult.CantLoadMore) function()

}