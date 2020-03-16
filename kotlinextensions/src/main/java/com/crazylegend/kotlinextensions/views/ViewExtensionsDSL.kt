package com.crazylegend.kotlinextensions.views

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat


/**
 * Created by hristijan on 6/7/19 to long live and prosper !
 */

inline fun <T : View> T.style(block: T.() -> Unit): T {
    return this.apply(block)
}

enum class Anchor {
    LEFT,
    RIGHT,
    TOP,
    BOTTOM,
    CENTERX,
    CENTERY
//    BASELINE(ConstraintLayout.LayoutParams.BASELINE),
//    START(ConstraintLayout.LayoutParams.START),
//    END(ConstraintLayout.LayoutParams.END)
}

fun alignLefts(vararg views: View) = align(Anchor.LEFT, views)
fun alignRights(vararg views: View) = align(Anchor.RIGHT, views)
fun alignTops(vararg views: View) = align(Anchor.TOP, views)
fun alignBottoms(vararg views: View) = align(Anchor.BOTTOM, views)
fun alignHorizontally(vararg views: View) = align(Anchor.CENTERY, views)
fun alignVertically(vararg views: View) = align(Anchor.CENTERX, views)

private fun align(edge: Anchor, views: Array<out View>) {
    var firstView: View? = null
    views.forEachIndexed { index, view ->
        if (index == 0) {
            firstView = view
        } else {
            when (edge) {
                Anchor.LEFT -> view.constrainLeftToLeftOf(firstView!!)
                Anchor.RIGHT -> view.constrainRightToRightOf(firstView!!)
                Anchor.TOP -> view.constrainTopToTopOf(firstView!!)
                Anchor.BOTTOM -> view.constrainBottomToBottomOf(firstView!!)
                Anchor.CENTERX -> view.constrainCenterXToCenterXOf(firstView!!)
                Anchor.CENTERY -> view.constrainCenterYToCenterYOf(firstView!!)
            }
        }
    }
}

fun View.returnMeasurements(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int,
        measurements: (widthMode: Int, heightMode: Int, widthSize: Int, heightSize: Int, initialWidth: Int, initialHeight: Int) -> Unit = { _, _, _, _, _, _ -> }
) {
    val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
    val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
    val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
    val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
    val initialWidth = paddingLeft + paddingRight
    val initialHeight = paddingTop + paddingBottom
    measurements(widthMode, heightMode, widthSize, heightSize, initialWidth, initialHeight)
}


// Constants
const val matchConstraint = ConstraintSet.MATCH_CONSTRAINT
const val matchParent: Int = ViewGroup.LayoutParams.MATCH_PARENT
const val wrapContent: Int = ViewGroup.LayoutParams.WRAP_CONTENT

// Style - Colors

fun View.color(resourceId: Int): Int {
    return ContextCompat.getColor(context, resourceId)
}

var TextView.textColor: Int
    get() = 0
    set(v) = setTextColor(v)

var Button.textColor: Int
    get() = 0
    set(v) = setTextColor(v)

var View.padding: Int
    get() = 0
    inline set(value) = setPadding(value, value, value, value)

var View.backgroundColor: Int
    get() = 0
    set(v) = setBackgroundColor(v)

// Style - Pixel densities

var Int.dp: Int
    get() {
        val metrics = Resources.getSystem().displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).toInt()
    }
    set(_) {}


var Float.dp: Float
    get() {
        val metrics = Resources.getSystem().displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, metrics)
    }
    set(_) {}


fun Number.dpToPx(context: Context? = null): Float {
    val res = context?.resources ?: Resources.getSystem()
    return this.toFloat() * res.displayMetrics.density
}

fun Number.pxToDp(context: Context? = null): Float {
    val res = context?.resources ?: Resources.getSystem()
    return this.toFloat() / res.displayMetrics.density
}

fun Number.spToPx(context: Context? = null): Float {
    val res = context?.resources ?: Resources.getSystem()
    return this.toFloat() * res.displayMetrics.scaledDensity
}

fun Number.pxToSp(context: Context? = null): Float {
    val res = context?.resources ?: Resources.getSystem()
    return this.toFloat() / res.displayMetrics.scaledDensity
}

// Layout - Helpers

fun ConstraintLayout.addConstraints(block: ConstraintSet.() -> Unit) {
    val cs = ConstraintSet()
    cs.clone(this)
    block(cs)
    cs.applyTo(this)
}


/// Represents a side of the parent view.
object I

class SinglePartialConstraint(val view: View, val margin: Int)

class MultiplePartialConstraint(val views: Array<View>, val margin: Int)

class SideConstraint(val constant: Int)


// I-view
operator fun I.minus(view: View): View {
    view.left(0)
    return view
}

// I-42
operator fun I.minus(margin: Int): SideConstraint {
    return SideConstraint(margin)
}

// (I-42)-view
operator fun SideConstraint.minus(view: View): View {
    view.left(constant)
    return view
}

// view-I
operator fun View.minus(side: I): View {
    this.right(0)
    return this
}

// (view-42)-I
operator fun SinglePartialConstraint.minus(side: I): View {
    view.right(margin)
    return view
}

// viewA-viewB
operator fun View.minus(view: View): Array<View> {
    // Somehow these don't have the same effect.
    // (not reflexive)
//    view.constrainLeftToRightOf(this)
    this.constrainRightToLeftOf(view)
    return arrayOf(this, view)
}

// view-42
operator fun View.minus(margin: Int): SinglePartialConstraint {
    return SinglePartialConstraint(this, margin)
}

// (previousView-42)-view
operator fun SinglePartialConstraint.minus(right: View): Array<View> {
    right.constrainLeftToRightOf(view, margin)
    return arrayOf(view, right)
}

// (viewA-viewB)-I
operator fun Array<View>.minus(side: I): Array<View> {
    lastOrNull()?.right(0)
    return this
}

// (viewA-viewB)-42
operator fun Array<View>.minus(margin: Int): MultiplePartialConstraint {
    return MultiplePartialConstraint(this, margin)
}

// (viewA-viewB)-(42-I)
operator fun MultiplePartialConstraint.minus(side: I): Array<View> {
    views.last().right(margin)
    return views
}

// (viewA-viewB-42)-view
operator fun MultiplePartialConstraint.minus(view: View): Array<View> {
    view.constrainLeftToRightOf(views.last(), margin)
    return views + view
}

fun horizontalLayout(vararg items: Any): Array<out Any> {
    var previousMargin: Int? = null
    var previousView: View? = null
    for ((viewCount, item) in items.withIndex()) {

        when (item) {
            is Int -> {
                previousMargin = item
                if (viewCount == items.count() - 1) { // Last Margin == Bottom
                    previousView?.let { previousView ->
                        previousView.right(item)
                    }
                }
            }
            is View -> {
                previousMargin?.let { previousMargin ->
                    if (viewCount == 1) {
                        item.left(previousMargin)
                    } else {
                        previousView?.let { previousView ->

                            item.constrainLeftToRightOf(previousView, previousMargin)
//                            item.constrainRightToLeftOf(previousView, previousMargin)
                        }
                    }
                }
                previousView = item
            }
            is String -> {
                previousMargin = null
                previousView = null
            }
        }
    }
    return items
}


fun <T : View> T.centerInParent(): T {
    return centerHorizontally().centerVertically()
}

fun <T : View> T.centerHorizontally(): T {
    (parent as? ConstraintLayout)?.let { constraintLayout ->
        constraintLayout.addConstraints {
            centerHorizontally(id, constraintLayout.id)
        }
    }
    return this
}

fun <T : View> T.centerVertically(): T {
    (parent as? ConstraintLayout)?.let { constraintLayout ->
        constraintLayout.addConstraints {
            centerVertically(id, constraintLayout.id)
        }
    }
    return this
}


fun <T : View> T.fillParent(padding: Int = 0): T {
    return fillVertically(padding).fillHorizontally(padding)
}

fun <T : View> T.fillVertically(padding: Int = 0): T {
    layoutParams.height = ConstraintSet.MATCH_CONSTRAINT // Needed to "match constraints"
    return top(padding).bottom(padding)
}

fun <T : View> T.fillHorizontally(padding: Int = 0): T {
    layoutParams.width = ConstraintSet.MATCH_CONSTRAINT // Needed to "match constraints"
    return left(padding).right(padding)
}


fun <T : View> T.top(margin: Int): T {
    return position(ConstraintLayout.LayoutParams.TOP, margin)
}

fun <T : View> T.left(margin: Int): T {
    return position(ConstraintLayout.LayoutParams.LEFT, margin)
}

fun <T : View> T.right(margin: Int): T {
    return position(ConstraintLayout.LayoutParams.RIGHT, margin)
}

fun <T : View> T.bottom(margin: Int): T {
    return position(ConstraintLayout.LayoutParams.BOTTOM, margin)
}

fun <T : View> T.position(position: Int, margin: Int): T {
    (parent as? ConstraintLayout)?.let { constraintLayout ->
        constraintLayout.addConstraints {
            connect(id, position, ConstraintLayout.LayoutParams.PARENT_ID, position, margin.dp)
        }
    }
    return this
}


// Top

fun <T : View> T.constrainTopToBottomOf(view: View, margin: Int = 0): T {
    return constrainTopToBottomOf(view.id, margin)
}

fun <T : View> T.constrainTopToBottomOf(viewId: Int, margin: Int = 0): T {
    (parent as? ConstraintLayout)?.addConstraints {
        connect(id, ConstraintLayout.LayoutParams.TOP, viewId, ConstraintLayout.LayoutParams.BOTTOM, margin.dp)
    }
    return this
}

fun <T : View> T.constrainTopToTopOf(view: View, margin: Int = 0): T {
    return constrainTopToTopOf(view.id, margin)
}

fun <T : View> T.constrainTopToTopOf(viewId: Int, margin: Int = 0): T {
    (parent as? ConstraintLayout)?.addConstraints {
        connect(id, ConstraintLayout.LayoutParams.TOP, viewId, ConstraintLayout.LayoutParams.TOP, margin.dp)
    }
    return this
}


// Left

fun <T : View> T.constrainLeftToRightOf(view: View, margin: Int = 0): T {
    return constrainLeftToRightOf(view.id, margin)
}

fun <T : View> T.constrainLeftToRightOf(viewId: Int, margin: Int = 0): T {
    (parent as? ConstraintLayout)?.addConstraints {
        connect(id, ConstraintLayout.LayoutParams.LEFT, viewId, ConstraintLayout.LayoutParams.RIGHT, margin.dp)
    }

    return this
}

fun <T : View> T.constrainLeftToLeftOf(view: View, margin: Int = 0): T {
    return constrainLeftToLeftOf(view.id, margin)
}

fun <T : View> T.constrainLeftToLeftOf(viewId: Int, margin: Int = 0): T {
    (parent as? ConstraintLayout)?.addConstraints {
        connect(id, ConstraintLayout.LayoutParams.LEFT, viewId, ConstraintLayout.LayoutParams.LEFT, margin.dp)
    }

    return this
}

// Right

fun <T : View> T.constrainRightToLeftOf(view: View, margin: Int = 0): T {
    return constrainRightToLeftOf(view.id, margin)
}

fun <T : View> T.constrainRightToLeftOf(viewId: Int, margin: Int = 0): T {
    (parent as? ConstraintLayout)?.addConstraints {
        connect(id, ConstraintLayout.LayoutParams.RIGHT, viewId, ConstraintLayout.LayoutParams.LEFT, margin.dp)
    }

    return this
}

fun <T : View> T.constrainRightToRightOf(view: View, margin: Int = 0): T {
    return constrainRightToRightOf(view.id, margin)
}

fun <T : View> T.constrainRightToRightOf(viewId: Int, margin: Int = 0): T {
    (parent as? ConstraintLayout)?.addConstraints {
        connect(id, ConstraintLayout.LayoutParams.RIGHT, viewId, ConstraintLayout.LayoutParams.RIGHT, margin.dp)
    }

    return this
}

// Bottom

fun <T : View> T.constrainBottomToTopOf(view: View, margin: Int = 0): T {
    return constrainBottomToTopOf(view.id, margin)
}

fun <T : View> T.constrainBottomToTopOf(viewId: Int, margin: Int = 0): T {
    (parent as? ConstraintLayout)?.addConstraints {
        connect(id, ConstraintLayout.LayoutParams.BOTTOM, viewId, ConstraintLayout.LayoutParams.TOP, margin.dp)
    }
    return this
}

fun <T : View> T.constrainBottomToBottomOf(view: View, margin: Int = 0): T {
    return constrainBottomToBottomOf(view.id, margin)
}

fun <T : View> T.constrainBottomToBottomOf(viewId: Int, margin: Int = 0): T {
    (parent as? ConstraintLayout)?.addConstraints {
        connect(id, ConstraintLayout.LayoutParams.BOTTOM, viewId, ConstraintLayout.LayoutParams.BOTTOM, margin.dp)
    }
    return this
}


// Center Y

// This is made possible by creating a "GONE" guideline and center on the guideline instead :)
fun <T : View> T.constrainCenterYToBottomOf(view: View): T {
    (parent as? ConstraintLayout)?.let { constraintLayout ->
        constraintLayout.addConstraints {
            val guideline = View(context)
            guideline.id = View.generateViewId()
            constraintLayout.addView(guideline)
            guideline.visibility = View.GONE
            guideline.constrainBottomToBottomOf(view)
            centerVertically(id, guideline.id)
        }
    }
    return this
}

fun <T : View> T.constrainCenterYToTopOf(view: View): T {
    (parent as? ConstraintLayout)?.let { constraintLayout ->
        constraintLayout.addConstraints {
            val guideline = View(context)
            guideline.id = View.generateViewId()
            constraintLayout.addView(guideline)
            guideline.visibility = View.GONE
            guideline.constrainTopToTopOf(view)
            centerVertically(id, guideline.id)
        }
    }
    return this
}

fun <T : View> T.constrainCenterYToCenterYOf(view: View): T {
    (parent as? ConstraintLayout)?.addConstraints {
        centerVertically(id, view.id)
    }
    return this
}


// Center X

fun <T : View> T.constrainCenterXToLeftOf(view: View): T {
    (parent as? ConstraintLayout)?.let { constraintLayout ->
        constraintLayout.addConstraints {
            val guideline = View(context)
            guideline.id = View.generateViewId()
            constraintLayout.addView(guideline)
            guideline.visibility = View.GONE
            guideline.constrainLeftToLeftOf(view)
            centerHorizontally(id, guideline.id)
        }
    }
    return this
}

fun <T : View> T.constrainCenterXToRightOf(view: View): T {
    (parent as? ConstraintLayout)?.let { constraintLayout ->
        constraintLayout.addConstraints {
            val guideline = View(context)
            guideline.id = View.generateViewId()
            constraintLayout.addView(guideline)
            guideline.visibility = View.GONE
            guideline.constrainRightToRightOf(view)
            centerHorizontally(id, guideline.id)
        }
    }
    return this
}


fun <T : View> T.constrainCenterXToCenterXOf(view: View): T {
    (parent as? ConstraintLayout)?.addConstraints {
        centerHorizontally(id, view.id)
    }
    return this
}


// Follow Edges

fun <T : View> T.followEdgesOf(view: View, margin: Int = 0): T {
    constrainTopToTopOf(view)
    constrainBottomToBottomOf(view)
    constrainLeftToLeftOf(view)
    constrainRightToRightOf(view)
    return this
}


// Layout - Size

fun <T : View> T.size(value: Int): T {
    return width(value).height(value)
}

fun <T : View> T.width(value: Int): T {
    return width(value.toFloat())
}

fun <T : View> T.height(value: Int): T {
    return height(value.toFloat())
}

fun <T : View> T.width(value: Float): T {

    if (value.toInt() == ConstraintSet.MATCH_CONSTRAINT) {
        layoutParams.width = value.toInt()
        return this
    }

    if (parent is ConstraintLayout) {
        (parent as? ConstraintLayout)?.let { constraintLayout ->
            constraintLayout.addConstraints {
                constrainWidth(id, value.dp.toInt())
            }
        }
    } else {
        if (layoutParams == null) {
            layoutParams = ViewGroup.LayoutParams(ConstraintSet.WRAP_CONTENT, ConstraintSet.WRAP_CONTENT)
        }

        if (layoutParams != null) {
            layoutParams.width = if (value > 0) value.dp.toInt() else value.toInt()
        } else {

            print("NULL")
        }
    }
    return this
}

fun <T : View> T.height(value: Float): T {

    if (value.toInt() == ConstraintSet.MATCH_CONSTRAINT) {
        layoutParams.height = value.toInt()
        return this
    }

    if (parent is ConstraintLayout) {
        (parent as? ConstraintLayout)?.let { constraintLayout ->
            constraintLayout.addConstraints {
                constrainHeight(id, value.dp.toInt())
            }
        }
    } else {

        if (layoutParams == null) {
            layoutParams = ViewGroup.LayoutParams(ConstraintSet.WRAP_CONTENT, ConstraintSet.WRAP_CONTENT)
        }

        if (layoutParams != null) {
            layoutParams.height = if (value > 0) value.dp.toInt() else value.toInt()
        }
    }
    return this
}


// Percent Size

fun <T : View> T.percentWidth(value: Float): T {
    layoutParams.width = ConstraintSet.MATCH_CONSTRAINT
    (parent as? ConstraintLayout)?.let { constraintLayout ->
        constraintLayout.addConstraints {
            constrainPercentWidth(id, value)
        }
    }
    return this
}

fun <T : View> T.percentHeight(value: Float): T {
    layoutParams.height = ConstraintSet.MATCH_CONSTRAINT
    (parent as? ConstraintLayout)?.let { constraintLayout ->
        constraintLayout.addConstraints {
            constrainPercentHeight(id, value)
        }
    }
    return this
}

