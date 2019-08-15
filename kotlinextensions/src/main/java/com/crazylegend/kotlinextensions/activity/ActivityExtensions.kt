package com.crazylegend.kotlinextensions.activity

import android.Manifest.permission.WRITE_SETTINGS
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.GONE
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.crazylegend.kotlinextensions.R
import com.crazylegend.kotlinextensions.packageutils.buildIsMarshmallowAndUp
import java.util.*


/**
 * Created by hristijan on 2/22/19 to long live and prosper !
 */

/**
 * Hide keyboard for an activity.
 */
fun Activity.hideSoftKeyboard() {
    if (currentFocus != null) {
        val inputMethodManager = getSystemService(
                Context
                        .INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}


/**
 * Returns the Activity's content (root) view.
 */
val Activity.rootView: View?
    get() = findViewById(android.R.id.content)

/**
 * Show keyboard for an activity.
 */
fun Activity.showKeyboard(toFocus: View) {
    toFocus.requestFocus()
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    // imm.showSoftInput(toFocus, InputMethodManager.SHOW_IMPLICIT);
    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

/**
 * Returns display density as ...DPI
 */
fun AppCompatActivity.getDisplayDensity(): String {
    val metrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(metrics)
    return when (metrics.densityDpi) {
        DisplayMetrics.DENSITY_LOW -> "LDPI"
        DisplayMetrics.DENSITY_MEDIUM -> "MDPI"
        DisplayMetrics.DENSITY_HIGH -> "HDPI"
        DisplayMetrics.DENSITY_XHIGH -> "XHDPI"
        DisplayMetrics.DENSITY_XXHIGH -> "XXHDPI"
        DisplayMetrics.DENSITY_XXXHIGH -> "XXXHDPI"
        else -> "XXHDPI"
    }
}

/**
 * Returns the StatusBarHeight in Pixels
 */
val Activity.getStatusBarHeight: Int
    get() {
        val rect = Rect()
        window.decorView.getWindowVisibleDisplayFrame(rect)
        return rect.top
    }


/**
 * Set Status Bar Color if Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
fun Activity.setStatusBarColor(@ColorInt color: Int) {
    window.statusBarColor = color
}

/**
 * Set Navigation Bar Color if Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
fun Activity.setNavigationBarColor(@ColorInt color: Int) {
    window.navigationBarColor = color
}

/**
 * Set Navigation Bar Divider Color if Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
 */
@RequiresApi(api = Build.VERSION_CODES.P)
fun Activity.setNavigationBarDividerColor(@ColorInt color: Int) {
    window.navigationBarDividerColor = color
}

/**
 *  Makes the activity enter fullscreen mode.
 */
@UiThread
fun Activity.enterFullScreenMode() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}


/**
 * Makes the Activity exit fullscreen mode.
 */
@UiThread
fun Activity.exitFullScreenMode() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
}

/**
 * Restarts an activity from itself with a fade animation
 * Keeps its existing extra bundles and has a intentBuilder to accept other parameters
 */
inline fun Activity.restart(intentBuilder: Intent.() -> Unit = {}) {
    val i = Intent(this, this::class.java)
    val oldExtras = intent.extras
    if (oldExtras != null)
        i.putExtras(oldExtras)
    i.intentBuilder()
    startActivity(i)
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out) //No transitions
    finish()
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
}

/**
 * Force restart an entire application
 */
@RequiresApi(Build.VERSION_CODES.M)
inline fun Activity.restartApplication() {
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    val pending = PendingIntent.getActivity(this, 666, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    if (buildIsMarshmallowAndUp)
        alarm.setExactAndAllowWhileIdle(AlarmManager.RTC, System.currentTimeMillis() + 100, pending)
    else
        alarm.setExact(AlarmManager.RTC, System.currentTimeMillis() + 100, pending)
    finish()
    System.exit(0)
}

fun Activity.finishSlideOut() {
    finish()
    overridePendingTransition(R.anim.fade_in, R.anim.abc_fade_out)
}


inline fun <reified T> Activity.startActivityForResult(
        requestCode: Int,
        bundleBuilder: Bundle.() -> Unit = {},
        intentBuilder: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java)
    intent.intentBuilder()
    val bundle = Bundle()
    bundle.bundleBuilder()
    startActivityForResult(intent, requestCode, if (bundle.isEmpty) null else bundle)
}


/**
 * Simplify using AlertDialog
 */
inline fun Activity.alert(body: AlertDialog.Builder.() -> AlertDialog.Builder) {
    AlertDialog.Builder(this)
            .body()
            .show()
}

inline fun <reified T : Any> Activity.launchActivity(
        requestCode: Int = -1,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()

    startActivityForResult(intent, requestCode, options)

}

inline fun <reified T : Any> Context.launchActivity(
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, options)

}

inline fun <reified T : Any> newIntent(context: Context): Intent =
        Intent(context, T::class.java)

fun Activity.setBackgroundColor(@ColorInt color: Int) {
    window.setBackgroundDrawable(ColorDrawable(color))
}


fun FragmentActivity.switchFragment(
        showFragment: Fragment,
        @IdRes containerId: Int,
        transaction: Int = FragmentTransaction.TRANSIT_NONE
) {
    supportFragmentManager.switch(showFragment, containerId, transaction)
}

fun Fragment.hide() {
    this.fragmentManager?.hide(this)
}

fun Fragment.show() {
    this.fragmentManager?.show(this)
}

fun Fragment.remove() {
    this.fragmentManager?.remove(this)
}

fun Fragment.showHide(
        vararg hideFragment: Fragment,
        transaction: Int = FragmentTransaction.TRANSIT_NONE
) {
    this.fragmentManager?.showHide(this, *hideFragment, transaction = transaction)
}

fun FragmentManager.add(
        addFragment: Fragment,
        @IdRes containerId: Int,
        isHide: Boolean = false,
        isAddStack: Boolean = false,
        tag: String = addFragment::class.java.name
) {
    val ft = this.beginTransaction()
    val fragmentByTag = this.findFragmentByTag(tag)
    if (fragmentByTag != null && fragmentByTag.isAdded) {
        ft.remove(fragmentByTag)
    }
    ft.add(containerId, addFragment, tag)
    if (isHide) ft.hide(addFragment)
    if (isAddStack) ft.addToBackStack(tag)

    ft.commit()
}


fun FragmentManager.add(
        addList: List<Fragment>,
        @IdRes containerId: Int,
        showIndex: Int = 0
) {
    val ft = this.beginTransaction()
    for (i in 0 until addList.size) {
        val addFragment = addList[i]
        val tag = addFragment::class.java.name
        val fragmentByTag = this.findFragmentByTag(tag)
        if (fragmentByTag != null && fragmentByTag.isAdded) {
            ft.remove(fragmentByTag)
        }
        ft.add(containerId, addFragment, tag)

        if (showIndex != i) ft.hide(addFragment)
    }
    ft.commit()
}


fun FragmentManager.hide(vararg hideFragment: Fragment) {
    hide(hideFragment.toList())
}


fun FragmentManager.hide(hideFragment: List<Fragment>) {
    val ft = this.beginTransaction()
    for (fragment in hideFragment) {
        ft.hide(fragment)
    }
    ft.commit()
}


fun FragmentManager.show(showFragment: Fragment) {
    val ft = this.beginTransaction()
    ft.show(showFragment)
    ft.commit()
}


fun FragmentManager.remove(vararg removeFragment: Fragment) {
    val ft = this.beginTransaction()
    for (fragment in removeFragment) {
        ft.remove(fragment)
    }
    ft.commit()
}


fun FragmentManager.removeTo(removeTo: Fragment, isIncludeSelf: Boolean = false) {
    val ft = this.beginTransaction()
    val fragments = this.getFmFragments()
    for (i in (fragments.size - 1)..0) {
        val fragment = fragments[i]
        if (fragment == removeTo && isIncludeSelf) {
            ft.remove(fragment)
            break
        }
        ft.remove(fragment)
    }
    ft.commit()
}


fun FragmentManager.removeAll() {
    val frg = getFmFragments()
    if (frg.isEmpty()) return

    val ft = this.beginTransaction()
    for (fragment in frg) {
        ft.remove(fragment)
    }
    ft.commit()
}


fun FragmentManager.showHide(
        showFragment: Fragment,
        vararg hideFragment: Fragment,
        transaction: Int = FragmentTransaction.TRANSIT_NONE
) {
    val ft = this.beginTransaction().setTransition(transaction)

    ft.show(showFragment)
    for (fragment in hideFragment) {
        if (fragment != showFragment) {
            ft.hide(fragment)
        }
    }

    ft.commit()
}


fun FragmentManager.replace(
        fragment: Fragment,
        @IdRes containerId: Int,
        isAddStack: Boolean = false,
        tag: String = fragment::class.java.name
) {
    val ft = this.beginTransaction()

    ft.replace(containerId, fragment, tag)
    if (isAddStack) ft.addToBackStack(tag)

    ft.commit()
}


fun FragmentManager.switch(
        showFragment: Fragment,
        @IdRes containerId: Int,
        transaction: Int = FragmentTransaction.TRANSIT_NONE
) {
    val ft = this.beginTransaction().setTransition(transaction)

    val tag = showFragment::class.java.name
    val fragmentByTag = this.findFragmentByTag(tag)
    if (fragmentByTag != null && fragmentByTag.isAdded) {
        ft.show(fragmentByTag)
    } else {
        ft.add(containerId, showFragment, tag)
    }

    for (tempF in this.getFmFragments()) {
        if (tempF != fragmentByTag) {
            ft.hide(tempF)
        }
    }
    ft.commit()
}

fun FragmentManager.getTop(): Fragment? {
    val frg = getFmFragments()
    return frg.ifEmpty { return null }[frg.size - 1]
}

fun FragmentManager.getFmFragments(): List<Fragment> {
    return this.fragments
}

inline fun <reified T : Fragment> FragmentManager.findFragment(): Fragment? {
    return this.findFragmentByTag(T::class.java.name)
}

inline var Context.sleepDuration: Int
    @RequiresPermission(WRITE_SETTINGS)
    set(value) {
        Settings.System.putInt(
                this.contentResolver,
                Settings.System.SCREEN_OFF_TIMEOUT,
                value
        )
    }
    get() {
        return try {
            Settings.System.getInt(
                    this.contentResolver,
                    Settings.System.SCREEN_OFF_TIMEOUT
            )
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
            -123
        }
    }


@SuppressLint("ObsoleteSdkInt")
fun Activity.hideBottomBar() {
    if (Build.VERSION.SDK_INT < 19) { // lower api
        val v = this.window.decorView
        v.systemUiVisibility = GONE
    } else {
        //for new api versions.
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = uiOptions
    }
}

/**
 * Sets the screen brightness. Call this before setContentView.
 * 0 is dimmest, 1 is brightest. Default value is 1
 */
inline fun Activity.brightness(brightness: Float = 1f) {
    val params = window.attributes
    params.screenBrightness = brightness // range from 0 - 1 as per docs
    window.attributes = params
    window.addFlags(WindowManager.LayoutParams.FLAGS_CHANGED)
}


inline fun <T> Activity.extra(key: String): Lazy<T?> {
    return lazy(LazyThreadSafetyMode.NONE) {
        @Suppress("UNCHECKED_CAST")
        intent.extras?.get(key) as T
    }
}

inline fun <T> Activity.extraOrNull(key: String): Lazy<T?> {
    return lazy(LazyThreadSafetyMode.NONE) {
        @Suppress("UNCHECKED_CAST")
        intent.extras?.get(key) as? T?
    }
}


@SuppressLint("ObsoleteSdkInt")
fun Activity.showBottomBar() {
    if (Build.VERSION.SDK_INT < 19) { // lower api
        val v = this.window.decorView
        v.systemUiVisibility = View.VISIBLE
    } else {
        //for new api versions.
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
        decorView.systemUiVisibility = uiOptions
    }
}

val View.hasNotch: Boolean
    @RequiresApi(Build.VERSION_CODES.P)
    get() {
        return rootWindowInsets?.displayCutout != null
    }


inline fun Activity.lockCurrentScreenOrientation() {
    requestedOrientation = when (resources.configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        else -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
    }
}

inline fun Activity.unlockScreenOrientation() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
}

inline val Activity.isInMultiWindow: Boolean
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            isInMultiWindowMode
        } else {
            false
        }
    }

/**
 * Iterate through fragments attached to FragmentManager and pop's one after the other.
 */
fun AppCompatActivity.popWholeBackStack() {
    val fragmentManager = this.supportFragmentManager
    for (i in 0 until fragmentManager.fragments.size) {
        fragmentManager.popBackStack()
    }
}

fun Activity.getBitmapFromUri(uri: Uri): Bitmap? {
    return contentResolver.openInputStream(uri)?.use {
        return@use BitmapFactory.decodeStream(it)
    }
}

fun Activity.makeSceneTransitionAnimation(view: View, transitionName: String): ActivityOptionsCompat =
        ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, transitionName)

fun Context.asActivity(): Activity = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext as Activity
    else -> throw IllegalStateException("Context $this NOT contains activity!")
}

fun Context.asFragmentActivity(): FragmentActivity = when (this) {
    is FragmentActivity -> this
    is Activity -> throw IllegalStateException("Context $this NOT support-v4 Activity")
    is ContextWrapper -> baseContext as FragmentActivity
    else -> throw IllegalStateException("Context $this NOT contains activity!")
}

inline fun <reified T : Any> Activity.launchActivityAndFinish() {
    val intent = newIntent<T>(this)
    startActivity(intent)
    finish()
}


fun AppCompatActivity.setupToolbar(
        toolbar: Toolbar,
        displayHomeAsUpEnabled: Boolean = true,
        displayShowHomeEnabled: Boolean = true,
        displayShowTitleEnabled: Boolean = false,
        showUpArrowAsCloseIcon: Boolean = false,
        @DrawableRes closeIconDrawableRes: Int? = null
) {
    setSupportActionBar(toolbar)
    supportActionBar?.apply {
        setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled)
        setDisplayShowHomeEnabled(displayShowHomeEnabled)
        setDisplayShowTitleEnabled(displayShowTitleEnabled)

        if (showUpArrowAsCloseIcon && closeIconDrawableRes != null) {
            setHomeAsUpIndicator(AppCompatResources.getDrawable(this@setupToolbar, closeIconDrawableRes))
        }
    }
}

fun AppCompatActivity.onSupportNavigateUpGoBack(): Boolean {
    onBackPressed()
    return true
}

fun isKeyboardSubmit(actionId: Int, event: KeyEvent?): Boolean =
        actionId == EditorInfo.IME_ACTION_GO ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.action == KeyEvent.ACTION_UP && event.keyCode == KeyEvent.KEYCODE_ENTER)

fun Activity.datePickerContext(): ContextWrapper {
    return object : ContextWrapper(this) {

        private var wrappedResources: Resources? = null

        override fun getResources(): Resources {
            val r = super.getResources()
            if (wrappedResources == null) {
                wrappedResources = object : Resources(r.assets, r.displayMetrics, r.configuration) {
                    @Throws(Resources.NotFoundException::class)
                    override fun getString(id: Int, vararg formatArgs: Any): String {
                        return try {
                            super.getString(id, *formatArgs)
                        } catch (ifce: IllegalFormatConversionException) {
                            Log.e("DatePickerDialogFix", "IllegalFormatConversionException Fixed!", ifce)
                            var template = super.getString(id)
                            template = template.replace(("%" + ifce.conversion).toRegex(), "%s")
                            String.format(configuration.locale, template, *formatArgs)
                        }

                    }
                }
            }
            return wrappedResources as Resources
        }
    }
}

fun Activity.enableFullScreen() {
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
}


fun Activity?.hideKeyboardForced() {
    this?.currentFocus?.let { currentFocus ->
        try {
            (currentFocus.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
        }
    }
}

fun Activity?.hideKeyboard() {
    (this?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

fun Activity?.showKeyboard() {
    (this?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}