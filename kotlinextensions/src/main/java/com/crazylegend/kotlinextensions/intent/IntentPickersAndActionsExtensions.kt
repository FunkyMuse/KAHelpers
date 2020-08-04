package com.crazylegend.kotlinextensions.intent

import android.Manifest.permission.SET_ALARM
import android.app.Activity
import android.app.SearchManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.*
import android.provider.Settings.Panel.ACTION_INTERNET_CONNECTIVITY
import android.widget.TimePicker
import androidx.annotation.RequiresPermission
import androidx.print.PrintHelper
import com.crazylegend.kotlinextensions.version.doIfSdk


/**
 * Created by hristijan on 3/28/19 to long live and prosper !
 */


@RequiresPermission(allOf = [SET_ALARM])
inline fun Activity.createAlarm(message: String, hour: Int, minutes: Int, onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
        putExtra(AlarmClock.EXTRA_MESSAGE, message)
        putExtra(AlarmClock.EXTRA_HOUR, hour)
        putExtra(AlarmClock.EXTRA_MINUTES, minutes)
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        onCantHandleAction()
    }
}

@RequiresPermission(allOf = [SET_ALARM])
inline fun Activity.startTimer(message: String, seconds: Int, onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(AlarmClock.ACTION_SET_TIMER).apply {
        putExtra(AlarmClock.EXTRA_MESSAGE, message)
        putExtra(AlarmClock.EXTRA_LENGTH, seconds)
        putExtra(AlarmClock.EXTRA_SKIP_UI, true)
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        onCantHandleAction()
    }
}


inline fun Activity.addEvent(title: String, location: String, begin: Long, end: Long, onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, title)
        putExtra(CalendarContract.Events.EVENT_LOCATION, location)
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end)
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        onCantHandleAction()
    }
}


inline fun Activity.selectContact(REQUEST_SELECT_CONTACT: Int, onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_PICK).apply {
        type = ContactsContract.Contacts.CONTENT_TYPE
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivityForResult(intent, REQUEST_SELECT_CONTACT)
    } else {
        onCantHandleAction()
    }
}

inline fun Activity.editContact(contactUri: Uri, email: String, onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_EDIT).apply {
        data = contactUri
        putExtra(ContactsContract.Intents.Insert.EMAIL, email)
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        onCantHandleAction()
    }
}

inline fun Activity.insertContact(name: String, email: String, onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_INSERT).apply {
        type = ContactsContract.Contacts.CONTENT_TYPE
        putExtra(ContactsContract.Intents.Insert.NAME, name)
        putExtra(ContactsContract.Intents.Insert.EMAIL, email)
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        onCantHandleAction()
    }
}

inline fun Activity.showMap(geoLocation: Uri, onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = geoLocation
    }
    if (intent.resolveActivity(packageManager) != null) {

        startActivity(intent)
    } else {
        onCantHandleAction()
    }
}

inline fun Activity.playMedia(file: Uri, onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = file
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        onCantHandleAction()
    }
}

inline fun Context.playSearchArtist(artist: String, onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH).apply {
        putExtra(MediaStore.EXTRA_MEDIA_FOCUS, MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE)
        putExtra(MediaStore.EXTRA_MEDIA_ARTIST, artist)
        putExtra(SearchManager.QUERY, artist)
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        onCantHandleAction()
    }
}


inline fun Context.searchWeb(query: String, onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
        putExtra(SearchManager.QUERY, query)
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        onCantHandleAction()
    }
}


/**
 * Available types
 *
ACTION_SETTINGS
ACTION_WIRELESS_SETTINGS
ACTION_AIRPLANE_MODE_SETTINGS
ACTION_WIFI_SETTINGS
ACTION_APN_SETTINGS
ACTION_BLUETOOTH_SETTINGS
ACTION_DATE_SETTINGS
ACTION_LOCALE_SETTINGS
ACTION_INPUT_METHOD_SETTINGS
ACTION_DISPLAY_SETTINGS
ACTION_SECURITY_SETTINGS
ACTION_LOCATION_SOURCE_SETTINGS
ACTION_INTERNAL_STORAGE_SETTINGS
ACTION_MEMORY_CARD_SETTINGS
 */
inline fun Activity.openSettingsCategory(category: String, onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(category)
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        onCantHandleAction()
    }
}

inline fun Activity.openSettingsCategory(category: String, resultCode: Int, onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(category)
    if (intent.resolveActivity(packageManager) != null) {
        startActivityForResult(intent, resultCode)
    } else {
        onCantHandleAction()
    }
}



fun Activity.composeMmsMessage(message: String, attachment: Uri, onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        data = Uri.parse("smsto:")  // This ensures only SMS apps respond
        putExtra("sms_body", message)
        putExtra(Intent.EXTRA_STREAM, attachment)
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        onCantHandleAction()
    }
}

fun Activity.doPhotoPrint(drawable: Int, jobName: String) {
    PrintHelper(this).apply {
        scaleMode = PrintHelper.SCALE_MODE_FIT
    }.also { printHelper ->
        val bitmap = BitmapFactory.decodeResource(resources, drawable)
        printHelper.printBitmap(jobName, bitmap)
    }
}


/**
 * Available types
 *
ACTION_SETTINGS
ACTION_WIRELESS_SETTINGS
ACTION_AIRPLANE_MODE_SETTINGS
ACTION_WIFI_SETTINGS
ACTION_APN_SETTINGS
ACTION_BLUETOOTH_SETTINGS
ACTION_DATE_SETTINGS
ACTION_LOCALE_SETTINGS
ACTION_INPUT_METHOD_SETTINGS
ACTION_DISPLAY_SETTINGS
ACTION_SECURITY_SETTINGS
ACTION_LOCATION_SOURCE_SETTINGS
ACTION_INTERNAL_STORAGE_SETTINGS
ACTION_MEMORY_CARD_SETTINGS
 */
inline fun Context.openSettingsCategory(category: String, onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(category)
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        onCantHandleAction()
    }
}

inline fun Context.openWifiSettings(crossinline onCantHandleAction: () -> Unit = {}) {
    doIfSdk(Build.VERSION_CODES.Q, {
        val intent = Intent(ACTION_INTERNET_CONNECTIVITY)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            onCantHandleAction()
        }
    }) {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            onCantHandleAction()
        }
    }
}


inline fun Context.composeMmsMessage(message: String, attachment: Uri, onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        data = Uri.parse("smsto:")  // This ensures only SMS apps respond
        putExtra("sms_body", message)
        putExtra(Intent.EXTRA_STREAM, attachment)
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        onCantHandleAction()
    }
}

inline fun Context.openWebPage(url: String, onCantHandleAction: () -> Unit = {}) {
    val webpage: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        onCantHandleAction()
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

fun Context.composeMessage(phone: String, message: String = "", onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        putExtra("sms_body", message)
        data = Uri.parse("sms:$phone")
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        onCantHandleAction()
    }
}

fun Context.dialPhoneNumber(phoneNumber: String, onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        onCantHandleAction()
    }
}

fun Context.showTimePicker(hourOfDay: Int, minute: Int, is24Hour: Boolean, action: (view: TimePicker, hourOfDay: Int, minute: Int) -> Unit) =
        TimePickerDialog(this, TimePickerDialog.OnTimeSetListener(action), hourOfDay, minute, is24Hour).show()

inline fun Context.applicationDetailsIntent(onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:$packageName")
    if (intent.canBeHandled(this))
        startActivity(intent)
    else
        onCantHandleAction()
}

@Suppress("DEPRECATION")
inline var TimePicker.hourCompat: Int
    set(value) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) hour = value else currentHour = value
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) hour else currentHour

@Suppress("DEPRECATION")
inline var TimePicker.minuteCompat: Int
    set(value) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) minute = value else currentMinute = value
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) minute else currentMinute
