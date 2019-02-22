package com.crazylegend.kotlinextensions.numbers


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