package com.crazylegend.kotlinextensions.date

import org.joda.time.*


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

