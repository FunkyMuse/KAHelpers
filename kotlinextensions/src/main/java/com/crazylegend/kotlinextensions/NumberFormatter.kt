package com.crazylegend.kotlinextensions

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */
class NumberFormatter(var formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat)

/*

usage

NumberFormatter().formatter.format(number.toDouble()).toString()

*/
