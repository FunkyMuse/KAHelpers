package dev.funkymuse.compose.core.lazylist

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember

/**
 * Created by funkymuse on 8/4/21 to long live and prosper !
 */

@Composable
fun LazyListState.isScrolledToTheEndState() =
    remember { derivedStateOf { isScrolledToTheEnd() } }

@Composable
fun LazyListState.isScrolledToTheEndAndNotScrollingState() =
    remember { derivedStateOf { isScrolledToTheEndAndNotScrolling() } }

@Composable
fun LazyListState.lastVisibleIndexState() =
    remember { derivedStateOf { lastVisibleIndex() } }

@Composable
fun LazyListState.firstVisibleIndexState() =
    remember { derivedStateOf { firstVisibleIndex() } }

@Composable
fun LazyListState.visibleItemSizeState() =
    remember { derivedStateOf { visibleItemsSize() } }

@Composable
fun LazyListState.totalItemsCountState() =
    remember { derivedStateOf { totalItemsCount() } }

@Composable
fun LazyListState.viewportEndOffsetState() =
    remember { derivedStateOf { viewportEndOffset() } }


@Composable
fun LazyListState.viewportStartOffsetState() =
    remember { derivedStateOf { viewportStartOffset() } }


