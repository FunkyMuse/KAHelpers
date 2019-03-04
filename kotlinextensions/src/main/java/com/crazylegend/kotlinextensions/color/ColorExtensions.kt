package com.crazylegend.kotlinextensions.color


/**
 * Created by hristijan on 3/4/19 to long live and prosper !
 */


/**
 * Return the color with 0xFF opacity.
 * E.g., `0xabcdef` will be translated to `0xFFabcdef`.
 */
val Int.opaque: Int
    get() = this or 0xff000000.toInt()


/**
 * Return the color with the given alpha value.
 * Examples:
 * ```
 *   0xabcdef.withAlpha(0xCF) == 0xCFabcdef
 *   0xFFabcdef.withAlpha(0xCF) == 0xCFabcdef
 * ```
 *
 * @param alpha the alpha channel value: [0x0..0xFF].
 * @return the color with the given alpha value applied.
 */
fun Int.withAlpha(alpha: Int): Int {
    require(alpha in 0..0xFF)
    return this and 0x00FFFFFF or (alpha shl 24)
}