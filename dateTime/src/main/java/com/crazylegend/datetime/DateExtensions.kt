package com.crazylegend.datetime

import android.content.Context
import android.text.format.DateFormat
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */

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
val currentDate get() = Date(System.currentTimeMillis())

/**
 * Gives [Calendar] object from Date
 */
inline val Date.calendar: Calendar
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar
    }


/**
 * Gets  Year directly from [Calendar] Object
 */
inline val Calendar.year: Int
    get() = get(Calendar.YEAR)

/**
 * Gets value of DayOfMonth from [Calendar] Object
 */
inline val Calendar.dayOfMonth: Int
    get() = get(Calendar.DAY_OF_MONTH)

/**
 * Gets value of Month from [Calendar] Object
 */
inline val Calendar.month: Int
    get() = get(Calendar.MONTH)

/**
 * Gets value of Hour from [Calendar] Object
 */
inline val Calendar.hour: Int
    get() = get(Calendar.HOUR)

/**
 * Gets value of HourOfDay from [Calendar] Object
 */
inline val Calendar.hourOfDay: Int
    get() = get(Calendar.HOUR_OF_DAY)

/**
 * Gets value of Minute from [Calendar] Object
 */
inline val Calendar.minute: Int
    get() = get(Calendar.MINUTE)

/**
 * Gets value of Second from [Calendar] Object
 */
inline val Calendar.second: Int
    get() = get(Calendar.SECOND)

/**
 * Gets value of DayOfMonth from [Date] Object
 */
inline val Date.yearFromCalendar: Int
    get() = calendar.year

/**
 * Gets value of DayOfMonth from [Date] Object
 */
inline val Date.dayOfMonth: Int
    get() = calendar.dayOfMonth

/**
 * Gets value of Month from [Date] Object
 */
inline val Date.monthFromCalendar: Int
    get() = calendar.month

/**
 * Gets value of Hour from [Date] Object
 */
inline val Date.hour: Int
    get() = calendar.hour

/**
 * Gets value of HourOfDay from [Date] Object
 */
inline val Date.hourOfDay: Int
    get() = calendar.hourOfDay

/**
 * Gets value of Minute from [Date] Object
 */
inline val Date.minute: Int
    get() = calendar.minute


/**
 * Gets value of Second from [Date] Object
 */
inline val Date.second: Int
    get() = calendar.second


/**
 * Gets value of Milliseconds of current time
 */
inline val now: Long
    get() = Calendar.getInstance().timeInMillis

/**
 * Gets current time in given format
 */
fun getCurrentTimeInFormat(stringFormat: String): String {
    val currentTime = Date()
    return SimpleDateFormat(stringFormat, Locale.getDefault()).format(currentTime)
}

/**
 * Formats date according to device's default date format
 */
fun Context.formatDateAccordingToDevice(date: Date): String {
    val format = DateFormat.getDateFormat(this)
    return format.format(date)
}

/**
 * Formats time according to device's default time format
 */
fun Context.formatTimeAccordingToDevice(date: Date): String {
    val format = DateFormat.getTimeFormat(this)
    return format.format(date)
}

fun Date.getCurrentTimeString(): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(this.time)
}

fun Date.getCurrentTimeHour(): Int {
    return this.getCurrentTimeString().split(":")[0].toInt()
}

fun Date.getCurrentTimeMinutes(): Int {
    return this.getCurrentTimeString().split(":")[1].toInt()
}

fun Date.getMonthNumber(): Int {
    val dateFormat = SimpleDateFormat("MM", Locale.getDefault())
    val mStr = dateFormat.format(this.time)
    return Integer.parseInt(mStr)
}

fun Date.getCurrentDayString(): String {
    val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
    return dateFormat.format(this.time)
}

fun Date.getCurrentWeekdayString(): String {
    val weekdayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    return weekdayFormat.format(this.time)
}

fun Date.getCurrentDateString(pattern: String): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormat.format(this.time)
}

// Converts current date to proper provided format
fun Date.convertTo(format: String): String? {
    var dateStr: String? = null
    val df = SimpleDateFormat(format)
    try {
        dateStr = df.format(this)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

    return dateStr
}

// Converts current date to Calendar
fun Date.toCalendar(): Calendar {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal
}

fun Date.isFuture(): Boolean {
    return !Date().before(this)
}

fun Date.isPast(): Boolean {
    return Date().before(this)
}

fun Date.isToday(): Boolean {
    return DateUtils.isToday(this.time)
}

fun Date.isYesterday(): Boolean {
    return DateUtils.isToday(this.time + DateUtils.DAY_IN_MILLIS)
}

fun Date.isTomorrow(): Boolean {
    return DateUtils.isToday(this.time - DateUtils.DAY_IN_MILLIS)
}

fun Date.today(): Date {
    return Date()
}

fun Date.yesterday(): Date {
    val cal = this.toCalendar()
    cal.add(Calendar.DAY_OF_YEAR, -1)
    return cal.time
}

fun Date.tomorrow(): Date {
    val cal = this.toCalendar()
    cal.add(Calendar.DAY_OF_YEAR, 1)
    return cal.time
}

fun Date.hour(): Int {
    return this.toCalendar().get(Calendar.HOUR)
}

fun Date.minute(): Int {
    return this.toCalendar().get(Calendar.MINUTE)
}

fun Date.second(): Int {
    return this.toCalendar().get(Calendar.SECOND)
}

fun Date.month(): Int {
    return this.toCalendar().get(Calendar.MONTH) + 1
}

fun Date.monthName(locale: Locale = Locale.getDefault()): String? {
    return this.toCalendar().getDisplayName(Calendar.MONTH, Calendar.LONG, locale)
}

fun Date.year(): Int {
    return this.toCalendar().get(Calendar.YEAR)
}

fun Date.day(): Int {
    return this.toCalendar().get(Calendar.DAY_OF_MONTH)
}

fun Date.dayOfWeek(): Int {
    return this.toCalendar().get(Calendar.DAY_OF_WEEK)
}

fun Date.dayOfWeekName(locale: Locale = Locale.getDefault()): String? {
    return this.toCalendar().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale)
}

fun Date.dayOfYear(): Int {
    return this.toCalendar().get(Calendar.DAY_OF_YEAR)
}


fun Date.resetHourMinSecForDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.time
}


fun Date.isDateToday(): Boolean {
    return this.resetHourMinSecForDate() == Date().resetHourMinSecForDate()
}

fun Date.isDateSameDay(compareDate: Date): Boolean {
    return this.resetHourMinSecForDate() == compareDate.resetHourMinSecForDate()
}

fun Date.format(pattern: String, locale: Locale): String {
    return SimpleDateFormat(pattern, locale).format(this)
}

fun Date.isDateThisYear(): Boolean {
    val calendar = Calendar.getInstance()

    val currentYear = calendar.get(Calendar.YEAR)
    calendar.time = this

    return currentYear == calendar.get(Calendar.YEAR)
}

fun Date.sameWeekLastYear(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
    val currentYear = calendar.get(Calendar.YEAR)
    calendar.set(Calendar.YEAR, currentYear - 1)
    calendar.set(Calendar.WEEK_OF_YEAR, currentWeek)
    return calendar.time
}

fun Date.lastDayOfMonth(): Date {
    val c = Calendar.getInstance()
    c.time = this
    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH))
    return c.time
}

fun Date.lastDayOfWeek(): Date {
    val c = Calendar.getInstance()
    c.time = this
    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_WEEK))
    return c.time
}

fun Date.getAge(): Int {
    val birthday = Calendar.getInstance()
    birthday.time = this
    val today = Calendar.getInstance()

    var age = today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR)
    if (today.get(Calendar.DAY_OF_YEAR) < birthday.get(Calendar.DAY_OF_YEAR)) {
        age -= 1
    }
    return age
}


val Date?.isTheDateToday: Boolean
    get() {
        if (this == null) return false
        return DateUtils.isToday(this.time)
    }

fun Date?.isDateYesterday(): Boolean {
    if (this == null) return false

    val instance = Calendar.getInstance()
    instance.time = this
    val yesterday = Calendar.getInstance()
    yesterday.add(Calendar.DAY_OF_YEAR, -1)

    return (yesterday.get(Calendar.YEAR) == instance.get(Calendar.YEAR) &&
            yesterday.get(Calendar.DAY_OF_YEAR) == instance.get(Calendar.DAY_OF_YEAR))
}

fun Date?.isDateTomorrow(): Boolean {
    if (this == null) return false

    val instance = Calendar.getInstance()
    instance.time = this
    val tomorrow = Calendar.getInstance()
    tomorrow.add(Calendar.DAY_OF_YEAR, 1)

    return (tomorrow.get(Calendar.YEAR) == instance.get(Calendar.YEAR) &&
            tomorrow.get(Calendar.DAY_OF_YEAR) == instance.get(Calendar.DAY_OF_YEAR))
}


fun Date.getDifferenceInDays(compareDate: Date): Long {
    val difference = compareDate.time - this.time
    return TimeUnit.MILLISECONDS.toDays(difference)
}

fun Date.getDifferenceInWeek(date: Date): Int {
    val calendar = Calendar.getInstance()
    val compareDate: Date = if (this.before(date)) {
        calendar.time = this
        date
    } else {
        calendar.time = date
        this
    }
    var i = 0
    while (calendar.time.before(compareDate)) {
        calendar.add(Calendar.WEEK_OF_YEAR, 1)
        i++
    }
    if (date.before(this)) {
        i *= -1
    }
    return --i
}

fun Date.getDifferenceInMonth(date: Date): Int {
    val calendar = Calendar.getInstance()
    val compareDate: Date = if (this.before(date)) {
        calendar.time = this
        date
    } else {
        calendar.time = date
        this
    }
    var i = 0
    while (calendar.time.before(compareDate)) {
        calendar.add(Calendar.MONTH, 1)
        i++
    }
    if (date.before(this)) {
        i *= -1
    }
    if (calendar.get(Calendar.DAY_OF_MONTH) == compareDate.dayOfMonth) {
        return i
    }
    return --i
}

fun Date.getMillisToNextMin(): Long {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.MINUTE, 1)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis - this.time
}


fun Date.getCurrentDateString(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(this.time)
}

fun Date.getCurrentDayAndMonthString(): String {
    val dateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
    return dateFormat.format(this.time)
}

fun Date.getCurrentYear(): String {
    val dateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    return dateFormat.format(this.time)
}

fun Date.getCurrentYearInt(): Int {
    return Integer.parseInt(this.getCurrentYear())
}

fun Long.extractDate(): Date {
    return Date(this * 1000L)
}


fun getDateDifferense(startDate: Date, endDate: Date): Pair<String, Long>? {

    var different = endDate.time - startDate.time

    val secondsInMilli: Long = 1000
    val minutesInMilli = secondsInMilli * 60
    val hoursInMilli = minutesInMilli * 60
    val daysInMilli = hoursInMilli * 24
    val monthsInMilli = daysInMilli * 30

    val elapsedDays = different / daysInMilli
    different %= daysInMilli

    val elapsedHours = different / hoursInMilli
    different %= hoursInMilli

    val elapsedMinutes = different / minutesInMilli
    different %= minutesInMilli

    val elapsedSeconds = different / secondsInMilli

    return when {
        elapsedDays > 0 -> {
            Pair("D", elapsedDays)
        }
        elapsedHours > 0 -> {
            Pair("H", elapsedHours)
        }
        elapsedMinutes > 0 -> {
            Pair("M", elapsedMinutes)
        }
        elapsedSeconds > 0 -> {
            Pair("S", elapsedSeconds)
        }
        else -> {
            null
        }
    }
}

