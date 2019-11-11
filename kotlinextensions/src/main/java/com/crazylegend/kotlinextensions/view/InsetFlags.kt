package com.crazylegend.kotlinextensions.view


/**
 * Created by crazy on 11/11/19 to long live and prosper !
 */


data class InsetFlags(
        val hasLeftInset: Boolean,
        val hasTopInset: Boolean,
        val hasRightInset: Boolean,
        val hasBottomInset: Boolean
) {
    companion object {

        val ALL = InsetFlags(hasLeftInset = true, hasTopInset = true, hasRightInset = true, hasBottomInset = true)
        val NO_TOP = InsetFlags(hasLeftInset = true, hasTopInset = false, hasRightInset = true, hasBottomInset = true)
        val NONE = InsetFlags(hasLeftInset = false, hasTopInset = false, hasRightInset = false, hasBottomInset = false)
        val BOTTOM = InsetFlags(hasLeftInset = false, hasTopInset = false, hasRightInset = false, hasBottomInset = true)
        val VERTICAL = InsetFlags(hasLeftInset = false, hasTopInset = true, hasRightInset = false, hasBottomInset = true)
        val HORIZONTAL = InsetFlags(hasLeftInset = true, hasTopInset = false, hasRightInset = true, hasBottomInset = true)
        val NO_BOTTOM: InsetFlags = InsetFlags(hasLeftInset = true, hasTopInset = true, hasRightInset = true, hasBottomInset = false)
    }
}