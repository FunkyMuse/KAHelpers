package com.crazylegend.permissions

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract


/**
 * Created by crazy on 5/4/20 to long live and prosper !
 *
 *  private var enableLocationRetryCount = 1
private val enableLocation = registerForActivityResult(LocationSettingsContract()) {
if (enableLocationRetryCount <= 2) {
checkIfLocationAccessIsEnabled()
enableLocationRetryCount++
} else {
shortToast(R.string.enable_location_access)
}
}

 * function call         enableLocation(null)

 */
class LocationSettingsContract : ActivityResultContract<Nothing, Unit>() {

    override fun createIntent(context: Context, input: Nothing): Intent {
        return Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    }

    override fun parseResult(resultCode: Int, intent: Intent?) = Unit
}