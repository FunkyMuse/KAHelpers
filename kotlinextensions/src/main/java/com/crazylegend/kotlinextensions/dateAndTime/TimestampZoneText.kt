package com.crazylegend.kotlinextensions.dateAndTime

import org.joda.time.DateTime
import org.joda.time.DateTimeZone


/**
 * Created by hristijan on 3/5/19 to long live and prosper !
 */
open class TimestampZoneText(val dateTime: DateTime?) {

    constructor(timestamp: Long, zone: DateTimeZone) : this(DateTime(timestamp, zone))

    constructor(timestamp: Long, zoneId: String) : this(DateTime(timestamp, DateTimeZone.forID(zoneId)))

    val timestamp: Long?
        get() = dateTime?.millis

    val zoneId: String?
        get() = dateTime?.zone?.id

    val timetext: String?
        get() = dateTime?.toIsoFormatHMSString()

    override fun toString(): String {
        return "TimestampZoneText(timestamp=$timestamp, zoneId=$zoneId, timetext=$timetext)"
    }
}