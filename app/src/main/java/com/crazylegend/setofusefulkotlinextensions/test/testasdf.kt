package com.crazylegend.setofusefulkotlinextensions.test

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.randomUUIDstring
import com.crazylegend.kotlinextensions.views.inflate
import com.crazylegend.setofusefulkotlinextensions.R


/**
 * Created by hristijan on 10/25/19 to long live and prosper !
 */
//extract the classes to separate files


/**
 * Template created by Hristijan to live long and prosper.
 */

class TestAdapter : ListAdapter<TestModel, TestViewHolder>(TestDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TestViewHolder(parent.inflate(R.layout.recycler_view_item))


    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val item = getItem(position) // model instance

    }

}



data class TestModel(val name: String = randomUUIDstring)

class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class TestDiffUtil : DiffUtil.ItemCallback<TestModel>() {
    override fun areItemsTheSame(oldItem: TestModel, newItem: TestModel) = oldItem == newItem

    override fun areContentsTheSame(oldItem: TestModel, newItem: TestModel) = oldItem == newItem
}
