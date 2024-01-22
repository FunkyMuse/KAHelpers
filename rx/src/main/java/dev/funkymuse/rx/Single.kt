package dev.funkymuse.rx

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleEmitter
import java.util.concurrent.Callable
import java.util.concurrent.Future




fun <T : Any> single(block: (SingleEmitter<T>) -> Unit): Single<T> = Single.create(block)

fun <T : Any> deferredSingle(block: () -> Single<T>): Single<T> = Single.defer(block)

fun <T : Any> singleOf(item: T) = item.toSingle()

fun <T : Any> singleFrom(block: () -> T): Single<T> = Single.fromCallable(block)

fun <T : Any> T.toSingle(): Single<T> = Single.just(this)

fun <T : Any> Future<T>.toSingle(): Single<T> = Single.fromFuture(this)
fun <T : Any> Callable<T>.toSingle(): Single<T> = Single.fromCallable(this)
fun <T : Any> (() -> T).toSingle(): Single<T> = Single.fromCallable(this)
fun <T : Any> Throwable.toSingle(): Single<T> = Single.error(this)

@JvmName("toSingleFromThrowable")
fun <T : Any> (() -> Throwable).toSingle(): Single<T> = Single.error(this)


