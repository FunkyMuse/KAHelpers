package com.crazylegend.kotlinextensions.views

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.core.view.forEach
import androidx.core.widget.NestedScrollView
import androidx.viewpager.widget.ViewPager
import kotlin.math.min


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */


fun Activity.setSystemBarColor(@ColorRes color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, color)
    }
}

fun Context.setSystemBarColorDialog(dialog: Dialog, @ColorRes color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = dialog.window
        window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, color)
    }
}

fun Activity.setSystemBarLight() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val view = findViewById<View>(android.R.id.content)
        var flags = view.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        view.systemUiVisibility = flags
    }
}

fun Dialog.setSystemBarLightDialog() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val view = this.findViewById<View>(android.R.id.content)
        var flags = view.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        view.systemUiVisibility = flags
    }
}


fun Activity.clearSystemBarLight(@ColorRes color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val window = window
        window.statusBarColor = ContextCompat.getColor(this, color)
    }
}

/**
 * Making notification bar transparent
 */
fun Activity.setSystemBarTransparent() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }
}

fun getEmailFromName(name: String?): String? {
    return if (name != null && name != "") {
        name.replace(" ".toRegex(), ".").toLowerCase() + "@mail.com"
    } else name
}

fun NestedScrollView.nestedScrollTo(targetView: View) {
    doOnLayout { scrollTo(500, targetView.bottom) }
}

fun dip2px(context: Context, dpValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun px2dip(context: Context, pxValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

fun View.toggleArrow(duration: Long = 200): Boolean {
    return if (rotation == 0f) {
        animate().setDuration(duration).rotation(180f)
        true
    } else {
        animate().setDuration(duration).rotation(0f)
        false
    }
}

@JvmOverloads
fun toggleArrow(show: Boolean, view: View, delay: Boolean = true): Boolean {
    return if (show) {
        view.animate().setDuration((if (delay) 200 else 0).toLong()).rotation(180f)
        true
    } else {
        view.animate().setDuration((if (delay) 200 else 0).toLong()).rotation(0f)
        false
    }
}

fun Toolbar.changeNavigateionIconColor(@ColorInt color: Int) {
    val drawable = this.navigationIcon
    drawable?.apply {
        mutate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }
}

fun Menu.changeMenuIconColor(@ColorInt color: Int) {
    for (i in 0 until this.size()) {
        val drawable = this.getItem(i).icon
        drawable?.apply {
            mutate()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
            } else {
                setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            }
        }
    }
}

fun Toolbar.changeOverflowMenuIconColor(@ColorInt color: Int) {
    try {
        val drawable = this.overflowIcon
        drawable?.apply {
            mutate()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
            } else {
                setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

}


fun insertPeriodically(text: String, insert: String, period: Int): String {
    val builder = StringBuilder(
            text.length + insert.length * (text.length / period) + 1
    )
    var index = 0
    var prefix = ""
    while (index < text.length) {
        builder.append(prefix)
        prefix = insert
        builder.append(text.substring(index, min(index + period, text.length)))
        index += period
    }
    return builder.toString()
}


fun Spinner.create(@LayoutRes itemLayout: Int, @IdRes textViewId: Int, items: Array<String>,
                   onItemSelected: (String, Int) -> Unit = { _, _ -> }) {
    val aAdapter = ArrayAdapter(context, itemLayout, textViewId, items)
    adapter = aAdapter
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            onItemSelected(items[position], position)
        }
    }
}

fun Spinner.create(@LayoutRes itemLayout: Int, @IdRes textViewId: Int, items: MutableList<String>,
                   onItemSelected: (String, Int) -> Unit = { _, _ -> }) {
    val aAdapter = ArrayAdapter(context, itemLayout, textViewId, items)
    adapter = aAdapter
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            onItemSelected(items[position], position)
        }
    }
}

fun AutoCompleteTextView.create(@LayoutRes itemLayout: Int, @IdRes textViewId: Int, items: Array<String>,
                                onItemSelected: (String, Int) -> Unit = { _, _ -> }) {
    val adapter = ArrayAdapter(context, itemLayout, textViewId, items)
    setAdapter(adapter)
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            onItemSelected(items[position], position)
        }
    }
}

fun AutoCompleteTextView.create(@LayoutRes itemLayout: Int, @IdRes textViewId: Int, items: MutableList<String>,
                                onItemSelected: (String, Int) -> Unit = { _, _ -> }) {
    val adapter = ArrayAdapter(context, itemLayout, textViewId, items)
    setAdapter(adapter)
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            onItemSelected(items[position], position)
        }
    }
}


fun ViewPager.onPageScrollStateChanged(onPageScrollStateChanged: (Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            onPageScrollStateChanged(state)
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
        }

    })
}


/** Performs the given action on each item in this menu. */
inline fun Menu.filter(action: (item: MenuItem) -> Boolean): List<MenuItem> {
    val filteredItems = mutableListOf<MenuItem>()
    this.forEach {
        if (action.invoke(it)) filteredItems.add(it)
    }
    return filteredItems
}


fun CompoundButton.setCheckedWithoutAnimation(checked: Boolean) {
    val beforeVisibility = visibility
    visibility = View.INVISIBLE
    isChecked = checked
    visibility = beforeVisibility
}