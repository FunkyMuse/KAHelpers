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