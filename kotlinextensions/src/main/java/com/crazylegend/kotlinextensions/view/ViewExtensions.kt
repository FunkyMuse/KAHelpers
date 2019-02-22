package com.crazylegend.kotlinextensions.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.transition.TransitionManager
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText

/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */


fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

val TextInputEditText.getString: String get() = this.text.toString()
val TextInputEditText.getStringTrimmed: String get() = this.text.toString().trim()

fun TextInputEditText.setTheText(text: String) {
    this.setText(text, TextView.BufferType.EDITABLE)
}


val EditText.getString: String get() = this.text.toString()
val EditText.getStringTrimmed: String get() = this.text.toString().trim()


fun EditText.setTheText(text: String) {
    this.setText(text, TextView.BufferType.EDITABLE)
}

val SearchView?.editTextSearchView get() = this?.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)


private var viewOriginalHeight: Int = 0

fun Button.enableButtonWithLoading( progressBar: ProgressBar) {
    progressBar.visibility = View.GONE
    if (this.tag != null) {
        this.text = this.tag.toString()
    }
    this.isEnabled = true
    this.alpha = 1f
}

fun Button.disableButtonWithLoading(progressBar: ProgressBar) {
    progressBar.visibility = View.VISIBLE
    this.tag = this.text
    this.text = ""
    this.isEnabled = false
    this.alpha = 0.7.toFloat()
}

fun Button.enableButton(color: Int) {
    this.isEnabled = true
    this.alpha = 1f
}

fun Button.disableButton(color: Int) {
    this.isEnabled = false
    this.alpha = 0.7.toFloat()
}

fun collapseLayout(linearLayout: LinearLayout, imageView: ImageView, dropUPIMG:Int, dropDOWNIMG:Int) {
    var firstClick = false

    imageView.setOnClickListener {
        if (firstClick) {

            TransitionManager.beginDelayedTransition(linearLayout)
            linearLayout.visibility = View.GONE
            imageView.setImageResource(dropDOWNIMG)

            firstClick = false

        } else {
            TransitionManager.beginDelayedTransition(linearLayout)
            linearLayout.visibility = View.VISIBLE
            imageView.setImageResource(dropUPIMG)

            firstClick = true

        }
    }


}


fun adjustAlpha(@ColorInt color: Int, factor: Float): Int {
    val alpha = Math.round(Color.alpha(color) * factor)
    val red = Color.red(color)
    val green = Color.green(color)
    val blue = Color.blue(color)
    return Color.argb(alpha, red, green, blue)
}

fun bottomBarColor(bottomBar: BottomNavigationView, color: Int) {
    try {
        val mMenuViewField = BottomNavigationView::class.java.getDeclaredField("mMenuView")
        mMenuViewField.isAccessible = true
        val mMenuView = mMenuViewField.get(bottomBar) as BottomNavigationMenuView
        val mButtonsField = BottomNavigationMenuView::class.java.getDeclaredField("mButtons")
        mButtonsField.isAccessible = true
        val mButtons = mButtonsField.get(mMenuView) as Array<*>

        for (item in mButtons) {
            item as BottomNavigationItemView
            ViewCompat.setBackground(item, ColorDrawable(color))
        }
    } catch (e: NoSuchFieldException) {
        e.printStackTrace()
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    }

}