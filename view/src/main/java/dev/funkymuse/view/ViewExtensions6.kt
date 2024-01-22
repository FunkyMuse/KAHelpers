package dev.funkymuse.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.max
import kotlin.math.min




private const val DEFAULT_DRAWER_GRAVITY = GravityCompat.START

val DrawerLayout?.isOpen: Boolean get() = this?.isDrawerOpen(GravityCompat.START) ?: false
val DrawerLayout?.isEndOpen: Boolean get() = this?.isDrawerOpen(GravityCompat.END) ?: false
fun DrawerLayout?.open() = this?.openDrawer(GravityCompat.START)
fun DrawerLayout?.openEnd() = this?.openDrawer(GravityCompat.END)
fun DrawerLayout?.close() = this?.closeDrawer(GravityCompat.START)
fun DrawerLayout?.closeEnd() = this?.openDrawer(GravityCompat.END)
fun DrawerLayout?.toggle() = if (isOpen) close() else open()
fun DrawerLayout?.toggleEnd() = if (isEndOpen) closeEnd() else closeEnd()

inline fun DrawerLayout.consume(gravity: Int = GravityCompat.START, func: () -> Unit): Boolean {
    func()
    close()
    return true
}

fun View.setLightStatusBar(condition: Boolean = true) {
    if (Build.VERSION.SDK_INT >= 23 && condition) {
        systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

val Context.hasNavigationBar: Boolean
    get() {
        return !ViewConfiguration.get(this).hasPermanentMenuKey()
    }

/**
 * Return true if navigation bar is at the bottom, false otherwise
 */
val Context.isNavigationBarHorizontal: Boolean
    get() {
        if (!hasNavigationBar) return false
        val dm = resources.displayMetrics
        return !navigationBarCanChangeItsPosition || dm.widthPixels < dm.heightPixels
    }

/**
 * Return true if navigation bar change its position when device rotates, false otherwise
 */
val Context.navigationBarCanChangeItsPosition: Boolean // Only phone between 0-599dp can
    get() {
        val dm = resources.displayMetrics
        return dm.widthPixels != dm.heightPixels && resources.configuration.smallestScreenWidthDp < 600
    }

/**
 * Return the status bar height. 0 otherwise
 */
val Context.statusBarHeight: Int
    get() {
        val id = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(id)
    }

inline fun <T : View> T.onClick(crossinline func: T.() -> Unit) {
    setOnClickListener { func() }
}

inline fun <T : View> T.onLongClick(crossinline func: T.() -> Unit) {
    setOnLongClickListener { func(); true }
}

inline fun <T : View> T.onGlobalLayout(crossinline func: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                func()
            }
        }
    })
}

inline fun <T : View> T.onPreDraw(crossinline func: T.() -> Unit) {
    viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            viewTreeObserver.removeOnPreDrawListener(this)
            func()
            return true
        }
    })
}


fun View.changeBackgroundColor(@ColorInt newColor: Int, duration: Int = 300) {
    val oldBackground = background
    val color = ColorDrawable(newColor)
    val ld = LayerDrawable(arrayOf<Drawable>(color))
    if (oldBackground == null) background = ld
    else {
        val td = TransitionDrawable(arrayOf(oldBackground, ld))
        background = td
        td.startTransition(duration)
    }
}


inline fun Snackbar.maxLines(lines: Int): Snackbar {
    view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action).maxLines = lines
    return this
}


/**
 * Sets receiver's visibility to [View.GONE] if [TextView.getText] is
 * null or empty; sets it to [View.VISIBLE] otherwise.
 */
fun TextView.collapseIfEmpty() {
    visibility = if (!text.isNullOrEmpty()) View.VISIBLE else View.GONE
}

/**
 * Sets receiver's visibility to [View.INVISIBLE] if [TextView.getText] is
 * null or empty; sets it to [View.VISIBLE] otherwise.
 */
fun TextView.hideIfEmpty() {
    visibility = if (!text.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
}


/**
 * Displays a popup by inflating menu with specified
 * [menu resource id][menuResourceId], calling [onClick] when an item
 * is clicked, and optionally calling [onInit] with
 * [PopupMenu] as receiver to initialize prior to display.
 */
fun View.showPopup(
    @MenuRes menuResourceId: Int,
    onInit: PopupMenu.() -> Unit = {},
    onClick: (MenuItem) -> Boolean
) {
    PopupMenu(context, this).apply {
        menuInflater.inflate(menuResourceId, menu)
        onInit(this)
        setOnMenuItemClickListener(onClick)
    }.show()
}

/**
 * Sets receiver's visibility to [View.INVISIBLE] if [value] is true;
 * sets it to [View.VISIBLE] otherwise. Opposite of [showIf]; also
 * see [collapseIf].
 */
fun View.hideIf(value: Boolean) {
    visibility = if (!value) View.VISIBLE else View.INVISIBLE
}

/**
 * Sets receiver's visibility to [View.GONE] if [value] is true;
 * sets it to [View.VISIBLE] otherwise. Opposite of [expandIf]; also
 * see [hideIf].
 */
fun View.collapseIf(value: Boolean) {
    visibility = if (!value) View.VISIBLE else View.GONE
}

inline fun <T : Adapter> AdapterView<T>.onItemSelected(crossinline action: (parent: AdapterView<*>?, view: View?, position: Int, id: Long) -> Unit = { _, _, _, _ -> }) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
            action(parent, view, position, id)
    }
}

/**
 * Returns the default, clear background for selectable items.  Reacts when touched.
 */
val View.selectableItemBackgroundResource: Int
    get() {
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        return outValue.resourceId
    }

/**
 * Returns the default, clear background for selectable items without a border.  Reacts when touched.
 */
val View.selectableItemBackgroundBorderlessResource: Int
    get() {
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true)
        return outValue.resourceId
    }

/**
 * Allows you to modify the elevation on a view without worrying about version.
 */
var View.elevationCompat: Float
    get() {
        return elevation
    }
    set(value) {
        elevation = value
    }

/**
 * Shows the soft input for the vindow.
 */
fun View.showSoftInput() {
    context.getSystemService(Context.INPUT_METHOD_SERVICE).let { it as InputMethodManager }.showSoftInput(this, 0)
}

/**
 * Hides the soft input for the vindow.
 */
fun View.hideSoftInput() {
    context.getSystemService(Context.INPUT_METHOD_SERVICE).let { it as InputMethodManager }
        .hideSoftInputFromWindow(this.applicationWindowToken, 0)
}

/**
 * Sets an on click listener for a view, but ensures the action cannot be triggered more often than [coolDown] milliseconds.
 */
inline fun View.setOnClickListenerCooldown(coolDown: Long = 1000L, crossinline action: (view: View) -> Unit) {
    setOnClickListener(object : View.OnClickListener {
        var lastTime = 0L
        override fun onClick(v: View) {
            val now = System.currentTimeMillis()
            if (now - lastTime > coolDown) {
                action(v)
                lastTime = now
            }
        }
    })
}

/**
 * Post functions
 */
inline fun <T : View> T.postLet(crossinline block: (T) -> Unit) {
    post { block(this) }
}

inline fun <T : View> T.postDelayedLet(delay: Long, crossinline block: (T) -> Unit) {
    postDelayed({ block(this) }, delay)
}

inline fun <T : View> T.postApply(crossinline block: T.() -> Unit) {
    post { block(this) }
}

inline fun <T : View> T.postDelayedApply(delay: Long, crossinline block: T.() -> Unit) {
    postDelayed({ block(this) }, delay)
}

fun TabLayout.addTab(@StringRes title: Int, @DrawableRes icon: Int, @LayoutRes customView: Int) {
    val tab = LayoutInflater.from(context).inflate(customView, this as ViewGroup, false) as TextView
    tab.setText(title)
    tab.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
    addTab(newTab().setCustomView(tab))
}

fun TabLayout.updateTabAt(position: Int, @StringRes title: Int, @DrawableRes icon: Int, @LayoutRes customView: Int) {
    val tab = LayoutInflater.from(context).inflate(customView, this as ViewGroup, false) as TextView
    tab.setText(title)
    tab.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
    getTabAt(position)?.customView = tab
}

fun TabLayout.Tabs(): List<TabLayout.Tab> {

    val tabs = mutableListOf<TabLayout.Tab>()

    (0..tabCount).forEach { index: Int ->
        getTabAt(index)?.let { tabs.add(it) }
    }

    return tabs
}

fun TextInputLayout.setTextInputLayoutUpperHintColor(@ColorInt color: Int) {
    defaultHintTextColor = ColorStateList(arrayOf(intArrayOf()), intArrayOf(color))
}

val View.centerX
    get() = x + width / 2

val View.centerY
    get() = y + height / 2


/**
 * Restricts [Int] to be within a [min] and a [max] value
 */
fun Int.clamp(min: Int, max: Int): Int {
    return max(min, min(max, this))
}

/**
 * Adds a leading zero if only one digit
 */
fun Int.padWithZero(): String {
    return String.format("%02d", this)
}

fun TextInputLayout.toggleTextHintColorOnEmpty(activeColor: Int, inactiveColor: Int) = setTextInputLayoutUpperHintColor(
    if (editText?.text?.isNotEmpty() == true)
        activeColor else
        inactiveColor
)

fun View.afterLatestMeasured(callback: () -> Unit) {
    this.post {
        callback()
    }
}

fun View.isLaidOutCompat(): Boolean {
    return ViewCompat.isLaidOut(this)
}

fun View.setbackgroundColorResource(@ColorRes resId: Int) {
    setBackgroundColor(ContextCompat.getColor(context, resId))
}

fun View.toggleVisibility() {
    if (isVisible) gone() else visible()
}


infix fun View.and(v: View): List<View> {
    return mutableListOf(this, v)
}

infix fun List<View>.and(v: View): List<View> {
    val list = mutableListOf<View>()
    list.addAll(this)
    list.add(v)
    return list
}
