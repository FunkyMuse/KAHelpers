package com.crazylegend.security

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import android.os.Debug
import android.os.Process
import android.util.Base64
import android.util.Log
import java.io.BufferedReader
import java.io.FileReader
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 * Created by crazy on 9/10/20 to long live and prosper !
 */

/**
 * Custom roms have this tag
 * @return Boolean
 */
fun isTestKeyBuild(): Boolean {
    val keys = Build.TAGS
    return keys != null && keys.contains("test-keys")
}

fun detectDebugger(): Boolean = Debug.isDebuggerConnected()

fun Context.isDebuggable(): Boolean = applicationContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0


fun isDebuggerAttached(): Boolean {
    val start = Debug.threadCpuTimeNanos()
    for (i in 0..999999) continue
    val stop = Debug.threadCpuTimeNanos()
    return stop - start >= 10000000
}

/**
 * If the count is more than 2 then the app is modified
 * @return Int
 */
fun zygoteCallCount(): Int {
    var zygoteInitCallCount = 0
    try {
        throw Exception("PiracyChecker")
    } catch (e: Exception) {
        for (stackTraceElement in e.stackTrace) {
            if (stackTraceElement.className == "com.android.internal.os.ZygoteInit") {
                zygoteInitCallCount++
            }
            if (stackTraceElement.className == "com.saurik.substrate.MS$2" && stackTraceElement.methodName == "invoked") {
                zygoteInitCallCount++
            }
            if (stackTraceElement.className == "de.robv.android.xposed.XposedBridge" && stackTraceElement.methodName == "main") {
                zygoteInitCallCount++
            }
            if (stackTraceElement.className == "de.robv.android.xposed.XposedBridge" && stackTraceElement.methodName == "handleHookedMethod") {
                zygoteInitCallCount++
            }
        }
    }
    return zygoteInitCallCount
}


fun Context.getSingInfoMD5(pkg: String): String? {
    return try {
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageManager.getPackageInfo(pkg, PackageManager.GET_SIGNING_CERTIFICATES)
        } else {
            packageManager.getPackageInfo(pkg, PackageManager.GET_SIGNATURES)
        }
        val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.signingInfo.signingCertificateHistory
        } else {
            packageInfo.signatures
        }
        val signature = signatures.firstOrNull() ?: return null
        getMd5(signature)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun getMd5(signature: Signature): String? {
    return encryptionMD5(signature.toByteArray())
}

private fun encryptionMD5(byteStr: ByteArray): String? {
    val md5StrBuff = StringBuffer()
    try {
        val messageDigest = MessageDigest.getInstance("MD5")
        messageDigest.reset()
        messageDigest.update(byteStr)
        val byteArray: ByteArray = messageDigest.digest()
        for (i in byteArray.indices) {
            if (Integer.toHexString(0xFF and byteArray[i].toInt()).length == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF and byteArray[i].toInt()))
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF and byteArray[i].toInt()))
            }
        }
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
        return null
    }
    return md5StrBuff.toString()
}

fun catchSubstrateAndXposed() {
    try {
        val libraries: MutableSet<String> = mutableSetOf()
        val mapsFilename = "/proc/" + Process.myPid() + "/maps"
        val reader = BufferedReader(FileReader(mapsFilename))
        var line: String
        while (reader.readLine().also { line = it } != null) {
            if (line.endsWith(".so") || line.endsWith(".jar")) {
                val n = line.lastIndexOf(" ")
                libraries.add(line.substring(n + 1))
            }
        }
        for (library in libraries) {
            if (library.contains("com.saurik.substrate")) {
                Log.d("HookDetection", "Substrate shared object found: $library")
            }
            if (library.contains("XposedBridge.jar")) {
                Log.d("HookDetection", "Xposed JAR found: $library")
            }
        }
        reader.close()
    } catch (e: Exception) {
        Log.wtf("HookDetection", e.toString())
    }
}


fun isEmulator(): Boolean {
    return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
            || Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
            || Build.HARDWARE.contains("goldfish")
            || Build.HARDWARE.contains("ranchu")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || Build.PRODUCT.contains("sdk_google")
            || Build.PRODUCT.contains("google_sdk")
            || Build.PRODUCT.contains("sdk")
            || Build.PRODUCT.contains("sdk_x86")
            || Build.PRODUCT.contains("vbox86p")
            || Build.PRODUCT.contains("emulator")
            || Build.PRODUCT.contains("simulator")) || isAlsoEmulator()
}


private fun isAlsoEmulator(): Boolean {
    val goldfish: Boolean = getSystemProperty("ro.hardware").contains("goldfish")
    val emu: Boolean = getSystemProperty("ro.kernel.qemu").isNotEmpty()
    val sdk: Boolean = getSystemProperty("ro.product.model") == "sdk"
    return goldfish || emu || sdk
}

private fun getSystemProperty(name: String): String {
    val systemPropertyClazz = Class.forName("android.os.SystemProperties")
    return systemPropertyClazz.getMethod("get", String::class.java).invoke(systemPropertyClazz, name) as String
}


fun Context.getShaSignature(packageName: String): String? {
    try {
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
        } else {
            packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        }
        val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.signingInfo.signingCertificateHistory
        } else {
            packageInfo.signatures
        }

        val signature = signatures.firstOrNull() ?: return null
        val md: MessageDigest = MessageDigest.getInstance("SHA1")
        md.update(signature.toByteArray())
        return String(Base64.encode(md.digest(), 0))
    } catch (e: Exception) {
        return null
    }
}