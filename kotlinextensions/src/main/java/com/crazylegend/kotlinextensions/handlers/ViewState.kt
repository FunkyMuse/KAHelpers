package com.crazylegend.kotlinextensions.handlers


/**
 * Created by hristijan on 9/6/19 to long live and prosper !
 */
sealed class ViewState {
    data class InitialState(val isVisible: Boolean = false, val text: String = "") : ViewState()
    data class Loading(val isVisible:Boolean = true, val text: String = "Loading") : ViewState()
    data class Success(val isVisible: Boolean = false, val text: String = "") : ViewState()
    data class Failure(val isVisible: Boolean = false, val text: String = "") : ViewState()
}