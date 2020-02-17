package com.crazylegend.kotlinextensions.dateAndTime

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */

/**
 * Obtains an [OffsetDateTime] instance from Instant
 * @param zone A time-zone ID, default is system default
 * @return [OffsetDateTime] An instance
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Instant.toOffsetDateTime(zone: ZoneId = ZoneId.systemDefault()): OffsetDateTime =
        OffsetDateTime.ofInstant(this, zone)