@file:Suppress("DEPRECATION")

package dev.funkymuse.kotlinextensions.pip

import android.app.Activity
import android.app.PictureInPictureParams
import android.os.Build
import androidx.annotation.RequiresApi
import dev.funkymuse.activity.hasPipPermission
import dev.funkymuse.activity.supportsPictureInPicture
import dev.funkymuse.intent.applicationDetailsIntent





@RequiresApi(Build.VERSION_CODES.N)
inline fun Activity.enterPIPMode(builderActions: PictureInPictureParams.Builder.() -> Unit = {}) {
    if (supportsPictureInPicture) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           val builder = PictureInPictureParams.Builder()
            builder.builderActions()
            enterPictureInPictureMode(builder.build())
        } else {
            enterPictureInPictureMode()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
inline fun Activity.checkPIPPermissionAndEnter(
        onCantHandleAction: () -> Unit = {},
        noinline builderActions: PictureInPictureParams.Builder.() -> Unit = {}) {
    if (!isInPictureInPictureMode) {
        if (supportsPictureInPicture) {
            if (hasPipPermission())
                enterPIPMode(builderActions)
            else {
                applicationDetailsIntent(onCantHandleAction)
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.N)
inline fun Activity.checkPIPPermissions(onPermissionDenied: () -> Unit = {}, onPermissionGranted: () -> Unit) {
    if (!isInPictureInPictureMode) {
        if (supportsPictureInPicture) {
            if (hasPipPermission())
                onPermissionGranted()
            else {
                onPermissionDenied()
            }
        }
    }
}