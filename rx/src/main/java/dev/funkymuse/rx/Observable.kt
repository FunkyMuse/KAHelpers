package dev.funkymuse.rx

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.functions.Function3
import io.reactivex.rxjava3.functions.Function4
import io.reactivex.rxjava3.functions.Function5
import io.reactivex.rxjava3.functions.Function6
import io.reactivex.rxjava3.functions.Function7
import io.reactivex.rxjava3.functions.Function8
import io.reactivex.rxjava3.functions.Function9


fun <T : Any> observable(block: (ObservableEmitter<T>) -> Unit): Observable<T> = Observable.create(block)

fun <T : Any> deferredObservable(block: () -> Observable<T>): Observable<T> = Observable.defer(block)

fun <T : Any> emptyObservable(): Observable<T> = Observable.empty()

fun <T : Any> observableOf(item: T): Observable<T> = Observable.just(item)
fun <T : Any> observableOf(vararg items: T): Observable<T> = Observable.fromIterable(items.toList())
fun <T : Any> observableOf(items: Iterable<T>): Observable<T> = Observable.fromIterable(items)

@JvmName("observableOfArray")
fun <T : Any> observableOf(items: Array<T>): Observable<T> = Observable.fromArray(*items)

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

fun <T : Any> observableFrom(block: () -> T): Observable<T> = Observable.fromCallable(block)

fun <T : Any> T.toObservable(): Observable<T> = Observable.just(this)
fun <T : Any> Throwable.toObservable(): Observable<T> = Observable.error(this)
fun <T : Any> (() -> Throwable).toObservable(): Observable<T> = Observable.error(this)

fun <First : Any, Second : Any, Res : Any> Observable<First>.zipWith(second: Observable<Second>, zipFun: (First, Second) -> Res): Observable<Res> =
        zipWith(second, BiFunction { t1, t2 -> zipFun(t1, t2) })

fun <First : Any, Second : Any> Observable<First>.zipWith(second: Observable<Second>): Observable<Pair<First, Second>> =
        zipWith(second) { t1, t2 -> Pair(t1, t2) }

fun <T : Any, R : Any> Observable<T>.mapSelf(mapper: T.() -> R) = map { it.mapper() }


fun <First : Any, Second : Any> zip(first: Observable<First>, second: Observable<Second>): Observable<Pair<First, Second>> =
        first.zipWith(second)

fun <First : Any, Second : Any, Res : Any> zip(
        first: Observable<First>,
        second: Observable<Second>,
        zipFun: (First, Second) -> Res
): Observable<Res> =
        first.zipWith(second, zipFun)

fun <First : Any, Second : Any, Third : Any,> zip(
        first: Observable<First>,
        second: Observable<Second>,
        third: Observable<Third>
): Observable<Triple<First, Second, Third>> = zip(first, second, third) { t1, t2, t3 -> Triple(t1, t2, t3) }

fun <First : Any, Second : Any, Third : Any, Res : Any> zip(
        first: Observable<First>,
        second: Observable<Second>,
        third: Observable<Third>,
        zipFun: (First, Second, Third) -> Res
): Observable<Res> = Observable.zip(first, second, third, Function3(zipFun))

fun <First : Any, Second : Any, Third : Any, Fourth :Any, Res : Any> zip(
        first: Observable<First>,
        second: Observable<Second>,
        third: Observable<Third>,
        fourth: Observable<Fourth>,
        zipFun: (First, Second, Third, Fourth) -> Res
): Observable<Res> = Observable.zip(first, second, third, fourth, Function4(zipFun))

fun <First : Any, Second : Any, Third : Any, Fourth :Any, Fifth : Any, Res : Any> zip(
        first: Observable<First>,
        second: Observable<Second>,
        third: Observable<Third>,
        fourth: Observable<Fourth>,
        fifth: Observable<Fifth>,
        zipFun: (First, Second, Third, Fourth, Fifth) -> Res
): Observable<Res> =
        Observable.zip(first, second, third, fourth, fifth, Function5(zipFun))

fun <First : Any, Second : Any, Third : Any, Fourth : Any, Fifth : Any, Sixth : Any, Res : Any> zip(
        first: Observable<First>,
        second: Observable<Second>,
        third: Observable<Third>,
        fourth: Observable<Fourth>,
        fifth: Observable<Fifth>,
        sixth: Observable<Sixth>,
        zipFun: (First, Second, Third, Fourth, Fifth, Sixth) -> Res
): Observable<Res> = Observable.zip(first, second, third, fourth, fifth, sixth, Function6(zipFun))

fun <First : Any, Second : Any, Third : Any, Fourth : Any, Fifth : Any, Sixth : Any, Seventh : Any, Res : Any> zip(
        first: Observable<First>,
        second: Observable<Second>,
        third: Observable<Third>,
        fourth: Observable<Fourth>,
        fifth: Observable<Fifth>,
        sixth: Observable<Sixth>,
        seventh: Observable<Seventh>,
        zipFun: (First, Second, Third, Fourth, Fifth, Sixth, Seventh) -> Res
): Observable<Res> = Observable.zip(first, second, third, fourth, fifth, sixth, seventh, Function7(zipFun))

fun <First : Any, Second : Any, Third : Any, Fourth : Any, Fifth : Any, Sixth : Any, Seventh : Any, Eight : Any, Res : Any> zip(
        first: Observable<First>,
        second: Observable<Second>,
        third: Observable<Third>,
        fourth: Observable<Fourth>,
        fifth: Observable<Fifth>,
        sixth: Observable<Sixth>,
        seventh: Observable<Seventh>,
        eight: Observable<Eight>,
        zipFun: (First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eight) -> Res
): Observable<Res> = Observable.zip(first, second, third, fourth, fifth, sixth, seventh, eight, Function8(zipFun))

fun <First : Any, Second : Any, Third : Any, Fourth : Any, Fifth : Any, Sixth : Any, Seventh : Any, Eight : Any, Ninth : Any, Res : Any> zip(
        first: Observable<First>,
        second: Observable<Second>,
        third: Observable<Third>,
        fourth: Observable<Fourth>,
        fifth: Observable<Fifth>,
        sixth: Observable<Sixth>,
        seventh: Observable<Seventh>,
        eight: Observable<Eight>,
        ninth: Observable<Ninth>,
        zipFun: (First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eight, Ninth) -> Res
): Observable<Res> =
        Observable.zip(first, second, third, fourth, fifth, sixth, seventh, eight, ninth, Function9(zipFun))

/**
 * COMBINE
 */

fun <First : Any, Second : Any, Res : Any> combine(
        first: Observable<First>,
        second: Observable<Second>,
        combFunc: (First, Second) -> Res
): Observable<Res> = Observable.combineLatest(first, second, BiFunction(combFunc))

fun <First : Any, Second : Any> combine(
        first: Observable<First>,
        second: Observable<Second>
): Observable<Pair<First, Second>> = combine(first, second) { t1, t2 -> Pair(t1, t2) }

fun <First : Any, Second : Any, Third : Any> combine(
        first: Observable<First>,
        second: Observable<Second>,
        third: Observable<Third>
): Observable<Triple<First, Second, Third>> =
        combine(first, second, third) { t1, t2, t3 -> Triple(t1, t2, t3) }

fun <First : Any, Second : Any, Third : Any, Res : Any> combine(
        first: Observable<First>,
        second: Observable<Second>,
        third: Observable<Third>,
        combFunc: (First, Second, Third) -> Res
): Observable<Res> = Observable.combineLatest(first, second, third, Function3(combFunc))

fun <First : Any, Second : Any, Third : Any, Fourth :Any, Res : Any> combine(
        first: Observable<First>,
        second: Observable<Second>,
        third: Observable<Third>,
        fourth: Observable<Fourth>,
        combFunc: (First, Second, Third, Fourth) -> Res
): Observable<Res> =
        Observable.combineLatest(first, second, third, fourth, Function4(combFunc))

fun <First : Any, Second : Any, Third : Any, Fourth :Any, Fifth : Any, Res : Any> combine(
        first: Observable<First>,
        second: Observable<Second>,
        third: Observable<Third>,
        fourth: Observable<Fourth>,
        fifth: Observable<Fifth>,
        combFunc: (First, Second, Third, Fourth, Fifth) -> Res
): Observable<Res> = Observable.combineLatest(first, second, third, fourth, fifth, Function5(combFunc))

fun <First : Any, Second : Any, Third : Any, Fourth : Any, Fifth : Any, Sixth : Any, Res : Any> combine(
        first: Observable<First>,
        second: Observable<Second>,
        third: Observable<Third>,
        fourth: Observable<Fourth>,
        fifth: Observable<Fifth>,
        six: Observable<Sixth>,
        combFunc: (First, Second, Third, Fourth, Fifth, Sixth) -> Res
): Observable<Res> =
        Observable.combineLatest(first, second, third, fourth, fifth, six, Function6(combFunc))

fun <First : Any, Second : Any, Third : Any, Fourth : Any, Fifth : Any, Sixth : Any, Seventh : Any, Res : Any> combine(
        first: Observable<First>,
        second: Observable<Second>,
        third: Observable<Third>,
        fourth: Observable<Fourth>,
        fifth: Observable<Fifth>,
        six: Observable<Sixth>,
        seventh: Observable<Seventh>,
        combFunc: (First, Second, Third, Fourth, Fifth, Sixth, Seventh) -> Res
): Observable<Res> =
        Observable.combineLatest(first, second, third, fourth, fifth, six, seventh, Function7(combFunc))

fun <First : Any, Second : Any, Third : Any, Fourth : Any, Fifth : Any, Sixth  : Any, Seventh :Any, Eighth : Any, Res : Any> combine(
        first: Observable<First>,
        second: Observable<Second>,
        third: Observable<Third>,
        fourth: Observable<Fourth>,
        fifth: Observable<Fifth>,
        six: Observable<Sixth>,
        seventh: Observable<Seventh>,
        eighth: Observable<Eighth>,
        combFun: (First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth) -> Res
): Observable<Res> =
        Observable.combineLatest(first, second, third, fourth, fifth, six, seventh, eighth, Function8(combFun))

fun <First : Any, Second : Any, Third : Any, Fourth : Any, Fifth : Any, Sixth  : Any, Seventh :Any, Eighth : Any, Ninth : Any, Res : Any> combine(
        first: Observable<First>,
        second: Observable<Second>,
        third: Observable<Third>,
        fourth: Observable<Fourth>,
        fifth: Observable<Fifth>,
        six: Observable<Sixth>,
        seventh: Observable<Seventh>,
        eighth: Observable<Eighth>,
        ninth: Observable<Ninth>,
        combFun: (First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth, Ninth) -> Res
): Observable<Res> =
        Observable.combineLatest(first, second, third, fourth, fifth, six, seventh, eighth, ninth, Function9(combFun))