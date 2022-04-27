package com.crazylegend.retrofit.viewstate.event

import kotlinx.coroutines.flow.Flow

interface ViewEventContract {
    val viewEvent: Flow<ViewEvent>

    suspend fun provideEvent(viewEvent: ViewEvent)
}