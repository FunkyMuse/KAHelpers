package dev.funkymuse.retrofit

import dev.funkymuse.retrofit.viewstate.event.ViewEventContract
import dev.funkymuse.retrofit.viewstate.event.ViewStatefulEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class FakeViewEvent : ViewEventContract {

    private val state = Channel<ViewStatefulEvent>(Channel.BUFFERED)
    val event = state.receiveAsFlow()

    override suspend fun provideEvent(viewStatefulEvent: ViewStatefulEvent) {
        state.send(viewStatefulEvent)
    }
}