package com.crazylegend.kotlinextensions.views

import android.app.WallpaperManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.Base64
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.widget.ImageViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.crazylegend.kotlinextensions.context.getColorCompat


/**
 * Created by hristijan on 3/4/19 to long live and prosper !
 */

/**
 * Change Imageview tint
 */
fun ImageView.setTint(color: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))

}
fun ImageView.setTint(@ColorRes colorRes: Int, mode: PorterDuff.Mode = PorterDuff.Mode.SRC_OVER) {
    this.setColorFilter(context.getColorCompat(colorRes), mode)
}

fun ImageView.loadBase64Image(base64Image: String?) {
    base64Image?.let {
        //        val data = base64Image.split(',')[1] //Get the second part
        Glide.with(context)
            .asBitmap()
            .load(Base64.decode(base64Image, Base64.DEFAULT))
            .into(this)
    }
}

sealed class Transformation {
    object CenterCrop : Transformation()
    object Circle : Transformation()
}

fun ImageView.loadImageResource(imageResource: String?, skipMemoryCache: Boolean = false, transformation: Transformation? = null) {
    imageResource?.let {
        loadImage(it, skipMemoryCache, transformation)
    }
}

fun ImageView.loadImageResource(imageResource: Int?, skipMemoryCache: Boolean = false, transformation: Transformation? = null) {
    imageResource?.let {
        loadImage(it, skipMemoryCache, transformation)
    }
}

fun ImageView.loadImageResource(imageResource: Bitmap?, skipMemoryCache: Boolean = false, transformation: Transformation? = null) {
    imageResource?.let {
        loadImage(it, skipMemoryCache, transformation)
    }
}

fun ImageView.loadImageResource(imageResource: Drawable?, skipMemoryCache: Boolean = false, transformation: Transformation? = null) {
    imageResource?.let {
        loadImage(it, skipMemoryCache, transformation)
    }
}

private fun ImageView.loadImage(imageResource: Any, skipMemoryCache: Boolean, transformation: Transformation?) {
    var requestOptions = RequestOptions()
        .skipMemoryCache(skipMemoryCache)

    requestOptions = when (transformation) {
        is Transformation.CenterCrop -> requestOptions.centerCrop()
        is Transformation.Circle -> requestOptions.circleCrop()
        else -> requestOptions // Do nothing
    }

    Glide.with(context)
        .load(imageResource)
        .apply(requestOptions)
        .into(this)
}



/**
 * Loads current device wallpaper to an Imageview
 */
fun ImageView.setWallpaper() {
    val wallpaperManager = WallpaperManager.getInstance(context)
    setImageDrawable(wallpaperManager.drawable)
}