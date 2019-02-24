package com.crazylegend.kotlinextensions

import android.util.Base64
import java.net.MalformedURLException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */

val String.decodeBase64: String get() = Base64.decode(this, Base64.DEFAULT).toString(Charsets.UTF_8)
val String.encodeBase64: String get() = Base64.encodeToString(this.toByteArray(Charsets.UTF_8), Base64.DEFAULT)


fun String?.isNotNullOrEmpty():Boolean {
    return !this.isNullOrEmpty()
}

val String.containsLetters get() = matches(".*[a-zA-Z].*".toRegex())

val String.containsNumbers get() = matches(".*[0-9].*".toRegex())


val String.isAlphanumeric get() = matches("^[a-zA-Z0-9]*$".toRegex())


val String.isAlphabetic get() = matches("^[a-zA-Z]*$".toRegex())

val String.mostCommonCharacter: Char? get() {
    if (length == 0) return null
    val map = HashMap<Char, Int>()
    for (char in toCharArray()) map[char] = (map[char] ?: 0) + 1
    var maxEntry = map.entries.elementAt(0)
    for (entry in map) maxEntry = if (entry.value > maxEntry.value) entry else maxEntry
    return maxEntry.key
}

fun String.removeFirstLastChar(): String =  this.substring(1, this.length - 1)

fun <T> T.concatAsString(b: T) : String {
    return this.toString() + b.toString()
}



/**
 * Get md5 string.
 */
fun String.md5() = encrypt(this, "MD5")

/**
 * Get sha1 string.
 */
fun String.sha1() = encrypt(this, "SHA-1")



/**
 * Check if String is Phone Number.
 */
fun String.isPhone(): Boolean {
    val p = "^1([34578])\\d{9}\$".toRegex()
    return matches(p)
}

/**
 * Check if String is Email.
 */
fun String.isEmail(): Boolean {
    val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
    return matches(p)
}


/**
 * Check if String is Number.
 */
fun String.isNumeric(): Boolean {
    val p = "^[0-9]+$".toRegex()
    return matches(p)
}


/**
 * Method to convert byteArray to String.
 */
private fun bytes2Hex(bts: ByteArray): String {
    var des = ""
    var tmp: String
    for (i in bts.indices) {
        tmp = Integer.toHexString(bts[i].toInt() and 0xFF)
        if (tmp.length == 1) {
            des += "0"
        }
        des += tmp
    }
    return des
}

/**
 * Method to get encrypted string.
 */
private fun encrypt(string: String?, type: String): String {
    if (string.isNullOrEmpty()) {
        return ""
    }
    val md5: MessageDigest
    return try {
        md5 = MessageDigest.getInstance(type)
        val bytes = md5.digest(string!!.toByteArray())
        bytes2Hex(bytes)
    } catch (e: NoSuchAlgorithmException) {
        ""
    }
}


/**
 * Method to check String equalsIgnoreCase
 */
fun String.equalsIgnoreCase(other: String) = this.toLowerCase().contentEquals(other.toLowerCase())


