package com.crazylegend.kotlinextensions.collections


/**
 * Created by crazy on 1/24/20 to long live and prosper !
 */


fun <K, V> MutableMap<K, V>.putAllIfNotNull(
        vararg data: Pair<K, V?>,
        predicate: (V) -> Boolean = { true }
) {
    for ((key, value) in data) {
        value?.also { if (predicate.invoke(it)) put(key, it) }
    }
}

fun <K, V : CharSequence> MutableMap<K, V>.putAllIfNotNullOrEmpty(vararg data: Pair<K, V?>) =
        putAllIfNotNull(*data) { it.isNotEmpty() }

fun <K, V : CharSequence> MutableMap<K, V>.putAllIfNotNullOrBlank(vararg data: Pair<K, V?>) =
        putAllIfNotNull(*data) { it.isNotBlank() }


/**
 * SAFE MUTABLE
 */

fun <E> List<E>?.safeMutable(default: MutableList<E> = mutableListOf()): MutableList<E> =
        (if (this is MutableList<E>) this else this?.toMutableList()) ?: default

fun <K, V> Map<K, V>?.safeMutable(default: MutableMap<K, V> = mutableMapOf()): MutableMap<K, V> =
        (if (this is MutableMap<K, V>) this else this?.toMutableMap()) ?: default


fun <T> Array<T>.resize(newSize: Int, creator: (Int) -> T): Array<T?> {
    val copiedArray = this.copyOf(newSize)
    for (i in size until newSize) {
        copiedArray[i] = creator(i)
    }
    return copiedArray
}


/**
 * Isolate the keys out of a list of pairs and store them in a set
 */
fun <T, E> Map<T, E>.keySet(): Set<T> {
    return this
            .map { (key, _) -> key }
            .toSet()
}


/**
 * Isolate the values out of a list of pairs and store them in a set
 */
fun <T, E> Map<T, E>.valueSet(): Set<E> {
    return this
            .map { (_, values) -> values }
            .toSet()
}


/**
 * Isolate the keys out of a list of pairs and store them in a set
 */
fun <T, E> List<Pair<T, E>>.keySet(): Set<T> {
    return this
            .map { (key, _) -> key }
            .toSet()
}


/**
 * Isolate the values out of a list of pairs and store them in a set
 */
fun <T, E> List<Pair<T, E>>.valueSet(): Set<E> {
    return this
            .map { (_, values) -> values }
            .toSet()
}

inline fun <reified T> Collection<T>.removeDuplicates(): MutableList<T> {
    return this.toSet().toMutableList()
}

inline fun <reified T> Collection<T>.onDuplicatesRemoved(noDups: (collection: Set<T>) -> Unit = {}) {
    noDups(toSet())
}


fun <T> Collection<T>?.onNullOrEmpty(onNotNullOrEmpty: () -> Unit = {}, function: () -> Unit) {
    if (this.isNullOrEmpty()) function() else onNotNullOrEmpty()
}

fun <T> Collection<T>?.onNull(onNotNull: () -> Unit = {}, function: () -> Unit) {
    if (this == null) function() else onNotNull()
}

fun <T> Collection<T>?.onNotNullOrEmpty(onNullOrEmpty: () -> Unit = {}, function: Collection<T>.() -> Unit) {
    if (this.isNullOrEmpty()) onNullOrEmpty() else this.function()
}

fun <T> Collection<T>?.onNotNull(onNull: () -> Unit = {}, function: Collection<T>.() -> Unit) {
    if (this == null) onNull() else this.function()
}