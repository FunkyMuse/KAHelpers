package com.crazylegend.kotlinextensions.displayMetrics

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */

fun Context.dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

fun Context.sp(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()

fun Fragment.dp(value: Int): Int? = activity?.dp(value)

fun Fragment.sp(value: Int): Int? = activity?.sp(value)

fun View.dp(value: Int): Int = context.dp(value)

fun View.sp(value: Int): Int = context.sp(value)