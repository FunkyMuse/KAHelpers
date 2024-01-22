package dev.funkymuse.bytearray

import java.io.File
import java.io.FileOutputStream
import kotlin.experimental.xor



/**
 * Xor two bytes array together, byte per byte.
 */
infix fun ByteArray.xor(other: ByteArray): ByteArray {
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
    get() = this.contentToString()

val ByteArray.hexString: String
    get() = joinToString("") { String.format("%02X", it) }


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

fun ByteArray.convertToHexString(): String {
    val stringBuilder = StringBuilder()
    this.forEach {
        val value = it.toInt() and 0xff
        var hexString = Integer.toHexString(value)
        if (hexString.length < 2) {
            hexString = "0$hexString"
        }
        stringBuilder.append(hexString)
    }
    return stringBuilder.toString()
}


fun ByteArray.base64Decode(): ByteArray {
    return if (this.isEmpty()) ByteArray(0) else android.util.Base64.decode(this, android.util.Base64.NO_WRAP)
}


fun ByteArray.base64EncodeToString(): String {
    return if (this.isEmpty())
        ""
    else
        android.util.Base64.encodeToString(this, android.util.Base64.NO_WRAP)
}


fun ByteArray.base64Encode(): ByteArray {
    return if (this.isEmpty())
        ByteArray(0)
    else
        android.util.Base64.encode(this, android.util.Base64.NO_WRAP)
}


