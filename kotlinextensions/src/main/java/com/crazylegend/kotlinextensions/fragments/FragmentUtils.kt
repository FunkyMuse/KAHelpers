package com.crazylegend.kotlinextensions.fragments

import android.content.Context
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


/**
 * Created by Hristijan on 2/4/19 to long live and prosper !
 */

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
