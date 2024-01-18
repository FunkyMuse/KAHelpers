package com.funkymuse.setofusefulkotlinextensions

import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.app.AppCompatActivity
import com.funkymuse.setofusefulkotlinextensions.databinding.ActivityPinchToZoomBinding
import com.funkymuse.view.pinchToZoom
import com.funkymuse.viewbinding.viewBinding



class PinchToZoomActivity : AppCompatActivity() {
    private val activityMainBinding by viewBinding(ActivityPinchToZoomBinding::inflate)

    private var gestureDetector: ScaleGestureDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)
        gestureDetector = activityMainBinding.image.pinchToZoom()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector?.onTouchEvent(event)
        return true
    }
}


