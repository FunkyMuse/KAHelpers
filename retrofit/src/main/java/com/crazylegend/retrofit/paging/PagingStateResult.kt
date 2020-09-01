package com.crazylegend.retrofit.paging

import okhttp3.ResponseBody


/**
 * Created by hristijan on 8/1/19 to long live and prosper !
 */
sealed class PagingStateResult {
    object Loading : PagingStateResult()
    object LoadingMore : PagingStateResult()
    object CantLoadMore : PagingStateResult()
    object PagingSuccess : PagingStateResult()
    object EmptyData : PagingStateResult()
    data class Error(val throwable: Throwable) : PagingStateResult()
    data class ApiError(val responseCode: Int, val errorBody: ResponseBody?) : PagingStateResult()
}