package com.crazylegend.retrofit.viewstate

/**
 * Created by funkymuse on 11/20/21 to long live and prosper !
 */
sealed interface ViewEvent {
    object Success : ViewEvent
    object Loading : ViewEvent
    object Idle : ViewEvent
    object Error : ViewEvent
    object ApiError : ViewEvent
}

val ViewEvent.isLoading get() = this is ViewEvent.Loading
val ViewEvent.isIdle get() = this is ViewEvent.Idle
val ViewEvent.isError get() = this is ViewEvent.Error
val ViewEvent.isApiError get() = this is ViewEvent.ApiError
val ViewEvent.isSuccess get() = this is ViewEvent.Success
