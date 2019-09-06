package com.crazylegend.kotlinextensions.rx

import io.reactivex.Maybe
import io.reactivex.MaybeEmitter
import java.util.concurrent.Callable
import java.util.concurrent.Future


/**
 * Created by hristijan on 3/1/19 to long live and prosper !
 */


fun <T> maybe(block: (MaybeEmitter<T>) -> Unit): Maybe<T> = Maybe.create(block)

fun <T> deferredMaybe(block: () -> Maybe<T>): Maybe<T> = Maybe.defer(block)

fun <T : Any> emptyMaybe(): Maybe<T> = Maybe.empty()

fun <T> maybeOf(item: T?): Maybe<T> = item.toMaybe()

fun <T> maybeFrom(block: () -> T): Maybe<T> = Maybe.fromCallable(block)

fun <T> T?.toMaybe(): Maybe<T> = if (this != null) {
    Maybe.just(this)
} else {
    Maybe.empty<T>()
}

fun <T : Any> Future<T>.toMaybe(): Maybe<T> = Maybe.fromFuture(this)
fun <T : Any> Callable<T>.toMaybe(): Maybe<T> = Maybe.fromCallable(this)
fun <T : Any> (() -> T).toMaybe(): Maybe<T> = Maybe.fromCallable(this)
fun <T> Throwable.toMaybe(): Maybe<T> = Maybe.error(this)
@JvmName("toMaybeFromThrowable")
fun <T> (() -> Throwable).toMaybe(): Maybe<T> = Maybe.error(this)

