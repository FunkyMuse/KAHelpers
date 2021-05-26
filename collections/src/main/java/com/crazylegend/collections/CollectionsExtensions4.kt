package com.crazylegend.collections

import android.os.Build
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

/**
 * Created by funkymuse on 5/26/21 to long live and prosper !
 */


fun <E> MutableList<E>.removeInRange(position: Int, count: Int) {
    this.removeAll(drop(position).take(count))
}

fun <E> ArrayList<E>.removeInRange(position: Int, count: Int) {
    this.removeAll(drop(position).take(count))
}

fun <T> List<T>?.sizeOrZero(): Int {
    return this?.size ?: 0
}

fun <T> List<T>?.orEmptyString(string: String): String {
    return if (this?.isEmpty() == true) "" else string
}

fun <T> flatten(vararg elements: List<T>): List<T> =
    elements.flatMap { it }

fun <T> flatten(list: List<List<T>>): List<T> =
    list.flatMap { it }

inline fun <reified T> union(l: List<T>, vararg elements: T): List<T> =
    flatten(l, elements.toList())

inline fun <reified T> union(l1: List<T>, l2: List<T>, vararg elements: T): List<T> =
    flatten(l1, l2, elements.toList())

inline fun <reified T> union(l1: List<T>, l2: List<T>, l3: List<T>, vararg elements: T): List<T> =
    flatten(l1, l2, l3, elements.toList())

inline fun <reified T> union(l1: List<T>, l2: List<T>, l3: List<T>, l4: List<T>, vararg elements: T): List<T> =
    flatten(l1, l2, l3, l4, elements.toList())

inline fun <reified T> union(l1: List<T>, l2: List<T>, l3: List<T>, l4: List<T>, l5: List<T>, vararg elements: T): List<T> =
    flatten(l1, l2, l3, l4, l5, elements.toList())

inline fun <T, R> Iterable<T>.mapToSet(transform: (T) -> R) =
    this.map(transform).toSet()

inline fun <T, R : Any> Iterable<T>.mapNotNullToSet(transform: (T) -> R?) =
    this.mapNotNull(transform).toSet()


fun <T> java.util.Comparator<T>.sort(list: MutableList<T>) {
    list.sortWith(this)
}


fun <T> listEqualsIgnoreOrder(list1: List<T>, list2: List<T>): Boolean {
    return HashSet(list1) == HashSet(list2)
}

inline fun <T> Array<T>.firstIndexed(
    filter: (index: Int, T) -> Boolean,
    onNext: (index: Int, T) -> Unit = { _, _ -> },
    onSuccess: () -> Unit = {},
    onFail: () -> Unit = {},
    onComplete: () -> Unit = {}
) {
    var isNull = true
    for ((index, element) in this.withIndex()) {
        if (filter(index, element)) {
            isNull = false
            onNext(index, element)
            break
        }
    }
    if (isNull) onFail() else onSuccess()
    onComplete()
}

inline fun <reified T> initArrayOf(size: Int, predicate: (index: Int) -> T): Array<T> {
    return arrayListOf<T>().also {
        for (index in 0 until size) {
            it.add(predicate(index))
        }
    }.toTypedArray()
}

inline fun MutableMap<String, Any>.transform(key: String, block: (Any?) -> String) {
    val t = get(key)
    this[key] = block.invoke(t)
}

inline fun <reified T : Any> MutableMap<String, Any>.makeTo(fromKey: String, toKey: String, block: (from: Any?) -> T) {
    val from = get(fromKey)
    this[toKey] = block.invoke(from)
}

fun <T, S> MutableList<T>.compose(
    mixList: List<S>,
    mixFilter: (T, S) -> Boolean,
    mixOperate: (T?, S) -> T,
    removeCannotMix: Boolean = true
) {
    val list1 = this
    val list2 = mutableListOf<S>().apply {
        addAll(mixList)
    }
    for (i in list1.size - 1 downTo 0) {
        run {
            val item1 = list1[i]
            for (j in list2.size - 1 downTo 0) {
                val item2 = list2[j]
                val mix = mixFilter(item1, item2)
                if (mix) {
                    list1[i] = mixOperate(item1, item2)
                    list2.removeAt(j)
                    return@run
                }
            }
            if (removeCannotMix) {
                list1.remove(item1)
            }
        }
    }
    list1.addAll(0, list2.map {
        mixOperate(null, it)
    })
}

inline fun <T> mutableListCreate(size: Int, create: (index: Int) -> T): MutableList<T> {
    val list = ArrayList<T>()
    for (index in 0 until size) {
        list.add(create(index))
    }
    return list
}

infix operator fun ByteArray.plus(byte: Byte) = ByteArray(this@plus.size + 1) { index ->
    when {
        index < this@plus.size -> this[index]
        index == this@plus.size -> byte
        else -> throw ArrayIndexOutOfBoundsException("ByteArray.plus return error with index:$index")
    }
}


infix operator fun ByteArray.plus(int: Int): ByteArray {
    if (int > 0XFF) {
        throw IndexOutOfBoundsException("An error occurred while int converting Byte, $int > 0XFF,please check index!!!")
    } else return this@plus + int.toByte()
}


fun ByteArray.toInt(isHighFront: Boolean = true): Int {
    val array = if (isHighFront) {
        this.reversedArray()
    } else {
        this
    }
    var temp = (array[0].toInt() and 0xff)
    val rangeSize = if (array.size <= 4) array.size else 4
    for (index in 1 until rangeSize) {
        temp = temp or (array[index].toInt() and 0xff shl 8 * index)
    }
    return temp
}

fun <T> LinkedList<T>.safePop(): T? {
    return if (size > 0) pop()
    else null
}


inline fun <E> Iterable<E>.forEachApply(action: E.() -> Unit) {
    for (element in this) element.action()
}

inline fun <E> Iterable<E>.forEachApplyIndexed(action: E.(index: Int) -> Unit) {
    var index = 0
    for (item in this) item.action(index++)
}

fun <T> Set<T>?.isEmpty(): Boolean = this?.isEmpty() ?: true


fun <K, V> Map<K, List<V>>.flatten(): List<Any?> =
    map { (k, v) ->
        mutableListOf<Any?>().apply {
            add(k)
            addAll(v)
        }
    }.flatten()

fun <K, V> Map<K, List<V>>.indexInSection(element: V): Int {
    forEach { (_, list) -> if (list.contains(element)) return list.indexOf(element) }
    return -1
}

fun <K, V> Map<K, List<V>>.keyIndex(key: K?): Int {
    key ?: return -1
    var index = 0
    forEach { (k, _) -> if (key == k) return index else index++ }
    return -1
}

fun <K, V> Map<K, List<V>>.keyAtIndex(index: Int): K? {
    keys.forEachIndexed { ind, k -> if (ind == index) return k }
    return null
}

fun <K, V> Map<K, List<V>>.sectionKey(element: V): K? {
    forEach { (k, list) -> if (list.contains(element)) return k }
    return null
}


inline fun <E, R : Comparable<R>> MutableList<E>.sortedSelf(crossinline selector: (E) -> R?): MutableList<E> =
    also { sortWith(compareBy(selector)) }

fun <E> List<E>.indexOf(item: E?): Int = if (item == null) -1 else indexOf(item)

fun <E> List<E>.isLast(element: E): Boolean = indexOf(element) == size - 1

fun <E> List<E>.nextAfter(index: Int): E? = if (index == count() - 1) null else this[index + 1]

fun <E> List<E>.nextAfter(element: E): E? =
    when (val index = indexOf(element)) {
        -1 -> throw IllegalAccessException("List doesn't contains $element")
        size - 1 -> null
        else -> this[index + 1]
    }


fun <E> List<E>.prevBefore(index: Int): E? = if (index == 0) null else this[index - 1]

fun <E> List<E>.prevBefore(element: E): E? =
    when (val index = indexOf(element)) {
        -1 -> throw IllegalAccessException("List doesn't contains $element")
        0 -> null
        else -> this[index - 1]
    }

fun <T> List<T>.random(): T = shuffled().first()


fun <T> List<T>?.isLast(index: Int): Boolean {
    return index == this?.lastIndex
}


fun <E> MutableList<E>.removeElement(element: E): E? {
    val index = indexOfOrNull(element) ?: return null
    return removeAt(index)
}

private fun <E> MutableList<E>.indexOfOrNull(element: E): Int? {
    val indexOf = indexOf(element)
    return if (indexOf != -1) indexOf else null
}


fun <T> Collection<T>?.isBlank(): Boolean = this == null || isEmpty()

fun <T> Collection<T?>.anyNull(): Boolean = any { it == null }

fun <T> Collection<T?>.allNull(): Boolean = all { it == null }

fun <T> Collection<T?>.countNulls(): Int = count { it == null }
fun <T> Collection<T?>.countNonNulls(): Int = size - countNulls()

fun <T : Any> Iterable<T?>.trimNulls(): List<T> = filterNotNull()
fun <T : Any> Iterable<T?>.trimNullsToMutableList(): MutableList<T> = filterNotNullTo(mutableListOf())

fun Iterable<String?>.trim(): List<String> = trimNulls().filterNot { it.isBlank() }
fun Iterable<String?>.trimToMutableList(): MutableList<String> = trimNulls().filterNotTo(mutableListOf()) { it.isBlank() }

fun <T1, T2> Iterable<T1>.combine(other: Iterable<T2>): List<Pair<T1, T2>> = combine(other) { thisItem: T1, otherItem: T2 -> Pair(thisItem, otherItem) }
fun <T1, T2> Iterable<T1>.combineToMutableList(other: Iterable<T2>): MutableList<Pair<T1, T2>> = combineToMutableList(other) { thisItem: T1, otherItem: T2 -> Pair(thisItem, otherItem) }

inline fun <T1, T2, R> Iterable<T1>.combine(other: Iterable<T2>, transform: (thisItem: T1, otherItem: T2) -> R): List<R> = flatMap { thisItem -> other.map { otherItem -> transform(thisItem, otherItem) } }

inline fun <T1, T2, R> Iterable<T1>.combineToMutableList(other: Iterable<T2>, transform: (thisItem: T1, otherItem: T2) -> R): MutableList<R> = flatMapTo(mutableListOf()) { thisItem -> other.map { otherItem -> transform(thisItem, otherItem) } }

inline fun <T, R> Iterable<T>.mapToMutableList(transform: (T) -> R): MutableList<R> = mapTo(mutableListOf(), transform)
inline fun <T, R> Iterable<T>.flatMapToMutableList(transform: (T) -> Iterable<R>): MutableList<R> = flatMapTo(mutableListOf(), transform)

inline fun <T> Int.timesToListOf(predicate: (Int) -> T): List<T> = (0 until this).map { predicate(it) }
inline fun <T> Int.timesToMutableListOf(predicate: (Int) -> T): MutableList<T> = (0 until this).mapToMutableList { predicate(it) }

fun <T> MutableList<T>.swapMutable(i: Int, j: Int): MutableList<T> {
    return apply {
        val aux = this[i]
        this[i] = this[j]
        this[j] = aux
    }
}

fun <T> List<T>.swapped(i: Int, j: Int): List<T> = toMutableList().swapMutable(i, j)

fun <T> List<T>.getRandom(generator: Random = Random()): T = get(generator.nextInt(size))

fun <T> MutableList<T>.shuffle(generator: Random = Random()): MutableList<T> = apply { Collections.shuffle(this, generator) }

fun <T> List<T>.shuffled(): List<T> = toMutableList().shuffle()

fun randomIntList(size: Int, generator: Random = Random()) = size.timesToListOf { generator.nextInt() }
fun randomIntList(size: Int, bound: Int, generator: Random = Random()) = size.timesToListOf { generator.nextInt(bound) }
fun randomFloatList(size: Int, generator: Random = Random()) = size.timesToListOf { generator.nextFloat() }
fun randomDoubleList(size: Int, generator: Random = Random()) = size.timesToListOf { generator.nextDouble() }
fun randomBooleanList(size: Int, generator: Random = Random()) = size.timesToListOf { generator.nextBoolean() }

fun <T> List<T>.encapsulate(): List<List<T>> = map { listOf(it) }
fun <T> List<T>.encapsulateToMutableList(): MutableList<MutableList<T>> = mapToMutableList { mutableListOf(it) }

fun <T> Collection<T>.init(): List<T> = take(size - 1)

inline val Collection<*>.half: Int get() = size / 2

fun <T> Collection<T>.firstHalf(): List<T> = take(half)
fun <T> Collection<T>.secondHalf(): List<T> = drop(half)

fun <T> Collection<T>.split(index: Int): Pair<List<T>, List<T>> = take(index) to drop(index)
fun <T> Collection<T>.split(): Pair<List<T>, List<T>> = split(half)

fun <K, V> Map<K, V>.keyAt(value: V): K? {
    val entry = entries.find { it.value == value }
    return entry?.key
}

fun <K, V> Map<K, V>.valueAt(key: V): V? {
    val entry = entries.find { it.key == key }
    return entry?.value
}

inline fun <reified T> MutableList<T>.removeIfCompat(noinline filter: (T) -> Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) removeIf(filter) else {
        for (i in size - 1 downTo 0) {
            if (filter(this[i])) {
                removeAt(i)
            }
        }
    }
}

fun <T> List<T>.second(): T? {
    return when {
        isEmpty() -> null
        size < 2 -> null
        else -> this[1]
    }
}

fun <T> ArrayList<T>.second(): T? {
    return when {
        isEmpty() -> null
        size < 2 -> null
        else -> this[1]
    }
}

/**
 * Returns an unmodifiable version of a list.
 */
inline fun <reified T> List<T>.unmodifiable(): List<T> =
    Collections.unmodifiableList(this)

/**
 * Returns an unmodifiable version of a set.
 */
inline fun <reified T> Set<T>.unmodifiable(): Set<T> =
    Collections.unmodifiableSet(this)


/**
 * Finds the first most common item
 * @receiver List<T>
 * @return T?
 */
fun <T> List<T>.mostCommon(): T? {
    return groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
}

/**
 * Finds the first least common item
 * @receiver List<T>
 * @return T?
 */
fun <T> List<T>.leastCommon(): T? {
    return groupingBy { it }.eachCount().minByOrNull { it.value }?.key
}

/**
 * Finds the first most common item
 * @receiver List<T>
 * @return T?
 */
fun <T> ArrayList<T>.mostCommon(): T? {
    return groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
}


/**
 * Finds the first least common item
 * @receiver List<T>
 * @return T?
 */
fun <T> ArrayList<T>.leastCommon(): T? {
    return groupingBy { it }.eachCount().minByOrNull { it.value }?.key
}


/**
 * Finds the first most common item
 * @receiver List<T>
 * @return T?
 */
fun <T> Collection<T>.mostCommon(): T? {
    return groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
}


/**
 * Finds the first least common item
 * @receiver List<T>
 * @return T?
 */
fun <T> Collection<T>.leastCommon(): T? {
    return groupingBy { it }.eachCount().minByOrNull { it.value }?.key
}


val <E> List<E>?.arrayList: ArrayList<E>
    get() = if (this is ArrayList) this else ArrayList(emptyList())


/**
 * ARRAY
 */

fun <T> Array<T>?.isNullOrEmpty() = this == null || isEmpty()

fun BooleanArray?.isNullOrEmpty() = this == null || isEmpty()

fun CharArray?.isNullOrEmpty() = this == null || isEmpty()

fun ByteArray?.isNullOrEmpty() = this == null || isEmpty()

fun ShortArray?.isNullOrEmpty() = this == null || isEmpty()

fun IntArray?.isNullOrEmpty() = this == null || isEmpty()

fun LongArray?.isNullOrEmpty() = this == null || isEmpty()

fun FloatArray?.isNullOrEmpty() = this == null || isEmpty()

fun DoubleArray?.isNullOrEmpty() = this == null || isEmpty()

fun <T> Array<T>?.isNotNullOrEmpty() = this != null && isNotEmpty()

fun BooleanArray?.isNotNullOrEmpty() = this != null && isNotEmpty()

fun CharArray?.isNotNullOrEmpty() = this != null && isNotEmpty()

fun ByteArray?.isNotNullOrEmpty() = this != null && isNotEmpty()

fun ShortArray?.isNotNullOrEmpty() = this != null && isNotEmpty()

fun IntArray?.isNotNullOrEmpty() = this != null && isNotEmpty()

fun LongArray?.isNotNullOrEmpty() = this != null && isNotEmpty()

fun FloatArray?.isNotNullOrEmpty() = this != null && isNotEmpty()

fun DoubleArray?.isNotNullOrEmpty() = this != null && isNotEmpty()

/**
 * SET
 */

fun <T> Set<T>?.isNullOrEmpty(): Boolean = this == null || isEmpty()


/**
 * LIST
 */

fun <T> T.mergeWith(items: Iterable<T>) =
    mutableListOf(this).apply { addAll(items) }

fun <T> T.mergeWith(vararg items: T) =
    mutableListOf(this).apply { addAll(items) }

fun <E : Comparable<E>> MutableList<E>.sortedSelf(): MutableList<E> =
    also { sortWith(Comparator { o1, o2 -> o1.compareTo(o2) }) }

/**
 * Returns the first element, or `null` if the list is empty.
 */
fun <T> Iterable<T>.skip(n: Int): List<T> {
    require(n >= 0) { "Requested skip count $n is less than zero." }
    if (n == 0) return this.toList()
    if (this is Collection<T>) {
        if (n >= size) return emptyList()
    }
    val list = ArrayList<T>()
    for ((index, item) in this.withIndex()) {
        if (index < n) continue
        list.add(item)
    }
    return list
}
