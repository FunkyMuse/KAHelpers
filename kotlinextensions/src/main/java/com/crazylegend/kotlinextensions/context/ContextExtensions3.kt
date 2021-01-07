package com.crazylegend.kotlinextensions.context

import android.Manifest
import android.Manifest.permission.REBOOT
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager.RunningAppProcessInfo.*
import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.appwidget.AppWidgetManager
import android.content.*
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.text.TextUtils.isEmpty
import android.util.TypedValue
import android.view.View
import androidx.annotation.*
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.crazylegend.kotlinextensions.basehelpers.DeviceRingerMode
import com.crazylegend.kotlinextensions.ifTrue
import com.crazylegend.kotlinextensions.string.toFile
import com.crazylegend.kotlinextensions.withOpacity
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*


/**
 * Created by hristijan on 2/27/19 to long live and prosper !
 */


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
fun Context.startApp(packageName: String) = isAppInstalled(packageName).ifTrue { startActivity(packageManager.getLaunchIntentForPackage(packageName)) }

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
 * Checks if App is in Background
 */
fun Context.isBackground(pName: String = packageName): Boolean {
    activityManager.runningAppProcesses.forEach {
        @Suppress("DEPRECATION")
        if (it.processName == pName)
            return it.importance == IMPORTANCE_BACKGROUND
    }
    return false
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
@RequiresApi(Build.VERSION_CODES.P)
@Throws(PackageManager.NameNotFoundException::class)
fun Context.getAppVersionCode(pName: String = packageName): Long {
    return packageManager.getPackageInfo(pName, 0).longVersionCode
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
        currentDate: Date = com.crazylegend.kotlinextensions.dateAndTime.currentDate,
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
val Context.getAndroidID: String?
    @SuppressLint("HardwareIds")
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
 * Reboot the application
 *
 * @param[restartIntent] optional, desired activity to show after the reboot
 */
fun Context.reboot(restartIntent: Intent? = this.packageManager.getLaunchIntentForPackage(this.packageName)) {
    restartIntent?.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    if (this is Activity) {
        this.startActivity(restartIntent)
        finishAffinity(this)
    } else {
        restartIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        this.startActivity(restartIntent)
    }
}

fun finishAffinity(activity: Activity) {
    activity.setResult(Activity.RESULT_CANCELED)
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> activity.finishAffinity()
        else -> ActivityCompat.finishAffinity(activity)
    }
}


fun Context.color(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)
fun Context.boolean(@BoolRes id: Int): Boolean = resources.getBoolean(id)
fun Context.integer(@IntegerRes id: Int): Int = resources.getInteger(id)
fun Context.dimen(@DimenRes id: Int): Float = resources.getDimension(id)
fun Context.dimenPixelSize(@DimenRes id: Int): Int = resources.getDimensionPixelSize(id)
fun Context.drawable(@DrawableRes id: Int): Drawable? = ContextCompat.getDrawable(this, id)

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


fun Context.watchYoutubeVideo(id: String) {
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

inline fun <reified T> Context.getAppWidgetsIdsFor(): IntArray {
    return AppWidgetManager.getInstance(this).getAppWidgetIds(
            ComponentName(this, T::class.java)
    )
}

fun Context?.openGoogleMaps(address: String?) {
    this ?: return
    if (isEmpty(address))
        return

    val gmmIntentUri = Uri.parse("geo:0,0?q=${address?.trim()}")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.`package` = "com.google.android.apps.maps"

    if (mapIntent.resolveActivity(packageManager) != null) {
        startActivity(mapIntent)
    }
}


fun Context.unRegisterReceiverSafe(broadcastReceiver: BroadcastReceiver) {
    // needs to be in try catch in order to avoid crashing on Samsung Lollipop devices https://issuetracker.google.com/issues/37001269#c3
    try {
        this.unregisterReceiver(broadcastReceiver)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    }
}

fun Context.getFontCompat(fontRes: Int): Typeface? {
    return ResourcesCompat.getFont(this, fontRes)
}

fun Context.registerReceiverSafe(broadcastReceiver: BroadcastReceiver, intentFilter: IntentFilter) {
    // needs to be in try catch in order to avoid crashing on Samsung Lollipop devices https://issuetracker.google.com/issues/37001269#c3
    try {
        this.registerReceiver(broadcastReceiver, intentFilter)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    }
}

fun Context.getProductionApplicationId(): String {
    val applicationId = packageName
    return when {
        applicationId.contains(".stage") -> applicationId.dropLast(6)
        applicationId.contains(".debug") -> applicationId.dropLast(6)
        else -> applicationId
    }
}


fun Context.areNotificationsEnabled(): Boolean {
    return NotificationManagerCompat.from(this).areNotificationsEnabled()
}

fun Context.createInputStreamFromUri(uri: Uri): InputStream? {
    return contentResolver.openInputStream(uri)
}


@RequiresApi(Build.VERSION_CODES.M)
inline fun <reified T> Context.systemService() = getSystemService(T::class.java)

fun <T> Context.systemService(name: String) = getSystemService(name) as? T


fun Context.coloredDrawable(@DrawableRes drawableResId: Int, @ColorRes filterColorResourceId: Int): Drawable? =
        drawable(drawableResId).apply { this?.setColorFilter(color(filterColorResourceId), PorterDuff.Mode.SRC_ATOP) }

fun Context?.quantityString(@PluralsRes res: Int, quantity: Int, vararg args: Any?) =
        if (this == null) "" else resources.getQuantityString(res, quantity, *args)

fun Context?.quantityString(@PluralsRes res: Int, quantity: Int) = quantityString(res, quantity, quantity)

fun Context.uriFromResource(@DrawableRes resId: Int): String = Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(resources.getResourcePackageName(resId))
        .appendPath(resources.getResourceTypeName(resId))
        .appendPath(resources.getResourceEntryName(resId))
        .build().toString()

val Context.actionBarSizeResourse: Int
    get() =
        getResourceIdAttribute(android.R.attr.actionBarSize)

val Context.selectableItemBackgroundResource: Int
    get() =
        getResourceIdAttribute(android.R.attr.selectableItemBackground)

val Context.actionBarItemBackgroundResource: Int
    get() =
        getResourceIdAttribute(android.R.attr.actionBarItemBackground)

fun Context.getResourceIdAttribute(@AttrRes attribute: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attribute, typedValue, true)
    theme.resolveAttribute(attribute, typedValue, true)
    return typedValue.resourceId
}

@RequiresPermission(REBOOT)
fun Context.reboot(reason: String?) {
    powerManager?.reboot(reason)
}

@SuppressLint("WrongConstant")
fun Context.disableBar() {
    try {
        val service = getSystemService("statusbar")
        val statusbarManager = Class.forName("android.app.StatusBarManager")
        val method = statusbarManager.getMethod("disable", Int::class.javaPrimitiveType)
        method.invoke(service, DISABLE_EXPAND or DISABLE_NAVIGATION)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

val DISABLE_EXPAND = 0x00010000
val DISABLE_NOTIFICATION_ICONS = 0x00020000
val DISABLE_NOTIFICATION_ALERTS = 0x00040000
val DISABLE_NOTIFICATION_TICKER = 0x00080000
val DISABLE_SYSTEM_INFO = 0x00100000
val DISABLE_HOME = 0x00200000
val DISABLE_RECENT = 0x01000000
val DISABLE_BACK = 0x00400000
val DISABLE_CLOCK = 0x00800000
val DISABLE_SEARCH = 0x02000000
val DISABLE_NONE = 0x00000000

val DISABLE_NAVIGATION = DISABLE_HOME or DISABLE_RECENT


val Context.processName: String?
    get() = activityManager.runningAppProcesses
            .filter { it.pid == Process.myPid() }
            .map { it.processName }
            .firstOrNull()


val Context.packageVersionName: String
    get() = packageManager.getPackageInfo(packageName, 0).versionName


val Context.isBackground: Boolean
    get() {
        val appProcess = activityManager.runningAppProcesses.firstOrNull { it.processName == packageName }
        return if (appProcess == null) {
            false
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                appProcess.importance == IMPORTANCE_CACHED || appProcess.importance == IMPORTANCE_SERVICE
            } else {
                appProcess.importance == IMPORTANCE_SERVICE || appProcess.importance == IMPORTANCE_BACKGROUND
            }
        }

    }


val isMiUi: Boolean
    get() = getSystemProperty("ro.miui.ui.version.name").isNotEmpty()


val isEmUi: Boolean
    get() = getSystemProperty("ro.build.version.emui").isNotEmpty()


fun getSystemProperty(propName: String): String {
    val process = Runtime.getRuntime().exec("getprop $propName")
    val input = BufferedReader(InputStreamReader(process.inputStream), 1024)
    val line = input.readLine()
    input.close()
    return line
}

/**
  * Disable navigation bar
  *
  * Note: System signature is required and the system UID is shared
  */
@SuppressLint("WrongConstant", "PrivateApi")
fun Context.disableNavigation() {
    try {
        val service = getSystemService("statusbar")
        val statusbarManager = Class.forName("android.app.StatusBarManager")
        val method = statusbarManager.getMethod("disable", Int::class.javaPrimitiveType)
        method.invoke(service, DISABLE_NAVIGATION)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


@SuppressLint("PrivateApi", "WrongConstant")
fun Context.disableNotificationBar() {
    try {
        val service = getSystemService("statusbar")
        val statusbarManager = Class.forName("android.app.StatusBarManager")
        val method = statusbarManager.getMethod("disable", Int::class.javaPrimitiveType)
        method.invoke(service, DISABLE_EXPAND)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Get color from resources with applied [opacity]
 */
@ColorInt
fun Context.colorWithOpacity(@ColorRes res: Int, @IntRange(from = 0, to = 100) opacity: Int): Int {
    return color(res).withOpacity(opacity)
}


/**
 * Get dimension defined by attribute [attr]
 */
fun Context.attrDimen(attr: Int): Int {
    return TypedValue.complexToDimensionPixelSize(attribute(attr).data, resources.displayMetrics)
}


/**
 * Get drawable defined by attribute [attr]
 */
fun Context.attrDrawable(attr: Int): Drawable? {
    val a = theme.obtainStyledAttributes(intArrayOf(attr))
    val attributeResourceId = a.getResourceId(0, 0)
    a.recycle()
    return drawable(attributeResourceId)
}

fun Context.tintedDrawable(@DrawableRes drawableId: Int, @ColorRes colorId: Int): Drawable? {
    val tint: Int = color(colorId)
    val drawable = drawable(drawableId)
    drawable?.mutate()
    drawable?.let {
        it.mutate()
        DrawableCompat.setTint(it, tint)
    }
    return drawable
}

fun Context.colors(@ColorRes stateListRes: Int): ColorStateList? {
    return ContextCompat.getColorStateList(this, stateListRes)
}

fun Context.attribute(value: Int): TypedValue {
    val ret = TypedValue()
    theme.resolveAttribute(value, ret, true)
    return ret
}

fun Context.musicVolume(): Int = audioManager
        .getStreamVolume(AudioManager.STREAM_MUSIC)

fun Context.propertyInAssets(propertyName: String) = Properties().also {
    val inputStream = this.assets.open("$propertyName.properties")
    it.load(inputStream)
    inputStream.close()
}

fun Context.jsonInAssets(jsonName: String): String {
    StringBuilder().let {
        this.assets.open(jsonName)
                .bufferedReader()
                .forEachLine { line ->
                    it.append(line)
                }
        return it.toString()
    }
}

fun Context.xmlInAssets(xmlName: String) = this.assets.open(xmlName)

fun Application.isApkInDebug(): Boolean {
    return try {
        val info = applicationInfo
        info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    } catch (e: Exception) {
        false
    }
}

val Context.isDarkTheme
    get() = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_NO,
        Configuration.UI_MODE_NIGHT_UNDEFINED -> false
        else -> true
    }

/**
 * Unwraps the [Activity] backing this [Context] if available. This is typically useful for
 * [View] instances, as they typically return an instance of [ContextWrapper] from [View.getContext]
 *
 * This property will return null for [Service] and [Application] backed [ContextWrapper]
 * instances as you would expect.
 */
val Context.unwrapActivity: Activity?
    get() {
        var wrapped = this
        while (wrapped is ContextWrapper)
            if (wrapped is Activity) return wrapped
            else wrapped = wrapped.baseContext

        return null
    }


fun Context.getSharedPreferencesByTag(tag: String) = getSharedPreferences(tag, Context.MODE_PRIVATE)


val Context.currentLocale: Locale
    get() = resources.configuration.run {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> locales.get(0)
            else -> locale
        }
    }


fun Context.getDeviceRingerMode(): DeviceRingerMode {
    return when (audioManager.ringerMode) {
        AudioManager.RINGER_MODE_SILENT -> DeviceRingerMode.SILENT
        AudioManager.RINGER_MODE_VIBRATE -> DeviceRingerMode.VIBRATE
        else -> DeviceRingerMode.NORMAL
    }
}