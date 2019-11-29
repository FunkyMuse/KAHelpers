package com.crazylegend.kotlinextensions.customviews

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import com.crazylegend.kotlinextensions.R
import com.crazylegend.kotlinextensions.delegates.view.ImageViewDrawableDelegate
import com.crazylegend.kotlinextensions.delegates.view.TextViewStringDelegate
import kotlinx.android.synthetic.main.customizable_card_view.view.*


/**
 * Created by crazy on 11/29/19 to long live and prosper !
 */
class CustomizableCardView : FrameLayout {

    init {
        inflate(context, R.layout.customizable_card_view, this)
    }

    var title: String? by TextViewStringDelegate(cc_title)
    var content: String? by TextViewStringDelegate(cc_content)
    var icon: Drawable? by ImageViewDrawableDelegate(cc_image)

    val cardView = cc_card
    val imageView = cc_image
    val titleText = cc_title
    val contentText = cc_content

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