package com.crazylegend.kotlinextensions.measurements

import kotlin.math.roundToInt


/**
 * Created by crazy on 1/24/20 to long live and prosper !
 */
object ImperialMetricUtils {
    private val stToLb = 14
    private val lbToKg = 0.45
    private val ftToIn = 12
    private val inToCm = 2.54

    fun cmToFtIn(cm: Double): Pair<Int, Int> {
        val inches = (cm / inToCm).roundToInt()
        val feet = inches / ftToIn
        return feet to inches - (feet * ftToIn)
    }

    fun kgToStLb(kg: Double): Pair<Int, Int> {
        val inches = (kg / lbToKg).roundToInt()
        val feet = inches / stToLb
        return feet to inches - (feet * stToLb)
    }
}