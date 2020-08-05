package com.crazylegend.kotlinextensions.rx.bindings

import android.view.MenuItem
import androidx.appcompat.widget.PopupMenu
import com.crazylegend.kotlinextensions.rx.mainThreadScheduler
import com.jakewharton.rxbinding4.appcompat.dismisses
import com.jakewharton.rxbinding4.appcompat.itemClicks
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */


fun PopupMenu.clickChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
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


fun PopupMenu.dismissChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                             callback: (state: PopupMenu) -> Unit = {}) {

    val changes = dismisses()
    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(this)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}
