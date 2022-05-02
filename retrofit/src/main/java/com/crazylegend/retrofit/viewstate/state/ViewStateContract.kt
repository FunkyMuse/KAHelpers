package com.crazylegend.retrofit.viewstate.state

import com.crazylegend.retrofit.apiresult.ApiResult
import com.crazylegend.retrofit.viewstate.event.ViewStatefulEvent
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by funkymuse on 11/20/21 to long live and prosper !
 */
interface ViewStateContract<T> {
    val viewState : StateFlow<ViewStatefulEvent>

    var payload: T?

    fun refreshPayload() {
        payload = null
    }

    suspend fun emitEvent(apiResult: ApiResult<T>)
    fun emitState(apiResult: ApiResult<T>)

    val isDataLoaded get() = payload != null

    val isDataNotLoaded get() = !isDataLoaded
}