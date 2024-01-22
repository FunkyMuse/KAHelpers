package dev.funkymuse.compose.core

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import dev.funkymuse.compose.core.lazylist.firstVisibleIndex
import dev.funkymuse.compose.core.lazylist.isScrolledToTheEndAndNotScrolling
import dev.funkymuse.compose.core.lazylist.lastVisibleIndex
import dev.funkymuse.compose.core.lazylist.totalItemsCount
import dev.funkymuse.compose.core.lazylist.viewportEndOffset
import dev.funkymuse.compose.core.lazylist.viewportStartOffset
import dev.funkymuse.compose.core.lazylist.visibleItemsSize
import dev.funkymuse.compose.core.utils.generateRandomIntegerList
import dev.funkymuse.compose.core.utils.setContentWithTestViewConfiguration
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test


internal class LazyListExtensionsKtTest {


    private val LazyListTag = "LazyListTag"

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun hasScrolledToTheEnd() {
        contentWithListAndState { list, state ->
            scrollToEnd(state, list)
            assert(state.lastVisibleIndex() != 0)
            assert(state.lastVisibleIndex() == list.lastIndex)
        }
    }

    @Test
    fun firstVisibleIndex() {
        contentWithListAndState { _, state ->
            assert(state.firstVisibleIndex() != null)
            assert(state.firstVisibleIndex() == 0)
        }
    }

    @Test
    fun hasScrolledToTheEndAndNotScrolling() {
        contentWithListAndState { list, state ->
            scrollToEnd(state, list)
            assert(state.lastVisibleIndex() != 0)
            assert(state.lastVisibleIndex() == list.lastIndex)
            assert(state.isScrolledToTheEndAndNotScrolling())
        }
    }

    @Test
    fun lastVisibleIndexCheck() {
        contentWithListAndState { list, state ->
            scrollToEnd(state, list)
            assert(state.lastVisibleIndex() != 0)
            assert(state.lastVisibleIndex() == list.lastIndex)
        }
    }

    @Test
    fun visibleItemsSizeCheck() {
        contentWithListAndState { list, state ->
            assert(list.isNotEmpty())
            assert(state.visibleItemsSize() != list.size)
        }
    }

    @Test
    fun totalItemsCountCheck() {
        contentWithListAndState { list, state ->
            assert(list.isNotEmpty())
            assert(state.totalItemsCount() == list.count())
        }
    }

    @Test
    fun viewPortOffsetStartCheck() {
        contentWithListAndState { _, state ->
            assert(state.viewportStartOffset() == state.layoutInfo.viewportStartOffset)
        }
    }

    @Test
    fun viewPortOffsetEndCheck() {
        contentWithListAndState { _, state ->
            assert(state.viewportEndOffset() == state.layoutInfo.viewportEndOffset)
        }
    }

    private fun scrollToEnd(state: LazyListState, list: List<Int>){
        runBlocking {
            state.scrollToItem(list.lastIndex)
        }
    }

    private fun contentWithListAndState(assertions: (list: List<Int>, state: LazyListState) -> Unit) {
        lateinit var state: LazyListState
        val list = generateRandomIntegerList(15)

        rule.setContentWithTestViewConfiguration {
            state = rememberLazyListState()
            LazyColumn(
                Modifier
                    .requiredSize(100.dp)
                    .testTag(LazyListTag),
                state = state
            ) {
                items(list) {
                    Spacer(
                        Modifier
                            .requiredSize(20.dp)
                            .testTag("$it"))
                }
            }
        }

        rule.runOnIdle {

            assertions(list, state)
        }
    }

}