package com.crazylegend.kotlinextensions.rx

import io.reactivex.Observable
import io.reactivex.ObservableEmitter


/**
 * Created by hristijan on 3/1/19 to long live and prosper !
 */


fun <T> observable(block: (ObservableEmitter<T>) -> Unit): Observable<T> = Observable.create(block)

fun <T> deferredObservable(block: () -> Observable<T>): Observable<T> = Observable.defer(block)

fun <T> emptyObservable(): Observable<T> = Observable.empty()

fun <T> observableOf(item: T): Observable<T> = Observable.just(item)
fun <T> observableOf(vararg items: T): Observable<T> = Observable.fromIterable(items.toList())
fun <T> observableOf(items: Iterable<T>): Observable<T> = Observable.fromIterable(items)
@JvmName("observableOfArray")
fun <T> observableOf(items: Array<T>): Observable<T> = Observable.fromArray(*items)

fun observableOf(items: BooleanArray): Observable<Boolean> =
    Observable.fromArray(*items.toTypedArray())

fun observableOf(items: ByteArray): Observable<Byte> = Observable.fromArray(*items.toTypedArray())
fun observableOf(items: CharArray): Observable<Char> = Observable.fromArray(*items.toTypedArray())
fun observableOf(items: DoubleArray): Observable<Double> =
    Observable.fromArray(*items.toTypedArray())

fun observableOf(items: FloatArray): Observable<Float> = Observable.fromArray(*items.toTypedArray())
fun observableOf(items: IntArray): Observable<Int> = Observable.fromArray(*items.toTypedArray())
fun observableOf(items: LongArray): Observable<Long> = Observable.fromArray(*items.toTypedArray())
fun observableOf(items: ShortArray): Observable<Short> = Observable.fromArray(*items.toTypedArray())

fun <T> observableFrom(block: () -> T): Observable<T> = Observable.fromCallable(block)

fun <T> T.toObservable(): Observable<T> = Observable.just(this)
fun <T> Throwable.toObservable(): Observable<T> = Observable.error(this)
fun <T> (() -> Throwable).toObservable(): Observable<T> = Observable.error(this)