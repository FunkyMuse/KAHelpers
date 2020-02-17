package com.crazylegend.kotlinextensions.curvedview

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.view.ViewGroup
import com.crazylegend.kotlinextensions.context.getCompatColor


/**
 * Created by hristijan on 4/22/19 to long live and prosper !
 */

class CurvedView(
        val startColor: Int,
        val endColor: Int,
        val curvature: Curvature) {

    data class Builder(
            var startColor: Int = 0,
            var endColor: Int = 0,
            var curvature: Curvature = Curvature.MEDIUM) {

        fun startColor(startColor: Int) = apply { this.startColor = startColor }
        fun endColor(endColor: Int) = apply { this.endColor = endColor }
        fun curvature(curvature: Curvature) = apply { this.curvature = curvature }

        fun build() = CurvedView(startColor, endColor, curvature)
    }

    enum class Curvature {
        HIGH, MEDIUM, LOW
    }
}

fun ViewGroup.curveIt(curvedView: CurvedView) {

    this.post {
        val paint = Paint()
        val width = this.measuredWidth
        val height = this.measuredHeight

        layoutParams.height = height
        val pallet = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(pallet)
        val r = getRectCoordinates(width, height, curvedView.curvature)
        paint.style = Paint.Style.FILL

        paint.shader = LinearGradient(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                context.getCompatColor(curvedView.startColor),
                context.getCompatColor(curvedView.endColor),
                Shader.TileMode.CLAMP
        )

        val drawable = BitmapDrawable(resources, pallet)

        canvas.drawArc(r, 0f, 180f, true, paint)

        this.background = drawable
        requestLayout()
    }

}

fun getRectCoordinates(width: Int, height: Int, curvature: CurvedView.Curvature): RectF {


    val multiplier: Float = when (curvature) {
        CurvedView.Curvature.HIGH -> 1f
        CurvedView.Curvature.MEDIUM -> 2f
        CurvedView.Curvature.LOW -> 3f
    }

    return RectF((-multiplier * width), ((-height * 2).toFloat()), ((multiplier + 1) * width), height.toFloat())
}