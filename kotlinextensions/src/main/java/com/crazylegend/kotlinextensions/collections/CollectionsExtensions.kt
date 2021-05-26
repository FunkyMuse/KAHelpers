@file:Suppress("UNCHECKED_CAST")

package com.crazylegend.kotlinextensions.collections

import android.content.res.TypedArray
import androidx.collection.LongSparseArray
import androidx.collection.SparseArrayCompat
import androidx.collection.forEach
import com.crazylegend.common.randomUUIDstring
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by hristijan on 2/22/19 to long live and prosper !
 */


/**
 * like map, but multithreaded. It uses the number of cores + 2 threads.
 */
inline fun <T, V> List<T>.pmap(operation: (T) -> V): List<V> {
    val threads = ArrayList<Thread>()
    val cores = Runtime.getRuntime().availableProcessors()
    // run each thread on a partitioned block to minimize thread setup/teardown
    val partitioned = this.split(cores + 2)
    val partitionedResult = partitioned.map { partition ->
        val thread = Thread()
        threads.add(thread)
        thread.run {
            partition.map(operation)
        }
    }
    // wait for threads to finish
    for (thread in threads) {
        thread.join()
    }

    return partitionedResult.flatten()
}

/**
 * Moves the given **T** item to the specified index
 */
fun <T> MutableList<T>.move(item: T, newIndex: Int) {
    val currentIndex = indexOf(item)
    if (currentIndex < 0) return
    removeAt(currentIndex)
    add(newIndex, item)
}

/**
 * Moves the given item at the `oldIndex` to the `newIndex`
 */
fun <T> MutableList<T>.moveAt(oldIndex: Int, newIndex: Int) {
    val item = this[oldIndex]
    removeAt(oldIndex)
    if (oldIndex > newIndex)
        add(newIndex, item)
    else
        add(newIndex - 1, item)
}

/**
 * Moves all items meeting a predicate to the given index
 */
fun <T> MutableList<T>.moveAll(newIndex: Int, predicate: (T) -> Boolean) {
    check(newIndex in 0 until size)
    val split = partition(predicate)
    clear()
    addAll(split.second)
    addAll(if (newIndex >= size) size else newIndex, split.first)
}

/**
 * Moves the given element at specified index up the **MutableList** by one increment
 * unless it is at the top already which will result in no movement
 */
fun <T> MutableList<T>.moveUpAt(index: Int) {
    if (index == 0) return
    if (index < 0 || index >= size) throw Exception("Invalid index $index for MutableList of size $size")
    val newIndex = index + 1
    val item = this[index]
    removeAt(index)
    add(newIndex, item)
}

/**
 * Moves the given element **T** up the **MutableList** by one increment
 * unless it is at the bottom already which will result in no movement
 */
fun <T> MutableList<T>.moveDownAt(index: Int) {
    if (index == size - 1) return
    if (index < 0 || index >= size) throw Exception("Invalid index $index for MutableList of size $size")
    val newIndex = index - 1
    val item = this[index]
    removeAt(index)
    add(newIndex, item)
}

/**
 * Moves the given element **T** up the **MutableList** by an index increment
 * unless it is at the top already which will result in no movement.
 * Returns a `Boolean` indicating if move was successful
 */
fun <T> MutableList<T>.moveUp(item: T): Boolean {
    val currentIndex = indexOf(item)
    if (currentIndex == -1) return false
    val newIndex = (currentIndex - 1)
    if (currentIndex <= 0) return false
    remove(item)
    add(newIndex, item)
    return true
}

/**
 * Moves the given element **T** up the **MutableList** by an index increment
 * unless it is at the bottom already which will result in no movement.
 * Returns a `Boolean` indicating if move was successful
 */
fun <T> MutableList<T>.moveDown(item: T): Boolean {
    val currentIndex = indexOf(item)
    if (currentIndex == -1) return false
    val newIndex = (currentIndex + 1)
    if (newIndex >= size) return false
    remove(item)
    add(newIndex, item)
    return true
}


/**
 * Moves first element **T** up an index that satisfies the given **predicate**, unless its already at the top
 */
inline fun <T> MutableList<T>.moveUp(crossinline predicate: (T) -> Boolean) = find(predicate)?.let { moveUp(it) }

/**
 * Moves first element **T** down an index that satisfies the given **predicate**, unless its already at the bottom
 */
inline fun <T> MutableList<T>.moveDown(crossinline predicate: (T) -> Boolean) = find(predicate)?.let { moveDown(it) }

/**
 * Moves all **T** elements up an index that satisfy the given **predicate**, unless they are already at the top
 */
inline fun <T> MutableList<T>.moveUpAll(crossinline predicate: (T) -> Boolean) = asSequence().withIndex()
        .filter { predicate.invoke(it.value) }
        .forEach { moveUpAt(it.index) }

/**
 * Moves all **T** elements down an index that satisfy the given **predicate**, unless they are already at the bottom
 */
inline fun <T> MutableList<T>.moveDownAll(crossinline predicate: (T) -> Boolean) = asSequence().withIndex()
        .filter { predicate.invoke(it.value) }
        .forEach { moveDownAt(it.index) }


/**
 * Swaps the index position of two items
 */
fun <T> MutableList<T>.swap(itemOne: T, itemTwo: T) = swap(indexOf(itemOne), indexOf(itemTwo))

@JvmName("combinationsExtension")
fun <A, B> List<A>.combinations(listB: List<B>): List<Pair<A, B>> =
        combinations<A, B>(this, listB)

@JvmName("combinations")
fun <A, B> combinations(listA: List<A>, listB: List<B>): List<Pair<A, B>> =
        listA.flatMap { first -> listB.map { second -> first to second } }

@JvmName("combinationsExtension")
fun <A, B, C> List<A>.combinations(listB: List<B>, listC: List<C>): List<Triple<A, B, C>> =
        combinations<A, B, C>(this, listB, listC)

@JvmName("combinations")
fun <A, B, C> combinations(listA: List<A>, listB: List<B>, listC: List<C>): List<Triple<A, B, C>> =
        listA.flatMap { first ->
            listB.flatMap { second ->
                listC.map { third ->
                    Triple(first, second, third)
                }
            }
        }

fun <T> combinations(vararg listOfList: List<T>): List<List<T>> {
    return listOfList.fold(
            initial = listOf(emptyList<T>()),
            operation = { accumulateListOfList: List<List<T>>, elementList: List<T> ->
                accumulateListOfList.flatMap { list: List<T> ->
                    elementList.map { element: T -> list + element }
                }
            }
    )
}

fun <T> Collection<T>.doIfContained(t: T, func: T.() -> Unit): Boolean {
    if (contains(t)) {
        t.func()
        return true
    }
    return false
}

infix fun <T> T.appendTo(list: List<T>) = list + listOf(this)
infix fun <T> T.prependTo(list: List<T>) = listOf(this) + list

fun <T> List<T>.swap(i: Int, j: Int): List<T> {
    if (isInBounds(i) && isInBounds(j)) {
        Collections.swap(this, i, j)
    }
    return this
}

private fun <T> List<T>.isInBounds(index: Int): Boolean {
    return index in 0..lastIndex
}


fun <T> List<T>.startWith(item: T): List<T> {
    val list = this.toMutableList()
    list.add(0, item)
    return list
}

fun <T> List<T>.startWith(data: List<T>): List<T> {
    val list = this.toMutableList()
    list.addAll(0, data)
    return list
}

fun <T> List<T>.startWithIfNotEmpty(item: T): List<T> {
    if (this.isNotEmpty()) {
        return startWith(item)
    }
    return this
}

fun <K, V> MutableMap<K, MutableList<V>>.deepCopy(): MutableMap<K, MutableList<V>> {
    val result = mutableMapOf<K, MutableList<V>>()
    for ((key, value) in this) {
        result[key] = value.toMutableList()
    }
    return result
}

fun <T> MutableList<T>.doIf(predicate: Boolean, action: MutableList<T>.() -> Any): MutableList<T> {
    if (predicate) {
        this.action()
    }
    return this
}

fun <T> MutableList<T>.removeFirst(predicate: (T) -> Boolean): Boolean {
    for (t in this) {
        if (predicate(t)) {
            this.remove(t)
            return true
        }
    }
    return false
}

fun <T> LongSparseArray<T>.toList(): List<T> {
    val list = mutableListOf<T>()

    this.forEach { _, value -> list.add(value) }

    return list
}

fun <T> LongSparseArray<T>.toggle(key: Long, item: T) {
    val current = this.get(key)
    if (current == null) {
        this.append(key, item)
    } else {
        this.remove(key)
    }
}

inline fun <T> Iterable<T>.toSparseArray(getKey: Function1<T, Int?>): SparseArrayCompat<T> {
    val array = SparseArrayCompat<T>()
    for (item in this) {
        array.append(getKey(item) ?: 0, item)
    }
    return array
}

inline fun <T> Iterable<T>.toLongSparseArray(getKey: Function1<T, Long?>): LongSparseArray<T> {
    val array = LongSparseArray<T>()
    for (item in this) {
        array.append(getKey(item) ?: 0L, item)
    }
    return array
}

inline fun <T> Iterable<T>.toLongSparseArrayGrouped(getKey: Function1<T, Long?>): LongSparseArray<MutableList<T>> {
    val array = LongSparseArray<MutableList<T>>()
    for (item in this) {
        val key: Long = getKey(item) ?: 0L
        var vList = array.get(key)
        if (vList == null) {
            vList = java.util.ArrayList()
            array.put(key, vList)
        }
        vList.add(item)
    }
    return array
}

inline fun <T, K> Iterable<T>.toHashMap(getKey: Function1<T, K>) = toHashMap(getKey, { it })

inline fun <T, K, V> Iterable<T>.toHashMap(getKey: Function1<T, K>, getValue: Function1<T, V>): HashMap<K, V> {
    val map = HashMap<K, V>()
    for (item in this) {
        map[getKey(item)] = getValue(item)
    }
    return map
}

inline fun <T, K> Iterable<T?>.toHashMapNullable(getKey: Function1<T?, K>) = toHashMapNullable(getKey, { it })

inline fun <T, K, V> Iterable<T?>.toHashMapNullable(getKey: Function1<T?, K>, getValue: Function1<T?, V?>): HashMap<K, V?> {
    val map = HashMap<K, V?>()
    for (item in this) {
        map[getKey(item)] = getValue(item)
    }
    return map
}

inline fun <T : TypedArray?, R> T.use(block: (T) -> R): R {
    var recycled = false
    try {
        return block(this)
    } catch (e: Exception) {
        recycled = true
        try {
            this?.recycle()
        } catch (exception: Exception) {
        }
        throw e
    } finally {
        if (!recycled) {
            this?.recycle()
        }
    }
}

fun generateRandomIntegerList(size: Int, range: IntRange): MutableList<Int> {
    val resultList = ArrayList<Int>(size)
    for (i in 1..size) {
        resultList.add(kotlin.random.Random.nextInt(range.first, range.last))
    }
    return resultList
}

fun generateRandomStringList(size: Int): MutableList<String> {
    val resultList = ArrayList<String>(size)
    for (i in 1..size) {
        resultList.add(randomUUIDstring)
    }
    return resultList
}


fun <T> List<T>.getStringRepresentation(maxElements: Int = Integer.MAX_VALUE): String {
    if (size > maxElements) return " The list is too long to output!"
    var string = ""
    forEach { string += "$it, " }
    string = string.trimEnd(' ')
    string = string.trimEnd(',')
    return string
}

fun <T : Comparable<T>> List<T>.isListSorted(): Boolean {
    return this == this.sorted()
}

val List<Int>.isBinarySearchable: Boolean
    get() {
        return (this == this.distinct().sorted())
    }

fun <K, V> Map<K, V?>.filterNotNullValues(): Map<K, V> {
    return mapNotNull { (key, nullableValue) ->
        nullableValue?.let { key to it }
    }.toMap()
}

fun <T, S> MutableMap<T, S>.replaceWith(map: Map<T, S>) {
    if (map === this) return
    clear()
    putAll(map)
}

/**
 * Adds the item if it is not in the collection or removes it if it is.
 * @return Whether this collection contains the item, AFTER this operation
 */
fun <T> MutableCollection<T>.addOrRemove(item: T): Boolean {
    return if (contains(item)) {
        remove(item)
        false
    } else {
        add(item)
        true
    }
}

/**
 * Returns true if an element matching the given [predicate] was found.
 */
inline fun <T> Iterable<T>.contains(predicate: (T) -> Boolean): Boolean {
    for (element in this) if (predicate(element)) return true
    return false
}

fun <T> MutableCollection<T>.replaceWith(collection: Collection<T>) {
    if (collection === this) return
    clear()
    addAll(collection)
}

/**
 * Replaces the first item within the list that matches the given predicate or adds the item to
 * the list if none match.
 */
fun <T> MutableList<T>.addOrReplace(item: T, predicate: (T) -> Boolean): Boolean {
    return this.indexOfFirst { predicate.invoke(it) }
            .takeIf { it >= 0 }
            ?.let { this[it] = item }
            ?.let { true }
            ?: this.add(item)
                    .let { false }
}

fun <S : MutableList<T>, T> S.addAnd(index: Int, item: T): S {
    add(index, item)
    return this
}

fun <S : MutableCollection<T>, T> S.addAnd(item: T): S {
    add(item)
    return this
}

fun <T> MutableCollection<T>.addAll(vararg items: T) {
    addAll(items)
}


val <T> Collection<T>?.isNotNullOrEmpty: Boolean get() = this != null && this.isNotEmpty()


/**
 * Checks if [element] is contained (ignoring case) by the array
 * @param element An element
 * @return Does the array contain [element]
 */
fun Array<String>.containsIgnoreCase(element: String): Boolean = any { it.equals(element, true) }

fun <E> Collection<E>.isLast(position: Int) = position == size - 1


inline fun <reified T> T.addToList(list: MutableList<T>): T = this.apply {
    list.add(this)
}

fun Collection<*>?.isIndexOutOfBounds(index: Int): Boolean {
    return this == null || index < 0 || index > this.size - 1
}


