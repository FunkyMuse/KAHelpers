package com.crazylegend.kotlinextensions.math

import android.content.res.Resources
import com.crazylegend.kotlinextensions.generators.RandomXS128
import java.util.*
import kotlin.math.ln
import kotlin.math.roundToLong


/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */

class MathUtils @Throws(IllegalAccessException::class)
private constructor() {

    private object Sin {
        internal val table = FloatArray(SIN_COUNT)

        init {
            for (i in 0 until SIN_COUNT)
                table[i] = kotlin.math.sin(((i + 0.5f) / SIN_COUNT * radFull).toDouble()).toFloat()
            var i = 0
            while (i < 360) {
                table[(i * degToIndex).toInt() and SIN_MASK] = kotlin.math.sin((i * degreesToRadians).toDouble()).toFloat()
                i += 90
            }
        }
    }

    init {
        throw IllegalAccessException()
    }

    companion object {


        val nanoToSec = 1 / 1000000000f

        // ---
        val FLOAT_ROUNDING_ERROR = 0.000001f // 32 bits
        val PI = 3.1415927f
        val PI2 = PI * 2

        val E = 2.7182818f

        private val SIN_BITS = 14 // 16KB. Adjust for accuracy.
        private val SIN_MASK = (-1 shl SIN_BITS).inv()
        private val SIN_COUNT = SIN_MASK + 1

        private val radFull = PI * 2
        private val degFull = 360f
        private val radToIndex = SIN_COUNT / radFull
        private val degToIndex = SIN_COUNT / degFull

        /**
         * multiply by this to convert from radians to degrees
         */
        val radiansToDegrees = 180f / PI
        val radDeg = radiansToDegrees
        /**
         * multiply by this to convert from degrees to radians
         */
        val degreesToRadians = PI / 180
        val degRad = degreesToRadians

        /**
         * Returns a pseudo-random number between min and max, inclusive.
         * The difference between min and max can be at most
         * `Integer.MAX_VALUE - 1`.
         *
         * @param min Minimum value
         * @param max Maximum value.  Must be greater than min.
         * @return Integer between min and max, inclusive.
         * @see Random.nextInt
         */
        fun randInt(min: Int, max: Int): Int {

            // NOTE: Usually this should be a field rather than a method
            // variable so that it is not re-seeded every call.

            // nextInt is normally exclusive of the top value,
            // so add 1 to make it inclusive

            return Random().nextInt(max - min + 1) + min
        }

        fun pxToDp(px: Int): Int {
            val pixel = px.toFloat()
            return (pixel / density).toInt()
        }

        fun pxToDp(px: Float): Float {
            return px / density
        }

        fun dpToPx(dp: Float): Float {
            val scale = density
            return dp * scale + 0.5f
        }

        fun dpToPx(dp: Int): Int {
            val scale = density
            return (dp * scale + 0.5f).toInt()
        }

        val densityDpi: Int
            get() = Resources.getSystem().displayMetrics.densityDpi

        val density: Float
            get() = Resources.getSystem().displayMetrics.density

        fun roundToInt(v: Double): Int {
            return (v + 0.5).toInt()
        }

        fun interpolateBetween(value: Float, min: Float, max: Float): Float {
            return value * (max - min) + 0.4f
        }

        fun randomRange(start: Float, end: Float): Float {
            return Random().nextFloat() * (end - start) + start
        }

        fun nearestNumber(number: Float, vararg numbers: Float): Float {
            var distance = Math.abs(numbers[0] - number)
            var index = 0
            for (c in 1 until numbers.size) {
                val cDistance = Math.abs(numbers[c] - number)
                if (cDistance < distance) {
                    index = c
                    distance = cDistance
                }
            }
            return numbers[index]
        }

        /**
         * Returns the sine in radians from a lookup table.
         */
        fun sin(radians: Float): Float {
            return Sin.table[(radians * radToIndex).toInt() and SIN_MASK]
        }

        /**
         * Returns the cosine in radians from a lookup table.
         */
        fun cos(radians: Float): Float {
            return Sin.table[((radians + PI / 2) * radToIndex).toInt() and SIN_MASK]
        }

        /**
         * Returns the sine in radians from a lookup table.
         */
        fun sinDeg(degrees: Float): Float {
            return Sin.table[(degrees * degToIndex).toInt() and SIN_MASK]
        }

        /**
         * Returns the cosine in radians from a lookup table.
         */
        fun cosDeg(degrees: Float): Float {
            return Sin.table[((degrees + 90) * degToIndex).toInt() and SIN_MASK]
        }

        // ---

        /**
         * Returns atan2 in radians, faster but less accurate than Math.atan2. Average error of 0.00231 radians (0.1323 degrees),
         * largest error of 0.00488 radians (0.2796 degrees).
         */
        fun atan2(y: Float, x: Float): Float {
            if (x == 0f) {
                if (y > 0f) return PI / 2
                return if (y == 0f) 0f else -PI / 2
            }
            val atan: Float
            val z = y / x
            if (kotlin.math.abs(z) < 1f) {
                atan = z / (1f + 0.28f * z * z)
                return if (x < 0f) atan + (if (y < 0f) -PI else PI) else atan
            }
            atan = PI / 2 - z / (z * z + 0.28f)
            return if (y < 0f) atan - PI else atan
        }

        // ---

        var random: Random = RandomXS128()

        /**
         * Returns a random number between 0 (inclusive) and the specified value (inclusive).
         */
        fun random(range: Int): Int {
            return random.nextInt(range + 1)
        }

        /**
         * Returns a random number between start (inclusive) and end (inclusive).
         */
        fun random(start: Int, end: Int): Int {
            return start + random.nextInt(end - start + 1)
        }

        /**
         * Returns a random number between 0 (inclusive) and the specified value (inclusive).
         */
        fun random(range: Long): Long {
            return (random.nextDouble() * range).roundToLong()
        }

        /**
         * Returns a random number between start (inclusive) and end (inclusive).
         */
        fun random(start: Long, end: Long): Long {
            return start + (random.nextDouble() * (end - start)) as Long
        }

        /**
         * Returns a random boolean value.
         */
        fun randomBoolean(): Boolean {
            return random.nextBoolean()
        }

        /**
         * Returns true if a random value between 0 and 1 is less than the specified value.
         */
        fun randomBoolean(chance: Float): Boolean {
            return random() < chance
        }

        /**
         * Returns random number between 0.0 (inclusive) and 1.0 (exclusive).
         */
        fun random(): Float {
            return random.nextFloat()
        }

        /**
         * Returns a random number between 0 (inclusive) and the specified value (exclusive).
         */
        fun random(range: Float): Float {
            return random.nextFloat() * range
        }

        /**
         * Returns a random number between start (inclusive) and end (exclusive).
         */
        fun random(start: Float, end: Float): Float {
            return start + random.nextFloat() * (end - start)
        }

        /**
         * Returns -1 or 1, randomly.
         */
        fun randomSign(): Int {
            return 1 or (random.nextInt() shr 31)
        }

        /**
         * Returns a triangularly distributed random number between -1.0 (exclusive) and 1.0 (exclusive), where values around zero are
         * more likely.
         *
         *
         * This is an optimized version of [randomTriangular(-1, 1, 0)][.randomTriangular]
         */
        fun randomTriangular(): Float {
            return random.nextFloat() - random.nextFloat()
        }

        /**
         * Returns a triangularly distributed random number between `-max` (exclusive) and `max` (exclusive), where values
         * around zero are more likely.
         *
         *
         * This is an optimized version of [randomTriangular(-max, max, 0)][.randomTriangular]
         *
         * @param max the upper limit
         */
        fun randomTriangular(max: Float): Float {
            return (random.nextFloat() - random.nextFloat()) * max
        }

        /**
         * Returns a triangularly distributed random number between `min` (inclusive) and `max` (exclusive), where values
         * around `mode` are more likely.
         *
         * @param min  the lower limit
         * @param max  the upper limit
         * @param mode the point around which the values are more likely
         */
        @JvmOverloads
        fun randomTriangular(min: Float, max: Float, mode: Float = (min + max) * 0.5f): Float {
            val u = random.nextFloat()
            val d = max - min
            return if (u <= (mode - min) / d) min + kotlin.math.sqrt((u * d * (mode - min)).toDouble()).toFloat() else max - kotlin.math.sqrt(((1 - u) * d * (max - mode)).toDouble()).toFloat()
        }

        // ---

        /**
         * Returns the next power of two. Returns the specified value if the value is already a power of two.
         */
        fun nextPowerOfTwo(value: Int): Int {
            var value = value
            if (value == 0) return 1
            value--
            value = value or (value shr 1)
            value = value or (value shr 2)
            value = value or (value shr 4)
            value = value or (value shr 8)
            value = value or (value shr 16)
            return value + 1
        }

        fun isPowerOfTwo(value: Int): Boolean {
            return value != 0 && value and value - 1 == 0
        }

        /**
         * Linearly interpolates between fromValue to toValue on progress position.
         */
        fun lerp(fromValue: Float, toValue: Float, progress: Float): Float {
            return fromValue + (toValue - fromValue) * progress
        }

        /**
         * Linearly interpolates between two angles in radians. Takes into account that angles wrap at two pi and always takes the
         * direction with the smallest delta angle.
         *
         * @param fromRadians start angle in radians
         * @param toRadians   target angle in radians
         * @param progress    interpolation value in the range [0, 1]
         * @return the interpolated angle in the range [0, PI2[
         */
        fun lerpAngle(fromRadians: Float, toRadians: Float, progress: Float): Float {
            val delta = (toRadians - fromRadians + PI2 + PI) % PI2 - PI
            return (fromRadians + delta * progress + PI2) % PI2
        }

        /**
         * Linearly interpolates between two angles in degrees. Takes into account that angles wrap at 360 degrees and always takes
         * the direction with the smallest delta angle.
         *
         * @param fromDegrees start angle in degrees
         * @param toDegrees   target angle in degrees
         * @param progress    interpolation value in the range [0, 1]
         * @return the interpolated angle in the range [0, 360[
         */
        fun lerpAngleDeg(fromDegrees: Float, toDegrees: Float, progress: Float): Float {
            val delta = (toDegrees - fromDegrees + 360f + 180f) % 360 - 180
            return (fromDegrees + delta * progress + 360f) % 360
        }

        // ---

        private val BIG_ENOUGH_INT = 16 * 1024
        private val BIG_ENOUGH_FLOOR = BIG_ENOUGH_INT.toDouble()
        private val CEIL = 0.9999999
        private val BIG_ENOUGH_CEIL = 16384.999999999996
        private val BIG_ENOUGH_ROUND = (BIG_ENOUGH_INT + 0.5f).toDouble()

        /**
         * Returns the largest integer less than or equal to the specified float. This method will only properly floor floats from
         * -(2^14) to (Float.MAX_VALUE - 2^14).
         */
        fun floor(value: Float): Int {
            return (value + BIG_ENOUGH_FLOOR).toInt() - BIG_ENOUGH_INT
        }

        /**
         * Returns the largest integer less than or equal to the specified float. This method will only properly floor floats that are
         * positive. Note this method simply casts the float to int.
         */
        fun floorPositive(value: Float): Int {
            return value.toInt()
        }

        /**
         * Returns the smallest integer greater than or equal to the specified float. This method will only properly ceil floats from
         * -(2^14) to (Float.MAX_VALUE - 2^14).
         */
        fun ceil(value: Float): Int {
            return BIG_ENOUGH_INT - (BIG_ENOUGH_FLOOR - value).toInt()
        }

        /**
         * Returns the smallest integer greater than or equal to the specified float. This method will only properly ceil floats that
         * are positive.
         */
        fun ceilPositive(value: Float): Int {
            return (value + CEIL).toInt()
        }

        /**
         * Returns the closest integer to the specified float. This method will only properly round floats from -(2^14) to
         * (Float.MAX_VALUE - 2^14).
         */
        fun round(value: Float): Int {
            return (value + BIG_ENOUGH_ROUND).toInt() - BIG_ENOUGH_INT
        }

        /**
         * Returns the closest integer to the specified float. This method will only properly round floats that are positive.
         */
        fun roundPositive(value: Float): Int {
            return (value + 0.5f).toInt()
        }

        /**
         * Returns true if the value is zero (using the default tolerance as upper bound)
         */
        fun isZero(value: Float): Boolean {
            return kotlin.math.abs(value) <= FLOAT_ROUNDING_ERROR
        }

        /**
         * Returns true if the value is zero.
         *
         * @param tolerance represent an upper bound below which the value is considered zero.
         */
        fun isZero(value: Float, tolerance: Float): Boolean {
            return kotlin.math.abs(value) <= tolerance
        }

        /**
         * Returns true if a is nearly equal to b. The function uses the default floating error tolerance.
         *
         * @param a the first value.
         * @param b the second value.
         */
        fun isEqual(a: Float, b: Float): Boolean {
            return kotlin.math.abs(a - b) <= FLOAT_ROUNDING_ERROR
        }

        /**
         * Returns true if a is nearly equal to b.
         *
         * @param a         the first value.
         * @param b         the second value.
         * @param tolerance represent an upper bound below which the two values are considered equal.
         */
        fun isEqual(a: Float, b: Float, tolerance: Float): Boolean {
            return kotlin.math.abs(a - b) <= tolerance
        }

        /**
         * @return the logarithm of value with base a
         */
        fun log(a: Float, value: Float): Float {
            return (ln(value.toDouble()) / ln(a.toDouble())).toFloat()
        }

        /**
         * @return the logarithm of value with base 2
         */
        fun log2(value: Float): Float {
            return log(2f, value)
        }
    }
}
/**
 * Returns a triangularly distributed random number between `min` (inclusive) and `max` (exclusive), where the
 * `mode` argument defaults to the midpoint between the bounds, giving a symmetric distribution.
 *
 *
 * This method is equivalent of [randomTriangular(min, max, (min + max) * .5f)][.randomTriangular]
 *
 * @param min the lower limit
 * @param max the upper limit
 */