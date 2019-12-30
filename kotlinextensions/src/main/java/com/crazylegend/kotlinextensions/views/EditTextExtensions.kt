package com.crazylegend.kotlinextensions.views

import android.annotation.SuppressLint
import android.os.Handler
import android.text.*
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import com.crazylegend.kotlinextensions.activity.isKeyboardSubmit
import com.crazylegend.kotlinextensions.context.clipboardManager
import com.crazylegend.kotlinextensions.context.getCompatDrawable
import com.crazylegend.kotlinextensions.context.inputMethodManager
import com.google.android.material.textfield.TextInputEditText
import java.net.MalformedURLException
import java.net.URL


/**
 * Created by hristijan on 3/4/19 to long live and prosper !
 */

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

fun EditText.isNotEmpty(): Boolean {
    return this.getString.isNotEmpty()
}

inline fun EditText.onKeyboardSubmit(crossinline block: EditText.() -> Unit) {
    setOnEditorActionListener { _, actionId, event ->
        return@setOnEditorActionListener if (isKeyboardSubmit(actionId, event)) {
            block()
            true
        } else {
            false
        }
    }
}

fun EditText.selectEnd() {
    if (!isFocused)
        return

    post {
        setSelection(text.toString().length)
    }
}

fun EditText.updateCursorLocation(index: Int = text.length) {
    Selection.setSelection(text, index)
}


fun EditText.isEmpty(): Boolean {
    return text.toString().isEmpty()
}


fun EditText.getTextLength(): Int {
    return text.length
}

fun EditText.getTextInt(): Int? {
    return text.toString().toIntOrNull()
}

fun EditText.getTextFloat(): Float? {
    return text.toString().toFloatOrNull()
}

fun EditText.getTextDouble(): Double? {
    return text.toString().toDoubleOrNull()
}

/**
 * Accepts 3 text watcher methods with a default empty implementation.
 *
 * @return The `TextWatcher` being added to EditText
 */
fun EditText.addTextWatcher(
    afterTextChanged: (text: Editable?) -> Unit = { _ -> },
    beforeTextChanged: (text: CharSequence?, start: Int, count: Int, after: Int) -> Unit = { _, _, _, _ -> },
    onTextChanged: (text: CharSequence?, start: Int, before: Int, count: Int) -> Unit = { _, _, _, _ -> }
): TextWatcher {

    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(s, start, before, count)
        }
    }

    addTextChangedListener(textWatcher)
    return textWatcher
}

fun EditText.setMaxLength(length: Int) {
    filters = arrayOf(InputFilter.LengthFilter(length))
}

fun EditText.focus() {
    if (hasFocus()) {
        setSelection(text.length)
    }
}

fun EditText.afterTextChanged(afterTextChanged: (chars: Editable?) -> Unit = { _ -> }) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    })
}

fun EditText.beforeTextChanged(beforeTextChanged: (chars: CharSequence?, start: Int, count: Int, after: Int) -> Unit = { _, _, _, _ -> }) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    })
}

fun EditText.onTextChanged(onTextChanged: (chars: CharSequence?, start: Int, count: Int, after: Int) -> Unit = { _, _, _, _ -> }) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(s, start, before, count)
        }

    })
}


fun EditText.requestFocusAndKeyboard() {
    requestFocus()
    context.inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun EditText.clearFocusAndKeyboard() {
    clearFocus()
    context.inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Resets the cursor drawable to the default.
 */
fun EditText.resetCursorColor() {
    try {
        val f = TextView::class.java.getDeclaredField("mCursorDrawableRes")
        f.isAccessible = true
        f.set(this, 0)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

/**
 * Listens for either the enter key to be pressed or the soft keyboard's editor action to activate.
 */
inline fun EditText.onImeAction(crossinline action: (text: String) -> Unit) {
    setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
        if ((event?.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            action(text.toString())
            return@OnKeyListener true
        }
        false
    })
    setOnEditorActionListener { v, actionId, event ->
        action(text.toString())
        true
    }
}

/**
 * Both sets the [EditText.setImeOptions] to "Done" and listens for the IME action.
 */
inline fun EditText.onDone(crossinline action: (text: String) -> Unit) {
    imeOptions = EditorInfo.IME_ACTION_DONE
    onImeAction {
        hideSoftInput()
        action(text.toString())
    }
}

/**
 * Both sets the [EditText.setImeOptions] to "Send" and listens for the IME action.
 */
inline fun EditText.onSend(crossinline action: (text: String) -> Unit) {
    imeOptions = EditorInfo.IME_ACTION_SEND
    onImeAction {
        hideSoftInput()
        action(text.toString())
    }
}

/**
 * Sets the cursor color of the edit text.
 */
fun EditText.setCursorColor(color: Int) {
    try {
        val fCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
        fCursorDrawableRes.isAccessible = true
        val mCursorDrawableRes = fCursorDrawableRes.getInt(this)
        val fEditor = TextView::class.java.getDeclaredField("mEditor")
        fEditor.isAccessible = true
        val editor = fEditor.get(this)
        val clazz = editor.javaClass
        val fCursorDrawable = clazz.getDeclaredField("mCursorDrawable")
        fCursorDrawable.isAccessible = true
        val drawables = arrayOf(
            context.getCompatDrawable(mCursorDrawableRes)?.mutate().apply {
                this?.let { DrawableCompat.setTint(it, color) }

            },
            context.getCompatDrawable(mCursorDrawableRes)?.mutate().apply {
                this?.let { DrawableCompat.setTint(it, color) }
            }
        )
        fCursorDrawable.set(editor, drawables)
    } catch (ignored: Throwable) {
    }
}

/**
 * Extension method to replace all text inside an [Editable] with the specified [newValue].
 */
fun Editable.replaceAll(newValue: String) {
    replace(0, length, newValue)
}

/**
 * Extension method to replace all text inside an [Editable] with the specified [newValue] while
 * ignoring any [android.text.InputFilter] set on the [Editable].
 */
fun Editable.replaceAllIgnoreFilters(newValue: String) {
    val currentFilters = filters
    filters = emptyArray()
    replaceAll(newValue)
    filters = currentFilters
}

/**
 * returns EditText text as URL
 */
fun EditText.getUrl(): URL? {
    return try {
        URL(text.toString())
    } catch (e: MalformedURLException) {
        null
    }
}

/**
 * Sets EditText text from Clipboard
 */
fun EditText.pasteFromClipBoard() {
    var text = ""

    val manager = context.clipboardManager

    manager?.primaryClip?.let {
        val item = manager.primaryClip?.getItemAt(0)
        text = item?.text.toString()
    }

    if (!TextUtils.isEmpty(text)) setText(text)
}



/**
 * Sets OnFocusChangeListener and calls specified function [block]
 * if this view become focused
 *
 * @see View.OnFocusChangeListener
 * @see View.setOnFocusChangeListener
 */
inline fun EditText.onFocused(crossinline block: () -> Unit) {
    setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) block.invoke()
    }
}

/**
 * Sets OnFocusChangeListener and calls specified function [block]
 * if this view become unfocused
 *
 * @see View.OnFocusChangeListener
 * @see View.setOnFocusChangeListener
 */
inline fun EditText.onUnFocused(crossinline block: () -> Unit) {
    setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) block.invoke()
    }
}

/**
 * Shows keyboard for this edit text
 */
fun EditText.showKeyboard() {
    requestFocus()
    context.inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * Shows keyboard for this edit text with delay
 *
 * @param delayMillis - delay in milliseconds before keyboard will be shown
 */
fun EditText.showKeyboardDelayed(delayMillis: Long) {
    Handler().postDelayed({
        requestFocus()
        showKeyboard()
    }, delayMillis)
}

/**
 * Hides keyboard for this edit text
 */
fun EditText.hideKeyboard() =
    context.inputMethodManager.hideSoftInputFromWindow(
        windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )

/**
 * Hides keyboard for this edit text with delay
 *
 * @param delayMillis - delay in milliseconds before keyboard will be hided
 */
fun EditText.hideKeyboardDelayed(delayMillis: Long) =
    Handler().postDelayed({ hideKeyboard() }, delayMillis)


 @SuppressLint("ClickableViewAccessibility")
 fun EditText.disableCopyAndPaste() {
    try {

        this.setOnLongClickListener { _ -> true }
        this.isLongClickable = false
        this.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                this.setInsertionDisabled()
            }
            false
        }
        this.setTextIsSelectable(false)
        this.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return false
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode) {

            }
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }

}

 fun EditText.setInsertionDisabled() {
    try {
        val editorField = TextView::class.java.getDeclaredField("mEditor")
        editorField.isAccessible = true
        val editorObject = editorField.get(this)

        // if this view supports insertion handles
        @SuppressLint("PrivateApi") val editorClass = Class.forName("android.widget.Editor")
        val mInsertionControllerEnabledField = editorClass.getDeclaredField("mInsertionControllerEnabled")
        mInsertionControllerEnabledField.isAccessible = true
        mInsertionControllerEnabledField.set(editorObject, false)

        // if this view supports selection handles
        val mSelectionControllerEnabledField = editorClass.getDeclaredField("mSelectionControllerEnabled")
        mSelectionControllerEnabledField.isAccessible = true
        mSelectionControllerEnabledField.set(editorObject, false)
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

fun EditText.clearError() {
    error = null
}

fun deleteAllWhenContainsStar(vararg editTexts: EditText) {
    for (et in editTexts) {
        et.deleteAllWhenContainsStar()
    }
}

fun EditText.deleteAllWhenContainsStar() {
    this.setOnKeyListener { _, keyCode, _ ->
        if (keyCode == KeyEvent.KEYCODE_DEL && this.text.toString().contains("*")) {
            this.setText("")
        }
        false
    }
}

fun EditText.setReadOnly(readOnly: Boolean, inputType: Int = InputType.TYPE_NULL) {
    isFocusable = !readOnly
    isFocusableInTouchMode = !readOnly
    this.inputType = inputType
}