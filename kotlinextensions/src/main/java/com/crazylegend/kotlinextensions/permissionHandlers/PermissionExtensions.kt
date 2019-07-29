package com.crazylegend.kotlinextensions.permissionHandlers

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.crazylegend.kotlinextensions.context.shortToast
import com.crazylegend.kotlinextensions.coroutines.mainCoroutine
import com.crazylegend.kotlinextensions.exhaustive
import com.crazylegend.kotlinextensions.fragments.finish
import com.crazylegend.kotlinextensions.fragments.shortToast
import com.crazylegend.kotlinextensions.permissionHandlers.coroutines.PermissionCouroutineManager
import com.google.android.material.snackbar.Snackbar


/**
 * Created by hristijan on 7/29/19 to long live and prosper !
 */


private const val SINGLE_ID_PERMISSION_ACTIVITY = 111

fun AppCompatActivity.checkSinglePermission(permissionName: String, rationaleText: String = "", actionOnGranted: () -> Unit = {}) {

    mainCoroutine {
        val permissionResult =
                PermissionCouroutineManager.requestPermissions(this, SINGLE_ID_PERMISSION_ACTIVITY, permissionName)

        when (permissionResult) {
            is PermissionResult.PermissionGranted -> {
                actionOnGranted()
            }
            is PermissionResult.PermissionDenied -> {
                retrySnackbar {
                    checkSinglePermission(permissionName)
                }
            }
            is PermissionResult.ShowRationale -> {
                checkSinglePermission(permissionName)
            }
            is PermissionResult.PermissionDeniedPermanently -> {
                shortToast(rationaleText)
            }
        }
    }
}


fun Fragment.checkSinglePermission(permissionName: String, rationaleText: String = "", actionOnGranted: () -> Unit = {}) {

    mainCoroutine {
        val permissionResult =
                PermissionCouroutineManager.requestPermissions(this, SINGLE_ID_PERMISSION_ACTIVITY, permissionName)

        when (permissionResult) {
            is PermissionResult.PermissionGranted -> {
                actionOnGranted()
            }
            is PermissionResult.PermissionDenied -> {
                requireActivity().retrySnackbar {
                    checkSinglePermission(permissionName)
                }
            }
            is PermissionResult.ShowRationale -> {
                checkSinglePermission(permissionName)
            }
            is PermissionResult.PermissionDeniedPermanently -> {
                shortToast(rationaleText)
            }
        }
    }
}

private const val MULTIPLE_ID_PERMISSION_ACTIVITY = 111

fun AppCompatActivity.checkMultiplePermissions(
        vararg permissions: String,
        shouldFinishActivityOnPermissionDeny: Boolean = false,
        rationaleText: String = "",
        actionOnGranted: () -> Unit = {}
) {

    mainCoroutine {
        val permissionResult =
                PermissionCouroutineManager.requestPermissions(
                        this,
                        MULTIPLE_ID_PERMISSION_ACTIVITY,
                        *permissions
                )

        when (permissionResult) {
            is PermissionResult.PermissionGranted -> {
                actionOnGranted()
            }
            is PermissionResult.PermissionDenied -> {
                retrySnackbar {
                    checkMultiplePermissions()
                }
            }
            is PermissionResult.ShowRationale -> {
                checkMultiplePermissions()
            }
            is PermissionResult.PermissionDeniedPermanently -> {
                shortToast(rationaleText)
                if (shouldFinishActivityOnPermissionDeny) {
                    finish()
                }
            }
        }
    }
}

fun Fragment.checkMultiplePermissions(
        vararg permissions: String, shouldFinishActivityOnPermissionDeny: Boolean = false,
        rationaleText: String = "",
        actionOnGranted: () -> Unit = {}
) {
    mainCoroutine {
        val permissionResult =
                PermissionCouroutineManager.requestPermissions(
                        this,
                        MULTIPLE_ID_PERMISSION_ACTIVITY,
                        *permissions
                )

        when (permissionResult) {
            is PermissionResult.PermissionGranted -> {
                actionOnGranted()
            }
            is PermissionResult.PermissionDenied -> {
                requireActivity().retrySnackbar {
                    checkMultiplePermissions()
                }
            }
            is PermissionResult.ShowRationale -> {
                checkMultiplePermissions()
            }
            is PermissionResult.PermissionDeniedPermanently -> {
                shortToast(rationaleText)

                if (shouldFinishActivityOnPermissionDeny) {
                    finish()
                }
            }
        }
    }
}

/**
 * Retry Snackbar for permissions
 * @receiver FragmentActivity
 * @param snackText String
 * @param retryText String
 * @param length Int
 * @param action Function0<Unit>
 */
fun FragmentActivity.retrySnackbar(snackText: String = "", retryText: String = "Retry ?", length: Int = Snackbar.LENGTH_INDEFINITE, action: () -> Unit) {
    val snackBar = Snackbar.make(
            findViewById(android.R.id.content),
            snackText,
            length
    )
    snackBar.show()
    snackBar.setAction(retryText) {
        action()
        snackBar.dismiss()
    }
}


/**

lifecycleScope.launch(Dispatchers.Main) {
PermissionCouroutineManager.requestPermissions(this@FRAGMENT/APPCOMPATACTIVITY, REQUEST_ID, PERMISSION_NAME)
.handlePermissions({
// Permission Permanently Denied
requestId, permanentlyDeniedPermissions ->


}, {
// Permission Denied

}, {
// Show Rationale

}, {
// Permission Granted
})


}

 * @receiver PermissionResult
 * @param onPermissionPermanentlyDenied Function2<[@kotlin.ParameterName] Int, [@kotlin.ParameterName] List<String>, Unit>
 * @param onPermissionDenied Function1<[@kotlin.ParameterName] Int, Unit>
 * @param onShouldShowRationale Function1<[@kotlin.ParameterName] Int, Unit>
 * @param onPermissionGranted Function1<[@kotlin.ParameterName] Int, Unit>
 */
fun PermissionResult.handlePermissions(
        onPermissionPermanentlyDenied: (requestId: Int, permanentlyDeniedPermissions: List<String>) -> Unit = { _, _ -> },
        onPermissionDenied: (requestId: Int) -> Unit = { _ -> },
        onShouldShowRationale: (requestId: Int) -> Unit = { _ -> },
        onPermissionGranted: (requestId: Int) -> Unit = { _ -> }
) {
    when (this) {
        is PermissionResult.PermissionGranted -> {
            onPermissionGranted(requestId)
        }
        is PermissionResult.PermissionDenied -> {
            onPermissionDenied(requestId)
        }
        is PermissionResult.ShowRationale -> {
            onShouldShowRationale(requestId)
        }
        is PermissionResult.PermissionDeniedPermanently -> {
            onPermissionPermanentlyDenied(requestId, permanentlyDeniedPermissions)
        }
    }.exhaustive
}
