package com.crazylegend.rxbindings

import com.crazylegend.rx.mainThreadScheduler
import com.google.android.material.appbar.AppBarLayout
import com.jakewharton.rxbinding4.material.offsetChanges
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */


fun AppBarLayout.offsetChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                               callback: (state: Int) -> Unit = {}) {

    val changes = offsetChanges()
    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}