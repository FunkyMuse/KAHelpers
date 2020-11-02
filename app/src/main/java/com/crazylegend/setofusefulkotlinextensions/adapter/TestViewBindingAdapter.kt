package com.crazylegend.setofusefulkotlinextensions.adapter

import com.crazylegend.customviews.databinding.CustomizableCardViewBinding
import com.crazylegend.recyclerview.AbstractViewBindingAdapter


/**
 * Created by crazy on 4/5/20 to long live and prosper !
 */
class TestViewBindingAdapter : AbstractViewBindingAdapter<TestModel, TestViewHolderShimmer, CustomizableCardViewBinding>(
        ::TestViewHolderShimmer, CustomizableCardViewBinding::inflate
) {

    override fun bindItems(item: TestModel, holder: TestViewHolderShimmer, position: Int, itemCount: Int) {
        holder.bind(item)
    }
}