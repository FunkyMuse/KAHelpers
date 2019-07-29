package com.crazylegend.kotlinextensions.permissionHandlers

import android.arch.lifecycle.LiveData
import android.content.Context
import android.support.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


/**
 * Interface definition for a callback to get [LiveData] of [PermissionResult]
 *
interface PermissionObserver {
fun setupObserver(permissionResultLiveData: LiveData<PermissionResult>)
}

 * Implement this interface to get [LiveData] for observing permission request result.
 */

/**

override fun setupObserver(permissionResultLiveData: LiveData<PermissionResult>) {
permissionResultLiveData.observe(this, Observer<PermissionResult> {
when (it) {
is PermissionResult.PermissionGranted -> {
if (it.requestId == REQUEST_ID) {
//Add your logic here after user grants permission(s)
}
}
is PermissionResult.PermissionDenied -> {
if (it.requestId == REQUEST_ID) {
//Add your logic to handle permission denial
}
}
is PermissionResult.PermissionDeniedPermanently -> {
if (it.requestId == REQUEST_ID) {
//Add your logic here if user denied permission(s) permanently.
//Ideally you should ask user to manually go to settings and enable permission(s)
}
}
is PermissionResult.ShowRational -> {
if (it.requestId == REQUEST_ID) {
//If user denied permission frequently then she/he is not clear about why you are asking this permission.
//This is your chance to explain them why you need permission.
}
}
}
})
}


 *
 */



/**
 * Created by hristijan on 6/17/19 to long live and prosper !
 */

class PermissionManager : BasePermissionManager() {

    private val permissionResultLiveEvent: SingleLiveEvent<PermissionResult> by lazy {
        SingleLiveEvent()
    }

    override fun onPermissionResult(permissionResult: PermissionResult) {
        permissionResultLiveEvent.postValue(permissionResult)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment != null) {
            (parentFragment as PermissionObserver).setupObserver(permissionResultLiveEvent)
        } else {
            (context as PermissionObserver).setupObserver(permissionResultLiveEvent)
        }
    }


    companion object {

        private const val TAG = "PermissionManager"

        /**
         * A static factory method to request permission from activity.
         * Your activity must implement [PermissionObserver]
         *
         * @param activity an instance of [AppCompatActivity] which is also [PermissionObserver]
         * @param requestId Request ID for permission request
         * @param permissions Permission(s) to request
         *
         * @throws [IllegalArgumentException] if your activity doesn't implement [PermissionObserver]
         */
        @JvmStatic
        @MainThread
        fun requestPermissions(
                activity: AppCompatActivity,
                requestId: Int,
                vararg permissions: String
        ) {
            _requestPermissions(
                    activity,
                    requestId,
                    *permissions
            )
        }

        /**
         * A static factory method to request permission from fragment.
         * Your fragment must implement [PermissionObserver]
         *
         * @param fragment an instance of [Fragment] which is also [PermissionObserver]
         * @param requestId Request ID for permission request
         * @param permissions Permission(s) to request
         *
         * @throws [IllegalArgumentException] if your fragment doesn't implement [PermissionObserver]
         */
        @JvmStatic
        @MainThread
        fun requestPermissions(
                fragment: Fragment,
                requestId: Int,
                vararg permissions: String
        ) {
            _requestPermissions(
                    fragment,
                    requestId,
                    *permissions
            )
        }

        private fun _requestPermissions(
                activityOrFragment: Any,
                requestId: Int,
                vararg permissions: String
        ) {

            val fragmentManager = if (activityOrFragment is AppCompatActivity) {
                activityOrFragment.supportFragmentManager
            } else {
                (activityOrFragment as Fragment).childFragmentManager
            }

            if (fragmentManager.findFragmentByTag(TAG) != null) {
                (fragmentManager.findFragmentByTag(TAG) as PermissionManager).requestPermissions(
                        requestId,
                        *permissions
                )
            } else {
                if (activityOrFragment !is PermissionObserver) {
                    throw IllegalArgumentException(
                            "Activity/Fragment must implement PermissionObserver"
                    )
                } else {
                    val permissionManager = PermissionManager()
                    fragmentManager.beginTransaction().add(
                            permissionManager,
                            TAG
                    ).commitNow()
                    permissionManager.requestPermissions(requestId, *permissions)
                }
            }
        }
    }


}