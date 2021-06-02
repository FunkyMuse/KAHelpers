package com.crazylegend.kotlinextensions.print

import android.content.Context
import android.graphics.BitmapFactory
import androidx.print.PrintHelper
import com.crazylegend.kotlinextensions.log.debug


/**
 * Created by crazy on 3/17/20 to long live and prosper !
 */


fun <T> List<T>.printList() {
    this.forEachIndexed { index, t ->
        "$index -> ".debug(t.toString())
    }
}

fun <T> Array<T>.printList() {
    this.forEachIndexed { index, t ->
        "$index -> ".debug(t.toString())
    }
}

fun <K, V> Map<K, V>.printMap() {
    this.forEach {
        "${it.key} -> ${it.value}".debug(this.toString())
    }
}

fun <K, V> HashMap<K, V>.printMap() {
    this.forEach {
        "${it.key} -> ${it.value}".debug(this.toString())
    }
}

fun Context.doPhotoPrint(drawable: Int, jobName: String) {
    PrintHelper(this).apply {
        scaleMode = PrintHelper.SCALE_MODE_FIT
    }.also { printHelper ->
        val bitmap = BitmapFactory.decodeResource(resources, drawable)
        printHelper.printBitmap(jobName, bitmap)
    }
}