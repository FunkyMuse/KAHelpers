package com.crazylegend.kotlinextensions.numbers

import androidx.annotation.IntRange
import java.math.RoundingMode
import java.text.DecimalFormat


/**
 * Created by hristijan on 2/20/19 to long live and prosper !
 */


val Number.getSatoshi: Double
    get() {
        return this.toDouble().times(0.00000001)
    }


val Int.numberOfDigits get() = toString().length

val Int.isEven get() = this % 2 == 0

val Int.isOdd get() = this % 2 != 0

val Int.isPositive get() = this > 0

val Int.isNegative get() = this < 0

val Int.tenth get() = this / 10
val Int.fourth get() = this / 4
val Int.quarter get() = fourth
val Int.third get() = this / 3
val Int.half get() = this / 2

val Int.doubled get() = this * 2
val Int.tripled get() = this * 3
val Int.quadrupled get() = this * 4

val Int.squared get() = this * this
val Int.cubed get() = this * this * this

val Int.sqrt get() = Math.sqrt(this.toDouble())
val Int.cbrt get() = Math.cbrt(this.toDouble())


fun Int.gcd(n: Int): Int {
    return if (n == 0) this else n.gcd(this % n)
}

infix fun Int.pow(power: Number): Double {
    return Math.pow(this.toDouble(), power.toDouble())
}

infix fun Int.toThe(power: Number): Double {
    return pow(power)
}

infix fun Int.root(n: Number): Double {
    return pow(1/n.toDouble())
}

/**
 * int time to 2 digit String
 */
fun Int.twoDigitTime() = if (this < 10) "0" + toString() else toString()



/**
 * Convert Celsius temperature to Fahrenheit
 */
fun Double.celsiusToFahrenheit() : Double = (this * 1.8) + 32

/**
 * Convert Fahrenheit temperature to Celsius
 */
fun Double.fahrenheitToCelsius() : Double = (this - 32) * 5/9


/**
 * Convert meters to miles
 */
val Double.metersToMiles: Double get() {
    return if (this != 0.0) {
        this / 1609.344
    } else -1.0
}



/**
 * Convert miles to meters
 */
val Double.milesToMeters: Double get() {
    return if (this != 0.0) {
       this * 1609.344
    } else -1.0
}




/**
 * Convets given bytes to human readable form.
 */
fun Long.convertBytesToHumanReadableForm(si: Boolean): String {
    val unit = if (si) 1000 else 1024
    if (this < unit) return toString() + " B"
    val exp = (Math.log(toDouble()) / Math.log(unit.toDouble())).toInt()
    val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i"
    return String.format("%.1f %sB", this / Math.pow(unit.toDouble(), exp.toDouble()), pre)
}


/* Divides */

infix fun Int.divides(other: Int): Boolean {
    return this == other || this % other == 0
}

/* Does Not Divide */
infix fun Int.doesNotDivide(other: Int): Boolean {
    return !(this divides other)
}

/**
* Returns Zero (0) if this number is null
*/
val Number?.orZero: Number get() =  this ?: 0

/**
 * Returns true if this number is null or zero (0)
 */
val Number?.isNullOrZero: Boolean get() = this == null || this == 0

/**
 * Returns true if this number is not null or zero (0)
 */
val Int?.isNotNullAndMoreThanZero: Boolean get() = this != null && this > 0

/**
 * Returns true if this number is not null or zero (0)
 */
val Long?.isNotNullAndMoreThanZero: Boolean get() = this != null && this > 0L

/**
 * Returns true if this number is not null or zero (0)
 */
val Float?.isNotNullAndMoreThanZero: Boolean get() = this != null && this > 0F

/**
 * Returns true if this number is not null or zero (0)
 */
val Double?.isNotNullAndMoreThanZero: Boolean get() = this != null && this > 0.0

fun Number.round(@IntRange(from = 1L) decimalCount: Int): String {
    val expression = StringBuilder().append("#.")
    (1..decimalCount).forEach { expression.append("#") }
    val formatter = DecimalFormat(expression.toString())
    formatter.roundingMode = RoundingMode.HALF_UP
    return formatter.format(this)
}