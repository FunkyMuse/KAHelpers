package com.crazylegend.kotlinextensions.dsls

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */

fun broadcastReceiver(block: (Context, Intent) -> Unit) = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        block(context, intent)
    }
}