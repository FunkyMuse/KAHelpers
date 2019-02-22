package com.crazylegend.kotlinextensions.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog


/**
 * Created by crazy on 2/7/19 to long live and prosper !
 */

object AppRaterDarkDarkTheme {


    fun app_launched(context: Context, APP_TITLE:String, DAYS_UNTIL_PROMPT:Int, LAUNCHES_UNTIL_PROMPT:Int, isDarkThemeEnabled: Boolean, @IdRes darkThemeColor:Int, @IdRes whiteThemeColor:Int) {
        val prefs = context.getSharedPreferences("apprater", 0)
        if (prefs.getBoolean("dontshowagain", false)) {
            return
        }


        val editor = prefs.edit()

        // Increment launch counter
        val launch_count = prefs.getLong("launch_count", 0) + 1
        editor.putLong("launch_count", launch_count)

        // Get date of first launch
        var date_firstLaunch: Long? = prefs.getLong("date_firstlaunch", 0)


        if (date_firstLaunch == 0L) {
            date_firstLaunch = System.currentTimeMillis()
            editor.putLong("date_firstlaunch", date_firstLaunch)
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch!! + DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000) {
                showRateDialog(context, editor, APP_TITLE, isDarkThemeEnabled, whiteThemeColor, darkThemeColor)
            }
        }

        editor.apply()
    }

    private fun showRateDialog(context: Context, editor: SharedPreferences.Editor?, APP_TITLE: String, isDarkThemeEnabled: Boolean, whiteThemeColor: Int, darkThemeColor: Int) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Rate $APP_TITLE")

        dialog.setMessage("If you're enjoy using $APP_TITLE, please take a moment to rate it. Thanks for your support!")

        dialog.setPositiveButton("Rate $APP_TITLE"){d, _ ->
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${context.packageName}")))
            d.dismiss()
        }

        dialog.setNegativeButton("Remind me later"){
            d, _ ->
            d.dismiss()
        }


        dialog.setNeutralButton("Don't show again"){
            d, _ ->
            if (editor != null) {
                editor.putBoolean("dontshowagain", true)
                editor.commit()
            }
            d.dismiss()
        }


        if (isDarkThemeEnabled) {
            dialog.show().window?.setBackgroundDrawableResource(darkThemeColor)

        } else {
            dialog.show().window?.setBackgroundDrawableResource(whiteThemeColor)
        }

    }
}