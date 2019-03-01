package com.crazylegend.kotlinextensions.date

import org.joda.time.*
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.*


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */


val DateTime.isToday : Boolean get() {
    return this.toLocalDate() == LocalDate()
}

val DateTime.hoursAgo : Int get() {
    return Hours.hoursBetween(this.toLocalDateTime(), LocalDateTime()).hours
}

val DateTime.minutesAgo : Int get() {
    return Minutes.minutesBetween(this.toLocalDateTime(), LocalDateTime()).minutes
}

val DateTime.secondsAgo : Int get() {
    return Seconds.secondsBetween(this.toLocalDateTime(), LocalDateTime()).seconds
}


val DateTime.daysAgo : Int get() {
    return Days.daysBetween(this.toLocalDateTime(), LocalDateTime()).days
}

val DateTime.monthsAgo : Int get() {
    return Months.monthsBetween(this.toLocalDateTime(), LocalDateTime()).months
}

val DateTime.yearsAgo : Int get() {
    return Years.yearsBetween(this.toLocalDateTime(), LocalDateTime()).years
}



val String.parseIsBeforeNow : Boolean get() {
    return DateTime.parse(this).isBeforeNow
}

val String.parseIsAfterNow : Boolean get() {
    return DateTime.parse(this).isAfterNow
}


fun String.parseIsBeforeNow(dateTimeFormat: DateTimeFormatter) : Boolean {
    return DateTime.parse(this, dateTimeFormat).isBeforeNow
}

fun String.parseIsAfterNow(dateTimeFormat: DateTimeFormatter) : Boolean {
    return DateTime.parse(this, dateTimeFormat).isAfterNow
}



fun Date.millisecondsSince(date: Date) = (time - date.time)
fun Date.secondsSince(date: Date): Double = millisecondsSince(date) / 1000.0
fun Date.minutesSince(date: Date): Double = secondsSince(date) / 60
fun Date.hoursSince(date: Date): Double = minutesSince(date) / 60
fun Date.daysSince(date: Date): Double = hoursSince(date) / 24
fun Date.weeksSince(date: Date): Double = daysSince(date) / 7
fun Date.monthsSince(date: Date): Double = weeksSince(date) / 4
fun Date.yearsSince(date: Date): Double = monthsSince(date) / 12


/**
 * get Current Date.
 */
fun currentDate() = Date(System.currentTimeMillis())