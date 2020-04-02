package com.crazylegend.kotlinextensions.misc

import androidx.fragment.app.Fragment
import java.util.*


/**
 * Created by crazy on 2/25/20 to long live and prosper !
 */

/**
 * Creates an [AutoClearedValue] associated with this fragment.
 */
fun <T : Any> Fragment.autoCleared() = AutoClearedValue<T>(this)


fun getCountryCode(countryName: String) =
        Locale.getISOCountries().find { Locale("", it).displayCountry == countryName }