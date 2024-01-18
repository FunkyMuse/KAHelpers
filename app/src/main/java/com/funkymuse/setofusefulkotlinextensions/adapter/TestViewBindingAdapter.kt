package com.funkymuse.setofusefulkotlinextensions.adapter

import com.funkymuse.recyclerview.AbstractViewBindingAdapter
import com.funkymuse.setofusefulkotlinextensions.customviews.databinding.CustomizableCardViewBinding


class TestViewBindingAdapter : AbstractViewBindingAdapter<TestModel, TestViewHolderShimmer, CustomizableCardViewBinding>(
        ::TestViewHolderShimmer, CustomizableCardViewBinding::inflate
) {

    override fun bindItems(item: TestModel, holder: TestViewHolderShimmer, position: Int, itemCount: Int) {
        holder.bind(item)
    }
}