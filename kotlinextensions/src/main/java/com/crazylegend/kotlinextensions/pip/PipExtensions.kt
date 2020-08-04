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
fun Activity.enterPIPMode(builder: PictureInPictureParams.Builder.() -> PictureInPictureParams.Builder = { this }) {
    if (supportsPictureInPicture) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            enterPictureInPictureMode(PictureInPictureParams.Builder()
                    .builder()
                    .build())
        } else {
            enterPictureInPictureMode()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
fun Activity.checkPIPPermissionAndEnter(builder: PictureInPictureParams.Builder.() -> PictureInPictureParams.Builder = { this }) {
    if (!isInPictureInPictureMode) {
        if (supportsPictureInPicture) {
            if (hasPipPermission())
                enterPIPMode(builder)
            else {
                applicationDetailsIntent()
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.N)
fun Fragment.enterPIPMode(builder: PictureInPictureParams.Builder.() -> PictureInPictureParams.Builder = { this }) {
    if (supportsPictureInPicture) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().enterPictureInPictureMode(PictureInPictureParams.Builder()
                    .builder()
                    .build())
        } else {
            requireActivity().enterPictureInPictureMode()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
fun Fragment.checkPIPPermissionAndEnter(builder: PictureInPictureParams.Builder.() -> PictureInPictureParams.Builder = { this }) {
    if (!requireActivity().isInPictureInPictureMode) {
        if (supportsPictureInPicture) {
            if (hasPipPermission())
                enterPIPMode(builder)
            else {
                requireContext().applicationDetailsIntent()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
fun Fragment.checkPIPPermissions(onPermissionDenied: () -> Unit = {}, onPermissionGranted: () -> Unit) {
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
fun Activity.checkPIPPermissions(onPermissionDenied: () -> Unit = {}, onPermissionGranted: () -> Unit) {
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