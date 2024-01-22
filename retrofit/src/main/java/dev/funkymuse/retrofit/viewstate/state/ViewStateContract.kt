package dev.funkymuse.retrofit.viewstate.state

import dev.funkymuse.retrofit.apiresult.ApiResult
import dev.funkymuse.retrofit.viewstate.event.ViewStatefulEvent
import kotlinx.coroutines.flow.StateFlow


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