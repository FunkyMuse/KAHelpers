package com.crazylegend.setofusefulkotlinextensions.adapter

import com.crazylegend.kotlinextensions.abstracts.AbstractViewBindingAdapter
import com.crazylegend.setofusefulkotlinextensions.databinding.RecyclerViewItemBinding


/**
 * Created by crazy on 4/5/20 to long live and prosper !
 */
class TestViewBindingAdapter : AbstractViewBindingAdapter<TestModel, TestViewHolderBinding, RecyclerViewItemBinding>(TestViewHolderBinding::class.java,
        RecyclerViewItemBinding::inflate) {

    override fun bindItems(item: TestModel, holder: TestViewHolderBinding, position: Int) {
        holder.bind(item)
    }
}