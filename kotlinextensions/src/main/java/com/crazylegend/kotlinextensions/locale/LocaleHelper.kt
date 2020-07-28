package com.crazylegend.kotlinextensions.locale

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.preference.PreferenceManager
import java.util.*


/**
 * Created by Hristijan on 11/6/18 to live long and prosper.
 */

/**
 *
 * In your application level implementation
class MainApplication : Application() {
override fun attachBaseContext(base: Context) {
super.attachBaseContext(LocaleHelper.onAttach(base, "en"))
}

 * Call this function when you need to set the locale [LocaleHelper.setLocale] like LocaleHelper.setLocale(context, "en")
 * Make sure to re-create the activity once you call this function, preferably use a base(abstract) activity that has the
 * [LocaleHelper.onAttach] so that all of your functions inherit from it, to ease your problems.

 * In your activity override this function
override fun attachBaseContext(newBase: Context?) {
super.attachBaseContext(newBase?.let { LocaleHelper.onAttach(it) })

 * If you're using Appcompat delegate or setting the locale on pre < API 24 devices make sure to use this override in activity
 * since they haven't fixed the bug in ages
 *
override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
if (overrideConfiguration != null) {
val uiMode = overrideConfiguration.uiMode
overrideConfiguration.setTo(baseContext.resources.configuration)
overrideConfiguration.uiMode = uiMode
}
super.applyOverrideConfiguration(overrideConfiguration)
}

 * When this gets released
 * https://android.googlesource.com/platform/frameworks/support/+/67f756b383344764f7d3539c5955fbe5f491a3ba
 * you can remove the applyOverrideConfiguration
}

 */
object LocaleHelper {

    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

    fun onAttach(context: Context): Context {
        val lang = getPersistedData(context, Locale.getDefault().language)
        return setLocale(context, lang)
    }

    fun onAttach(context: Context, defaultLanguage: String): Context {
        val lang = getPersistedData(context, defaultLanguage)
        return setLocale(context, lang)
    }

    fun getLanguage(context: Context): String? {
        return getPersistedData(context, Locale.getDefault().language)
    }

    fun setLocale(context: Context, language: String?): Context {
        persist(context, language)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else updateResourcesLegacy(context, language)

    }

    private fun getPersistedData(context: Context, defaultLanguage: String): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage)
    }

    private fun persist(context: Context, language: String?) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()

        editor.putString(SELECTED_LANGUAGE, language)
        editor.apply()
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String?): Context {
        val locale = Locale(language.toString())
        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }

    @Suppress("DEPRECATION")
    private fun updateResourcesLegacy(context: Context, language: String?): Context {
        val locale = Locale(language.toString())
        Locale.setDefault(locale)

        val resources = context.resources

        val configuration = resources.configuration
        configuration.locale = locale
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

        return context
    }

    fun getLocalizedString(context: Context, resourceId: Int): String? {
        val savedLanguage = getLanguage(context) ?: return null
        val requestedLocale = Locale(savedLanguage)
        val config = Configuration(context.resources.configuration)
        config.setLocale(requestedLocale)
        return context.createConfigurationContext(config).getText(resourceId).toString()
    }

    fun getLocalizedResources(context: Context): Resources? {
        val savedLanguage = getLanguage(context) ?: return null
        val requestedLocale = Locale(savedLanguage)
        val config = Configuration(context.resources.configuration)
        config.setLocale(requestedLocale)
        return context.createConfigurationContext(config).resources
    }

}
