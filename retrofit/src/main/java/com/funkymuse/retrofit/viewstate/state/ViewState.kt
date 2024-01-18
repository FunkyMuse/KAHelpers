package com.funkymuse.retrofit.viewstate.state

import com.funkymuse.retrofit.apiresult.ApiResult
import com.funkymuse.retrofit.viewstate.event.ViewEventContract
import com.funkymuse.retrofit.viewstate.event.ViewStatefulEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class ViewState<T>(
    private val viewEventContract: ViewEventContract? = null,
    defaultViewState: ViewStatefulEvent = ViewStatefulEvent.Idle
) : ViewStateContract<T> {

    private val _viewState: MutableStateFlow<ViewStatefulEvent> = MutableStateFlow(defaultViewState)
    override val viewState = _viewState.asStateFlow()

    override var payload: T? = null

    override suspend fun emitEvent(apiResult: ApiResult<T>) {
        viewEventContract?.provideEvent(apiResult.asViewEvent())
    }

    override fun emitState(apiResult: ApiResult<T>) {
        _viewState.value = apiResult.asViewEvent()
    }

    override val isDataLoaded get() = payload != null

    override val isDataNotLoaded get() = !isDataLoaded

}

