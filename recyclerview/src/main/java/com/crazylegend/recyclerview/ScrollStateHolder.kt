package com.crazylegend.recyclerview

import android.os.Bundle
import android.os.Parcelable
import androidx.recyclerview.widget.RecyclerView

/**
 * Persists scroll state for nested RecyclerViews.
 *
 * 1. Call [saveScrollState] in [RecyclerView.Adapter.onViewRecycled]
 * to save the scroll position.
 *
 * 2. Call [restoreScrollState] in [RecyclerView.Adapter.onBindViewHolder]
 * after changing the adapter's contents to restore the scroll position
 *
 *

In fragment or activity
override fun onSaveInstanceState(outState: Bundle) {
super.onSaveInstanceState(outState)
if (::scrollStateHolder.isInitialized)
scrollStateHolder.onSaveInstanceState(outState)
}

private lateinit var scrollStateHolder: ScrollStateHolder
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
super.onViewCreated(view, savedInstanceState)
scrollStateHolder = ScrollStateHolder(savedInstanceState)
}
 */
class ScrollStateHolder(savedInstanceState: Bundle? = null) {

    companion object {
        const val STATE_BUNDLE = "scroll_state_bundle"
    }

    /**
     * Provides a key that uniquely identifies a RecyclerView
     */
    interface ScrollStateKeyProvider {
        fun getScrollStateKey(): String?
    }


    /**
     * Persists the [RecyclerView.LayoutManager] states
     */
    private val scrollStates = hashMapOf<String, Parcelable>()

    /**
     * Keeps track of the keys that point to RecyclerViews
     * that have new scroll states that should be saved
     */
    private val scrolledKeys = mutableSetOf<String>()

    init {
        savedInstanceState?.getBundle(STATE_BUNDLE)?.let { bundle ->
            bundle.keySet().forEach { key ->
                bundle.getParcelable<Parcelable>(key)?.let {
                    scrollStates[key] = it
                }
            }
        }
    }

    fun setupRecyclerView(recyclerView: RecyclerView, scrollKeyProvider: ScrollStateKeyProvider) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    saveScrollState(recyclerView, scrollKeyProvider)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val key = scrollKeyProvider.getScrollStateKey()
                if (key != null && dx != 0) {
                    scrolledKeys.add(key)
                }
            }
        })
    }

    fun onSaveInstanceState(outState: Bundle) {
        val stateBundle = Bundle()
        scrollStates.entries.forEach {
            stateBundle.putParcelable(it.key, it.value)
        }
        outState.putBundle(STATE_BUNDLE, stateBundle)
    }

    fun clearScrollState() {
        scrollStates.clear()
        scrolledKeys.clear()
    }

    /**
     * Saves this RecyclerView layout state for a given key
     */
    fun saveScrollState(
            recyclerView: RecyclerView,
            scrollKeyProvider: ScrollStateKeyProvider
    ) {
        val key = scrollKeyProvider.getScrollStateKey() ?: return
        // Check if we scrolled the RecyclerView for this key
        if (scrolledKeys.contains(key)) {
            val layoutManager = recyclerView.layoutManager ?: return
            layoutManager.onSaveInstanceState()?.let { scrollStates[key] = it }
            scrolledKeys.remove(key)
        }
    }

    /**
     * Restores this RecyclerView layout state for a given key
     */
    fun restoreScrollState(
            recyclerView: RecyclerView,
            scrollKeyProvider: ScrollStateKeyProvider
    ) {
        val key = scrollKeyProvider.getScrollStateKey() ?: return
        val layoutManager = recyclerView.layoutManager ?: return
        val savedState = scrollStates[key]
        if (savedState != null) {
            layoutManager.onRestoreInstanceState(savedState)
        } else {
            // If we don't have any state for this RecyclerView,
            // make sure we reset the scroll position
            layoutManager.scrollToPosition(0)
        }
        // Mark this key as not scrolled since we just restored the state
        scrolledKeys.remove(key)
    }

}

/*

inside the parent adapter and the recycler must use com.crazylegend.recyclerview.OrientationAwareRecyclerView


 override fun onViewRecycled(holder: AdventuresParentViewHolder) {
        super.onViewRecycled(holder)
        holder.onRecycled()
    }

    override fun onViewDetachedFromWindow(holder: AdventuresParentViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetachedFromWindow()
    }


class AdventuresParentViewHolder(binding: ItemviewRouteBinding,
                                 private val scrollStateHolder: ScrollStateHolder,
                                 clickListener: forItemClickListener<RouteModel>) : AbstractViewHolder(binding.root), ScrollStateHolder.ScrollStateKeyProvider {

    private val titleTextView = binding.titleText
    private val recyclerView = binding.recycler
    private val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    private val adapter = RouteAdapter()
    private val snapHelper = GravitySnapHelper(Gravity.START)
    private var currentItem: AdventuresAdapterModel? = null

    override fun getScrollStateKey(): String? = currentItem?.id.toString()

    init {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator?.changeDuration = 0
        snapHelper.attachToRecyclerView(recyclerView)
        scrollStateHolder.setupRecyclerView(recyclerView, this)
        adapter.forItemClickListener = clickListener
        addDragListener(recyclerView, CategoriesFragment.onViewPagerCanDrag)
    }

    fun onBound(item: AdventuresAdapterModel) {
        currentItem = item
        titleTextView.text = item.title
        adapter.submitList(item.list)
        scrollStateHolder.restoreScrollState(recyclerView, this)
    }

    fun onRecycled() {
        scrollStateHolder.saveScrollState(recyclerView, this)
        currentItem = null
    }

    */
/**
 * If we fast scroll while this ViewHolder's RecyclerView is still settling the scroll,
 * the view will be detached and won't be snapped correctly
 *
 * To fix that, we snap again without smooth scrolling.
 *//*

    fun onDetachedFromWindow() {
        if (recyclerView.scrollState != RecyclerView.SCROLL_STATE_IDLE) {
            snapHelper.findSnapView(layoutManager)?.let {
                val snapDistance = snapHelper.calculateDistanceToFinalSnap(layoutManager, it)
                if (snapDistance[0] != 0 || snapDistance[1] != 0) {
                    recyclerView.scrollBy(snapDistance[0], snapDistance[1])
                }
            }
        }
    }

    private fun addDragListener(recyclerView: RecyclerView, onViewPagerCanDrag: onViewPagerCanDrag?) {
        recyclerView.scrollListener({ _, newState ->
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                onViewPagerCanDrag?.canDrag()
            } else {
                onViewPagerCanDrag?.canNotDrag()
            }
        })
    }

}*/
