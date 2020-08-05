package com.crazylegend.kotlinextensions.rx.bindings

import androidx.core.widget.NestedScrollView
import com.crazylegend.kotlinextensions.rx.mainThreadScheduler
import com.jakewharton.rxbinding4.core.scrollChangeEvents
import com.jakewharton.rxbinding4.view.ViewScrollChangeEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */

fun NestedScrollView.scrollChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                                   callback: (event: ViewScrollChangeEvent) -> Unit = {}) {

    val changes = scrollChangeEvents()
    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}