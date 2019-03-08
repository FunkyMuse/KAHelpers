package com.crazylegend.kotlinextensions.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.crazylegend.kotlinextensions.context.getIntent
import com.crazylegend.kotlinextensions.context.notification


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

fun AppCompatActivity.clearAllFragment() {
    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
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