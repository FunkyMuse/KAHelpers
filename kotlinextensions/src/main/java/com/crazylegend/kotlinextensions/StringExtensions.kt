package com.crazylegend.kotlinextensions

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Base64
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.net.URI
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.*
import java.util.regex.Pattern
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import java.nio.file.Path
import java.nio.file.Paths


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */

val String.decodeBase64: String get() = Base64.decode(this, Base64.DEFAULT).toString(Charsets.UTF_8)
val String.encodeBase64: String get() = Base64.encodeToString(this.toByteArray(Charsets.UTF_8), Base64.DEFAULT)


fun String?.isNotNullOrEmpty(): Boolean {
    return !this.isNullOrEmpty()
}

val String.containsLetters get() = matches(".*[a-zA-Z].*".toRegex())

val String.containsNumbers get() = matches(".*[0-9].*".toRegex())


val String.isAlphanumeric get() = matches("^[a-zA-Z0-9]*$".toRegex())


val String.isAlphabetic get() = matches("^[a-zA-Z]*$".toRegex())

val String.mostCommonCharacter: Char?
    get() {
        if (length == 0) return null
        val map = HashMap<Char, Int>()
        for (char in toCharArray()) map[char] = (map[char] ?: 0) + 1
        var maxEntry = map.entries.elementAt(0)
        for (entry in map) maxEntry = if (entry.value > maxEntry.value) entry else maxEntry
        return maxEntry.key
    }

fun String.removeFirstLastChar(): String = this.substring(1, this.length - 1)

fun <T> T.concatAsString(b: T): String {
    return this.toString() + b.toString()
}


/**
 * Get md5 string.
 */
val String.md5 get() = encrypt(this, "MD5")

/**
 * Get sha1 string.
 */
val String.sha1 get() = encrypt(this, "SHA-1")


/**
 * Check if String is Phone Number.
 */
val String.isPhone: Boolean
    get() {
        val p = "^1([34578])\\d{9}\$".toRegex()
        return matches(p)
    }

/**
 * Check if String is Email.
 */
val String.isEmail: Boolean
    get() {
        val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
        return matches(p)
    }


/**
 * Check if String is Number.
 */
val String.isNumeric: Boolean
    get() {
        val p = "^[0-9]+$".toRegex()
        return matches(p)
    }


/**
 * Method to check String equalsIgnoreCase
 */
fun String.equalsIgnoreCase(other: String) = this.toLowerCase().contentEquals(other.toLowerCase())


/**
 * Returns a new File Object with the Current String as Its path
 */
val String.asFile get() = File(this)


/**
 * Returns a new File Object with the Current String as Its path
 */
fun String.toFile() = File(this)


/**
 * Encode String to URL
 */
val String.encodeToUrlUTF8: String
    get() {
        return URLEncoder.encode(this, "UTF-8")
    }

/**
 * Decode String to URL
 */
val String.decodeToUrlUTF8: String
    get() {
        return URLDecoder.decode(this, "UTF-8")
    }

/**
 * Encode String to URL
 */
fun String.encodeToUrl(charSet: String = "UTF-8"): String = URLEncoder.encode(this, charSet)

/**
 * Decode String to URL
 */
fun String.decodeToUrl(charSet: String = "UTF-8"): String = URLDecoder.decode(this, charSet)

/**
 **
 * Encrypt String to AES with the specific Key
 */
fun String.encryptAES(key: String): String {
    var crypted: ByteArray? = null
    try {
        val skey = SecretKeySpec(key.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, skey)
        crypted = cipher.doFinal(toByteArray())
    } catch (e: Exception) {
        println(e.toString())
    }
    return String(Base64.encode(crypted, Base64.DEFAULT))
}

/**
 * Decrypt String to AES with the specific Key
 */
fun String.decryptAES(key: String): String {
    var output: ByteArray? = null
    try {
        val skey = SecretKeySpec(key.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, skey)
        output = cipher.doFinal(Base64.decode(this, Base64.DEFAULT))
    } catch (e: Exception) {
        println(e.toString())
    }
    return output?.let { String(it) } ?: ""
}

/**
 * encode The String to Binary
 */
fun String.encodeToBinary(): String {
    val stringBuilder = StringBuilder()
    toCharArray().forEach {
        stringBuilder.append(Integer.toBinaryString(it.toInt()))
        stringBuilder.append(" ")
    }
    return stringBuilder.toString()
}

/**
 * Decode the String from binary
 */
val String.deCodeToBinary: String
    get() {
        val stringBuilder = StringBuilder()
        split(" ").forEach {
            stringBuilder.append(Integer.parseInt(it.replace(" ", ""), 2))
        }
        return stringBuilder.toString()
    }

/**
 * Save String to a Given File
 */
fun String.saveToFile(file: File) = FileOutputStream(file).bufferedWriter().use {
    it.write(this)
    it.flush()
    it.close()
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
        val bytes = md5.digest(string.toByteArray())
        bytes2Hex(bytes)
    } catch (e: NoSuchAlgorithmException) {
        ""
    }
}

/**
 * Clear all HTML tags from a string.
 */
fun String.clearHtmlTags(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this).toString()
    }
}

/**
 * Returns true if this string contains exactly the provided string.
 * This method uses RegEx to evaluate and its case-sensitive. What makes it different from the classic
 * [contains] is that it doesn't uses [indexOf], hence it's more performant when used on long char sequences
 * and has much higher probabilities of not returning false positives per approximation.
 */
fun String.containsExact(string: String): Boolean =
    Pattern.compile("(?<=^|[^a-zA-Z0-9])\\Q$string\\E(?=\$|[^a-zA-Z0-9])")
        .matcher(this)
        .find()

/**
 * Converts string to integer safely otherwise returns zero
 */
fun String.toIntOrZero() : Int {
    var value = 0
    tryOrIgnore {
        value = this.toInt()
    }
    return value
}

/**
 * Converts a string to boolean such as 'Y', 'yes', 'TRUE'
 */

fun String.toBoolean(): Boolean {
    return this != "" &&
            (this.equals("TRUE", ignoreCase = true)
                    || this.equals("Y", ignoreCase = true)
                    || this.equals("YES", ignoreCase = true))
}

/**
 * Converts string to camel case. Handles multiple strings and empty strings
 */
fun String.convertToCamelCase(): String {
    var titleText = ""
    if (!this.isEmpty()) {
        val words = this.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        words.filterNot { it.isEmpty() }
            .map { it.substring(0, 1).toUpperCase() + it.substring(1).toLowerCase() }
            .forEach { titleText += it + " " }
    }
    return titleText.trim { it <= ' ' }
}

fun String.ellipsize(at: Int): String {
    if (this.length > at) {
        return this.substring(0, at) + "..."
    }
    return this
}

@Suppress("DEPRECATION")
inline fun String.htmlToSpanned(): Spanned {
    return if (Build.VERSION.SDK_INT >= 24) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}

fun String.noNumbers() :Boolean {
    return matches(Regex(".*\\d.*"))
}

fun String.onlyNumbers() :Boolean {
    return !matches(Regex("\\d+"))
}

fun String.allUpperCase() :Boolean {
    return toUpperCase() != this
}

fun String.allLowerCase() :Boolean {
    return toLowerCase() != this
}

fun String.atLeastOneLowerCase() :Boolean {
    return matches(Regex("[A-Z0-9]+"))
}

fun String.atLeastOneUpperCase() :Boolean {
    return matches(Regex("[a-z0-9]+"))
}

fun String.atLeastOneNumber() :Boolean {
    return !matches(Regex(".*\\d.*"))
}

fun String.startsWithNonNumber() :Boolean {
    return Character.isDigit(this[0])
}

fun String.noSpecialCharacter() :Boolean {
    return !matches(Regex("[A-Za-z0-9]+"))
}

fun String.atLeastOneSpecialCharacter() :Boolean {
    return matches(Regex("[A-Za-z0-9]+"))
}

fun Any.readResourceText(resource: String): String? {
    return this.javaClass.classLoader?.getResource(resource)?.readText()
}

private const val MESSAGE_INVALID_LENGTH = "Length argument must not be less than zero."
private const val CHARACTERS: String = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
private val random: Random = SecureRandom()

/**
 * Generate and return a random alpha-numeric [String] of an arbitrary [length],
 * or an empty [String] if the [length] parameter is equal to zero.
 *
 * @throws IllegalArgumentException If the [length] parameter is less than zero.
 */

fun randomString(length: Int): String {
    if (length < 0) throw IllegalArgumentException(MESSAGE_INVALID_LENGTH)
    if (length == 0) return ""

    return buildString {
        (1..length)
            .forEach {
                val selection = random.nextInt(CHARACTERS.length)
                val character = CHARACTERS[selection]
                append(character)
            }
    }
}

/**
 * Generates and returns a random array of bytes of an arbitrary [length], or an
 * empty [ByteArray] if the [length] parameter is equal to zero.
 *
 * @throws IllegalArgumentException If the [length] parameter is less than zero.
 * @author soykdan@gmail.com
 */
fun randomBytes(length: Int): ByteArray {
    if (length < 0) throw IllegalArgumentException(MESSAGE_INVALID_LENGTH)
    if (length == 0) return ByteArray(0)

    val bytes = ByteArray(length)

    random.nextBytes(bytes)

    return bytes
}

fun String.occurrences(pattern: String): Int
        = length - replace(pattern, "").length

/**
 * Convenience method for creating a [URL] from a valid [String].
 */
fun String.toURL(): URL
        = URL(this)

fun String.toURI(): URI
        = URI(this)



@RequiresApi(Build.VERSION_CODES.O)
fun String.toPath(): Path
        = Paths.get(this)

/**
 * Convenience method to clear all content from a [StringBuilder].
 *
 * @see StringBuilder.setLength
 * @author soykdan@gmail.com
 */
fun StringBuilder.clear(): Unit
        = setLength(0)
