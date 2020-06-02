package com.crazylegend.setofusefulkotlinextensions.adapter

import com.crazylegend.kotlinextensions.abstracts.AbstractViewBindingAdapterRxBus
import com.crazylegend.kotlinextensions.databinding.CustomizableCardViewBinding


/**
 * Created by crazy on 4/5/20 to long live and prosper !
 */
class TestViewBindingAdapter : AbstractViewBindingAdapterRxBus<TestModel, TestViewHolderBinding, CustomizableCardViewBinding>(TestViewHolderBinding::class.java,
        CustomizableCardViewBinding::inflate) {

    override fun bindItems(item: TestModel, holder: TestViewHolderBinding, position: Int, itemCount: Int) {
        holder.bind(item)
    }
}