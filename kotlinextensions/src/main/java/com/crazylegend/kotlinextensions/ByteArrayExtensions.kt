package com.crazylegend.kotlinextensions

import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.experimental.xor


/**
 * Created by hristijan on 3/7/19 to long live and prosper !
 */
/**
 * Xor two bytes array together, byte per byte.
 */
infix fun ByteArray.xor(other: ByteArray): ByteArray {
    assert(size == other.size)
    val out = ByteArray(size)
    for (i in indices) out[i] = (this[i].toInt() xor other[i].toInt()).toByte()
    return out
}

fun ByteArray.bigEndian(): ByteArray {
    // Reverse and prepend a 0 (in order to force the positive sign).
    val reversed = ByteArray(size + 1)
    for (i in indices) reversed[size - i] = this[i]
    return reversed
}

val ByteArray.string: String
    get() = Arrays.toString(this)

val ByteArray.hexString: String
    get() = map { String.format("%02X", it) }.joinToString("")


/**
 * A [ByteArray] which implements [Object.equals] and [Object.hashCode]
 */
open class ComparableByteArray(val bytes: ByteArray) {
    override fun equals(other: Any?) = other is ComparableByteArray && bytes contentEquals other.bytes
    override fun hashCode() = bytes.contentHashCode()
}


fun ByteArray.saveAt(pathName: String): Boolean = try {
    FileOutputStream(File(pathName)).use { it.write(this) }
    true
} catch (e: Exception) {
    e.printStackTrace()
    false
}

infix fun Byte.shr(that: Int): Int = this.toInt().shr(that)
infix fun Int.shr(that: Byte): Int = this.shr(that.toInt()) // Not necessary in this case because no there's (Int shl Byte)
infix fun Byte.shr(that: Byte): Int = this.toInt().shr(that.toInt()) // Not necessary in this case because no there's (Byte shl Byte)

infix fun Byte.and(that: Int): Int = this.toInt().and(that)
infix fun Int.and(that: Byte): Int = this.and(that.toInt()) // Not necessary in this case because no there's (Int and Byte)
infix fun Byte.and(that: Byte): Int = this.toInt().and(that.toInt()) // Not necessary in this case because no there's (Byte and Byte)


infix fun Byte.shl(that: Int): Int = this.toInt().shl(that)
infix fun Int.shl(that: Byte): Int = this.shl(that.toInt()) // Not necessary in this case because no there's (Int shl Byte)
infix fun Byte.shl(that: Byte): Int = this.toInt().shl(that.toInt()) // Not necessary in this case because no there's (Byte shl Byte)


inline val ByteArray?.xorAll: Byte
    get() {
        var result: Byte = 0
        this?.forEach {
            result = result xor it
        }
        return result
    }


@JvmOverloads
fun ByteArray.arrayCopy(srcPos: Int = 0, des: ByteArray, desPos: Int = 0) {
    System.arraycopy(this, srcPos, des, desPos, this.size)
}



inline val Int.byteArray: ByteArray
    get() = byteArrayOf(((this shr 24) and 0xFF).toByte(), ((this shr 16) and 0xFF).toByte(),
            ((this shr 0) and 0xFF).toByte(), this.toByte())


inline val Short.byteArray: ByteArray
    get() = byteArrayOf(((this.toInt() shr 8) and 0xFF).toByte(), this.toByte())

inline val Byte.sign: Int
    get() = when {
        this < 0 -> -1
        this > 0 -> 1
        else -> 0
    }