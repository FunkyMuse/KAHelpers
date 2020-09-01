package com.crazylegend.kotlinextensions.intent

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri


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

fun Intent.restart() = apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
}

fun Intent.singleTop() = apply {
    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
}

fun Intent.reorderToFront() = apply {
    addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
}

fun Intent.canBeHandled(context: Context) = this.resolveActivity(context.packageManager) != null

fun Context.intentCanBeHandled(intent: Intent) = intent.resolveActivity(packageManager) != null


fun Context.getActivityPendingIntent(requestCode: Int = 0, intent: Intent, flags: Int = PendingIntent.FLAG_ONE_SHOT): PendingIntent =
        PendingIntent.getActivity(this, requestCode, intent, flags)

fun Context.getBroadcastPendingIntent(requestCode: Int = 0, intent: Intent, flags: Int = PendingIntent.FLAG_ONE_SHOT): PendingIntent =
        PendingIntent.getBroadcast(this, requestCode, intent, flags)

fun Context.getProductionApplicationId(): String {
    val applicationId = packageName
    return when {
        applicationId.contains(".stage") -> applicationId.dropLast(6)
        applicationId.contains(".debug") -> applicationId.dropLast(6)
        else -> applicationId
    }
}


fun Context?.openGoogleMaps(query: String, placeId: String) {
    val queryEncoded = Uri.encode(query)
    val gmmIntentUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$queryEncoded&query_place_id=$placeId")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    this?.apply {
        if (mapIntent.canBeHandled(this)) {
            startActivity(mapIntent)
        }
    }

}

fun Activity.pickImage(PICK_IMAGES_CODE: Int, allowMultiple: Boolean = false, title: String = "Pick images",
                       onCantHandleIntent: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            .addCategory(Intent.CATEGORY_OPENABLE)
            .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple)
            .setType("image/*")
    if (intentCanBeHandled(intent))
        startActivityForResult(Intent.createChooser(intent, title), PICK_IMAGES_CODE)
    else
        onCantHandleIntent()
}

fun Activity.openFile(mimeType: String, requestCode: Int, title: String = "Select file via",
                      onCantHandleIntent: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            .addCategory(Intent.CATEGORY_OPENABLE)
            .setType(mimeType)

    if (intentCanBeHandled(intent))
        startActivityForResult(Intent.createChooser(intent, title), requestCode)
    onCantHandleIntent()
}