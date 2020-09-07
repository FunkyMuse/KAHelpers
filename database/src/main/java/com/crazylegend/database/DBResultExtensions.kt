package com.crazylegend.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


/**
 * Created by hristijan on 7/18/19 to long live and prosper !
 */

fun databaseError(throwable: Throwable) = DBResult.DBError(throwable)
fun <T> databaseSuccess(value: T) = DBResult.Success(value)
val databaseQuerying get() = DBResult.Querying
val databaseEmptyDB get() = DBResult.EmptyDB

fun <T> databaseSubscribe(response: T?): DBResult<T> = if (response == null) databaseEmptyDB else databaseSuccess<T>(response)


fun <T> databaseSubscribeList(response: T?, includeEmptyData: Boolean = false): DBResult<T> {
    when {
        response == null -> {
            return databaseEmptyDB
        }
        includeEmptyData -> {
            return if (includeEmptyData) {
                return response.isListAndNotNullOrEmpty<T?, DBResult<T>>(actionFalse = {
                    databaseEmptyDB
                }, actionTrue = {
                    databaseSubscribe<T>(response)
                })
            } else {
                databaseSubscribe<T>(response)
            }
        }
        else -> {
            return databaseSubscribe(response)
        }
    }
}


inline fun <T> DBResult<T>.handle(queryingDB: () -> Unit,
                                  emptyDB: () -> Unit,
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


fun <T> MutableLiveData<DBResult<T>>.subscribe(queryModel: T?, includeEmptyData: Boolean = false) {
    if (includeEmptyData) {
        if (queryModel == null) {
            value = databaseEmptyDB
        } else {
            value = databaseSuccess(queryModel)
        }
    } else {
        queryModel?.apply {
            value = databaseSuccess(this)
        }
    }
}


fun <T> MutableLiveData<DBResult<T>>.subscribePost(queryModel: T?, includeEmptyData: Boolean = false) {
    if (includeEmptyData) {
        if (queryModel == null) {
            postValue(databaseEmptyDB)
        } else {
            postValue(databaseSuccess(queryModel))
        }
    } else {
        queryModel?.apply {
            postValue(databaseSuccess(this))
        }
    }
}


fun <T> MutableLiveData<DBResult<T>>.subscribeList(queryModel: T?, includeEmptyData: Boolean = false) {
    if (includeEmptyData) {
        if (queryModel == null) {
            value = databaseEmptyDB
        } else {
            if (this is List<*>) {
                val list = this as List<*>
                if (list.isNullOrEmpty()) {
                    value = databaseEmptyDB
                } else {
                    value = databaseSuccess(queryModel)
                }
            } else {
                value = databaseSuccess(queryModel)
            }
        }
    } else {
        queryModel?.apply {
            value = databaseSuccess(this)
        }
    }
}


fun <T> MutableLiveData<DBResult<T>>.subscribeListPost(queryModel: T?, includeEmptyData: Boolean = false) {
    if (includeEmptyData) {
        if (queryModel == null) {
            postValue(databaseEmptyDB)
        } else {
            if (this is List<*>) {
                val list = this as List<*>
                if (list.isNullOrEmpty()) {
                    postValue(databaseEmptyDB)
                } else {
                    postValue(databaseSuccess(queryModel))
                }
            } else {
                postValue(databaseSuccess(queryModel))
            }
        }
    } else {
        queryModel?.apply {
            postValue(databaseSuccess(this))
        }
    }
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
            else -> {
            }
        }
    }
}

inline fun <T> LiveData<DBResult<T>>.onSuccess(action: (model: T) -> Unit = { _ -> }) {
    value?.let {
        when (it) {
            is DBResult.Success -> {
                action(it.value)
            }
            else -> {
            }
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