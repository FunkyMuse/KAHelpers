package com.crazylegend.setofusefulkotlinextensions.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.setofusefulkotlinextensions.R
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val card = itemView.testCard

    init {
        with(card.cardView) {
            setContentPadding(10, 10, 10, 10)
        }
    }

    fun bind(item: TestModel) {
        card.title = item.title
        card.content = item.body
        card.imageView.setImageResource(R.drawable.pin_code_highlight_state)
    }
}