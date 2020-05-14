package com.crazylegend.kotlinextensions.permissionHandlers.coroutines


/**
 * Created by hristijan on 6/17/19 to long live and prosper !
 */
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.crazylegend.kotlinextensions.permissionHandlers.BasePermissionManager
import com.crazylegend.kotlinextensions.permissionHandlers.PermissionResult
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 *
 *
.
.
.
launch {
//CoroutineScope

val permissionResult = PermissionManager.requestPermissions(           //Suspends the coroutine
this@Fragment,
REQUEST_ID,
Manifest.permission.ACCESS_FINE_LOCATION,
Manifest.permission.READ_CONTACTS,
Manifest.permission.CAMERA
)

//Resume coroutine once result is ready
when(permissionResult) {
is PermissionResult.PermissionGranted -> {
//Add your logic here after user grants permission(s)
}
is PermissionResult.PermissionDenied -> {
//Add your logic to handle permission denial
}
is PermissionResult.PermissionDeniedPermanently -> {
//Add your logic here if user denied permission(s) permanently.
//Ideally you should ask user to manually go to settings and enable permission(s)
}
is PermissionResult.ShowRationale -> {
//If user denied permission frequently then she/he is not clear about why you are asking this permission.
//This is your chance to explain them why you need permission.
}
}

}

 *
 *
 */

/**
 * Permission manager which handles checking permission is granted or not and if not then will request permission.
 * This is nothing but a headless fragment which wraps the boilerplate code for checking and requesting permission
 * and suspends the coroutines until result is available.
 * A simple [Fragment] subclass.
 */
@Deprecated("This is gonna be removed in the next release", ReplaceWith("ActivityResultContract"))
class PermissionCouroutineManager : BasePermissionManager() {

    private var completableDeferred: CompletableDeferred<PermissionResult>? = null

    override fun onPermissionResult(permissionResult: PermissionResult) {
        completableDeferred?.complete(permissionResult)
    }

    companion object {

        private const val TAG = "PermissionManager"

        /**
         * A static factory method to request permission from activity.
         *
         * @param activity an instance of [AppCompatActivity]
         * @param requestId Request ID for permission request
         * @param permissions Permission(s) to request
         *
         * @return [PermissionResult]
         *
         * Suspends the coroutines until result is available.
         */
        suspend fun requestPermissions(
                activity: AppCompatActivity,
                requestId: Int,
                vararg permissions: String
        ): PermissionResult? {
            return withContext(Dispatchers.Main) {
                return@withContext _requestPermissions(
                        activity,
                        requestId,
                        *permissions
                )
            }
        }

        /**
         * A static factory method to request permission from fragment.
         *
         * @param fragment an instance of [Fragment]
         * @param requestId Request ID for permission request
         * @param permissions Permission(s) to request
         *
         * @return [PermissionResult]
         *
         * Suspends the coroutines until result is available.
         */
        suspend fun requestPermissions(
                fragment: Fragment,
                requestId: Int,
                vararg permissions: String
        ): PermissionResult? {
            return withContext(Dispatchers.Main) {
                return@withContext _requestPermissions(
                        fragment,
                        requestId,
                        *permissions
                )
            }
        }

        private suspend fun _requestPermissions(
                activityOrFragment: Any,
                requestId: Int,
                vararg permissions: String
        ): PermissionResult? {
            val fragmentManager = if (activityOrFragment is AppCompatActivity) {
                activityOrFragment.supportFragmentManager
            } else {
                (activityOrFragment as Fragment).childFragmentManager
            }

            return if (fragmentManager.findFragmentByTag(TAG) != null) {
                val permissionManager = fragmentManager.findFragmentByTag(TAG) as PermissionCouroutineManager
                permissionManager.completableDeferred = CompletableDeferred()
                permissionManager.requestPermissions(
                        requestId,
                        *permissions
                )
                if (permissionManager.completableDeferred == null) {
                    null
                } else {
                    permissionManager.completableDeferred?.await()
                }
            } else {
                val permissionManager = PermissionCouroutineManager().apply {
                    completableDeferred = CompletableDeferred()
                }
                fragmentManager.beginTransaction().add(
                        permissionManager,
                        TAG
                ).commitNowAllowingStateLoss()
                permissionManager.requestPermissions(requestId, *permissions)
                return if (permissionManager.completableDeferred == null) {
                    null
                } else {
                    permissionManager.completableDeferred?.await()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        completableDeferred?.apply {
            if (isActive) {
                cancel()
            }
        }

    }
}



