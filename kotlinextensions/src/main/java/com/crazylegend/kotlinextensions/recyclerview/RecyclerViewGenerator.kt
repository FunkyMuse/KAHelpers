package com.crazylegend.kotlinextensions.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.abstracts.AbstractListAdapter


/**
 * Created by crazy on 3/9/20 to long live and prosper !
 */

/**
 * private val generatedAdapter by lazy {
 * generateRecycler<TestModel, TestViewHolder>(
layout = R.layout.recycler_view_item,
viewHolder = TestViewHolder::class.java) { item, holder, _ ->
holder.bind(item)
}
}
 * make sure you have added a proguard rule see [AbstractListAdapter.onCreateViewHolder]
 * @param layout Int the layout res id
 * @param viewHolder Class<VH> this one is used for reflection to instantiate the [RecyclerView.ViewHolder]
 * @param areItemsTheSameCallback callback invocation as a function parameter that returns Boolean as a condition whether items are the same
 * @param areContentsTheSameCallback callback invocation as a function parameter that returns Boolean as a condition whether contents of the items are the same
 * @param binder just as you would call [RecyclerView.Adapter.onBindViewHolder]
 * @return AbstractListAdapter<T, VH>
 */
inline fun <reified T, VH : RecyclerView.ViewHolder> generateRecycler(
        layout: Int,
        viewHolder: Class<VH>,
        noinline areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        crossinline binder: (item: T, holder: VH, position: Int) -> Unit): AbstractListAdapter<T, VH> {

    return object : AbstractListAdapter<T, VH>(viewHolder, areItemsTheSameCallback, areContentsTheSameCallback) {
        override val getLayout: Int
            get() = layout

        override fun bindItems(item: T, holder: VH, position: Int) {
            binder(item, holder, position)
        }
    }
}


/**
 * private val generatedAdapter by lazy {
activityMainBinding.recycler.generateVerticalAdapter<TestModel, TestViewHolder>(
layout = R.layout.recycler_view_item,
viewHolder = TestViewHolder::class.java) { item, holder, _ ->
holder.bind(item)
}
}
 * make sure you have added a proguard rule see [AbstractListAdapter.onCreateViewHolder]
 * @receiver RecyclerView
 * @param layout Int the layout res id
 * @param viewHolder Class<VH> this one is used for reflection to instantiate the [RecyclerView.ViewHolder]
 * @param areItemsTheSameCallback callback invocation as a function parameter that returns Boolean as a condition whether items are the same
 * @param areContentsTheSameCallback callback invocation as a function parameter that returns Boolean as a condition whether contents of the items are the same
 * @param binder just as you would call [RecyclerView.Adapter.onBindViewHolder]
 * @return AbstractListAdapter<T, VH>
 */
inline fun <reified T, VH : RecyclerView.ViewHolder> RecyclerView.generateVerticalAdapter(
        layout: Int,
        viewHolder: Class<VH>,
        noinline areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        crossinline binder: (item: T, holder: VH, position: Int) -> Unit): AbstractListAdapter<T, VH> {

    val adapter = object : AbstractListAdapter<T, VH>(viewHolder, areItemsTheSameCallback, areContentsTheSameCallback) {
        override val getLayout: Int
            get() = layout

        override fun bindItems(item: T, holder: VH, position: Int) {
            binder(item, holder, position)
        }
    }
    initRecyclerViewAdapter(adapter)
    return adapter
}


/**
 * lazily [generateVerticalAdapter]
 */
inline fun <reified T, VH : RecyclerView.ViewHolder> RecyclerView.verticalAdapter(
        layout: Int,
        viewHolder: Class<VH>,
        noinline areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        crossinline binder: (item: T, holder: VH, position: Int) -> Unit): Lazy<AbstractListAdapter<T, VH>> = lazy {
     generateVerticalAdapter(layout, viewHolder, areItemsTheSameCallback, areContentsTheSameCallback, binder)
}


/**
 * lazily [generateRecycler]
 */
inline fun <reified T, VH : RecyclerView.ViewHolder> recyclerAdapter(
        layout: Int,
        viewHolder: Class<VH>,
        noinline areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        crossinline binder: (item: T, holder: VH, position: Int) -> Unit): Lazy<AbstractListAdapter<T, VH>> = lazy {
     generateRecycler(layout, viewHolder, areItemsTheSameCallback, areContentsTheSameCallback, binder)
}


/**
 * same as the other but this initializes the recycler view with has fixed size [generateVerticalAdapter]
 */
inline fun <reified T, VH : RecyclerView.ViewHolder> RecyclerView.generateVerticalAdapter(
        layout: Int,
        viewHolder: Class<VH>,
        hasFixedSize: Boolean = false,
        noinline areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        crossinline binder: (item: T, holder: VH, position: Int) -> Unit): AbstractListAdapter<T, VH> {

    val adapter = object : AbstractListAdapter<T, VH>(viewHolder, areItemsTheSameCallback, areContentsTheSameCallback) {
        override val getLayout: Int
            get() = layout

        override fun bindItems(item: T, holder: VH, position: Int) {
            binder(item, holder, position)
        }
    }
    initRecyclerViewAdapter(adapter, RecyclerView.VERTICAL, hasFixedSize)
    return adapter
}


/**
 * lazily [generateVerticalAdapter]
 */
inline fun <reified T, VH : RecyclerView.ViewHolder> RecyclerView.verticalAdapter(
        layout: Int,
        viewHolder: Class<VH>,
        hasFixedSize: Boolean = false,
        noinline areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        crossinline binder: (item: T, holder: VH, position: Int) -> Unit): Lazy<AbstractListAdapter<T, VH>> = lazy {
    generateVerticalAdapter(layout, viewHolder, hasFixedSize, areItemsTheSameCallback, areContentsTheSameCallback, binder)
}



/**
 * private val generatedAdapter by lazy {
activityMainBinding.recycler.generateHorizontalAdapter<TestModel, TestViewHolder>(
layout = R.layout.recycler_view_item,
viewHolder = TestViewHolder::class.java) { item, holder, _ ->
holder.bind(item)
}
}
 * make sure you have added a proguard rule see [AbstractListAdapter.onCreateViewHolder]
 * @receiver RecyclerView
 * @param layout Int the layout res id
 * @param viewHolder Class<VH> this one is used for reflection to instantiate the [RecyclerView.ViewHolder]
 * @param areItemsTheSameCallback callback invocation as a function parameter that returns Boolean as a condition whether items are the same
 * @param areContentsTheSameCallback callback invocation as a function parameter that returns Boolean as a condition whether contents of the items are the same
 * @param binder just as you would call [RecyclerView.Adapter.onBindViewHolder]
 * @return AbstractListAdapter<T, VH>
 */
inline fun <reified T, VH : RecyclerView.ViewHolder> RecyclerView.generateHorizontalAdapter(
        layout: Int,
        viewHolder: Class<VH>,
        noinline areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        crossinline binder: (item: T, holder: VH, position: Int) -> Unit): AbstractListAdapter<T, VH> {

    val adapter = object : AbstractListAdapter<T, VH>(viewHolder, areItemsTheSameCallback, areContentsTheSameCallback) {
        override val getLayout: Int
            get() = layout

        override fun bindItems(item: T, holder: VH, position: Int) {
            binder(item, holder, position)
        }
    }
    initRecyclerViewAdapter(adapter, RecyclerView.HORIZONTAL)
    return adapter
}

/**
 * lazily [generateHorizontalAdapter]
 */
inline fun <reified T, VH : RecyclerView.ViewHolder> RecyclerView.horizontalAdapter(
        layout: Int,
        viewHolder: Class<VH>,
        noinline areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        crossinline binder: (item: T, holder: VH, position: Int) -> Unit): Lazy<AbstractListAdapter<T, VH>> = lazy {
    generateHorizontalAdapter(layout, viewHolder, areItemsTheSameCallback, areContentsTheSameCallback, binder)
}




inline fun <reified T, VH : RecyclerView.ViewHolder> RecyclerView.generateHorizontalAdapter(
        layout: Int,
        viewHolder: Class<VH>,
        hasFixedSize:Boolean = false,
        noinline areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        crossinline binder: (item: T, holder: VH, position: Int) -> Unit): AbstractListAdapter<T, VH> {

    val adapter = object : AbstractListAdapter<T, VH>(viewHolder, areItemsTheSameCallback, areContentsTheSameCallback) {
        override val getLayout: Int
            get() = layout

        override fun bindItems(item: T, holder: VH, position: Int) {
            binder(item, holder, position)
        }
    }
    initRecyclerViewAdapter(adapter, RecyclerView.HORIZONTAL, hasFixedSize)
    return adapter
}

inline fun <reified T, VH : RecyclerView.ViewHolder> RecyclerView.horizontalAdapter(
        layout: Int,
        viewHolder: Class<VH>,
        hasFixedSize:Boolean = false,
        noinline areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        crossinline binder: (item: T, holder: VH, position: Int) -> Unit): Lazy<AbstractListAdapter<T, VH>> = lazy {
    generateHorizontalAdapter(layout, viewHolder, hasFixedSize, areItemsTheSameCallback, areContentsTheSameCallback, binder)
}




/**
 * The same as the function above, except it receives a layout manager and fixed size for [initRecyclerViewAdapter]
 * @receiver RecyclerView
 * @param layoutManager LayoutManager
 * @param fixedSize Boolean
 * @param layout Int
 * @param viewHolder Class<VH>
 * @param areItemsTheSameCallback Function2<[@kotlin.ParameterName] T, [@kotlin.ParameterName] T, Boolean?>
 * @param areContentsTheSameCallback Function2<[@kotlin.ParameterName] T, [@kotlin.ParameterName] T, Boolean?>
 * @param binder Function3<[@kotlin.ParameterName] T, [@kotlin.ParameterName] VH, [@kotlin.ParameterName] Int, Unit>
 * @return AbstractListAdapter<T, VH>
 */
inline fun <reified T, VH : RecyclerView.ViewHolder> RecyclerView.generateAdapter(
        layoutManager: RecyclerView.LayoutManager, fixedSize: Boolean = false,
        layout: Int,
        viewHolder: Class<VH>,
        noinline areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        crossinline binder: (item: T, holder: VH, position: Int) -> Unit): AbstractListAdapter<T, VH> {

    val adapter = object : AbstractListAdapter<T, VH>(viewHolder, areItemsTheSameCallback, areContentsTheSameCallback) {
        override val getLayout: Int
            get() = layout

        override fun bindItems(item: T, holder: VH, position: Int) {
            binder(item, holder, position)
        }
    }
    initRecyclerViewAdapter(adapter, layoutManager, fixedSize)
    return adapter
}


/**
 * [generateAdapter] but lazily
 */
inline fun <reified T, VH : RecyclerView.ViewHolder> RecyclerView.adapter(
        layoutManager: RecyclerView.LayoutManager, fixedSize: Boolean = false,
        layout: Int,
        viewHolder: Class<VH>,
        noinline areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        crossinline binder: (item: T, holder: VH, position: Int) -> Unit): Lazy<AbstractListAdapter<T, VH>> = lazy {
   generateAdapter(layoutManager, fixedSize, layout, viewHolder, areItemsTheSameCallback, areContentsTheSameCallback, binder)
}

