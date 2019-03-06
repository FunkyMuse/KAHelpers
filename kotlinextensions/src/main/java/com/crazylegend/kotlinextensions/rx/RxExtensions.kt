package com.crazylegend.kotlinextensions.rx

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers


/**
 * Created by hristijan on 3/5/19 to long live and prosper !
 */


fun <T : Any> Observable<T>.joinToString(
    separator: String? = null,
    prefix: String? = null,
    postfix: String? = null
): Single<String> = collect(
    { StringBuilder(prefix ?: "") })
{ builder: StringBuilder, next: T ->
    builder.append(if (builder.length == prefix?.length ?: 0) "" else separator ?: "").append(next)
}
    .map { it.append(postfix ?: "").toString() }


fun <T : Any> Observable<T>.withIndex(): Observable<IndexedValue<T>> =
    zipWith(Observable.range(0, Int.MAX_VALUE), BiFunction { value, index -> IndexedValue(index, value) })


private val mainThread = AndroidSchedulers.mainThread()
private val newThread = Schedulers.newThread()
private val ioThread = Schedulers.io()
/**
 * observe on main thread
 * subscribe on new thread
 * unsubsidised on error and on complete and removes the need to handle it afterwards
 * @usage
 * someObservable
 *  .runSafeOnMain()
 *  .subscribe({}, {])
 */
fun <T> Observable<T>.runSafeOnMain(): Observable<T> =
    observeOn(mainThread)
        .subscribeOn(newThread)
        .doOnError { unsubscribeOn(newThread) }
        .doOnComplete { unsubscribeOn(newThread) }

fun <T> Observable<T>.runSafeOnIO(): Observable<T> =
    observeOn(ioThread)
        .subscribeOn(newThread)
        .doOnError { unsubscribeOn(newThread) }
        .doOnComplete { unsubscribeOn(newThread) }

fun <T> Flowable<T>.runSafeOnMain(): Flowable<T> =
    observeOn(mainThread)
        .subscribeOn(newThread)
        .doOnError { unsubscribeOn(newThread) }
        .doOnComplete { unsubscribeOn(newThread) }

fun <T> Flowable<T>.runSafeOnIO(): Flowable<T> =
    observeOn(ioThread)
        .subscribeOn(newThread)
        .doOnError { unsubscribeOn(newThread) }
        .doOnComplete { unsubscribeOn(newThread) }

fun <T> Single<T>.runSafeOnMain(): Single<T> =
    observeOn(mainThread)
        .subscribeOn(newThread)
        .doOnError { unsubscribeOn(newThread) }
        .doOnSuccess { unsubscribeOn(newThread) }

fun <T> Single<T>.runSafeOnIO(): Single<T> =
    observeOn(ioThread)
        .subscribeOn(newThread)
        .doOnError { unsubscribeOn(newThread) }
        .doOnSuccess { unsubscribeOn(newThread) }

fun Completable.runSafeOnMain(): Completable =
    observeOn(mainThread)
        .subscribeOn(newThread)
        .doOnError { unsubscribeOn(newThread) }
        .doOnComplete { unsubscribeOn(newThread) }

fun Completable.runSafeOnIO(): Completable =
    observeOn(ioThread)
        .subscribeOn(newThread)
        .doOnError { unsubscribeOn(newThread) }
        .doOnComplete { unsubscribeOn(newThread) }

fun <T> Maybe<T>.runSafeOnMain(): Maybe<T> =
    observeOn(mainThread)
        .subscribeOn(newThread)
        .doOnError { unsubscribeOn(newThread) }
        .doOnSuccess { unsubscribeOn(newThread) }

fun <T> Maybe<T>.runSafeOnIO(): Maybe<T> =
    observeOn(ioThread)
        .subscribeOn(newThread)
        .doOnError { unsubscribeOn(newThread) }
        .doOnSuccess { unsubscribeOn(newThread) }

