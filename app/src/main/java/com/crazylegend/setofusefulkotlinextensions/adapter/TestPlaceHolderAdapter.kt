package com.crazylegend.setofusefulkotlinextensions.adapter

import android.view.ViewGroup
import com.crazylegend.customviews.databinding.CustomizableCardViewBinding
import com.crazylegend.recyclerview.PlaceholderAdapter
import com.crazylegend.view.inflater


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */
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