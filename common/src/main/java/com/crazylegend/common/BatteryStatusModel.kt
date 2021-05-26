package com.crazylegend.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by funkymuse on 5/26/21 to long live and prosper !
 */
@Parcelize
data class BatteryStatusModel(val isCharging: Boolean,
                              val isUsbCharging: Boolean,
                              val wirelessCharge: Boolean,
                              val isACCharging: Boolean,
                              val batteryCapacity: Float,
                              val batteryScale: Float) : Parcelable {

    val batteryPercentage get() = (batteryCapacity * batteryScale).toInt()

    /**
     * AC Charge = 1
     * USB Charge = 2
     * Wireless charge = 3
     * Invalid type  = -1
     */
    val chargingType
        get() = when {
            isACCharging -> 1
            isUsbCharging -> 2
            wirelessCharge -> 3
            else -> -1
        }
}