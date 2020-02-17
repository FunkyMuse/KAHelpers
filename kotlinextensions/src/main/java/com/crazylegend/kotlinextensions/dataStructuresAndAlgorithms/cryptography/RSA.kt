package com.crazylegend.kotlinextensions.dataStructuresAndAlgorithms.cryptography

import com.crazylegend.kotlinextensions.dataStructuresAndAlgorithms.mathematics.areCoprime
import com.crazylegend.kotlinextensions.dataStructuresAndAlgorithms.mathematics.generateRandomPrime
import com.crazylegend.kotlinextensions.dataStructuresAndAlgorithms.mathematics.raiseToPowerBig
import java.math.BigInteger
import kotlin.random.Random


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */


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
            .map { char -> rsaEncrypt(char.toInt(), publicKey).toString() }

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
            .map { rsaDecrypt(it.toLong(), privateKey).toChar() }
    println() // to test
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