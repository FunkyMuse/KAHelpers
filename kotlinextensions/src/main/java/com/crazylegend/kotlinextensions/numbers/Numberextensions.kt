package com.crazylegend.kotlinextensions.numbers

import android.content.Context
import android.os.Build
import androidx.annotation.DimenRes
import androidx.annotation.IntRange
import androidx.annotation.RequiresApi
import com.crazylegend.kotlinextensions.byteArray
import com.crazylegend.kotlinextensions.math.log
import com.crazylegend.kotlinextensions.xorAll
import java.lang.Double.doubleToRawLongBits
import java.lang.Double.longBitsToDouble
import java.lang.Float.floatToRawIntBits
import java.lang.Float.intBitsToFloat
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.Duration
import java.util.*
import kotlin.experimental.xor
import kotlin.math.ln
import kotlin.math.pow


/**
 * Created by hristijan on 2/20/19 to long live and prosper !
 */

fun Int.sleep() = this.toLong().sleep()

fun Long.sleep() = Thread.sleep(this)

fun Int.randomStrings(): String {
    val randomStringBuilder = StringBuilder()
    var tempChar: Char
    for (i in 0 until this) {
        tempChar = (Random().nextInt(96) + 32).toChar()
        randomStringBuilder.append(tempChar)
    }
    return randomStringBuilder.toString()
}

val Number.getSatoshi: Double
    get() {
        return this.toDouble().times(0.00000001)
    }

fun Float.ieee754(): Int = floatToRawIntBits(this)
fun Double.ieee754(): Long = doubleToRawLongBits(this)
fun Int.ieee754(): Float = intBitsToFloat(this)
fun Long.ieee754(): Double = longBitsToDouble(this)

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
    return pow(1 / n.toDouble())
}

/**
 * int time to 2 digit String
 */
fun Int.twoDigitTime() = if (this < 10) "0" + toString() else toString()


/**
 * Convert Celsius temperature to Fahrenheit
 */
fun Double.celsiusToFahrenheit(): Double = (this * 1.8) + 32

/**
 * Convert Fahrenheit temperature to Celsius
 */
fun Double.fahrenheitToCelsius(): Double = (this - 32) * 5 / 9


/**
 * Convert meters to miles
 */
val Double.metersToMiles: Double
    get() {
        return if (this != 0.0) {
            this / 1609.344
        } else -1.0
    }

/**
 * Converts meters to km
 */
val Double.metersToKM get() = this / 1000

/**
 * Returns kilometers to meters
 */
val Double.kilometersToMeters get() = this * 1000

/**
 * Convert miles to meters
 */
val Double.milesToMeters: Double
    get() {
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
    val exp = (ln(toDouble()) / ln(unit.toDouble())).toInt()
    val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i"
    return String.format("%.1f %sB", this / unit.toDouble().pow(exp.toDouble()), pre)
}


/**
 * Is the integer dividable
 * @receiver Int
 * @param other Int
 * @return Boolean
 */
infix fun Int.divides(other: Int): Boolean {
    return this == other || this % other == 0
}

/**
 * Is the integer not dividable
 * @receiver Int
 * @param other Int
 * @return Boolean
 */
infix fun Int.doesNotDivide(other: Int): Boolean {
    return !(this divides other)
}

/**
 * Returns Zero (0) if this number is null
 */
val Number?.orZero: Number get() = this ?: 0

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
    repeat((1..decimalCount).count()) { expression.append("#") }
    val formatter = DecimalFormat(expression.toString())
    formatter.roundingMode = RoundingMode.HALF_UP
    return formatter.format(this)
}

/**
 * Decompose an [Int] value into it's component digits.
 *
 * @return An ordered list of each digit.
 */
fun Int.digits(): List<Int> {
    var value = this
    val digits = mutableListOf<Int>()
    do {
        digits.add(value % 10)
        value /= 10
    } while (value > 0 || value < 0)
    return digits.reversed()
}


fun BigDecimal.isZero() = this == BigDecimal.ZERO
fun BigDecimal.isNotZero() = !isZero()

val Number.nanos @RequiresApi(Build.VERSION_CODES.O) get() = Duration.ofNanos(this.toLong())!!
val Number.millis @RequiresApi(Build.VERSION_CODES.O) get() = Duration.ofMillis(this.toLong())!!
val Number.seconds @RequiresApi(Build.VERSION_CODES.O) get() = Duration.ofSeconds(this.toLong())!!
val Number.minutes @RequiresApi(Build.VERSION_CODES.O) get() = Duration.ofMinutes(this.toLong())!!
val Number.hours @RequiresApi(Build.VERSION_CODES.O) get() = Duration.ofHours(this.toLong())!!
val Number.days @RequiresApi(Build.VERSION_CODES.O) get() = Duration.ofDays(this.toLong())!!

fun Number.toHexString(width: Int) = String.format("0x%0${width}X", this)

inline val Long.seconds @RequiresApi(Build.VERSION_CODES.O) get() = Duration.ofSeconds(this)
inline val Int.seconds get() = toLong().seconds

infix fun Int.erase(n: Int): Int {
    val p = 10 pow n
    if (p > Int.MAX_VALUE) {
        throw  IndexOutOfBoundsException("divisor: $p > Int.MAX_VALUE")
    }
    val divisor = p.toInt()
    return (this / divisor) * divisor
}


fun Double.format(pattern: String): String {
    val df = DecimalFormat(pattern)
    return df.format(this)
}

fun Double.format(pattern: String, init: DecimalFormat.() -> Unit): String {
    val df = DecimalFormat(pattern)
    df.init()
    return df.format(this)
}

fun Double.format(pattern: String, roundingMode: RoundingMode): String {
    return format(pattern) {
        this.roundingMode = roundingMode
    }
}

fun Double.format(pattern: String, groupingSeperator: Char): String {
    val symbols = DecimalFormatSymbols()
    symbols.groupingSeparator = groupingSeperator
    val df = DecimalFormat(pattern, symbols)
    return df.format(this)
}

fun Double.format(pattern: String, groupingSeperator: Char, decimalSeperator: Char): String {
    val symbols = DecimalFormatSymbols()
    symbols.decimalSeparator = decimalSeperator
    symbols.groupingSeparator = groupingSeperator
    val df = DecimalFormat(pattern, symbols)
    return df.format(this)
}

fun Double.roundDown(pattern: String): String {
    return format(pattern, RoundingMode.DOWN)
}

fun Double.roundHalfUp() = BigDecimal(this).setScale(0, BigDecimal.ROUND_HALF_UP).toDouble()

fun Double.roundToZero() = BigDecimal(this).setScale(0, BigDecimal.ROUND_DOWN).toDouble()


fun Int.getString(context: Context?): String? {
    return context?.getString(this)
}

fun Int.forEach(callback: (i: Int) -> Unit) {
    for (i in 0 until this) {
        callback(i)
    }
}


fun Long.toBigInteger() = BigInteger(toString())
fun Number.toBigInteger() = toLong().toBigInteger()

fun Double.toBigDecimal(mathContext: MathContext = MathContext.UNLIMITED) = BigDecimal(this, mathContext)
fun Number.toBigDecimal(mathContext: MathContext = MathContext.UNLIMITED) = toDouble().toBigDecimal(mathContext)

fun BigInteger.even() = mod(2L.toBigInteger()) == BigInteger.ZERO
fun BigInteger.odd() = !even()

infix fun BigInteger.pow(exp: Int): BigInteger = pow(exp)
infix fun BigDecimal.pow(exp: Int): BigDecimal = pow(exp)

fun BigInteger.fitsInLong(): Boolean = this in (Long.MIN_VALUE.toBigInteger()..Long.MAX_VALUE.toBigInteger())
fun BigInteger.fitsInInt(): Boolean = this in (Int.MIN_VALUE.toBigInteger()..Int.MAX_VALUE.toBigInteger())

fun BigDecimal.fitsInDouble(): Boolean = this in (-Double.MAX_VALUE.toBigDecimal()..Double.MAX_VALUE.toBigDecimal())
fun BigDecimal.fitsInFloat(): Boolean = this in (-Float.MAX_VALUE.toBigDecimal()..Float.MAX_VALUE.toBigDecimal())

fun BigDecimal.round(precision: Int = 1, roundingMode: RoundingMode = RoundingMode.HALF_UP): BigDecimal = round(MathContext(precision, roundingMode))

fun MathContext.round(n: BigDecimal): BigDecimal = run(n::round)

fun MathContext.roundFunctionBigDecimal(): (BigDecimal) -> BigDecimal = { round(it) }

fun Double.round(mathContext: MathContext = MathContext(1)): BigDecimal = run(mathContext::round)
fun Float.round(mathContext: MathContext = MathContext(1)): BigDecimal = run(mathContext::round)

fun Double.round(precision: Int = 1, roundingMode: RoundingMode = RoundingMode.HALF_UP): BigDecimal = round(MathContext(precision, roundingMode))
fun Float.round(precision: Int = 1, roundingMode: RoundingMode = RoundingMode.HALF_UP): BigDecimal = round(MathContext(precision, roundingMode))

fun MathContext.round(n: Double): BigDecimal = run(n.toBigDecimal()::round)
fun MathContext.round(n: Float): BigDecimal = run(n.toBigDecimal()::round)

fun MathContext.roundFunctionDouble(): (Double) -> BigDecimal = { round(it) }
fun MathContext.roundFunctionFloat(): (Float) -> BigDecimal = { round(it) }

fun Number.roundDiv(y: Number, mathContext: MathContext): BigDecimal = toBigDecimal().divide(y.toBigDecimal(), mathContext)

fun BigInteger.ln(): Double = log()
fun BigInteger.log(): Double {
    var ref = this
    val blex = bitLength() - 1022 // any value in 60..1023 is ok
    if (blex > 0) ref = shiftRight(blex)
    val res = ref.toDouble().log()
    return if (blex > 0) res + blex * LOG2 else res
}


fun Number.fibonacci(): BigInteger = toBigInteger().fibonacci()
fun BigInteger.fibonacci(): BigInteger {
    require(this >= BigInteger.ZERO) { "Cannot compute fibonacci for negative numbers" }
    fun fib(n: BigInteger): Pair<BigInteger, BigInteger> {
        if (n == BigInteger.ZERO) return BigInteger.ZERO to BigInteger.ONE
        val (a, b) = fib(n / TWO_BIG)
        val c = a * (b * TWO_BIG - a)
        val d = a * a + b * b
        return if (n % TWO_BIG == BigInteger.ZERO) c to d else d to c + d
    }
    return fib(this).first
}

fun probablePrime(bits: Int, random: Random = Random()) = BigInteger.probablePrime(bits, random)

fun BigInteger.isPrime(certainty: Int = 5, precise: Boolean = false): Boolean {
    if (!isProbablePrime(certainty)) return false else if (!precise) return true

    if (TWO_BIG != this && mod(TWO_BIG) == BigInteger.ZERO) return false

    var index = BigInteger.ONE
    val indices = generateSequence { index += TWO_BIG; if (index.multiply(index) <= this) index else null }
    indices.forEach {
        if (mod(it) == BigInteger.ZERO) return false
    }
    return true;
}


val LOG2 by lazy { Math.log(2.0) }
val TWO_BIG by lazy { 2.toBigInteger() }

inline fun <reified T : Number> Context?.dimen(@DimenRes res: Int): T =
        (if (this == null) 0f else resources.getDimension(res)).let { dimen ->
            when (T::class) {
                Float::class -> dimen as T
                Int::class -> dimen.toInt() as T
                Double::class -> dimen.toDouble() as T
                Long::class -> dimen.toLong() as T
                Short::class -> dimen.toShort() as T
                else -> throw IllegalArgumentException("Unknown dimen type")
            }
        }


/**
 * Calculates the percentage of a value in a given range.
 *
 * @param value Input value.
 * @param min   - Min Range Value.
 * @param max   - Max Range Value.
 * @return Percentage of a given value in a range. If within range, returned values are between [0f,1f]
 */
fun Float.valueToPercentOfRange(min: Float = 0f, max: Float) = (this - min) / (max - min)

/**
 * Calculates a value in a given range by percentage.
 *
 * @param percent - Percentage. Expecting a value [0f,1f] if expected to be in range.
 * @param min     - Min Range Value.
 * @param max     - Max Range Value.
 * @return Concrete value by a given percentage. If percentage value [0f, 1f] then the returned value will be in [min, max] range.
 */
fun Float.percentToValueOfRange(min: Float = 0f, max: Float) = min + this * (max - min)


fun Short.clamp(min: Short, max: Short): Short {
    if (this < min) return min
    return if (this > max) max else this
}

fun Int.clamp(min: Int, max: Int): Int {
    if (this < min) return min
    return if (this > max) max else this
}

fun Long.clamp(min: Long, max: Long): Long {
    if (this < min) return min
    return if (this > max) max else this
}

fun Float.clamp(min: Float, max: Float): Float {
    if (this < min) return min
    return if (this > max) max else this
}

fun Double.clamp(min: Double, max: Double): Double {
    if (this < min) return min
    return if (this > max) max else this
}

const val BYTES_TO_KB: Long = 1024
const val BYTES_TO_MB = BYTES_TO_KB * 1024
const val BYTES_TO_GB = BYTES_TO_MB * 1024
const val BYTES_TO_TB = BYTES_TO_GB * 1024

/**
 * alternative to Formatter.formatFileSize which doesn't show bytes and rounds to int
 */
fun Long.formatBytes(): String {
    if (this <= 0)
        return "0 bytes"

    return when {
        this / BYTES_TO_TB > 0 -> String.format("%.2f TB", this / BYTES_TO_TB.toFloat())
        this / BYTES_TO_GB > 0 -> String.format("%.2f GB", this / BYTES_TO_GB.toFloat())
        this / BYTES_TO_MB > 0 -> String.format("%.2f MB", this / BYTES_TO_MB.toFloat())
        this / BYTES_TO_KB > 0 -> String.format("%.2f KB", this / BYTES_TO_KB.toFloat())
        else -> "$this bytes"
    }
}


infix fun Byte.xorx(short: Short) = this xorx short.byteArray

infix fun Byte.xorx(byteArray: ByteArray?) = if (byteArray == null) this else (this xor byteArray.xorAll)

infix fun Byte.shlx(bitCount: Int) = (toInt() and 0xFF) shl bitCount

infix fun Byte.shrx(bitCount: Int) = (toInt() and 0xFF) shr bitCount

infix fun Byte.shr(bitCount: Int) = (this shrx bitCount).toByte()

infix fun Byte.shl(bitCount: Int) = (this shlx bitCount).toByte()

infix fun Byte.equals(value: Int) = this == value.toByte()

infix fun Byte.notEquals(value: Int) = this != value.toByte()
