package com.crazylegend.kotlinextensions.collections

import android.util.SparseArray
import java.util.*


/**
 * Created by hristijan on 2/22/19 to long live and prosper !
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
fun <K, E> HashMap<K, E>.addIfNotExist(key: K, obj: E): E? = if (!this.containsKey(key)) put(key, obj) else { null }

/**
 * Adds [E] to this map if the same doesn't exist
 */
fun <K, E> MutableMap<K, E>.addIfNotExist(key: K, obj: E): E? = if (!this.containsKey(key)) put(key, obj) else { null }

fun <T> List<T>.randomItem(): T {
    return this[Random().nextInt(size)]
}

/**
 * Returns a list containing all values in a [SparseArray].
 */
val <T> SparseArray<T>.values: List<T>
    get() = ArrayList<T>().apply {
        for (i in 0 until size()) { add(valueAt(i)) }
    }

/**
 * Sets value for specified key.
 */
operator fun <T> SparseArray<T>.set(key: Int, value: T?) { put(key, value) }

/**
 * Copies keys and values from specified [SparseArray], overwriting own
 * key/value pairs.
 */
operator fun <T> SparseArray<T>.plusAssign(array: SparseArray<T?>) {
    for (i in 0 until array.size()) { put(array.keyAt(i), array.valueAt(i)) }
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