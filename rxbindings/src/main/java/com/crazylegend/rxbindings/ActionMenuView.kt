package com.crazylegend.rxbindings

import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.ActionMenuView
import com.crazylegend.rx.mainThreadScheduler
import com.jakewharton.rxbinding4.appcompat.itemClicks
import com.jakewharton.rxbinding4.material.dismisses
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
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
