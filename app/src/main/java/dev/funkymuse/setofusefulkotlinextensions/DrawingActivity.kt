package dev.funkymuse.setofusefulkotlinextensions

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.app.AppCompatActivity
import dev.funkymuse.kotlinextensions.motionEvent.isNewGesture
import dev.funkymuse.kotlinextensions.views.clear
import dev.funkymuse.setofusefulkotlinextensions.databinding.ActivityDrawingBinding
import dev.funkymuse.viewbinding.viewBinding


class DrawingActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityDrawingBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.touchListenerChoices.setOnCheckedStateChangeListener { chipGroup, _ ->
            val checkedId = chipGroup.checkedChipId
            binding.drawingCanvas.isTouchEventListenerEnabled = false
            binding.drawingCanvas.clear()
            binding.gestureDescription.clear()
            when (checkedId) {
                R.id.defaultTouch -> setUpDefaultTouchListener()
                R.id.singleTouch -> setUpSingleTouchListener()
                R.id.multiTouch -> setUpMultiTouchListener()
                R.id.gestureDetector -> setUpGestureDetector()
                R.id.scaleGestureDetector -> setUpScaleGestureDetector()
            }
        }
    }

    private fun setUpDefaultTouchListener() {
        binding.drawingCanvas.setOnTouchListener(null)
        binding.drawingCanvas.isTouchEventListenerEnabled = true
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpSingleTouchListener() {
        binding.drawingCanvas.setOnTouchListener { _, event ->
            val eventDescription = event.singleTouchDescription()
            when {
                eventDescription.isEmpty() -> {
                    return@setOnTouchListener super.onTouchEvent(event)
                }

                event.isNewGesture() -> {
                    binding.gestureDescription.text = eventDescription
                    return@setOnTouchListener true
                }

                else -> {
                    binding.gestureDescription.text = (eventDescription)
                    return@setOnTouchListener true
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpMultiTouchListener() {
        binding.drawingCanvas.setOnTouchListener { _, event ->
            val eventDescription = event.multiTouchDescription()
            when {
                eventDescription.isEmpty() -> {
                    return@setOnTouchListener super.onTouchEvent(event)
                }

                event.isNewGesture() -> {
                    binding.gestureDescription.text = (eventDescription)
                    return@setOnTouchListener true
                }

                else -> {
                    binding.gestureDescription.text = (eventDescription)
                    return@setOnTouchListener true
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    private fun setUpGestureDetector() {
        val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
            override fun onShowPress(event: MotionEvent) {
                binding.gestureDescription.text = (event.description("Press"))
            }

            override fun onSingleTapUp(event: MotionEvent): Boolean {
                binding.gestureDescription.text = (event.description("Single tap up"))
                return true
            }

            override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
                binding.gestureDescription.text = (event.description("Single tap confirmed"))
                return true
            }

            override fun onDown(event: MotionEvent): Boolean {
                binding.gestureDescription.text = ("")
                binding.gestureDescription.text = (event.description("Down"))
                return true
            }

            override fun onFling(
                event1: MotionEvent?, event2: MotionEvent, velocityX: Float,
                velocityY: Float
            ): Boolean {
                binding.gestureDescription.text = (event1.description("Fling start"))
                binding.gestureDescription.text = (event2.description("Fling end"))
                binding.gestureDescription.text =
                    ("Fling velocity (${velocityX.toInt()}px/s, ${velocityY.toInt()}px/s)")
                return true
            }

            override fun onScroll(
                event1: MotionEvent?, event2: MotionEvent, distanceX: Float,
                distanceY: Float
            ): Boolean {
                binding.gestureDescription.text = (event2.description("Scroll"))
                binding.gestureDescription.text =
                    ("Scroll distance (${distanceX.toInt()}, ${distanceY.toInt()})")
                return true
            }

            override fun onLongPress(event: MotionEvent) {
                binding.gestureDescription.text = (event.description("Long press"))
            }

            override fun onDoubleTap(event: MotionEvent): Boolean {
                binding.gestureDescription.text = (event.description("Double tap"))
                return true
            }

            override fun onDoubleTapEvent(event: MotionEvent): Boolean {
                binding.gestureDescription.text = (event.description("Double tap event"))
                return true
            }

            override fun onContextClick(event: MotionEvent): Boolean {
                binding.gestureDescription.text = (event.description("Context click"))
                return true
            }

            fun MotionEvent?.description(description: String): String {
                return if (this == null) "Empty press" else "$description at (${x.toInt()}, ${y.toInt()})"
            }
        }

        val gestureDetector = GestureDetector(this, gestureListener)

        binding.drawingCanvas.setOnTouchListener { _, event ->
            val isEventHandledByGestureListener = gestureDetector.onTouchEvent(event)
            if (isEventHandledByGestureListener) {
                return@setOnTouchListener true
            } else {
                return@setOnTouchListener super.onTouchEvent(event)
            }
        }
    }

    fun ScaleGestureDetector?.description(type: String): String {
        return when {
            this == null -> "Null scale gesture detector"
            type.isEmpty() -> "Scale event with factor ${this.scaleFactor}"
            else -> "$type Scale event with factor ${this.scaleFactor}"
        }
    }

    fun MotionEvent.singleTouchDescription(): String {
        val eventLiteral = when (action) {
            MotionEvent.ACTION_DOWN -> "Down"
            MotionEvent.ACTION_UP -> "Up"
            MotionEvent.ACTION_MOVE -> "Move"
            MotionEvent.ACTION_CANCEL -> "Cancel"
            MotionEvent.ACTION_OUTSIDE -> "Outside"
            else -> ""
        }
        return if (eventLiteral.isEmpty()) {
            ""
        } else {
            "$eventLiteral action at (${x.toInt()}, ${y.toInt()})"
        }
    }

    fun MotionEvent.multiTouchDescription(): String {
        val eventLiteral = when (actionMasked) {
            MotionEvent.ACTION_DOWN -> "Down"
            MotionEvent.ACTION_UP -> "Up"
            MotionEvent.ACTION_MOVE -> "Move"
            MotionEvent.ACTION_CANCEL -> "Cancel"
            MotionEvent.ACTION_OUTSIDE -> "Outside"
            MotionEvent.ACTION_POINTER_DOWN -> "Pointer down"
            MotionEvent.ACTION_POINTER_UP -> "Pointer up"
            else -> ""
        }
        return if (eventLiteral.isEmpty()) {
            ""
        } else {
            val stringBuilder = StringBuilder("$eventLiteral action")
            if (actionMasked == MotionEvent.ACTION_POINTER_DOWN || actionMasked == MotionEvent.ACTION_POINTER_UP) {
                stringBuilder.append(" (pointer id: ${actionIndex})")
            }
            stringBuilder.append(" {")
            for (i in 0 until pointerCount) {
                stringBuilder.append(
                    "\n\t\tPointer with id ${getPointerId(i)} at (${getX(i).toInt()}, ${
                        getY(
                            i
                        ).toInt()
                    })"
                )
            }
            stringBuilder.append("\n}")
            stringBuilder.toString()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpScaleGestureDetector() {
        val scaleGestureDetectorListener = object : ScaleGestureDetector.OnScaleGestureListener {
            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                binding.gestureDescription.text = (detector.description("Begin"))
                return true
            }

            override fun onScale(detector: ScaleGestureDetector): Boolean {
                binding.gestureDescription.text = (detector.description(""))
                return true
            }

            override fun onScaleEnd(detector: ScaleGestureDetector) {
                binding.gestureDescription.text = (detector.description("End"))
            }
        }

        val scaleGestureDetector = ScaleGestureDetector(this, scaleGestureDetectorListener)

        binding.drawingCanvas.setOnTouchListener { _, event ->
            val isEventHandledByGestureListener = scaleGestureDetector.onTouchEvent(event)
            if (isEventHandledByGestureListener) {
                return@setOnTouchListener true
            } else {
                return@setOnTouchListener super.onTouchEvent(event)
            }
        }
    }
}

