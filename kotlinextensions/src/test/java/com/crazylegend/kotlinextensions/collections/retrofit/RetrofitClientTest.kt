package com.crazylegend.kotlinextensions.collections.retrofit

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.crazylegend.kotlinextensions.retrofit.RetrofitClient
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import retrofit2.create

/**
 * Created by crazy on 6/15/20 to long live and prosper !
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class RetrofitClientTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private var retrofit: FakeInterface? = null

    @Before
    fun setupRetrofitInterface() {
        retrofit = RetrofitClient.moshiInstanceCoroutines(ApplicationProvider.getApplicationContext(), "https://www.facebook.com/", true).create()
    }

    @Test
    fun getRetrofitInstance() {
        assert(retrofit!=null)
    }
}