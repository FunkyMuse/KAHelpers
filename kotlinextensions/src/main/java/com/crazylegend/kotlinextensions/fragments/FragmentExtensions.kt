package com.crazylegend.kotlinextensions.fragments

import android.content.Intent
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
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