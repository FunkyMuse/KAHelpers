package com.crazylegend.kotlinextensions.rx

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter


/**
 * Created by hristijan on 3/1/19 to long live and prosper !
 */

fun <T> flowable(backPressureStrategy: BackpressureStrategy = BackpressureStrategy.LATEST,
                 block: (FlowableEmitter<T>) -> Unit): Flowable<T> = Flowable.create(block, backPressureStrategy)

fun <T> deferredFlowable(block: () -> Flowable<T>): Flowable<T> = Flowable.defer(block)

fun <T> emptyFlowable(): Flowable<T> = Flowable.empty()

fun <T> flowableOf(item: T): Flowable<T> = Flowable.just(item)
fun <T> flowableOf(vararg items: T): Flowable<T> = Flowable.fromIterable(items.toList())
fun <T> flowableOf(items: Iterable<T>): Flowable<T> = Flowable.fromIterable(items)

@JvmName("flowableOfArray")
fun <T> flowableOf(items: Array<T>): Flowable<T> = Flowable.fromArray(*items)

fun flowableOf(items: BooleanArray): Flowable<Boolean> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: ByteArray): Flowable<Byte> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: CharArray): Flowable<Char> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: DoubleArray): Flowable<Double> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: FloatArray): Flowable<Float> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: IntArray): Flowable<Int> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: LongArray): Flowable<Long> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: ShortArray): Flowable<Short> = Flowable.fromArray(*items.toTypedArray())

fun <T> flowableFrom(block: () -> T): Flowable<T> = Flowable.fromCallable(block)

fun <T> T.toFlowable(): Flowable<T> = Flowable.just(this)
fun <T> Throwable.toFlowable(): Flowable<T> = Flowable.error(this)
fun <T> (() -> Throwable).toFlowable(): Flowable<T> = Flowable.error(this)