package com.crazylegend.setofusefulkotlinextensions.adapter

import com.crazylegend.kotlinextensions.abstracts.AbstractListAdapter2
import com.crazylegend.setofusefulkotlinextensions.R


/**
 * Created by hristijan on 10/25/19 to long live and prosper !
 */
//extract the classes to separate files


/**
 * Template created by Hristijan to live long and prosper.
 */

class TestAdapter22 : AbstractListAdapter2<TestModel, TestViewHolder>(
        TestViewHolder::class.java,
        areContentsTheSameCallback = {old, new -> old.id == new.id },
        areItemsTheSameCallback = {old, new ->old.id == new.id }) {

    override val getLayout: Int
        get() = R.layout.recycler_view_item

    override fun bindItems(item: TestModel, holder: TestViewHolder, position: Int) {
        holder.bind(item)
    }

}


