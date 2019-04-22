package com.crazylegend.kotlinextensions

import android.annotation.TargetApi
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.StrikethroughSpan
import android.util.Base64
import android.util.Patterns
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.core.text.isDigitsOnly
import java.io.File
import java.io.FileOutputStream
import java.net.URI
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


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

fun String.removeFirstChar(): String {
    return if (this.isEmpty()){
        ""
    } else {
        this.substring(1)
    }
}
fun String.removeLastCharacter(): String {
    return  if (this.isEmpty()){
        ""
    } else {
        this.substring(0, this.length - 1)
    }
}


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


fun String.urlencode(): String = URLEncoder.encode(this,"UTF-8")
fun String.urldecode(): String = URLDecoder.decode(this,"UTF-8")

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
fun Context.getFilePath(uri: Uri): String {
    var path = ""
    if (uri.isExternalStorageDocument()) {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":")
        val storageType = split[0]

        if (storageType.equals("primary", true)) {
            path = "${Environment.getExternalStorageDirectory().path}/${split[1]}"
        }
    } else if (uri.isDownloadDocuments()) {
        val id = DocumentsContract.getDocumentId(uri)
        val contentUri = ContentUris.withAppendedId(
            Uri.parse("content://downloads/public_downloads"),
            id.toLong())
        path = getDataColumn(contentUri, null, null)
    } else if (uri.isMediaDocument()) {
        val id = DocumentsContract.getDocumentId(uri)
        val split = id.split(":")
        val mediaType = split[0]
        val selection = "_id=?"
        val selectionArg = arrayOf(split[1])

        when {
            mediaType.equals("image", true) -> path = getDataColumn(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArg)
            mediaType.equals("video", true) -> path = getDataColumn(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, selection, selectionArg)
            mediaType.equals("audio", true) -> path = getDataColumn(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, selection, selectionArg)
        }


    } else if (uri.isMediaUri()) {
        path = getDataColumn(uri, null, null)
    }
    return path
}



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


infix fun String?.useIfEmpty(otherString: String?) = if ((this ?: "").isEmptyString()) otherString ?: "" else (this ?: "")

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

    for (i in startFixed..endFixed){
        result += text[i]
        if (i + 1 >= text.length) return result
    }
    return result
}


fun stringConcatenateArrays(first: List<Char>, second: List<Char>): List<Char> {
    var result = ArrayList<Char>(first.size + second.size)
    var j = 0
    for (i in 0 until first.size) i.run {
        result[this] = first[this]
        j = this
    }
    for (k in 0 until second.size) result[k + j + 1] = second[k]
    return result
}

fun String.searchForPattern(pattern: String): Int {
    //if the searched text is longer than the original
    if (pattern.length > this.length) return -1
    //if the searched text is null
    if (pattern.isEmpty()) return 0

    for (i in 0 until this.length - pattern.length){
        var j = i
        while (this[j] == pattern[j - i]){
            if ((j - i) + 1 == pattern.length) return i
            if (j + 1 == this.length) break
            j++
        }
    }
    return -1
}

fun String.removeSymbols(replacement: String = "�"): String {
    return this.replace(Regex("[^\\p{ASCII}‘’]"), replacement)
}

fun String.containsAny(vararg strings: String): Boolean {
    return strings.any { this.contains(it) }
}

fun String.capitalizeWords(): String {
    return this.split(" ").joinToString(" ") { it.capitalize() }
}

fun String.camelCaseWords(): String {
    return this.toLowerCase().capitalizeWords()
}

fun String.trimTo(length: Int): String {
    return if (this.length < length) {
        this
    } else {
        this.substring(0, length - 1).plus("…")
    }
}

fun List<String>.containsCaseInsensitive(string: String): Boolean {
    forEach {
        if (it.equals(string, true)) {
            return true
        }
    }
    return false
}

fun List<String>.indexCaseInsensitive(string: String): Int {
    forEachIndexed { index, s ->
        if (s.equals(string, true)) {
            return index
        }
    }
    return -1
}

val arabicChars = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')

fun String.asArabicNumbers(): String {
    if (TextUtils.isEmpty(this))
        return ""

    val builder = StringBuilder()
    for (i in 0 until this.length) {
        if (Character.isDigit(this[i])) {
            builder.append(arabicChars[this[i].toInt() - 48])
        } else {
            builder.append(this[i])
        }
    }
    return "" + builder.toString()
}

fun String.parseAssetFile(): Uri = Uri.parse("file:///android_asset/$this")

fun String.parseInternalStorageFile(absolutePath:String): Uri = Uri.parse("$absolutePath/$this")

fun String.parseExternalStorageFile(): Uri = Uri.parse("${Environment.getExternalStorageDirectory()}/$this")

fun String.parseFile(): Uri = Uri.fromFile(File(this))

val Uri.fileExists: Boolean
    get() = File(toString()).exists()


/**
 * Removes all occurrences of the [value] in the string
 * @param value A vlaue
 * @param ignoreCase Ignore case?
 * @return A new string with all occurrences of the [value] removed
 */
fun String.remove(value: String, ignoreCase: Boolean = false): String = replace(value, "", ignoreCase)

/**
 * Removes decimal number format symbol
 * @return A new string without `,`
 */
fun String.removeNumberFormat(): String = remove(",")


/**
 * Removes decimal number format symbol
 * @return A new string without `,`
 */
fun String.removeNumberFormatDot(): String = remove(".")

/** The Char array representing by this string */
inline val String.ch: Array<Char> get() = this.toCharArray().toTypedArray()

fun String?.strikeThrough() = this?.let { SpannableString(it).apply { setSpan(StrikethroughSpan(), 0, it.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) } }


fun String.abbreviateMiddle(middle: String, length: Int): String {
    val str = this

    if (this.isEmpty()) {
        return this
    }

    if (length >= str.length || length < middle.length + 2) {
        return this
    }

    val targetSting = length - middle.length
    val startOffset = targetSting / 2 + targetSting % 2
    val endOffset = str.length - targetSting / 2

    val builder = StringBuilder(length)
    builder.append(str.substring(0, startOffset))
    builder.append(middle)
    builder.append(str.substring(endOffset))

    return builder.toString()
}


private val NON_DIGIT_REGEX = Regex("[^A-Za-z0-9]")
private val DIGIT_REGEX = Regex("[^0-9]")

fun String?.replaceNonDigit() = this?.replace(NON_DIGIT_REGEX, "")

fun String?.replaceDigit() = this?.replace(DIGIT_REGEX, "")

fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String?.isInt(): Boolean {
    if (isNullOrBlank()) return false
    return try {
        val int = this!!.toInt()
        true
    } catch (exc: ParseException) {
        false
    }
}

fun String.removeSpace(): String = replace(" ", "")

fun String.toDate(format: DateFormat): Date? {
    return try {
        format.parse(this)
    } catch (exc: ParseException) {
        exc.printStackTrace()
        null
    }
}
