package com.crazylegend.kotlinextensions.views

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AlertDialog


/**
 * Created by CrazyLegenD on 2/7/19 to long live and prosper !
 */

/*
USAGE
AppRater.app_launched(this, getString(R.string.app_name), 0, 1)*/


object AppRater {


    fun app_launched(context: Context, APP_TITLE:String, DAYS_UNTIL_PROMPT:Int, LAUNCHES_UNTIL_PROMPT:Int, message:String?=null) {
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
                showRateDialog(context, editor, APP_TITLE, message)
            }
        }

        editor.apply()
    }

    private fun showRateDialog(context: Context, editor: SharedPreferences.Editor?, APP_TITLE: String, message: String?) {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialog = dialogBuilder.create()
        dialog.setTitle("Rate $APP_TITLE")

        dialog.setMessage(message?:"If you're enjoy using $APP_TITLE, please take a moment to rate it. Thanks for your support!")

        dialog.setButton(Dialog.BUTTON_POSITIVE, "Rate $APP_TITLE"){
            dialog, _ ->
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${context.packageName}")))
            dialog.dismiss()
        }


        dialog.setButton(Dialog.BUTTON_NEUTRAL, "Remind me later"){
         dialog, _ ->
            dialog.dismiss()
        }

        dialog.setButton(Dialog.BUTTON_NEGATIVE, "Don't show again"){
            dialog, _ ->
            if (editor != null) {
                editor.putBoolean("dontshowagain", true)
                editor.commit()
            }
            dialog.dismiss()
        }

        dialog.show()
    }
}