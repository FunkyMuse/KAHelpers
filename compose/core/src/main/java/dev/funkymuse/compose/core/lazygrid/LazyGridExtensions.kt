package dev.funkymuse.compose.core.lazygrid

import androidx.compose.foundation.lazy.grid.LazyGridState


/**
 * Created by funkymuse on 4/4/21 to long live and prosper !
 */

/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [isScrolledToTheEndState]
 * Usage: val hasUserScrolledToTheEnd by remember { derivedStateOf { mySavedListState.isScrolledToTheEnd() } }
 * @receiver _root_ide_package_.androidx.compose.foundation.lazy.grid.LazyGridState
 * @return Boolean
 */
fun LazyGridState.isScrolledToTheEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [isScrolledToTheEndAndNotScrollingState]
 * Usage: val isScrolledToTheEndAndNotScrolling by remember { derivedStateOf { mySavedListState.isScrolledToTheEndAndNotScrolling() } }
 * @receiver _root_ide_package_.androidx.compose.foundation.lazy.grid.LazyGridState
 * @return Boolean
 */
fun LazyGridState.isScrolledToTheEndAndNotScrolling() = isScrolledToTheEnd() && !isScrollInProgress

/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [lastVisibleIndexState]
 * Usage: val lastVisibleIndex by remember { derivedStateOf { mySavedListState.lastVisibleIndex() } }
 * @receiver _root_ide_package_.androidx.compose.foundation.lazy.grid.LazyGridState
 * @return Boolean
 */
fun LazyGridState.lastVisibleIndex() = layoutInfo.visibleItemsInfo.lastOrNull()?.index


/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [firstVisibleIndexState]
 * Usage: val firstVisibleIndex by remember { derivedStateOf { mySavedListState.firstVisibleIndex() } }
 * @receiver _root_ide_package_.androidx.compose.foundation.lazy.grid.LazyGridState
 * @return Boolean
 */
fun LazyGridState.firstVisibleIndex() = layoutInfo.visibleItemsInfo.firstOrNull()?.index


/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [visibleItemSizeState]
 * Usage: val visibleItemsSize by remember { derivedStateOf { mySavedListState.visibleItemsSize() } }
 * @receiver _root_ide_package_.androidx.compose.foundation.lazy.grid.LazyGridState
 * @return Int
 */
fun LazyGridState.visibleItemsSize() = layoutInfo.visibleItemsInfo.size

/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [totalItemsCountState]
 * Usage: val totalItemsCount by remember { derivedStateOf { mySavedListState.totalItemsCount() } }
 * @receiver _root_ide_package_.androidx.compose.foundation.lazy.grid.LazyGridState
 * @return Int
 */
fun LazyGridState.totalItemsCount() = layoutInfo.totalItemsCount

/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [viewportEndOffsetState]
 * Usage: val viewportEndOffset by remember { derivedStateOf { mySavedListState.viewportEndOffset() } }
 * @receiver _root_ide_package_.androidx.compose.foundation.lazy.grid.LazyGridState
 * @return Int
 */
fun LazyGridState.viewportEndOffset() = layoutInfo.viewportEndOffset

/**
 * Use [androidx.compose.runtime.remember] with [androidx.compose.runtime.derivedStateOf]
 * in order to avoid unnecessary composition or [viewportStartOffsetState]
 * Usage: val viewportStartOffset by remember { derivedStateOf { mySavedListState.viewportStartOffset() } }
 * @receiver _root_ide_package_.androidx.compose.foundation.lazy.grid.LazyGridState
 * @return Int
 */
fun LazyGridState.viewportStartOffset() = layoutInfo.viewportStartOffset
