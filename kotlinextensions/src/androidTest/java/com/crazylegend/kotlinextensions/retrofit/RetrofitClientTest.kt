package com.crazylegend.kotlinextensions.retrofit

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.create

/**
 * Created by crazy on 6/15/20 to long live and prosper !
 */
@RunWith(AndroidJUnit4::class)
class RetrofitClientTest {

    private var retrofit: FakeInterface? = null

    @Test
    fun getRetrofitInstance() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        retrofit = RetrofitClient.moshiInstanceCoroutines(appContext, "https://www.facebook.com/", true).create()
        assert(retrofit!=null)
    }
}