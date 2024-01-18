package com.funkymuse.setofusefulkotlinextensions

import com.funkymuse.retrofit.viewstate.event.ViewEventContract
import com.funkymuse.retrofit.viewstate.event.ViewStatefulEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext

//should be injected
class ViewEventProvider : ViewEventContract {

    private val channelEvents: Channel<ViewStatefulEvent> = Channel(Channel.BUFFERED)
    val viewStatefulEvent: Flow<ViewStatefulEvent> = channelEvents.receiveAsFlow()

    override suspend fun provideEvent(viewStatefulEvent: ViewStatefulEvent) = withContext(Dispatchers.Main.immediate) {
        channelEvents.send(viewStatefulEvent)
    }
}