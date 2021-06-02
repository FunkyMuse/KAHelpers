package com.crazylegend.kotlinextensions.memory


/**
 * Created by crazy on 2/24/19 to long live and prosper !
 */

fun getMemoryUsage(transform: (Long, Long, Long, Long, Int) -> String = { _, _, max, usage, percent -> "$usage / $max MB in use ($percent%)" }): Pair<Int, String> {
    val total = Runtime.getRuntime().totalMemory()
    val free = Runtime.getRuntime().freeMemory()
    val max = Runtime.getRuntime().maxMemory()
    val usage = total - free
    val percent = ((usage.toDouble() / max) * 100).toInt()
    return percent to transform(total, free, max, usage, percent)
}

val fileSizeUnits = arrayOf("bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")
fun Long.toFileSizeString(): String {
    var bytesToCalculate = this
    val sizeToReturn: String
    var index = 0
    while (index < fileSizeUnits.size) {
        if (bytesToCalculate < 1024) {
            break
        }
        bytesToCalculate /= 1024
        index++
    }
    sizeToReturn = bytesToCalculate.toString() + " " + fileSizeUnits[index]
    return sizeToReturn
}

fun Long.toFileSize(): Long {
    var bytesToCalculate = this
    var index = 0
    while (index < fileSizeUnits.size) {
        if (bytesToCalculate < 1024) {
            break
        }
        bytesToCalculate /= 1024
        index++
    }
    return bytesToCalculate
}

