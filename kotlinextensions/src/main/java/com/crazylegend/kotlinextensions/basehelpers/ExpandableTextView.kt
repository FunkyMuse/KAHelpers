package com.crazylegend.kotlinextensions.basehelpers

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.crazylegend.kotlinextensions.R




/**
 * Created by hristijan on 3/15/19 to long live and prosper !
 */
class ExpandableTextView : TextView, View.OnClickListener {
    /* Custom method because standard getMaxLines() requires API > 16 */
    var myMaxLines = Integer.MAX_VALUE
        private set

    var customDrawable = R.drawable.icon_more_text
        private set

    fun setMaxLinesToText(maxLines: Int) {
        myMaxLines = maxLines
    }

    fun setTheCustomDrawableOnExpand(drawable:Int){
        customDrawable = drawable
    }

    constructor(context: Context) : super(context) {
        setOnClickListener(this)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setOnClickListener(this)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setOnClickListener(this)
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        /* If text longer than MAX_LINES set DrawableBottom - I'm using '...' icon */
        post {
            if (lineCount > MAX_LINES)
                setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, customDrawable)
            else
                setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

            maxLines = MAX_LINES
        }
    }


    override fun setMaxLines(maxLines: Int) {
        myMaxLines = maxLines
        super.setMaxLines(maxLines)
    }

    override fun onClick(v: View) {
        /* Toggle between expanded collapsed states */
        maxLines = if (myMaxLines == Integer.MAX_VALUE)
            MAX_LINES
        else
            Integer.MAX_VALUE
    }

    companion object {
        private const val MAX_LINES = 5
    }
}