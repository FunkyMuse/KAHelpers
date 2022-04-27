package com.crazylegend.setofusefulkotlinextensions

import com.crazylegend.retrofit.viewstate.event.ViewEvent
import com.crazylegend.retrofit.viewstate.event.ViewEventContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext

//should be injected
class ViewEventProvider : ViewEventContract {

    private val channelEvents: Channel<ViewEvent> = Channel(Channel.BUFFERED)
    override val viewEvent: Flow<ViewEvent> = channelEvents.receiveAsFlow()

    override suspend fun provideEvent(viewEvent: ViewEvent) = withContext(Dispatchers.Main.immediate) {
        channelEvents.send(viewEvent)
    }
}