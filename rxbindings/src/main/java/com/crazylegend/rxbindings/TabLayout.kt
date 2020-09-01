package com.crazylegend.rxbindings

import com.crazylegend.rx.mainThreadScheduler
import com.google.android.material.tabs.TabLayout
import com.jakewharton.rxbinding4.material.TabLayoutSelectionEvent
import com.jakewharton.rxbinding4.material.selectionEvents
import com.jakewharton.rxbinding4.material.selections
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */

fun TabLayout.selectionChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                               callback: (tab: TabLayout.Tab) -> Unit = {}) {
    val changes = selections()

    changes.debounce(debounce, debounceTime)

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

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}

