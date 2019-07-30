package com.crazylegend.kotlinextensions.http

import java.io.File
import java.net.URLConnection.getFileNameMap


/**
 * Created by hristijan on 7/30/19 to long live and prosper !
 */

// parse some new media type.
fun File.mediaType(): String {
    return getFileNameMap().getContentTypeFor(name) ?: when (extension.toLowerCase()) {
        "json" -> "application/json"
        "js" -> "application/javascript"
        "apk" -> "application/vnd.android.package-archive"
        "md" -> "text/x-markdown"
        "webp" -> "image/webp"
        else -> "application/octet-stream"
    }
}
