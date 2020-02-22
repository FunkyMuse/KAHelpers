package com.crazylegend.kotlinextensions.views

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.crazylegend.kotlinextensions.R
import com.crazylegend.kotlinextensions.context.getSharedPreferencesByTag
import com.crazylegend.kotlinextensions.fragments.removeFragmentWithStateLoss
import com.crazylegend.kotlinextensions.intent.canBeHandled
import com.crazylegend.kotlinextensions.sharedprefs.putBoolean
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.dialog_app_rater.view.*


/**
 * Created by CrazyLegenD on 2/7/19 to long live and prosper !
 *
 * Modified on 21/01/2020
 */

/**
 * USAGE

AppRater.appLaunched(this, supportFragmentManager, 0, 0){
appTitle = getString(R.string.app_name)
buttonsBGColor = getCompatColor(R.color.colorAccent)
}
 */
object AppRater {

    private const val prefKey = "appRater"
    private const val doNotShowAgainPref = "doNotShowAgain"
    private const val launchCountPref = "launchCount"
    private const val dateFirstLaunchPref = "dateFirLaunched"
    private lateinit var appRaterDialog: AppRaterDialog

    fun appLaunched(context: Context, fragmentManager: FragmentManager, DAYS_UNTIL_PROMPT: Int, LAUNCHES_UNTIL_PROMPT: Int,
                    appRaterModelSetup: AppRaterModelSetup.() -> Unit = {}) {
        val modelToModify = AppRaterModelSetup()
        appRaterModelSetup.invoke(modelToModify)

        val prefs = context.getSharedPreferencesByTag(prefKey)
        if (prefs.getBoolean(doNotShowAgainPref, false)) {
            return
        }

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
        if (launchCount >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= dateFirstLaunch + DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000) {
                showRateDialog(modelToModify, fragmentManager)
            }
        }

        editor.apply()
    }

    private fun showRateDialog(message: AppRaterModelSetup, fragmentManager: FragmentManager) {
        fragmentManager.findFragmentByTag(DIALOG_TAG)?.apply {
            fragmentManager.removeFragmentWithStateLoss(this)
        }
        appRaterDialog = AppRaterDialog()
        appRaterDialog.addArguments(message)
        appRaterDialog.show(fragmentManager, DIALOG_TAG)
    }

    @Parcelize
    class AppRaterModelSetup(
            var appTitle: String = "Rate my app",
            var content: String = "If you're enjoying using this application, please take a moment to rate it.\nThanks for your support !",
            var buttonsCornerRadius: Int? = null,
            var contentTextSize: Float? = null,
            var rateMeButtonText: String? = null,
            var doNotShowAgainButtonText: String? = null,
            var remindMeLaterButtonText: String? = null,
            var backgroundButtonsResource: Int? = null, //use 0 to remove background and also removes the button too
            var buttonsBGColor: Int? = null) : Parcelable {


        operator fun invoke(callback: AppRaterModelSetup.() -> Unit = {}) {
            callback.invoke(this)
        }
    }

    private const val DIALOG_TAG = "rateDialogTag"
    private const val argumentModel = "argument"

    class AppRaterDialog : DialogFragment() {

        private val sharedPreferences by lazy { requireContext().getSharedPreferencesByTag(prefKey) }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.dialog_app_rater, container, false)

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val argumentModel: AppRaterModelSetup? = arguments?.getParcelable(argumentModel)
            argumentModel ?: return
            view.dar_content?.setPrecomputedText(argumentModel.content)
            view.dar_title?.setPrecomputedText(argumentModel.appTitle)

            argumentModel.contentTextSize?.apply {
                view.dar_content?.textSize = this
            }

            argumentModel.buttonsBGColor?.apply {
                view.dar_rate?.setBackgroundColor(this)
                view.dar_doNotShowAgain?.setBackgroundColor(this)
                view.dar_remindMeLater?.setBackgroundColor(this)
            }

            view.dar_rate?.text = argumentModel.rateMeButtonText ?: "Rate"
            view.dar_doNotShowAgain?.text = argumentModel.doNotShowAgainButtonText
                    ?: "Don't show again"
            view.dar_remindMeLater?.text = argumentModel.remindMeLaterButtonText
                    ?: "Remind me later"

            argumentModel.backgroundButtonsResource?.apply {
                view.dar_rate?.setBackgroundResource(this)
                view.dar_doNotShowAgain?.setBackgroundResource(this)
                view.dar_remindMeLater?.setBackgroundResource(this)
            }

            argumentModel.buttonsCornerRadius?.apply {
                view.dar_rate?.cornerRadius = this
                view.dar_doNotShowAgain?.cornerRadius = this
                view.dar_remindMeLater?.cornerRadius = this
            }

            view.dar_rate?.setOnClickListenerCooldown {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${requireContext().packageName}"))
                if (intent.canBeHandled(requireContext())) {
                    requireContext().startActivity(intent)
                }
                dismissAllowingStateLoss()
            }


            view.dar_doNotShowAgain?.setOnClickListenerCooldown {
                sharedPreferences.putBoolean(doNotShowAgainPref, true)
                dismissAllowingStateLoss()
            }

            view.dar_remindMeLater?.setOnClickListenerCooldown {
                dismissAllowingStateLoss()
            }
        }

        fun addArguments(appRaterModelSetup: AppRaterModelSetup) {
            arguments = bundleOf(Pair(argumentModel, appRaterModelSetup))
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val dialog = super.onCreateDialog(savedInstanceState)
            with(dialog) {
                window?.attributes?.windowAnimations = R.style.DialogAnimation
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                window?.setBackgroundDrawableResource(R.drawable.rounded_bg_theme_compatible)
            }
            return dialog
        }

    }
}