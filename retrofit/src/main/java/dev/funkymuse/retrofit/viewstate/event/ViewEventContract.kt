package dev.funkymuse.retrofit.viewstate.event

fun interface ViewEventContract {
    suspend fun provideEvent(viewStatefulEvent: ViewStatefulEvent)
}