package dev.funkymuse.datastructuresandalgorithms.memoization

class Memoize<in T, out R>(val function: (type: T) -> R) : (T) -> R {

    private val valuesCache = mutableMapOf<T, R>()

    override fun invoke(invocationType: T): R = valuesCache.getOrPut(invocationType) { function(invocationType) }

}