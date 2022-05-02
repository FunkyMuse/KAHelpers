package com.crazylegend.retrofit

import com.crazylegend.retrofit.viewstate.event.ViewEventContract
import com.crazylegend.retrofit.viewstate.event.ViewStatefulEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class FakeViewEvent : ViewEventContract {

    private val state = Channel<ViewStatefulEvent>(Channel.BUFFERED)
    val event = state.receiveAsFlow()

    override suspend fun provideEvent(viewStatefulEvent: ViewStatefulEvent) {
        state.send(viewStatefulEvent)
    }
}