package com.crazylegend.kotlinextensions.file

import android.content.Context
import android.net.Uri


/**
 * Created by hristijan on 3/5/19 to long live and prosper !
 */


/**
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
fun Uri.isExternalStorageDocument(): Boolean {
    return "com.android.externalstorage.documents" == authority
}

/**
 * @return Whether the Uri authority is DownloadsProvider.
 */
fun Uri.isDownloadsDocument(): Boolean {
    return "com.android.providers.downloads.documents" == authority
}

/**
 * @return Whether the Uri authority is MediaProvider.
 */
fun Uri.isMediaDocument(): Boolean {
    return "com.android.providers.media.documents" == authority
}

/**
 * Opens and reads the entirety of the Uri into a ByteArray.
 */
fun Uri.toByteArray(context: Context): ByteArray {
    return context.contentResolver.openInputStream(this).run {
        val array = this.readBytes()
        close()
        array
    }
}