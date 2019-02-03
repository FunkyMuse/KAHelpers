package com.crazylegend.kotlinextensions

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */

inline fun <reified T> Context.launch() {
    this.startActivity(Intent(this, T::class.java))
}

inline fun <reified T> Fragment.launch() {
    this.requireActivity().startActivity(Intent(this.requireActivity(), T::class.java))
}

fun Context.shortToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Fragment.shortToast(text: String) {
    Toast.makeText(this.requireActivity(), text, Toast.LENGTH_SHORT).show()
}

fun Fragment.longToast(text: String) {
    Toast.makeText(this.requireActivity(), text, Toast.LENGTH_LONG).show()
}

fun Fragment.finish() {
    this.requireActivity().finish()
}
fun AppCompatActivity.showBackButton() {
    this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
}

fun Fragment.getAppCompatActivity(): AppCompatActivity {
    return this.requireActivity() as AppCompatActivity
}

fun Context.snackBar(text: String, actionText: String, length:Int, action: () -> Unit) {
    this as AppCompatActivity
    val snackbar =
        Snackbar.make(this.findViewById(android.R.id.content), text, length)
    snackbar.setAction(actionText) {
        action()
        snackbar.dismiss()
    }
    snackbar.show()

}

@Throws(GlideException::class)
 fun Context.loadImg(imgUrl: String, view: ImageView) {
    Glide.with(this)
        .load(imgUrl)
        .into(view)
}


@Throws(GlideException::class)
 fun Context.loadImgNoCache(imgUrl: String, view: ImageView) {
    Glide.with(this)
        .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
        .load(imgUrl)
        .into(view)
}

@Throws(GlideException::class)
 fun Context.loadImgWithTransformation(imgUrl: String, view: ImageView, transformation:RequestOptions) {
    Glide.with(this)
        .applyDefaultRequestOptions(transformation)
        .load(imgUrl)
        .into(view)
}