package com.funkymuse.permissions

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract

class AccessibilityContract : ActivityResultContract<Nothing, Unit>() {
    override fun createIntent(context: Context, input: Nothing): Intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    override fun parseResult(resultCode: Int, intent: Intent?): Unit = Unit
}