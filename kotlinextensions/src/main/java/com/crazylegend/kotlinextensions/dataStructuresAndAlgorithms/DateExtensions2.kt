package com.crazylegend.kotlinextensions.dataStructuresAndAlgorithms

import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by crazy on 8/9/20 to long live and prosper !
 */


fun Date.with(
        year: Int = -1,
        month: Int = -1,
        day: Int = -1,
        hour: Int = -1,
        minute: Int = -1,
        second: Int = -1,
        millisecond: Int = -1
): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    if (year > -1) calendar.set(Calendar.YEAR, year)
    if (month > 0) calendar.set(Calendar.MONTH, month - 1)
    if (day > 0) calendar.set(Calendar.DATE, day)
    if (hour > -1) calendar.set(Calendar.HOUR_OF_DAY, hour)
    if (minute > -1) calendar.set(Calendar.MINUTE, minute)
    if (second > -1) calendar.set(Calendar.SECOND, second)
    if (millisecond > -1) calendar.set(Calendar.MILLISECOND, millisecond)
    return calendar.time
}

fun Date.with(weekday: Int = -1): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    if (weekday > -1) calendar.set(Calendar.WEEK_OF_MONTH, weekday)
    return calendar.time
}

val Date.beginningOfYear: Date
    get() = with(month = 1, day = 1, hour = 0, minute = 0, second = 0, millisecond = 0)

val Date.endOfYear: Date
    get() = with(month = 12, day = 31, hour = 23, minute = 59, second = 59, millisecond = 999)

val Date.beginningOfMonth: Date
    get() = with(day = 1, hour = 0, minute = 0, second = 0, millisecond = 0)

val Date.endOfMonth: Date
    get() = endOfMonth()

fun Date.endOfMonth(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val lastDay = calendar.getActualMaximum(Calendar.DATE)
    return with(day = lastDay, hour = 23, minute = 59, second = 59)
}

val Date.beginningOfDay: Date
    get() = with(hour = 0, minute = 0, second = 0, millisecond = 0)

val Date.endOfDay: Date
    get() = with(hour = 23, minute = 59, second = 59, millisecond = 999)

val Date.beginningOfHour: Date
    get() = with(minute = 0, second = 0, millisecond = 0)

val Date.endOfHour: Date
    get() = with(minute = 59, second = 59, millisecond = 999)

val Date.beginningOfMinute: Date
    get() = with(second = 0, millisecond = 0)

val Date.endOfMinute: Date
    get() = with(second = 59, millisecond = 999)

fun Date.toString(format: String): String = SimpleDateFormat(format).format(this)

val Date.isSunday: Boolean
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
    }

val Date.isMonday: Boolean
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
    }

val Date.isTuesday: Boolean
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY
    }

val Date.isWednesday: Boolean
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY
    }

val Date.isThursday: Boolean
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY
    }

val Date.isFriday: Boolean
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY
    }

val Date.isSaturday: Boolean
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
    }

val Date.isWeekend: Boolean
    get() = this.isSaturday || this.isSunday

val Date.isWeekday: Boolean
    get() = !this.isWeekend

val Date.isToday: Boolean
    get() = isDateIn(this, 0)

val Date.isYesterday: Boolean
    get() = isDateIn(this, -1)

val Date.isTomorrow: Boolean
    get() = isDateIn(this, 1)

private fun isDateIn(date: Date, variable: Int = 0): Boolean {
    val now = Calendar.getInstance()
    val cdate = Calendar.getInstance()
    cdate.timeInMillis = date.time

    now.add(Calendar.DATE, variable)

    return (now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
            && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
            && now.get(Calendar.DATE) == cdate.get(Calendar.DATE))
}