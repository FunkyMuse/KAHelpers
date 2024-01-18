package com.funkymuse.rx

import io.reactivex.rxjava3.disposables.Disposable




/**
 * Safely dispose = if not null and not already disposed
 */
fun Disposable?.safeDispose() {
    if (this?.isDisposed == false) {
        dispose()
    }
}
