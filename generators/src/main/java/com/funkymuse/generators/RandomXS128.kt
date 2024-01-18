package com.funkymuse.generators

import java.util.Random


class RandomXS128 : Random {

    /**
     * The first half of the internal state of this pseudo-random number generator.
     */
    private var seed0: Long = 0

    /**
     * The second half of the internal state of this pseudo-random number generator.
     */
    private var seed1: Long = 0

    /**
     * Creates a new random number generator. This constructor sets the seed of the random number generator to a value very likely
     * to be distinct from any other invocation of this constructor.
     *
     *
     * This implementation creates a [Random] instance to generate the initial seed.
     */
    constructor() {
        setSeed(Random().nextLong())
    }

    /**
     * Creates a new random number generator using a single `long` seed.
     *
     * @param seed the initial seed
     */
    constructor(seed: Long) {
        setSeed(seed)
    }

    /**
     * Creates a new random number generator using two `long` seeds.
     *
     * @param seed0 the first part of the initial seed
     * @param seed1 the second part of the initial seed
     */
    constructor(seed0: Long, seed1: Long) {
        setState(seed0, seed1)
    }

    /**
     * Returns the next pseudo-random, uniformly distributed `long` value from this random number generator's sequence.
     *
     *
     * Subclasses should override this, as this is used by all other methods.
     */
    override fun nextLong(): Long {
        var s1 = this.seed0
        val s0 = this.seed1
        this.seed0 = s0
        s1 = s1 xor (s1 shl 23)
        this.seed1 = s1 xor s0 xor s1.ushr(17) xor s0.ushr(26) + s0

        return this.seed1
    }

    /**
     * This protected method is final because, contrary to the superclass, it's not used anymore by the other methods.
     */
    override fun next(bits: Int): Int {
        return (nextLong() and (1L shl bits) - 1).toInt()
    }

    /**
     * Returns the next pseudo-random, uniformly distributed `int` value from this random number generator's sequence.
     *
     *
     * This implementation uses [.nextLong] internally.
     */
    override fun nextInt(): Int {
        return nextLong().toInt()
    }

    /**
     * Returns a pseudo-random, uniformly distributed `int` value between 0 (inclusive) and the specified value (exclusive),
     * drawn from this random number generator's sequence.
     *
     *
     * This implementation uses [.nextLong] internally.
     *
     * @param n the positive bound on the random number to be returned.
     * @return the next pseudo-random `int` value between `0` (inclusive) and `n` (exclusive).
     */
    override fun nextInt(n: Int): Int {
        return nextLong(n.toLong()).toInt()
    }

    /**
     * Returns a pseudo-random, uniformly distributed `long` value between 0 (inclusive) and the specified value (exclusive),
     * drawn from this random number generator's sequence. The algorithm used to generate the value guarantees that the result is
     * uniform, provided that the sequence of 64-bit values produced by this generator is.
     *
     *
     * This implementation uses [.nextLong] internally.
     *
     * @param n the positive bound on the random number to be returned.
     * @return the next pseudo-random `long` value between `0` (inclusive) and `n` (exclusive).
     */
    fun nextLong(n: Long): Long {
        require(n > 0) { "n must be positive" }
        while (true) {
            val bits = nextLong().ushr(1)
            val value = bits % n
            if (bits - value + (n - 1) >= 0) return value
        }
    }

    /**
     * Returns a pseudo-random, uniformly distributed `double` value between 0.0 and 1.0 from this random number generator's
     * sequence.
     *
     *
     * This implementation uses [.nextLong] internally.
     */
    override fun nextDouble(): Double {
        return nextLong().ushr(11) * NORM_DOUBLE
    }

    /**
     * Returns a pseudo-random, uniformly distributed `float` value between 0.0 and 1.0 from this random number generator's
     * sequence.
     *
     *
     * This implementation uses [.nextLong] internally.
     */
    override fun nextFloat(): Float {
        return (nextLong().ushr(40) * NORM_FLOAT).toFloat()
    }

    /**
     * Returns a pseudo-random, uniformly distributed `boolean ` value from this random number generator's sequence.
     *
     *
     * This implementation uses [.nextLong] internally.
     */
    override fun nextBoolean(): Boolean {
        return nextLong() and 1 != 0L
    }

    /**
     * Generates random bytes and places them into a user-supplied byte array. The number of random bytes produced is equal to the
     * length of the byte array.
     *
     *
     * This implementation uses [.nextLong] internally.
     */
    override fun nextBytes(bytes: ByteArray) {
        var n: Int
        var i = bytes.size
        while (i != 0) {
            n = if (i < 8) i else 8 // min(i, 8);
            var bits = nextLong()
            while (n-- != 0) {
                bytes[--i] = bits.toByte()
                bits = bits shr 8
            }
        }
    }

    /**
     * Sets the internal seed of this generator based on the given `long` value.
     *
     *
     * The given seed is passed twice through a hash function. This way, if the user passes a small value we avoid the short
     * irregular transient associated with states having a very small number of bits set.
     *
     * @param seed a nonzero seed for this generator (if zero, the generator will be seeded with [Long.MIN_VALUE]).
     */
    override fun setSeed(seed: Long) {
        val seed0 = murmurHash3(if (seed == 0L) java.lang.Long.MIN_VALUE else seed)
        setState(seed0, murmurHash3(seed0))
    }

    /**
     * Sets the internal state of this generator.
     *
     * @param seed0 the first part of the internal state
     * @param seed1 the second part of the internal state
     */
    fun setState(seed0: Long, seed1: Long) {
        this.seed0 = seed0
        this.seed1 = seed1
    }

    /**
     * Returns the internal seeds to allow state saving.
     *
     * @param seed must be 0 or 1, designating which of the 2 long seeds to return
     * @return the internal seed that can be used in setState
     */
    fun getState(seed: Int): Long {
        return if (seed == 0) seed0 else seed1
    }

    companion object {

        /**
         * Normalization constant for double.
         */
        private val NORM_DOUBLE = 1.0 / (1L shl 53)

        /**
         * Normalization constant for float.
         */
        private val NORM_FLOAT = 1.0 / (1L shl 24)

        private fun murmurHash3(x: Long): Long {
            var xTemp = x
            xTemp = xTemp xor xTemp.ushr(33)
            xTemp *= -0xae502812aa7333L
            xTemp = xTemp xor xTemp.ushr(33)
            xTemp *= -0x3b314601e57a13adL
            xTemp = xTemp xor xTemp.ushr(33)

            return xTemp
        }
    }

}
