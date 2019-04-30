package com.crazylegend.kotlinextensions.database

import com.crazylegend.kotlinextensions.exhaustive


/**
 * Created by hristijan on 4/10/19 to long live and prosper !
 */

sealed class DBResult<out T> {

    data class Success<T>(val value: T) : DBResult<T>()
    object Quering : DBResult<Nothing>()
    object EmptyDB : DBResult<Nothing>()
    data class DBError(val message: String, val exception: Exception?=null, val throwable: Throwable) : DBResult<Nothing>()

}


fun <T> DBResult<T>.handle(queryingDB: () -> Unit,
                           emptyDB: () -> Unit,
                           dbError: (message: String, throwable: Throwable, exception: java.lang.Exception?) -> Unit = { _, _, _ -> },
                           success: T.() -> Unit) {
    when (this) {
        is DBResult.Success -> {
            success.invoke(value)
        }
        DBResult.Quering -> {
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

