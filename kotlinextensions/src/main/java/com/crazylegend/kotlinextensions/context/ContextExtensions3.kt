package com.crazylegend.kotlinextensions.context

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import java.io.File


/**
 * Created by hristijan on 2/27/19 to long live and prosper !
 */

fun Context.getVideoDuration(videoFile: File): Long? {
    val retriever = MediaMetadataRetriever()
    var videoDuration = Long.MAX_VALUE
    try {
        retriever.setDataSource(this, Uri.fromFile(videoFile))
        val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        videoDuration = java.lang.Long.parseLong(time)
        retriever.release()
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    } catch (e: SecurityException) {
        e.printStackTrace()
    }
    return videoDuration
}

