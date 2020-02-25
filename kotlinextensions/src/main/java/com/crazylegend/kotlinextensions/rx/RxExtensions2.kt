package com.crazylegend.kotlinextensions.rx

import io.reactivex.disposables.Disposable


/**
 * Created by crazy on 2/25/20 to long live and prosper !
 */

/**
 * Safely dispose = if not null and not already disposed
 */
fun Disposable?.safeDispose() {
    if (this?.isDisposed == false) {
        dispose()
    }
}
