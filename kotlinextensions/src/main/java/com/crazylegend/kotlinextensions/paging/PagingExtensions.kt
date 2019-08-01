package com.crazylegend.kotlinextensions.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crazylegend.kotlinextensions.exhaustive
import okhttp3.ResponseBody
import retrofit2.Response


/**
 * Created by hristijan on 8/1/19 to long live and prosper !
 */

fun <T> PagingStateResult<T>.handle(
        loading: () -> Unit,
        cantLoadMore: () -> Unit,
        emptyData: () -> Unit,
        calError: (message: String, throwable: Throwable, exception: Exception?) -> Unit = { _, _, _ -> },
        apiError: (errorBody: ResponseBody?, responseCode: Int) -> Unit = { _, _ -> },
        success: () -> Unit
) {

    when (this) {
        PagingStateResult.Loading -> {
            loading()
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
            calError(message, throwable, exception)
        }
        is PagingStateResult.ApiError -> {
            apiError(errorBody, responseCode)
        }
    }.exhaustive
}


fun <T> MutableLiveData<PagingStateResult<T>>.getSuccess(action: () -> Unit = {}) {
    value?.let {
        when (it) {
            is PagingStateResult.PagingSuccess -> {
                action()
            }
            else -> { }
        }
    }
}

fun <T> LiveData<PagingStateResult<T>>.getSuccess(action: () -> Unit = { }) {
    value?.let {
        when (it) {
            is PagingStateResult.PagingSuccess -> {
                action()
            }
            else -> { }
        }
    }
}


fun <T> MutableLiveData<PagingStateResult<T>>.onCantLoadMore(action: () -> Unit = {}) {
    value?.let {
        when (it) {
            is PagingStateResult.CantLoadMore -> {
                action()
            }
            else -> {
            }
        }
    }
}

fun <T> LiveData<PagingStateResult<T>>.onCantLoadMore(action: () -> Unit = {}) {
    value?.let {
        when (it) {
            is PagingStateResult.CantLoadMore -> {
                action()
            }
            else -> {
            }
        }
    }
}

fun <T> MutableLiveData<PagingStateResult<T>>.callError(throwable: Throwable) {
    value = PagingStateResult.Error(throwable.message.toString(), java.lang.Exception(throwable), throwable)
}

fun <T> MutableLiveData<PagingStateResult<T>>.callErrorPost(throwable: Throwable) {
    postValue(PagingStateResult.Error(throwable.message.toString(), java.lang.Exception(throwable), throwable))
}

fun <T> MutableLiveData<PagingStateResult<T>>.success() {
    value = PagingStateResult.PagingSuccess
}

fun <T> MutableLiveData<PagingStateResult<T>>.successPost() {
    postValue(PagingStateResult.PagingSuccess)
}

fun <T> MutableLiveData<PagingStateResult<T>>.apiError(code: Int, errorBody: ResponseBody?) {
    value = PagingStateResult.ApiError(code, errorBody)
}


fun <T> MutableLiveData<PagingStateResult<T>>.apiErrorPost(code: Int, errorBody: ResponseBody?) {
    postValue(PagingStateResult.ApiError(code, errorBody))
}


fun <T> MutableLiveData<PagingStateResult<T>>.loading() {
    value = PagingStateResult.Loading
}

fun <T> MutableLiveData<PagingStateResult<T>>.emptyData() {
    value = PagingStateResult.EmptyData
}

fun <T> MutableLiveData<PagingStateResult<T>>.loadingPost() {
    postValue(PagingStateResult.Loading)
}

fun <T> MutableLiveData<PagingStateResult<T>>.emptyDataPost() {
    postValue(PagingStateResult.EmptyData)
}


fun <T> MutableLiveData<PagingStateResult<T>>.cantLoadMorePost() {
    postValue(PagingStateResult.CantLoadMore)
}

fun <T> MutableLiveData<PagingStateResult<T>>.cantLoadMore() {
    value = PagingStateResult.CantLoadMore
}
fun <T> MutableLiveData<PagingStateResult<T>>.subscribe(response: Response<T>?, includeEmptyData: Boolean = false) {
    response?.let { serverResponse ->
        if (serverResponse.isSuccessful) {
            serverResponse.body()?.apply {
                if (includeEmptyData) {
                    if (this == null) {
                        value = PagingStateResult.EmptyData
                    } else {
                        value = PagingStateResult.PagingSuccess
                    }
                } else {
                    value = PagingStateResult.PagingSuccess
                }
            }
        } else {
            value = PagingStateResult.ApiError(serverResponse.code(), serverResponse.errorBody())
        }
    }
}

fun <T> MutableLiveData<PagingStateResult<T>>.subscribePost(response: Response<T>?, includeEmptyData: Boolean = false) {
    response?.let { serverResponse ->
        if (serverResponse.isSuccessful) {
            serverResponse.body()?.apply {
                if (includeEmptyData) {
                    if (this == null) {
                        postValue(PagingStateResult.EmptyData)
                    } else {
                        postValue(PagingStateResult.PagingSuccess)
                    }
                } else {
                    postValue(PagingStateResult.PagingSuccess)
                }
            }
        } else {
            postValue(PagingStateResult.ApiError(serverResponse.code(), serverResponse.errorBody()))
        }
    }
}


fun <T> MutableLiveData<PagingStateResult<T>>.subscribeList(response: Response<T>?, includeEmptyData: Boolean = false) {
    response?.let { serverResponse ->
        if (serverResponse.isSuccessful) {
            serverResponse.body()?.apply {
                if (includeEmptyData) {
                    if (this == null) {
                        value = PagingStateResult.EmptyData
                    } else {
                        if (this is List<*>) {
                            val list = this as List<*>
                            if (list.isNullOrEmpty()) {
                                value = PagingStateResult.EmptyData
                            } else {
                                value = PagingStateResult.PagingSuccess
                            }
                        } else {
                            value = PagingStateResult.PagingSuccess
                        }
                    }
                } else {
                    value = PagingStateResult.PagingSuccess
                }
            }
        } else {
            value = PagingStateResult.ApiError(serverResponse.code(), serverResponse.errorBody())
        }
    }

}

fun <T> MutableLiveData<PagingStateResult<T>>.subscribeListPost(response: Response<T>?, includeEmptyData: Boolean = false) {
    response?.let { serverResponse ->
        if (serverResponse.isSuccessful) {
            serverResponse.body()?.apply {
                if (includeEmptyData) {
                    if (this == null) {
                        postValue(PagingStateResult.EmptyData)
                    } else {
                        if (this is List<*>) {
                            val list = this as List<*>
                            if (list.isNullOrEmpty()) {
                                postValue(PagingStateResult.EmptyData)
                            } else {
                                postValue(PagingStateResult.PagingSuccess)
                            }
                        } else {
                            postValue(PagingStateResult.PagingSuccess)
                        }
                    }
                } else {
                    postValue(PagingStateResult.PagingSuccess)
                }
            }
        } else {
            postValue(PagingStateResult.ApiError(serverResponse.code(), serverResponse.errorBody()))
        }
    }
}


