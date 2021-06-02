package com.crazylegend.locale

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
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


fun Context.getLocalizedString(requestedLocale: Locale, resourceId: Int): String {
    val config = Configuration(resources.configuration)
    config.setLocale(requestedLocale)
    return createConfigurationContext(config).getText(resourceId).toString()
}


fun Context.getLocalizedResources(requestedLocale: Locale): Resources {
    val config = Configuration(resources.configuration)
    config.setLocale(requestedLocale)
    return createConfigurationContext(config).resources
}