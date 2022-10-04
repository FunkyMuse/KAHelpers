package com.crazylegend.permissions

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */

class AccessibilityContract : ActivityResultContract<Nothing, Unit>() {
    override fun createIntent(context: Context, input: Nothing): Intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    override fun parseResult(resultCode: Int, intent: Intent?): Unit = Unit
}