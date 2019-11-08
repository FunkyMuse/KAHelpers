package com.crazylegend.kotlinextensions.environment

import android.os.Environment
import android.os.Environment.MEDIA_MOUNTED
import android.os.Environment.MEDIA_MOUNTED_READ_ONLY


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */


inline val Environment.isExternalStorageWritable
    get() = Environment.getExternalStorageState() == MEDIA_MOUNTED

inline val Environment.isExternalStorageReadable
    get() = Environment.getExternalStorageState() == MEDIA_MOUNTED_READ_ONLY || isExternalStorageWritable

inline val dataDir get() = Environment.getDataDirectory()

inline val downloadCacheDir get() = Environment.getDownloadCacheDirectory()

inline val rootDir get() = Environment.getRootDirectory()
