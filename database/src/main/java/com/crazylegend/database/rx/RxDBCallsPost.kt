package com.crazylegend.database.rx

import androidx.lifecycle.MutableLiveData
import com.crazylegend.database.*
import com.crazylegend.rx.ioThreadScheduler
import com.crazylegend.rx.mainThreadScheduler
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

/**
 * Created by crazy on 9/1/20 to long live and prosper !
 */


/**
 *
 * @receiver Flowable<T>?
 * @param result MutableLiveData<DBResult<R>>
 * @param compositeDisposable CompositeDisposable
 * @param dropBackPressure Boolean
 * @param includeEmptyData Boolean
 */
fun <T> Flowable<T>?.makeDBCallPost(
        result: MutableLiveData<DBResult<T>>,
        compositeDisposable: CompositeDisposable,
        dropBackPressure: Boolean = false,
        includeEmptyData: Boolean = false
) {
    result.queryingPost()
    this?.let { call ->
        if (dropBackPressure) {
            call.onBackpressureDrop()
        }
        call.subscribeOn(ioThreadScheduler)
                .observeOn(mainThreadScheduler)
                .subscribe({
                    result.subscribePost(it, includeEmptyData)
                }, {
                    result.callErrorPost(it)
                }).addTo(compositeDisposable)
    }
}

fun <T> Flowable<T>?.makeDBCallListPost(
        result: MutableLiveData<DBResult<T>>,
        compositeDisposable: CompositeDisposable,
        dropBackPressure: Boolean = false,
        includeEmptyData: Boolean = true
) {
    result.queryingPost()
    this?.let { call ->
        if (dropBackPressure) {
            call.onBackpressureDrop()
        }
        call.subscribeOn(ioThreadScheduler)
                .observeOn(mainThreadScheduler)
                .subscribe({
                    result.subscribeListPost(it, includeEmptyData)
                }, {
                    result.callErrorPost(it)
                }).addTo(compositeDisposable)
    }
}

/**
 *
 * @receiver Single<T>?
 * @param result MutableLiveData<DBResult<R>>
 * @param compositeDisposable CompositeDisposable
 * @param includeEmptyData Boolean
 */
fun <T> Single<T>?.makeDBCallPost(
        result: MutableLiveData<DBResult<T>>,
        compositeDisposable: CompositeDisposable,
        includeEmptyData: Boolean = false
) {
    result.queryingPost()
    this?.subscribeOn(ioThreadScheduler)?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribePost(it, includeEmptyData)
    }, {
        result.callErrorPost(it)
    })?.addTo(compositeDisposable)

}

fun <T> Single<T>?.makeDBCallListPost(
        result: MutableLiveData<DBResult<T>>,
        compositeDisposable: CompositeDisposable,
        includeEmptyData: Boolean = true
) {
    result.queryingPost()
    this?.subscribeOn(ioThreadScheduler)?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribeListPost(it, includeEmptyData)
    }, {
        result.callErrorPost(it)
    })?.addTo(compositeDisposable)

}

/**
 *
 * @receiver Observable<T>?
 * @param result MutableLiveData<RetrofitResult<R>>
 * @param compositeDisposable CompositeDisposable
 * @param includeEmptyData Boolean
 */
fun <T> Observable<T>?.makeDBCallPost(
        result: MutableLiveData<DBResult<T>>,
        compositeDisposable: CompositeDisposable,
        includeEmptyData: Boolean = false
) {
    result.queryingPost()

    this?.subscribeOn(ioThreadScheduler)?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribePost(it, includeEmptyData)
    }, {
        result.callErrorPost(it)
    })?.addTo(compositeDisposable)

}

fun <T> Observable<T>?.makeDBCallListPost(
        result: MutableLiveData<DBResult<T>>,
        compositeDisposable: CompositeDisposable,
        includeEmptyData: Boolean = true
) {
    result.queryingPost()
    this?.subscribeOn(ioThreadScheduler)?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribeListPost(it, includeEmptyData)
    }, {
        result.callErrorPost(it)
    })?.addTo(compositeDisposable)

}

/**
 *
 * @receiver Maybe<T>?
 * @param result MutableLiveData<RetrofitResult<R>>
 * @param compositeDisposable CompositeDisposable
 * @param includeEmptyData Boolean
 */
fun <T> Maybe<T>?.makeDBCallPost(
        result: MutableLiveData<DBResult<T>>,
        compositeDisposable: CompositeDisposable,
        includeEmptyData: Boolean = false
) {
    result.queryingPost()
    this?.subscribeOn(ioThreadScheduler)?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribeListPost(it, includeEmptyData)
    }, {
        result.callErrorPost(it)
    })?.addTo(compositeDisposable)
}

fun <T> Maybe<T>?.makeDBCallListPost(
        result: MutableLiveData<DBResult<T>>,
        compositeDisposable: CompositeDisposable,
        includeEmptyData: Boolean = true
) {
    result.queryingPost()

    this?.subscribeOn(ioThreadScheduler)?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribeListPost(it, includeEmptyData)
    }, {
        result.callErrorPost(it)
    })?.addTo(compositeDisposable)
}


fun <T> CompositeDisposable.makeDBCallPost(result: MutableLiveData<DBResult<T>>, dropBackPressure: Boolean = false,
                                           includeEmptyData: Boolean = false, function: () -> Flowable<T>?) {
    result.queryingPost()
    val disposable = function()
            ?.subscribeOn(ioThreadScheduler)
            ?.observeOn(mainThreadScheduler)

    if (dropBackPressure) {
        disposable?.onBackpressureDrop()
                ?.subscribe({
                    result.subscribePost(it, includeEmptyData)
                }, {
                    result.callErrorPost(it)
                })
                ?.addTo(this)
    } else {
        disposable
                ?.subscribe({
                    result.subscribePost(it, includeEmptyData)
                }, {
                    result.callErrorPost(it)
                })
                ?.addTo(this)
    }

}

fun <T> CompositeDisposable.makeDBCallListPost(result: MutableLiveData<DBResult<T>>, dropBackPressure: Boolean = false,
                                               includeEmptyData: Boolean = false, function: () -> Flowable<T>?) {
    result.queryingPost()
    val disposable = function()
            ?.subscribeOn(ioThreadScheduler)
            ?.observeOn(mainThreadScheduler)

    if (dropBackPressure) {
        disposable?.onBackpressureDrop()
                ?.subscribe({
                    result.subscribeListPost(it, includeEmptyData)
                }, {
                    result.callErrorPost(it)
                })
                ?.addTo(this)
    } else {
        disposable
                ?.subscribe({
                    result.subscribeListPost(it, includeEmptyData)
                }, {
                    result.callErrorPost(it)
                })
                ?.addTo(this)
    }

}

fun <T> CompositeDisposable.makeDBCallSinglePost(result: MutableLiveData<DBResult<T>>,
                                                 includeEmptyData: Boolean = false, function: () -> Single<T>?) {
    result.queryingPost()
    function()
            ?.subscribeOn(ioThreadScheduler)
            ?.observeOn(mainThreadScheduler)
            ?.subscribe({
                result.subscribePost(it, includeEmptyData)
            }, {
                result.callErrorPost(it)
            })
            ?.addTo(this)
}

fun <T> CompositeDisposable.makeDBCallListSinglePost(result: MutableLiveData<DBResult<T>>,
                                                     includeEmptyData: Boolean = true, function: () -> Single<T>?) {
    result.queryingPost()
    function()
            ?.subscribeOn(ioThreadScheduler)
            ?.observeOn(mainThreadScheduler)
            ?.subscribe({
                result.subscribeListPost(it, includeEmptyData)
            }, {
                result.callErrorPost(it)
            })
            ?.addTo(this)
}

fun <T> CompositeDisposable.makeDBCallMaybePost(result: MutableLiveData<DBResult<T>>,
                                                includeEmptyData: Boolean = false, function: () -> Maybe<T>?) {
    result.queryingPost()
    function()
            ?.subscribeOn(ioThreadScheduler)
            ?.observeOn(mainThreadScheduler)
            ?.subscribe({
                result.subscribeListPost(it, includeEmptyData)
            }, {
                result.callErrorPost(it)
            })
            ?.addTo(this)
}

fun <T> CompositeDisposable.makeDBCallListMaybePost(result: MutableLiveData<DBResult<T>>,
                                                    includeEmptyData: Boolean = true, function: () -> Maybe<T>?) {
    result.queryingPost()
    function()
            ?.subscribeOn(ioThreadScheduler)
            ?.observeOn(mainThreadScheduler)
            ?.subscribe({
                result.subscribeListPost(it, includeEmptyData)
            }, {
                result.callErrorPost(it)
            })
            ?.addTo(this)
}