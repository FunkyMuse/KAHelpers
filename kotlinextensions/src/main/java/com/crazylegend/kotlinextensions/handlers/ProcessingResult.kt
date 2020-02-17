package com.crazylegend.kotlinextensions.handlers


/**
 * Created by hristijan on 4/1/19 to long live and prosper !
 */
sealed class ProcessingResult<out T> {
    data class Success<T>(val value: T) : ProcessingResult<T>()
    object Processing : ProcessingResult<Nothing>()
    data class Error(val throwable: Throwable) : ProcessingResult<Nothing>()
    object Finished : ProcessingResult<Nothing>()
}