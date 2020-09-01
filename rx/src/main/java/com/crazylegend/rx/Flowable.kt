package com.crazylegend.rx

import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableEmitter
import io.reactivex.rxjava3.functions.*


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


fun <First, Second, Res> Flowable<First>.zipWith(second: Flowable<Second>, zipFun: (First, Second) -> Res): Flowable<Res> =
        zipWith(second, BiFunction { t1, t2 -> zipFun(t1, t2) })

fun <First, Second> Flowable<First>.zipWith(second: Flowable<Second>): Flowable<Pair<First, Second>> =
        zipWith(second) { t1, t2 -> Pair(t1, t2) }

fun <T, R> Flowable<T>.mapSelf(mapper: T.() -> R) = map { it.mapper() }


fun <First, Second> zip(first: Flowable<First>, second: Flowable<Second>): Flowable<Pair<First, Second>> =
        first.zipWith(second)

fun <First, Second, Res> zip(
        first: Flowable<First>,
        second: Flowable<Second>,
        zipFun: (First, Second) -> Res
): Flowable<Res> =
        first.zipWith(second, zipFun)

fun <First, Second, Third> zip(
        first: Flowable<First>,
        second: Flowable<Second>,
        third: Flowable<Third>
): Flowable<Triple<First, Second, Third>> = zip(first, second, third) { t1, t2, t3 -> Triple(t1, t2, t3) }

fun <First, Second, Third, Res> zip(
        first: Flowable<First>,
        second: Flowable<Second>,
        third: Flowable<Third>,
        zipFun: (First, Second, Third) -> Res
): Flowable<Res> = Flowable.zip(first, second, third, Function3(zipFun))

fun <First, Second, Third, Fourth, Res> zip(
        first: Flowable<First>,
        second: Flowable<Second>,
        third: Flowable<Third>,
        fourth: Flowable<Fourth>,
        zipFun: (First, Second, Third, Fourth) -> Res
): Flowable<Res> = Flowable.zip(first, second, third, fourth, Function4(zipFun))

fun <First, Second, Third, Fourth, Fifth, Res> zip(
        first: Flowable<First>,
        second: Flowable<Second>,
        third: Flowable<Third>,
        fourth: Flowable<Fourth>,
        fifth: Flowable<Fifth>,
        zipFun: (First, Second, Third, Fourth, Fifth) -> Res
): Flowable<Res> =
        Flowable.zip(first, second, third, fourth, fifth, Function5(zipFun))

fun <First, Second, Third, Fourth, Fifth, Sixth, Res> zip(
        first: Flowable<First>,
        second: Flowable<Second>,
        third: Flowable<Third>,
        fourth: Flowable<Fourth>,
        fifth: Flowable<Fifth>,
        sixth: Flowable<Sixth>,
        zipFun: (First, Second, Third, Fourth, Fifth, Sixth) -> Res
): Flowable<Res> = Flowable.zip(first, second, third, fourth, fifth, sixth, Function6(zipFun))

fun <First, Second, Third, Fourth, Fifth, Sixth, Seventh, Res> zip(
        first: Flowable<First>,
        second: Flowable<Second>,
        third: Flowable<Third>,
        fourth: Flowable<Fourth>,
        fifth: Flowable<Fifth>,
        sixth: Flowable<Sixth>,
        seventh: Flowable<Seventh>,
        zipFun: (First, Second, Third, Fourth, Fifth, Sixth, Seventh) -> Res
): Flowable<Res> = Flowable.zip(first, second, third, fourth, fifth, sixth, seventh, Function7(zipFun))

fun <First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eight, Res> zip(
        first: Flowable<First>,
        second: Flowable<Second>,
        third: Flowable<Third>,
        fourth: Flowable<Fourth>,
        fifth: Flowable<Fifth>,
        sixth: Flowable<Sixth>,
        seventh: Flowable<Seventh>,
        eight: Flowable<Eight>,
        zipFun: (First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eight) -> Res
): Flowable<Res> = Flowable.zip(first, second, third, fourth, fifth, sixth, seventh, eight, Function8(zipFun))

fun <First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eight, Ninth, Res> zip(
        first: Flowable<First>,
        second: Flowable<Second>,
        third: Flowable<Third>,
        fourth: Flowable<Fourth>,
        fifth: Flowable<Fifth>,
        sixth: Flowable<Sixth>,
        seventh: Flowable<Seventh>,
        eight: Flowable<Eight>,
        ninth: Flowable<Ninth>,
        zipFun: (First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eight, Ninth) -> Res
): Flowable<Res> =
        Flowable.zip(first, second, third, fourth, fifth, sixth, seventh, eight, ninth, Function9(zipFun))

/**
 * COMBINE
 */

fun <First, Second, Res> combine(
        first: Flowable<First>,
        second: Flowable<Second>,
        combFunc: (First, Second) -> Res
): Flowable<Res> = Flowable.combineLatest(first, second, BiFunction(combFunc))

fun <First, Second> combine(
        first: Flowable<First>,
        second: Flowable<Second>
): Flowable<Pair<First, Second>> = combine(first, second) { t1, t2 -> Pair(t1, t2) }

fun <First, Second, Third> combine(
        first: Flowable<First>,
        second: Flowable<Second>,
        third: Flowable<Third>
): Flowable<Triple<First, Second, Third>> =
        combine(first, second, third) { t1, t2, t3 -> Triple(t1, t2, t3) }

fun <First, Second, Third, Res> combine(
        first: Flowable<First>,
        second: Flowable<Second>,
        third: Flowable<Third>,
        combFunc: (First, Second, Third) -> Res
): Flowable<Res> = Flowable.combineLatest(first, second, third, Function3(combFunc))

fun <First, Second, Third, Fourth, Res> combine(
        first: Flowable<First>,
        second: Flowable<Second>,
        third: Flowable<Third>,
        fourth: Flowable<Fourth>,
        combFunc: (First, Second, Third, Fourth) -> Res
): Flowable<Res> =
        Flowable.combineLatest(first, second, third, fourth, Function4(combFunc))

fun <First, Second, Third, Fourth, Fifth, Res> combine(
        first: Flowable<First>,
        second: Flowable<Second>,
        third: Flowable<Third>,
        fourth: Flowable<Fourth>,
        fifth: Flowable<Fifth>,
        combFunc: (First, Second, Third, Fourth, Fifth) -> Res
): Flowable<Res> = Flowable.combineLatest(first, second, third, fourth, fifth, Function5(combFunc))

fun <First, Second, Third, Fourth, Fifth, Sixth, Res> combine(
        first: Flowable<First>,
        second: Flowable<Second>,
        third: Flowable<Third>,
        fourth: Flowable<Fourth>,
        fifth: Flowable<Fifth>,
        six: Flowable<Sixth>,
        combFunc: (First, Second, Third, Fourth, Fifth, Sixth) -> Res
): Flowable<Res> =
        Flowable.combineLatest(first, second, third, fourth, fifth, six, Function6(combFunc))

fun <First, Second, Third, Fourth, Fifth, Sixth, Seventh, Res> combine(
        first: Flowable<First>,
        second: Flowable<Second>,
        third: Flowable<Third>,
        fourth: Flowable<Fourth>,
        fifth: Flowable<Fifth>,
        six: Flowable<Sixth>,
        seventh: Flowable<Seventh>,
        combFunc: (First, Second, Third, Fourth, Fifth, Sixth, Seventh) -> Res
): Flowable<Res> =
        Flowable.combineLatest(first, second, third, fourth, fifth, six, seventh, Function7(combFunc))

fun <First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth, Res> combine(
        first: Flowable<First>,
        second: Flowable<Second>,
        third: Flowable<Third>,
        fourth: Flowable<Fourth>,
        fifth: Flowable<Fifth>,
        six: Flowable<Sixth>,
        seventh: Flowable<Seventh>,
        eighth: Flowable<Eighth>,
        combFun: (First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth) -> Res
): Flowable<Res> =
        Flowable.combineLatest(first, second, third, fourth, fifth, six, seventh, eighth, Function8(combFun))

fun <First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth, Ninth, Res> combine(
        first: Flowable<First>,
        second: Flowable<Second>,
        third: Flowable<Third>,
        fourth: Flowable<Fourth>,
        fifth: Flowable<Fifth>,
        six: Flowable<Sixth>,
        seventh: Flowable<Seventh>,
        eighth: Flowable<Eighth>,
        ninth: Flowable<Ninth>,
        combFun: (First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth, Ninth) -> Res
): Flowable<Res> =
        Flowable.combineLatest(first, second, third, fourth, fifth, six, seventh, eighth, ninth, Function9(combFun))