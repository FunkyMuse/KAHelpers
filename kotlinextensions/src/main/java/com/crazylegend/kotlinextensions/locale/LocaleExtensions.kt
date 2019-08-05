package com.crazylegend.kotlinextensions.locale

import android.os.Build
import android.os.LocaleList
import java.util.*


/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */

val defaultLocale: Locale
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) LocaleList.getDefault()[0] else Locale.getDefault()

infix fun Locale.equals(locale: Locale): Boolean =
        if (this == locale) true else country == locale.country && language == locale.language