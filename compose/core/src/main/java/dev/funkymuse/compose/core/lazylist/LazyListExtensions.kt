package dev.funkymuse.compose.core.lazylist

import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.LazyListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by funkymuse on 4/4/21 to long live and prosper !
 */

/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [isScrolledToTheEndState]
 * Usage: val hasUserScrolledToTheEnd by remember { derivedStateOf { mySavedListState.isScrolledToTheEnd() } }
 * @receiver LazyListState
 * @return Boolean
 */
fun LazyListState.isScrolledToTheEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [isScrolledToTheEndAndNotScrollingState]
 * Usage: val isScrolledToTheEndAndNotScrolling by remember { derivedStateOf { mySavedListState.isScrolledToTheEndAndNotScrolling() } }
 * @receiver LazyListState
 * @return Boolean
 */
fun LazyListState.isScrolledToTheEndAndNotScrolling() = isScrolledToTheEnd() && !isScrollInProgress

/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [lastVisibleIndexState]
 * Usage: val lastVisibleIndex by remember { derivedStateOf { mySavedListState.lastVisibleIndex() } }
 * @receiver LazyListState
 * @return Boolean
 */
fun LazyListState.lastVisibleIndex() = layoutInfo.visibleItemsInfo.lastOrNull()?.index


/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [firstVisibleIndexState]
 * Usage: val firstVisibleIndex by remember { derivedStateOf { mySavedListState.firstVisibleIndex() } }
 * @receiver LazyListState
 * @return Boolean
 */
fun LazyListState.firstVisibleIndex() = layoutInfo.visibleItemsInfo.firstOrNull()?.index


/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [visibleItemSizeState]
 * Usage: val visibleItemsSize by remember { derivedStateOf { mySavedListState.visibleItemsSize() } }
 * @receiver LazyListState
 * @return Int
 */
fun LazyListState.visibleItemsSize() = layoutInfo.visibleItemsInfo.size

/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [totalItemsCountState]
 * Usage: val totalItemsCount by remember { derivedStateOf { mySavedListState.totalItemsCount() } }
 * @receiver LazyListState
 * @return Int
 */
fun LazyListState.totalItemsCount() = layoutInfo.totalItemsCount

/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [viewportEndOffsetState]
 * Usage: val viewportEndOffset by remember { derivedStateOf { mySavedListState.viewportEndOffset() } }
 * @receiver LazyListState
 * @return Int
 */
fun LazyListState.viewportEndOffset() = layoutInfo.viewportEndOffset

/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [viewportStartOffsetState]
 * Usage: val viewportStartOffset by remember { derivedStateOf { mySavedListState.viewportStartOffset() } }
 * @receiver LazyListState
 * @return Int
 */
fun LazyListState.viewportStartOffset() = layoutInfo.viewportStartOffset

fun LazyListState.animateScrollAndCentralizeItem(index: Int, coroutineScope: CoroutineScope) {
    val itemInfo = layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
    coroutineScope.launch {
        if (itemInfo != null) {
            val center = this@animateScrollAndCentralizeItem.layoutInfo.viewportEndOffset / 2
            val childCenter = itemInfo.offset + itemInfo.size / 2
            this@animateScrollAndCentralizeItem.animateScrollBy((childCenter - center).toFloat())
        } else {
            this@animateScrollAndCentralizeItem.animateScrollToItem(index)
        }
    }
}
