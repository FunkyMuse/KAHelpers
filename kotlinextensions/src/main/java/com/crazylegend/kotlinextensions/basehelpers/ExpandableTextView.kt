package com.crazylegend.kotlinextensions.basehelpers

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.crazylegend.kotlinextensions.R


/**
 * Created by hristijan on 3/15/19 to long live and prosper !
 */
open class ExpandableTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : TextView(context, attrs), View.OnClickListener {
    /* Custom method because standard getMaxLines() requires API > 16 */
    var myMaxLines = Integer.MAX_VALUE
        private set

    var customDrawable: Int = R.drawable.icon_more_text
        private set

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


    fun setCustomDrawable(drawable:Int){
        customDrawable = drawable
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