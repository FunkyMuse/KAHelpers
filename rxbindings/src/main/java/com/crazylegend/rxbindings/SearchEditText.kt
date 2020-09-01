package com.crazylegend.rxbindings

import androidx.leanback.widget.SearchEditText
import com.crazylegend.rx.mainThreadScheduler
import com.jakewharton.rxbinding4.leanback.keyboardDismisses
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */


fun SearchEditText.keyboardDismissChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, compositeDisposable: CompositeDisposable,
                                          callback: (state: SearchEditText) -> Unit = {}) {

    val changes = keyboardDismisses()
    changes.debounce(debounce, debounceTime)

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(this)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}


fun SearchEditText.textChanges(debounce: Long = 300L, debounceTime: TimeUnit = TimeUnit.MILLISECONDS, skipInitialValue: Boolean = true,
                               compositeDisposable: CompositeDisposable,
                               callback: (text: String) -> Unit = {}) {
    val changes = textChanges()
    if (skipInitialValue) {
        changes.skipInitialValue()
    }

    changes.debounce(debounce, debounceTime)
            .map { it.toString() }

            .observeOn(mainThreadScheduler)
            .subscribe({
                callback(it.toString())
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
}

