package com.funkymuse.datetime

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId




/**
 * Obtains an [OffsetDateTime] instance from Instant
 * @param zone A time-zone ID, default is system default
 * @return [OffsetDateTime] An instance
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Instant.toOffsetDateTime(zone: ZoneId = ZoneId.systemDefault()): OffsetDateTime =
        OffsetDateTime.ofInstant(this, zone)