package dev.funkymuse.jodadatetime

import org.joda.time.DateTime
import org.joda.time.DateTimeConstants
import org.joda.time.DateTimeZone
import org.joda.time.Days
import org.joda.time.Duration
import org.joda.time.Hours
import org.joda.time.Instant
import org.joda.time.Interval
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.Minutes
import org.joda.time.Months
import org.joda.time.MutableDateTime
import org.joda.time.Period
import org.joda.time.ReadableDuration
import org.joda.time.ReadableInstant
import org.joda.time.ReadablePeriod
import org.joda.time.Seconds
import org.joda.time.Years
import org.joda.time.base.AbstractInstant
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat
import java.sql.Timestamp
import java.util.Date


@JvmField
val EPOCH = DateTime(0)


val DateTime.isToday: Boolean
    get() {
        return this.toLocalDate() == LocalDate()
    }

val DateTime.hoursAgo: Int
    get() {
        return Hours.hoursBetween(this.toLocalDateTime(), LocalDateTime()).hours
    }

val DateTime.minutesAgo: Int
    get() {
        return Minutes.minutesBetween(this.toLocalDateTime(), LocalDateTime()).minutes
    }

val DateTime.secondsAgo: Int
    get() {
        return Seconds.secondsBetween(this.toLocalDateTime(), LocalDateTime()).seconds
    }


val DateTime.daysAgo: Int
    get() {
        return Days.daysBetween(this.toLocalDateTime(), LocalDateTime()).days
    }

val DateTime.monthsAgo: Int
    get() {
        return Months.monthsBetween(this.toLocalDateTime(), LocalDateTime()).months
    }

val DateTime.yearsAgo: Int
    get() {
        return Years.yearsBetween(this.toLocalDateTime(), LocalDateTime()).years
    }


val String.parseIsBeforeNow: Boolean
    get() {
        return DateTime.parse(this).isBeforeNow
    }

val String.parseIsAfterNow: Boolean
    get() {
        return DateTime.parse(this).isAfterNow
    }


fun String.parseIsBeforeNow(dateTimeFormat: DateTimeFormatter): Boolean {
    return DateTime.parse(this, dateTimeFormat).isBeforeNow
}

fun String.parseIsAfterNow(dateTimeFormat: DateTimeFormatter): Boolean {
    return DateTime.parse(this, dateTimeFormat).isAfterNow
}


/** Convert `Date` to [DateTime] */
fun Date.toDateTime(): DateTime = DateTime(this.time)

/** Convert `Date` to `LocalDateTime` */
fun Date.toLocalDateTime(): LocalDateTime = LocalDateTime.fromDateFields(this)

/** Convert `Date` to `LocalDate` */
fun Date.toLocalDate(): LocalDate = LocalDate.fromDateFields(this)

/** Convert `Date` to `LocalTime` */
fun Date.toLocalTime(): LocalTime = LocalTime.fromDateFields(this)

/** Convert `Instant` to [DateTime] with TimeZone UTC */
fun AbstractInstant.dateTimeUTC(): DateTime = this.toDateTime(DateTimeZone.UTC)

/** Convert `Instant` to `MutableDateTimeWrapper` with TimeZone UTC */
fun AbstractInstant.mutableDateTimeUTC(): MutableDateTime = this.toMutableDateTime(DateTimeZone.UTC)

/** Millsecond */
//fun Int.millis(): DurationBuilder = DurationBuilder(Period.millis(this))
fun Int.millis(): Period = Period.millis(this)

/** Seconds */
//fun Int.seconds(): DurationBuilder = DurationBuilder(Period.seconds(this))
fun Int.seconds(): Period = Period.seconds(this)

/** N Minutes */
//fun Int.minutes(): DurationBuilder = DurationBuilder(Period.minutes(this))
fun Int.minutes(): Period = Period.minutes(this)

/** N Hours */
//fun Int.hours(): DurationBuilder = DurationBuilder(Period.hours(this))
fun Int.hours(): Period = Period.hours(this)

/** Period in N days */
fun Int.days(): Period = Period.days(this)

/** Period in N weeks */
fun Int.weeks(): Period = Period.weeks(this)

/** Period in N months */
fun Int.months(): Period = Period.months(this)

/** Period in N years */
fun Int.years(): Period = Period.years(this)

///**  N times duration */
//operator fun Int.times(builder: DurationBuilder): DurationBuilder = DurationBuilder(builder.period.multipliedBy(this))

/**  N times period */
operator fun Int.times(period: Period): Period = period.multipliedBy(this)


/** N millis period */
fun Long.millis(): Period = Period.millis(this.toInt())

/** N seconds period */
fun Long.seconds(): Period = Period.seconds(this.toInt())

/** N minutes period */
fun Long.minutes(): Period = Period.minutes(this.toInt())

/** N hours period */
fun Long.hours(): Period = Period.hours(this.toInt())


/** N days period */
fun Long.days(): Period = Period.days(this.toInt())

/** N weeks period */
fun Long.weeks(): Period = Period.weeks(this.toInt())

/** N months period */
fun Long.months(): Period = Period.months(this.toInt())

/** N years period */
fun Long.years(): Period = Period.years(this.toInt())

///**  N times duration */
//operator fun Long.times(builder: DurationBuilder): DurationBuilder =
// DurationBuilder(builder.period.multipliedBy(this.toInt()))

/**  N times period  */
operator fun Long.times(period: Period): Period = period.multipliedBy(this.toInt())

/**
 * get `DateTimeFormatter` with specified pattern
 */
fun dateTimeFormat(pattern: String): DateTimeFormatter = DateTimeFormat.forPattern(pattern)

/** Parse string to [DateTime] */
fun String.toDateTime(pattern: String? = null): DateTime? = try {
    if (pattern.isNullOrBlank()) DateTime(this)
    else DateTime.parse(this, dateTimeFormat(pattern))
} catch (ignored: Throwable) {
    null
}

/** Parse string to `Interval` */
fun String.toInterval(): Interval? = try {
    Interval.parse(this)
} catch (ignored: Throwable) {
    null
}

/** Parse string to `LocalDate` */
fun String.toLocalDate(pattern: String? = null): LocalDate? = try {
    if (pattern.isNullOrBlank()) LocalDate(this)
    else LocalDate.parse(this, dateTimeFormat(pattern))
} catch (ignored: Throwable) {
    null
}

/** Parse string to `LocalTime` */
fun String.toLocalTime(pattern: String? = null): LocalTime? = try {
    if (pattern.isNullOrBlank())
        LocalTime(this)
    else LocalTime.parse(this, dateTimeFormat(pattern))
} catch (ignored: Throwable) {
    null
}

/** Convert json text to [DateTime] */
fun dateTimeFromJson(json: String): DateTime = DateTime(json)


/** Build [DateTime] */
@JvmOverloads
fun dateTimeOf(year: Int,
               month: Int = 1,
               day: Int = 1,
               hours: Int = 0,
               minutes: Int = 0,
               seconds: Int = 0,
               millis: Int = 0): DateTime =
        DateTime(year, month, day, hours, minutes, seconds, millis)

/** Start time of Day from this dateTime */
fun DateTime.startOfDay(): DateTime = this.withTimeAtStartOfDay()

fun DateTime.startOfWeek(): DateTime =
        this.minusDays(this.dayOfWeek - DateTimeConstants.MONDAY).startOfDay()

/** Start time of Month from this dateTime */
fun DateTime.startOfMonth(): DateTime = dateTimeOf(this.year, this.monthOfYear)

/** Start time of Year from this dateTime */
fun DateTime.startOfYear(): DateTime = dateTimeOf(this.year)

@JvmOverloads
fun DateTime.trimToHour(hour: Int = this.hourOfDay): DateTime =
        startOfDay().withHourOfDay(hour)

@JvmOverloads
fun DateTime.trimToMinute(minute: Int = this.minuteOfHour): DateTime =
        trimToHour().withMinuteOfHour(minute)

@JvmOverloads
fun DateTime.trimToSecond(second: Int = this.secondOfMinute): DateTime =
        trimToMinute().withSecondOfMinute(second)


/** DateTime `-` operator */
operator fun DateTime.minus(millis: Long): DateTime = this.minus(millis)

operator fun DateTime.minus(duration: ReadableDuration): DateTime = this.minus(duration)
operator fun DateTime.minus(period: ReadablePeriod): DateTime = this.minus(period)
//operator fun DateTime.minus(builder: DurationBuilder): DateTime = this.minus(builder.period)


/** DateTime `+` operator */
operator fun DateTime.plus(millis: Long): DateTime = this.plus(millis)

/** DateTime `+` operator */
operator fun DateTime.plus(duration: ReadableDuration): DateTime = this.plus(duration)

/** DateTime `+` operator */
operator fun DateTime.plus(period: ReadablePeriod): DateTime = this.plus(period)
// operator fun DateTime.plus(builder: DurationBuilder): DateTime = this.plus(builder.period)


/** next day */
fun DateTime.tomorrow(): DateTime = this.nextDay()

/** last day */
fun DateTime.yesterday(): DateTime = this.lastDay()

fun DateTime.nextSecond(): DateTime = this.plusSeconds(1)
fun DateTime.nextMinute(): DateTime = this.plusMinutes(1)
fun DateTime.nextHour(): DateTime = this.plusHours(1)
fun DateTime.nextDay(): DateTime = this.plusDays(1)
fun DateTime.nextWeek(): DateTime = this.plusWeeks(1)
fun DateTime.nextMonth(): DateTime = this.plusMonths(1)
fun DateTime.nextYear(): DateTime = this.plusYears(1)

fun DateTime.lastSecond(): DateTime = this.minusSeconds(1)
fun DateTime.lastMinute(): DateTime = this.minusMinutes(1)
fun DateTime.lastHour(): DateTime = this.minusHours(1)
fun DateTime.lastDay(): DateTime = this.minusDays(1)
fun DateTime.lastWeek(): DateTime = this.minusWeeks(1)
fun DateTime.lastMonth(): DateTime = this.minusMonths(1)
fun DateTime.lastYear(): DateTime = this.minusYears(1)

/** Convert [DateTime] to [Timestamp] */
fun DateTime.toTimestamp(): Timestamp = Timestamp(this.millis)

/** Convert [DateTime] timezone to UTC */
fun DateTime.asUtc(): DateTime = this.toDateTime(DateTimeZone.UTC)

/** Convert [DateTime] timezone to specified time zone. not specified to system default time zone */
@JvmOverloads
fun DateTime.asLocal(tz: DateTimeZone = DateTimeZone.getDefault()): DateTime = this.toDateTime(tz)

/** Convert [DateTime] to ISO DateTime Format String (YYYY-MM-DD hh:nn:ss.zzz) */
fun DateTime.toIsoFormatString(): String = ISODateTimeFormat.dateTime().print(this)

/** Convert [DateTime] to ISO Date Format String (YYYY-MM-DD) */
fun DateTime.toIsoFormatDateString(): String = ISODateTimeFormat.date().print(this)

/** Convert [DateTime] to ISO Time Format String (hh:nn:ss.zzz) */
fun DateTime.toIsoFormatTimeString(): String = ISODateTimeFormat.time().print(this)

/** Convert [DateTime] to ISO Time Format String without millis (hh:nn:ss) */
fun DateTime.toIsoFormatTimeNoMillisString(): String = ISODateTimeFormat.timeNoMillis().print(this)

/** Convert [DateTime] to ISO DateTime Format String without millis (YYYY-MM-DD hh:nn:ss) */
fun DateTime.toIsoFormatHMSString(): String = ISODateTimeFormat.dateHourMinuteSecond().print(this)


infix fun <T : ReadableInstant> T.min(that: T): T {
    return if (this < that) this else that
}

infix fun <T : ReadableInstant> T.max(that: T): T {
    return if (this > that) this else that
}

/** get minimum [DateTime] */
infix fun DateTime.min(that: DateTime): DateTime {
    return if (this < that) this else that
}

/** get maximum [DateTime] */
infix fun DateTime.max(that: DateTime): DateTime {
    return if (this > that) this else that
}

fun minOf(a: DateTime, b: DateTime, vararg args: DateTime): DateTime {
    var min = if (a < b) a else b
    args.forEach {
        if (it < min) {
            min = it
        }
    }
    return min
}

fun maxOf(a: DateTime, b: DateTime, vararg args: DateTime): DateTime {
    var max = if (a < b) b else a
    args.forEach {
        if (it > max) {
            max = it
        }
    }
    return max
}

/** Get month interval in specified [DateTime] */
fun DateTime.monthInterval(months: Int = 1): Interval {
    val start = this.startOfMonth()
    return Interval(start, start + months.months())
}

fun DateTime.weekInterval(weeks: Int = 1): Interval {
    val start = this.startOfWeek()
    return Interval(start, start + weeks.weeks())
}

/** Get day interval in specified [DateTime] */
fun DateTime.dayInterval(days: Int = 1): Interval {
    val start = this.startOfDay()
    return Interval(start, start + days.days())
}

fun DateTime.hourInterval(hours: Int = 1): Interval {
    val start = this.trimToMinute(0)
    return Interval(start, start + hours.hours())
}

fun DateTime.minuteInterval(minutes: Int = 1): Interval {
    val start = this.trimToSecond(0)
    return Interval(start, start + minutes.minutes())
}

/** current [DateTime] */
fun now(): DateTime = DateTime.now()

/** Today (only date part without time part) */
fun today(): DateTime = now().withTimeAtStartOfDay()

/** next day of current [DateTime] */
fun tomorrow(): DateTime = today().nextDay()

/** last day of current [DateTime] */
fun yesterday(): DateTime = today().lastDay()

fun nextSecond(): DateTime = now().plusSeconds(1)
fun nextMinute(): DateTime = now().plusMinutes(1)
fun nextHour(): DateTime = now().plusHours(1)
fun nextDay(): DateTime = now().plusDays(1)
fun nextWeek(): DateTime = now().plusWeeks(1)
fun nextMonth(): DateTime = now().plusMonths(1)
fun nextYear(): DateTime = now().plusYears(1)

fun lastSecond(): DateTime = now().minusSeconds(1)
fun lastMinute(): DateTime = now().minusMinutes(1)
fun lastHour(): DateTime = now().minusHours(1)
fun lastDay(): DateTime = now().minusDays(1)
fun lastWeek(): DateTime = now().minusWeeks(1)
fun lastMonth(): DateTime = now().minusMonths(1)
fun lastYear(): DateTime = now().minusYears(1)

/** `-` operator for `LocalDateTime` */
operator fun LocalDateTime.minus(period: ReadablePeriod): LocalDateTime = this.minus(period)

/** `-` operator for `LocalDateTime` */
operator fun LocalDateTime.minus(duration: ReadableDuration): LocalDateTime = this.minus(duration)

/** `+` operator for `LocalDateTime` */
operator fun LocalDateTime.plus(period: ReadablePeriod): LocalDateTime = this.plus(period)

/** `+` operator for `LocalDateTime` */
operator fun LocalDateTime.plus(duration: ReadableDuration): LocalDateTime = this.plus(duration)

/** `-` operator for `LocalDate` */
operator fun LocalDate.minus(period: Period): LocalDate = this.minus(period)

/** `-` operator for `LocalDate` */
operator fun LocalDate.minus(duration: ReadableDuration): LocalDate = this.minus(duration.toPeriod())

/** `+` operator for `LocalDate` */
operator fun LocalDate.plus(period: Period): LocalDate = this.plus(period)

/** `+` operator for `LocalDate` */
operator fun LocalDate.plus(duration: ReadableDuration): LocalDate = this.plus(duration.toPeriod())

/** `-` operator for `LocalTime` */
operator fun LocalTime.minus(period: Period): LocalTime = this.minus(period)

/** `-` operator for `LocalTime` */
operator fun LocalTime.minus(duration: ReadableDuration): LocalTime = this.minus(duration.toPeriod())

/** `+` operator for `LocalTime` */
operator fun LocalTime.plus(period: Period): LocalTime = this.plus(period)

/** `+` operator for `LocalTime` */
operator fun LocalTime.plus(duration: ReadableDuration): LocalTime = this.plus(duration.toPeriod())

/**
 * empty `Duration`
 */
val emptyDuration: Duration = Duration.ZERO

/** specified days duration */
fun standardDays(days: Long): Duration = Duration.standardDays(days)

/** specified hours duration */
fun standardHours(hours: Long): Duration = Duration.standardHours(hours)

/** specified minutes duration */
fun standardMinutes(minutes: Long): Duration = Duration.standardMinutes(minutes)

/** specified seconds duration */
fun standardSeconds(seconds: Long): Duration = Duration.standardSeconds(seconds)

/** duration of days */
fun dayDurationOf(days: Long): Duration = Duration.standardDays(days)

/** duration of hours */
fun hourDurationOf(hours: Long): Duration = Duration.standardHours(hours)

/** duration of minutes */
fun minuteDurationOf(minutes: Long): Duration = Duration.standardMinutes(minutes)

/** duration of seconds */
fun secondDurationOf(seconds: Long): Duration = Duration.standardSeconds(seconds)

/** duration with millis */
fun milliDurationOf(millis: Long): Duration = Duration.millis(millis)


val Int.dayDuration: Duration get() = Duration.standardDays(this.toLong())
val Int.hourDuration: Duration get() = Duration.standardHours(this.toLong())
val Int.minuteDuration: Duration get() = Duration.standardMinutes(this.toLong())
val Int.secondDuration: Duration get() = Duration.standardSeconds(this.toLong())
val Int.milliDuration: Duration get() = Duration.millis(this.toLong())

val Long.dayDuration: Duration get() = Duration.standardDays(this)
val Long.hourDuration: Duration get() = Duration.standardHours(this)
val Long.minuteDuration: Duration get() = Duration.standardMinutes(this)
val Long.secondDuration: Duration get() = Duration.standardSeconds(this)
val Long.milliDuration: Duration get() = Duration.millis(this)

val Duration.days: Long get() = this.standardDays
val Duration.hours: Long get() = this.standardHours
val Duration.minutes: Long get() = this.standardMinutes
val Duration.seconds: Long get() = this.standardSeconds

/** absolute duration */
fun Duration.absoluteDuration(): Duration = if (this < emptyDuration) -this else this

/** get [DateTime] from current time + duration */
fun Duration.fromNow(): DateTime = now() + this

/** get [DateTime] from current time - duration */
fun Duration.agoNow(): DateTime = now() - this

/** get [DateTime] from EPOCH + duration */
fun Duration.afterEpoch(): DateTime = EPOCH + this

/** difference duration with other duration  */
fun Duration.diff(other: Duration): Duration = this - other

operator fun Duration.unaryMinus(): Duration = this.negated()
operator fun Duration.div(divisor: Long): Duration = this.dividedBy(divisor)
operator fun Duration.times(multiplicand: Long): Duration = this.multipliedBy(multiplicand)

val Duration.isZero: Boolean get() = this.millis == 0L

infix fun Duration.min(that: Duration): Duration {
    return if (this < that) this else that
}

infix fun Duration.max(that: Duration): Duration {
    return if (this > that) this else that
}

fun minOf(a: Duration, b: Duration, vararg args: Duration): Duration {
    var min = if (a < b) a else b
    args.forEach {
        if (it < min)
            min = it
    }
    return min
}

fun maxOf(a: Duration, b: Duration, vararg args: Duration): Duration {
    var max = if (a > b) a else b
    args.forEach {
        if (it > max)
            max = it
    }
    return max
}


/** current time - specified period */
fun Period.ago(): DateTime = DateTime.now() - this

fun Period.later(): DateTime = DateTime.now() + this
fun Period.from(moment: DateTime): DateTime = moment + this
fun Period.before(moment: DateTime): DateTime = moment - this
fun Period.standardDuration(): Duration = this.toStandardDuration()

val Period.duration: Duration get() = this.toStandardDuration()

fun periodOfYears(y: Int): Period = Period.years(y)
fun periodOfMonths(m: Int): Period = Period.months(m)
fun periodOfWeek(w: Int): Period = Period.weeks(w)
fun periodOfDay(d: Int): Period = Period.days(d)
fun periodOfHours(h: Int): Period = Period.hours(h)
fun periodOfMinutes(m: Int): Period = Period.minutes(m)
fun periodOfSeconds(s: Int): Period = Period.seconds(s)
fun periodOfMillis(m: Int): Period = Period.millis(m)

operator fun Period.minus(period: ReadablePeriod?): Period = this.minus(period)
operator fun Period.plus(period: ReadablePeriod?): Period = this.plus(period)

operator fun Period.times(scalar: Int): Period = this.multipliedBy(scalar)
operator fun Period.unaryMinus(): Period = this.negated()

operator fun Period.rangeTo(end: ReadableInstant): Interval = Interval(this, end)

/** create new `Instant` instance with milliseconds */
fun instantOf(millis: Long): Instant = Instant(millis)

operator fun Instant.minus(millis: Long): Instant = this.minus(millis)
operator fun Instant.minus(duration: ReadableDuration): Instant = this.minus(duration)
operator fun Instant.minus(period: Period): Instant = this.minus(period.toStandardDuration())

operator fun Instant.plus(millis: Long): Instant = this.plus(millis)
operator fun Instant.plus(duration: ReadableDuration): Instant = this.plus(duration)
operator fun Instant.plus(period: Period): Instant = this.plus(period.toStandardDuration())

operator fun ReadableInstant.rangeTo(endExclusive: ReadableInstant): Interval = Interval(this, endExclusive)
operator fun ReadableInstant.rangeTo(duration: ReadableDuration): Interval = Interval(this, duration)
operator fun ReadableInstant.rangeTo(period: ReadablePeriod): Interval = Interval(this, period)

fun thisSecond(): Interval = now().secondOfMinute().toInterval()
fun thisMinute(): Interval = now().minuteOfHour().toInterval()
fun thisHour(): Interval = now().hourOfDay().toInterval()

fun <T : ReadableInstant> minOf(a: T, b: T, vararg args: T): T {
    var min = if (a < b) a else b
    args.forEach {
        if (it < min)
            min = it
    }
    return min
}

fun <T : ReadableInstant> maxOf(a: T, b: T, vararg args: T): T {
    var max = if (a > b) a else b
    args.forEach {
        if (it > max)
            max = it
    }
    return max
}