package com.crazylegend.kotlinextensions.rx.bindings

import com.crazylegend.kotlinextensions.rx.mainThreadScheduler
import com.crazylegend.kotlinextensions.rx.newThreadScheduler
import com.google.android.material.chip.Chip
import com.jakewharton.rxbinding3.material.closeIconClicks
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */

fun Chip.closeIconChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                               callback: (state: Chip) -> Unit = {}) {

    val changes = closeIconClicks()
    changes.debounce(debounce, debounceTime)
            .subscribeOn(newThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(this)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}