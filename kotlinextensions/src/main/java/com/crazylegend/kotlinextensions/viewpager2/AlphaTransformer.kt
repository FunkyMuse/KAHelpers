package com.crazylegend.kotlinextensions.viewpager2

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs


/**
 * Created by crazy on 2/22/20 to long live and prosper !
 */
class AlphaTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val absPos = abs(position)
        page.apply {
            translationY = absPos * 500f
            translationX = absPos * 500f
            scaleX = 1f
            scaleY = 1f
        }
        when {
            position < -1 ->
                page.alpha = 0.1f
            position <= 1 -> {
                page.alpha = 0.2f.coerceAtLeast(1 - abs(position))
            }
            else -> page.alpha = 0.1f
        }
    }

}