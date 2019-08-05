package com.crazylegend.kotlinextensions.coroutines


import android.os.Build
import android.view.View
import android.view.View.OnAttachStateChangeListener
import kotlinx.coroutines.Job
/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */



class AutoDisposableJob(private val view: View, private val wrapped: Job)
    : Job by wrapped, OnAttachStateChangeListener {
    override fun onViewAttachedToWindow(v: View?) = Unit

    override fun onViewDetachedFromWindow(v: View?) {
        cancel()
        view.removeOnAttachStateChangeListener(this)
    }

    private fun isViewAttached() =
            view.isAttachedToWindow || view.windowToken != null

    init {
        if(isViewAttached()) {
            view.addOnAttachStateChangeListener(this)
        } else {
            cancel()
        }

        invokeOnCompletion {
            view.post {
                view.removeOnAttachStateChangeListener(this)
            }
        }
    }
}

fun Job.asAutoDisposable(view: View) = AutoDisposableJob(view, this)
