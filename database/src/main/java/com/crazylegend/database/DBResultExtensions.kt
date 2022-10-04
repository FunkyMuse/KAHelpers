package com.crazylegend.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow


/**
 * Created by hristijan on 7/18/19 to long live and prosper !
 */

fun databaseError(throwable: Throwable) = DBResult.DBError(throwable)
fun <T> databaseSuccess(value: T) = DBResult.Success(value)
val databaseQuerying get() = DBResult.Querying
val databaseEmptyDB get() = DBResult.EmptyDB

fun <T> databaseSubscribe(response: T?): DBResult<T> = if (response == null) databaseEmptyDB else databaseSuccess<T>(response)


fun <T> databaseSubscribeList(response: T?, includeEmptyData: Boolean = true): DBResult<T> {
    return when {
        response == null -> {
            databaseEmptyDB
        }
        includeEmptyData -> {
            response.isListAndIsNullOrEmpty<T?, DBResult<T>>(actionFalse = {
                databaseSubscribe<T>(response)
            }, actionTrue = {
                databaseEmptyDB
            })
        }
        else -> {
            databaseSubscribe(response)
        }
    }
}


inline fun <T> DBResult<T>.handle(queryingDB: () -> Unit = {},
                                  emptyDB: () -> Unit = {},
                                  dbError: (throwable: Throwable) -> Unit = { _ -> },
                                  success: T.() -> Unit) {
    when (this) {
        is DBResult.Success -> {
            success.invoke(value)
        }
        DBResult.Querying -> {
            queryingDB()
        }
        DBResult.EmptyDB -> {
            emptyDB()
        }
        is DBResult.DBError -> {
            dbError(throwable)
        }
    }
}


fun <T> MutableLiveData<DBResult<T>>.querying() {
    value = databaseQuerying
}

fun <T> MutableLiveData<DBResult<T>>.queryingPost() {
    postValue(databaseQuerying)
}

fun <T> MutableLiveData<DBResult<T>>.emptyData() {
    value = databaseEmptyDB
}

fun <T> MutableLiveData<DBResult<T>>.emptyDataPost() {
    postValue(databaseEmptyDB)
}

fun <T> MutableLiveData<DBResult<T>>.subscribe(queryModel: T?) {
    value = databaseSubscribe(queryModel)
}


fun <T> MutableLiveData<DBResult<T>>.subscribePost(queryModel: T?) {
    postValue(databaseSubscribe(queryModel))
}


fun <T> MutableLiveData<DBResult<T>>.subscribeList(queryModel: T?, includeEmptyData: Boolean = false) {
    value = databaseSubscribeList(queryModel, includeEmptyData)
}


fun <T> MutableLiveData<DBResult<T>>.subscribeListPost(queryModel: T?, includeEmptyData: Boolean = false) {
    postValue(databaseSubscribeList(queryModel, includeEmptyData))
}

fun <T> MutableLiveData<DBResult<T>>.callError(throwable: Throwable) {
    value = databaseError(throwable)
}

fun <T> MutableLiveData<DBResult<T>>.callErrorPost(throwable: Throwable) {
    postValue(databaseError(throwable))
}


fun <T> MutableLiveData<DBResult<T>>.success(model: T) {
    value = databaseSuccess(model)
}

fun <T> MutableLiveData<DBResult<T>>.successPost(model: T) {
    postValue(databaseSuccess(model))
}

inline fun <T> MutableLiveData<DBResult<T>>.onSuccess(action: (T) -> Unit) {
    value?.let {
        when (it) {
            is DBResult.Success -> {
                action(it.value)
            }

            else -> {}
        }
    }
}

inline fun <T> LiveData<DBResult<T>>.onSuccess(action: (model: T) -> Unit = { _ -> }) {
    value?.let {
        when (it) {
            is DBResult.Success -> {
                action(it.value)
            }

            else -> {}
        }
    }
}

inline val <T> MutableLiveData<DBResult<T>>.getSuccess: T?
    get() {
        return value?.let {
            when (it) {
                is DBResult.Success -> {
                    it.value
                }
                else -> {
                    null
                }
            }
        }
    }

inline val <T> LiveData<DBResult<T>>.getSuccess: T?
    get() {
        return value?.let {
            when (it) {
                is DBResult.Success -> {
                    it.value
                }
                else -> {
                    null
                }
            }
        }
    }

internal inline fun <T, R> T.isListAndNotNullOrEmpty(actionFalse: () -> R, actionTrue: () -> R): R =
        if (this is List<*> && !this.isNullOrEmpty()) actionTrue() else actionFalse()

internal inline fun <T, R> T.isListAndIsNullOrEmpty(actionFalse: () -> R, actionTrue: () -> R): R =
        if (this is List<*> && this.isNullOrEmpty()) actionTrue() else actionFalse()

fun <T> MutableStateFlow<DBResult<T>>.querying() {
    value = databaseQuerying
}


fun <T> MutableStateFlow<DBResult<T>>.emptyData() {
    value = databaseEmptyDB
}


fun <T> MutableStateFlow<DBResult<T>>.subscribe(queryModel: T?) {
    value = databaseSubscribe(queryModel)
}


fun <T> MutableStateFlow<DBResult<T>>.subscribeList(queryModel: T?, includeEmptyData: Boolean = false) {
    value = databaseSubscribeList(queryModel, includeEmptyData)
}


fun <T> MutableStateFlow<DBResult<T>>.callError(throwable: Throwable) {
    value = databaseError(throwable)
}

fun <T> MutableStateFlow<DBResult<T>>.success(model: T) {
    value = databaseSuccess(model)
}


suspend fun <T> MutableSharedFlow<DBResult<T>>.querying() {
    emit(databaseQuerying)
}


suspend fun <T> MutableSharedFlow<DBResult<T>>.emptyData() {
    emit(databaseEmptyDB)
}


suspend fun <T> MutableSharedFlow<DBResult<T>>.subscribe(queryModel: T?) {
    emit(databaseSubscribe(queryModel))
}


suspend fun <T> MutableSharedFlow<DBResult<T>>.subscribeList(queryModel: T?, includeEmptyData: Boolean = false) {
    emit(databaseSubscribeList(queryModel, includeEmptyData))
}


suspend fun <T> MutableSharedFlow<DBResult<T>>.callError(throwable: Throwable) {
    emit(databaseError(throwable))
}

suspend fun <T> MutableSharedFlow<DBResult<T>>.success(model: T) {
    emit(databaseSuccess(model))
}