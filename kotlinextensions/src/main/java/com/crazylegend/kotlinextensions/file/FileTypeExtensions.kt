package com.crazylegend.kotlinextensions.file

import android.webkit.MimeTypeMap
import androidx.documentfile.provider.DocumentFile
import com.crazylegend.kotlinextensions.containsInArray
import com.crazylegend.kotlinextensions.isEmptyString
import java.io.File


/**
 * Created by hristijan on 3/7/19 to long live and prosper !
 */


/**
 * Determines if current [DocumentFile] is an image
 * Courtesy: Code of Amaze file manager
 */
inline val DocumentFile.isImage: Boolean
    get() {
        val type = this.type
        return type != null && !type.isEmptyString() &&
                (type.startsWith("image") || type.containsInArray("application/vnd" +
                        ".oasis.opendocument.graphics", "application/vnd.oasis.opendocument.graphics-template", "application/vnd.oasis.opendocument.image", "application/vnd.stardivision.draw", "application/vnd.sun.xml.draw", "application/vnd.sun.xml.draw.template"))
    }

/**
 * Determines if current [DocumentFile] is an Audio
 * Courtesy: Code of Amaze file manager
 */
inline val DocumentFile.isAudio: Boolean
    get() {
        val type = this.type
        return type != null && !type.isEmptyString() && (type.startsWith("audio") ||
                type.containsInArray("application/ogg", "application/x-flac"))
    }
/**
 * Determines if current [DocumentFile] is a Video
 * Courtesy: Code of Amaze file manager
 */
inline val DocumentFile.isVideo: Boolean
    get() {
        val type = this.type
        return type != null && !type.isEmptyString() && (type.startsWith("video") ||
                type.containsInArray("application/x-quicktimeplayer", "application/x-shockwave-flash"))
    }

val DocumentFile.hasPreview: Boolean
    get() = isImage || isVideo

fun File.isAudioFile(): Boolean {
    return fileIsMimeType("audio/*", MimeTypeMap.getSingleton())  ||
            fileIsMimeType("application/ogg", MimeTypeMap.getSingleton())
}

 fun File.fileIsMimeType(mimeType: String?, mimeTypeMap: MimeTypeMap): Boolean {
    if (mimeType == null || mimeType == "*/*") {
        return true
    } else {
        // get the file mime type
        val filename = this.toURI().toString()
        val dotPos = filename.lastIndexOf('.')
        if (dotPos == -1) {
            return false
        }
        val fileExtension = filename.substring(dotPos + 1).toLowerCase()
        val fileType = mimeTypeMap.getMimeTypeFromExtension(fileExtension) ?: return false
        // check the 'type/subtype' pattern
        if (fileType == mimeType) {
            return true
        }
        // check the 'type/*' pattern
        val mimeTypeDelimiter = mimeType.lastIndexOf('/')
        if (mimeTypeDelimiter == -1) {
            return false
        }
        val mimeTypeMainType = mimeType.substring(0, mimeTypeDelimiter)
        val mimeTypeSubtype = mimeType.substring(mimeTypeDelimiter + 1)
        if (mimeTypeSubtype != "*") {
            return false
        }
        val fileTypeDelimiter = fileType.lastIndexOf('/')
        if (fileTypeDelimiter == -1) {
            return false
        }
        val fileTypeMainType = fileType.substring(0, fileTypeDelimiter)
        if (fileTypeMainType == mimeTypeMainType) {
            return true
        }
    }
    return false
}