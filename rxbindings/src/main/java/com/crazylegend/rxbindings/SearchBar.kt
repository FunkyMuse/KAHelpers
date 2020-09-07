package com.crazylegend.rxbindings

import androidx.leanback.widget.SearchBar
import com.crazylegend.rx.mainThreadScheduler
import com.jakewharton.rxbinding4.leanback.SearchBarSearchQueryEvent
import com.jakewharton.rxbinding4.leanback.searchQueryChangeEvents
import com.jakewharton.rxbinding4.leanback.searchQueryChanges
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */


fun SearchBar.queryTextChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                               callback: (text: String) -> Unit = {}) {
    val changes = searchQueryChanges()

    changes.debounce(debounce, debounceTime)

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

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}


