package com.funkymuse.kotlinextensions.misc





fun setThreadPriority(priority: Int) {
    android.os.Process.setThreadPriority(priority)
}

fun safeSleep(millisecond: Int) {
    try {
        Thread.sleep(millisecond.toLong())
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }

}

fun startNewThread(runnable: Runnable) {
    Thread(runnable).start()
}