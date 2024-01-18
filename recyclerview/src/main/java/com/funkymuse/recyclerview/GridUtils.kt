package com.funkymuse.recyclerview

import android.content.Context


fun Context.calculateNoOfColumns(columnWidthDp: Float): Int { // For example columnWidthdp=300 or 190 etc...
    val displayMetrics = this.resources.displayMetrics
    val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
    return (screenWidthDp / columnWidthDp + 0.5).toInt()
}