package com.crazylegend.setofusefulkotlinextensions.nav

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.crazylegend.common.ifTrue
import com.crazylegend.fragment.viewCoroutineScope
import com.crazylegend.internetdetector.InternetDetector
import com.crazylegend.lifecycle.repeatingJobOnStarted
import com.crazylegend.retrofit.retrofitResult.*
import com.crazylegend.retrofit.throwables.isNoConnectionException
import com.crazylegend.retrofit.viewstate.ViewEvent
import com.crazylegend.retrofit.viewstate.handleApiError
import com.crazylegend.retrofit.viewstate.isError
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
class TestFragment : Fragment(R.layout.fragment_test) {

    private val binding by viewBinding(FragmentTestBinding::bind)

    private val adapter by lazy {
        TestViewBindingAdapter()
    }
    private val internetDetector by lazy {
        InternetDetector(requireContext())
    }
    private val testAVM by viewModels<TestAVM>()

    private var errorSnackBar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = adapter

        repeatingJobOnStarted {
            testAVM.posts.collect { retrofitResult ->
                updateUIstate(retrofitResult)
            }
        }
        repeatingJobOnStarted {
            testAVM.viewEvent.collect { viewEvent ->
                handleViewEvents(viewEvent)
            }
        }

        binding.swipeToRefresh.setOnRefreshListener {
            binding.swipeToRefresh.setIsRefreshing()
            testAVM.getPosts()
            binding.swipeToRefresh.setIsNotRefreshing()
        }
        binding.test.setOnClickListenerCooldown {
            testAVM.getRandomPosts()
        }
    }

    private fun handleViewEvents(viewEvent: ViewEvent) {
        if (viewEvent.isError && testAVM.isDataLoaded) {
            showErrorSnack()
        }
    }


    private fun updateUIstate(retrofitResult: RetrofitResult<List<TestModel>>) {
        retrofitResult
                .onApiError { errorBody, _ ->
                    Toast.makeText(requireContext(), handleApiError(testAVM.savedStateHandle, errorBody), LENGTH_LONG).show()
                }
                .onError { retryOnInternetAvailable(it) }

        binding.text.isVisible = testAVM.isDataNotLoaded and (retrofitResult.isError || retrofitResult.isApiError)
        binding.centerBigLoading.isVisible = retrofitResult.isLoading and testAVM.isDataNotLoaded
        binding.progress.isVisible = retrofitResult.isLoading and testAVM.isDataLoaded
        adapter.submitList(testAVM.payload)
    }

    private fun showErrorSnack() {
        errorSnackBar = Snackbar.make(requireView(), "Error has occurred", Snackbar.LENGTH_LONG)
        errorSnackBar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        errorSnackBar = null
    }

    private fun retryOnInternetAvailable(throwable: Throwable) {
        throwable.isNoConnectionException
                .ifTrue {
                    viewCoroutineScope.launch { observeInternetConnectivity() }
                }
    }

    private suspend fun observeInternetConnectivity() {
        internetDetector.state.collect { hasConnection ->
            if (hasConnection) {
                testAVM.getPosts()
            }
        }
    }

}