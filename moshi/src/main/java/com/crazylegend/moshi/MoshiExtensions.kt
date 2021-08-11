package com.crazylegend.moshi

import android.content.SharedPreferences
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * Created by crazy on 2/25/20 to long live and prosper !
 */


inline fun <reified T : Any> T.toJson(
    factory: JsonAdapter.Factory,
    customBuilder: Moshi.Builder = Moshi.Builder()
): String = customBuilder
    .add(factory).build()
    .adapter(T::class.java).toJson(this)


inline fun <reified T : Any> String.fromJson(
    factory: JsonAdapter.Factory,
    customBuilder: Moshi.Builder = Moshi.Builder()
): T? = customBuilder
    .add(factory).build()
    .adapter(T::class.java).fromJson(this)


inline fun <reified T : Any> String.toJsonObjectList(
    factory: JsonAdapter.Factory,
    customBuilder: Moshi.Builder = Moshi.Builder()
): List<T>? {
    return Types.newParameterizedType(List::class.java, T::class.java).let { type ->
        customBuilder
            .add(factory).build()
            .adapter<List<T>>(type).fromJson(this)
    }
}


inline fun <reified T : Any> String.toJsonObjectArrayList(
    factory: JsonAdapter.Factory,
    customBuilder: Moshi.Builder = Moshi.Builder()
): ArrayList<T>? {
    return Types.newParameterizedType(ArrayList::class.java, T::class.java).let { type ->
        customBuilder
            .add(factory).build()
            .adapter<ArrayList<T>>(type).fromJson(this)
    }
}


inline fun <reified T : Any> String.toJsonObjectMutableList(
    factory: JsonAdapter.Factory,
    customBuilder: Moshi.Builder = Moshi.Builder()
): MutableList<T>? {
    return Types.newParameterizedType(MutableList::class.java, T::class.java).let { type ->
        customBuilder
            .add(factory).build()
            .adapter<MutableList<T>>(type).fromJson(this)
    }
}


fun SharedPreferences.putObject(factory: JsonAdapter.Factory, key: String, obj: Any?) {
    putString(key, obj?.toJson(factory) ?: "")
}

inline fun <reified T : Any> SharedPreferences.getObject(
    factory: JsonAdapter.Factory,
    key: String
): T? {
    val string = getString(key, null)
    if (string == null || string.isEmpty()) return null
    return getString(key, null)?.fromJson(factory)
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
 * @param factory JsonAdapter.Factory usually KotlinJsonAdapterFactory or your own choice
 * @receiver SharedPreferences
 * @param key Function1<KProperty<*>, String>
 * @return ReadWriteProperty<Any, T?>
 */
inline fun <reified T> SharedPreferences.obj(
    factory: JsonAdapter.Factory,
    crossinline key: (KProperty<*>) -> String = KProperty<*>::name
): ReadWriteProperty<Any, T?> = object : ReadWriteProperty<Any, T?> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T? =
        getObject(factory, key(property))

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) =
        putObject(factory, key(property), value)
}
