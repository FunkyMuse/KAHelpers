package com.crazylegend.accesssibility

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.content.getSystemService
import androidx.core.os.bundleOf

/**
 * Created by crazy on 10/20/20 to long live and prosper !
 */

/**
 * Based on {@link com.android.settingslib.accessibility.AccessibilityUtils#getEnabledServicesFromSettings(Context,int)}
 * @see <a href="https://github.com/android/platform_frameworks_base/blob/d48e0d44f6676de6fd54fd8a017332edd6a9f096/packages/SettingsLib/src/com/android/settingslib/accessibility/AccessibilityUtils.java#L55">AccessibilityUtils</a>
 */
inline fun <reified T : AccessibilityService> Context.hasAccessibilityPermission(): Boolean {
    val expectedComponentName = ComponentName(this, T::class.java)
    val enabledServicesSetting = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            ?: return false
    val colonSplitter = TextUtils.SimpleStringSplitter(':')
    colonSplitter.setString(enabledServicesSetting)
    while (colonSplitter.hasNext()) {
        val componentNameString = colonSplitter.next()
        val enabledService = ComponentName.unflattenFromString(componentNameString)
        if (enabledService != null && enabledService == expectedComponentName) return true
    }
    return false
}


val Context.isAccessibilityEnabled get() = getSystemService<AccessibilityManager>()?.isEnabled ?: false


inline fun <reified T : AccessibilityService> Context.isAccessibilityServiceRunning(): Boolean {
    val settingsString = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
    return settingsString != null && settingsString.contains("${packageName}/${T::class.java.name}")
}

fun Context.askForAccessibilityPermission() = startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))

inline fun <reified T> Context.askForAccessibilityPermissionHighlight(
        argKey: String = ":settings:fragment_args_key",
        showFragsKey: String = ":settings:show_fragment_args"
) {
    startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
        val showArgs = packageName + "/" + T::class.java.name
        putExtra(argKey, showArgs)
        putExtra(showFragsKey, bundleOf(argKey to showArgs))
    })
}

fun logViewHierarchy(nodeInfo: AccessibilityNodeInfo, depth: Int = 0,
                     callback: (space: String, iteratedNode: AccessibilityNodeInfo) -> Unit) {
    var spacerString = ""
    for (i in 0 until depth) {
        spacerString += '-'
    }
    //Log the info you care about here... I chose classname and view resource name, because they are simple, but interesting.
    callback(spacerString, nodeInfo)

    for (i in 0 until nodeInfo.childCount) {
        logViewHierarchy(nodeInfo.getChild(i), depth + 1, callback)
    }
}