package com.crazylegend.kotlinextensions.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.crazylegend.kotlinextensions.R


/**
 * Created by crazy on 2/6/19 to long live and prosper !
 */
class RateItDialogFragment : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setTitle("Rate us ?")
            .setMessage("If you're enjoying this application, kindly give us a rate and support us.")
            .setPositiveButton("Rate us") { _, _ ->
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + requireActivity().packageName)
                    )
                )

                getSharedPreferences(requireActivity()).edit(true) {
                    this.putBoolean(DISABLED, true)
                }
                dismiss()
            }
            .setNeutralButton(
                "Remind Me later"
            ) { _, _ -> dismiss() }
            .setNegativeButton("Don't show again") { _, _ ->
                getSharedPreferences(requireActivity()).edit(true) {
                    this.putBoolean(DISABLED, true)
                }
                dismiss()
            }.create()
    }

    companion object {
        private const val LAUNCHES_UNTIL_PROMPT = 10
        private const val DAYS_UNTIL_PROMPT = 3
        private const val MILLIS_UNTIL_PROMPT = DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000
        private const val PREF_NAME = "APP_RATER"
        private const val LAST_PROMPT = "LAST_PROMPT"
        private const val LAUNCHES = "LAUNCHES"
        private const val DISABLED = "DISABLED"

        fun show(context: Context, fragmentManager: FragmentManager) {
            var shouldShow = false
            val sharedPreferences = getSharedPreferences(context)
            val currentTime = System.currentTimeMillis()
            var lastPromptTime = sharedPreferences.getLong(LAST_PROMPT, 0)
            if (lastPromptTime == 0L) {
                lastPromptTime = currentTime
                sharedPreferences.edit(true) {
                    this.putLong(LAST_PROMPT, lastPromptTime)
                }
            }

            if (!sharedPreferences.getBoolean(DISABLED, false)) {
                val launches = sharedPreferences.getInt(LAUNCHES, 0) + 1
                if (launches > LAUNCHES_UNTIL_PROMPT) {
                    if (currentTime > lastPromptTime + MILLIS_UNTIL_PROMPT) {
                        shouldShow = true
                    }
                }
                sharedPreferences.edit(true) {
                    this.putInt(LAUNCHES, launches)
                }
            }

            if (shouldShow) {


                sharedPreferences.edit(true) {
                    this.putInt(LAUNCHES, 0).putLong(LAST_PROMPT, System.currentTimeMillis()).apply()
                }
                RateItDialogFragment().show(fragmentManager, null)

            }
        }

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREF_NAME, 0)
        }
    }
}