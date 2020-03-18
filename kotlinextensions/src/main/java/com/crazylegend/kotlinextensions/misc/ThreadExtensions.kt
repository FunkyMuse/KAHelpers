package com.crazylegend.kotlinextensions.misc




/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */


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

fun startNewThread( runnable: Runnable) {
    Thread(runnable).start()
}