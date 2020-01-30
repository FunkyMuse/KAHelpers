package com.crazylegend.kotlinextensions.glide

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.RequestOptions


/**
 * Created by Crazy on 2/5/19 to long live and prosper !
 */

@Throws(GlideException::class)
fun Context.loadImg(imgUrl: Any, view: AppCompatImageView) {

    Glide.with(this)
            .load(imgUrl)
            .into(view)
}


@Throws(GlideException::class)
fun Context.loadImg(imgUrl: Any, view: AppCompatImageView, error: Drawable) {
    Glide.with(this)
            .load(imgUrl)
            .error(error)
            .into(view)
}

@Throws(GlideException::class)
fun Context.loadImg(imgUrl: Any, view: AppCompatImageView, error: Drawable, placeHolder: Drawable) {
    Glide.with(this)
            .load(imgUrl)
            .placeholder(placeHolder)
            .error(error)
            .into(view)
}


@Throws(GlideException::class)
fun Context.loadImgNoCache(imgUrl: Any, view: AppCompatImageView) {
    Glide.with(this)
            .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .load(imgUrl)
            .into(view)
}

@Throws(GlideException::class)
fun Context.loadImgNoCache(imgUrl: Any, view: AppCompatImageView, error: Drawable) {
    Glide.with(this)
            .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .load(imgUrl)
            .error(error)
            .into(view)
}

@Throws(GlideException::class)
fun Context.loadImgNoCache(imgUrl: Any, view: AppCompatImageView, error: Drawable, placeHolder: Drawable) {
    Glide.with(this)
            .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .load(imgUrl)
            .placeholder(placeHolder)
            .error(error)
            .into(view)
}

@Throws(GlideException::class)
fun AppCompatImageView.loadImgNoCache(imgUrl: Any, error: Drawable, placeHolder: Drawable) {
    Glide.with(this)
            .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .load(imgUrl)
            .placeholder(placeHolder)
            .error(error)
            .into(this)
}

@Throws(GlideException::class)
fun Context.loadImgWithTransformation(imgUrl: Any, view: AppCompatImageView, transformation: RequestOptions) {
    Glide.with(this)
            .applyDefaultRequestOptions(transformation)
            .load(imgUrl)
            .into(view)
}


@Throws(GlideException::class)
fun View.loadImg(imgUrl: Any, view: AppCompatImageView) {
    Glide.with(this)
            .load(imgUrl)
            .into(view)
}


@Throws(GlideException::class)
fun View.loadImg(imgUrl: Any, view: AppCompatImageView, error: Drawable) {
    Glide.with(this)
            .load(imgUrl)
            .error(error)
            .into(view)
}

@Throws(GlideException::class)
fun View.loadImg(imgUrl: Any, view: AppCompatImageView, error: Drawable, placeHolder: Drawable) {
    Glide.with(this)
            .load(imgUrl)
            .placeholder(placeHolder)
            .error(error)
            .into(view)
}


@Throws(GlideException::class)
fun View.loadImgNoCache(imgUrl: Any, view: AppCompatImageView) {
    Glide.with(this)
            .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .load(imgUrl)
            .into(view)
}

@Throws(GlideException::class)
fun View.loadImgNoCache(imgUrl: Any, view: AppCompatImageView, error: Drawable) {
    Glide.with(this)
            .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .load(imgUrl)
            .error(error)
            .into(view)
}

@Throws(GlideException::class)
fun AppCompatImageView.loadImgNoCache(image: Any?) {
    Glide.with(this)
            .load(image)
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .into(this)
}


@Throws(GlideException::class)
fun View.loadImgWithTransformation(imgUrl: Any, view: AppCompatImageView, transformation: RequestOptions) {
    Glide.with(this)
            .applyDefaultRequestOptions(transformation)
            .load(imgUrl)
            .into(view)
}

@Throws(GlideException::class)
fun AppCompatImageView.loadImgNoCache(image: Any?, options: () -> BaseRequestOptions<*>) {
    Glide.with(this)
            .load(image)
            .apply(options())
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .into(this)
}

