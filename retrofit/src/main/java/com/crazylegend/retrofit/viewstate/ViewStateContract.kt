package com.crazylegend.retrofit.viewstate

import com.crazylegend.retrofit.retrofitResult.RetrofitResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by funkymuse on 11/20/21 to long live and prosper !
 */
interface ViewStateContract<T> {
    val viewEvent: Flow<ViewEvent>
    val data : StateFlow<RetrofitResult<T>>

    var payload: T?

    fun refreshPayload() {
        payload = null
    }

    suspend fun emitEvent(retrofitResult: RetrofitResult<T>)

    val isDataLoaded get() = payload != null

    val isDataNotLoaded get() = !isDataLoaded
}