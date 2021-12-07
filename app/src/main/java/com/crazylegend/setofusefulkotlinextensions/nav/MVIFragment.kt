package com.crazylegend.setofusefulkotlinextensions.nav

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.crazylegend.common.ifTrue
import com.crazylegend.fragment.viewCoroutineScope
import com.crazylegend.internetdetector.InternetDetector
import com.crazylegend.lifecycle.repeatingJobOnStarted
import com.crazylegend.retrofit.apiresult.*
import com.crazylegend.retrofit.throwables.isNoConnectionException
import com.crazylegend.retrofit.viewstate.*
import com.crazylegend.setofusefulkotlinextensions.R
import com.crazylegend.setofusefulkotlinextensions.TestAVM
import com.crazylegend.setofusefulkotlinextensions.adapter.TestModel
import com.crazylegend.setofusefulkotlinextensions.adapter.TestViewBindingAdapter
import com.crazylegend.setofusefulkotlinextensions.databinding.FragmentTestBinding
import com.crazylegend.view.setIsNotRefreshing
import com.crazylegend.view.setIsRefreshing
import com.crazylegend.view.setOnClickListenerCooldown
import com.crazylegend.viewbinding.viewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Hristijan, date 2/15/21
 */
class MVIFragment : Fragment(R.layout.fragment_test) {

    private val binding by viewBinding(FragmentTestBinding::bind)

    private val adapter by lazy { TestViewBindingAdapter() }
    private val internetDetector by lazy { InternetDetector(requireContext()) }
    private val testAVM by viewModels<TestAVM>()

    private var snackBar: Snackbar? = null
    private var toast: Toast? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = adapter

        repeatingJobOnStarted { testAVM.posts.collect { retrofitResult -> updateUIState(retrofitResult) } }
        repeatingJobOnStarted { testAVM.viewEvent.collect { viewEvent -> handleViewEvents(viewEvent) } }

        binding.swipeToRefresh.setOnRefreshListener {
            binding.swipeToRefresh.setIsRefreshing()
            testAVM.getPosts()
        }
        binding.staticPosts.setOnClickListenerCooldown {
            testAVM.getRandomPosts()
        }
    }

    private fun handleViewEvents(viewEvent: ViewEvent) {
        viewEvent.isError.and(testAVM.isDataLoaded).ifTrue(::showErrorSnack)
        viewEvent.isApiError.ifTrue {
            toast?.cancel()
            toast = Toast.makeText(requireContext(), handleApiError(testAVM.savedStateHandle, viewEvent.asApiErrorBody), LENGTH_LONG)
            toast?.show()
        }
    }


    private fun updateUIState(apiResult: ApiResult<List<TestModel>>) {
        apiResult.getAsThrowable?.let { throwable -> throwable.isNoConnectionException.ifTrue { retryOnInternetAvailable(throwable) } }

        !apiResult.isLoading.ifTrue { binding.swipeToRefresh.setIsNotRefreshing() }
        binding.error.isVisible = testAVM.isDataNotLoaded and (apiResult.isError || apiResult.isApiError)
        binding.centerBigLoading.isVisible = apiResult.isLoading and testAVM.isDataNotLoaded
        binding.progress.isVisible = apiResult.isLoading and testAVM.isDataLoaded
        adapter.submitList(testAVM.payload)
    }

    private fun showErrorSnack() {
        snackBar?.dismiss()
        snackBar = Snackbar.make(requireView(), "Error has occurred", Snackbar.LENGTH_LONG)
        snackBar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snackBar = null
        toast = null
    }

    private fun retryOnInternetAvailable(throwable: Throwable) {
        throwable.isNoConnectionException.ifTrue { viewCoroutineScope.launch { observeInternetConnectivity() } }
    }

    private suspend fun observeInternetConnectivity() {
        internetDetector.state.collect { hasConnection -> hasConnection.ifTrue { testAVM.getPosts() } }
    }

}