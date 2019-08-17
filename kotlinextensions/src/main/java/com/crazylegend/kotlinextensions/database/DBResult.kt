package com.crazylegend.kotlinextensions.database


/**
 * Created by hristijan on 4/10/19 to long live and prosper !
 */

sealed class DBResult<out T> {

    data class Success<T>(val value: T) : DBResult<T>()
    object Querying : DBResult<Nothing>()
    object EmptyDB : DBResult<Nothing>()
    data class DBError(val throwable: Throwable) : DBResult<Nothing>()

}

