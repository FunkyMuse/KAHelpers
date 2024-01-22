package dev.funkymuse.math

import android.content.Context
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.random.Random




fun Double.sin(): Double = Math.sin(this)

fun Double.cos(): Double = Math.cos(this)
fun Double.tan(): Double = Math.tan(this)
fun Double.asin(): Double = Math.asin(this)
fun Double.acos(): Double = Math.acos(this)
fun Double.atan(): Double = Math.atan(this)
fun Double.toRadians(): Double = Math.toRadians(this)
fun Double.toDegrees(): Double = Math.toDegrees(this)
fun Double.exp(): Double = Math.exp(this)
fun Double.log(): Double = Math.log(this)
fun Double.log10(): Double = Math.log10(this)
fun Double.sqrt(): Double = Math.sqrt(this)
fun Double.cbrt(): Double = Math.cbrt(this)
fun Double.IEEEremainder(divisor: Double): Double = Math.IEEEremainder(this, divisor)
fun Double.ceil(): Double = Math.ceil(this)
fun Double.floor(): Double = Math.floor(this)
fun Double.rint(): Double = Math.rint(this)
fun Double.atan2(x: Double): Double = Math.atan2(this, x)
fun Double.pow(exp: Double): Double = Math.pow(this, exp)
fun Double.round(): Long = Math.round(this)
fun Double.abs(): Double = Math.abs(this)
fun Double.ulp(): Double = Math.ulp(this)
fun Double.signum(): Double = Math.signum(this)
fun Double.sinh(): Double = Math.sinh(this)
fun Double.cosh(): Double = Math.cosh(this)
fun Double.tanh(): Double = Math.tanh(this)
fun Double.expm1(): Double = Math.expm1(this)
fun Double.log1p(): Double = Math.log1p(this)
fun Double.copySign(sign: Double): Double = Math.copySign(this, sign)
fun Double.exponent(): Int = Math.getExponent(this)
fun Double.next(direction: Double): Double = Math.nextAfter(this, direction)
fun Double.nextUp(): Double = Math.nextUp(this)
fun Double.scalb(scaleFactor: Int): Double = Math.scalb(this, scaleFactor)
fun Double.clamp(min: Double, max: Double): Double = Math.max(min, Math.min(this, max))
fun Float.sin(): Float = FloatMath.sin(this)
fun Float.cos(): Float = FloatMath.cos(this)
fun Float.tan(): Float = FloatMath.tan(this)
fun Float.asin(): Float = FloatMath.asin(this)
fun Float.acos(): Float = FloatMath.acos(this)
fun Float.atan(): Float = FloatMath.atan(this)
fun Float.toRadians(): Float = FloatMath.toRadians(this)
fun Float.toDegrees(): Float = FloatMath.toDegrees(this)
fun Float.exp(): Float = FloatMath.exp(this)
fun Float.log(): Float = FloatMath.log(this)
fun Float.log10(): Float = FloatMath.log10(this)
fun Float.sqrt(): Float = FloatMath.sqrt(this)
fun Float.cbrt(): Float = FloatMath.cbrt(this)
fun Float.IEEEremainder(divisor: Float): Float = FloatMath.IEEEremainder(this, divisor)
fun Float.ceil(): Float = FloatMath.ceil(this)
fun Float.floor(): Float = FloatMath.floor(this)
fun Float.rint(): Float = FloatMath.rint(this)
fun Float.atan2(x: Float): Float = FloatMath.atan2(this, x)
fun Float.pow(exp: Float): Float = FloatMath.pow(this, exp)
fun Float.round(): Int = Math.round(this)
fun Float.abs(): Float = Math.abs(this)
fun Float.ulp(): Float = Math.ulp(this)
fun Float.signum(): Float = Math.signum(this)
fun Float.sinh(): Float = FloatMath.sinh(this)
fun Float.cosh(): Float = FloatMath.cosh(this)
fun Float.tanh(): Float = FloatMath.tanh(this)
fun Float.expm1(): Float = FloatMath.expm1(this)
fun Float.log1p(): Float = FloatMath.log1p(this)
fun Float.copySign(sign: Float): Float = Math.copySign(this, sign)
fun Float.exponent(): Int = Math.getExponent(this)
fun Float.next(direction: Float): Float = FloatMath.nextAfter(this, direction)
fun Float.next(direction: Double): Float = Math.nextAfter(this, direction)
fun Float.nextUp(): Float = Math.nextUp(this)
fun Float.scalb(scaleFactor: Int): Float = Math.scalb(this, scaleFactor)
fun Float.clamp(min: Float, max: Float): Float = Math.max(min, Math.min(this, max))

fun Number?.orZero(): Number = this ?: 0

fun Int?.orZero(): Int = this ?: 0

fun Float?.orZero(): Float = this ?: 0.0f

fun Double?.orZero(): Double = this ?: 0.0

fun Int.getString(context: Context?): String? {
    return context?.getString(this)
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

fun Double.roundDown(pattern: String): String {
    return format(pattern, RoundingMode.DOWN)
}

fun Double.roundHalfUp() = BigDecimal(this).setScale(0, BigDecimal.ROUND_HALF_UP).toDouble()

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

fun Float.valueToPercentOfRange(min: Float = 0f, max: Float) = (this - min) / (max - min)
fun Float.percentToValueOfRange(min: Float = 0f, max: Float) = min + this * (max - min)

fun Int.sleep() = this.toLong().sleep()

fun Long.sleep() = Thread.sleep(this)

fun Int.randomStrings(): String {
    val randomStringBuilder = StringBuilder()
    var tempChar: Char
    for (i in 0 until this) {
        tempChar = (Random.nextInt(96) + 32).toChar()
        randomStringBuilder.append(tempChar)
    }
    return randomStringBuilder.toString()
}

/**
 * Linearly interpolate between two values.
 */
fun lerp(a: Float, b: Float, t: Float): Float {
    return a + (b - a) * t
}


object FloatMath {
    val PI: Float = Math.PI.toFloat()
    val E: Float = Math.E.toFloat()

    fun sin(value: Float): Float = Math.sin(value.toDouble()).toFloat()
    fun cos(value: Float): Float = Math.cos(value.toDouble()).toFloat()
    fun tan(value: Float): Float = Math.tan(value.toDouble()).toFloat()
    fun sqrt(value: Float): Float = Math.sqrt(value.toDouble()).toFloat()
    fun acos(value: Float): Float = Math.acos(value.toDouble()).toFloat()
    fun asin(value: Float): Float = Math.asin(value.toDouble()).toFloat()
    fun atan(value: Float): Float = Math.atan(value.toDouble()).toFloat()
    fun atan2(x: Float, y: Float): Float = Math.atan2(x.toDouble(), y.toDouble()).toFloat()
    fun pow(x: Float, y: Float): Float = Math.pow(x.toDouble(), y.toDouble()).toFloat()
    fun ceil(x: Float): Float = Math.ceil(x.toDouble()).toFloat()
    fun floor(x: Float): Float = Math.floor(x.toDouble()).toFloat()
    fun toRadians(angdeg: Float): Float = Math.toRadians(angdeg.toDouble()).toFloat()
    fun toDegrees(angrad: Float): Float = Math.toDegrees(angrad.toDouble()).toFloat()
    fun exp(x: Float): Float = Math.exp(x.toDouble()).toFloat()
    fun log(x: Float): Float = Math.log(x.toDouble()).toFloat()
    fun log10(x: Float): Float = Math.log10(x.toDouble()).toFloat()
    fun cbrt(x: Float): Float = Math.cbrt(x.toDouble()).toFloat()
    fun IEEEremainder(x: Float, y: Float): Float = Math.IEEEremainder(x.toDouble(), y.toDouble()).toFloat()
    fun rint(x: Float): Float = Math.rint(x.toDouble()).toFloat()
    fun sinh(x: Float): Float = Math.sinh(x.toDouble()).toFloat()
    fun cosh(x: Float): Float = Math.cosh(x.toDouble()).toFloat()
    fun tanh(x: Float): Float = Math.tanh(x.toDouble()).toFloat()
    fun expm1(x: Float): Float = Math.expm1(x.toDouble()).toFloat()
    fun log1p(x: Float): Float = Math.log1p(x.toDouble()).toFloat()
    fun nextAfter(start: Float, direction: Float): Float = Math.nextAfter(start, direction.toDouble())
    fun clamp(value: Float, min: Float, max: Float): Float = Math.max(min, Math.min(value, max))
}