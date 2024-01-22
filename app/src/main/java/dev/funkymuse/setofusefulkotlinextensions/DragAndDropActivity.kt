package dev.funkymuse.setofusefulkotlinextensions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.funkymuse.customviews.dragAndDrop.DragAndDropListener
import dev.funkymuse.setofusefulkotlinextensions.databinding.ActivityDragAndDropBinding
import dev.funkymuse.viewbinding.viewBinding



class DragAndDropActivity : AppCompatActivity() {

    private val activityMainBinding by viewBinding(ActivityDragAndDropBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        val listener = DragAndDropListener(R.drawable.outline_gray_solid, R.drawable.outline_gray_dashed)
        activityMainBinding.container1.addDragAndDropListener(listener)
        activityMainBinding.container2.addDragAndDropListener(listener)
        activityMainBinding.container3.addDragAndDropListener(listener)
        activityMainBinding.container4.addDragAndDropListener(listener)

    }
}