package com.crazylegend.kotlinextensions.social

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.crazylegend.kotlinextensions.intent.intentCanBeHandled


/**
 * Created by crazy on 3/17/20 to long live and prosper !
 */

/**
 * @param pageId Facebook page id
 * @return true if Facebook app found
 */
fun Context.openFacebookPage(pageId: String, onCantHandleIntent: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/${pageId}"))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    if (intentCanBeHandled(intent)) {
        startActivity(intent)
    } else {
        onCantHandleIntent()
    }
}

/**
 * @param userId Instagram user id
 * @return true if Instagram app found
 */
fun Context.openInstagram(userId: String, onCantHandleIntent: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/${userId}"))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    if (intentCanBeHandled(intent)) {
        startActivity(intent)
    } else {
        onCantHandleIntent()
    }
}

/**
 * @param channelId Youtube channel id
 * @return true if Youtube app found
 */
fun Context.openYoutube(channelId: String, onCantHandleIntent: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/${channelId}"))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    if (intentCanBeHandled(intent)) {
        startActivity(intent)
    } else {
        onCantHandleIntent()
    }
}

/**
 * @param userId Twitter user id
 * @return true if Twitter app found
 */
fun Context.openTwitter(userId: String, onCantHandleIntent: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=${userId}"))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    if (intentCanBeHandled(intent)) {
        startActivity(intent)
    } else {
        onCantHandleIntent()
    }
}