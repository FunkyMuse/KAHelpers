package dev.funkymuse.compose.core.lazy_staggered_grid

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember

/**
 * Created by funkymuse on 8/4/21 to long live and prosper !
 */

@Composable
fun LazyStaggeredGridState.isScrolledToTheEndState() =
    remember { derivedStateOf { isScrolledToTheEnd() } }

@Composable
fun LazyStaggeredGridState.isScrolledToTheEndAndNotScrollingState() =
    remember { derivedStateOf { isScrolledToTheEndAndNotScrolling() } }

@Composable
fun LazyStaggeredGridState.lastVisibleIndexState() =
    remember { derivedStateOf { lastVisibleIndex() } }

@Composable
fun LazyStaggeredGridState.firstVisibleIndexState() =
    remember { derivedStateOf { firstVisibleIndex() } }

@Composable
fun LazyStaggeredGridState.visibleItemSizeState() =
    remember { derivedStateOf { visibleItemsSize() } }

@Composable
fun LazyStaggeredGridState.totalItemsCountState() =
    remember { derivedStateOf { totalItemsCount() } }


