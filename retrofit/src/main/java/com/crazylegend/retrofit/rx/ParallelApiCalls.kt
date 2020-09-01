package com.crazylegend.retrofit.rx

import androidx.lifecycle.MutableLiveData
import com.crazylegend.retrofit.retrofitResult.RetrofitResult
import com.crazylegend.retrofit.retrofitResult.callError
import com.crazylegend.retrofit.retrofitResult.subscribe
import com.crazylegend.rx.ioThreadScheduler
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


fun <T, U> CompositeDisposable.parallelApiCall(
        firstCall: Flowable<Response<T>>,
        secondCall: Flowable<Response<U>>,
        onError: (throwable: Throwable) -> Unit,
        onFirstCall: (model: Response<T>) -> Unit,
        onSecondCall: (model: Response<U>) -> Unit
) {
    Flowable.zip(
            firstCall.subscribeOn(ioThreadScheduler),
            secondCall.subscribeOn(ioThreadScheduler),
            { firstResponse: Response<T>, secondResponse: Response<U> ->
                Pair(firstResponse, secondResponse)
            }
    ).observeOn(mainThreadScheduler)
            .subscribe({
                onFirstCall(it.first)
                onSecondCall(it.second)
            }, {
                onError(it)
            }).addTo(this)
}

fun <T, U> CompositeDisposable.parallelApiCall(
        firstCall: Flowable<Response<T>>,
        secondCall: Flowable<Response<U>>,
        firstResult: MutableLiveData<RetrofitResult<T>>,
        secondResult: MutableLiveData<RetrofitResult<U>>,
        onError: (throwable: Throwable) -> Unit
) {
    Flowable.zip(
            firstCall.subscribeOn(ioThreadScheduler),
            secondCall.subscribeOn(ioThreadScheduler),
            { firstResponse: Response<T>, secondResponse: Response<U> ->
                Pair(firstResponse, secondResponse)
            }
    ).observeOn(mainThreadScheduler)
            .subscribe({
                firstResult.subscribe(it.first)
                secondResult.subscribe(it.second)
            }, {
                onError(it)
            }).addTo(this)
}

fun <T, U> CompositeDisposable.parallelApiCall(
        firstCall: Flowable<Response<T>>,
        secondCall: Flowable<Response<U>>,
        firstResult: MutableLiveData<RetrofitResult<T>>,
        secondResult: MutableLiveData<RetrofitResult<U>>
) {
    Flowable.zip(
            firstCall.subscribeOn(ioThreadScheduler),
            secondCall.subscribeOn(ioThreadScheduler),
            { firstResponse: Response<T>, secondResponse: Response<U> ->
                Pair(firstResponse, secondResponse)
            }
    ).observeOn(mainThreadScheduler)
            .subscribe({
                firstResult.subscribe(it.first)
                secondResult.subscribe(it.second)
            }, {
                firstResult.callError(it)
                secondResult.callError(it)
            }).addTo(this)
}

//

fun <T, U> CompositeDisposable.parallelApiCall(
        firstCall: Observable<Response<T>>,
        secondCall: Observable<Response<U>>,
        onError: (throwable: Throwable) -> Unit,
        onFirstCall: (model: Response<T>) -> Unit,
        onSecondCall: (model: Response<U>) -> Unit
) {
    Observable.zip(
            firstCall.subscribeOn(ioThreadScheduler),
            secondCall.subscribeOn(ioThreadScheduler),
            { firstResponse: Response<T>, secondResponse: Response<U> ->
                Pair(firstResponse, secondResponse)
            }
    ).observeOn(mainThreadScheduler)
            .subscribe({
                onFirstCall(it.first)
                onSecondCall(it.second)
            }, {
                onError(it)
            }).addTo(this)
}

fun <T, U> CompositeDisposable.parallelApiCall(
        firstCall: Observable<Response<T>>,
        secondCall: Observable<Response<U>>,
        firstResult: MutableLiveData<RetrofitResult<T>>,
        secondResult: MutableLiveData<RetrofitResult<U>>,
        onError: (throwable: Throwable) -> Unit
) {
    Observable.zip(
            firstCall.subscribeOn(ioThreadScheduler),
            secondCall.subscribeOn(ioThreadScheduler),
            { firstResponse: Response<T>, secondResponse: Response<U> ->
                Pair(firstResponse, secondResponse)
            }
    ).observeOn(mainThreadScheduler)
            .subscribe({
                firstResult.subscribe(it.first)
                secondResult.subscribe(it.second)
            }, {
                onError(it)
            }).addTo(this)
}

fun <T, U> CompositeDisposable.parallelApiCall(
        firstCall: Observable<Response<T>>,
        secondCall: Observable<Response<U>>,
        firstResult: MutableLiveData<RetrofitResult<T>>,
        secondResult: MutableLiveData<RetrofitResult<U>>
) {
    Observable.zip(
            firstCall.subscribeOn(ioThreadScheduler),
            secondCall.subscribeOn(ioThreadScheduler),
            { firstResponse: Response<T>, secondResponse: Response<U> ->
                Pair(firstResponse, secondResponse)
            }
    ).observeOn(mainThreadScheduler)
            .subscribe({
                firstResult.subscribe(it.first)
                secondResult.subscribe(it.second)
            }, {
                firstResult.callError(it)
                secondResult.callError(it)
            }).addTo(this)
}

//

fun <T, U> CompositeDisposable.parallelApiCall(
        firstCall: Single<Response<T>>,
        secondCall: Single<Response<U>>,
        onError: (throwable: Throwable) -> Unit,
        onFirstCall: (model: Response<T>) -> Unit,
        onSecondCall: (model: Response<U>) -> Unit
) {
    Single.zip(
            firstCall.subscribeOn(ioThreadScheduler),
            secondCall.subscribeOn(ioThreadScheduler),
            { firstResponse: Response<T>, secondResponse: Response<U> ->
                Pair(firstResponse, secondResponse)
            }
    ).observeOn(mainThreadScheduler)
            .subscribe({
                onFirstCall(it.first)
                onSecondCall(it.second)
            }, {
                onError(it)
            }).addTo(this)
}

fun <T, U> CompositeDisposable.parallelApiCall(
        firstCall: Single<Response<T>>,
        secondCall: Single<Response<U>>,
        firstResult: MutableLiveData<RetrofitResult<T>>,
        secondResult: MutableLiveData<RetrofitResult<U>>,
        onError: (throwable: Throwable) -> Unit
) {
    Single.zip(
            firstCall.subscribeOn(ioThreadScheduler),
            secondCall.subscribeOn(ioThreadScheduler),
            { firstResponse: Response<T>, secondResponse: Response<U> ->
                Pair(firstResponse, secondResponse)
            }
    ).observeOn(mainThreadScheduler)
            .subscribe({
                firstResult.subscribe(it.first)
                secondResult.subscribe(it.second)
            }, {
                onError(it)
            }).addTo(this)
}

fun <T, U> CompositeDisposable.parallelApiCall(
        firstCall: Single<Response<T>>,
        secondCall: Single<Response<U>>,
        firstResult: MutableLiveData<RetrofitResult<T>>,
        secondResult: MutableLiveData<RetrofitResult<U>>
) {
    Single.zip(
            firstCall.subscribeOn(ioThreadScheduler),
            secondCall.subscribeOn(ioThreadScheduler),
            { firstResponse: Response<T>, secondResponse: Response<U> ->
                Pair(firstResponse, secondResponse)
            }
    ).observeOn(mainThreadScheduler)
            .subscribe({
                firstResult.subscribe(it.first)
                secondResult.subscribe(it.second)
            }, {
                firstResult.callError(it)
                secondResult.callError(it)
            }).addTo(this)
}

//

fun <T, U> CompositeDisposable.parallelApiCall(
        firstCall: Maybe<Response<T>>,
        secondCall: Maybe<Response<U>>,
        onError: (throwable: Throwable) -> Unit,
        onFirstCall: (model: Response<T>) -> Unit,
        onSecondCall: (model: Response<U>) -> Unit
) {
    Maybe.zip(
            firstCall.subscribeOn(ioThreadScheduler),
            secondCall.subscribeOn(ioThreadScheduler),
            { firstResponse: Response<T>, secondResponse: Response<U> ->
                Pair(firstResponse, secondResponse)
            }
    ).observeOn(mainThreadScheduler)
            .subscribe({
                onFirstCall(it.first)
                onSecondCall(it.second)
            }, {
                onError(it)
            }).addTo(this)
}

fun <T, U> CompositeDisposable.parallelApiCall(
        firstCall: Maybe<Response<T>>,
        secondCall: Maybe<Response<U>>,
        firstResult: MutableLiveData<RetrofitResult<T>>,
        secondResult: MutableLiveData<RetrofitResult<U>>,
        onError: (throwable: Throwable) -> Unit
) {
    Maybe.zip(
            firstCall.subscribeOn(ioThreadScheduler),
            secondCall.subscribeOn(ioThreadScheduler),
            { firstResponse: Response<T>, secondResponse: Response<U> ->
                Pair(firstResponse, secondResponse)
            }
    ).observeOn(mainThreadScheduler)
            .subscribe({
                firstResult.subscribe(it.first)
                secondResult.subscribe(it.second)
            }, {
                onError(it)
            }).addTo(this)
}

fun <T, U> CompositeDisposable.parallelApiCall(
        firstCall: Maybe<Response<T>>,
        secondCall: Maybe<Response<U>>,
        firstResult: MutableLiveData<RetrofitResult<T>>,
        secondResult: MutableLiveData<RetrofitResult<U>>
) {
    Maybe.zip(
            firstCall.subscribeOn(ioThreadScheduler),
            secondCall.subscribeOn(ioThreadScheduler),
            { firstResponse: Response<T>, secondResponse: Response<U> ->
                Pair(firstResponse, secondResponse)
            }
    ).observeOn(mainThreadScheduler)
            .subscribe({
                firstResult.subscribe(it.first)
                secondResult.subscribe(it.second)
            }, {
                firstResult.callError(it)
                secondResult.callError(it)
            }).addTo(this)
}