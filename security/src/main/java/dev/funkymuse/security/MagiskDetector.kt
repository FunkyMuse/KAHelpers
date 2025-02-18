package dev.funkymuse.security

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log


class MagiskDetector(context: Context) {
    private val pm: PackageManager = context.packageManager
    private val packages = pm.getInstalledPackages(PackageManager.GET_META_DATA or PackageManager.GET_ACTIVITIES)

    fun checkForMagisk(): Boolean {
        for (pkg in packages) {
            val pkgName = pkg.packageName
            val appInfo = pkg.applicationInfo?: return false
            if (pkgName == ORIGINAL_PACKAGE) return true

            val label = pm.getApplicationLabel(appInfo).toString()
            if (label.equals(ORIGINAL_LABEL, true)) return true

            if (pkgName.startsWith(
                            SCREAMBLED_PACKAGE_PREFIX,
                            true
                    ) && !PACKAGE_NOT.any { pkgName.contains(it) } && label.contains(RENAMED_LABEL, true)
            ) {
                // Getting in here alone is a 90% guarantee it is magisk
                if (DEBUG_LOG) {
                    Log.d(TAG, pkgName)
                    Log.d(TAG, label)
                }
                val res = pm.getResourcesForApplication(appInfo)
                // Iterate over each subsection of resource ids and try to guess the sub sections of those sections
                for (i in 0x7f010000..0x7f130000 step 10000) {
                    for (j in i..(i + 10000) step SMALL_STEP - 100) {
                        try {
                            for (k in j..(j + SMALL_STEP)) {
                                val name = res.getResourceName(k)
                                if (DEBUG_LOG) {
                                    Log.d(TAG, name)
                                }
                                if (name.startsWith(ORIGINAL_PACKAGE) || name.substringAfter('/') in resourceNames) return true
                            }
                        } catch (ignored: Exception) {
                        }
                    }
                }
            }
        }
        return false
    }

    companion object {
        val resourceNames = listOf(
                "ic_magiskhide",
                "settings_su_adb",
                "magiskhide",
                "magiskFragment"
                //"settings_su_app_adb",
                //"nonroot_utils",
                //"magisk_update_title",
                //"magiskHideFragment"
        )

        val PACKAGE_NOT = listOf(
                "manager",
                "com.android."
        )
        const val TAG = "DETECT"
        const val ORIGINAL_PACKAGE = "com.topjohnwu.magisk"
        const val ORIGINAL_LABEL = "Magisk Manager"
        const val RENAMED_LABEL = "Manager"
        const val SCREAMBLED_PACKAGE_PREFIX = "com."
        const val DEBUG_LOG = false
        const val SMALL_STEP = 300
    }
}