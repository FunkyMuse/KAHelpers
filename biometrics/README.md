# Biometrics

[![](https://jitpack.io/v/CraZyLegenD/Set-Of-Useful-Kotlin-Extensions-and-Helpers.svg)](https://jitpack.io/#CraZyLegenD/Set-Of-Useful-Kotlin-Extensions-and-Helpers)


## Usage
1. Add JitPack to your project build.gradle

```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
   }
}
```

2. Add the dependency in the application build.gradle

```gradle
dependencies {

//biometrics
implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:biometrics:$utilsVersion"

}
```

3. Add necessary permissions
```xml
<uses-permission android:name="android.permission.USE_BIOMETRIC" />

//if you're using the legacy FingerprintController
<uses-permission android:name="android.permission.VIBRATE" />
<uses-permission android:name="android.permission.USE_FINGERPRINT" />
```

4. Example

```kotlin
canAuthenticate(hardwareUnavailable = {
            //some message about hardware missing
        }, noFingerprintsEnrolled = {
            //make user action to enroll fingerprints
        }) {
            biometricAuth(promptInfoAction = {
                setTitle("Verification required")
                setSubtitle("Action paused")
                setDescription("Please verify your identity to proceed with the action")
                setDeviceCredentialAllowed(true)
                this
            }, onAuthFailed = {
                //auth failed action
            }, onAuthError = { errorCode, errorMessage ->
                //handle auth error message and codes

            }) {
                //handle successful authentication
            }
        }

```
    
