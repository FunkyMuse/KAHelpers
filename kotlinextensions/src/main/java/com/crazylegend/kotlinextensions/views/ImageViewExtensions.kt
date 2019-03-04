package com.crazylegend.kotlinextensions.views

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target


/**
 * Created by hristijan on 3/4/19 to long live and prosper !
 */

/**
 * Change Imageview tint
 */
fun ImageView.setTint(color: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))

}


/**
 * Glide加载图片
 * @param url
 * @param placeholder
 * @param error
 * @param isCircle
 * @param isCenterCrop
 * @param roundRadius
 * @param isCrossFade
 * @param isForceOriginalSize 是
 */
fun ImageView.load(url: Any, placeholder: Int = 0, error: Int = 0,
                   isCircle: Boolean = false,
                   isCenterCrop: Boolean = false,
                   roundRadius: Int = 0,
                   isCrossFade: Boolean = false,
                   isForceOriginalSize: Boolean = false) {
    val options = RequestOptions().placeholder(placeholder).error(error).apply {
        if (isCenterCrop && scaleType != ImageView.ScaleType.CENTER_CROP)
            scaleType = ImageView.ScaleType.CENTER_CROP
        if (isCircle) {
            circleCrop()
        } else if (roundRadius != 0) {
            if (scaleType == ImageView.ScaleType.CENTER_CROP) {
                transforms(CenterCrop(), RoundedCorners(roundRadius))
            } else {
                transform(RoundedCorners(roundRadius))
            }
        }
        if(isForceOriginalSize){
            override(Target.SIZE_ORIGINAL)
        }
    }
    Glide.with(context).load(url)
        .apply(options)
        .apply {
            if (isCrossFade) transition(DrawableTransitionOptions.withCrossFade())
        }
        .into(this)
}