package com.crazylegend.datastructuresandalgorithms.memoization

/**
 * Created by crazy on 1/4/21 to long live and prosper !
 */
class Memoize<in T, out R>(val function: (type: T) -> R) : (T) -> R {

    private val valuesCache = mutableMapOf<T, R>()

    override fun invoke(invocationType: T): R = valuesCache.getOrPut(invocationType) { function(invocationType) }

}