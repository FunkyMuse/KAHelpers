package com.crazylegend.kotlinextensions.rx.bindings

import android.view.MenuItem
import com.crazylegend.kotlinextensions.rx.mainThreadScheduler
import com.crazylegend.kotlinextensions.rx.newThreadScheduler
import com.google.android.material.navigation.NavigationView
import com.jakewharton.rxbinding3.material.itemSelections
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */


fun NavigationView.selectionChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                                          callback: (state: MenuItem) -> Unit = {}) {

    val changes = itemSelections()
    changes.debounce(debounce, debounceTime)
            .subscribeOn(newThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}