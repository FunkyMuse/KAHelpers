package com.crazylegend.kotlinextensions.glide

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.RequestOptions


/**
 * Created by Crazy on 2/5/19 to long live and prosper !
 */

fun Context.loadImg(imgUrl: Any?, view: AppCompatImageView) {
    Glide.with(this)
            .load(imgUrl)
            .into(view)
}


fun Context.loadImg(imgUrl: Any?, view: AppCompatImageView, error: Drawable) {
    Glide.with(this)
            .load(imgUrl)
            .error(error)
            .into(view)
}

fun Context.loadImg(imgUrl: Any?, view: AppCompatImageView, error: Int) {
    Glide.with(this)
            .load(imgUrl)
            .error(error)
            .into(view)
}

fun Context.loadImg(imgUrl: Any?, view: AppCompatImageView, error: Drawable?, placeHolder: Drawable?) {
    Glide.with(this)
            .load(imgUrl)
            .placeholder(placeHolder)
            .error(error)
            .into(view)
}


fun Context.loadImg(imgUrl: Any?, view: AppCompatImageView, error: Int, placeHolder: Int) {
    Glide.with(this)
            .load(imgUrl)
            .placeholder(placeHolder)
            .error(error)
            .into(view)
}


fun Context.loadImgNoCache(imgUrl: Any?, view: AppCompatImageView) {
    Glide.with(this)
            .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .load(imgUrl)
            .into(view)
}

fun Context.loadImgNoCache(imgUrl: Any?, view: AppCompatImageView, error: Drawable) {
    Glide.with(this)
            .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .load(imgUrl)
            .error(error)
            .into(view)
}

fun Context.loadImgNoCache(imgUrl: Any?, view: AppCompatImageView, error: Int) {
    Glide.with(this)
            .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .load(imgUrl)
            .error(error)
            .into(view)
}

fun Context.loadImgNoCache(imgUrl: Any?, view: AppCompatImageView, error: Drawable, placeHolder: Drawable) {
    Glide.with(this)
            .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .load(imgUrl)
            .placeholder(placeHolder)
            .error(error)
            .into(view)
}

fun AppCompatImageView.loadImgNoCache(imgUrl: Any?, error: Drawable, placeHolder: Drawable) {
    Glide.with(this)
            .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .load(imgUrl)
            .placeholder(placeHolder)
            .error(error)
            .into(this)
}

fun AppCompatImageView.loadImgNoCache(imgUrl: Any?, error: Int, placeHolder: Int) {
    Glide.with(this)
            .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .load(imgUrl)
            .placeholder(placeHolder)
            .error(error)
            .into(this)
}



fun Context.loadImgWithTransformation(imgUrl: Any?, view: AppCompatImageView, transformation: RequestOptions) {
    Glide.with(this)
            .applyDefaultRequestOptions(transformation)
            .load(imgUrl)
            .into(view)
}


fun View.loadImg(imgUrl: Any?, view: AppCompatImageView) {
    Glide.with(this)
            .load(imgUrl)
            .into(view)
}


fun View.loadImg(imgUrl: Any?, view: AppCompatImageView, error: Drawable) {
    Glide.with(this)
            .load(imgUrl)
            .error(error)
            .into(view)
}

fun View.loadImg(imgUrl: Any?, view: AppCompatImageView, error: Int) {
    Glide.with(this)
            .load(imgUrl)
            .error(error)
            .into(view)
}

fun View.loadImg(imgUrl: Any?, view: AppCompatImageView, error: Drawable, placeHolder: Drawable) {
    Glide.with(this)
            .load(imgUrl)
            .placeholder(placeHolder)
            .error(error)
            .into(view)
}

fun View.loadImg(imgUrl: Any?, view: AppCompatImageView, error: Int, placeHolder: Int) {
    Glide.with(this)
            .load(imgUrl)
            .placeholder(placeHolder)
            .error(error)
            .into(view)
}


fun View.loadImgNoCache(imgUrl: Any?, view: AppCompatImageView) {
    Glide.with(this)
            .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .load(imgUrl)
            .into(view)
}

fun View.loadImgNoCache(imgUrl: Any?, view: AppCompatImageView, error: Drawable) {
    Glide.with(this)
            .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .load(imgUrl)
            .error(error)
            .into(view)
}

fun View.loadImgNoCache(imgUrl: Any?, view: AppCompatImageView, error: Int) {
    Glide.with(this)
            .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .load(imgUrl)
            .error(error)
            .into(view)
}

fun AppCompatImageView.loadImgNoCache(image: Any?) {
    Glide.with(this)
            .load(image)
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .into(this)
}


fun View.loadImgWithTransformation(imgUrl: Any?, view: AppCompatImageView, transformation: RequestOptions) {
    Glide.with(this)
            .applyDefaultRequestOptions(transformation)
            .load(imgUrl)
            .into(view)
}

fun AppCompatImageView.loadImgNoCache(image: Any?, options: () -> BaseRequestOptions<*>) {
    Glide.with(this)
            .load(image)
            .apply(options())
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .into(this)
}

fun AppCompatImageView.loadImage(image: Any?, thisStuff: RequestBuilder<Drawable>.() -> RequestBuilder<Drawable>) {
    Glide.with(this)
            .load(image)
            .thisStuff()
            .into(this)
}

fun AppCompatImageView.loadImage(image: Any?, thisStuff: RequestBuilder<Drawable>.() -> RequestBuilder<Drawable>, options: () -> BaseRequestOptions<*>) {
    Glide.with(this)
            .load(image)
            .thisStuff()
            .apply(options())
            .into(this)
}

