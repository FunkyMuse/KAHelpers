package dev.funkymuse.setofusefulkotlinextensions.adapter

import android.view.ViewGroup
import dev.funkymuse.recyclerview.PlaceholderAdapter
import dev.funkymuse.setofusefulkotlinextensions.customviews.databinding.CustomizableCardViewBinding
import dev.funkymuse.view.inflater


class TestPlaceHolderAdapter : PlaceholderAdapter<TestViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            TestViewHolder(CustomizableCardViewBinding.inflate(parent.inflater, parent, false))

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.showPlaceHolder()
    }

    override fun onViewDetachedFromWindow(holder: TestViewHolder) {
        holder.end()
        super.onViewDetachedFromWindow(holder)
    }
}