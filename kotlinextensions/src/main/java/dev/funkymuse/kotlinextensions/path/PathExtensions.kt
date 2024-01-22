package dev.funkymuse.kotlinextensions.path

import android.graphics.Path





fun Path.moveTo(x: Int, y: Int) {
    moveTo(x.toFloat(), y.toFloat())
}

fun Path.lineTo(x: Int, y: Int) {
    this.lineTo(x.toFloat(), y.toFloat())
}

fun Path.cubicTo(startX: Int, startY: Int, endX: Int, endY: Int, startXBias: Float = 0.5F, endXBias: Float = 0.5F) {
    cubicTo(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat(), startXBias, endXBias)
}

fun Path.cubicToFloat(startX: Float, startY: Float, endX: Float, endY: Float, startXBias: Float = 0.5F, endXBias: Float = 0.5F) {
    val reversed = endX < startX && endY < startY
    if (reversed) {
        val difX = startX - endX
        cubicTo(endX + (difX * (1 - startXBias)), startY, endX + difX * endXBias, endY, endX, endY)
    } else {
        val difX = endX - startX
        cubicTo(startX + (difX * (1 - startXBias)), startY, startX + difX * endXBias, endY, endX, endY)
    }
}