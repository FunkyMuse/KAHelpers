package com.crazylegend.kotlinextensions.rx.bindings

import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.ActionMenuView
import com.crazylegend.kotlinextensions.rx.mainThreadScheduler
import com.jakewharton.rxbinding3.appcompat.itemClicks
import com.jakewharton.rxbinding3.material.dismisses
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */


fun ActionMenuView.clickChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                                callback: (state: MenuItem) -> Unit = {}) {

    val changes = itemClicks()
    changes.debounce(debounce, debounceTime)
            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}


fun ActionMenuView.dismissChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                                  callback: (state: View) -> Unit = {}) {

    val changes = dismisses()
    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}
