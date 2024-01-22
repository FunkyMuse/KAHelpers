package dev.funkymuse.rx

import io.reactivex.rxjava3.subjects.AsyncSubject
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.CompletableSubject
import io.reactivex.rxjava3.subjects.MaybeSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.ReplaySubject
import io.reactivex.rxjava3.subjects.SingleSubject
import io.reactivex.rxjava3.subjects.UnicastSubject


fun <T> AsyncSubject(): AsyncSubject<T> = AsyncSubject.create()

fun <T> BehaviorSubject(): BehaviorSubject<T> = BehaviorSubject.create()

fun <T : Any> BehaviorSubject(default: T): BehaviorSubject<T> = BehaviorSubject.createDefault(default)

fun CompletableSubject(): CompletableSubject = CompletableSubject.create()

fun <T> MaybeSubject(): MaybeSubject<T> = MaybeSubject.create()

fun <T> PublishSubject(): PublishSubject<T> = PublishSubject.create()

fun <T> ReplaySubject(): ReplaySubject<T> = ReplaySubject.create()

fun <T> SingleSubject(): SingleSubject<T> = SingleSubject.create()

fun <T> UnicastSubject(): UnicastSubject<T> = UnicastSubject.create()

