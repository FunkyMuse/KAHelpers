package com.crazylegend.common


/**
 * Created by hristijan on 3/1/19 to long live and prosper !
 */

/**
 * Converts Boolean to Int, if true then 1 else 0
 */
fun Boolean.toInt(): Int = if (this) 1 else 0

/**
 * Toggle the Boolean Value, if it's true then it will become false ....
 */
fun Boolean.toggle() = !this

inline fun <reified T> Boolean.whether(yes: () -> T, no: () -> T): T = if (this) yes() else no()

inline fun <reified T> Boolean.either(t: T): Pair<Boolean, T> = Pair(this, t)

inline infix fun <reified T> Pair<Boolean, T>.or(t: T): T = if (first) second else t


fun Boolean?.orFalse(): Boolean = this ?: false
fun Boolean?.orTrue(): Boolean = this ?: true

fun Boolean?.nullAsFalse(): Boolean {
    return this ?: false
}

fun Boolean?.nullAsTrue(): Boolean {
    return this ?: true
}


inline fun Boolean.ifTrue(function: () -> Unit): Boolean {
    if (this) function()
    return this
}


inline fun Boolean.ifFalse(function: () -> Unit): Boolean {
    if (!this) function()
    return this
}


inline fun Boolean.ifTrue(falseFunction: () -> Unit = {}, trueFunction: () -> Unit): Boolean {
    if (this) trueFunction() else falseFunction()
    return this
}

inline fun Boolean.ifFalse(trueFunction: () -> Unit = {}, falseFunction: () -> Unit): Boolean {
    if (this) trueFunction() else falseFunction()
    return this
}
