package com.crazylegend.kotlinextensions.gson

import com.google.gson.Gson


/**
 * Created by hristijan on 2/22/19 to long live and prosper !
 */

inline fun <reified T: Any> Gson.fromJson(json: String): T = this.fromJson(json, T::class.java)

