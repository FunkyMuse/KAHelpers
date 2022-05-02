package com.crazylegend.retrofit

import com.crazylegend.retrofit.apiresult.*
import com.crazylegend.retrofit.throwables.NoConnectionException
import com.crazylegend.retrofit.throwables.isNoConnectionException
import com.crazylegend.retrofit.viewstate.event.asError
import com.crazylegend.retrofit.viewstate.event.isError
import com.crazylegend.retrofit.viewstate.event.isLoading
import com.crazylegend.retrofit.viewstate.event.isSuccess
import com.crazylegend.retrofit.viewstate.state.ViewState
import com.crazylegend.retrofit.viewstate.state.asViewStatePayload
import com.crazylegend.retrofit.viewstate.state.asViewStatePayloadWithEvents
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Created by funkymuse on 11/20/21 to long live and prosper !
 */
@ExperimentalCoroutinesApi
class ViewStateTest {

    private val testScope = TestScope()


    @Test
    fun `view state payload once`() = testScope.runTest {

        val retrofitResult = ApiResult.Success(listOf(""))
        val viewState = ViewState<List<String>>()

        retrofitResult.asViewStatePayloadWithEvents(viewState)

        assert(viewState.isDataLoaded)
        assert(viewState.payload != null)
        assert(!viewState.isDataNotLoaded)
    }


    @Test
    fun `view state payload change`() {

        val testList = listOf("")
        val retrofitResult = ApiResult.Success(testList)
        val viewState = ViewState<List<String>>()

        assert(viewState.payload == null)
        assert(viewState.isDataNotLoaded)
        assertEquals("Payload should be null", viewState.payload, null)

        retrofitResult.asViewStatePayload(viewState)

        assert(viewState.payload != null)
        assert(viewState.isDataLoaded)
        assertEquals("Payload not the same", viewState.payload, testList)

        ApiResult.Loading.asViewStatePayload(viewState)

        val newList = listOf("Test", "Test1")
        val retrofitResultNew = ApiResult.Success(newList)
        retrofitResultNew.asViewStatePayload(viewState)

        assertEquals("Payload not the same", newList, viewState.payload)
        assert(viewState.payload != null)
        assert(viewState.isDataLoaded)
        assertEquals("Payload not the same", viewState.payload, newList)

    }

    @Test
    fun `view state payload event`() = testScope.runTest {

        val testList = listOf("")
        val retrofitResult = ApiResult.Success(testList)
        val viewEvent = FakeViewEvent()
        val viewState = ViewState<List<String>>(viewEvent)
        retrofitResult.asViewStatePayloadWithEvents(viewState)


        val firstItem = viewEvent.event.first()
        val firstState = viewState.viewState.first()
        assertTrue(firstState.isSuccess)
        assertTrue(firstItem.isSuccess)

        ApiResult.Loading.asViewStatePayloadWithEvents(viewState)

        val firstItemSecondTime = viewEvent.event.first()
        val firstStateSecondTime = viewState.viewState.first()
        assertTrue(firstStateSecondTime.isLoading)
        assertTrue(firstItemSecondTime.isLoading)

        ApiResult.Error(NoConnectionException()).asViewStatePayloadWithEvents(viewState)

        val firstItemThirdTime = viewEvent.event.first()
        val firstStateThirdTime = viewState.viewState.first()
        assertTrue(firstStateThirdTime.isError)
        assertTrue(firstItemThirdTime.isError)
        assertTrue(viewState.payload != null)
        assertTrue(!firstStateThirdTime.isSuccess)
        assertTrue(firstStateThirdTime.asError().throwable.isNoConnectionException)
    }

}