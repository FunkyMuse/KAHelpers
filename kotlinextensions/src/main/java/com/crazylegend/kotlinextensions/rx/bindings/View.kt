package com.crazylegend.kotlinextensions.rx.bindings

import android.view.MotionEvent
import android.view.View
import com.crazylegend.kotlinextensions.rx.mainThreadScheduler
import com.jakewharton.rxbinding4.material.dismisses
import com.jakewharton.rxbinding4.view.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */


fun View.focusChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, skipInitialValue: Boolean = true,
                      compositeDisposable: CompositeDisposable,
                      callback: (focus: Boolean) -> Unit = {}) {
    val changes = focusChanges()
    if (skipInitialValue) {
        changes.skipInitialValue()
    }

    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}


fun View.layoutChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                       callback: (focus: View) -> Unit = {}) {
    val changes = layoutChanges()

    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(this)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}


fun View.hoverChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                      callback: (hover: MotionEvent) -> Unit = {}) {
    val changes = hovers()

    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}


fun View.scrollChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                       callback: (viewScrollChangeEvent: ViewScrollChangeEvent) -> Unit = {}) {
    val changes = scrollChangeEvents()
    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}


fun View.systemUiVisibilityChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                                   callback: (systemUiVisibilityChanges: Int) -> Unit = {}) {
    val changes = systemUiVisibilityChanges()
    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}


fun View.dismissChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                        callback: (state: View) -> Unit = {}) {

    val changes = dismisses()
    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}