package dev.funkymuse.string

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.core.text.isDigitsOnly
import java.io.File
import java.net.URI
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.file.Path
import java.nio.file.Paths
import java.security.SecureRandom
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*




fun String.noNumbers(): Boolean {
    return matches(Regex(".*\\d.*"))
}

fun String.onlyNumbers(): Boolean {
    return !matches(Regex("\\d+"))
}

fun String.allUpperCase(): Boolean {
    return uppercase() != this
}

fun String.allLowerCase(): Boolean {
    return lowercase() != this
}

fun String.atLeastOneLowerCase(): Boolean {
    return matches(Regex("[A-Z0-9]+"))
}

fun String.atLeastOneUpperCase(): Boolean {
    return matches(Regex("[a-z0-9]+"))
}

fun String.atLeastOneNumber(): Boolean {
    return !matches(Regex(".*\\d.*"))
}

fun String.startsWithNonNumber(): Boolean {
    return Character.isDigit(this[0])
}

fun String.noSpecialCharacter(): Boolean {
    return !matches(Regex("[A-Za-z0-9]+"))
}

fun String.atLeastOneSpecialCharacter(): Boolean {
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

fun String.occurrences(pattern: String): Int = length - replace(pattern, "").length

/**
 * Convenience method for creating a [URL] from a valid [String].
 */
fun String.toURL(): URL = URL(this)

fun String.toURI(): URI = URI(this)


@RequiresApi(Build.VERSION_CODES.O)
fun String.toPath(): Path = Paths.get(this)

/**
 * Convenience method to clear all content from a [StringBuilder].
 *
 * @see StringBuilder.setLength
 * @author soykdan@gmail.com
 */
fun StringBuilder.clear(): Unit = setLength(0)


fun String.urlencode(): String = URLEncoder.encode(this, "UTF-8")
fun String.urldecode(): String = URLDecoder.decode(this, "UTF-8")

/**
 * Gets extension from filePath
 */
val String.extension: String
    get() {
        return if (isEmptyString()) {
            ""
        } else {
            val dot = lastIndexOf(".")
            if (dot >= 0) {
                substring(dot)
            } else {
                ""
            }
        }
    }

fun String.isLocal() = !isEmptyString() && (startsWith("http://") || startsWith("https://"))

fun Uri.isMediaUri() = authority.equals("media", true)

fun File.getUri() = Uri.fromFile(this)

fun Uri.isExternalStorageDocument() = authority == "com.android.externalstorage.documents"

fun Uri.isDownloadDocuments() = authority == "com.android.providers.downloads.documents"

fun Uri.isMediaDocument() = authority == "com.android.providers.media.documents"


@TargetApi(19)
fun Context.getDataColumn(uri: Uri, selection: String?, selectionArg: Array<String>?): String {
    val column = "_data"
    val projection = arrayOf(column)

    val cursor = contentResolver.query(uri, projection, selection, selectionArg, null)
    cursor.use {
        if (it != null) {
            val columnIndex = it.getColumnIndexOrThrow(column)
            it.moveToFirst()
            return it.getString(columnIndex)
        }
    }
    return ""
}

@JvmOverloads
fun String.failSafeSplit(delimeter: String = ","): List<String>? {
    return if (contains(delimeter)) {
        this.split(delimeter)
    } else {
        listOf(this)
    }
}

@JvmOverloads
fun String?.containsInArray(vararg names: String, ignoreCase: Boolean = true): Boolean {
    this?.let {
        it.replace(" ", "")
        it.replace("\n", "")
        it.replace(",\n", "")
        it.replace(", ", "")
        return names.any { this.equals(it, ignoreCase) }
    }
    return false
}

fun String.convertDateFormat(fromFormat: String, toFormat: String): String {
    return if (this.isDateStringProperlyFormatted(fromFormat)) {
        try {
            this toDate fromFormat asString toFormat
        } catch (e: Exception) {
            e.printStackTrace()
            this
        }

    } else {
        this
    }
}

fun String.isDateStringProperlyFormatted(dateFormat: String): Boolean {
    var isProperlyFormatted = false
    val format = SimpleDateFormat(dateFormat, Locale.getDefault())
    //SetLenient means dateString will be checked strictly with dateFormat. Otherwise it will format as per wish.
    format.isLenient = false
    try {
        format.parse(this)
        isProperlyFormatted = true
    } catch (e: ParseException) {
        //exception means dateString is not parsable by dateFormat. Thus dateIsProperlyFormatted.
    }

    return isProperlyFormatted
}

infix fun Date.asString(parseFormat: String): String {
    return try {
        SimpleDateFormat(parseFormat, Locale.getDefault()).format(this)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }

}

infix fun String.toDate(currentFormat: String): Date {
    return try {
        if (this.isEmptyString()) {
            Date()
        } else {
            SimpleDateFormat(currentFormat, Locale.getDefault()).parse(this)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        //If It can not be parsed, return today's date instead of null. So return value of this method does not create null pointer exception.
        Date()
    }
}

fun CharSequence.getDouble(): Double {
    return try {
        if (isDigitsOnly()) {
            if (contains(".")) {
                toString().toDouble()
            } else {
                getNumber().toDouble()
            }
        } else {
            0.0
        }
    } catch (e: Exception) {
        0.0
    }
}


infix fun String?.useIfEmpty(otherString: String?) = if ((this ?: "").isEmptyString()) otherString
    ?: "" else (this ?: "")

fun CharSequence.isEmptyString(): Boolean {
    return this.isEmpty() || this.toString().equals("null", true)
}


fun CharSequence.isDigitOnly(): Boolean {
    return (0 until length).any { Character.isDigit(this[it]) }
}

fun CharSequence.getNumber(): Int {
    return if (isDigitOnly()) {
        Integer.parseInt(toString())
    } else {
        0
    }
}

fun String.occurencesOf(other: String): Int = split(other).size - 1

val String.asColor: Int @ColorInt get() = Color.parseColor(this)

/**
 * Computes the substring starting at start and ending at endInclusive,
 *      which is the length of text unless specified otherwise.
 */
fun stringSubstring(text: String, start: Int, endInclusive: Int = text.length - 1): String {
    var result = ""
    val startFixed = if (start < 0) 0 else start
    val endFixed = if (endInclusive > text.length - 1) text.length - 1 else endInclusive

    for (i in startFixed..endFixed) {
        result += text[i]
        if (i + 1 >= text.length) return result
    }
    return result
}


