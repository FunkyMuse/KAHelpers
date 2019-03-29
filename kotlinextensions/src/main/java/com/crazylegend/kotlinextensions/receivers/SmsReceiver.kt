package com.crazylegend.kotlinextensions.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */

/**
 *      <receiver
 *              android:name=".services.sms.IncomingSmsReceiver"
 *              android:enabled="true">
 *              <intent-filter>
 *                      <action android:name="android.provider.Telephony.SMS_RECEIVED" />
 *              </intent-filter>
 *      </receiver>
 *
 */

abstract class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.extras == null)
            return

        // @see https://developer.android.com/reference/android/telephony/SmsMessage.html
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

        onSmsMessages(messages)
    }

    abstract fun onSmsMessages(messages: Array<SmsMessage>)
}