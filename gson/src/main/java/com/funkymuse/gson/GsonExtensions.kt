package com.funkymuse.gson

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.io.File
import java.nio.charset.Charset
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty



val gson: Gson by lazy {
    Gson()
}
val iso8601Format = "yyyy-MM-dd'T'HH:mm:ssZZZZZ" // works on all android versions but not in unit tests

val gsonWithDate by lazy {
    GsonBuilder()
            .disableHtmlEscaping()
            .setDateFormat(iso8601Format)
            .create()
}


inline fun <reified T : Any> Gson.fromJson(json: String): T = this.fromJson(json, T::class.java)

val prettyJsonString: (String) -> String = { jsonStr ->
    val jsonElement = JsonParser.parseString(jsonStr)
    GsonBuilder().setPrettyPrinting().create().toJson(jsonElement)
}

val prettyJsonObject: (JSONObject) -> String = { json ->
    val jso = JsonParser.parseString(json.toString()).asJsonObject
    GsonBuilder().setPrettyPrinting().create().toJson(jso)
}

val gsonWithoutHTMLEscaping: Gson by lazy {
    GsonBuilder()
            .disableHtmlEscaping()
            .create()
}

val gsonPrettyPrinting: Gson by lazy {
    GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create()
}

fun Any.toJson(): String = gson.toJson(this)

fun Any.toJsonPrettyPrinting(): String = gsonPrettyPrinting.toJson(this)

inline fun <reified T> Gson.fromJsonTypeToken(json: String): T = this.fromJson(json, object : TypeToken<T>() {}.type)

inline fun <reified T> File.asJsonFromText(charset: Charset = Charsets.UTF_8) {
    Gson().fromJsonTypeToken<T>(readText(charset))
}

fun SharedPreferences.putObject(key: String, obj: Any?) {
    putString(key, obj?.toJson() ?: "")
}

inline fun <reified T> SharedPreferences.getObject(key: String): T? {
    val string = getString(key, null)
    if (string == null || string.isEmpty()) return null
    return gson.fromJson(getString(key, null), T::class.java)
}

fun SharedPreferences.putString(key: String, value: String) {
    edit { putString(key, value) }
}

private inline fun SharedPreferences.edit(
        commit: Boolean = false,
        action: SharedPreferences.Editor.() -> Unit
) {
    val editor = edit()
    action(editor)
    if (commit) {
        editor.commit()
    } else {
        editor.apply()
    }
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
    override fun getValue(thisRef: Any, property: KProperty<*>): T? = getObject(key(property))

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) = putObject(key(property), value)
}