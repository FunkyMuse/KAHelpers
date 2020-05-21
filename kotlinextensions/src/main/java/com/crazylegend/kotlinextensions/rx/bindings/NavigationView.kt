package com.crazylegend.kotlinextensions.rx.bindings

import android.view.MenuItem
import com.crazylegend.kotlinextensions.rx.mainThreadScheduler
import com.google.android.material.navigation.NavigationView
import com.jakewharton.rxbinding4.material.itemSelections
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */


fun NavigationView.selectionChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                                          callback: (state: MenuItem) -> Unit = {}) {

    val changes = itemSelections()
    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}