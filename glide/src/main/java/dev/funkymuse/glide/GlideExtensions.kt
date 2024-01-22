package dev.funkymuse.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Base64
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.RequestOptions


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

fun Context.loadImage(image: Any?, appCompatImageView: AppCompatImageView, thisStuff: RequestBuilder<Drawable>.() -> RequestBuilder<Drawable>) {
    Glide.with(this)
            .load(image)
            .thisStuff()
            .into(appCompatImageView)
}


fun View.loadImage(image: Any?, appCompatImageView: AppCompatImageView, thisStuff: RequestBuilder<Drawable>.() -> RequestBuilder<Drawable>) {
    Glide.with(this)
            .load(image)
            .thisStuff()
            .into(appCompatImageView)
}

fun ImageView.loadBase64Image(base64Image: String?) {
    base64Image?.let {
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
