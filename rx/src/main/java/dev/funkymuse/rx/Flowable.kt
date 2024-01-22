package dev.funkymuse.rx

import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableEmitter
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.functions.Function3
import io.reactivex.rxjava3.functions.Function4
import io.reactivex.rxjava3.functions.Function5
import io.reactivex.rxjava3.functions.Function6
import io.reactivex.rxjava3.functions.Function7
import io.reactivex.rxjava3.functions.Function8
import io.reactivex.rxjava3.functions.Function9


fun <T : Any> flowable(
    backPressureStrategy: BackpressureStrategy = BackpressureStrategy.LATEST,
    block: (FlowableEmitter<T>) -> Unit
): Flowable<T> = Flowable.create(block, backPressureStrategy)

fun <T : Any> deferredFlowable(block: () -> Flowable<T>): Flowable<T> = Flowable.defer(block)

fun <T : Any> emptyFlowable(): Flowable<T> = Flowable.empty()

fun <T : Any> flowableOf(item: T): Flowable<T> = Flowable.just(item)
fun <T : Any> flowableOf(vararg items: T): Flowable<T> = Flowable.fromIterable(items.toList())
fun <T : Any> flowableOf(items: Iterable<T>): Flowable<T> = Flowable.fromIterable(items)

@JvmName("flowableOfArray")
fun <T : Any> flowableOf(items: Array<T>): Flowable<T> = Flowable.fromArray(*items)

fun flowableOf(items: BooleanArray): Flowable<Boolean> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: ByteArray): Flowable<Byte> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: CharArray): Flowable<Char> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: DoubleArray): Flowable<Double> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: FloatArray): Flowable<Float> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: IntArray): Flowable<Int> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: LongArray): Flowable<Long> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: ShortArray): Flowable<Short> = Flowable.fromArray(*items.toTypedArray())

fun <T : Any> flowableFrom(block: () -> T): Flowable<T> = Flowable.fromCallable(block)

fun <T : Any> T.toFlowable(): Flowable<T> = Flowable.just(this)
fun <T : Any> Throwable.toFlowable(): Flowable<T> = Flowable.error(this)
fun <T : Any> (() -> Throwable).toFlowable(): Flowable<T> = Flowable.error(this)


fun <First : Any, Second : Any, Res : Any> Flowable<First>.zipWith(second: Flowable<Second>, zipFun: (First, Second) -> Res): Flowable<Res> =
    zipWith(second, BiFunction { t1, t2 -> zipFun(t1, t2) })

fun <First : Any, Second : Any> Flowable<First>.zipWith(second: Flowable<Second>): Flowable<Pair<First, Second>> =
    zipWith(second) { t1, t2 -> Pair(t1, t2) }

fun <T : Any, R : Any> Flowable<T>.mapSelf(mapper: T.() -> R) = map { it.mapper() }


fun <First : Any, Second : Any> zip(first: Flowable<First>, second: Flowable<Second>): Flowable<Pair<First, Second>> =
    first.zipWith(second)

fun <First : Any, Second : Any, Res : Any> zip(
    first: Flowable<First>,
    second: Flowable<Second>,
    zipFun: (First, Second) -> Res
): Flowable<Res> =
    first.zipWith(second, zipFun)

fun <First : Any, Second : Any, Third : Any> zip(
    first: Flowable<First>,
    second: Flowable<Second>,
    third: Flowable<Third>
): Flowable<Triple<First, Second, Third>> = zip(first, second, third) { t1, t2, t3 -> Triple(t1, t2, t3) }

fun <First : Any, Second : Any, Third : Any, Res : Any> zip(
    first: Flowable<First>,
    second: Flowable<Second>,
    third: Flowable<Third>,
    zipFun: (First, Second, Third) -> Res
): Flowable<Res> = Flowable.zip(first, second, third, Function3(zipFun))

fun <First : Any, Second : Any, Third : Any, Fourth : Any, Res : Any> zip(
    first: Flowable<First>,
    second: Flowable<Second>,
    third: Flowable<Third>,
    fourth: Flowable<Fourth>,
    zipFun: (First, Second, Third, Fourth) -> Res
): Flowable<Res> = Flowable.zip(first, second, third, fourth, Function4(zipFun))

fun <First : Any, Second : Any, Third : Any, Fourth : Any, Fifth : Any, Res : Any> zip(
    first: Flowable<First>,
    second: Flowable<Second>,
    third: Flowable<Third>,
    fourth: Flowable<Fourth>,
    fifth: Flowable<Fifth>,
    zipFun: (First, Second, Third, Fourth, Fifth) -> Res
): Flowable<Res> =
    Flowable.zip(first, second, third, fourth, fifth, Function5(zipFun))

fun <First : Any, Second : Any, Third : Any, Fourth : Any, Fifth : Any, Sixth : Any, Res : Any> zip(
    first: Flowable<First>,
    second: Flowable<Second>,
    third: Flowable<Third>,
    fourth: Flowable<Fourth>,
    fifth: Flowable<Fifth>,
    sixth: Flowable<Sixth>,
    zipFun: (First, Second, Third, Fourth, Fifth, Sixth) -> Res
): Flowable<Res> = Flowable.zip(first, second, third, fourth, fifth, sixth, Function6(zipFun))

fun <First : Any, Second : Any, Third : Any, Fourth : Any, Fifth : Any, Sixth : Any, Seventh : Any, Res : Any> zip(
    first: Flowable<First>,
    second: Flowable<Second>,
    third: Flowable<Third>,
    fourth: Flowable<Fourth>,
    fifth: Flowable<Fifth>,
    sixth: Flowable<Sixth>,
    seventh: Flowable<Seventh>,
    zipFun: (First, Second, Third, Fourth, Fifth, Sixth, Seventh) -> Res
): Flowable<Res> = Flowable.zip(first, second, third, fourth, fifth, sixth, seventh, Function7(zipFun))

fun <First : Any, Second : Any, Third : Any, Fourth : Any, Fifth : Any, Sixth : Any, Seventh : Any, Eight : Any, Res : Any> zip(
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

fun <First : Any, Second : Any, Third : Any, Fourth : Any, Fifth : Any, Sixth : Any, Seventh : Any, Eight : Any, Ninth : Any, Res : Any> zip(
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

fun <First : Any, Second : Any, Res : Any> combine(
    first: Flowable<First>,
    second: Flowable<Second>,
    combFunc: (First, Second) -> Res
): Flowable<Res> = Flowable.combineLatest(first, second, BiFunction(combFunc))

fun <First : Any, Second : Any> combine(
    first: Flowable<First>,
    second: Flowable<Second>
): Flowable<Pair<First, Second>> = combine(first, second) { t1, t2 -> Pair(t1, t2) }

fun <First : Any, Second : Any, Third : Any> combine(
    first: Flowable<First>,
    second: Flowable<Second>,
    third: Flowable<Third>
): Flowable<Triple<First, Second, Third>> =
    combine(first, second, third) { t1, t2, t3 -> Triple(t1, t2, t3) }

fun <First : Any, Second : Any, Third : Any, Res : Any> combine(
    first: Flowable<First>,
    second: Flowable<Second>,
    third: Flowable<Third>,
    combFunc: (First, Second, Third) -> Res
): Flowable<Res> = Flowable.combineLatest(first, second, third, Function3(combFunc))

fun <First : Any, Second : Any, Third : Any, Fourth : Any, Res : Any> combine(
    first: Flowable<First>,
    second: Flowable<Second>,
    third: Flowable<Third>,
    fourth: Flowable<Fourth>,
    combFunc: (First, Second, Third, Fourth) -> Res
): Flowable<Res> =
    Flowable.combineLatest(first, second, third, fourth, Function4(combFunc))

fun <First : Any, Second : Any, Third : Any, Fourth : Any, Fifth : Any, Res : Any> combine(
    first: Flowable<First>,
    second: Flowable<Second>,
    third: Flowable<Third>,
    fourth: Flowable<Fourth>,
    fifth: Flowable<Fifth>,
    combFunc: (First, Second, Third, Fourth, Fifth) -> Res
): Flowable<Res> = Flowable.combineLatest(first, second, third, fourth, fifth, Function5(combFunc))

fun <First : Any, Second : Any, Third : Any, Fourth : Any, Fifth : Any, Sixth : Any, Res : Any> combine(
    first: Flowable<First>,
    second: Flowable<Second>,
    third: Flowable<Third>,
    fourth: Flowable<Fourth>,
    fifth: Flowable<Fifth>,
    six: Flowable<Sixth>,
    combFunc: (First, Second, Third, Fourth, Fifth, Sixth) -> Res
): Flowable<Res> =
    Flowable.combineLatest(first, second, third, fourth, fifth, six, Function6(combFunc))

fun <First : Any, Second : Any, Third : Any, Fourth : Any, Fifth : Any, Sixth : Any, Seventh : Any, Res : Any> combine(
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

fun <First : Any, Second : Any, Third : Any, Fourth : Any, Fifth : Any, Sixth : Any, Seventh : Any, Eighth : Any, Res : Any> combine(
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

fun <First : Any, Second : Any, Third : Any, Fourth : Any, Fifth : Any, Sixth : Any, Seventh : Any, Eighth : Any, Ninth : Any, Res : Any> combine(
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