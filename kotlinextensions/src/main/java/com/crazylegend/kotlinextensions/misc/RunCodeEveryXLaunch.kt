package com.crazylegend.kotlinextensions.misc

import android.content.Context
import com.crazylegend.kotlinextensions.context.getSharedPreferencesByTag


/**
 * Created by crazy on 8/13/20 to long live and prosper !
 */
object RunCodeEveryXLaunch {

    private const val defaultPrefKey = "RunCodeEveryXLaunch"
    private const val launchCountPref = "launchCount"
    private const val dateFirstLaunchPref = "dateFirLaunched"

    fun runCode(context: Context, launchesUntilRun: Int, prefKey: String = defaultPrefKey, codeToRun: () -> Unit) {

        val prefs = context.getSharedPreferencesByTag(prefKey)

        val editor = prefs.edit()

        // Increment launch counter
        val launchCount = prefs.getLong(launchCountPref, 0) + 1
        editor.putLong(launchCountPref, launchCount)

        // Get date of first launch
        var dateFirstLaunch = prefs.getLong(dateFirstLaunchPref, 0)

        if (dateFirstLaunch == 0L) {
            dateFirstLaunch = System.currentTimeMillis()
            editor.putLong(dateFirstLaunchPref, dateFirstLaunch)
        }

        // Wait at least n days before opening
        if (launchCount >= launchesUntilRun) {
            if (System.currentTimeMillis() >= dateFirstLaunch) {
                codeToRun()
                //reset launch count and date of first launch
                editor.putLong(launchCountPref, 0)
                editor.putLong(dateFirstLaunchPref, 0)
            }
        }

        editor.apply()
    }
}