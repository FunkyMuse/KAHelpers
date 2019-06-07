package com.crazylegend.kotlinextensions.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.crazylegend.kotlinextensions.context.getIntent
import com.crazylegend.kotlinextensions.context.notification
import com.crazylegend.kotlinextensions.log.debug


/**
 * Created by hristijan on 2/22/19 to long live and prosper !
 */


fun Fragment.shortToast(text: String) {
    Toast.makeText(this.requireActivity(), text, Toast.LENGTH_SHORT).show()
}

fun Fragment.longToast(text: String) {
    Toast.makeText(this.requireActivity(), text, Toast.LENGTH_LONG).show()
}

fun Fragment.finish() {
    this.requireActivity().finish()
}

inline fun <reified T> Fragment.launch() {
    this.requireActivity().startActivity(Intent(this.requireActivity(), T::class.java))
}


val Fragment.getAppCompatActivity get() = this.requireActivity() as AppCompatActivity

inline fun Fragment.notification(body: NotificationCompat.Builder.() -> Unit, channelID: String) =
    requireActivity().notification(body, channelID)

fun FragmentActivity.popFragment() {
    val fm = supportFragmentManager
    if (fm.backStackEntryCount == 0) return
    fm.popBackStack()
}

fun FragmentActivity.popFragment(name: String, flags: Int) {
    val fm = supportFragmentManager
    if (fm.backStackEntryCount == 0) return
    fm.popBackStack(name, flags)
}

fun FragmentActivity.popFragment(id: Int, flags: Int) {
    val fm = supportFragmentManager
    if (fm.backStackEntryCount == 0) return
    fm.popBackStack(id, flags)
}

fun AppCompatActivity.getCurrentActiveFragment(@IdRes frameId : Int): Fragment? {
    return supportFragmentManager.findFragmentById(frameId)
}

fun AppCompatActivity.clearAllFragments() {
    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun Fragment.isFragmentPresent(tag:String): Fragment? {
    return fragmentManager?.findFragmentByTag(tag)
}


fun Fragment.isFragmentPresent(id:Int): Fragment? {
    return fragmentManager?.findFragmentById(id)
}





/**
 * Calls fragment's `setHasOptionMenu` with `true` as default
 * @receiver Fragment
 * @param[hasOptionsMenu]: Default `true`, Pass false to not have options menu
 */
fun Fragment.allowOptionsMenu(hasOptionsMenu: Boolean = true) {
    setHasOptionsMenu(hasOptionsMenu)
}

/**
 * Call's Parent activity's `setSupportActionBar` from Fragment
 * @receiver Fragment
 * @param[toolbar]: Toolbar to set support actionbar
 */
fun Fragment.setSupportActionbar(toolbar: Toolbar) {
    val appcompatActivity = this.activity as AppCompatActivity?
    appcompatActivity?.setSupportActionBar(toolbar)
}

/**
 * Go back to fragment whose tag matches with name
 * @param[name]: Name of the tag.
 * @param[flag]: Flag, Defaults to 0, optionally you can pass POP_BACKSTACK_INCLUSIVE
 * @receiver FragmentActivity
 */
fun FragmentActivity.goBackToFragment(name: String, flag: Int = 0) {
    supportFragmentManager.popBackStackImmediate(name, flag)
}

/**
 * Go back to fragment whose tag matches with name
 * @param[name]: Name of the tag.
 * @param[flag]: Flag, Defaults to 0, optionally you can pass POP_BACKSTACK_INCLUSIVE
 * @receiver Fragment
 */
fun Fragment.goBackToFragment(name: String, flag: Int = 0) {
    fragmentManager?.popBackStackImmediate(name, flag)
}


/**
 * Starts Activity
 * @param[flags] Flags to pass to the intent
 * @param[data] Uri to pass to intent
 * @param[extras] Extra to pass to intent
 */
inline fun <reified T : Activity> Fragment.startActivity(flags: Int = 0,
                                                         data: Uri? = null,
                                                         extras: Bundle? = null) = this.startActivity(
    getIntent<T>(flags, extras, data))

/**
 * Calls `startActivityForResult` using given flags, bundles and url
 * @param[flags] Flags to pass to the intent
 * @param[data] Uri to pass to intent
 * @param[extras] Extra to pass to intent
 */
inline fun <reified T : Activity> Fragment.startActivityForResult(
    flags: Int = 0,
    data: Uri? = null,
    extras: Bundle? = null, requestCode: Int) = this.startActivityForResult(getIntent<T>
    (flags, extras, data),
    requestCode)

/**
 * Generates intent from Fragment
 * @return: Generated intent from Flags, Data and Bundle.
 * @param[flags] Flags to pass to the intent
 * @param[data] Uri to pass to intent
 * @param[bundle] Extra to pass to intent
 */
inline fun <reified T : Context> Fragment.getIntent(flags: Int = 0,
                                                    bundle: Bundle? = null,
                                                    data: Uri? = null
): Intent? {
    return context?.getIntent<T>(flags, bundle, data)
}

fun Fragment.alert(style: Int, init: AlertDialog.Builder .() -> Unit) {
    val contextWrapper = ContextThemeWrapper(context, style)
    val builder = AlertDialog.Builder(contextWrapper)
    builder.init()
    builder.create()
    builder.show()
}

inline fun <reified T> Fragment.intent(body: Intent.() -> Unit ): Intent {
    val intent = Intent(requireActivity(), T::class.java)
    intent.body()
    return intent
}

inline fun <reified T> Fragment.startActivity(body: Intent.() -> Unit ) {
    val intent = Intent(requireActivity(), T::class.java)
    intent.body()
    startActivity(intent)
}

inline fun <reified T> FragmentActivity.intent(body: Intent.() -> Unit ): Intent {
    val intent = Intent(this, T::class.java)
    intent.body()
    return intent
}

inline fun <reified T> FragmentActivity.startActivity(body: Intent.() -> Unit ) {
    val intent = Intent(this, T::class.java)
    intent.body()
    startActivity(intent)
}

fun Context.getFragmentWithTag(tag: String): Fragment? {
    return (this as AppCompatActivity).supportFragmentManager.findFragmentByTag(tag)
}

fun Context.isFragmentWithTagVisible(tag: String): Boolean {
    (this as AppCompatActivity)
    val presentFragment = this.supportFragmentManager.findFragmentByTag(tag)?.isVisible

    return if (presentFragment != null) {
        this.supportFragmentManager.findFragmentByTag(tag) != null && presentFragment
    } else {
        false
    }
}

fun AppCompatActivity.addFragment(@NonNull fragment: Fragment, @Nullable tag: String, @IdRes layoutId: Int) {
    supportFragmentManager
        .beginTransaction()
        .add(layoutId, fragment, tag)
        .commit()
}

fun Fragment.addFragment(@NonNull fragment: Fragment, @Nullable tag: String, @IdRes layoutId: Int) {
    fragmentManager
        ?.beginTransaction()
        ?.add(layoutId, fragment, tag)
        ?.commit()
}

fun Context.replaceFragment(@StringRes title: Int, @NonNull fragment: Fragment, @Nullable tag: String, @IdRes layoutId: Int) {
    (this as AppCompatActivity)
        .supportFragmentManager
        .beginTransaction()
        .replace(layoutId, fragment, tag)
        .commit()
    if (this.supportActionBar != null) {
        this.supportActionBar?.setTitle(title)
    }
}

fun Context.replaceFragment(@Nullable title: String?, @NonNull fragment: Fragment, @Nullable tag: String, @IdRes layoutId: Int) {
    (this as AppCompatActivity)
        .supportFragmentManager
        .beginTransaction()
        .replace(layoutId, fragment, tag)
        .commit()
    if (title != null) {
        if (this.supportActionBar != null) {
            this.supportActionBar?.title = title
        }
    }
}

fun Context.addFragment(@Nullable title: String?, @NonNull fragment: Fragment, @Nullable tag: String, @IdRes layoutId: Int) {
    (this as AppCompatActivity)
        .supportFragmentManager
        .beginTransaction()
        .add(layoutId, fragment, tag)
        .commit()
    if (title != null) {
        if (this.supportActionBar != null) {
            this.supportActionBar?.title = title
        }
    }
}


fun Fragment.getFragmentWithTag(tag: String): Fragment? {
    val activity = this.requireActivity() as AppCompatActivity
    return activity.supportFragmentManager.findFragmentByTag(tag)
}

fun Fragment.isFragmentWithTagVisible(tag: String): Boolean {
    val activity = this.requireActivity() as AppCompatActivity

    val presentFragment = activity.supportFragmentManager.findFragmentByTag(tag)?.isVisible

    return if (presentFragment != null) {
        activity.supportFragmentManager.findFragmentByTag(tag) != null && presentFragment
    } else {
        false
    }
}

fun Fragment.replaceFragment(@StringRes title: Int, @NonNull fragment: Fragment, @Nullable tag: String, @IdRes layoutId: Int) {
    val activity = this.requireActivity() as AppCompatActivity

    activity.supportFragmentManager
        .beginTransaction()
        .replace(layoutId, fragment, tag)
        .commit()
    if (activity.supportActionBar != null) {
        activity.supportActionBar?.setTitle(title)
    }
}

fun Fragment.replaceFragment(@Nullable title: String?, @NonNull fragment: Fragment, @Nullable tag: String, @IdRes layoutId: Int) {
    val activity = this.requireActivity() as AppCompatActivity

    activity.supportFragmentManager
        .beginTransaction()
        .replace(layoutId, fragment, tag)
        .commit()
    if (title != null) {
        if (activity.supportActionBar != null) {
            activity.supportActionBar?.title = title
        }
    }
}

fun Fragment.addFragment(@Nullable title: String?, @NonNull fragment: Fragment, @Nullable tag: String, @IdRes layoutId: Int) {
    val activity = this.requireActivity() as AppCompatActivity

    activity.supportFragmentManager
        .beginTransaction()
        .add(layoutId, fragment, tag)
        .commit()
    if (title != null) {
        if (activity.supportActionBar != null) {
            activity.supportActionBar?.title = title
        }
    }
}


fun AppCompatActivity.getFragmentWithTag(tag: String): Fragment? {
    return this.supportFragmentManager.findFragmentByTag(tag)
}

fun AppCompatActivity.isFragmentWithTagVisible(tag: String): Boolean {
    val presentFragment = this.supportFragmentManager.findFragmentByTag(tag)?.isVisible

    return if (presentFragment != null) {
        this.supportFragmentManager.findFragmentByTag(tag) != null && presentFragment
    } else {
        false
    }
}

fun AppCompatActivity.replaceFragment(@StringRes title: Int, @NonNull fragment: Fragment, @Nullable tag: String, @IdRes layoutId: Int) {
    supportFragmentManager
        .beginTransaction()
        .replace(layoutId, fragment, tag)
        .commit()
    if (this.supportActionBar != null) {
        this.supportActionBar?.setTitle(title)
    }
}

fun AppCompatActivity.replaceFragment(@Nullable title: String?, @NonNull fragment: Fragment, @Nullable tag: String, @IdRes layoutId: Int) {
    supportFragmentManager
        .beginTransaction()
        .replace(layoutId, fragment, tag)
        .commit()
    if (title != null) {
        if (this.supportActionBar != null) {
            this.supportActionBar?.title = title
        }
    }
}

fun AppCompatActivity.addFragment(@Nullable title: String?, @NonNull fragment: Fragment, @Nullable tag: String, @IdRes layoutId: Int) {
    supportFragmentManager
        .beginTransaction()
        .add(layoutId, fragment, tag)
        .commit()
    if (title != null) {
        if (this.supportActionBar != null) {
            this.supportActionBar?.title = title
        }
    }
}


fun AppCompatActivity.removeFragmentBackstack(fragment: Fragment) {
    supportFragmentManager.beginTransaction().remove(fragment).commitNow()
    supportFragmentManager.popBackStack(fragment.tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun Context.removeFragmentBackstack(fragment: Fragment) {
    this as AppCompatActivity
    supportFragmentManager.beginTransaction().remove(fragment).commitNow()
    supportFragmentManager.popBackStack(fragment.tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun Fragment.removeFragmentBackstack(fragment: Fragment) {
    val activity = this.requireActivity() as AppCompatActivity
    activity.supportFragmentManager.beginTransaction().remove(fragment).commitNow()
    activity.supportFragmentManager.popBackStack(fragment.tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun AppCompatActivity.removeFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction().remove(fragment).commitNow()
}

fun Context.removeFragment(fragment: Fragment) {
    this as AppCompatActivity
    supportFragmentManager.beginTransaction().remove(fragment).commitNow()
}

fun Fragment.removeFragment(fragment: Fragment) {
    val activity = this.requireActivity() as AppCompatActivity
    activity.supportFragmentManager.beginTransaction().remove(fragment).commitNow()
}

fun AppCompatActivity.printBackStack() {
    this.debug("Fragment", "Current BackStack:  " + supportFragmentManager.backStackEntryCount)
    for (entry in 0 until supportFragmentManager.backStackEntryCount) {
        val stackEntry = supportFragmentManager.getBackStackEntryAt(entry)
        this.debug("Fragment", "[" + stackEntry.id + "] " + stackEntry.name)
    }
}

fun AppCompatActivity.currentFragment(@IdRes container: Int): Fragment? {
    return supportFragmentManager.findFragmentById(container)
}

fun FragmentActivity.isFragmentAtTheTop(fragment: Fragment): Boolean =
        supportFragmentManager.fragments.last() == fragment

fun AppCompatActivity.isFragmentAtTheTop(fragment: Fragment): Boolean =
        supportFragmentManager.fragments.last() == fragment


inline fun FragmentActivity.inTransaction(
        allowStateLoss: Boolean = false,
        block: FragmentTransaction.() -> Unit
) {
    with(supportFragmentManager) {
        beginTransaction().apply {
            block(this)

            if (!isStateSaved) {
                commit()
            } else if (allowStateLoss) {
                commitAllowingStateLoss()
            }
        }
    }
}

inline fun Fragment.inTransaction(
        allowStateLoss: Boolean = false,
        block: FragmentTransaction.() -> Unit
) {
    activity?.inTransaction(allowStateLoss, block)
}

fun Fragment.navigateBack() {
    activity?.onBackPressed()
}

fun Fragment.isAtTheTop(): Boolean = activity?.isFragmentAtTheTop(this).orFalse()