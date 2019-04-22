@file:Suppress("UNCHECKED_CAST")

package com.crazylegend.kotlinextensions.collections

import android.content.res.TypedArray
import android.util.LongSparseArray
import android.util.SparseArray
import androidx.core.util.forEach
import java.util.*
import kotlin.NoSuchElementException


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
 * Gets mid element
 */
@Throws(NoSuchElementException::class)
fun <T> List<T>.midElement(): T {
    if (isEmpty())
        throw NoSuchElementException("List is empty.")
    return this[size / 2]
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
 * get mid index of a list
 */
val <T> List<T>.midIndex: Int
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

/**
 * Returns a [Deque] filled with all elements of this collection.
 */
fun <T> Iterable<T>.toDeque(): Deque<T> {
    return if (this is Collection<T>) this.toDeque()
    else toCollection(ArrayDeque<T>())
}

/**
 * Returns a [Deque] filled with all elements of this collection.
 */
fun <T> Collection<T>.toDeque(): Deque<T>
        = ArrayDeque<T>(this)

/**
 * Returns an empty new [Deque].
 */
fun <T> dequeOf(): Deque<T>
        = arrayDequeOf()

/**
 * Returns an empty new [ArrayDeque].
 */
fun <T> arrayDequeOf(): ArrayDeque<T>
        = ArrayDeque()

/**
 * Returns a new [Deque] with the given elements.
 */
fun <T> dequeOf(vararg elements: T): Deque<T> {
    return if (elements.isEmpty()) ArrayDeque()
    else elements.toCollection(ArrayDeque<T>())
}

/**
 * Returns a new [ArrayDeque] with the given elements.
 */
fun <T> arrayDequeOf(vararg elements: T): ArrayDeque<T> {
    return if (elements.isEmpty()) ArrayDeque()
    else elements.toCollection(ArrayDeque())
}

/**
 * Find the index of the minimal element of an array.
 * @param [arr] an array of any Comparable type.
 * @return the index of the first minimal element in the array.
 */
fun <T: Comparable<T>> indexOfMin(arr: Array<T>) : Int {
    var minIndex = 0
    val minItem = arr[0]

    for(i in 1 .. arr.size) {
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
    if(this.isEmpty()) return emptyList()
    if(partitionSize < 1) throw IllegalArgumentException("partitionSize must be positive")

    val result = ArrayList<List<T>>()
    var entry = ArrayList<T>(partitionSize)
    for (item in this) {
        if(entry.size == partitionSize) {
            result.add(entry)
            entry = ArrayList<T>()
        }
        entry.add(item)
    }
    result.add(entry)
    return result
}

/**
 * like map, but multithreaded. It uses the number of cores + 2 threads.
 */
inline fun <T,V> List<T>.pmap(operation: (T) -> V): List<V> {
    val threads = ArrayList<Thread>()
    val cores = Runtime.getRuntime().availableProcessors();
    // run each thread on a partitioned block to minimize thread setup/teardown
    val partitioned = this.split(cores + 2);
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
fun <T> MutableList<T>.move(item: T, newIndex: Int)  {
    val currentIndex = indexOf(item)
    if (currentIndex < 0) return
    removeAt(currentIndex)
    add(newIndex, item)
}

/**
 * Moves the given item at the `oldIndex` to the `newIndex`
 */
fun <T> MutableList<T>.moveAt(oldIndex: Int, newIndex: Int)  {
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
    check(newIndex in 0..(size - 1))
    val split = partition(predicate)
    clear()
    addAll(split.second)
    addAll(if (newIndex >= size) size else newIndex,split.first)
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
    if (currentIndex <=0) return false
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
    if (newIndex >= size)  return false
    remove(item)
    add(newIndex, item)
    return true
}


/**
 * Moves first element **T** up an index that satisfies the given **predicate**, unless its already at the top
 */
inline fun <T> MutableList<T>.moveUp(crossinline predicate: (T) -> Boolean)  = find(predicate)?.let { moveUp(it) }

/**
 * Moves first element **T** down an index that satisfies the given **predicate**, unless its already at the bottom
 */
inline fun <T> MutableList<T>.moveDown(crossinline predicate: (T) -> Boolean)  = find(predicate)?.let { moveDown(it) }

/**
 * Moves all **T** elements up an index that satisfy the given **predicate**, unless they are already at the top
 */
inline fun <T> MutableList<T>.moveUpAll(crossinline predicate: (T) -> Boolean)  = asSequence().withIndex()
    .filter { predicate.invoke(it.value) }
    .forEach { moveUpAt(it.index) }

/**
 * Moves all **T** elements down an index that satisfy the given **predicate**, unless they are already at the bottom
 */
inline fun <T> MutableList<T>.moveDownAll(crossinline predicate: (T) -> Boolean)  = asSequence().withIndex()
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
    if (isInBounds(i) && isInBounds(j)){
        Collections.swap(this, i, j)
    }
    return this
}

private fun <T> List<T>.isInBounds(index: Int): Boolean {
    return index in 0..lastIndex
}


inline fun <T> List<T>.startWith(item: T): List<T> {
    val list = this.toMutableList()
    list.add(0, item)
    return list
}

inline fun <T> List<T>.startWith(data: List<T>): List<T> {
    val list = this.toMutableList()
    list.addAll(0, data)
    return list
}

inline fun <T> List<T>.startWithIfNotEmpty(item: T): List<T> {
    if (this.isNotEmpty()){
        return startWith(item)
    }
    return this
}

inline fun <K, V> MutableMap<K, MutableList<V>>.deepCopy(): MutableMap<K, MutableList<V>> {
    val result = mutableMapOf<K, MutableList<V>>()
    for ((key, value) in this){
        result[key] = value.toMutableList()
    }
    return result
}

fun <T> MutableList<T>.doIf(predicate: Boolean, action: MutableList<T>.() -> Any): MutableList<T> {
    if (predicate){
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

fun <T> LongSparseArray<T>.toList(): List<T>{
    val list = mutableListOf<T>()

    this.forEach { _, value -> list.add(value) }

    return list
}

fun <T> LongSparseArray<T>.toggle(key: Long, item: T){
    val current = this.get(key)
    if (current == null){
        this.append(key, item)
    } else {
        this.remove(key)
    }
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
    for (i in 1..size){
        resultList.add( kotlin.random.Random.nextInt(range.start, range.endInclusive))
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

fun <T: Comparable<T>> List<T>.isListSorted(): Boolean {
    return this == this.sorted()
}

val List<Int>.isBinarySearcheable: Boolean get()  {
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

fun <S: MutableList<T>, T> S.addAnd(index: Int, item: T): S {
    add(index, item)
    return this
}

fun <S: MutableCollection<T>, T> S.addAnd(item: T): S{
    add(item)
    return this
}

fun <T> MutableCollection<T>.addAll(vararg items: T) {
    addAll(items)
}

val <T> Collection<T>?.isNullOrEmpty: Boolean get()  {
    return (this == null || this.isEmpty())
}
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
    return if(this?.isEmpty() == true) "" else string
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


fun <T> Comparator<T>.sort(list: MutableList<T>) {
    list.sortWith(this)
}