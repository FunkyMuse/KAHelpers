package com.crazylegend.setofusefulkotlinextensions

import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.app.AppCompatActivity
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
import com.crazylegend.kotlinextensions.views.pinchToZoom
import com.crazylegend.setofusefulkotlinextensions.databinding.ActivityPinchToZoomBinding


/**
 * Created by crazy on 8/7/20 to long live and prosper !
 */
class PinchToZoomActivity : AppCompatActivity() {
    private val activityMainBinding by viewBinding(ActivityPinchToZoomBinding::inflate)

    private var gestureDetector: ScaleGestureDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)
        gestureDetector = activityMainBinding.image.pinchToZoom()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector?.onTouchEvent(event)
        return true
    }
}


