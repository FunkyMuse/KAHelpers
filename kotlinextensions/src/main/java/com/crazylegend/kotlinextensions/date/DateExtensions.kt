package com.crazylegend.kotlinextensions.date

import org.joda.time.DateTime
import org.joda.time.LocalDate


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */

fun DateTime.isToday(): Boolean {
    return this.toLocalDate() == LocalDate()
}

