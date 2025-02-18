package dev.funkymuse.kotlinextensions.packageutils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.provider.Settings

/**
 * Checks if a given package is installed
 * @param packageName packageId
 * @return true if installed with activity, false otherwise
 */
fun Context.isAppInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun Context.isAppEnabled(packageName: String): Boolean {
    return try {
        packageManager.getApplicationInfo(packageName, 0).enabled
    } catch (e: Exception) {
        false
    }
}


fun Context.whoInstalledMyApp(packageName: String) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        packageManager.getInstallSourceInfo(packageName).installingPackageName
    } else {
        packageManager.getInstallerPackageName(packageName)
    }


fun Context.showAppInfo(packageName: String) {
    try {
        //Open the specific App Info page:
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
        //Open the generic Apps page:
        val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
        startActivity(intent)
    }
}

inline val buildIsMarshmallowAndUp: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

inline val buildIsNougatAndUp: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

inline val buildIsOreoAndUp: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

inline val buildIsPieAndUp: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

inline val buildIs10AndUp: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

inline val buildIs11AndUp: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R


const val INSTALLER_GOOGLE_PLAY_VENDING = "com.android.vending"
const val INSTALLER_GOOGLE_PLAY_FEEDBACK = "com.google.android.feedback"

inline val Context.installerPackageName: String?
    get() = whoInstalledMyApp(packageName)

inline val Context.isFromGooglePlay: Boolean
    get() {
        val installer = installerPackageName
        return arrayOf(
            INSTALLER_GOOGLE_PLAY_FEEDBACK,
            INSTALLER_GOOGLE_PLAY_VENDING
        ).any { it == installer }
    }

fun PackageManager.isIntentSafe(intent: Intent): Boolean {
    return queryIntentActivities(intent, 0).isNotEmpty()
}

/**
 * Return whether the given PackageInfo represents a system package or not.
 * User-installed packages (Market or otherwise) should not be denoted as
 * system packages.
 *
 * @param pkgInfo
 * @return
 */
fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
    return (pkgInfo.applicationInfo?.flags ?: -1) and ApplicationInfo.FLAG_SYSTEM != 0
}

fun Context.launchAnApp(packageName: String) {
    val launchApp = packageManager.getLaunchIntentForPackage(packageName)
    startActivity(launchApp)
}

fun PackageManager.getAppInfoFromPackageName(packageName: String): ResolveInfo? {
    val intent = Intent()
    intent.`package` = packageName
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    return resolveActivity(intent, 0)
}

fun PackageManager.getAvailableApplications(): MutableList<ResolveInfo> {

    val mainIntent = Intent(Intent.ACTION_MAIN, null)
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

    return queryIntentActivities(mainIntent, 0)
}

fun Context.openAppInfo(packageName: String) {
    try {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)

    } catch (e: ActivityNotFoundException) {
        val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
        startActivity(intent)
    }
}

