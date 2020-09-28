package com.crazylegend.customviews.autoStart

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.crazylegend.customviews.*


/**
 * Created by crazy on 8/6/20 to long live and prosper !
 *
 * adb shell "dumpsys activity activities | grep mResumedActivity"
 * to contribute
 */
object AutoStartHelper {

    private val Context.doNotShowAgainPrefs get() = getSharedPreferencesByTag(DIALOG_TAG)

    private const val DIALOG_SHOWN_TAG = "isAutoStartDialogShown"
    private val Context.isDialogShown get() = doNotShowAgainPrefs.getBoolean(DIALOG_SHOWN_TAG, false)
    private fun Context.setDialogShown() = doNotShowAgainPrefs.putBoolean(DIALOG_SHOWN_TAG, true)
    fun setDialogNotShown(context: Context) = context.doNotShowAgainPrefs.putBoolean(DIALOG_SHOWN_TAG, false)

    private val POWER_MANAGER_INTENTS = arrayOf(
            Intent().setComponent(ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            Intent().setComponent(ComponentName("com.miui.securitycenter", "com.miui.powercenter.PowerSettings")),
            Intent().setComponent(ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            Intent().setComponent(ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")),
            Intent().setComponent(ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            Intent().setComponent(ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
            Intent().setComponent(ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            Intent().setComponent(ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            Intent().setComponent(ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            Intent().setComponent(ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            Intent().setComponent(ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            Intent().setComponent(ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            Intent().setComponent(ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
            Intent().setComponent(ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
            Intent().setComponent(ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity")),
            Intent().setComponent(ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.powersaver.PowerSaverSettings")),
            Intent().setComponent(ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
            Intent().setComponent(ComponentName("com.transsion.phonemanager", "com.itel.autobootmanager.activity.AutoBootMgrActivity")),
            Intent().setComponent(ComponentName("com.oneplus.security", "com.oneplus.security.chainlaunch.view.ChainLaunchAppListActivity")),
            Intent().setComponent(ComponentName("com.evenwell.powersaving.g3", "com.evenwell.powersaving.g3.exception.PowerSaverExceptionActivity"))
    )

    private val defaultBundle
        get() = bundleOf(
                Pair(ConfirmationDialogAutoStart.TEXT_FIELD, "Please enable auto-start ability for this application."),
                Pair(ConfirmationDialogAutoStart.CONFIRM_TEXT, "Allow"),
                Pair(ConfirmationDialogAutoStart.DO_NOT_SHOW_AGAIN_VISIBILITY, true)
        )

    fun checkAutoStartManually(context: Context, action: Intent.() -> Unit) {
        iterateIntents(context, action)
    }

    fun checkAutoStart(context: Context, dialogBundle: Bundle = defaultBundle) {
        context.isDialogShown.ifFalse {
            iterateIntents(context) {
                showAlert(context, dialogBundle) {
                    context.startActivity(this)
                }
            }
        }
    }

    private inline fun iterateIntents(context: Context, action: Intent.() -> Unit) {
        for (intent in POWER_MANAGER_INTENTS) {
            if (isIntentResolvable(context, intent)) {
                intent.action()
                break
            }
        }
    }

    private fun isIntentResolvable(context: Context, intent: Intent): Boolean =
            context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null


    private lateinit var confirmationDialogAutoStart: ConfirmationDialogAutoStart
    const val DIALOG_TAG = "dialogAutoStartTag"

    private fun showAlert(context: Context, bundle: Bundle, action: () -> Unit) {
        when (context) {
            is AppCompatActivity -> {
                showDialog(context.supportFragmentManager, context, context, bundle, action)
            }

            is Fragment -> {
                showDialog(context.childFragmentManager, context.viewLifecycleOwner, context, bundle, action)
            }
            else -> {
                throw IllegalStateException("Context must be a Fragment or an Activity")
            }
        }
    }


    private inline fun showDialog(fragmentManager: FragmentManager, lifecycleOwner: LifecycleOwner, context: Context, bundle: Bundle, crossinline action: () -> Unit) {
        fragmentManager.apply {
            findFragmentByTag(DIALOG_TAG)?.remove()
            confirmationDialogAutoStart = ConfirmationDialogAutoStart()
            confirmationDialogAutoStart.arguments = bundle
            setFragmentResultListener(ConfirmationDialogAutoStart.REQ_KEY, lifecycleOwner) { _, result ->
                result.getBoolean(ConfirmationDialogAutoStart.RESULT_KEY, false).ifTrue {
                    action()
                }
                result.getBoolean(ConfirmationDialogAutoStart.DO_NOT_SHOW_AGAIN_RESULT_KEY, false).ifTrue {
                    context.setDialogShown()
                }
            }
            confirmationDialogAutoStart.show(this, DIALOG_TAG)
        }
    }


}