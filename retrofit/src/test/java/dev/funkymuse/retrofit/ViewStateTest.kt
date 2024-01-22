package dev.funkymuse.retrofit

import dev.funkymuse.retrofit.apiresult.ApiResult
import dev.funkymuse.retrofit.throwables.NoConnectionException
import dev.funkymuse.retrofit.throwables.isNoConnectionException
import dev.funkymuse.retrofit.viewstate.event.asError
import dev.funkymuse.retrofit.viewstate.event.isError
import dev.funkymuse.retrofit.viewstate.event.isLoading
import dev.funkymuse.retrofit.viewstate.event.isSuccess
import dev.funkymuse.retrofit.viewstate.state.ViewState
import dev.funkymuse.retrofit.viewstate.state.asViewStatePayload
import dev.funkymuse.retrofit.viewstate.state.asViewStatePayloadWithEvents
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


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