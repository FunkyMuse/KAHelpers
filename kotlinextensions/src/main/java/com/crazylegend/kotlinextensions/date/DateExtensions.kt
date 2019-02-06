package com.crazylegend.kotlinextensions.date

import org.joda.time.*
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter


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

