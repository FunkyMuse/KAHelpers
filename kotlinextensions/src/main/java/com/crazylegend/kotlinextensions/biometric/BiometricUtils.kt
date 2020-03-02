package com.crazylegend.kotlinextensions.biometric

import android.Manifest.permission.USE_BIOMETRIC
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.*
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executors


/**
 * Created by hristijan on 4/2/19 to long live and prosper !
 */

/**
 * Use [canAuthenticate] before calling this function, just to see if the user can be authenticated, don't do stupid shit and blame the API or function
 * if you get a weird result code that you forgot to handle, peace.
 */
@RequiresApi(Build.VERSION_CODES.P)
@RequiresPermission(allOf = [USE_BIOMETRIC])
fun FragmentActivity.biometricAuth(
        promptInfo: BiometricPrompt.PromptInfo,
        onAuthFailed: () -> Unit,
        onAuthError: (errorCode: Int, errorMessage: String) -> Unit = { _, _ -> },
        onAuthSuccess: (result: BiometricPrompt.AuthenticationResult) -> Unit = { _ -> }): BiometricPrompt {
    val executor = Executors.newSingleThreadExecutor()

    val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            onAuthError(errorCode, errString.toString())
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            onAuthFailed()
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            onAuthSuccess(result)
        }
    })

    biometricPrompt.authenticate(promptInfo)
    return biometricPrompt
}

/**
 * Use [canAuthenticate] before calling this function, just to see if the user can be authenticated, don't do stupid shit and blame the API or function
 * if you get a weird result code that you forgot to handle, peace.
 */
@RequiresApi(Build.VERSION_CODES.P)
@RequiresPermission(allOf = [USE_BIOMETRIC])
fun AppCompatActivity.biometricAuth(
        promptInfo: BiometricPrompt.PromptInfo,
        onAuthFailed: () -> Unit,
        onAuthError: (errorCode: Int, errorMessage: String) -> Unit = { _, _ -> },
        onAuthSuccess: (result: BiometricPrompt.AuthenticationResult) -> Unit = { _ -> }): BiometricPrompt {
    val executor = Executors.newSingleThreadExecutor()

    val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            onAuthError(errorCode, errString.toString())
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            onAuthFailed()
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            onAuthSuccess(result)
        }
    })

    biometricPrompt.authenticate(promptInfo)
    return biometricPrompt
}


fun FragmentActivity.canAuthenticate(hardwareUnavailable: () -> Unit = {}, noFingerprintsEnrolled: () -> Unit = {}, canAuthenticateAction: () -> Unit = {}) {
    when (BiometricManager.from(this).canAuthenticate()) {
        BIOMETRIC_SUCCESS -> {
            canAuthenticateAction()
        }
        BIOMETRIC_ERROR_NONE_ENROLLED -> {
            noFingerprintsEnrolled()
        }
        BIOMETRIC_ERROR_NO_HARDWARE -> {
            hardwareUnavailable()
        }

        BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            hardwareUnavailable()
        }
    }
}