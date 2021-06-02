package com.crazylegend.numbers

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */


fun getNumberFormatter(locale: Locale): DecimalFormat {
    return NumberFormat.getInstance(locale) as DecimalFormat
}
