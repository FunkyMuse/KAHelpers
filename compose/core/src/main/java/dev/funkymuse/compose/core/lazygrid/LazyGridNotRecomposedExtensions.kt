package dev.funkymuse.compose.core.lazygrid

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember

/**
 * Created by funkymuse on 8/4/21 to long live and prosper !
 */

@Composable
fun LazyGridState.isScrolledToTheEndState() =
    remember { derivedStateOf { isScrolledToTheEnd() } }

@Composable
fun LazyGridState.isScrolledToTheEndAndNotScrollingState() =
    remember { derivedStateOf { isScrolledToTheEndAndNotScrolling() } }

@Composable
fun LazyGridState.lastVisibleIndexState() =
    remember { derivedStateOf { lastVisibleIndex() } }

@Composable
fun LazyGridState.firstVisibleIndexState() =
    remember { derivedStateOf { firstVisibleIndex() } }

@Composable
fun LazyGridState.visibleItemSizeState() =
    remember { derivedStateOf { visibleItemsSize() } }

@Composable
fun LazyGridState.totalItemsCountState() =
    remember { derivedStateOf { totalItemsCount() } }

@Composable
fun LazyGridState.viewportEndOffsetState() =
    remember { derivedStateOf { viewportEndOffset() } }


@Composable
fun LazyGridState.viewportStartOffsetState() =
    remember { derivedStateOf { viewportStartOffset() } }


