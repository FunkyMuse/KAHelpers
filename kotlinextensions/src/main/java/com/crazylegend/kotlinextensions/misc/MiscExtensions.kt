package com.crazylegend.kotlinextensions.misc

import android.content.Context
import android.content.pm.FeatureInfo
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



/**
 * https://android.googlesource.com/platform/cts/+/2b87267/tests/tests/graphics/src/android/opengl/cts/OpenGlEsVersionTest.java
 */
fun Context.getOpenGLVersion(): Int {
    val featureInfos = packageManager.systemAvailableFeatures
    if (featureInfos.isNotEmpty()) {
        for (featureInfo in featureInfos) {
            // Null feature name means this feature is the open gl es version feature.
            if (featureInfo.name == null) {
                return if (featureInfo.reqGlEsVersion != FeatureInfo.GL_ES_VERSION_UNDEFINED) {
                    getMajorVersion(featureInfo.reqGlEsVersion)
                } else {
                    1 // Lack of property means OpenGL ES version 1
                }
            }
        }
    }
    return 1
}

/** @see FeatureInfo.getGlEsVersion
 */
private fun getMajorVersion(glEsVersion: Int): Int {
    return glEsVersion and -0x10000 shr 16
}

