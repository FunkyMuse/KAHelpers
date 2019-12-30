package com.crazylegend.kotlinextensions.customviews

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.Keep
import com.crazylegend.kotlinextensions.R
import com.crazylegend.kotlinextensions.delegates.view.ImageViewDrawableDelegate
import com.crazylegend.kotlinextensions.delegates.view.TextViewStringDelegate
import kotlinx.android.synthetic.main.customizable_card_view.view.*


/**
 * Created by crazy on 11/29/19 to long live and prosper !
 */
@Keep
class CustomizableCardView : FrameLayout {

    private val view = inflate(context, R.layout.customizable_card_view, this)

    val cardView get() = view.cc_card
    val imageView get() = view.cc_image
    val titleText get() = view.cc_title
    val contentText get() = view.cc_content
    val container get() = view.cc_layout

    var title: String? by TextViewStringDelegate(titleText)
    var content: String? by TextViewStringDelegate(contentText)
    var icon: Drawable? by ImageViewDrawableDelegate(imageView)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        attrs ?: return
        val attributeValues = context.obtainStyledAttributes(attrs, R.styleable.CustomizableCardView)
        with(attributeValues) {
            try {
                icon = getDrawable(R.styleable.CustomizableCardView_ic_icon)
                title = getString(R.styleable.CustomizableCardView_ic_title)
                content = getString(R.styleable.CustomizableCardView_ic_content)
            } finally {
                recycle()
            }
        }
    }

}