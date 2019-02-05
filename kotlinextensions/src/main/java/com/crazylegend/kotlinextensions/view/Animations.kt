package com.crazylegend.kotlinextensions.view

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.RelativeLayout
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.crazylegend.kotlinextensions.R
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat


/**
 * Created by Hristijan on 2/4/19 to long live and prosper !
 */

fun slideLeftRight(toHideView: View, fromView: View, toShowView: View, switchViews: Boolean) {

    if (switchViews) {
        if (toShowView.visibility == View.GONE) {
            toShowView.visibility = View.VISIBLE
        }
        val params =
            RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.LEFT_OF, toShowView.id)
        val marginLayoutParams = fromView.layoutParams as ViewGroup.MarginLayoutParams
        marginLayoutParams.setMargins(0, 0, -20, 0)
        params.setMargins(0, 0, -70, 0)
        fromView.layoutParams = params

        fromView.bringToFront()
        val translateAnimation = TranslateAnimation(0f, (-fromView.width / 2).toFloat(), 0f, 0f)
        translateAnimation.duration = 1000
        translateAnimation.fillAfter = true
        toHideView.startAnimation(translateAnimation)

        val translateAnimationRight = TranslateAnimation((-fromView.width / 2).toFloat(), 0f, 0f, 0f)
        translateAnimationRight.duration = 1000
        translateAnimationRight.fillAfter = true
        toShowView.startAnimation(translateAnimationRight)
    } else {
        val params =
            RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.LEFT_OF, toHideView.id)
        val marginLayoutParams = fromView.layoutParams as ViewGroup.MarginLayoutParams
        marginLayoutParams.setMargins(0, 0, -20, 0)
        params.setMargins(0, 0, -70, 0)
        fromView.bringToFront()
        val translateAnimation = TranslateAnimation(0f, (-fromView.width / 2).toFloat(), 0f, 0f)
        translateAnimation.duration = 1000
        translateAnimation.fillAfter = true
        toShowView.startAnimation(translateAnimation)

        // toShowView.setVisibility(View.GONE);

        val translateAnimationRight = TranslateAnimation((-fromView.width / 2).toFloat(), 0f, 0f, 0f)
        translateAnimationRight.duration = 1000
        translateAnimationRight.fillAfter = true
        toHideView.startAnimation(translateAnimationRight)
    }
}


fun View.slideUp() {

    val valueAnimator = ValueAnimator.ofInt(this.height, 0)
    valueAnimator.addUpdateListener { animation ->
        this.layoutParams.height = animation.animatedValue as Int
        this.requestLayout()
    }

    valueAnimator.interpolator = DecelerateInterpolator()
    valueAnimator.duration = 500
    valueAnimator.start()
}


fun Context.slideDown(view:View){
    view.startAnimation( AnimationUtils.loadAnimation(this, R.anim.slide_down))
}

fun Context.slideUp(view:View){
    view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up))
}


fun View.slideDown(context: Context){
    this.startAnimation( AnimationUtils.loadAnimation(context, R.anim.slide_down))
}

fun View.slideUp(context: Context){
    this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up))
}



fun View.slideDown(second: View) {

    val valueAnimator = ValueAnimator.ofInt(0, second.height)
    valueAnimator.interpolator = DecelerateInterpolator()
    valueAnimator.addUpdateListener { animation ->
        this.layoutParams.height = animation.animatedValue as Int
        this.requestLayout()
    }

    valueAnimator.interpolator = DecelerateInterpolator()
    valueAnimator.duration = 500
    valueAnimator.start()
}


fun Context.leftToRight(view:View){
    view.startAnimation( AnimationUtils.loadAnimation(this, R.anim.left_to_right))
}

fun View.animate(context: Context, @IdRes animation:Int){
    this.startAnimation(AnimationUtils.loadAnimation(context, animation))
}

fun Context.animate(view: View, @IdRes animation:Int){
    view.startAnimation(AnimationUtils.loadAnimation(this, animation))
}

fun Context.rightToLeft(view:View){
    view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.right_to_left))
}


fun View.leftToRight(context: Context){
    this.startAnimation( AnimationUtils.loadAnimation(context, R.anim.left_to_right))
}

fun View.rightToLeft(context: Context){
    this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.right_to_left))
}

fun Context.transition(@IdRes enterTransition:Int, @IdRes exitTransition:Int){
    (this as AppCompatActivity).overridePendingTransition(enterTransition,exitTransition)
}

fun Activity.animate( intent: Intent, transitionImage:View, EXTRA_IMAGE:String){
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, transitionImage, EXTRA_IMAGE)
    ActivityCompat.startActivity(this, intent, options.toBundle())
}