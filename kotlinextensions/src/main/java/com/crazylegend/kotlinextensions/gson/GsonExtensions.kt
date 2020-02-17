package com.crazylegend.kotlinextensions.gson

import com.crazylegend.kotlinextensions.dateAndTime.TimestampConvert
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import org.json.JSONObject


/**
 * Created by hristijan on 2/22/19 to long live and prosper !
 */
val gson: Gson by lazy {
    Gson()
}

val gsonWithDate by lazy {
    GsonBuilder()
            .disableHtmlEscaping()
            .setDateFormat(TimestampConvert.iso8601Format())
            .create()
}


inline fun <reified T : Any> Gson.fromJson(json: String): T = this.fromJson(json, T::class.java)

val prettyJsonString: (String) -> String = { jsonStr ->
    val jsonElement = JsonParser().parse(jsonStr)
    GsonBuilder().setPrettyPrinting().create().toJson(jsonElement)
}

val prettyJsonObject: (JSONObject) -> String = { json ->
    val jso = JsonParser().parse(json.toString()).asJsonObject
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

inline fun <reified T> Gson.fromJsonTypeToken(json: String): T = this.fromJson<T>(json, object : TypeToken<T>() {}.type)