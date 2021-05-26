package com.crazylegend.common

import android.Manifest.permission.ACCESS_WIFI_STATE
import android.Manifest.permission.INTERNET
import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.AssetManager
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build.*
import android.os.Build.VERSION.*
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.os.Handler
import android.os.Looper
import android.os.Process.killProcess
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.annotation.RequiresPermission
import androidx.collection.LruCache
import com.crazylegend.common.device.DefaultUserAgent
import java.io.Closeable
import java.math.BigInteger
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by hristijan on 2/22/19 to long live and prosper borrowed from http://kotlinextensions.com
 */


/**
 * Method to check is aboveApi.
 */
inline fun aboveApi(api: Int, included: Boolean = false, block: () -> Unit) {
    if (SDK_INT > if (included) api - 1 else api) {
        block()
    }
}

/**
 * Method to check is belowApi.
 */
inline fun belowApi(api: Int, included: Boolean = false, block: () -> Unit) {
    if (SDK_INT < if (included) api + 1 else api) {
        block()
    }
}

fun AssetManager.openAsString(fileName: String): String {
    val inputStream = open(fileName)
    val size = inputStream.available()
    val buffer = ByteArray(size)
    inputStream.read(buffer)
    inputStream.close()
    return String(buffer, Charset.forName("UTF-8"))
}


/**
 * Check if is Main Thread.
 */
inline val isMainThread: Boolean get() = Looper.myLooper() == Looper.getMainLooper()


/**
 * Extension method to run block of code after specific Delay.
 */
fun runDelayed(delay: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, action: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(action, timeUnit.toMillis(delay))
}

/**
 * Extension method to run block of code on UI Thread after specific Delay.
 */
fun runDelayedOnUiThread(delay: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, action: () -> Unit) {
    ContextHandler.handler.postDelayed(action, timeUnit.toMillis(delay))
}

fun runOnUiThread(action: () -> Unit) {
    ContextHandler.handler.post {
        action()
    }
}


/**
 * Extension method to get the TAG name for all object
 */
fun <T : Any> T.TAG() = this::class.simpleName

val <T : Any> T.TAG get() = this::class.simpleName.toString()

inline fun <reified T> tag(): String {
    return T::class.java.simpleName
}


val <T> T.exhaustive: T
    get() = this


fun <T> T.asNullable(): T? = this


/**
 * Provides handler and mainThreadScheduler.
 */
private object ContextHandler {
    val handler = Handler(Looper.getMainLooper())
    val mainThread = Looper.getMainLooper().thread
}

/**
 * try the code in [runnable], If it runs then its perfect if its not, It won't crash your app.
 */
inline fun tryOrIgnore(runnable: () -> Unit) = try {
    runnable()
} catch (e: Exception) {
    e.printStackTrace()
}

/**
 * get CurrentTimeInMillis from System.currentTimeMillis
 */
inline val currentTimeMillis: Long get() = System.currentTimeMillis()





/**
 * Use [batteryStatusIntent] for the param
 * @param batteryIntent Intent
 * @return BatteryStatusModel
 */
fun getBatteryInfo(batteryIntent: Intent): BatteryStatusModel {
    val status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
    val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
            || status == BatteryManager.BATTERY_STATUS_FULL
    val chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
    val usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
    val acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC
    val wirelessCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS

    val level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
    val scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

    val batteryPct = level / scale.toFloat()
    return BatteryStatusModel(isCharging, usbCharge, wirelessCharge, acCharge, batteryPct, scale.toFloat())
}

val Context.actualPackageName: String?
    get() = applicationContext.javaClass.`package`?.name

val Context.flavor: String?
    get() = getBuildConfigValue(actualPackageName, "FLAVOR") as String?

val Context.appName: String
    get() {
        val applicationInfo = applicationContext.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) {
            applicationInfo.nonLocalizedLabel.toString()
        } else {
            applicationContext.getString(stringId)
        }
    }

/**
 * Gets a field from the project's BuildConfig. This is useful when, for example, flavors
 * are used at the project level to set custom fields.
 * @param fieldName The name of the field-to-access
 * @return The value of the field, or `null` if the field is not found.
 */
fun getBuildConfigValue(packageName: String?, fieldName: String): Any? {
    val buildConfigClassName = "$packageName.BuildConfig"
    return try {
        val clazz = Class.forName(buildConfigClassName)
        val field = clazz.getField(fieldName)
        field.get(null)
    } catch (e: ClassNotFoundException) {
        null
    } catch (e: NoSuchFieldException) {
        null
    } catch (e: IllegalAccessException) {
        null
    }
}

val Context.shortAppName: String?
    get() = actualPackageName?.substringAfterLast('.')



val Context.batteryStatusIntent: Intent?
    get() = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

val Context.batteryHelperStatus: Int
    get() = batteryStatusIntent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1

val Context.isCharging
    get() = batteryHelperStatus == BatteryManager.BATTERY_STATUS_CHARGING
            || batteryHelperStatus == BatteryManager.BATTERY_STATUS_FULL

// How are we charging?
val Context.isChargePlugCharging
    get() = batteryStatusIntent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1
val Context.isUsbCharging get() = isChargePlugCharging == BatteryManager.BATTERY_PLUGGED_USB
val Context.isACcharging get() = isChargePlugCharging == BatteryManager.BATTERY_PLUGGED_AC


/**
 * Use [batteryStatusIntent] for the param
 * @param batteryIntent Intent
 * @return Float
 */
fun getBatteryLevel(batteryIntent: Intent): Float {
    val level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
    val scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
    return level / scale.toFloat()
}

/**
 * Gets value with specific key from the cache. If the value is not present,
 * calls [defaultValue] to obtain a non-null value which is placed into the
 * cache, then returned.
 *
 * This method is thread-safe.
 */
inline fun <K, V> LruCache<K, V>.getOrPut(key: K, defaultValue: () -> V): V {
    synchronized(this) {
        this[key]?.let { return it }
        return defaultValue().apply { put(key, this) }
    }
}

/**
 * Gets value with specific key from the cache. If the value is not present,
 * calls [defaultValue] to obtain a value which is placed into the cache
 * if not null, then returned.
 *
 * This method is thread-safe.
 */
inline fun <K, V> LruCache<K, V>.getOrPutNotNull(key: K, defaultValue: () -> V?): V? {
    synchronized(this) {
        this[key]?.let { return it }
        return defaultValue()?.apply { put(key, this) }
    }
}

/**
 * Returns an array containing the keys in the cache.
 */
fun <V> LruCache<Int, V>.keys(): IntArray =
        snapshot().keys.toIntArray()

/**
 * Returns an array containing the keys in the cache.
 */
fun <V> LruCache<Long, V>.keys(): LongArray =
        snapshot().keys.toLongArray()

/**
 * Returns an array containing the keys in the cache.
 */
inline fun <reified K, V> LruCache<K, V>.keys(): Array<K> =
        snapshot().keys.toTypedArray()

val randomUUIDstring get() = UUID.randomUUID().toString()

fun CharSequence.isEmptyNullOrStringNull(): Boolean {
    return isNullOrEmpty() || this == "null"
}

fun Any?.ifNull(block: () -> Unit) {
    if (this == null) block()
}


fun UUID.toLong(): Long {
    var longValue: Long
    do {
        val buffer = ByteBuffer.wrap(ByteArray(16))
        buffer.putLong(leastSignificantBits)
        buffer.putLong(mostSignificantBits)
        val bi = BigInteger(buffer.array())
        longValue = bi.toLong()
    } while (longValue < 0)
    return longValue
}


inline fun <T> T.alsoIfTrue(boolean: Boolean, block: (T) -> Unit): T {
    if (boolean) block(this)
    return this
}

inline fun <T> T.alsoIfFalse(boolean: Boolean, block: (T) -> Unit): T {
    if (!boolean) block(this)
    return this
}

inline fun <T> tryOrNull(block: () -> T): T? = try {
    block()
} catch (e: Exception) {
    null
}


inline fun <T> tryOrNullPrint(block: () -> T): T? = try {
    block()
} catch (e: Exception) {
    e.printStackTrace()
    null
}

inline fun tryOrPrint(block: () -> Unit) = try {
    block()
} catch (e: Exception) {
    e.printStackTrace()
}

inline fun trySilently(block: () -> Unit) = try {
    block()
} catch (e: Exception) {
}

inline fun <T> tryOrElse(defaultValue: T, block: () -> T): T = tryOrNull(block)
        ?: defaultValue

inline fun tryOrElse(defaultBlock: () -> Unit = {}, block: () -> Unit) = try {
    block()
} catch (e: Exception) {
    defaultBlock()
}

inline fun <T> T.applyIf(condition: Boolean, block: T.() -> T): T = apply {
    if (condition) {
        block()
    }
}

inline fun <T, R> T.letIf(condition: Boolean, block: (T) -> R): R? = let {
    if (condition) {
        block(it)
    } else {
        null
    }
}

inline fun <T, R> T.runIf(condition: Boolean, block: T.() -> R): R? = run {
    if (condition) {
        block()
    } else {
        null
    }
}

inline fun <T> T.alsoIf(condition: Boolean, block: (T) -> T): T = also {
    if (condition) {
        block(it)
    }
}

inline fun <T, R> withIf(receiver: T, condition: Boolean, block: T.() -> R): R? = with(receiver) {
    if (condition) {
        block()
    } else {
        null
    }
}

/**
 * Creates a [ContentValues] by reading [pairs] - similarly to how [mapOf]
 * and similar methods work.
 *
 * Note that value types are limited to those acceptable by [ContentValues] -
 * specifically, [String], [Byte], [Short], [Int], [Long], [Float], [Double],
 * [Boolean], [ByteArray].
 */
fun contentValuesOf(vararg pairs: Pair<String, Any?>): ContentValues {
    return ContentValues().apply {
        pairs.forEach {
            when (it.second) {
                null -> putNull(it.first)
                is String -> put(it.first, it.second as String)
                is Byte -> put(it.first, it.second as Byte)
                is Short -> put(it.first, it.second as Short)
                is Int -> put(it.first, it.second as Int)
                is Long -> put(it.first, it.second as Long)
                is Float -> put(it.first, it.second as Float)
                is Double -> put(it.first, it.second as Double)
                is Boolean -> put(it.first, it.second as Boolean)
                is ByteArray -> put(it.first, it.second as ByteArray)
            }
        }
    }
}

/**
 * Shorthand for [ContentValues.set].
 */
operator fun ContentValues.set(key: String, value: String?) {
    put(key, value)
}

/**
 * Shorthand for [ContentValues.set].
 */
operator fun ContentValues.set(key: String, value: Byte?) {
    put(key, value)
}

/**
 * Shorthand for [ContentValues.set].
 */
operator fun ContentValues.set(key: String, value: Short?) {
    put(key, value)
}

/**
 * Shorthand for [ContentValues.set].
 */
operator fun ContentValues.set(key: String, value: Int?) {
    put(key, value)
}

/**
 * Shorthand for [ContentValues.set].
 */
operator fun ContentValues.set(key: String, value: Long?) {
    put(key, value)
}

/**
 * Shorthand for [ContentValues.set].
 */
operator fun ContentValues.set(key: String, value: Float?) {
    put(key, value)
}

/**
 * Shorthand for [ContentValues.set].
 */
operator fun ContentValues.set(key: String, value: Double?) {
    put(key, value)
}

/**
 * Shorthand for [ContentValues.set].
 */
operator fun ContentValues.set(key: String, value: Boolean?) {
    put(key, value)
}

/**
 * Shorthand for [ContentValues.set].
 */
operator fun ContentValues.set(key: String, value: ByteArray?) {
    put(key, value)
}

/**
 * Invokes [block], passing it the receiver and returning the value
 * it returns. Recycles the receiver after [block] completes.
 *
 * @param block Lambda function accepting an instance of the receiver.
 *
 * @return Value returned by [block].
 */
fun <T> TypedArray.use(block: (TypedArray) -> T): T {
    val r = block(this)
    recycle()
    return r
}

/**
 * Assume that this Int is a color and apply [opacity] to it.
 */
@ColorInt
fun Int.withOpacity(@IntRange(from = 0, to = 100) opacity: Int): Int {
    return Color.argb((opacity / 100f * 255).toInt(), Color.red(this), Color.green(this), Color.blue(this))
}


@SuppressLint("HardwareIds")
fun Context.createDeviceBuild(): Map<String, String> {
    val info = LinkedHashMap<String, String>()
    // http://developer.android.com/reference/android/os/Build.html

    info["Model"] = MODEL
    info["Manufacturer"] = MANUFACTURER
    info["Release"] = RELEASE
    info["SDK_INT"] = SDK_INT.toString()
    info["TIME"] = Date(TIME).toString()

    if (SDK_INT >= LOLLIPOP)
        info["SUPPORTED_ABIS"] = Arrays.toString(SUPPORTED_ABIS)
    else {

        @Suppress("DEPRECATION")
        info["CPU_ABI"] = CPU_ABI
        @Suppress("DEPRECATION")
        info["CPU_ABI2"] = CPU_ABI2
    }

    info["Board"] = BOARD
    info["Bootloader"] = BOOTLOADER
    info["Brand"] = BRAND
    info["Device"] = DEVICE
    info["Display"] = DISPLAY
    info["Fingerprint"] = FINGERPRINT
    info["Hardware"] = HARDWARE
    info["Host"] = HOST
    info["Id"] = ID
    info["Product"] = PRODUCT
    info["Tags"] = TAGS
    info["Type"] = TYPE
    info["User"] = USER
    info["ANDROID ID"] = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)


    // http://developer.android.com/reference/android/os/Build.VERSION.html
    info["Codename"] = CODENAME
    info["Incremental"] = INCREMENTAL
    info["User Agent"] = DefaultUserAgent.getDefaultUserAgent(this)
    info["HTTP Agent"] = System.getProperty("http.agent") ?: ""

    return info
}

/**
 * Simple way to calculate approximated function execution time
 *
 * @property actionName - String to define action name in logcat
 * @property action - action to measure
 */
inline fun benchmarkAction(actionName: String, action: () -> Unit) {
    Log.i("BENCHMARK", "___________________________________")
    Log.i("BENCHMARK", "Action name: $actionName")
    val startTime = System.currentTimeMillis()
    Log.i("BENCHMARK", "Start time: $startTime")
    action.invoke()
    val endTime = System.currentTimeMillis()
    Log.i("BENCHMARK", "End time: $endTime")
    Log.i("BENCHMARK", "Action duration (millis): ${endTime - startTime}}")
}


/**
 * Checking that all elements is equals
 *
 * @property values - vararg of checking elements
 *
 * allIsEqual("test", "test", "test") will return true
 */
fun <T> allIsEqual(vararg values: T): Boolean {
    when {
        values.isEmpty() -> return false
        values.size == 1 -> return true
    }
    values.forEach {
        if ((it == values.first()).not()) return false
    }
    return true
}

/**
 * Executing out callback if all vararg values is not null
 *
 * Like .multilet , but not returning values
 *
 * @property values - vararg of checking elements
 *
 * val a = "a"
 * val b = "b"
 * val c = null
 *
 * allIsNotNull(a,b) {
 *     this action will be executed
 * }
 *
 * allIsNotNull(a,b,c) {
 *     this action will NOT be executed
 * }
 *
 */
fun <T, R> allIsNotNull(vararg values: T, out: () -> R?): R? {
    values.forEach {
        if (it == null) return null
    }
    return out()
}


/**
 * Implementation of lazy that is not thread safe. Useful when you know what thread you will be
 * executing on and are not worried about synchronization.
 */
fun <T> lazyFast(operation: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE) {
    operation()
}


/**
 * Gets current wallpaper
 * @receiver Context
 * @return Drawable?
 */
fun Context.getWallpaperDrawable(): Drawable? = WallpaperManager.getInstance(this).peekDrawable()


/**
 * Returns true if the current layout direction is [View.LAYOUT_DIRECTION_RTL].
 *
 * @return
 *
 *This always returns false on versions below JELLY_BEAN_MR1.
 */
fun isRtlLayout() =
        TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL




/**
 * Return the MAC address.
 *
 * Must hold
 * `<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />`,
 * `<uses-permission android:name="android.permission.INTERNET" />`
 *
 * @return the MAC address
 */
@RequiresPermission(allOf = [ACCESS_WIFI_STATE, INTERNET])
fun getMacAddress(context: Context): String {
    return getMacAddress(
            context,
            *((null as Array<String>?)!!)
    )
}

/**
 * Return the MAC address.
 *
 * Must hold
 * `<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />`,
 * `<uses-permission android:name="android.permission.INTERNET" />`
 *
 * @return the MAC address
 */
@RequiresPermission(allOf = [ACCESS_WIFI_STATE, INTERNET])
fun getMacAddress(context: Context, vararg excepts: String): String {
    var macAddress =
            getMacAddressByWifiInfo(context)
    if (isAddressNotInExcepts(
                    macAddress,
                    *excepts
            )
    ) {
        return macAddress
    }
    macAddress =
            getMacAddressByNetworkInterface()
    if (isAddressNotInExcepts(
                    macAddress,
                    *excepts
            )
    ) {
        return macAddress
    }
    macAddress = getMacAddressByInetAddress()
    if (isAddressNotInExcepts(
                    macAddress,
                    *excepts
            )
    ) {
        return macAddress
    }

    return ""
}


private fun isAddressNotInExcepts(address: String, vararg excepts: String): Boolean {
    if (excepts.isEmpty()) {
        return "02:00:00:00:00:00" != address
    }
    for (filter in excepts) {
        if (address == filter) {
            return false
        }
    }
    return true
}

private fun getMacAddressByNetworkInterface(): String {
    try {
        val nis = NetworkInterface.getNetworkInterfaces()
        while (nis.hasMoreElements()) {
            val ni = nis.nextElement()
            if (ni == null || !ni.name.equals("wlan0", ignoreCase = true)) continue
            val macBytes = ni.hardwareAddress
            if (macBytes != null && macBytes.isNotEmpty()) {
                val sb = StringBuilder()
                for (b in macBytes) {
                    sb.append(String.format("%02x:", b))
                }
                return sb.substring(0, sb.length - 1)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return "02:00:00:00:00:00"
}

private fun getMacAddressByInetAddress(): String {
    try {
        val inetAddress = getInetAddress()
        if (inetAddress != null) {
            val ni = NetworkInterface.getByInetAddress(inetAddress)
            if (ni != null) {
                val macBytes = ni.hardwareAddress
                if (macBytes != null && macBytes.isNotEmpty()) {
                    val sb = StringBuilder()
                    for (b in macBytes) {
                        sb.append(String.format("%02x:", b))
                    }
                    return sb.substring(0, sb.length - 1)
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return "02:00:00:00:00:00"
}


private fun getInetAddress(): InetAddress? {
    try {
        val nis = NetworkInterface.getNetworkInterfaces()
        while (nis.hasMoreElements()) {
            val ni = nis.nextElement()
            // To prevent phone of xiaomi return "10.0.2.15"
            if (!ni.isUp) continue
            val addresses = ni.inetAddresses
            while (addresses.hasMoreElements()) {
                val inetAddress = addresses.nextElement()
                if (!inetAddress.isLoopbackAddress) {
                    val hostAddress = inetAddress.hostAddress
                    if (hostAddress.indexOf(':') < 0) return inetAddress
                }
            }
        }
    } catch (e: SocketException) {
        e.printStackTrace()
    }

    return null
}


/**
 * Return the android id of device.
 *
 * @return the android id of device
 */
@SuppressLint("HardwareIds")
fun Context.getAndroidID(): String {

    val id = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
    )
    return id ?: ""
}


@SuppressLint("HardwareIds", "MissingPermission")
private fun getMacAddressByWifiInfo(context: Context): String {
    try {
        val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = wifi.connectionInfo
        if (info != null) return info.macAddress
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return "02:00:00:00:00:00"
}


/**
 * Return the manufacturer of the product/hardware.
 *
 * e.g. Xiaomi
 *
 * @return the manufacturer of the product/hardware
 */
fun getManufacturer(): String = MANUFACTURER

/**
 * Return the model of device.
 *
 * e.g. MI2SC
 *
 * @return the model of device
 */
fun getDeviceModel(): String {
    var model: String? = MODEL
    model = model?.trim { it <= ' ' }?.replace("\\s*".toRegex(), "") ?: ""
    return model
}

/**
 * Return an ordered list of ABIs supported by this device. The most preferred ABI is the first
 * element in the list.
 *
 * @return an ordered list of ABIs supported by this device
 */
fun getSupportedABIs(): Array<String> = SUPPORTED_ABIS




inline fun <reified T> Any?.cast() = this as? T

inline fun <reified T> Any.force() = this as T

inline fun <T> T?.ifNotNull(toBoolean: T.() -> Boolean) =
        if (this != null) toBoolean() else false

inline fun killProcess(cleanUps: () -> Unit = {}) {
    cleanUps()
    killProcess(android.os.Process.myPid())
}

fun Closeable.closeQuietly() {
    try {
        close()
    } catch (rethrown: RuntimeException) {
        throw rethrown
    } catch (_: Exception) {
    }
}

inline fun needPermissionsFor(action: () -> Unit) = try {
    action.invoke()
    false
} catch (e: SecurityException) {
    true
}
