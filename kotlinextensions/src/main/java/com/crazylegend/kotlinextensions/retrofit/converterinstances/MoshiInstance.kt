package com.crazylegend.kotlinextensions.retrofit.converterinstances

import com.squareup.moshi.Moshi


/**
 * Created by hristijan on 4/22/19 to long live and prosper !
 */
object MoshiInstance {
    private var moshi: Moshi? = null

    fun getMoshiInstance(): Moshi? {
        if (moshi == null){
            moshi = Moshi.Builder().build()
        }
        return moshi
    }
}