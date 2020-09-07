package com.crazylegend.rxbindings

import androidx.viewpager2.widget.ViewPager2
import com.crazylegend.rx.mainThreadScheduler
import com.jakewharton.rxbinding4.viewpager2.PageScrollEvent
import com.jakewharton.rxbinding4.viewpager2.pageScrollEvents
import com.jakewharton.rxbinding4.viewpager2.pageScrollStateChanges
import com.jakewharton.rxbinding4.viewpager2.pageSelections
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

import java.util.concurrent.TimeUnit


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */

fun ViewPager2.scrollStateChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                                  callback: (pageScrollStateChanges: Int) -> Unit = {}) {
    val changes = pageScrollStateChanges()
    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}


fun ViewPager2.pageSelectionsChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS,
                                     skipInitialValue: Boolean = true,
                                     compositeDisposable: CompositeDisposable,
                                     callback: (pageSelections: Int) -> Unit = {}) {
    val changes = pageSelections()
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


fun ViewPager2.pageScrollChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS,
                                 compositeDisposable: CompositeDisposable,
                                 callback: (viewPagerPageScrollEvent: PageScrollEvent) -> Unit = {}) {
    val changes = pageScrollEvents()
    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}



