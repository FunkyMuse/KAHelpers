package com.crazylegend.retrofit

import com.crazylegend.retrofit.apiresult.*
import com.crazylegend.retrofit.retrofitResult.*
import com.crazylegend.retrofit.throwables.NoConnectionException
import com.crazylegend.retrofit.throwables.isNoConnectionException
import com.crazylegend.retrofit.viewstate.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Created by funkymuse on 11/20/21 to long live and prosper !
 */
@ExperimentalCoroutinesApi
class ViewStateTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    @Test
    fun `view state payload once`() = mainCoroutineRule.runBlockingTest {

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
    fun `view state payload event`() = mainCoroutineRule.runBlockingTest {

        val testList = listOf("")
        val retrofitResult = ApiResult.Success(testList)
        val viewState = ViewState<List<String>>()
        retrofitResult.asViewStatePayloadWithEvents(viewState)


        val firstItem = viewState.viewEvent.first()
        val firstState = viewState.data.first()
        assertTrue(firstState.isSuccess)
        assertTrue(firstItem.isSuccess)

        ApiResult.Loading.asViewStatePayloadWithEvents(viewState)

        val firstItemSecondTime = viewState.viewEvent.first()
        val firstStateSecondTime = viewState.data.first()
        assertTrue(firstStateSecondTime.isLoading)
        assertTrue(firstItemSecondTime.isLoading)

        ApiResult.Error(NoConnectionException()).asViewStatePayloadWithEvents(viewState)

        val firstItemThirdTime = viewState.viewEvent.first()
        val firstStateThirdTime = viewState.data.first()
        assertTrue(firstStateThirdTime.isError)
        assertTrue(firstItemThirdTime.isError)
        assertTrue(viewState.payload != null)
        assertTrue(!firstStateThirdTime.isSuccess)
        assertTrue(firstStateThirdTime.asError().throwable.isNoConnectionException)
    }

}