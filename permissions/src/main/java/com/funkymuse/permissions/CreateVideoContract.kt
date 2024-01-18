package com.funkymuse.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract


/**
private val registerDocumentContract = registerForActivityResult(CreateVideo()) {
if (it == null) {
//Operation cancelled or no application to handle the action!
} else {
val uri = it
}
}

call it as tryOrIgnore { registerDocumentContract.launch(videoName.mp4) }
it's wrapped in try or ignore in case no app can handle the intent
 */
class CreateVideoContract(private val videoType :String = "video/*") : ActivityResultContract<String?, Uri?>() {

    override fun createIntent(context: Context, input: String?): Intent {
        return Intent(Intent.ACTION_CREATE_DOCUMENT)
                .setType(videoType)
                .putExtra(Intent.EXTRA_TITLE, input)
    }

    override fun getSynchronousResult(context: Context, input: String?) = null

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (intent == null || resultCode != Activity.RESULT_OK) null else intent.data
    }
}