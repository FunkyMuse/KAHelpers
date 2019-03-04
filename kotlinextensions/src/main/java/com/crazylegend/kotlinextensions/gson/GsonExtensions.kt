package com.crazylegend.kotlinextensions.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import org.json.JSONObject


/**
 * Created by hristijan on 2/22/19 to long live and prosper !
 */

inline fun <reified T: Any> Gson.fromJson(json: String): T = this.fromJson(json, T::class.java)

val prettyJsonString: (String) -> String = { jsonStr ->
    val jsonElement = JsonParser().parse(jsonStr)
    GsonBuilder().setPrettyPrinting().create().toJson(jsonElement)
}

val prettyJsonObject: (JSONObject) -> String = { json ->
    val jso = JsonParser().parse(json.toString()).asJsonObject
    GsonBuilder().setPrettyPrinting().create().toJson(jso)
}