package com.crazylegend.kotlinextensions.handlers

/**
 * Created by hristijan on 4/1/19 to long live and prosper !
 */
sealed class BasicResult<out T> {
    data class Success<T>(val value: T) : BasicResult<T>()
    data class Error(val message: String, val exception: Exception?=null, val throwable: Throwable) : BasicResult<Nothing>()
    object NoData : BasicResult<Nothing>()
}