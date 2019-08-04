package com.crazylegend.kotlinextensions.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crazylegend.kotlinextensions.exhaustive
import okhttp3.ResponseBody
import retrofit2.Response


/**
 * Created by hristijan on 8/1/19 to long live and prosper !
 */

fun PagingStateResult.handle(
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


fun MutableLiveData<PagingStateResult>.getSuccess(action: () -> Unit = {}) {
    value?.let {
        when (it) {
            is PagingStateResult.PagingSuccess -> {
                action()
            }
            else -> {
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
            else -> {
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
            else -> {
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
            else -> {
            }
        }
    }
}

fun MutableLiveData<PagingStateResult>.callError(throwable: Throwable) {
    value = PagingStateResult.Error(throwable.message.toString(), java.lang.Exception(throwable), throwable)
}

fun MutableLiveData<PagingStateResult>.callErrorPost(throwable: Throwable) {
    postValue(PagingStateResult.Error(throwable.message.toString(), java.lang.Exception(throwable), throwable))
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

fun MutableLiveData<PagingStateResult>.emptyData() {
    value = PagingStateResult.EmptyData
}

fun MutableLiveData<PagingStateResult>.loadingPost() {
    postValue(PagingStateResult.Loading)
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

fun <T> MutableLiveData<PagingStateResult>.subscribe(response: Response<T>?, includeEmptyData: Boolean = false) {
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

fun <T> MutableLiveData<PagingStateResult>.subscribePost(response: Response<T>?, includeEmptyData: Boolean = false) {
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


fun <T> MutableLiveData<PagingStateResult>.subscribeList(response: Response<T>?, includeEmptyData: Boolean = false) {
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

fun <T> MutableLiveData<PagingStateResult>.subscribeListPost(response: Response<T>?, includeEmptyData: Boolean = false) {
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


