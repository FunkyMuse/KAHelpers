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

fun <T> MutableLiveData<DBResult<T>>.emptyData() {
    value = DBResult.EmptyDB
}


fun <T> MutableLiveData<DBResult<T>>.subscribe(response: T?, includeEmptyData: Boolean = false) {
    if (includeEmptyData){
        if (response == null){
            value = DBResult.EmptyDB
        } else {
            value = DBResult.Success(response)
        }
    } else {
        response?.apply {
            value = DBResult.Success(this)
        }
    }
}

fun <T> MutableLiveData<DBResult<T>>.callError(throwable: Throwable) {
    value = DBResult.DBError(throwable.message.toString(), Exception(throwable), throwable)
}

fun <T> MutableLiveData<DBResult<T>>.success(model:T) {
    value = DBResult.Success(model)
}

fun <T> MutableLiveData<DBResult<T>>.getSuccess(action: (T) -> Unit) {
    value?.let {
        when (it) {
            is DBResult.Success -> {
                action(it.value)
            }
            else -> {}
        }
    }
}

fun <T> LiveData<DBResult<T>>.getSuccess(action: (model: T) -> Unit = { _ -> }) {
    value?.let {
        when (it) {
            is DBResult.Success -> {
                action(it.value)
            }
            else -> {}
        }
    }
}