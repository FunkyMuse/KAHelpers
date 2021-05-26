package com.crazylegend.kotlinextensions.views

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.SeekBar
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Hristijan, date 5/26/21
 */


fun List<View>.gone() {
    this.forEach { it.gone() }
}

fun List<View>.invisible() {
    this.forEach { it.invisible() }
}

fun List<View>.visible() {
    this.forEach { it.visible() }
}


fun SearchView.textListener(
    onQuerySubmit: (queryTextSubmit: String) -> Unit = { _ -> },
    onQueryChange: (queryTextChange: String) -> Unit = { _ -> }
) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            onQuerySubmit(query.toString())
            return true
        }

        override fun onQueryTextChange(query: String?): Boolean {
            onQueryChange(query.toString())
            return true
        }
    })
}

val SearchView?.getSubmitButton get() = this?.findViewById<ImageView>(androidx.appcompat.R.id.search_go_btn)
val SearchView?.getCloseButton get() = this?.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
val SearchView?.getVoiceButton get() = this?.findViewById<ImageView>(androidx.appcompat.R.id.search_voice_btn)
val SearchView?.getCollapsedIcon get() = this?.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)


fun BottomSheetBehavior<*>.sliderListener(
    onSlide: (bottomSheet: View, slideOffset: Float) -> Unit = { _, _ -> },
    onStateChanged: (bottomSheet: View, newState: Int) -> Unit = { _, _ -> }
) {


    this.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            onSlide(bottomSheet, slideOffset)
        }


        override fun onStateChanged(bottomSheet: View, newState: Int) {
            onStateChanged(bottomSheet, newState)
        }
    })

}

fun View.enable() {
    this.isEnabled = true
}

fun View.toggleEnabled() {
    this.isEnabled = !this.isEnabled
}

fun View.disable() {
    this.isEnabled = false
}

fun View.toggleSelected() {
    this.isSelected = !this.isSelected
}

fun View.windowBackground(): Int {
    return context.themeAttributeToColor(android.R.attr.windowBackground)
}

// Used to tint buttons
fun Context.textColorTertiary(): Int {
    return this.themeAttributeToColor(android.R.attr.textColorTertiary)
}

fun MenuItem.disable() {
    this.isEnabled = false
}

fun MenuItem.enable() {
    this.isEnabled = true
}

fun MenuItem.toggleEnabled() {
    this.isEnabled = !this.isEnabled
}

fun MenuItem.check() {
    this.isChecked = true
}

fun MenuItem.unCheck() {
    this.isChecked = false
}

fun MenuItem.toggleChecked() {
    this.isChecked = !this.isChecked
}

fun Context.themeAttributeToColor(themeAttributeId: Int, fallbackColor: Int = Color.WHITE): Int {
    val outValue = TypedValue()
    val theme = this.theme
    val resolved = theme.resolveAttribute(themeAttributeId, outValue, true)
    if (resolved) {
        return ContextCompat.getColor(this, outValue.resourceId)
    }
    return fallbackColor
}

fun SeekBar.onProgressChanged(callback: (theSeekBar: SeekBar, progress: Int, fromUser: Boolean) -> Unit) {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onStopTrackingTouch(seekBar: SeekBar) = Unit

        override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) =
            callback(seekBar, progress, fromUser)
    })
}


fun CompoundButton.onChecked(onChecked: (View, Boolean) -> Unit) {
    setOnCheckedChangeListener { buttonView, isChecked ->
        onChecked(buttonView, isChecked)
    }
}

fun View.dismissKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}


fun View.snackbar(
    message: Int, duration: Int = Snackbar.LENGTH_SHORT,
    actionName: Int = 0, actionTextColor: Int = 0, action: (View) -> Unit = {}
): Snackbar {
    val snackbar = Snackbar.make(this, message, duration)

    if (actionName != 0 && action != {}) snackbar.setAction(actionName, action)
    if (actionTextColor != 0) snackbar.setActionTextColor(actionTextColor)

    snackbar.show()
    return snackbar
}

fun View.snackbar(
    message: Int, duration: Int = Snackbar.LENGTH_SHORT,
    actionName: String = "", actionTextColor: Int = 0, action: (View) -> Unit = {}
): Snackbar {
    val snackbar = Snackbar.make(this, message, duration)

    if (actionName != "" && action != {}) snackbar.setAction(actionName, action)
    if (actionTextColor != 0) snackbar.setActionTextColor(actionTextColor)

    snackbar.show()
    return snackbar
}

fun View.snackbar(
    message: String, duration: Int = Snackbar.LENGTH_SHORT,
    actionName: Int = 0, actionTextColor: Int = 0, action: (View) -> Unit = {}
): Snackbar {
    val snackbar = Snackbar.make(this, message, duration)

    if (actionName != 0 && action != {}) snackbar.setAction(actionName, action)
    if (actionTextColor != 0) snackbar.setActionTextColor(actionTextColor)

    snackbar.show()
    return snackbar
}

fun View.snackbar(
    message: String, duration: Int = Snackbar.LENGTH_SHORT,
    actionName: String = "", actionTextColor: Int = 0, action: (View) -> Unit = {}
): Snackbar {
    val snackbar = Snackbar.make(this, message, duration)

    if (actionName != "" && action != {}) snackbar.setAction(actionName, action)
    if (actionTextColor != 0) snackbar.setActionTextColor(actionTextColor)

    snackbar.show()
    return snackbar
}

fun View.animateTranslationX(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateTranslationY(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}


fun View.animateTranslationZ(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Z, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateScaleX(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.SCALE_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateScaleY(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.SCALE_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateAlpha(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.ALPHA, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateRotation(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateRotationX(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateRotationY(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateX(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateY(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}


fun View.animateZ(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.Z, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.translationXAnimator(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.translationYAnimator(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}


fun View.translationZAnimator(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Z, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.scaleXAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.SCALE_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.scaleYAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.SCALE_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.alphaAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.ALPHA, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.rotationAnimator(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.rotationXAnimator(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.rotationYAnimator(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

