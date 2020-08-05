package com.crazylegend.kotlinextensions.rx.bindings

import androidx.appcompat.widget.SearchView
import com.crazylegend.kotlinextensions.rx.mainThreadScheduler
import com.jakewharton.rxbinding4.appcompat.queryTextChanges
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */

fun SearchView.textChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, skipInitialValue: Boolean = true,
                           compositeDisposable: CompositeDisposable,
                           callback: (text: String) -> Unit = {}) {
    val changes = queryTextChanges()
    if (skipInitialValue) {
        changes.skipInitialValue()
    }

    changes.debounce(debounce, debounceTime)
            .map { it.toString() }
            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it.toString())
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}
