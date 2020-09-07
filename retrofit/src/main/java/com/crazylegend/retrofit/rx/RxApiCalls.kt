package com.crazylegend.retrofit.rx

import androidx.lifecycle.MutableLiveData
import com.crazylegend.retrofit.retrofitResult.*
import com.crazylegend.rx.mainThreadScheduler
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import retrofit2.Response

/**
 * Created by crazy on 9/1/20 to long live and prosper !
 */


/**
 *
 * @receiver Flowable<T>?
 * @param result MutableLiveData<RetrofitResult<R>>
 * @param compositeDisposable CompositeDisposable
 * @param dropBackPressure Boolean
 * @param includeEmptyData Boolean
 */
fun <T : Response<R>, R> Flowable<T>?.makeApiCall(
        result: MutableLiveData<RetrofitResult<R>>,
        compositeDisposable: CompositeDisposable,
        dropBackPressure: Boolean = false
) {
    result.loading()

    this?.let { call ->

        if (dropBackPressure) {
            call.onBackpressureDrop()
        }
        call.observeOn(mainThreadScheduler)
                .subscribe({
                    result.subscribe(it)
                }, {
                    result.callError(it)
                }).addTo(compositeDisposable)
    }

}


fun <T : Response<R>, R> Flowable<T>?.makeApiCallList(
        result: MutableLiveData<RetrofitResult<R>>,
        compositeDisposable: CompositeDisposable,
        dropBackPressure: Boolean = false,
        includeEmptyData: Boolean = true
) {
    result.loading()

    this?.let { call ->

        if (dropBackPressure) {
            call.onBackpressureDrop()
        }
        call.observeOn(mainThreadScheduler)
                .subscribe({
                    result.subscribeList(it, includeEmptyData)
                }, {
                    result.callError(it)
                }).addTo(compositeDisposable)
    }

}

/**
 *
 * @receiver Single<T>?
 * @param result MutableLiveData<RetrofitResult<R>>
 * @param compositeDisposable CompositeDisposable
 * @param includeEmptyData Boolean
 */
fun <T : Response<R>, R> Single<T>?.makeApiCall(
        result: MutableLiveData<RetrofitResult<R>>,
        compositeDisposable: CompositeDisposable
) {
    result.loading()
    this?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribe(it)
    }, {
        result.callError(it)
    })?.addTo(compositeDisposable)

}

fun <T : Response<R>, R> Single<T>?.makeApiCallList(
        result: MutableLiveData<RetrofitResult<R>>,
        compositeDisposable: CompositeDisposable,
        includeEmptyData: Boolean = true
) {
    result.loading()
    this?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribeList(it, includeEmptyData)
    }, {
        result.callError(it)
    })?.addTo(compositeDisposable)

}

/**
 *
 * @receiver Observable<T>?
 * @param result MutableLiveData<RetrofitResult<R>>
 * @param compositeDisposable CompositeDisposable
 * @param includeEmptyData Boolean
 */
fun <T : Response<R>, R> Observable<T>?.makeApiCall(
        result: MutableLiveData<RetrofitResult<R>>,
        compositeDisposable: CompositeDisposable
) {
    result.loading()
    this?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribe(it)
    }, {
        result.callError(it)
    })?.addTo(compositeDisposable)

}

fun <T : Response<R>, R> Observable<T>?.makeApiCallList(
        result: MutableLiveData<RetrofitResult<R>>,
        compositeDisposable: CompositeDisposable,
        includeEmptyData: Boolean = true
) {
    result.loading()
    this?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribeList(it, includeEmptyData)
    }, {
        result.callError(it)
    })?.addTo(compositeDisposable)

}

/**
 *
 * @receiver Maybe<T>?
 * @param result MutableLiveData<RetrofitResult<R>>
 * @param compositeDisposable CompositeDisposable
 * @param includeEmptyData Boolean
 */
fun <T : Response<R>, R> Maybe<T>?.makeApiCall(
        result: MutableLiveData<RetrofitResult<R>>,
        compositeDisposable: CompositeDisposable
) {
    result.loading()
    this?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribe(it)
    }, {
        result.callError(it)
    })?.addTo(compositeDisposable)

}

fun <T : Response<R>, R> Maybe<T>?.makeApiCallList(
        result: MutableLiveData<RetrofitResult<R>>,
        compositeDisposable: CompositeDisposable,
        includeEmptyData: Boolean = true
) {
    result.loading()
    this?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribeList(it, includeEmptyData)
    }, {
        result.callError(it)
    })?.addTo(compositeDisposable)

}

/**
 *
 * @receiver CompositeDisposable
 * @param result MutableLiveData<RetrofitResult<T>>
 * @param dropBackPressure Boolean
 * @param includeEmptyData Boolean
 * @param function Function0<Flowable<Response<T>>?>
 */
fun <T> CompositeDisposable.makeApiCall(result: MutableLiveData<RetrofitResult<T>>, dropBackPressure: Boolean = false,
                                        function: () -> Flowable<Response<T>>?) {
    result.loading()
    val disposable = function()
            ?.observeOn(mainThreadScheduler)


    if (dropBackPressure) {
        disposable?.onBackpressureDrop()
                ?.subscribe({
                    result.subscribe(it)
                }, {
                    result.callError(it)
                })
                ?.addTo(this)
    } else {
        disposable
                ?.subscribe({
                    result.subscribe(it)
                }, {
                    result.callError(it)
                })
                ?.addTo(this)
    }

}

fun <T> CompositeDisposable.makeApiCallList(result: MutableLiveData<RetrofitResult<T>>, dropBackPressure: Boolean = false,
                                            includeEmptyData: Boolean = true, function: () -> Flowable<Response<T>>?) {
    result.loading()
    val disposable = function()
            ?.observeOn(mainThreadScheduler)


    if (dropBackPressure) {
        disposable?.onBackpressureDrop()
                ?.subscribe({
                    result.subscribeList(it, includeEmptyData)
                }, {
                    result.callError(it)
                })
                ?.addTo(this)
    } else {
        disposable
                ?.subscribe({
                    result.subscribeList(it, includeEmptyData)
                }, {
                    result.callError(it)
                })
                ?.addTo(this)
    }

}


/**
 *
 * @receiver CompositeDisposable
 * @param result MutableLiveData<RetrofitResult<T>>
 * @param includeEmptyData Boolean
 * @param function Function0<Single<Response<T>>?>
 */
fun <T> CompositeDisposable.makeApiCallSingle(result: MutableLiveData<RetrofitResult<T>>,
                                              function: () -> Single<Response<T>>?) {
    result.loading()
    function()
            ?.observeOn(mainThreadScheduler)
            ?.subscribe({
                result.subscribe(it)
            }, {
                result.callError(it)
            })
            ?.addTo(this)
}

fun <T> CompositeDisposable.makeApiCallListSingle(result: MutableLiveData<RetrofitResult<T>>,
                                                  includeEmptyData: Boolean = true, function: () -> Single<Response<T>>?) {
    result.loading()
    function()
            ?.observeOn(mainThreadScheduler)
            ?.subscribe({
                result.subscribeList(it, includeEmptyData)
            }, {
                result.callError(it)
            })
            ?.addTo(this)
}

fun <T> CompositeDisposable.makeApiCallMaybe(result: MutableLiveData<RetrofitResult<T>>,
                                             function: () -> Maybe<Response<T>>?) {
    result.loading()
    function()
            ?.observeOn(mainThreadScheduler)
            ?.subscribe({
                result.subscribe(it)
            }, {
                result.callError(it)
            })
            ?.addTo(this)
}

fun <T> CompositeDisposable.makeApiCallListMaybe(result: MutableLiveData<RetrofitResult<T>>,
                                                 includeEmptyData: Boolean = true, function: () -> Maybe<Response<T>>?) {
    result.loading()
    function()
            ?.observeOn(mainThreadScheduler)
            ?.subscribe({
                result.subscribeList(it, includeEmptyData)
            }, {
                result.callError(it)
            })
            ?.addTo(this)
}
