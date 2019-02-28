package com.crazylegend.kotlinextensions.context

import android.app.Activity
import android.content.*
import android.net.Uri
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import java.io.File
import java.io.BufferedReader
import java.io.InputStreamReader


/**
 * Created by Hristijan on 2/4/19 to long live and prosper !
 */

fun Activity?.hideKeyboard() {
    if (this != null) {
        val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        if (inputManager != null) {
            val v = this.currentFocus
            if (v != null) {
                inputManager.hideSoftInputFromWindow(
                    v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }
}

fun Context.showKeyboard(toFocus: View) {
    toFocus.requestFocus()
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    // imm.showSoftInput(toFocus, InputMethodManager.SHOW_IMPLICIT);
    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun Context.openEmail() {
    try {
        val emailClientNames = ArrayList<String>()
        val emailClientPackageNames = ArrayList<String>()
        // finding list of email clients that support send email
        val intent = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts("mailto", "abc@gmail.com", null)
        )
        val packages = packageManager.queryIntentActivities(intent, 0)
        if (!packages.isEmpty()) {
            for (resolveInfo in packages) {
                // finding the package name
                val packageName = resolveInfo.activityInfo.packageName
                emailClientNames.add(resolveInfo.loadLabel(packageManager).toString())
                emailClientPackageNames.add(packageName)
            }
            // a selection dialog  for the email clients
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select email client")
            builder.setItems(emailClientNames.toTypedArray()) { _, which ->
                // on click we launch the right package
                val theIntent = packageManager.getLaunchIntentForPackage(emailClientPackageNames[which])
                startActivity(theIntent)
            }
            val dialog = builder.create()
            dialog.show()
        }
    } catch (e: ActivityNotFoundException) {
        // Show error message
        shortToast("No email clients available")
    }
}

fun Context.openWebPage(url: String) {
    try {
        val webpage = Uri.parse(url)
        val myIntent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(myIntent)
    } catch (e: ActivityNotFoundException) {
        longToast("No application can handle this request. Please install a web browser or check your URL.")
        e.printStackTrace()
    }
}

fun Context.sendEmail(myEmail:String, subject:String, text:String) {
    val i = Intent(Intent.ACTION_SEND)
    i.type = "message/rfc822"
    i.putExtra(Intent.EXTRA_EMAIL, arrayOf(myEmail))
    i.putExtra(Intent.EXTRA_TEXT, text)
    i.putExtra(Intent.EXTRA_SUBJECT, subject)

    try {
        startActivity(Intent.createChooser(i, "Send e-mail..."))
    } catch (ex: android.content.ActivityNotFoundException) {
        shortToast("There are no email clients installed.")
    }
}

fun Context.sendEmail(emails:Array<String>, subject:String, text:String) {
    val i = Intent(Intent.ACTION_SEND)
    i.type = "message/rfc822"
    i.putExtra(Intent.EXTRA_EMAIL, emails)
    i.putExtra(Intent.EXTRA_TEXT, text)
    i.putExtra(Intent.EXTRA_SUBJECT, subject)

    try {
        startActivity(Intent.createChooser(i, "Send e-mail..."))
    } catch (ex: android.content.ActivityNotFoundException) {
        shortToast("There are no email clients installed.")
    }
}

fun Context.copyToClipboard(text:String){
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("", text)
    clipboard.primaryClip = clip
}

val Context.getTextFromClipboard :String? get() {

    val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
    val item = clipboard?.primaryClip?.getItemAt(0)

    return item?.text.toString()
}

/**
 * {@link ContextCompat#getColor(int)}.
 */
fun Context.getColorCompat(color: Int) = ContextCompat.getColor(this, color)

/**
 * {@link ContextCompat#getDrawable(int)}.
 */
fun Context.getDrawablecompat(drawable: Int) = ContextCompat.getDrawable(this, drawable)



/**
 * Device width in pixels
 */
inline val Context.displayWidth: Int
    get() = resources.displayMetrics.widthPixels

/**
 * Device height in pixels
 */
inline val Context.displayHeight: Int
    get() = resources.displayMetrics.heightPixels


/**
 * Context.displayMetricks
 */
inline val Context.displayMetricks: DisplayMetrics
    get() = resources.displayMetrics

/**
 * Get LayoutInflater
 */
inline val Context.inflater: LayoutInflater
    get() = LayoutInflater.from(this)