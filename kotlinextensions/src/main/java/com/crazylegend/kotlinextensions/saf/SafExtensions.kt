package com.crazylegend.kotlinextensions.saf

import android.Manifest.permission.ACCESS_MEDIA_LOCATION
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.RequiresPermission
import androidx.exifinterface.media.ExifInterface


/**
 * Created by crazy on 11/11/19 to long live and prosper !
 */

@RequiresPermission(ACCESS_MEDIA_LOCATION)
fun Context.getLocationFromImages(uri: Uri, latNLongCallBack: (latNLong: DoubleArray) -> Unit = { _ -> }) {
    val photoUri = MediaStore.setRequireOriginal(uri)
    contentResolver.openInputStream(photoUri).use { stream ->
        stream?.let {
            ExifInterface(it).run {
                // If lat/long is null, fall back to the coordinates (0, 0).
                val latLong = this.latLong ?: doubleArrayOf(0.0, 0.0)
                latNLongCallBack(latLong)
            }
        }
    }
}

