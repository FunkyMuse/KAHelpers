package com.crazylegend.kotlinextensions.context

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.util.TypedValue
import androidx.annotation.*
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.crazylegend.kotlinextensions.enums.ContentColumns
import com.crazylegend.kotlinextensions.enums.ContentOrder
import com.crazylegend.kotlinextensions.toFile
import java.io.File
import java.util.*


/**
 * Created by hristijan on 2/27/19 to long live and prosper !
 */

/**
 * Get a video duration in milliseconds
 */
@RequiresPermission(allOf = [android.Manifest.permission.READ_EXTERNAL_STORAGE])
fun Context.getVideoDuration(videoFile: File): Long? {
    val retriever = MediaMetadataRetriever()
    var videoDuration = Long.MAX_VALUE
    try {
        retriever.setDataSource(this, Uri.fromFile(videoFile))
        val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        videoDuration = java.lang.Long.parseLong(time)
        retriever.release()
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    } catch (e: SecurityException) {
        e.printStackTrace()
    }
    return videoDuration
}

/**
 * ask the system to scan your file easily with a broadcast.
 */
fun Context.requestMediaScanner(url: String) {
    val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
    val contentUri = Uri.fromFile(File(url))
    mediaScanIntent.data = contentUri
    this.sendBroadcast(mediaScanIntent)
}

/**
 * check if you can resolve the intent
 */
fun Context.isIntentResolvable(intent: Intent) =
    packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isNotEmpty()


/**
 * start third party App
 *
 * *If App Installed ;)
 */
fun Context.startApp(packageName: String) =
    if (isAppInstalled(packageName)) startActivity(packageManager.getLaunchIntentForPackage(packageName)) else {
    }


/**
 * Check if an App is Installed on the user device.
 */
fun Context.isAppInstalled(packageName: String): Boolean {
    return try {
        packageManager.getApplicationInfo(packageName, 0)
        true
    } catch (ignore: Exception) {
        false
    }
}



/**
 * Want All the Images from the User Phone?
 *
 * Get them easily with the below method, Make Sure You have READ_EXTERNAL_STORAGE Permission
 */
@RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
fun Context.getAllImages(
    sortBy: ContentColumns = ContentColumns.DATE_ADDED,
    order: ContentOrder = ContentOrder.DESCENDING
): List<String>? {
    val data = mutableListOf<String>()
    val cursor = contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        arrayOf(MediaStore.Images.Media.DATA),
        null,
        null,
        sortBy.s + " " + order.s
    )
    cursor?.let {
        val columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        while (cursor.isClosed.not() && cursor.moveToNext()) {
            cursor.getString(columnIndexData).let {
                if (it.toFile().exists()) {
                    data.add(it)
                }
            }
        }
        cursor.close()
    }
    return data.toList()
}


/**
 * Checks if App is in Background
 */
fun Context.isBackground(pName: String = packageName): Boolean {
    activityManager.runningAppProcesses.forEach {
        @Suppress("DEPRECATION")
        if (it.processName == pName)
            return it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND
    }
    return false
}

/**
 * Want All the Videos from the User Phone?
 *
 * Get them easily with the below method, Make Sure You have READ_EXTERNAL_STORAGE Permission
 */
@RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
fun Context.getAllVideos(
    sortBy: ContentColumns = ContentColumns.DATE_ADDED,
    order: ContentOrder = ContentOrder.DESCENDING
): List<String>? {
    val data = mutableListOf<String>()
    val cursor = contentResolver.query(
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        arrayOf(MediaStore.Video.Media.DATA),
        null,
        null,
        sortBy.s + " " + order.s
    )
    cursor?.let {
        val columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
        while (cursor.isClosed.not() && cursor.moveToNext()) {
            cursor.getString(columnIndexData).let {
                if (it.toFile().exists()) {
                    data.add(it)
                }
            }
        }
        cursor.close()
    }
    return data.toList()
}

/**
 * get Application Name,
 *
 * @property pName the Package Name of the Target Application, Default is Current.
 *
 * Provide Package or will provide the current App Detail
 */
@Throws(PackageManager.NameNotFoundException::class)
fun Context.getAppName(pName: String = packageName): String {
    return packageManager.getApplicationLabel(packageManager.getApplicationInfo(pName, 0)).toString()
}

/**
 * get Application Icon,
 *
 * @property pName the Package Name of the Target Application, Default is Current.
 *
 * Provide Package or will provide the current App Detail
 */
@Throws(PackageManager.NameNotFoundException::class)
fun Context.getAppIcon(pName: String = packageName): Drawable {
    return packageManager.getApplicationInfo(pName, 0).loadIcon(packageManager)
}

/**
 * get Application Size in Bytes
 *
 * @property pName the Package Name of the Target Application, Default is Current.
 *
 * Provide Package or will provide the current App Detail
 */
@Throws(PackageManager.NameNotFoundException::class)
fun Context.getAppSize(pName: String = packageName): Long {
    return packageManager.getApplicationInfo(pName, 0).sourceDir.toFile().length()
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
    return packageManager.getApplicationInfo(pName, 0).sourceDir.toFile()
}


/**
 * get Application Version Name
 *
 * @property pName the Package Name of the Target Application, Default is Current.
 *
 * Provide Package or will provide the current App Detail
 */
@Throws(PackageManager.NameNotFoundException::class)
fun Context.getAppVersionName(pName: String = packageName): String {
    return packageManager.getPackageInfo(pName, 0).versionName
}

/**
 * get Application Version Code
 *
 * @property pName the Package Name of the Target Application, Default is Current.
 *
 * Provide Package or will provide the current App Detail
 */
@Throws(PackageManager.NameNotFoundException::class)
fun Context.getAppVersionCode(pName: String = packageName): Long {
    return packageManager.getPackageInfo(pName, 0).longVersionCode
}




/**
 * All the Audios from the User Phone
 *
 * Get them easily with the below method, Make Sure You have READ_EXTERNAL_STORAGE Permission
 */
@RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
fun Context.getAllAudios(
    sortBy: ContentColumns = ContentColumns.DATE_ADDED,
    order: ContentOrder = ContentOrder.DESCENDING
): List<String>? {
    val data = mutableListOf<String>()
    val cursor = contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        arrayOf(MediaStore.Audio.Media.DATA),
        null,
        null,
        sortBy.s + " " + order.s
    )
    cursor?.let {
        val columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        while (cursor.isClosed.not() && cursor.moveToNext()) {
            cursor.getString(columnIndexData).let {
                if (it.toFile().exists()) {
                    data.add(it)
                }
            }
        }
        cursor.close()
    }
    return data.toList()
}

/**
 * Show Date Picker and Get the Picked Date Easily
 */
fun Context.showDatePicker(year: Int, month: Int, day: Int, onDatePicked: (year: Int, month: Int, day: Int) -> Unit) {
    DatePickerDialog(this, { _, pyear, pmonth, pdayOfMonth ->
        onDatePicked(pyear, pmonth, pdayOfMonth)
    }, year, month, day).show()
}

/**
 * Show the Time Picker and Get the Picked Time Easily
 */
fun Context.showTimePicker(
    currentDate: Date = com.crazylegend.kotlinextensions.date.currentDate,
    is24Hour: Boolean = false,
    onDatePicked: (hour: Int, minute: Int) -> Unit
) {
    @Suppress("DEPRECATION")
    TimePickerDialog(this, { _, hourOfDay, minute ->
        onDatePicked(hourOfDay, minute)

    }, currentDate.hours, currentDate.minutes, is24Hour).show()
}

/**
 * get Android ID
 */
val Context.getAndroidID: String? @SuppressLint("HardwareIds")
get() {
    return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
}


/**
 * get Device IMEI
 *
 * Requires READ_PHONE_STATE Permission
 */
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("HardwareIds")
@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun Context.getIMEI() = telephonyManager?.imei


/**
 * Creates shortcut launcher for pre/post oreo devices
 */
@Suppress("DEPRECATION")
inline fun <reified T> Activity.createShortcut(title: String, @DrawableRes icon: Int) {
    val shortcutIntent = Intent(this, T::class.java)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) { // code for adding shortcut on pre oreo device
        val intent = Intent("com.android.launcher.action.INSTALL_SHORTCUT")
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent)
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title)
        intent.putExtra("duplicate", false)
        val parcelable = Intent.ShortcutIconResource.fromContext(this, icon)
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, parcelable)
        this.sendBroadcast(intent)
       // println("added_to_homescreen")
    } else {
        val shortcutManager = this.getSystemService(ShortcutManager::class.java)
        if (shortcutManager.isRequestPinShortcutSupported) {
            val pinShortcutInfo = ShortcutInfo.Builder(this, "some-shortcut-")
                .setIntent(shortcutIntent)
                .setIcon(Icon.createWithResource(this, icon))
                .setShortLabel(title)
                .build()

            shortcutManager.requestPinShortcut(pinShortcutInfo, null)
           // println("added_to_homescreen")
        } else {
           // println("failed_to_add")
        }
    }
}


/**
 * Reboot the application
 *
 * @param[restartIntent] optional, desired activity to show after the reboot
 */
fun Context.reboot(restartIntent: Intent = this.packageManager.getLaunchIntentForPackage(this.packageName)) {
    restartIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    if (this is Activity) {
        this.startActivity(restartIntent)
        finishAffinity(this)
    } else {
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        this.startActivity(restartIntent)
    }
}

/* ********************************************
 *               Private methods              *
 ******************************************** */

private fun finishAffinity(activity: Activity) {
    activity.setResult(Activity.RESULT_CANCELED)
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> activity.finishAffinity()
        else -> ActivityCompat.finishAffinity(activity)
    }
}


inline fun Context.color(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)
inline fun Context.boolean(@BoolRes id: Int): Boolean = resources.getBoolean(id)
inline fun Context.integer(@IntegerRes id: Int): Int = resources.getInteger(id)
inline fun Context.dimen(@DimenRes id: Int): Float = resources.getDimension(id)
inline fun Context.dimenPixelSize(@DimenRes id: Int): Int = resources.getDimensionPixelSize(id)
inline fun Context.drawable(@DrawableRes id: Int): Drawable? = ContextCompat.getDrawable(this, id)

//Attr retrievers
fun Context.resolveColor(@AttrRes attr: Int, @ColorInt fallback: Int = 0): Int {
    val a = theme.obtainStyledAttributes(intArrayOf(attr))
    try {
        return a.getColor(0, fallback)
    } finally {
        a.recycle()
    }
}

fun Context.resolveDrawable(@AttrRes attr: Int): Drawable? {
    val a = theme.obtainStyledAttributes(intArrayOf(attr))
    try {
        return a.getDrawable(0)
    } finally {
        a.recycle()
    }
}

fun Context.resolveBoolean(@AttrRes attr: Int, fallback: Boolean = false): Boolean {
    val a = theme.obtainStyledAttributes(intArrayOf(attr))
    try {
        return a.getBoolean(0, fallback)
    } finally {
        a.recycle()
    }
}

fun Context.resolveString(@AttrRes attr: Int, fallback: String = ""): String {
    val v = TypedValue()
    return if (theme.resolveAttribute(attr, v, true)) v.string.toString() else fallback
}


fun Context.cancelNotification(notificationID: Int) = NotificationManagerCompat.from(this).cancel(notificationID)


fun Context.string(@StringRes str: Int): String {
    return getString(str)
}


fun Context.dimenInt(@DimenRes dmn: Int): Int {
    return resources.getDimensionPixelSize(dmn)
}

fun Context.int(@IntegerRes int: Int): Int {
    return resources.getInteger(int)
}

fun Context.font(@FontRes font: Int): Typeface? {
    return ResourcesCompat.getFont(this, font)
}

fun Context.stringArray(array: Int): Array<String> {
    return resources.getStringArray(array)
}

fun Context.intArray(array: Int): IntArray {
    return resources.getIntArray(array)
}

/**
 * Checks if a Broadcast can be resolved
 */
fun Context.canResolveBroadcast(intent: Intent) = packageManager.queryBroadcastReceivers(intent, 0).isNotEmpty()

/**
 * Checks if a Provider exists with given name
 */
fun Context.providerExists(providerName: String) = packageManager.resolveContentProvider(providerName, 0) != null


fun Context.watchYoutubeVideo( id: String) {
    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
    val webIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("http://www.youtube.com/watch?v=$id")
    )
    try {
        this.startActivity(appIntent)
    } catch (ex: ActivityNotFoundException) {
        this.startActivity(webIntent)
    }

}