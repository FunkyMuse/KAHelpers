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
 */
fun <T : Response<R>, R> Flowable<T>?.makeApiCallPost(
        result: MutableLiveData<RetrofitResult<R>>,
        compositeDisposable: CompositeDisposable,
        dropBackPressure: Boolean = false
) {
    result.loadingPost()

    this?.let { call ->

        if (dropBackPressure) {
            call.onBackpressureDrop()
        }
        call.observeOn(mainThreadScheduler)
                .subscribe({
                    result.subscribePost(it)
                }, {
                    result.callErrorPost(it)
                }).addTo(compositeDisposable)
    }

}


fun <T : Response<R>, R> Flowable<T>?.makeApiCallListPost(
        result: MutableLiveData<RetrofitResult<R>>,
        compositeDisposable: CompositeDisposable,
        dropBackPressure: Boolean = false,
        includeEmptyData: Boolean = true
) {
    result.loadingPost()

    this?.let { call ->

        if (dropBackPressure) {
            call.onBackpressureDrop()
        }
        call.observeOn(mainThreadScheduler)
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
 * @param result MutableLiveData<RetrofitResult<R>>
 * @param compositeDisposable CompositeDisposable
 */
fun <T : Response<R>, R> Single<T>?.makeApiCallPost(
        result: MutableLiveData<RetrofitResult<R>>,
        compositeDisposable: CompositeDisposable
) {
    result.loadingPost()
    this?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribePost(it)
    }, {
        result.callErrorPost(it)
    })?.addTo(compositeDisposable)

}

fun <T : Response<R>, R> Single<T>?.makeApiCallListPost(
        result: MutableLiveData<RetrofitResult<R>>,
        compositeDisposable: CompositeDisposable,
        includeEmptyData: Boolean = true
) {
    result.loadingPost()
    this?.observeOn(mainThreadScheduler)?.subscribe({
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
 */
fun <T : Response<R>, R> Observable<T>?.makeApiCallPost(
        result: MutableLiveData<RetrofitResult<R>>,
        compositeDisposable: CompositeDisposable
) {
    result.loadingPost()
    this?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribePost(it)
    }, {
        result.callErrorPost(it)
    })?.addTo(compositeDisposable)

}

fun <T : Response<R>, R> Observable<T>?.makeApiCallListPost(
        result: MutableLiveData<RetrofitResult<R>>,
        compositeDisposable: CompositeDisposable,
        includeEmptyData: Boolean = true
) {
    result.loadingPost()
    this?.observeOn(mainThreadScheduler)?.subscribe({
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
 */
fun <T : Response<R>, R> Maybe<T>?.makeApiCallPost(
        result: MutableLiveData<RetrofitResult<R>>,
        compositeDisposable: CompositeDisposable
) {
    result.loadingPost()
    this?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribePost(it)
    }, {
        result.callErrorPost(it)
    })?.addTo(compositeDisposable)

}

fun <T : Response<R>, R> Maybe<T>?.makeApiCallListPost(
        result: MutableLiveData<RetrofitResult<R>>,
        compositeDisposable: CompositeDisposable,
        includeEmptyData: Boolean = true
) {
    result.loadingPost()
    this?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribeListPost(it, includeEmptyData)
    }, {
        result.callErrorPost(it)
    })?.addTo(compositeDisposable)

}

/**
 *
 * @receiver CompositeDisposable
 * @param result MutableLiveData<RetrofitResult<T>>
 * @param dropBackPressure Boolean
 * @param function Function0<Flowable<Response<T>>?>
 */
fun <T> CompositeDisposable.makeApiCallPost(result: MutableLiveData<RetrofitResult<T>>, dropBackPressure: Boolean = false,
                                            function: () -> Flowable<Response<T>>?) {
    result.loadingPost()
    val disposable = function()
            ?.observeOn(mainThreadScheduler)


    if (dropBackPressure) {
        disposable?.onBackpressureDrop()
                ?.subscribe({
                    result.subscribePost(it)
                }, {
                    result.callErrorPost(it)
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

fun <T> CompositeDisposable.makeApiCallListPost(result: MutableLiveData<RetrofitResult<T>>, dropBackPressure: Boolean = false,
                                                includeEmptyData: Boolean = true, function: () -> Flowable<Response<T>>?) {
    result.loadingPost()
    val disposable = function()
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


/**
 *
 * @receiver CompositeDisposable
 * @param result MutableLiveData<RetrofitResult<T>>
 * @param function Function0<Single<Response<T>>?>
 */
fun <T> CompositeDisposable.makeApiCallSinglePost(result: MutableLiveData<RetrofitResult<T>>,
                                                  function: () -> Single<Response<T>>?) {
    result.loadingPost()
    function()
            ?.observeOn(mainThreadScheduler)
            ?.subscribe({
                result.subscribePost(it)
            }, {
                result.callErrorPost(it)
            })
            ?.addTo(this)
}

fun <T> CompositeDisposable.makeApiCallListSinglePost(result: MutableLiveData<RetrofitResult<T>>,
                                                      includeEmptyData: Boolean = true, function: () -> Single<Response<T>>?) {
    result.loadingPost()
    function()
            ?.observeOn(mainThreadScheduler)
            ?.subscribe({
                result.subscribeListPost(it, includeEmptyData)
            }, {
                result.callErrorPost(it)
            })
            ?.addTo(this)
}

fun <T> CompositeDisposable.makeApiCallMaybePost(result: MutableLiveData<RetrofitResult<T>>,
                                                 function: () -> Maybe<Response<T>>?) {
    result.loadingPost()
    function()
            ?.observeOn(mainThreadScheduler)
            ?.subscribe({
                result.subscribePost(it)
            }, {
                result.callErrorPost(it)
            })
            ?.addTo(this)
}

fun <T> CompositeDisposable.makeApiCallListMaybePost(result: MutableLiveData<RetrofitResult<T>>,
                                                     includeEmptyData: Boolean = true, function: () -> Maybe<Response<T>>?) {
    result.loadingPost()
    function()
            ?.observeOn(mainThreadScheduler)
            ?.subscribe({
                result.subscribeListPost(it, includeEmptyData)
            }, {
                result.callErrorPost(it)
            })
            ?.addTo(this)
}

