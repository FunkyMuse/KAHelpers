package com.crazylegend.kotlinextensions.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import java.util.*


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */

/**
 * <receiver android:name=".services.calls.TrackingPhoneCallReceiver" android:enabled="true">
 * <intent-filter>
 * <action android:name="android.intent.action.NEW_OUTGOING_CALL"></action>
 * <action android:name="android.intent.action.PHONE_STATE"></action>
</intent-filter> *
</receiver> *
 *
 */
abstract class PhoneCallReceiver : BroadcastReceiver() {

    protected var outgoingSavedNumber: String? = null

    private lateinit var savedContext: Context

    override fun onReceive(context: Context, intent: Intent) {
        savedContext = context
        if (listener == null) {
            listener = PhoneCallStartEndDetector()
        }

        //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
        if ("android.intent.action.NEW_OUTGOING_CALL" == intent.action) {
            listener!!.setOutgoingNumber(intent.extras!!.getString("android.intent.extra.PHONE_NUMBER"))
            return
        }

        //The other intent tells us the phone state changed.  Here we set a listener to deal with it
        val telephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephony.listen(listener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    //Derived classes should override these to respond to specific events of interest
    protected abstract fun onIncomingCallStarted(number: String, start: Date)

    protected abstract fun onOutgoingCallStarted(number: String?, start: Date)

    protected abstract fun onIncomingCallEnded(number: String?, start: Date, end: Date)

    protected abstract fun onOutgoingCallEnded(number: String?, start: Date, end: Date)

    protected abstract fun onMissedCall(number: String?, start: Date)

    /**
     * Deals with actual events
     */
    inner class PhoneCallStartEndDetector : PhoneStateListener() {

        private var lastState = TelephonyManager.CALL_STATE_IDLE
        private lateinit var callStartTime: Date
        private var isIncoming: Boolean = false
        private var savedNumber: String? = ""  // because the passed incoming is only valid in ringing

        /**
         * The outgoing number is only sent via a separate intent, so we need to store it out of band
         *
         * @param number
         */
        fun setOutgoingNumber(number: String?) {
            savedNumber = number
        }

        /**
         * Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
         * Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
         *
         * @param state
         * @param incomingNumber
         */
        override fun onCallStateChanged(state: Int, incomingNumber: String) {
            super.onCallStateChanged(state, incomingNumber)
            if (lastState == state) {
                //No change, debounce extras
                return
            }
            when (state) {
                TelephonyManager.CALL_STATE_RINGING -> {
                    isIncoming = true
                    callStartTime = Date()
                    savedNumber = incomingNumber
                    onIncomingCallStarted(incomingNumber, callStartTime)
                }
                TelephonyManager.CALL_STATE_OFFHOOK ->
                    //Transition of ringing->offhook are pickups of incoming calls.  Nothing donw on them
                    if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                        isIncoming = false
                        callStartTime = Date()
                        onOutgoingCallStarted(savedNumber, callStartTime)
                    }
                TelephonyManager.CALL_STATE_IDLE ->
                    //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                    when {
                        lastState == TelephonyManager.CALL_STATE_RINGING -> //Ring but no pickup-  a miss
                            onMissedCall(savedNumber, callStartTime)
                        isIncoming -> onIncomingCallEnded(savedNumber, callStartTime, Date())
                        else -> onOutgoingCallEnded(savedNumber, callStartTime, Date())
                    }
            }
            lastState = state
        }
    }

    companion object {

        /**
         * The receiver will be recreated whenever android feels like it.  We need a static variable to remember data between instantiations
         */
        internal var listener: PhoneCallStartEndDetector? = null
    }
}