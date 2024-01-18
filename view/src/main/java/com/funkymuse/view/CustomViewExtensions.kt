package com.funkymuse.view

import android.view.View




/**
 * Use in [View.onMeasure] to simplify measurements
 * @receiver View
 * @param widthMeasureSpec Int
 * @param heightMeasureSpec Int
 * @param measurements Function6<[@kotlin.ParameterName] Int, [@kotlin.ParameterName] Int, [@kotlin.ParameterName] Int, [@kotlin.ParameterName] Int, [@kotlin.ParameterName] Int, [@kotlin.ParameterName] Int, Unit>
 */
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


