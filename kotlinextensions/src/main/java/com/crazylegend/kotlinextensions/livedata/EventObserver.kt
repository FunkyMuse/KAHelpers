package com.crazylegend.kotlinextensions.livedata

import androidx.lifecycle.Observer


/**
 * An [Observer] for [SingleEvent]s, simplifying the pattern of checking if the [SingleEvent]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [SingleEvent]'s contents has not been handled.
 */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<SingleEvent<T>> {
    override fun onChanged(singleEvent: SingleEvent<T>?) {
        singleEvent?.getContentIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}