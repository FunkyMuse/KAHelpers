package com.crazylegend.retrofit.viewstate

import com.crazylegend.retrofit.apiresult.*
import com.crazylegend.retrofit.retrofitResult.*
import com.crazylegend.retrofit.viewstate.onApiError
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Created by funkymuse on 11/20/21 to long live and prosper !
 */
class ViewState<T>(
        capacity: Int = Channel.UNLIMITED,
        onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND,
        onUndeliveredElement: ((ViewEvent) -> Unit)? = null,
        defaultApiState : ApiResult<T> = ApiResult.Idle
) : ViewStateContract<T> {

    private val viewEvents: Channel<ViewEvent> = Channel(capacity, onBufferOverflow, onUndeliveredElement)
    override val viewEvent = viewEvents.receiveAsFlow()

    private val dataState : MutableStateFlow<ApiResult<T>> = MutableStateFlow(defaultApiState)
    override val data = dataState.asStateFlow()

    override var payload: T? = null

    override suspend fun emitEvent(apiResult: ApiResult<T>) {
        dataState.value = apiResult
        apiResult
                .onLoading { viewEvents.send(ViewEvent.Loading) }
                .onError { throwable -> viewEvents.send(ViewEvent.Error(throwable)) }
                .onApiError { errorBody, code -> viewEvents.send(ViewEvent.ApiError(errorBody, code)) }
                .onIdle { viewEvents.send(ViewEvent.Idle) }
                .onSuccess { viewEvents.send(ViewEvent.Success) }
    }

    override val isDataLoaded get() = payload != null

    override val isDataNotLoaded get() = !isDataLoaded

}