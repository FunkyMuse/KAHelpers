package com.crazylegend.kotlinextensions.rx.bindings

import com.crazylegend.kotlinextensions.rx.mainThreadScheduler
import com.crazylegend.kotlinextensions.rx.newThreadScheduler
import com.google.android.material.appbar.AppBarLayout
import com.jakewharton.rxbinding3.material.offsetChanges
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */


fun AppBarLayout.offsetChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                           callback: (state: Int) -> Unit = {}) {

    val changes = offsetChanges()
    changes.debounce(debounce, debounceTime)
            .subscribeOn(newThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}