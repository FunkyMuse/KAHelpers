package com.funkymuse.setofusefulkotlinextensions.adapter

import android.view.ViewGroup
import com.funkymuse.recyclerview.PlaceholderAdapter
import com.funkymuse.setofusefulkotlinextensions.customviews.databinding.CustomizableCardViewBinding
import com.funkymuse.view.inflater


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