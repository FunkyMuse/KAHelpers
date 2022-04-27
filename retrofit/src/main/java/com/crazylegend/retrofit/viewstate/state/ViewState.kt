package com.crazylegend.retrofit.viewstate.state

import com.crazylegend.retrofit.apiresult.ApiResult
import com.crazylegend.retrofit.apiresult.onApiErrorSuspend
import com.crazylegend.retrofit.apiresult.onErrorSuspend
import com.crazylegend.retrofit.apiresult.onIdleSuspend
import com.crazylegend.retrofit.apiresult.onLoadingSuspend
import com.crazylegend.retrofit.apiresult.onSuccessSuspend
import com.crazylegend.retrofit.viewstate.event.ViewEvent
import com.crazylegend.retrofit.viewstate.event.ViewEventContract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Created by funkymuse on 11/20/21 to long live and prosper !
 */
class ViewState<T>(
    private val viewEventContract: ViewEventContract? = null,
    defaultApiState: ApiResult<T> = ApiResult.Idle
) : ViewStateContract<T> {

    private val dataState: MutableStateFlow<ApiResult<T>> = MutableStateFlow(defaultApiState)
    override val data = dataState.asStateFlow()

    override var payload: T? = null

    override suspend fun emitEvent(apiResult: ApiResult<T>) {
        apiResult.asViewEvent()
        dataState.value = apiResult
        apiResult
            .onLoadingSuspend { provideEvent(ViewEvent.Loading) }
            .onErrorSuspend { throwable -> provideEvent(ViewEvent.Error(throwable)) }
            .onApiErrorSuspend { errorBody, code -> provideEvent(ViewEvent.ApiError(errorBody, code)) }
            .onIdleSuspend { provideEvent(ViewEvent.Idle) }
            .onSuccessSuspend { provideEvent(ViewEvent.Success) }
    }

    private suspend fun provideEvent(event: ViewEvent) {
        viewEventContract?.provideEvent(event)
    }

    override val isDataLoaded get() = payload != null

    override val isDataNotLoaded get() = !isDataLoaded

}

