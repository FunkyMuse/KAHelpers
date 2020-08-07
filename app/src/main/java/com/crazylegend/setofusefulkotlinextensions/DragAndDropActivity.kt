package com.crazylegend.setofusefulkotlinextensions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.crazylegend.kotlinextensions.dragAndDrop.DragAndDropListener
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
import com.crazylegend.setofusefulkotlinextensions.databinding.ActivityDragAndDropBinding


/**
 * Created by crazy on 8/7/20 to long live and prosper !
 */
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