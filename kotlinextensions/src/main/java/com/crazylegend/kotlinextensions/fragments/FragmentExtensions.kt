package com.crazylegend.kotlinextensions.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.*
import com.crazylegend.kotlinextensions.activity.newIntent
import com.crazylegend.kotlinextensions.context.getColorCompat
import com.crazylegend.kotlinextensions.context.getIntent
import com.crazylegend.kotlinextensions.context.notification
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.orFalse


/**
 * Created by hristijan on 2/22/19 to long live and prosper !
 */


fun Fragment.shortToast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}

fun Fragment.longToast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
}


fun Fragment.colors(@ColorRes stateListRes: Int): ColorStateList? {
    return ContextCompat.getColorStateList(requireContext(), stateListRes)
}

fun Fragment.attribute(value: Int): TypedValue {
    val ret = TypedValue()
    requireContext().theme.resolveAttribute(value, ret, true)
    return ret
}

inline fun <reified T : Any> Fragment.launchActivityAndFinish() {
    launch<T>()
    finish()
}


/**
 * Get color from resource with fragment context.
 */
fun Fragment.compatColor(@ColorRes res: Int) = requireContext().getColorCompat(res)


/**
 * Set arguments to fragment and return current instance
 */
inline fun <reified T : Fragment> T.withArguments(args: Bundle): T {
    this.arguments = args
    return this
}


/**
 * Set target fragment with request code and return current instance
 */
fun Fragment.withTargetFragment(fragment: Fragment, reqCode: Int): Fragment {
    setTargetFragment(fragment, reqCode)
    return this
}

fun Fragment.drawable(@DrawableRes id: Int): Drawable? = ContextCompat.getDrawable(requireContext(), id)

/**
 * Get dimension defined by attribute [attr]
 */
fun Fragment.attrDimen(attr: Int): Int {
    return TypedValue.complexToDimensionPixelSize(attribute(attr).data, resources.displayMetrics)
}

/**
 * Get drawable defined by attribute [attr]
 */
fun Fragment.attrDrawable(attr: Int): Drawable? {
    val a = requireContext().theme.obtainStyledAttributes(intArrayOf(attr))
    val attributeResourceId = a.getResourceId(0, 0)
    a.recycle()
    return drawable(attributeResourceId)
}


fun Fragment.shortToast(resId: Int) = Toast.makeText(requireContext(), resId, Toast.LENGTH_SHORT).show()
fun Fragment.longToast(resId: Int) = Toast.makeText(requireContext(), resId, Toast.LENGTH_LONG).show()

inline fun <reified T : Any> Fragment.launchActivity(
        requestCode: Int = -1,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(requireContext())
    intent.init()

    startActivityForResult(intent, requestCode, options)

}

inline fun <reified T : Any> Fragment.launchActivity(
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(requireContext())
    intent.init()
    startActivity(intent, options)

}

inline fun <reified T> Fragment.startActivityForResult(
        requestCode: Int,
        bundleBuilder: Bundle.() -> Unit = {},
        intentBuilder: Intent.() -> Unit = {}
) {
    val intent = Intent(requireContext(), T::class.java)
    intent.intentBuilder()
    val bundle = Bundle()
    bundle.bundleBuilder()
    startActivityForResult(intent, requestCode, if (bundle.isEmpty) null else bundle)
}

fun Fragment.finish() {
    requireActivity().finish()
}

inline fun <reified T> Fragment.launch() {
    this.requireContext().startActivity(Intent(this.requireContext(), T::class.java))
}


val Fragment.getAppCompatActivity get() = this.requireContext() as AppCompatActivity

inline fun Fragment.notification(body: NotificationCompat.Builder.() -> Unit, channelID: String) =
        requireContext().notification(body, channelID)

fun FragmentActivity.popFragment() {
    val fm = supportFragmentManager
    if (fm.backStackEntryCount == 0) return
    fm.popBackStack()
}

fun Fragment.ifIsAddedAction(action: () -> Unit = {}) {
    if (isAdded) action()

}

fun Fragment.ifIsAttachedAction(action: () -> Unit = {}) {
    if (isAdded && activity != null) action()

}

fun Fragment.ifIsVisibleAction(action: () -> Unit = {}) {
    if (isVisible) action()

}

fun Fragment.ifIsResumedAction(action: () -> Unit = {}) {
    if (isResumed) action()
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

fun AppCompatActivity.getCurrentActiveFragment(@IdRes frameId: Int): Fragment? {
    return supportFragmentManager.findFragmentById(frameId)
}

fun AppCompatActivity.clearAllFragments() {
    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun Fragment.isFragmentPresent(tag: String): Fragment? {
    return parentFragmentManager.findFragmentByTag(tag)
}


fun Fragment.isFragmentPresent(id: Int): Fragment? {
    return parentFragmentManager.findFragmentById(id)
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
    parentFragmentManager.popBackStackImmediate(name, flag)
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

inline fun <reified T> Fragment.intent(body: Intent.() -> Unit): Intent {
    val intent = Intent(requireContext(), T::class.java)
    intent.body()
    return intent
}

inline fun <reified T> Fragment.startActivity(body: Intent.() -> Unit) {
    val intent = Intent(requireContext(), T::class.java)
    intent.body()
    startActivity(intent)
}

inline fun <reified T> FragmentActivity.intent(body: Intent.() -> Unit): Intent {
    val intent = Intent(this, T::class.java)
    intent.body()
    return intent
}

inline fun <reified T> FragmentActivity.startActivity(body: Intent.() -> Unit) {
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
    parentFragmentManager
            .beginTransaction()
            .add(layoutId, fragment, tag)
            .commit()
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
    val activity = this.requireContext() as AppCompatActivity
    return activity.supportFragmentManager.findFragmentByTag(tag)
}

fun Fragment.isFragmentWithTagVisible(tag: String): Boolean {
    val activity = this.requireContext() as AppCompatActivity

    val presentFragment = activity.supportFragmentManager.findFragmentByTag(tag)?.isVisible

    return if (presentFragment != null) {
        activity.supportFragmentManager.findFragmentByTag(tag) != null && presentFragment
    } else {
        false
    }
}

fun Fragment.replaceFragment(@StringRes title: Int, @NonNull fragment: Fragment, @Nullable tag: String, @IdRes layoutId: Int) {
    val activity = this.requireContext() as AppCompatActivity

    activity.supportFragmentManager
            .beginTransaction()
            .replace(layoutId, fragment, tag)
            .commit()
    if (activity.supportActionBar != null) {
        activity.supportActionBar?.setTitle(title)
    }
}

fun Fragment.replaceFragment(@Nullable title: String?, @NonNull fragment: Fragment, @Nullable tag: String, @IdRes layoutId: Int) {
    val activity = this.requireContext() as AppCompatActivity

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
    val activity = this.requireContext() as AppCompatActivity

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

fun Fragment.removeFragmentBackstack() {
    val activity = this.requireContext() as AppCompatActivity
    activity.supportFragmentManager.beginTransaction().remove(this).commitNow()
    activity.supportFragmentManager.popBackStack(this.tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun AppCompatActivity.removeFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction().remove(fragment).commitNow()
}

fun Context.removeFragment(fragment: Fragment) {
    this as AppCompatActivity
    supportFragmentManager.beginTransaction().remove(fragment).commitNow()
}

fun Fragment.removeFragment() {
    val activity = this.requireContext() as AppCompatActivity
    activity.supportFragmentManager.beginTransaction().remove(this).commitNow()
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

fun FragmentManager.applyActions(actions: (fragmentTransaction: FragmentTransaction) -> Unit = { _ -> }) {
    commit {
        actions(this)
    }
}

fun Fragment?.hideKeyboard() {
    (this?.context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

fun Fragment.showKeyboard() {
    (context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.also { inputMethodManager ->
                view?.rootView?.apply { post { inputMethodManager.showSoftInput(this, 0) } }
                        ?: inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }
}