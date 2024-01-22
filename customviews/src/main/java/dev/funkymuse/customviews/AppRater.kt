package dev.funkymuse.customviews

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import dev.funkymuse.customviews.AppRater.AppRaterModelSetup.Companion.DEFAULT_APP_TITLE
import dev.funkymuse.customviews.AppRater.AppRaterModelSetup.Companion.DEFAULT_CONTENT
import dev.funkymuse.setofusefulkotlinextensions.customviews.R
import dev.funkymuse.setofusefulkotlinextensions.customviews.databinding.DialogAppRaterBinding
import dev.funkymuse.viewbinding.viewBinding


/**
 * USAGE

AppRater.appLaunched(this, supportFragmentManager, 0, 0){
appTitle = getString(R.string.app_name)
buttonsBGColor = getCompatColor(R.color.colorAccent)
}
 */
object AppRater {

    private const val appRaterModelSetupKey = "AppRaterModelSetupKey"
    private const val prefKey = "appRater"
    private const val doNotShowAgainPref = "doNotShowAgain"
    private const val launchCountPref = "launchCount"
    private const val dateFirstLaunchPref = "dateFirLaunched"
    private lateinit var appRaterDialog: AppRaterDialog

    fun appLaunched(
        context: Context,
        fragmentManager: FragmentManager,
        DAYS_UNTIL_PROMPT: Int,
        LAUNCHES_UNTIL_PROMPT: Int,
        appRaterModelSetup: AppRaterModelSetup.() -> Unit = {}
    ) {
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

    class AppRaterModelSetup(
        var appTitle: String = "Rate my app",
        var content: String = "If you're enjoying using this application, please take a moment to rate it.\nThanks for your support !",
        var buttonsCornerRadius: Int? = null,
        var contentTextSize: Float? = null,
        var rateMeButtonText: String? = null,
        var doNotShowAgainButtonText: String? = null,
        var remindMeLaterButtonText: String? = null,
        var backgroundButtonsResource: Int? = null, //use 0 to remove background and also removes the button too
        var buttonsBGColor: Int? = null
    ) {
        companion object {
            const val DEFAULT_APP_TITLE = "Rate my app"
            const val DEFAULT_CONTENT =
                "If you're enjoying using this application, please take a moment to rate it.\nThanks for your support !"
        }

        operator fun invoke(callback: AppRaterModelSetup.() -> Unit = {}) {
            callback.invoke(this)
        }
    }

    private const val DIALOG_TAG = "rateDialogTag"
    private const val argumentModel = "argument"

    class AppRaterDialog : DialogFragment(R.layout.dialog_app_rater) {

        private val sharedPreferences by lazy { requireContext().getSharedPreferencesByTag(prefKey) }

        private val binding by viewBinding(DialogAppRaterBinding::bind)

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val argumentModel = arguments?.run {
                AppRaterModelSetup(
                    appTitle = getString(appRaterModelSetupKey + "appTitle", DEFAULT_APP_TITLE),
                    content = getString(appRaterModelSetupKey + "content", DEFAULT_CONTENT),
                    buttonsCornerRadius = runCatching { getInt(appRaterModelSetupKey + "buttonsCornerRadius") }.getOrNull(),
                    buttonsBGColor = runCatching { getInt(appRaterModelSetupKey + "buttonsBGColor") }.getOrNull(),
                    backgroundButtonsResource = runCatching { getInt(appRaterModelSetupKey + "backgroundButtonsResource") }.getOrNull(),
                    contentTextSize = runCatching { getFloat(appRaterModelSetupKey + "contentTextSize") }.getOrNull(),
                    rateMeButtonText = getString(appRaterModelSetupKey + "rateMeButtonText", null),
                    doNotShowAgainButtonText = getString(
                        appRaterModelSetupKey + "doNotShowAgainButtonText",
                        null
                    ),
                    remindMeLaterButtonText = getString(
                        appRaterModelSetupKey + "remindMeLaterButtonText",
                        null
                    ),
                )
            } ?: return

            binding.content.text = (argumentModel.content)
            binding.title.text = (argumentModel.appTitle)

            argumentModel.contentTextSize?.apply {
                binding.content.textSize = this
            }

            argumentModel.buttonsBGColor?.apply {
                binding.rate.setBackgroundColor(this)
                binding.doNotShowAgain.setBackgroundColor(this)
                binding.remindMeLater.setBackgroundColor(this)
            }

            binding.rate.text = argumentModel.rateMeButtonText ?: "Rate"
            binding.doNotShowAgain.text = argumentModel.doNotShowAgainButtonText
                ?: "Don't show again"
            binding.remindMeLater.text = argumentModel.remindMeLaterButtonText
                ?: "Remind me later"

            argumentModel.backgroundButtonsResource?.apply {
                binding.rate.setBackgroundResource(this)
                binding.doNotShowAgain.setBackgroundResource(this)
                binding.remindMeLater.setBackgroundResource(this)
            }

            argumentModel.buttonsCornerRadius?.apply {
                binding.rate.cornerRadius = this
                binding.doNotShowAgain.cornerRadius = this
                binding.remindMeLater.cornerRadius = this
            }

            binding.rate.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${requireContext().packageName}")
                )

                if (intent.resolveActivity(requireContext().packageManager) != null) {
                    requireContext().startActivity(intent)
                }
                dismissAllowingStateLoss()
            }


            binding.doNotShowAgain.setOnClickListener {
                sharedPreferences.putBoolean(doNotShowAgainPref, true)
                dismissAllowingStateLoss()
            }

            binding.remindMeLater.setOnClickListener {
                dismissAllowingStateLoss()
            }
        }

        fun addArguments(appRaterModelSetup: AppRaterModelSetup) {
            arguments = bundleOf(Pair(argumentModel, appRaterModelSetup))
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val dialog = super.onCreateDialog(savedInstanceState)
            with(dialog) {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                window?.setBackgroundDrawableResource(R.drawable.rounded_bg_theme_compatible)
            }
            return dialog
        }

    }

}