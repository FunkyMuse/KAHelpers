package com.crazylegend.rxbindings

import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.rx.mainThreadScheduler
import com.jakewharton.rxbinding4.recyclerview.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */


fun RecyclerView.scrollChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                               callback: (event: RecyclerViewScrollEvent) -> Unit = {}) {

    val changes = scrollEvents()
    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}


fun RecyclerView.flingChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                              callback: (event: RecyclerViewFlingEvent) -> Unit = {}) {

    val changes = flingEvents()
    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}

fun RecyclerView.childAttachStateChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                                         callback: (event: RecyclerViewChildAttachStateChangeEvent) -> Unit = {}) {

    val changes = childAttachStateChangeEvents()
    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}

fun RecyclerView.scrollStateChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                                    callback: (state: Int) -> Unit = {}) {

    val changes = scrollStateChanges()
    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}

fun RecyclerView.Adapter<*>.dataChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS,
                                        skipInitialValue: Boolean = true,
                                        compositeDisposable: CompositeDisposable,
                                        callback: (state: RecyclerView.Adapter<*>) -> Unit = {}) {

    val changes = dataChanges()
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

