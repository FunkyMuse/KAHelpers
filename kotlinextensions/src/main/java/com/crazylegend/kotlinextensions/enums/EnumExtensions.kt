package com.crazylegend.kotlinextensions.enums


/**
 * Created by hristijan on 3/20/19 to long live and prosper !
 */

inline fun <reified T : Enum<T>> find(predicate: (item: T) -> Boolean): T? = enumValues<T>().find { predicate(it) }
inline fun <reified T : Enum<T>> first(predicate: (item: T) -> Boolean): T = enumValues<T>().first { predicate(it) }

inline fun <reified T : Enum<T>> convert(ord: Int): T = enumValues<T>()[ord]