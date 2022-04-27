package com.crazylegend.retrofit.viewstate.event

import kotlinx.coroutines.flow.Flow

interface ViewEventContract {
    //decouple this as well so that a direct flow is not forced, hmm
    val viewEvent: Flow<ViewEvent>

    suspend fun provideEvent(viewEvent: ViewEvent)
}