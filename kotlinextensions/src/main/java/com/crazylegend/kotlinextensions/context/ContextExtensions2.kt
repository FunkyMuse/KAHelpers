package com.crazylegend.kotlinextensions.context

import android.app.Activity
import android.app.ActivityManager
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.content.pm.SigningInfo
import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File


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


fun Context.hideKeyboard() {
    val activity = this as Activity?
    if (activity != null) {
        val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

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

fun Context.sendEmail(myEmail: String, subject: String, text: String) {
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

fun Context.sendEmail(emails: Array<String>, subject: String, text: String) {
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


val Context.getTextFromClipboard: String?
    get() {

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
 * Context.displayMetrics
 */
inline val Context.displayMetrics: DisplayMetrics
    get() = resources.displayMetrics

/**
 * Get LayoutInflater
 */
inline val Context.inflater: LayoutInflater
    get() = LayoutInflater.from(this)


/**
 * Share text using the `Intent.createChooser` method
 */
fun Context.shareText(text: String, subject: String = ""): Boolean = try {
    val intent = Intent(android.content.Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(android.content.Intent.EXTRA_TEXT, text)
    startActivity(Intent.createChooser(intent, null))
    true
} catch (e: ActivityNotFoundException) {
    e.printStackTrace()
    false
}

/**
 * Send an SMS using the default messages client in the system
 */
fun Context.sendSMS(number: String, text: String = ""): Boolean = try {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$number"))
    intent.putExtra("sms_body", text)
    startActivity(intent)
    true
} catch (e: Exception) {
    e.printStackTrace()
    false
}


fun Context.copyToClipboard(text: String) {
    val clipboard = clipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard?.setPrimaryClip(clip)
}


fun Context.getTextFromClipboard(): CharSequence {
    val clipData = clipboardManager?.primaryClip
    if (clipData != null && clipData.itemCount > 0) {
        return clipData.getItemAt(0).coerceToText(this)
    }

    return ""
}

fun Context.getUriFromClipboard(): Uri? {
    val clipData = clipboardManager?.primaryClip
    if (clipData != null && clipData.itemCount > 0) {
        return clipData.getItemAt(0).uri
    }

    return null
}

fun Context.hideKeyboard(window: Window, view: View?) {
    if (view?.windowToken != null)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}

fun Context.isShowKeyboard(): Boolean {
    return inputMethodManager.isAcceptingText
}

fun Context.toggleKeyboard() {
    if (inputMethodManager.isActive)
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS)
}

/**
 * Gets intent from Context
 * @return: Generated intent from Flags, Data and Bundle.
 * @param[flags] Flags to pass to the intent
 * @param[data] Uri to pass to intent
 * @param[bundle] Extra to pass to intent
 * @receiver Context to generate intent from
 */
@JvmOverloads
inline fun <reified T : Context> Context.getIntent(
        flags: Int = 0,
        bundle: Bundle? = null,
        data: Uri? = null
): Intent = Intent(this, T::class.java).apply {
    this.flags = flags
    this.data = data
    bundle?.let {
        putExtras(it)
    }
}

fun Context?.isActivityFinished(): Boolean {
    this ?: return false
    return if (this is Activity) {
        this.isActivityFinishing()
    } else {
        true
    }
}

fun Context?.isActivityActive(): Boolean {
    this ?: return false
    return if (this is Activity) {
        !this.isActivityFinishing()
    } else {
        false
    }
}

fun Activity.isActivityFinishing(): Boolean = this.isFinishing || this.isDestroyed


fun Context.getActivityPendingIntent(
        requestCode: Int = 0,
        intent: Intent,
        flags: Int = PendingIntent.FLAG_ONE_SHOT
): PendingIntent = PendingIntent.getActivity(this, requestCode, intent, flags)

fun Context.getBroadcastPendingIntent(
        requestCode: Int = 0,
        intent: Intent,
        flags: Int = PendingIntent.FLAG_ONE_SHOT
): PendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, flags)

fun Toolbar?.changeNavigateUpColor(@IdRes color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

        this?.navigationIcon?.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
    } else {
        this?.navigationIcon?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)

    }
}

fun AppCompatActivity.customToolbarDrawable(drawable: Drawable?) {
    supportActionBar?.setBackgroundDrawable(drawable)
}

fun AppCompatActivity.customIndicator(drawable: Drawable?) {
    supportActionBar?.setHomeAsUpIndicator(drawable)
}

fun AppCompatActivity.customIndicator(drawableResource: Int) {
    supportActionBar?.setHomeAsUpIndicator(drawableResource)
}


fun Context.dip(value: Int): Int = (value * (resources.displayMetrics.density)).toInt()
fun Context.bool(@BoolRes resourceId: Int): Boolean = resources.getBoolean(resourceId)
fun Context.colorStateList(@ColorRes resourceId: Int): ColorStateList? = ContextCompat.getColorStateList(this, resourceId)
fun Context.drawable(@DrawableRes resourceId: Int, tintColorResId: Int): Drawable? =
        ContextCompat.getDrawable(this, resourceId)?.apply {
            setTint(color(tintColorResId))
        }

fun Context.string(@StringRes resourceId: Int, vararg args: Any?): String = resources.getString(resourceId, *args)
fun Context.quantityString(@PluralsRes resourceId: Int, quantity: Int): String = resources.getQuantityString(resourceId, quantity, quantity)

fun Context.getAppPath(packageName: String = this.packageName): String {
    if (packageName.isBlank()) return ""
    return try {
        val pm = packageManager
        val pi = pm.getPackageInfo(packageName, 0)
        pi?.applicationInfo?.sourceDir ?: ""
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        ""
    }
}


/**
 * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}
 *
 * @receiver Context
 * @param file File
 */
fun Context.installApp(file: File) {
    if (!file.exists()) return
    this.startActivity(getInstallAppIntent(file, true))
}

private fun Context.getInstallAppIntent(file: File, isNewTask: Boolean = false): Intent {
    val intent = Intent(Intent.ACTION_VIEW)
    val data: Uri
    val type = "application/vnd.android.package-archive"
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        data = Uri.fromFile(file)
    } else {
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val authority = this.packageName
        data = FileProvider.getUriForFile(this, authority, file)
    }
    grantUriPermission(packageName, data, Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.setDataAndType(data, type)
    return if (isNewTask) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) else intent
}

@RequiresApi(Build.VERSION_CODES.P)
fun Context.getAppSignature(packageName: String = this.packageName): SigningInfo? {
    if (packageName.isBlank()) return null
    return try {
        packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)?.signingInfo
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        null
    }
}


fun Context.isAppInForeground(): Boolean {
    val am = this.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            ?: return false
    val info = am.runningAppProcesses
    if (info.isNullOrEmpty()) return false
    for (aInfo in info) {
        if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            return aInfo.processName == this.packageName
        }
    }
    return false
}

