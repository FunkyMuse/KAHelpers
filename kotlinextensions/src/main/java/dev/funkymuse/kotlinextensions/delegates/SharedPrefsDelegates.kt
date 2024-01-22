package dev.funkymuse.kotlinextensions.delegates

import android.content.SharedPreferences
import dev.funkymuse.sharedpreferences.putBoolean
import dev.funkymuse.sharedpreferences.putFloat
import dev.funkymuse.sharedpreferences.putInt
import dev.funkymuse.sharedpreferences.putString
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty





/**
 * Use sharedPreferences.string( key = { "MY_KEY" } )
 * @receiver SharedPreferences
 * @param defaultValue String
 * @param key Function1<KProperty<*>, String>
 * @return ReadWriteProperty<Any, String>
 */
fun SharedPreferences.string(defaultValue: String = "", key: (KProperty<*>) -> String = KProperty<*>::name): ReadWriteProperty<Any, String> = object : ReadWriteProperty<Any, String> {
    override fun getValue(thisRef: Any, property: KProperty<*>) = getString(key(property), defaultValue).toString()

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) = putString(key(property), value)
}


/**
 * Use sharedPreferences.stringNullable( key = { "MY_KEY" }, , defaultValue = null )
 * @receiver SharedPreferences
 * @param defaultValue String?
 * @param key Function1<KProperty<*>, String>
 * @return ReadWriteProperty<Any, String?>
 */
fun SharedPreferences.stringNullable(
        defaultValue: String? = null,
        key: (KProperty<*>) -> String = KProperty<*>::name
): ReadWriteProperty<Any, String?> = object : ReadWriteProperty<Any, String?> {
    override fun getValue(thisRef: Any, property: KProperty<*>) = getString(key(property), defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) = edit().putString(key(property), value).apply()
}

/**
 * Use sharedPreferences.int( key = { "MY_KEY" }, defaultValue = 1 )
 * @receiver SharedPreferences
 * @param defaultValue Int
 * @param key Function1<KProperty<*>, String>
 * @return ReadWriteProperty<Any, Int>
 */
fun SharedPreferences.int(
        defaultValue: Int = 0,
        key: (KProperty<*>) -> String = KProperty<*>::name
): ReadWriteProperty<Any, Int> = object : ReadWriteProperty<Any, Int> {
    override fun getValue(thisRef: Any, property: KProperty<*>) = getInt(key(property), defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) = putInt(key(property), value)
}


/**
 * Use sharedPreferences.float( key = { "MY_KEY" }, defaultValue = 1f )
 * @receiver SharedPreferences
 * @param defaultValue Float
 * @param key Function1<KProperty<*>, String>
 * @return ReadWriteProperty<Any, Float>
 */
fun SharedPreferences.float(
        defaultValue: Float = 0f,
        key: (KProperty<*>) -> String = KProperty<*>::name
): ReadWriteProperty<Any, Float> = object : ReadWriteProperty<Any, Float> {
    override fun getValue(thisRef: Any, property: KProperty<*>) = getFloat(key(property), defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) = putFloat(key(property), value)
}

/**
 * Use sharedPreferences.boolean( key = { "MY_KEY" }, defaultValue = false )
 * @receiver SharedPreferences
 * @param defaultValue Boolean
 * @param key Function1<KProperty<*>, String>
 * @return ReadWriteProperty<Any, Boolean>
 */
fun SharedPreferences.boolean(
        defaultValue: Boolean = false,
        key: (KProperty<*>) -> String = KProperty<*>::name
): ReadWriteProperty<Any, Boolean> = object : ReadWriteProperty<Any, Boolean> {
    override fun getValue(thisRef: Any, property: KProperty<*>) = getBoolean(key(property), defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) = putBoolean(key(property), value)
}

/**
 * fun SharedPreferences.int(def: Int = 0, key: String? = null) =
 * delegate(def, key, SharedPreferences::getInt, Editor::putInt)
 * @receiver SharedPreferences
 * @param defaultValue T
 * @param key String?
 * @param getter [@kotlin.ExtensionFunctionType] Function3<SharedPreferences, String, T, T>
 * @param setter [@kotlin.ExtensionFunctionType] Function3<Editor, String, T, Editor>
 * @return ReadWriteProperty<Any, T>
 */
private inline fun <T> SharedPreferences.delegate(
        defaultValue: T,
        key: String?,
        crossinline getter: SharedPreferences.(String, T) -> T,
        crossinline setter: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
): ReadWriteProperty<Any, T> {
    return object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>) =
                getter(key ?: property.name, defaultValue)

        override fun setValue(thisRef: Any, property: KProperty<*>,
                              value: T) =
                edit().setter(key ?: property.name, value).apply()
    }
}


