package com.crazylegend.file

import java.io.File
import java.net.URLConnection

/**
 * Created by Hristijan, date 5/26/21
 */

fun File.mediaType(): String {
    return URLConnection.getFileNameMap().getContentTypeFor(name) ?: when (extension.lowercase()) {
        "json" -> "application/json"
        "js" -> "application/javascript"
        "apk" -> "application/vnd.android.package-archive"
        "md" -> "text/x-markdown"
        "webp" -> "image/webp"
        else -> "application/octet-stream"
    }
}
