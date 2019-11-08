package com.crazylegend.kotlinextensions


/**
 * Created by hristijan on 3/1/19 to long live and prosper !
 */

/**
 * Converts Boolean to Int, if true then 1 else 0
 */
fun Boolean.toInt():Int = if(this) 1 else 0

/**
 * Toggle the Boolean Value, if it's true then it will become false ....
 */
fun Boolean.toggle() = !this

inline fun <reified T> Boolean.whether(yes: () -> T, no: () -> T): T = if (this) yes() else no()

inline fun <reified T> Boolean.either(t: T): Pair<Boolean, T> = Pair(this, t)

inline infix fun <reified T> Pair<Boolean, T>.or(t: T): T = if (first) second else t

fun Boolean.onTrue(function: () -> Unit) {
    if (this) {
        function()
    }
}

fun Boolean.onFalse(function: () -> Unit) {
    if (!this) {
        function()
    }
}

fun Boolean?.orFalse(): Boolean = this ?: false

fun Boolean?.nullAsFalse(): Boolean {
    return this ?: false
}

fun Boolean?.nullAsTrue(): Boolean {
    return this ?: false
}
