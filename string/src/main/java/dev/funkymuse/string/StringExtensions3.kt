package dev.funkymuse.string

import android.content.Context
import android.content.pm.PackageManager
import android.text.Spanned
import android.util.Base64
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import dev.funkymuse.common.string.toHtmlSpan
import java.io.File
import java.security.SignatureException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec



/***
 * Computes RFC 2104-compliant HMAC signature. This can be used to sign the Amazon S3
 * request urls
 *
 * @param data The data to be signed.
 * @param key  The signing key.
 * @return The Base64-encoded RFC 2104-compliant HMAC signature.
 * @throws java.security.SignatureException when signature generation fails
 */
@Throws(SignatureException::class)
fun getHMac(data: String, key: String): String? {

    var result: String? = null
    try {

        @Suppress("LocalVariableName") val HMAC_SHA1_ALGORITHM = "HmacSHA1"

        // get an hmac_sha1 key from the raw key bytes
        val signingKey = SecretKeySpec(key.toByteArray(), HMAC_SHA1_ALGORITHM)

        // get an hmac_sha1 Mac instance &
        // initialize with the signing key
        val mac = Mac.getInstance(HMAC_SHA1_ALGORITHM)
        mac.init(signingKey)

        // compute the hmac on input data bytes
        val digest = mac.doFinal(data.toByteArray())

        if (digest != null) {
            // Base 64 Encode the results
            result = Base64.encodeToString(digest, Base64.NO_WRAP)
        }

    } catch (e: Exception) {
        throw SignatureException("Failed to generate HMAC : " + e.message)
    }

    return result
}

fun Context.getHtmlSpannedString(@StringRes id: Int): Spanned = getString(id).toHtmlSpan()

fun Context.getHtmlSpannedString(@StringRes id: Int, vararg formatArgs: Any): Spanned = getString(id, *formatArgs).toHtmlSpan()

fun Context.getQuantityHtmlSpannedString(@PluralsRes id: Int, quantity: Int): Spanned = resources.getQuantityString(id, quantity).toHtmlSpan()

fun Context.getQuantityHtmlSpannedString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any): Spanned = resources.getQuantityString(id, quantity, *formatArgs).toHtmlSpan()

fun String.capitalize() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }


/**
 * get Application Size in Bytes
 *
 * @property pName the Package Name of the Target Application, Default is Current.
 *
 * Provide Package or will provide the current App Detail
 */
@Throws(PackageManager.NameNotFoundException::class)
fun Context.getAppSize(pName: String = packageName): Long {
    return packageManager.getApplicationInfo(pName, 0).sourceDir.asFile().length()
}

/**
 * get Application Apk File
 *
 * @property pName the Package Name of the Target Application, Default is Current.
 *
 * Provide Package or will provide the current App Detail
 */
@Throws(PackageManager.NameNotFoundException::class)
fun Context.getAppApk(pName: String = packageName): File {
    return packageManager.getApplicationInfo(pName, 0).sourceDir.asFile()
}