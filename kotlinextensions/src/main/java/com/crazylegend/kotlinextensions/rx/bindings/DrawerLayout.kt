package com.crazylegend.kotlinextensions.rx.bindings

import androidx.drawerlayout.widget.DrawerLayout
import com.crazylegend.kotlinextensions.rx.mainThreadScheduler
import com.jakewharton.rxbinding3.drawerlayout.drawerOpen
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */

fun DrawerLayout.drawerOpenChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                                   skipInitial: Boolean = true,
                                   gravity: Int,
                                   callback: (state: Boolean) -> Unit = {}) {

    val changes = drawerOpen(gravity)
    if (skipInitial) {
        changes.skipInitialValue()
    }
    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}
