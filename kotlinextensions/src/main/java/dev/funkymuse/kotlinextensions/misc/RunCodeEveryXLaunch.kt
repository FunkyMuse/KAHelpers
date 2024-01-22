package dev.funkymuse.kotlinextensions.misc

import android.content.Context



object RunCodeEveryXLaunch {

    private const val defaultPrefKey = "RunCodeEveryXLaunch"
    private const val launchCountPref = "launchCount"
    private const val dateFirstLaunchPref = "dateFirLaunched"

    fun runCode(context: Context, launchesUntilRun: Int, prefKey: String = defaultPrefKey, codeToRun: () -> Unit) {

        val prefs = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE)

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