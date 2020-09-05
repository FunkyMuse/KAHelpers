package com.crazylegend.kotlinextensions.views

import android.app.WallpaperManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.util.Base64
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.widget.ImageViewCompat
import com.crazylegend.kotlinextensions.context.getColorCompat
import java.io.FileOutputStream


/**
 * Created by hristijan on 3/4/19 to long live and prosper !
 */

/**
 * Change Imageview tint
 */
fun ImageView.setTint(color: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))

}

fun ImageView.clear() {
    setImageDrawable(null)
}

fun ImageView.setTint(@ColorRes colorRes: Int, mode: PorterDuff.Mode = PorterDuff.Mode.SRC_OVER) {
    this.setColorFilter(context.getColorCompat(colorRes), mode)
}

fun ImageView.setBase64(base64: String, flag: Int) {
    if (base64.isNotEmpty()) {
        val thumb: Bitmap?
        val bytes = Base64.decode(base64, flag)
        val file = createTempFile(suffix = ".png")
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(file)
            out.write(bytes)
            out.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            out?.close()
        }
        thumb = BitmapFactory.decodeFile(file.absolutePath)
        file.delete()
        setImageBitmap(thumb)
    }
}


/**
 * Loads current device wallpaper to an Imageview
 */
fun ImageView.setWallpaper() {
    val wallpaperManager = WallpaperManager.getInstance(context)
    setImageDrawable(wallpaperManager.drawable)
}

