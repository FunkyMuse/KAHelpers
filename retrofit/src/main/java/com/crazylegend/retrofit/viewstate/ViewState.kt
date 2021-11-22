package com.crazylegend.retrofit.viewstate

import com.crazylegend.retrofit.retrofitResult.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Created by funkymuse on 11/20/21 to long live and prosper !
 */
class ViewState<T>(
        capacity: Int = Channel.BUFFERED,
        onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND,
        onUndeliveredElement: ((ViewEvent) -> Unit)? = null,
        defaultRetrofitState : RetrofitResult<T> = RetrofitResult.Idle
) : ViewStateContract<T> {

    private val viewEvents: Channel<ViewEvent> = Channel(capacity, onBufferOverflow, onUndeliveredElement)
    override val viewEvent = viewEvents.receiveAsFlow()

    private val dataState : MutableStateFlow<RetrofitResult<T>> = MutableStateFlow(defaultRetrofitState)
    override val data = dataState.asStateFlow()

    override var payload: T? = null

    override suspend fun emitEvent(retrofitResult: RetrofitResult<T>) {
        dataState.value = retrofitResult
        retrofitResult
                .onLoading { viewEvents.send(ViewEvent.Loading) }
                .onError { viewEvents.send(ViewEvent.Error) }
                .onApiError { _, _ -> viewEvents.send(ViewEvent.ApiError) }
                .onIdle { viewEvents.send(ViewEvent.Idle) }
                .onSuccess { viewEvents.send(ViewEvent.Success) }
    }

    override val isDataLoaded get() = payload != null

    override val isDataNotLoaded get() = !isDataLoaded

}