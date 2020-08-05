package com.crazylegend.kotlinextensions.customviews

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Keep
import com.crazylegend.kotlinextensions.R
import com.crazylegend.kotlinextensions.databinding.CustomizableCardViewBinding
import com.google.android.material.card.MaterialCardView


/**
 * Created by crazy on 11/29/19 to long live and prosper !
 */
@Keep
class CustomizableCardView : MaterialCardView {

    private val view = inflate(context, R.layout.customizable_card_view, this)
    val binding get() = CustomizableCardViewBinding.bind(view)

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
                binding.image.setImageDrawable(getDrawable(R.styleable.CustomizableCardView_ic_icon))
                binding.title.text = getString(R.styleable.CustomizableCardView_ic_title)
                binding.content.text = getString(R.styleable.CustomizableCardView_ic_content)
            } finally {
                recycle()
            }
        }
    }

}