package com.crazylegend.kotlinextensions.rx.bindings

import androidx.leanback.widget.SearchBar
import com.crazylegend.kotlinextensions.rx.mainThreadScheduler
import com.crazylegend.kotlinextensions.rx.newThreadScheduler
import com.jakewharton.rxbinding3.leanback.SearchBarSearchQueryEvent
import com.jakewharton.rxbinding3.leanback.searchQueryChangeEvents
import com.jakewharton.rxbinding3.leanback.searchQueryChanges
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */


fun SearchBar.queryTextChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                               callback: (text: String) -> Unit = {}) {
    val changes = searchQueryChanges()

    changes.debounce(debounce, debounceTime)
            .subscribeOn(newThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}


fun SearchBar.queryEventChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                                callback: (text: SearchBarSearchQueryEvent) -> Unit = {}) {
    val changes = searchQueryChangeEvents()

    changes.debounce(debounce, debounceTime)
            .subscribeOn(newThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}


