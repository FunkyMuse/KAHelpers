package com.crazylegend.kotlinextensions.generators


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.crazylegend.kotlinextensions.generators.MathExtensions.Companion.random
import com.crazylegend.kotlinextensions.generators.MathExtensions.Companion.randomBoolean
import java.text.MessageFormat.format
import kotlin.math.roundToInt

/**
 * Created by hristijan on 4/15/19 to long live and prosper !
 */

fun createRandomImageUrl(): String {

    val landscape = randomBoolean()
    val endpoint = randomBoolean()

    val width = random(300, 400)
    val height = random(200, 300)

    return format(
        if (endpoint)
            "https://lorempixel.com/{0}/{1}/"
        else
            "https://picsum.photos/{0}/{1}/",
        if (landscape) width else height, if (landscape) height else width
    )
}

@JvmOverloads
fun generateRandomColorDrawable(alpha: Int = 255, red: Int = 255, green: Int = 255, blue: Int = 255): ColorDrawable = ColorDrawable(generateRandomColor(alpha, red, green, blue))

@JvmOverloads
fun generateRandomColor(alpha: Int = 255, red: Int = 255, green: Int = 255, blue: Int = 255): Int {

    var r = random(256)
    var g = random(256)
    var b = random(256)

    // mix the color
    r = ((r + red) / 2f).roundToInt()
    g = ((g + green) / 2f).roundToInt()
    b = ((b + blue) / 2f).roundToInt()

    return Color.argb(alpha, r, g, b)
}