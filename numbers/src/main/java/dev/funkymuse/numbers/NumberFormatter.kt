package dev.funkymuse.numbers

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale


fun getNumberFormatter(locale: Locale): DecimalFormat {
    return NumberFormat.getInstance(locale) as DecimalFormat
}
