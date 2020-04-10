package com.crazylegend.setofusefulkotlinextensions.adapter

import android.view.ViewGroup
import com.crazylegend.kotlinextensions.recyclerview.PlaceholderAdapter
import com.crazylegend.kotlinextensions.views.inflate
import com.crazylegend.setofusefulkotlinextensions.R


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */
class TestPlaceHolderAdapter : PlaceholderAdapter<TestViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TestViewHolder(parent.inflate(R.layout.customizable_card_view))

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
       holder.showPlaceHolder()
    }
}