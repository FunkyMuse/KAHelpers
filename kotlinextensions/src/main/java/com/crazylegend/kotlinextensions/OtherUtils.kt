package com.crazylegend.kotlinextensions

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.AssetManager
import android.content.res.TypedArray
import android.graphics.Color
import android.os.BatteryManager
import android.os.Build
import android.os.Build.*
import android.os.Build.VERSION.*
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.collection.LruCache
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.basehelpers.InMemoryCache
import com.crazylegend.kotlinextensions.context.batteryManager
import com.crazylegend.kotlinextensions.misc.DefaultUserAgent
import com.crazylegend.kotlinextensions.memory.bytes
import java.io.InputStream
import java.math.BigInteger
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
    if (Build.VERSION.SDK_INT > if (included) api - 1 else api) {
        block()
    }
}

/**
 * Method to check is belowApi.
 */
inline fun belowApi(api: Int, included: Boolean = false, block: () -> Unit) {
    if (Build.VERSION.SDK_INT < if (included) api + 1 else api) {
        block()
    }
}

inline fun AssetManager.openAsString(fileName: String): String {
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
    Handler().postDelayed(action, timeUnit.toMillis(delay))
}

/**
 * Extension method to run block of code on UI Thread after specific Delay.
 */
fun runDelayedOnUiThread(delay: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, action: () -> Unit) {
    ContextHandler.handler.postDelayed(action, timeUnit.toMillis(delay))
}

/**
 * Extension method to get the TAG name for all object
 */
fun <T : Any> T.TAG() = this::class.simpleName

val <T : Any> T.TAG get() = this::class.simpleName.toString()


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
fun tryOrIgnore(runnable: () -> Unit) = try {
    runnable()
} catch (e: Exception) {
    e.printStackTrace()
}

/**
 * get CurrentTimeInMillis from System.currentTimeMillis
 */
inline val currentTimeMillis: Long get() = System.currentTimeMillis()


/**
 * get Saved Data from memory, null if it os not exists
 */
fun getFromMemory(key: String): Any? = InMemoryCache.get(key)


/**
 * put Something In Memory to use it later
 */
fun putInMemory(key: String, any: Any?) = InMemoryCache.put(key, any)


fun Context.dp2px(dpValue: Float): Int {
    return (dpValue * resources.displayMetrics.density + 0.5f).toInt()
}
fun Context.dp2px(dpValue: Int): Int {
    return (dpValue * resources.displayMetrics.density + 0.5f).toInt()
}
fun Context.px2dp(pxValue: Int): Float {
    return pxValue / resources.displayMetrics.density + 0.5f
}
fun Context.px2dp(pxValue: Float): Float {
    return pxValue / resources.displayMetrics.density + 0.5f
}
fun Fragment.dp2px(dpValue: Float): Int {
    return requireActivity().dp2px(dpValue)
}
fun Fragment.dp2px(dpValue: Int): Int {
    return requireActivity().dp2px(dpValue)
}
fun Fragment.px2dp(pxValue: Int): Float {
    return requireActivity().px2dp(pxValue)
}
fun View.px2dp(pxValue: Float): Float? {
    return context?.px2dp(pxValue)
}
fun View.dp2px(dpValue: Float): Int? {
    return context?.dp2px(dpValue)
}
fun View.dp2px(dpValue: Int): Int? {
    return context?.dp2px(dpValue)
}
fun View.px2dp(pxValue: Int): Float? {
    return context?.px2dp(pxValue)
}
fun RecyclerView.ViewHolder.px2dp(pxValue: Float): Float? {
    return itemView.px2dp(pxValue)
}
fun RecyclerView.ViewHolder.dp2px(dpValue: Float): Int? {
    return itemView.dp2px(dpValue)
}
fun RecyclerView.ViewHolder.dp2px(dpValue: Int): Int? {
    return itemView.dp2px(dpValue)
}
fun RecyclerView.ViewHolder.px2dp(pxValue: Int): Float? {
    return itemView.px2dp(pxValue)
}

fun getBatteryInfo(batteryIntent: Intent): String {
    val status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
    val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
            || status == BatteryManager.BATTERY_STATUS_FULL
    val chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
    val usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
    val acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC

    val level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
    val scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

    val batteryPct = level / scale.toFloat()
    return "Battery Info: isCharging=$isCharging isUsbCharging=$usbCharge isACcharging=$acCharge batteryPct=$batteryPct"
}



val Context.getBatteryPercentage get() =  batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)


val Context.batteryStatusIntent: Intent? get() =  IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
    registerReceiver(null, ifilter)
}

val Context.batteryHelperStatus: Int get() = batteryStatusIntent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1

val Context.isCharging get()  = batteryHelperStatus == BatteryManager.BATTERY_STATUS_CHARGING
        || batteryHelperStatus == BatteryManager.BATTERY_STATUS_FULL

// How are we charging?
val Context.isChargePlugCharging get()  = batteryStatusIntent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1
val Context.isUsbCharging get() =  isChargePlugCharging == BatteryManager.BATTERY_PLUGGED_USB
val Context.isACcharging get() =  isChargePlugCharging == BatteryManager.BATTERY_PLUGGED_AC



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

val randomUUIDstring get()  = UUID.randomUUID().toString()

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

fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String = bufferedReader(charset).use { it.readText() }

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


fun getMemoryUsage(transform: (Long, Long, Long, Long, Int) -> String = { total, free, max, usage, percent -> "$usage / $max MB in use ($percent%)" }): Pair<Int, String> {
    val total = Runtime.getRuntime().totalMemory().bytes.toMegaBytes
    val free = Runtime.getRuntime().freeMemory().bytes.toMegaBytes
    val max = Runtime.getRuntime().maxMemory().bytes.toMegaBytes
    val usage = total - free
    val percent = ((usage.toDouble()/max) * 100).toInt()
    return percent to transform(total, free, max, usage, percent)
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
fun benchmarkAction(actionName: String, action: () -> Unit) {
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
fun <T>allIsEqual(vararg values: T): Boolean {
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
fun <T, R>allIsNotNull(vararg values: T, out: () -> R?): R? {
    values.forEach {
        if (it == null) return null
    }
    return out()
}