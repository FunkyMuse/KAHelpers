package com.funkymuse.kotlinextensions.dsls

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent




inline fun broadcastReceiver(crossinline block: (Context, Intent) -> Unit) = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        block(context, intent)
    }
}