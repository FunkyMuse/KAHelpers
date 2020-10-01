package com.crazylegend.rx

import io.reactivex.rxjava3.disposables.Disposable


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

/**
* Better way to add disposable to composite dispossable 
*/
fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}
