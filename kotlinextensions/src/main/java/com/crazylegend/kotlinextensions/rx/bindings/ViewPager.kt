package com.crazylegend.kotlinextensions.rx.bindings

import androidx.viewpager.widget.ViewPager
import com.crazylegend.kotlinextensions.rx.mainThreadScheduler
import com.jakewharton.rxbinding3.viewpager.ViewPagerPageScrollEvent
import com.jakewharton.rxbinding3.viewpager.pageScrollEvents
import com.jakewharton.rxbinding3.viewpager.pageScrollStateChanges
import com.jakewharton.rxbinding3.viewpager.pageSelections
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */



fun ViewPager.scrollStateChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
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


fun ViewPager.pageSelectionsChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS,
                                    skipInitialValue:Boolean = true,
                                    compositeDisposable: CompositeDisposable,
                                 callback: (pageSelections: Int) -> Unit = {}) {
    val changes = pageSelections()
    if (skipInitialValue){
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


fun ViewPager.pageScrollChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS,
                                    compositeDisposable: CompositeDisposable,
                                 callback: (viewPagerPageScrollEvent: ViewPagerPageScrollEvent) -> Unit = {}) {
    val changes = pageScrollEvents()
    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}


