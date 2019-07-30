package com.crazylegend.kotlinextensions.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crazylegend.kotlinextensions.exhaustive


/**
 * Created by hristijan on 7/18/19 to long live and prosper !
 */


fun <T> DBResult<T>.handle(queryingDB: () -> Unit,
                           emptyDB: () -> Unit,
                           dbError: (message: String, throwable: Throwable, exception: java.lang.Exception?) -> Unit = { _, _, _ -> },
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
            dbError(message, throwable, exception)
        }
    }.exhaustive
}


fun <T> MutableLiveData<DBResult<T>>.querying() {
    value = DBResult.Querying
}

fun <T> MutableLiveData<DBResult<T>>.queryingPost() {
    postValue(DBResult.Querying)
}

fun <T> MutableLiveData<DBResult<T>>.emptyData() {
    value = DBResult.EmptyDB
}

fun <T> MutableLiveData<DBResult<T>>.emptyDataPost() {
    postValue(DBResult.EmptyDB)
}


fun <T> MutableLiveData<DBResult<T>>.subscribe(queryModel: T?, includeEmptyData: Boolean = false) {
    if (includeEmptyData) {
        if (queryModel == null) {
            value = DBResult.EmptyDB
        } else {
            value = DBResult.Success(queryModel)
        }
    } else {
        queryModel?.apply {
            value = DBResult.Success(this)
        }
    }
}


fun <T> MutableLiveData<DBResult<T>>.subscribePost(queryModel: T?, includeEmptyData: Boolean = false) {
    if (includeEmptyData) {
        if (queryModel == null) {
            postValue(DBResult.EmptyDB)
        } else {
            postValue(DBResult.Success(queryModel))
        }
    } else {
        queryModel?.apply {
            postValue(DBResult.Success(this))
        }
    }
}


fun <T> MutableLiveData<DBResult<T>>.subscribeList(queryModel: T?, includeEmptyData: Boolean = false) {
    if (includeEmptyData) {
        if (queryModel == null) {
            value = DBResult.EmptyDB
        } else {
            if (this is List<*>) {
                val list = this as List<*>
                if (list.isNullOrEmpty()) {
                    value = DBResult.EmptyDB
                } else {
                    value = DBResult.Success(queryModel)
                }
            } else {
                value = DBResult.Success(queryModel)
            }
        }
    } else {
        queryModel?.apply {
            value = DBResult.Success(this)
        }
    }
}


fun <T> MutableLiveData<DBResult<T>>.subscribeListPost(queryModel: T?, includeEmptyData: Boolean = false) {
    if (includeEmptyData) {
        if (queryModel == null) {
            postValue(DBResult.EmptyDB)
        } else {
            if (this is List<*>) {
                val list = this as List<*>
                if (list.isNullOrEmpty()) {
                    postValue(DBResult.EmptyDB)
                } else {
                    postValue(DBResult.Success(queryModel))
                }
            } else {
                postValue(DBResult.Success(queryModel))
            }
        }
    } else {
        queryModel?.apply {
            postValue(DBResult.Success(this))
        }
    }
}

fun <T> MutableLiveData<DBResult<T>>.callError(throwable: Throwable) {
    value = DBResult.DBError(throwable.message.toString(), Exception(throwable), throwable)
}

fun <T> MutableLiveData<DBResult<T>>.callErrorPost(throwable: Throwable) {
    postValue(DBResult.DBError(throwable.message.toString(), Exception(throwable), throwable))
}


fun <T> MutableLiveData<DBResult<T>>.success(model: T) {
    value = DBResult.Success(model)
}

fun <T> MutableLiveData<DBResult<T>>.successPost(model: T) {
    postValue(DBResult.Success(model))
}

fun <T> MutableLiveData<DBResult<T>>.getSuccess(action: (T) -> Unit) {
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

fun <T> LiveData<DBResult<T>>.getSuccess(action: (model: T) -> Unit = { _ -> }) {
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