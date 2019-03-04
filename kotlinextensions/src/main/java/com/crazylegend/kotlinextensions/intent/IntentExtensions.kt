package com.crazylegend.kotlinextensions.intent

import android.content.Intent


/**
 * Created by crazy on 2/24/19 to long live and prosper !
 */

fun Intent.clearTask() = apply {
    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
}

fun Intent.clearTop() = apply {
    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
}

fun Intent.clearWhenTaskReset() = apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
}

fun Intent.excludeFromRecents() = apply {
    addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
}

fun Intent.multipleTask() = apply {
    addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
}

fun Intent.newTask() = apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

fun Intent.noAnimation() = apply {
    addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
}

fun Intent.noHistory() = apply {
    addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
}

fun Intent.singleTop() = apply {
    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
}

fun Intent.reorderToFront() = apply {
    addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
}