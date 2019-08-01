package com.crazylegend.kotlinextensions.paging

import okhttp3.ResponseBody


/**
 * Created by hristijan on 8/1/19 to long live and prosper !
 */
sealed class PagingStateResult<out T> {
    object Loading : PagingStateResult<Nothing>()
    object CantLoadMore : PagingStateResult<Nothing>()
    object PagingSuccess : PagingStateResult<Nothing>()
    object EmptyData : PagingStateResult<Nothing>()
    data class Error(val message: String, val exception: Exception?=null, val throwable: Throwable) : PagingStateResult<Nothing>()
    data class ApiError(val responseCode: Int, val errorBody: ResponseBody?) : PagingStateResult<Nothing>()
}