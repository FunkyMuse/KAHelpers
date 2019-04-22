package com.crazylegend.kotlinextensions.canvas

import android.graphics.Canvas
import android.graphics.Paint


/**
 * Created by hristijan on 4/22/19 to long live and prosper !
 */

fun Canvas.drawCircle(cx: Int, cy: Int, radius: Int, paint: Paint) {
    drawCircle(cx.toFloat(), cy.toFloat(), radius.toFloat(), paint)
}

fun Canvas.drawCircle(cx: Int, cy: Int, radius: Float, paint: Paint) {
    drawCircle(cx.toFloat(), cy.toFloat(), radius, paint)
}