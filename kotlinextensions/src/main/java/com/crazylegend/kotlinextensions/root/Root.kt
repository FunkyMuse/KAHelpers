package com.crazylegend.kotlinextensions.root

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader


/**
 * Created by crazy on 2/6/19 to long live and prosper !
 */



fun isRooted(): Boolean {

    // get from build info
    val buildTags = android.os.Build.TAGS
    if (buildTags != null && buildTags.contains("test-keys")) {
        return true
    }

    // check if /system/app/Superuser.apk is present
    try {
        val file = File("/system/app/Superuser.apk")
        if (file.exists()) {
            return true
        }
    } catch (e1: Exception) {
        // ignore
    }

    // try executing commands
    return (canExecuteCommand("/system/xbin/which su")
            || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su"))
}

// executes a command on the system
private fun canExecuteCommand(command: String): Boolean {
    return try {
        Runtime.getRuntime().exec(command)
        true
    } catch (e: Exception) {
        false
    }
}

fun isRootAvailable(): Boolean {
    for (pathDir in System.getenv("PATH")!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
        if (File(pathDir, "su").exists()) {
            return true
        }
    }
    return false
}

fun isRootGiven(): Boolean {
    if (isRootAvailable()) {
        var process: Process? = null
        try {
            process = Runtime.getRuntime().exec(arrayOf("su", "-c", "id"))
            val bufferedReader = BufferedReader(InputStreamReader(process!!.inputStream))
            val output = bufferedReader.readLine()
            if (output != null && output.toLowerCase().contains("uid=0"))
                return true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            process?.destroy()
        }
    }

    return false
}