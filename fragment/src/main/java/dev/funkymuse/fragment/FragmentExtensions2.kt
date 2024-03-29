package dev.funkymuse.fragment

import android.app.PictureInPictureParams
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import dev.funkymuse.common.tryOrElse




fun Fragment.hide() {
    this.parentFragmentManager.hide(this)
}

fun Fragment.show() {
    this.parentFragmentManager.show(this)
}

fun Fragment.remove() {
    this.parentFragmentManager.remove(this)
}

fun Fragment.showHide(
        vararg hideFragment: Fragment,
        transaction: Int = FragmentTransaction.TRANSIT_NONE
) {
    this.parentFragmentManager.showHide(this, *hideFragment, transaction = transaction)
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
    for (i in addList.indices) {
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
    val fragments = this.getFragmentManagerFragments()
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
    val frg = getFragmentManagerFragments()
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

    for (tempF in this.getFragmentManagerFragments()) {
        if (tempF != fragmentByTag) {
            ft.hide(tempF)
        }
    }
    ft.commit()
}

fun FragmentManager.getTopFragment(): Fragment? {
    val frg = getFragmentManagerFragments()
    return frg.ifEmpty { return null }[frg.size - 1]
}

fun FragmentManager.getFragmentManagerFragments(): List<Fragment> {
    return this.fragments
}

inline fun <reified T : Fragment> FragmentManager.findFragment(): Fragment? {
    return this.findFragmentByTag(T::class.java.name)
}


@RequiresApi(Build.VERSION_CODES.N)
inline fun Fragment.enterPIPMode(
    builderActions: PictureInPictureParams.Builder.() -> Unit = {}) {
    if (supportsPictureInPicture) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val builder = PictureInPictureParams.Builder()
            builder.builderActions()
            requireActivity().enterPictureInPictureMode(builder.build())
        } else {
            requireActivity().enterPictureInPictureMode()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
inline fun Fragment.checkPIPPermissionAndEnter(
    onCantHandleAction: () -> Unit = {},
    builderActions: PictureInPictureParams.Builder.() -> Unit = {}) {
    if (!requireActivity().isInPictureInPictureMode) {
        if (supportsPictureInPicture) {
            if (hasPipPermission())
                enterPIPMode(builderActions)
            else {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:${requireContext().packageName}")
                tryOrElse(onCantHandleAction) {
                    startActivity(intent)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
inline fun Fragment.checkPIPPermissions(onPermissionDenied: () -> Unit = {}, onPermissionGranted: () -> Unit) {
    if (!requireActivity().isInPictureInPictureMode) {
        if (supportsPictureInPicture) {
            if (hasPipPermission())
                onPermissionGranted()
            else {
                onPermissionDenied()
            }
        }
    }
}

/**
 * Creates an [AutoClearedValueInFragment] associated with this fragment.
 */
fun <T : Any> Fragment.autoCleared() = AutoClearedValueInFragment<T>(this)