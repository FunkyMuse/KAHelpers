package com.crazylegend.kotlinextensions.pin

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.crazylegend.kotlinextensions.R


/**
 * Created by hristijan on 3/5/19 to long live and prosper !
 */

class PinEditText : AppCompatEditText {

    private lateinit var pinPainter: PinPainter

    private var onClickListener: View.OnClickListener? = null
    private var onEditorActionListener: TextView.OnEditorActionListener? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused) {
            text?.length?.let { setSelection(it) }
        }
    }

    override fun setOnClickListener(onClickListener: View.OnClickListener?) {
        this.onClickListener = onClickListener
    }

    override fun setOnEditorActionListener(onEditorActionListener: TextView.OnEditorActionListener) {
        this.onEditorActionListener = onEditorActionListener
    }

    /** This is a replacement method for the base TextView class' method of the same name. This
     * method is used in hidden class android.widget.Editor to determine whether the PASTE/REPLACE popup
     * appears when triggered from the text insertion handle. Returning false forces this window
     * to never appear.
     * This function is privately use by the EditText so we have to create a function with the same name
     * @return false
     */
    fun canPaste(): Boolean = false

    /** This is a replacement method for the base TextView class' method of the same name. This method
     * is used in hidden class android.widget.Editor to determine whether the PASTE/REPLACE popup
     * appears when triggered from the text insertion handle. Returning false forces this window
     * to never appear.
     * @return false
     */
    override fun isSuggestionsEnabled(): Boolean = false

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val (newWidthMeasureSpec, newHeightMeasureSpec) = pinPainter.getCalculatedMeasureSpecSize()
        setMeasuredDimension(newWidthMeasureSpec, newHeightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        pinPainter.draw(canvas)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        isCursorVisible = false
        isLongClickable = false
        customSelectionActionModeCallback = ActionModeCallbackInterceptor()
        maxLines = DEFAULT_PIN_MAX_LINES
        setBackgroundColor(Color.TRANSPARENT)

        initClickListener()
        initOnEditorActionListener()

        var normalStateDrawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.pin_default_normal_state)
        var highlightStateDrawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.pin_default_highlight_state)
        var pinWidth = context.dpToPx(DEFAULT_PIN_WIDTH)
        var pinHeight = context.dpToPx(DEFAULT_PIN_HEIGHT)
        var pinTotal = DEFAULT_PIN_TOTAL
        var pinSpace = context.dpToPx(DEFAULT_PIN_SPACE)

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PinEditText, defStyleAttr, 0)

            try {

                pinTotal = getTextViewMaxLength(attrs, pinTotal)
                pinWidth = typedArray.getDimension(R.styleable.PinEditText_pinWidth, pinWidth)
                pinHeight = typedArray.getDimension(R.styleable.PinEditText_pinHeight, pinHeight)
                pinSpace = typedArray.getDimension(R.styleable.PinEditText_pinSpace, pinSpace)

                typedArray.getDrawable(R.styleable.PinEditText_pinNormalStateDrawable)?.let {
                    normalStateDrawable = it
                }
                typedArray.getDrawable(R.styleable.PinEditText_pinHighlightStateDrawable)?.let {
                    highlightStateDrawable = it
                }

            } finally {
                typedArray.recycle()
            }

        }

        require(normalStateDrawable != null) {
            "normalStateDrawable must not be null"
        }

        require(highlightStateDrawable != null) {
            "highlightStateDrawable must not be null"
        }

        pinPainter = PinPainter(
            normalStateDrawable!!,
            highlightStateDrawable!!,
            this,
            pinWidth,
            pinHeight,
            pinTotal,
            pinSpace)
    }

    private fun initClickListener() {
        super.setOnClickListener { view ->
            // Force the cursor to the end every time we click at this EditText
            text?.length?.let { setSelection(it) }
            onClickListener?.onClick(view)
        }
    }

    private fun initOnEditorActionListener() {
        super.setOnEditorActionListener { view, actionId, event ->
            // For some reasons the soft keyboard does not response when tap :(
            // But after set OnEditorActionListener it works :)
            onEditorActionListener?.onEditorAction(view, actionId, event) ?: false
        }
    }

    private fun getTextViewMaxLength(attrs: AttributeSet, defaultMaxLength: Int): Int {
        return attrs.getAttributeIntValue(XML_NAMESPACE_ANDROID, "maxLength", defaultMaxLength)
    }

    private fun Context.dpToPx(dp: Int) = dp * resources.displayMetrics.density

    /**
     * Prevents the action bar (top horizontal bar with cut, copy, paste, etc.) from appearing
     * by intercepting the callback that would cause it to be created, and returning false.
     */
    private class ActionModeCallbackInterceptor : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean = false
        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean = false
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean = false
        override fun onDestroyActionMode(mode: ActionMode) {}
    }

    companion object {
        /**
         * android: namespace
         */
        private const val XML_NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android"

        private const val DEFAULT_PIN_WIDTH = 24
        private const val DEFAULT_PIN_HEIGHT = 24
        private const val DEFAULT_PIN_TOTAL = 4
        private const val DEFAULT_PIN_SPACE = 16
        private const val DEFAULT_PIN_MAX_LINES = 1

    }

}

/*

<com.crazylegend.kotlinextensions.pin.PinEditText
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:maxLength="4"
android:inputType="textNoSuggestions"
app:pinHighlightStateDrawable="@drawable/your_pin_highlight_state"
app:pinNormalStateDrawable="@drawable/your_pin_normal_state"
app:pinSpace="16dp"
app:pinHeight="24dp"
app:pinWidth="24dp"/>
*/

/*
android:maxLength: The total pin (Required to make PinEditText limit its length)
android:inputType: Preferred "textNoSuggestions" (If you want text) or "number" (If you want only number)
app:pinSpace: Space between each pin (Default: 16dp)
app:pinHeight: Height of each pin (Default: 24dp)
app:pinWidth: Width of each pin (Default: 24dp)
app:pinHighlightStateDrawable: Drawable of each pin that already has text
app:pinNormalStateDrawable: Drawable of each empty pin
*/
