package com.crazylegend.kotlinextensions.rx

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableEmitter


/**
 * Created by hristijan on 3/1/19 to long live and prosper !
 */


fun completable(block: (CompletableEmitter) -> Unit): Completable = Completable.create(block)

fun deferredCompletable(block: () -> Completable): Completable = Completable.defer(block)

fun completableOf(action: () -> Unit): Completable = Completable.fromAction(action)

fun Throwable.toCompletable(): Completable = Completable.error(this)

fun (() -> Throwable).toCompletable(): Completable = Completable.error(this)