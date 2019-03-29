package com.crazylegend.kotlinextensions.math

import android.content.Context
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.random.Random


/**
 * Created by hristijan on 2/22/19 to long live and prosper !
 */

inline fun Double.sin(): Double = Math.sin(this)
inline fun Double.cos(): Double = Math.cos(this)
inline fun Double.tan(): Double = Math.tan(this)
inline fun Double.asin(): Double = Math.asin(this)
inline fun Double.acos(): Double = Math.acos(this)
inline fun Double.atan(): Double = Math.atan(this)
inline fun Double.toRadians(): Double = Math.toRadians(this)
inline fun Double.toDegrees(): Double = Math.toDegrees(this)
inline fun Double.exp(): Double = Math.exp(this)
inline fun Double.log(): Double = Math.log(this)
inline fun Double.log10(): Double = Math.log10(this)
inline fun Double.sqrt(): Double = Math.sqrt(this)
inline fun Double.cbrt(): Double = Math.cbrt(this)
inline fun Double.IEEEremainder(divisor: Double): Double = Math.IEEEremainder(this, divisor)
inline fun Double.ceil(): Double = Math.ceil(this)
inline fun Double.floor(): Double = Math.floor(this)
inline fun Double.rint(): Double = Math.rint(this)
inline fun Double.atan2(x: Double): Double = Math.atan2(this, x)
inline fun Double.pow(exp: Double): Double = Math.pow(this, exp)
inline fun Double.round(): Long = Math.round(this)
inline fun Double.abs(): Double = Math.abs(this)
inline fun Double.ulp(): Double = Math.ulp(this)
inline fun Double.signum(): Double = Math.signum(this)
inline fun Double.sinh(): Double = Math.sinh(this)
inline fun Double.cosh(): Double = Math.cosh(this)
inline fun Double.tanh(): Double = Math.tanh(this)
inline fun Double.expm1(): Double = Math.expm1(this)
inline fun Double.log1p(): Double = Math.log1p(this)
inline fun Double.copySign(sign: Double): Double = Math.copySign(this, sign)
inline fun Double.exponent(): Int = Math.getExponent(this)
inline fun Double.next(direction: Double): Double = Math.nextAfter(this, direction)
inline fun Double.nextUp(): Double = Math.nextUp(this)
inline fun Double.scalb(scaleFactor: Int): Double = Math.scalb(this, scaleFactor)
inline fun Double.clamp(min: Double, max: Double): Double = Math.max(min, Math.min(this, max))
inline fun Float.sin(): Float = FloatMath.sin(this)
inline fun Float.cos(): Float = FloatMath.cos(this)
inline fun Float.tan(): Float = FloatMath.tan(this)
inline fun Float.asin(): Float = FloatMath.asin(this)
inline fun Float.acos(): Float = FloatMath.acos(this)
inline fun Float.atan(): Float = FloatMath.atan(this)
inline fun Float.toRadians(): Float = FloatMath.toRadians(this)
inline fun Float.toDegrees(): Float = FloatMath.toDegrees(this)
inline fun Float.exp(): Float = FloatMath.exp(this)
inline fun Float.log(): Float = FloatMath.log(this)
inline fun Float.log10(): Float = FloatMath.log10(this)
inline fun Float.sqrt(): Float = FloatMath.sqrt(this)
inline fun Float.cbrt(): Float = FloatMath.cbrt(this)
inline fun Float.IEEEremainder(divisor: Float): Float = FloatMath.IEEEremainder(this, divisor)
inline fun Float.ceil(): Float = FloatMath.ceil(this)
inline fun Float.floor(): Float = FloatMath.floor(this)
inline fun Float.rint(): Float = FloatMath.rint(this)
inline fun Float.atan2(x: Float): Float = FloatMath.atan2(this, x)
inline fun Float.pow(exp: Float): Float = FloatMath.pow(this, exp)
inline fun Float.round(): Int = Math.round(this)
inline fun Float.abs(): Float = Math.abs(this)
inline fun Float.ulp(): Float = Math.ulp(this)
inline fun Float.signum(): Float = Math.signum(this)
inline fun Float.sinh(): Float = FloatMath.sinh(this)
inline fun Float.cosh(): Float = FloatMath.cosh(this)
inline fun Float.tanh(): Float = FloatMath.tanh(this)
inline fun Float.expm1(): Float = FloatMath.expm1(this)
inline fun Float.log1p(): Float = FloatMath.log1p(this)
inline fun Float.copySign(sign: Float): Float = Math.copySign(this, sign)
inline fun Float.exponent(): Int = Math.getExponent(this)
inline fun Float.next(direction: Float): Float = FloatMath.nextAfter(this, direction)
inline fun Float.next(direction: Double): Float = Math.nextAfter(this, direction)
inline fun Float.nextUp(): Float = Math.nextUp(this)
inline fun Float.scalb(scaleFactor: Int): Float = Math.scalb(this, scaleFactor)
inline fun Float.clamp(min: Float, max: Float): Float = Math.max(min, Math.min(this, max))

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

object FloatMath {
    val PI: Float = Math.PI.toFloat()
    val E: Float = Math.E.toFloat()

    inline fun sin(value: Float): Float = Math.sin(value.toDouble()).toFloat()
    inline fun cos(value: Float): Float = Math.cos(value.toDouble()).toFloat()
    inline fun tan(value: Float): Float = Math.tan(value.toDouble()).toFloat()
    inline fun sqrt(value: Float): Float = Math.sqrt(value.toDouble()).toFloat()
    inline fun acos(value: Float): Float = Math.acos(value.toDouble()).toFloat()
    inline fun asin(value: Float): Float = Math.asin(value.toDouble()).toFloat()
    inline fun atan(value: Float): Float = Math.atan(value.toDouble()).toFloat()
    inline fun atan2(x: Float, y: Float): Float = Math.atan2(x.toDouble(), y.toDouble()).toFloat()
    inline fun pow(x: Float, y: Float): Float = Math.pow(x.toDouble(), y.toDouble()).toFloat()
    inline fun ceil(x: Float): Float = Math.ceil(x.toDouble()).toFloat()
    inline fun floor(x: Float): Float = Math.floor(x.toDouble()).toFloat()
    inline fun toRadians(angdeg: Float): Float = Math.toRadians(angdeg.toDouble()).toFloat()
    inline fun toDegrees(angrad: Float): Float = Math.toDegrees(angrad.toDouble()).toFloat()
    inline fun exp(x: Float): Float = Math.exp(x.toDouble()).toFloat()
    inline fun log(x: Float): Float = Math.log(x.toDouble()).toFloat()
    inline fun log10(x: Float): Float = Math.log10(x.toDouble()).toFloat()
    inline fun cbrt(x: Float): Float = Math.cbrt(x.toDouble()).toFloat()
    inline fun IEEEremainder(x: Float, y: Float): Float = Math.IEEEremainder(x.toDouble(), y.toDouble()).toFloat()
    inline fun rint(x: Float): Float = Math.rint(x.toDouble()).toFloat()
    inline fun sinh(x: Float): Float = Math.sinh(x.toDouble()).toFloat()
    inline fun cosh(x: Float): Float = Math.cosh(x.toDouble()).toFloat()
    inline fun tanh(x: Float): Float = Math.tanh(x.toDouble()).toFloat()
    inline fun expm1(x: Float): Float = Math.expm1(x.toDouble()).toFloat()
    inline fun log1p(x: Float): Float = Math.log1p(x.toDouble()).toFloat()
    inline fun nextAfter(start: Float, direction: Float): Float = Math.nextAfter(start, direction.toDouble())
    inline fun clamp(value: Float, min: Float, max: Float): Float = Math.max(min, Math.min(value, max))
}