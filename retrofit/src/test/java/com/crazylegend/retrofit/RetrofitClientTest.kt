package com.crazylegend.retrofit

import androidx.test.ext.junit.runners.AndroidJUnit4
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
        retrofit = RetrofitClient.moshiInstanceCoroutines("https://www.facebook.com/").create()
        assert(retrofit != null)
    }
}