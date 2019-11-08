package com.crazylegend.kotlinextensions.json

import org.json.JSONArray
import org.json.JSONObject
/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */


fun JSONObject.getStringOrNull(name: String): String? {
    return if (has(name)) getString(name) else null
}

fun JSONObject.getIntOrNull(name: String): Int? {
    return if (has(name)) getInt(name) else null
}

fun JSONObject.getBooleanOrNull(name: String): Boolean? {
    return if (has(name)) getBoolean(name) else null
}

fun JSONObject.getDoubleOrNull(name: String): Double? {
    return if (has(name)) getDouble(name) else null
}

fun JSONObject.getLongOrNull(name: String): Long? {
    return if (has(name)) getLong(name) else null
}

fun JSONObject.getJSONObjectOrNull(name: String): JSONObject? {
    return if (has(name)) getJSONObject(name) else null
}

fun JSONObject.getJSONArrayOrNull(name: String): JSONArray? {
    return if (has(name)) getJSONArray(name) else null
}
