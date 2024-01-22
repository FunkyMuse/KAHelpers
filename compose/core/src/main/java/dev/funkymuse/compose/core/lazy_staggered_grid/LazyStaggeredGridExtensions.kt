package dev.funkymuse.compose.core.lazy_staggered_grid

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState


/**
 * Created by funkymuse on 4/4/21 to long live and prosper !
 */

/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [isScrolledToTheEndState]
 * Usage: val hasUserScrolledToTheEnd by remember { derivedStateOf { mySavedListState.isScrolledToTheEnd() } }
 * @receiver _root_ide_package_.androidx.compose.foundation.lazy.grid.LazyStaggeredGridState
 * @return Boolean
 */
fun LazyStaggeredGridState.isScrolledToTheEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [isScrolledToTheEndAndNotScrollingState]
 * Usage: val isScrolledToTheEndAndNotScrolling by remember { derivedStateOf { mySavedListState.isScrolledToTheEndAndNotScrolling() } }
 * @receiver _root_ide_package_.androidx.compose.foundation.lazy.grid.LazyStaggeredGridState
 * @return Boolean
 */
fun LazyStaggeredGridState.isScrolledToTheEndAndNotScrolling() = isScrolledToTheEnd() && !isScrollInProgress

/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [lastVisibleIndexState]
 * Usage: val lastVisibleIndex by remember { derivedStateOf { mySavedListState.lastVisibleIndex() } }
 * @receiver _root_ide_package_.androidx.compose.foundation.lazy.grid.LazyStaggeredGridState
 * @return Boolean
 */
fun LazyStaggeredGridState.lastVisibleIndex() = layoutInfo.visibleItemsInfo.lastOrNull()?.index


/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [firstVisibleIndexState]
 * Usage: val firstVisibleIndex by remember { derivedStateOf { mySavedListState.firstVisibleIndex() } }
 * @receiver _root_ide_package_.androidx.compose.foundation.lazy.grid.LazyStaggeredGridState
 * @return Boolean
 */
fun LazyStaggeredGridState.firstVisibleIndex() = layoutInfo.visibleItemsInfo.firstOrNull()?.index


/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [visibleItemSizeState]
 * Usage: val visibleItemsSize by remember { derivedStateOf { mySavedListState.visibleItemsSize() } }
 * @receiver _root_ide_package_.androidx.compose.foundation.lazy.grid.LazyStaggeredGridState
 * @return Int
 */
fun LazyStaggeredGridState.visibleItemsSize() = layoutInfo.visibleItemsInfo.size

/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [totalItemsCountState]
 * Usage: val totalItemsCount by remember { derivedStateOf { mySavedListState.totalItemsCount() } }
 * @receiver _root_ide_package_.androidx.compose.foundation.lazy.grid.LazyStaggeredGridState
 * @return Int
 */
fun LazyStaggeredGridState.totalItemsCount() = layoutInfo.totalItemsCount