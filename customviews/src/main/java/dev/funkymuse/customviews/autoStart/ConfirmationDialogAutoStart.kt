package dev.funkymuse.customviews.autoStart

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import dev.funkymuse.setofusefulkotlinextensions.customviews.R
import dev.funkymuse.setofusefulkotlinextensions.customviews.databinding.DialogConfirmationAutoStartHelperBinding
import dev.funkymuse.viewbinding.viewBinding


class ConfirmationDialogAutoStart : DialogFragment(R.layout.dialog_confirmation_auto_start_helper) {

    override fun onCreateDialog(savedInstanceState: Bundle?) = with(super.onCreateDialog(savedInstanceState)) {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        window?.setBackgroundDrawableResource(R.drawable.rounded_bg_theme_compatible)
        this
    }

    private val binding by viewBinding(DialogConfirmationAutoStartHelperBinding::bind)

    companion object {
        const val REQ_KEY = "requestKey"
        const val RESULT_KEY = "resultKey"
        const val DO_NOT_SHOW_AGAIN_RESULT_KEY = "doNotShowAgainResult"
        const val TEXT_FIELD = "text"
        const val CANCEL_TEXT = "cancelText"
        const val CONFIRM_TEXT = "confirmText"
        const val DO_NOT_SHOW_AGAIN_VISIBILITY = "doNotShowAgainVISIBILITY"
        const val DO_NOT_SHOW_AGAIN_TEXT = "doNotShowAgainText"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.apply {
            binding.dialogText.text = getString(TEXT_FIELD, "Please enable auto-start ability for this application.")
            binding.cancelButton.text = getString(CANCEL_TEXT, "Cancel")
            binding.confirmButton.text = getString(CONFIRM_TEXT, "Submit")
            val doNotShowAgainVisibility = getBoolean(DO_NOT_SHOW_AGAIN_VISIBILITY, false)
            binding.dontShowAgain.visibleIfTrueGoneOtherwise(doNotShowAgainVisibility)
            if (doNotShowAgainVisibility) {
                binding.dontShowAgain.text = getString(DO_NOT_SHOW_AGAIN_TEXT, "Do not show again")
            }
        }

        binding.cancelButton.setOnClickListener {
            setFragmentResult(REQ_KEY, bundleOf(RESULT_KEY to false, DO_NOT_SHOW_AGAIN_RESULT_KEY to binding.dontShowAgain.isChecked))
            dismissAllowingStateLoss()
        }

        binding.confirmButton.setOnClickListener {
            setFragmentResult(REQ_KEY, bundleOf(RESULT_KEY to true, DO_NOT_SHOW_AGAIN_RESULT_KEY to binding.dontShowAgain.isChecked))
            dismissAllowingStateLoss()
        }
    }

    private fun View.visibleIfTrueGoneOtherwise(condition: Boolean) {
        visibility = if (condition) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}