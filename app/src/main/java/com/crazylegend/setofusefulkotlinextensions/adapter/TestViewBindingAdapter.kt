package com.crazylegend.setofusefulkotlinextensions.adapter

import com.crazylegend.customviews.databinding.CustomizableCardViewBinding
import com.crazylegend.kotlinextensions.abstracts.AbstractViewBindingAdapter


/**
 * Created by crazy on 4/5/20 to long live and prosper !
 */
class TestViewBindingAdapter : AbstractViewBindingAdapter<TestModel, TestViewHolderBinding, CustomizableCardViewBinding>(TestViewHolderBinding::class.java,
        CustomizableCardViewBinding::inflate) {

    override fun bindItems(item: TestModel, holder: TestViewHolderBinding, position: Int, itemCount: Int) {
        holder.bind(item)
    }
}