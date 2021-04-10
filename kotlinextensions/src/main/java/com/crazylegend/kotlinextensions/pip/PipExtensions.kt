@file:Suppress("DEPRECATION")

package com.crazylegend.kotlinextensions.pip

import android.app.Activity
import android.app.PictureInPictureParams
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.crazylegend.kotlinextensions.activity.hasPipPermission
import com.crazylegend.kotlinextensions.activity.supportsPictureInPicture
import com.crazylegend.kotlinextensions.fragments.hasPipPermission
import com.crazylegend.kotlinextensions.fragments.supportsPictureInPicture
import com.crazylegend.kotlinextensions.intent.applicationDetailsIntent


/**
 * Created by crazy on 8/4/20 to long live and prosper !
 */


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
inline fun Fragment.enterPIPMode(
        builderActions: PictureInPictureParams.Builder.() -> Unit = {}) {
    if (supportsPictureInPicture) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val builder = PictureInPictureParams.Builder()
            builder.builderActions()
            requireActivity().enterPictureInPictureMode(builder.build())
        } else {
            requireActivity().enterPictureInPictureMode()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
inline fun Fragment.checkPIPPermissionAndEnter(
        onCantHandleAction: () -> Unit = {},
        builderActions: PictureInPictureParams.Builder.() -> Unit = {}) {
    if (!requireActivity().isInPictureInPictureMode) {
        if (supportsPictureInPicture) {
            if (hasPipPermission())
                enterPIPMode(builderActions)
            else {
                requireContext().applicationDetailsIntent(onCantHandleAction)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
inline fun Fragment.checkPIPPermissions(onPermissionDenied: () -> Unit = {}, onPermissionGranted: () -> Unit) {
    if (!requireActivity().isInPictureInPictureMode) {
        if (supportsPictureInPicture) {
            if (hasPipPermission())
                onPermissionGranted()
            else {
                onPermissionDenied()
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