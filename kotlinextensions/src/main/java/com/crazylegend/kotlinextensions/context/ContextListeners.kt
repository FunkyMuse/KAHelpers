package com.crazylegend.kotlinextensions.context

import android.content.Context


/**
 * Created by hristijan on 7/26/19 to long live and prosper !
 */


/**
 * Thermal listener
 * @receiver Context
 */
fun Context.thermalListener(onThermalChange: (thermal: Int) -> Unit = { _ -> }) {

    /*public static final int THERMAL_STATUS_NONE = 0;
    public static final int THERMAL_STATUS_LIGHT = 1;
    public static final int THERMAL_STATUS_MODERATE = 2;
    public static final int THERMAL_STATUS_SEVERE = 3;
    public static final int THERMAL_STATUS_CRITICAL = 4;
    public static final int THERMAL_STATUS_EMERGENCY = 5;
    public static final int THERMAL_STATUS_SHUTDOWN = 6;*/

    powerManager?.addThermalStatusListener {
        onThermalChange(it)
    }
}

