package com.crazylegend.kotlinextensions.delegates

import android.content.SharedPreferences
import com.crazylegend.kotlinextensions.sharedprefs.getObject
import com.crazylegend.kotlinextensions.sharedprefs.putObject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * Created by crazy on 11/13/19 to long live and prosper !
 */


fun SharedPreferences.string(defaultValue: String = "", key: (KProperty<*>) -> String = KProperty<*>::name): ReadWriteProperty<Any, String> = object : ReadWriteProperty<Any, String> {
    override fun getValue(thisRef: Any, property: KProperty<*>) = getString(key(property), defaultValue).toString()

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) = edit().putString(key(property), value).apply()
}


fun SharedPreferences.stringNullable(
        defaultValue: String? = null,
        key: (KProperty<*>) -> String = KProperty<*>::name
): ReadWriteProperty<Any, String?> = object : ReadWriteProperty<Any, String?> {
    override fun getValue(thisRef: Any, property: KProperty<*>) = getString(key(property), defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) = edit().putString(key(property), value).apply()
}

fun SharedPreferences.int(
        defaultValue: Int = 0,
        key: (KProperty<*>) -> String = KProperty<*>::name
): ReadWriteProperty<Any, Int> = object : ReadWriteProperty<Any, Int> {
    override fun getValue(thisRef: Any, property: KProperty<*>) = getInt(key(property), defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) = edit().putInt(key(property), value).apply()
}


fun SharedPreferences.float(
        defaultValue: Float = 0f,
        key: (KProperty<*>) -> String = KProperty<*>::name
): ReadWriteProperty<Any, Float> = object : ReadWriteProperty<Any, Float> {
    override fun getValue(thisRef: Any, property: KProperty<*>) = getFloat(key(property), defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) = edit().putFloat(key(property), value).apply()
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

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) = edit().putBoolean(key(property), value).apply()
}

/**
 * Use sharedPreferences.obj( key = { "MY_KEY" } )
 * @receiver SharedPreferences
 * @param key Function1<KProperty<*>, String>
 * @return ReadWriteProperty<Any, T?>
 */
inline fun <reified T> SharedPreferences.obj(
        crossinline key: (KProperty<*>) -> String = KProperty<*>::name
): ReadWriteProperty<Any, T?> = object : ReadWriteProperty<Any, T?> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T?  = getObject(key(property))

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) = putObject(key(property), value)
}




