package com.crazylegend.kotlinextensions.rx.bindings

import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.crazylegend.kotlinextensions.rx.mainThreadScheduler
import com.jakewharton.rxbinding4.slidingpanelayout.panelOpens
import com.jakewharton.rxbinding4.slidingpanelayout.panelSlides
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */

fun SlidingPaneLayout.panelOpenChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, skipInitialValue: Boolean = true,
                                       compositeDisposable: CompositeDisposable,
                                       callback: (state: Boolean) -> Unit = {}) {
    val changes = panelOpens()
    if (skipInitialValue) {
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


fun SlidingPaneLayout.panelSlideChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                                        callback: (state: Float) -> Unit = {}) {
    val changes = panelSlides()

    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}
