package com.crazylegend.kotlinextensions.retrofit.converterinstances

import com.google.gson.Gson


/**
 * Created by hristijan on 4/22/19 to long live and prosper !
 */
object GsonInstance {
    private var gson: Gson? = null
    fun getGsonInstance(): Gson? {
        if (gson == null) {
            gson = Gson()
        }
        return gson
    }
}