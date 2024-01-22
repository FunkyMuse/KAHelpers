package dev.funkymuse.setofusefulkotlinextensions.adapter

import dev.funkymuse.recyclerview.AbstractViewBindingAdapter
import dev.funkymuse.setofusefulkotlinextensions.customviews.databinding.CustomizableCardViewBinding


class TestViewBindingAdapter : AbstractViewBindingAdapter<TestModel, TestViewHolderShimmer, CustomizableCardViewBinding>(
        ::TestViewHolderShimmer, CustomizableCardViewBinding::inflate
) {

    override fun bindItems(item: TestModel, holder: TestViewHolderShimmer, position: Int, itemCount: Int) {
        holder.bind(item)
    }
}