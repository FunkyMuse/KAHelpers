package com.crazylegend.kotlinextensions.collections

import android.util.SparseArray
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by funkymuse on 5/26/21 to long live and prosper !
 */


/**
 * USAGE
 * listOf(1, 2, 3) * 4 gives you [4, 8, 12]
 */
operator fun List<Int>.times(by: Int): List<Int> {
    return this.map { it * by }
}

/**
 * Swaps two values
 */
fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}

fun <T> MutableList<T>.swapAsList(index1: Int, index2: Int): MutableList<T> {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
    return this
}


/**
 * Gets mid element
 */
fun <T> List<T>.midElement(): T? {
    return if (isEmpty())
        null
    else
        this[size / 2]
}


/**
 * Gets mid element
 */
fun <T> ArrayList<T>.midElement(): T? {
    return if (isEmpty())
        null
    else
        this[size / 2]
}


/**
 * Iterate the receiver [List] backwards.
 */
inline fun <T> List<T>.forEachReversed(f: (T) -> Unit) {
    var i = size - 1
    while (i >= 0) {
        f(get(i))
        i--
    }
}

/**
 * Iterate the receiver [List] backwards.
 */
inline fun <T> List<T>.forEachReversedIndexed(f: (Int, T) -> Unit) {
    var i = size - 1
    while (i >= 0) {
        f(i, get(i))
        i--
    }
}


/**
 * Iterate the receiver [ArrayList] backwards.
 */
inline fun <T> ArrayList<T>.forEachReversed(f: (T) -> Unit) {
    var i = size - 1
    while (i >= 0) {
        f(get(i))
        i--
    }
}

/**
 * Iterate the receiver [ArrayList] backwards.
 */
inline fun <T> ArrayList<T>.forEachReversedIndexed(f: (Int, T) -> Unit) {
    var i = size - 1
    while (i >= 0) {
        f(i, get(i))
        i--
    }
}


/**
 * get mid index of a list
 */
val <T> List<T>.midIndex: Int
    get() = if (size == 0) 0 else size / 2


/**
 * get mid index of a list
 */
val <T> ArrayList<T>.midIndex: Int
    get() = if (size == 0) 0 else size / 2


/**
 * Adds [E] to this list if the same doesn't exist
 */
infix fun <E> ArrayList<E>.addIfNotExist(obj: E) = if (!this.contains(obj)) add(obj) else false

/**
 * Adds [E] to this list if the same doesn't previously exist.
 * so
 */
infix fun <E> MutableList<E>.addIfNotExist(obj: E) = if (!this.contains(obj)) add(obj) else false

fun <T> List<List<T>>.concat(): List<T> = fold(listOf()) { acc, l -> acc + l }
fun <T> List<List<T>>.concatToMutableList(): MutableList<T> = concat().toMutableList()


/**
 * Adds [E] to this map if the same doesn't exist
 */
fun <K, E> java.util.HashMap<K, E>.addIfNotExist(key: K, obj: E): E? = if (!this.containsKey(key)) put(key, obj) else {
    null
}

/**
 * Adds [E] to this map if the same doesn't exist
 */
fun <K, E> MutableMap<K, E>.addIfNotExist(key: K, obj: E): E? = if (!this.containsKey(key)) put(key, obj) else {
    null
}

fun <T> List<T>.randomItem(): T {
    return this[Random().nextInt(size)]
}


fun <T> ArrayList<T>.randomItem(): T {
    return this[Random().nextInt(size)]
}


/**
 * Returns a list containing all values in a [SparseArray].
 */
val <T> SparseArray<T>.values: List<T>
    get() = ArrayList<T>().apply {
        for (i in 0 until size()) {
            add(valueAt(i))
        }
    }

/**
 * Sets value for specified key.
 */
operator fun <T> SparseArray<T>.set(key: Int, value: T?) {
    put(key, value)
}

/**
 * Copies keys and values from specified [SparseArray], overwriting own
 * key/value pairs.
 */
operator fun <T> SparseArray<T>.plusAssign(array: SparseArray<T?>) {
    for (i in 0 until array.size()) {
        put(array.keyAt(i), array.valueAt(i))
    }
}

/**
 * Populates and returns a [SparseArray] by populating with keys
 * provided by [keySelector] and values that are the elements themselves.
 *
 * For instances where the keys repeat, the last key/value pair will be added
 * to the array.
 */
inline fun <V> Iterable<V>.associateBy(keySelector: (V) -> Int): SparseArray<V> =
    associateByTo(SparseArray(), keySelector)

/**
 * Populates and returns a [SparseArray] by populating with keys
 * provided by [keySelector] and values provided by [valueTransform].
 *
 * For instances where the keys repeat, the last key/value pair will be added
 * to the array.
 */
inline fun <T, V> Iterable<T>.associateBy(keySelector: (T) -> Int,
                                          valueTransform: (T) -> V): SparseArray<V> =
    associateByTo(SparseArray(), keySelector, valueTransform)

/**
 * Populates and returns the [destination] array by populating with keys
 * provided by [keySelector] and values that are the elements themselves.
 *
 * For instances where the keys repeat, the last key/value pair will be added
 * to the array.
 */
inline fun <V, M : SparseArray<in V>> Iterable<V>.associateByTo(destination: M,
                                                                keySelector: (V) -> Int): M {
    for (element in this) destination.put(keySelector(element), element)
    return destination
}

/**
 * Populates and returns the [destination] array by populating with keys
 * provided by [keySelector] and values provided by [valueTransform].
 *
 * For instances where the keys repeat, the last key/value pair will be added
 * to the array.
 */
inline fun <T, V, M : SparseArray<in V>> Iterable<T>.associateByTo(destination: M,
                                                                   keySelector: (T) -> Int,
                                                                   valueTransform: (T) -> V): M {
    for (element in this) {
        destination.put(keySelector(element), valueTransform(element))
    }
    return destination
}

/**
 * Performs given [action] on each key/value pair.
 */
inline fun <V> SparseArray<V>.forEach(action: (Int, V) -> Unit) {
    for (i in 0 until size()) action(keyAt(i), valueAt(i))
}

/**
 * Writes all entries matching given [predicate] to [destination] array.
 *
 * @return Destination array.
 */
inline fun <V> SparseArray<V>.filterTo(destination: SparseArray<V>,
                                       predicate: (Int, V) -> Boolean): SparseArray<V> {
    forEach { k, v -> if (predicate(k, v)) destination[k] = v }
    return destination
}

/**
 * Filters all entries matching given [predicate] into a new [SparseArray].
 */
inline fun <V> SparseArray<V>.filter(predicate: (Int, V) -> Boolean): SparseArray<V> =
    filterTo(SparseArray(), predicate)

/**
 * Gets value with specific key. If the value is not present,
 * calls [defaultValue] to obtain a non-null value which is placed into the
 * array, then returned.
 *
 * This method is not thread-safe.
 */
inline fun <V> SparseArray<V>.getOrPut(key: Int, defaultValue: () -> V): V {
    this[key]?.let { return it }
    return defaultValue().apply { put(key, this) }
}

/**
 * Returns a [Deque] filled with all elements of this collection.
 */
fun <T> Iterable<T>.toDeque(): Deque<T> {
    return if (this is Collection<T>) this.toDeque()
    else toCollection(java.util.ArrayDeque<T>())
}

/**
 * Returns a [Deque] filled with all elements of this collection.
 */
fun <T> Collection<T>.toDeque(): Deque<T> = java.util.ArrayDeque<T>(this)

/**
 * Returns an empty new [Deque].
 */
fun <T> dequeOf(): Deque<T> = arrayDequeOf()

/**
 * Returns an empty new [ArrayDeque].
 */
fun <T> arrayDequeOf(): java.util.ArrayDeque<T> = java.util.ArrayDeque()

/**
 * Returns a new [Deque] with the given elements.
 */
fun <T> dequeOf(vararg elements: T): Deque<T> {
    return if (elements.isEmpty()) java.util.ArrayDeque()
    else elements.toCollection(java.util.ArrayDeque<T>())
}

/**
 * Returns a new [ArrayDeque] with the given elements.
 */
fun <T> arrayDequeOf(vararg elements: T): java.util.ArrayDeque<T> {
    return if (elements.isEmpty()) java.util.ArrayDeque()
    else elements.toCollection(java.util.ArrayDeque())
}

/**
 * Find the index of the minimal element of an array.
 * @param [arr] an array of any Comparable type.
 * @return the index of the first minimal element in the array.
 */
fun <T : Comparable<T>> indexOfMin(arr: Array<T>): Int {
    var minIndex = 0
    val minItem = arr[0]

    for (i in 1..arr.size) {
        if (arr[minIndex] < minItem)
            minIndex = i
    }

    return minIndex
}


inline fun <T> count(start: T, step: T, crossinline add: (T, T) -> T): Sequence<T> = sequence {
    var n = start

    while (true) {
        yield(n)
        n = add(n, step)
    }
}

/**
 * Make an iterator that returns values starting with [start] with evenly space [step].
 */
fun count(start: Byte, step: Byte = 1): Sequence<Byte> = count(start, step) { n, s -> (n + s).toByte() }

/**
 * Make an iterator that returns values starting with [start] with evenly space [step].
 */
fun count(start: Short, step: Short = 1): Sequence<Short> = count(start, step) { n, s -> (n + s).toShort() }

/**
 * Make an iterator that returns values starting with [start] with evenly space [step].
 */
fun count(start: Int, step: Int = 1): Sequence<Int> = count(start, step) { n, s -> n + s }

/**
 * Make an iterator that returns values starting with [start] with evenly space [step].
 */
fun count(start: Long, step: Long = 1L): Sequence<Long> = count(start, step) { n, s -> n + s }

/**
 * Make an iterator that returns values starting with [start] with evenly space [step].
 */
fun count(start: Float, step: Float = 1f): Sequence<Float> = count(start, step) { n, s -> n + s }

/**
 * Make an iterator that returns values starting with [start] with evenly space [step].
 */
fun count(start: Double, step: Double = 1.0): Sequence<Double> = count(start, step) { n, s -> n + s }

/**
 * Make an iterator returning elements from the iterable and saving a copy of each.
 * When the iterable is exhausted, return elements from the saved copy. Repeats indefinitely.
 */
fun <T> Iterable<T>.cycle(): Sequence<T> = sequence {
    val saved = mutableListOf<T>()

    for (item in this@cycle) {
        yield(item)
        saved.add(item)
    }
    if (saved.isNotEmpty()) {
        while (true) {
            for (item in saved) {
                yield(item)
            }
        }
    }
}

/**
 * Make an iterator that returns element over and over again. Runs indefinitely.
 */
fun <T> T.repeat(): Sequence<T> = sequence {
    while (true) {
        yield(this@repeat)
    }
}

/**
 * Make an iterator that returns element over and over again. Runs [count] times.
 */
fun <T> T.repeat(count: Int): Sequence<T> = sequence {
    for (i in 1..count) {
        yield(this@repeat)
    }
}

/**
 * Returns the first element matching the given [predicate],
 * or the result of calling the [defaultValue] function if no such element is found.
 */
inline fun <T> Iterable<T>.firstOrElse(predicate: (T) -> Boolean, defaultValue: () -> T): T {
    for (element in this) if (predicate(element)) return element

    return defaultValue()
}

/**
 * Returns the first element which is not null.
 * @throws [NoSuchElementException] if there are no elements or all elements are null.
 */
fun <T> Iterable<T?>.firstNotNull(): T = this.first { it != null } as T

/**
 * Returns the first element which is not null.
 * @throws [NoSuchElementException] if there are no elements or all elements are null.
 */
fun <T> List<T?>.firstNotNull(): T = this.first { it != null } as T


/**
 * Returns the first element which is not null.
 * @throws [NoSuchElementException] if there are no elements or all elements are null.
 */
fun <T> ArrayList<T?>.firstNotNull(): T = this.first { it != null } as T


/**
 * Returns the first element which is not null,
 * or the result of calling the [defaultValue] function if there are no elements or all elements are null.
 */
inline fun <T> Iterable<T?>.firstNotNullOrElse(defaultValue: () -> T): T = firstOrElse({ it != null }, defaultValue) as T

/**
 * Returns the first element which is not null,
 * or the result of calling the [defaultValue] function if there are no elements or all elements are null.
 */
inline fun <T> List<T?>.firstNotNullOrElse(defaultValue: () -> T): T = firstOrElse({ it != null }, defaultValue) as T

/**
 * Returns the last element matching the given [predicate],
 * or the result of calling the [defaultValue] function if no such element is found.
 */
inline fun <T> Iterable<T>.lastOrElse(predicate: (T) -> Boolean, defaultValue: () -> T): T {
    var last: T? = null
    var found = false
    for (element in this) {
        if (predicate(element)) {
            last = element
            found = true
        }
    }
    if (!found) {
        return defaultValue()
    }
    return last as T
}

/**
 * Returns the last element matching the given [predicate],
 * or the result of calling the [defaultValue] function if no such element is found.
 */
inline fun <T> List<T>.lastOrElse(predicate: (T) -> Boolean, defaultValue: () -> T): T {
    val iterator = this.listIterator(size)
    while (iterator.hasPrevious()) {
        val element = iterator.previous()
        if (predicate(element)) return element
    }

    return defaultValue()
}

/**
 * Returns the last element which is not null.
 * @throws [NoSuchElementException] if there are no elements or all elements are null.
 */
fun <T> Iterable<T?>.lastNotNull(): T = this.last { it != null } as T

/**
 * Returns the last element which is not null.
 * @throws [NoSuchElementException] if there are no elements or all elements are null.
 */
fun <T> List<T?>.lastNotNull(): T = this.last { it != null } as T

/**
 * Returns the last element which is not null,
 * or the result of calling the [defaultValue] function if there are no elements or all elements are null.
 */
inline fun <T> Iterable<T?>.lastNotNullOrElse(defaultValue: () -> T): T = lastOrElse({ it != null }, defaultValue) as T

/**
 * Returns the last element which is not null,
 * or the result of calling the [defaultValue] function if there are no elements or all elements are null.
 */
inline fun <T> List<T?>.lastNotNullOrElse(defaultValue: () -> T): T = lastOrElse({ it != null }, defaultValue) as T


/**
 * splits a list into sublists
 * @param partitionSize the max size of each sublist. The last sub list may be shorter.
 * @return a List of Lists of T
 */
fun <T> List<T>.split(partitionSize: Int): List<List<T>> {
    if (this.isEmpty()) return emptyList()
    if (partitionSize < 1) throw IllegalArgumentException("partitionSize must be positive")

    val result = ArrayList<List<T>>()
    var entry = ArrayList<T>(partitionSize)
    for (item in this) {
        if (entry.size == partitionSize) {
            result.add(entry)
            entry = ArrayList<T>()
        }
        entry.add(item)
    }
    result.add(entry)
    return result
}
