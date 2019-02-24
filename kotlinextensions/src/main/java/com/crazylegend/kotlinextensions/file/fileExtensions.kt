package com.crazylegend.kotlinextensions.file

import java.io.File


/**
 * Created by crazy on 2/24/19 to long live and prosper !
 */


/**
 * Deletes all files
 */
fun File.deleteAll() {
    if (isFile && exists()) {
        delete()
        return
    }
    if (isDirectory) {
        val files = listFiles()
        if (files == null || files.isEmpty()) {
            delete()
            return
        }
        files.forEach { it.deleteAll() }
        delete()
    }
}