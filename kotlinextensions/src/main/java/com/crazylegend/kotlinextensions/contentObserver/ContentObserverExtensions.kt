package com.crazylegend.kotlinextensions.contentObserver

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper


/**
 * Created by crazy on 7/31/20 to long live and prosper !
 */


inline fun ContentResolver.registerObserver(
        uri: Uri,
        crossinline observer: (change: Boolean) -> Unit
): ContentObserver {
    val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            observer(selfChange)
        }
    }
    registerContentObserver(uri, true, contentObserver)
    return contentObserver
}