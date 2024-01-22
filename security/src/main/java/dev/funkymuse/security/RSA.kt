package dev.funkymuse.security

import java.math.BigInteger
import kotlin.random.Random





/**
 * Two numbers are co-prime when the GCD of the numbers is 1.
 * This method uses an algorithm called the Euclidean algorithm to compute GCD.
 */
private fun areCoprime(first: Long, second: Long): Boolean {
    fun greatestCommonDivisor(first: Long, second: Long): Long {
        val smaller = if (first <= second) first else second
        val bigger = if (first > second) first else second

        return if (bigger % smaller == 0L) smaller
        else greatestCommonDivisor(smaller, bigger % smaller)
    }

    return greatestCommonDivisor(first, second) == 1L
}

/**
 * Generates a random prime number within range, using isPrime() function to check for primality.
 */
private fun generateRandomPrime(range: IntRange): Int {
    val count = range.count()
    var i = 0
    var number: Int
    do {
        number = Random.nextInt(range.first, range.last)
        if (isPrime(number.toLong())) return number
        i++
        if (i >= count) return -1
    } while (true)
}

/**
 * This method returns true if the number is a prime number.
 * Simply, it checks all the numbers up to the numbers square root and returns false if the number is divisible by any,
 * otherwise returns true.
 */
private fun isPrime(num: Long): Boolean {
    // Negative integers can not be prime. 0 is also not a prime.
    if (num < 1) return false
    // Taking shortcuts - if a number is divisible without the remainder by small natural numbers, it is not a prime number.
    if (num > 10 && (num % 2 == 0L ||
                    num % 3 == 0L ||
                    num % 5 == 0L ||
                    num % 7 == 0L ||
                    num % 9 == 0L)) return false
    // If the number is belonging to small known prime numbers, return true.
    // Likewise, small known not-primes, return false.
    when (num) {
        in setOf<Long>(2, 3, 5, 7) -> return true
        in setOf<Long>(1, 4, 6, 8, 9) -> return false
        else -> {
            // Otherwise, take every number up to the numbers square root.
            // If none of those the number is divisible by without the remainder, it is a prime number.
            val sqrt = Math.sqrt(num.toDouble())
            for (i in 2..sqrt.toInt()) if (num % i == 0L) return false
        }
    }
    return true
}


private fun generateRandomPrimePair(range: IntRange): Pair<Int, Int> {
    val num1 = generateRandomPrime(range)
    var num2 = generateRandomPrime(range)
    while (num2 == num1) num2 = generateRandomPrime(range)
    return Pair(num1, num2)
}

private fun generateRandomCoprime(range: IntRange, number: Int): Int {
    while (true) Random.nextInt(range.first, range.last).run {
        if (areCoprime(number.toLong(), this.toLong())) return this
    }
}

/**
 * Generates two random keys from prime numbers within the range.
 */
fun generateRandomKeys(primeRange: IntRange): Pair<RSAPublicKey, RSAPrivateKey> {
    fun generateE(intRange: IntRange, n: Int, phi: Int): Int {
        for (i in intRange) {
            if (areCoprime(i.toLong(), n.toLong()) && areCoprime(i.toLong(), phi.toLong())) return i
        }
        return 1
    }

    fun generateD(e: Int, phi: Int, intRange: IntRange): Int {
        for (number in intRange) {
            if ((number * e) % phi == 1) return number
        }
        return -1
    }

    val randomPrimes = generateRandomPrimePair(primeRange)
    val n = randomPrimes.first * randomPrimes.second
    val phi = (randomPrimes.first - 1) * (randomPrimes.second - 1)
    val e = generateE(2 until phi, n, phi)
    val d = generateD(e, phi, 2 until phi)

    val private = RSAPrivateKey(n, d)
    val public = RSAPublicKey(n, e)
    return Pair(public, private)
}

fun generateRandomKeys(primeRange: IntRange, minimumModulus: Int): Pair<RSAPublicKey, RSAPrivateKey> {
    fun generateE(intRange: IntRange, n: Int, phi: Int): Int {
        for (i in intRange) {
            if (areCoprime(i.toLong(), n.toLong()) && areCoprime(i.toLong(), phi.toLong())) return i
        }
        return 1
    }

    fun generateD(e: Int, phi: Int, intRange: IntRange): Int {
        for (number in intRange) {
            if ((number * e) % phi == 1) return number
        }
        return -1
    }

    var n = 0
    var phi = 0
    while (n <= minimumModulus) {
        generateRandomPrimePair(primeRange).run {
            n = this.first * this.second
            phi = (this.first - 1) * (this.second - 1)
        }
    }
    val e = generateE(2 until phi, n, phi)
    val d = generateD(e, phi, 2 until phi)

    val private = RSAPrivateKey(n, d)
    val public = RSAPublicKey(n, e)
    return Pair(public, private)
}

/**
 * Iteratively raises a number to a certain power.
 * For description refer to the method above.
 */
private fun raiseToPowerBig(num: Long, pow: Int): BigInteger {
    var result = BigInteger.valueOf(num)
    for (i in pow downTo 2) result *= BigInteger.valueOf(num)
    return result
}

/**
 * Encrypt a number.
 * For this to work properly, the modulus has to be bigger than the number.
 * The formula for encryption is c = m^e mod n, where e is the public key exponent and n is the modulus.
 */
fun rsaEncrypt(message: Int, publicKey: RSAPublicKey): Long {
    // Modulus must be bigger than the message to encrypt.
    if (publicKey.modulus <= message) return -1
    return (raiseToPowerBig(
            message.toLong(),
            publicKey.exponent
    ).mod(BigInteger.valueOf(publicKey.modulus.toLong())).toLong())
}

fun rsaEncrypt(message: String, publicKey: RSAPublicKey): Pair<String, Int> {
    var chunks = message
            .map { char -> rsaEncrypt(char.code, publicKey).toString() }

    var longest = 0
    chunks.forEach { if (it.length > longest) longest = it.length }
    chunks = chunks.map {
        var result = it
        while (result.length < longest) result = "0$result"
        return@map result
    }
    val result = chunks.reduce { acc, s -> acc + s }
    return Pair(result, longest)
}

fun rsaDecrypt(message: String, blockSize: Int, privateKey: RSAPrivateKey): String {
    val chunks = message
            .chunked(blockSize)
            .map {
                if (it[0] != '0') return@map it
                else {
                    var result = it
                    while (result[0] == '0') {
                        result = result.substring(1)
                    }
                    return@map result
                }
            }
            .map { rsaDecrypt(it.toLong(), privateKey).toInt().toChar() }
    var result = ""
    chunks.forEach { result += it.toString() }
    return result
}

fun rsaDecrypt(message: Long, privateKey: RSAPrivateKey): Long {
    val power = raiseToPowerBig(message, privateKey.d)
    return (power % BigInteger.valueOf(privateKey.modulus.toLong())).toLong()
}

class RSAPublicKey(val modulus: Int, val exponent: Int)
class RSAPrivateKey(val modulus: Int, val d: Int)