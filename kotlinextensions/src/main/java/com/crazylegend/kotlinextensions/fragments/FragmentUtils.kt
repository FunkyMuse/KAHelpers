package com.crazylegend.kotlinextensions.fragments

import android.content.Context
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


/**
 * Created by Hristijan on 2/4/19 to long live and prosper !
 */

fun Context.getFragmentWithTag(tag: String): Fragment? {
    return (this as AppCompatActivity).supportFragmentManager.findFragmentByTag(tag)
}

fun Context.isFragmentWithTagVisible(tag: String): Boolean {
    (this as AppCompatActivity)
    val presentFragment = this.supportFragmentManager.findFragmentByTag(tag)?.isVisible

    return if (presentFragment!=null){
        this.supportFragmentManager.findFragmentByTag(tag) != null &&
                presentFragment
    } else {
        false
    }
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

fun Context.replaceFragment( @Nullable title: String?, @NonNull fragment: Fragment, @Nullable tag: String, @IdRes layoutId: Int) {
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