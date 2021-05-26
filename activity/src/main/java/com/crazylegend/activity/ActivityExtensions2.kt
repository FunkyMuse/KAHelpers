package com.crazylegend.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Build
import android.provider.Settings
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity

/**
 * Created by crazy on 9/7/20 to long live and prosper !
 */

fun AppCompatActivity.showBackButton() {
    this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
}

fun AppCompatActivity.hideToolbar() {
    this.supportActionBar?.hide()
}

fun AppCompatActivity.showToolbar() {
    this.supportActionBar?.show()
}

fun Activity?.hideKeyboardSoft() {
    if (this != null) {
        val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (inputManager != null) {
            val v = this.currentFocus
            if (v != null) {
                inputManager.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }
}

fun Activity.isActivityFinishing(): Boolean = this.isFinishing || this.isDestroyed


fun AppCompatActivity.customToolbarDrawable(drawable: Drawable?) {
    supportActionBar?.setBackgroundDrawable(drawable)
}

fun AppCompatActivity.customIndicator(drawable: Drawable?) {
    supportActionBar?.setHomeAsUpIndicator(drawable)
}

fun AppCompatActivity.customIndicator(drawableResource: Int) {
    supportActionBar?.setHomeAsUpIndicator(drawableResource)
}


fun FragmentActivity.fullScreenGestureNavigationActivity() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        if (isGestureNavigationEnabled()) {
            // Extends the PhotoView in the whole screen
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            // Hides StatusBar and Navigation bar, you have to tap to appear
            // window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            // Fixes the Full Screen black bar in screen with notch
            window.attributes.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
    }
}

fun AppCompatActivity.fullScreenGestureNavigationActivity() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        if (isGestureNavigationEnabled()) {
            // Extends the PhotoView in the whole screen
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            // Hides StatusBar and Navigation bar, you have to tap to appear
            // window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            // Fixes the Full Screen black bar in screen with notch
            window.attributes.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
    }
}

/**
 * Creates shortcut launcher for pre/post oreo devices
 */
@Suppress("DEPRECATION")
inline fun <reified T> Activity.createShortcut(title: String, @DrawableRes icon: Int) {
    val shortcutIntent = Intent(this, T::class.java)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) { // code for adding shortcut on pre oreo device
        val intent = Intent("com.android.launcher.action.INSTALL_SHORTCUT")
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent)
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title)
        intent.putExtra("duplicate", false)
        val parcelable = Intent.ShortcutIconResource.fromContext(this, icon)
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, parcelable)
        this.sendBroadcast(intent)
        // println("added_to_homescreen")
    } else {
        val shortcutManager = this.getSystemService(ShortcutManager::class.java)
        if (shortcutManager.isRequestPinShortcutSupported) {
            val pinShortcutInfo = ShortcutInfo.Builder(this, "some-shortcut-")
                    .setIntent(shortcutIntent)
                    .setIcon(Icon.createWithResource(this, icon))
                    .setShortLabel(title)
                    .build()

            shortcutManager.requestPinShortcut(pinShortcutInfo, null)
            // println("added_to_homescreen")
        } else {
            // println("failed_to_add")
        }
    }
}

private fun Context.isGestureNavigationEnabled() = Settings.Secure.getInt(contentResolver, "navigation_mode", 0) == 2

fun Context?.isActivityFinished(): Boolean {
    this ?: return false
    return if (this is Activity) {
        this.isActivityFinishing()
    } else {
        true
    }
}

fun Context?.isActivityActive(): Boolean {
    this ?: return false
    return if (this is Activity) {
        !this.isActivityFinishing()
    } else {
        false
    }
}