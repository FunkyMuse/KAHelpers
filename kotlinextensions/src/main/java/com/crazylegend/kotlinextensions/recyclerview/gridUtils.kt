package com.crazylegend.kotlinextensions.recyclerview

import android.content.Context


/**
 * Created by hristijan on 2/20/19 to long live and prosper !
 */

fun Context.calculateNoOfColumns(columnWidthDp: Float): Int { // For example columnWidthdp=300 or 190 etc...
    val displayMetrics = this.resources.displayMetrics
    val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
    return (screenWidthDp / columnWidthDp + 0.5).toInt()
}