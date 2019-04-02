package com.crazylegend.kotlinextensions.biometric

import android.Manifest.permission.USE_BIOMETRIC
import androidx.annotation.RequiresPermission
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executors


/**
 * Created by hristijan on 4/2/19 to long live and prosper !
 */

@RequiresPermission(allOf = [USE_BIOMETRIC])
fun FragmentActivity.biometricAuth(
    promptInfo: BiometricPrompt.PromptInfo,
    onAuthFailed:()-> Unit,
    onAuthError:(errorCode:Int, errorMessage:String)->Unit = {_,_->},
    onAuthSuccess:(result:BiometricPrompt.AuthenticationResult) -> Unit = {_->}): BiometricPrompt {
    val executor = Executors.newSingleThreadExecutor()

    val biometricPrompt = BiometricPrompt(this,executor, object: BiometricPrompt.AuthenticationCallback(){
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