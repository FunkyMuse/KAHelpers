package com.crazylegend.kotlinextensions.rx.bindings

import com.crazylegend.kotlinextensions.rx.mainThreadScheduler
import com.crazylegend.kotlinextensions.rx.newThreadScheduler
import com.google.android.material.tabs.TabLayout
import com.jakewharton.rxbinding3.material.TabLayoutSelectionEvent
import com.jakewharton.rxbinding3.material.selectionEvents
import com.jakewharton.rxbinding3.material.selections
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */

fun TabLayout.selectionChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                               callback: (tab: TabLayout.Tab) -> Unit = {}) {
    val changes = selections()

    changes.debounce(debounce, debounceTime)
            .subscribeOn(newThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}

fun TabLayout.selectionEventChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                               callback: (tab: TabLayoutSelectionEvent) -> Unit = {}) {
    val changes = selectionEvents()

    changes.debounce(debounce, debounceTime)
            .subscribeOn(newThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}

